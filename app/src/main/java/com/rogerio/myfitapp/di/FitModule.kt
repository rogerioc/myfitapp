package com.rogerio.myfitapp.di

import com.rogerio.myfitapp.BuildConfig
import com.rogerio.myfitapp.detailgoal.DetailGoalViewModel
import com.rogerio.myfitapp.detailgoal.FitDetailDataInteractor
import com.rogerio.myfitapp.presentation.FitInteractor
import com.rogerio.myfitapp.presentation.MyFitViewModel
import com.rogerio.myfitapp.services.createService
import com.rogerio.myfitapp.services.repository.FitDataSource
import com.rogerio.myfitapp.services.repository.FitRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT = 60L
private const val WRITE_TIMEOUT = 60L
private const val READ_TIMEOUT = 60L

const val OK_HTTP_CLIENT = "okHttpClient"
const val MOSHI = "moshi"


val fitModule = module {
    single {
        createService(
            get(named(OK_HTTP_CLIENT)),
            get(named(MOSHI)),
            BuildConfig.BASE_URL
        )
    }

    single(named(MOSHI)) {
        getMoshi()
    }

    single(named(OK_HTTP_CLIENT)) {
        getOkHttpClient()
    }

    factory<FitDataSource> {
        FitRepository(get())
    }

    factory {
        FitInteractor(get())
    }

    factory {
        MyFitViewModel(get())
    }

    factory {
        FitDetailDataInteractor(androidContext())
    }

    factory {
        DetailGoalViewModel(get())
    }
}


private fun getMoshi(): Moshi {
    return Moshi.Builder()
        .build()
}

private fun getOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().apply {

        connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(
                logging
            )
        }

    }.build()
}