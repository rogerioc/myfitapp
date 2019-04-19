package com.rogerio.myfitapp.detailgoal

import androidx.databinding.ObservableField
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.result.DataReadResponse
import com.rogerio.myfitapp.core.BaseViewModel
import com.rogerio.myfitapp.core.addTo
import com.rogerio.myfitapp.detailgoal.model.DetailViewEntity
import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import com.rogerio.myfitapp.services.repository.FitDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.text.DateFormat.getTimeInstance
import java.util.concurrent.TimeUnit

class DetailGoalViewModel(
        private val interactor: FitDetailDataInteractor
) : BaseViewModel() {

    val model = ObservableField<DetailViewEntity>()
    private val LOG_TAG = "DetailGoalViewModel"

    init {
        Timber.tag(LOG_TAG)
        setNewData(DetailViewEntity(
                steps = "0"
        ))

    }

    private var fitLists: List<FitItemViewEntity>? = emptyList()

    fun start() {
        interactor.getFitData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setDataPoint(it)
                }
                        , {

                }).addTo(this)

        interactor.getSensonData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setHistorics(it)
                }, {

                }).addTo(this)

    }


    private fun setNewData(detailViewEntity: DetailViewEntity) {
        model.set(detailViewEntity)
    }

    private fun setHistoric(dataSet: DataSet) {
        Timber.i("----- Historic --------")
        Timber.i("Data returned for Data type: %s", dataSet.getDataType().getName())
        val dateFormat = getTimeInstance()

        for (dp in dataSet.getDataPoints()) {
            Timber.i("Data point:")
            Timber.i("\tType: " + dp.getDataType().getName())
            Timber.i("\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)))
            Timber.i("\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)))
            for (field in dp.getDataType().getFields()) {
                Timber.i("\tField: " + field.getName() + " Value: " + dp.getValue(field))
            }
        }

    }

    private fun setHistorics(it: DataReadResponse) {
        var dataSet = it.buckets
        dataSet?.let {
            it.forEach {
                it.getDataSet(DataType.AGGREGATE_STEP_COUNT_DELTA)?.let { it1 -> setHistoric(it1) }
            }
        }

    }


    private fun setDataPoint(dataPoint: DataPoint?) {
        dataPoint?.let {
            var totalSteps = 0
            for (field in it.getDataType().getFields()) {
                val steps = it.getValue(field).asInt()
                totalSteps += steps
            }

            setNewData(DetailViewEntity(
                    steps = totalSteps.toString()
            ))
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
}