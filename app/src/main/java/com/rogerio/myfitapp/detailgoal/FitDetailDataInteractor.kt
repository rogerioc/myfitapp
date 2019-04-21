package com.rogerio.myfitapp.detailgoal

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataUpdateRequest
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
        private val context: Context
) {
    private val NAME_DATA = "Step Count"
    //private var googleSignInAccount: GoogleSignInAccount?
    private var hasData: Boolean = false

    private var googleSignInAccount: GoogleSignInAccount?

    init {
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context)
        Timber.tag("FitDetailDataInteractor")
    }


    fun getSensonData(): PublishSubject<DataPoint> {
        val setDataPoint = PublishSubject.create<DataPoint>()
        googleSignInAccount?.let {
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

    fun day(): PublishSubject<DataSet> {
        val setDaily = PublishSubject.create<DataSet>()
        googleSignInAccount?.let {
            Fitness.getHistoryClient(context, it)
                    .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                    .addOnSuccessListener {
                        setDaily.onNext(it)
                    }.addOnFailureListener {
                        setDaily.onError(it)
                    }.addOnCompleteListener {
                        setDaily.onComplete()
                    }
        } ?: run {
            setDaily.onComplete()
        }
        return setDaily
    }

    fun getHistoryData(): PublishSubject<DataReadResponse> {
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
                .bucketByActivityType(1, TimeUnit.SECONDS)
                .build()

        googleSignInAccount?.let {
            Fitness.getHistoryClient(context, it)
                    .readData(readRequest)
                    .addOnSuccessListener(OnSuccessListener<DataReadResponse> {
                        Timber.d("onSuccess -> %s", it.toString())
                        hasData = true
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
        } ?: run {
            setDataHistory.onComplete()
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
        if (!hasData)
            insertData(setDataPoint, dataSet)
        else
            updatetData(setDataPoint, dataSet, startTime, endTime)

        return setDataPoint
    }

    private fun updatetData(setDataPoint: PublishSubject<Void>, dataSet: DataSet, startTime: Long, endTime: Long) {
        val request = DataUpdateRequest.Builder()
                .setDataSet(dataSet)
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .build()

        googleSignInAccount?.let {
            Fitness.getHistoryClient(context, it).updateData(request).addOnSuccessListener {
                setDataPoint.onComplete()
            }.addOnFailureListener {
                setDataPoint.onError(it)
            }.addOnCanceledListener {
                setDataPoint.onError(Exception("Cancellled"))
            }.addOnCompleteListener {
                Timber.i("Save data Completable ")
            }

        } ?: run {
            setDataPoint.onComplete()
        }

    }

    private fun insertData(setDataPoint: PublishSubject<Void>, dataSet: DataSet) {

        googleSignInAccount?.let {
            Fitness.getHistoryClient(context, it).insertData(dataSet).addOnSuccessListener {
                setDataPoint.onComplete()
            }.addOnFailureListener {
                setDataPoint.onError(it)
            }.addOnCanceledListener {
                setDataPoint.onError(Exception("Cancellled"))
            }.addOnCompleteListener {
                Timber.i("Save data Completable ")
            }

        } ?: run {
            setDataPoint.onComplete()
        }
    }
}