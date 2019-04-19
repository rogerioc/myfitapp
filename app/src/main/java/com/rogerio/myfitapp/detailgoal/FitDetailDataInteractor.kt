package com.rogerio.myfitapp.detailgoal

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.rogerio.myfitapp.helpers.ItemsToFitItemsViewEntityMapper
import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import com.rogerio.myfitapp.services.repository.FitDataSource
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


class FitDetailDataInteractor(
        private val context: Context,
        private val repository: FitDataSource
) {
    private val NAME_DATA = "Step Count"
    private var gs: GoogleSignInAccount?

    init {
        gs = GoogleSignIn.getLastSignedInAccount(context)
    }

    fun getFitsList(): Single<List<FitItemViewEntity>> =
            repository.getFitList()
                    .map(ItemsToFitItemsViewEntityMapper())

    fun getFitData(): PublishSubject<DataPoint> {
        val setDataPoint = PublishSubject.create<DataPoint>()
        gs?.let {
            val myStepCountListener = OnDataPointListener {
                setDataPoint.onNext(it)
            }
            Fitness.getSensorsClient(context, it)
                    .add(
                            SensorRequest.Builder()
                                    .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                    .setSamplingRate(10, TimeUnit.SECONDS)  // sample once per minute
                                    .build(),
                            myStepCountListener!!
                    )
        }
        return setDataPoint
    }

    fun getSensonData(): PublishSubject<DataReadResponse> {
        val setDataHistory = PublishSubject.create<DataReadResponse>()
        val cal = Calendar.getInstance()
        cal.setTime(Date())
        val endTime = cal.getTimeInMillis()
        cal.add(Calendar.YEAR, -1)
        val startTime = cal.getTimeInMillis()
        val readRequest = DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                //.read(DataType.TYPE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.HOURS)
                .build()

        gs?.let {
            Fitness.getHistoryClient(context, it)
                    .readData(readRequest)
                    .addOnSuccessListener(OnSuccessListener<DataReadResponse> {
                        Timber.d("onSuccess -> %s", it.toString())
                        //viewModel.setHistorics(it)
                        setDataHistory.onNext(it)
                    })
                    .addOnFailureListener(OnFailureListener { e ->
                        //viewModel.setHistoryFailed(e) */
                        setDataHistory.onError(e)
                    })
                    .addOnCompleteListener(OnCompleteListener<DataReadResponse> {
                        //viewModel.setHistoricsCompleted()
                        setDataHistory.onComplete()
                    })

        }
        return setDataHistory
    }


    fun saveData(stepCountDelta: Int): PublishSubject<Void> {
        val cal = Calendar.getInstance()
        val now = Date()
        cal.time = now
        val endTime = cal.timeInMillis
        cal.add(Calendar.HOUR_OF_DAY, -1)
        val startTime = cal.timeInMillis
        val setDataPoint = PublishSubject.create<Void>()

        val dataSource = DataSource.Builder()
                .setAppPackageName(context)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName(NAME_DATA)
                .setType(DataSource.TYPE_RAW)
                .build()

        val dataSet = DataSet.create(dataSource)

        val point = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
        point.getValue(Field.FIELD_STEPS).setInt(stepCountDelta)
        dataSet.add(point)

        gs?.let {
            Fitness.getHistoryClient(context, it).insertData(dataSet).addOnSuccessListener {
                setDataPoint.onComplete()
            }.addOnFailureListener {
                setDataPoint.onError(it)
            }
        }
        return setDataPoint
    }
}