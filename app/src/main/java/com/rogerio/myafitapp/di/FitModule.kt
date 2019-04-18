package com.rogerio.myafitapp.di

import com.rogerio.myafitapp.BuildConfig
import com.rogerio.myafitapp.presentation.FitInteractor
import com.rogerio.myafitapp.presentation.MyFitViewModel
import com.rogerio.myafitapp.services.createService
import com.rogerio.myafitapp.services.repository.FitDataSource
import com.rogerio.myafitapp.services.repository.FitRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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