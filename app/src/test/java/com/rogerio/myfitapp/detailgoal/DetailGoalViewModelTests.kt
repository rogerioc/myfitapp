package com.rogerio.myfitapp.detailgoal

import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.result.DataReadResponse
import com.nhaarman.mockitokotlin2.whenever
import com.rogerio.myfitapp.helpersTest.RxImmediateSchedulerRule
import io.reactivex.subjects.PublishSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailGoalViewModelTests {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var interactor: FitDetailDataInteractor

    private lateinit var viewModel: DetailGoalViewModel

    private lateinit var setDataPoint: PublishSubject<DataPoint>

    private lateinit var setDaily: PublishSubject<DataSet>

    private lateinit var setDataHistory: PublishSubject<DataReadResponse>

    @Before
    fun setUp() {
        viewModel = DetailGoalViewModel(interactor)
        setDaily = PublishSubject.create<DataSet>()
        setDataHistory = PublishSubject.create<DataReadResponse>()
        setDataPoint = PublishSubject.create<DataPoint>()

        whenever(interactor.getHistoryData()).thenReturn(
            setDataHistory
        )

        whenever(interactor.getSensonData()).thenReturn(
            setDataPoint
        )

        whenever(interactor.day()).thenReturn(
            setDaily
        )
    }

    @Test
    fun shouldStartShowFeatureNotImplemented() {
        Assert.assertFalse(viewModel.showFeature.get())
    }

    @Test
    fun shouldNullShowFeatureNotImplemented() {
        viewModel.setFitItemViewEntity(null)
        Assert.assertFalse(viewModel.showFeature.get())
    }

    @Test
    fun shouldStart() {
        var fitItemViewEntity = getFitItemViewEntity()
        fitItemViewEntity = fitItemViewEntity.copy(type = "step")
        viewModel.setFitItemViewEntity(fitItemViewEntity)
        Assert.assertTrue(viewModel.showFeature.get())
    }

    @Test
    fun shouldGoalOK() {
        var fitItemViewEntity = getFitItemViewEntity()
        fitItemViewEntity = fitItemViewEntity.copy(type = "step",
            goal = 0
        )
        viewModel.setFitItemViewEntity(fitItemViewEntity)
        setDaily.onNext(getDataSet())
        Assert.assertFalse(viewModel.showMessage.get())
    }

}