package com.rogerio.myfitapp.detailgoal

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.result.DataReadResponse
import com.rogerio.myfitapp.R
import com.rogerio.myfitapp.core.BaseViewModel
import com.rogerio.myfitapp.core.addTo
import com.rogerio.myfitapp.detailgoal.model.DetailViewEntity
import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.text.DateFormat.getTimeInstance
import java.util.concurrent.TimeUnit

class DetailGoalViewModel(
        private val interactor: FitDetailDataInteractor
) : BaseViewModel() {

    val model = ObservableField<DetailViewEntity>()
    private val LOG_TAG = "DetailGoalViewModel"
    private val STEP_TYPE = "step"
    private val STEPS_NAME = "steps"
    private var totalSteps = 0
    private var fitItemViewEntity: FitItemViewEntity? = null
    private var isStarted = false

    val message = ObservableInt(R.string.goal_almost)
    val colorMessage = ObservableInt(R.color.colorWarning)
    val showMessage = ObservableBoolean(false)
    val value = ObservableInt(0)
    val valuePoints = ObservableInt(0)
    val showFeature = ObservableBoolean(false)

    init {
        Timber.tag(LOG_TAG)
        setNewData(DetailViewEntity(
                steps = "0"
        ))
    }


    fun start() {

        if (fitItemViewEntity?.type.equals(STEP_TYPE)) {
            showFeature.set(true)
            isStarted = true
            fetchData()
        } else {
            showFeature.set(false)
        }
    }

    private fun fetchData() {
        interactor.getHistoryData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setHistorics(it)
                }
                        , {
                    Timber.e("Get Data -> %s", it.toString())
                }).addTo(this)

        interactor.getSensonData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setDataPoint(it)
                }, {
                    Timber.e("Get Sensor -> %s", it.toString())
                }).addTo(this)

        interactor.day()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.i("------ Daily -------")
                    totalSteps += setHistoricAndGetSteps(it)
                    managerResult()
                }, {
                    Timber.e(it)
                })
    }

    private fun managerResult() {
        val goal = fitItemViewEntity?.goal ?: 0
        showMessage.set(false)
        val almostData = (totalSteps - (totalSteps / 3))
        if (totalSteps >= goal) {
            message.set(R.string.goal_ok)
            colorMessage.set(R.color.colorOk)
            showMessage.set(true)
            valuePoints.set(fitItemViewEntity?.reward?.points ?: 0)
            value.set(goal)
        } else if (almostData < goal) {
            message.set(R.string.goal_almost)
            colorMessage.set(R.color.colorWarning)
            value.set(goal - almostData)
        } else {
            message.set(R.string.goal_not)
            colorMessage.set(R.color.colorWrong)
            value.set(goal - totalSteps)
        }
        setNewStep()
    }


    private fun setNewData(detailViewEntity: DetailViewEntity) {
        model.set(detailViewEntity)
    }

    private fun setHistoricAndGetSteps(dataSet: DataSet): Int {
        Timber.i("----- Historic --------")
        Timber.i("Data returned for Data type: %s", dataSet.getDataType().getName())
        val dateFormat = getTimeInstance()
        var steps = 0
        for (dp in dataSet.getDataPoints()) {
            Timber.i("Data point:")
            Timber.i("\tType: " + dp.getDataType().getName())
            Timber.i("\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)))
            Timber.i("\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)))
            for (field in dp.getDataType().getFields()) {
                Timber.i("\tField: " + field.getName() + " Value: " + dp.getValue(field))

                if (field.getName().equals(STEPS_NAME)) {
                    steps += dp.getValue(field).asInt()
                    setNewStep()
                }
            }
        }
        return steps
    }

    private fun setHistorics(it: DataReadResponse) {
        var dataSet = it.buckets
        dataSet?.let {
            it.forEach {
                it.getDataSet(DataType.AGGREGATE_STEP_COUNT_DELTA)?.let { it1 -> setHistoricAndGetSteps(it1) }
            }
        }

    }


    private fun setDataPoint(dataPoint: DataPoint?) {
        dataPoint?.let {
            for (field in it.getDataType().getFields()) {
                val steps = it.getValue(field).asInt()
                totalSteps += steps
            }

            setNewStep()
            Timber.i("----> %s", totalSteps)
            interactor.saveData(totalSteps)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Timber.v("SAVED DATA")
                    }, {
                        Timber.e("Erro save data -> %s", it.toString())
                    }).addTo(this)
        }
    }

    private fun setNewStep() {
        val detail = model.get()
        detail?.let {
            val new = it.copy(
                    steps = totalSteps.toString(),
                    description = fitItemViewEntity?.description ?: ""
            )
            setNewData(new)
        }
    }


    fun setFitItemViewEntity(fitItemViewEntity: FitItemViewEntity?) {
        this.fitItemViewEntity = fitItemViewEntity
        if (!isStarted) {
            start()
        }
    }
}