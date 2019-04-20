package com.rogerio.myfitapp.di

import android.app.Application
import com.nhaarman.mockitokotlin2.mock
import com.rogerio.myfitapp.detailgoal.DetailGoalViewModel
import com.rogerio.myfitapp.detailgoal.FitDetailDataInteractor
import com.rogerio.myfitapp.presentation.FitInteractor
import com.rogerio.myfitapp.presentation.MyFitViewModel
import com.rogerio.myfitapp.services.FitApi
import com.rogerio.myfitapp.services.repository.FitDataSource
import com.rogerio.myfitapp.services.repository.FitRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FitModuleTests : KoinTest {

    @Before
    fun setUp() {
        val app = mock<Application>()
        startKoin {
            androidContext(app)
            modules(fitModule)
        }

        //declareMock<androidContext()>()
    }

    @Test
    fun shouldCreateService() {
        val service: FitApi = get()
        assertNotNull(service)
    }

    @Test
    fun shouldCreateMoshi() {
        val moshi: Moshi = get(named(MOSHI))
        assertNotNull(moshi)
    }

    @Test
    fun shouleCreateOkHttp() {
        val okHttp: OkHttpClient = get(named(OK_HTTP_CLIENT))
        assertNotNull(okHttp)
    }

    @Test
    fun shouldCreateRepository() {
        val repository: FitDataSource = get()
        assertNotNull(repository)
    }

    @Test
    fun shouldCreateMyFitViewModel() {
        val viewModel: MyFitViewModel = get()
        assertNotNull(viewModel)
    }

//    @Test
//    fun shouldCreateDetailGoalViewModel() {
//        val  viewModel: DetailGoalViewModel = get()
//        assertNotNull(viewModel)
//    }

    @Test
    fun shouldInteractors() {
        val fitInteractor: FitInteractor = get()
        //val fitDetailInteractor: FitDetailDataInteractor = get()

        assertNotNull(fitInteractor)
        //assertNotNull(fitDetailInteractor)
    }

    @After
    fun stop(){
        stopKoin()
    }
}