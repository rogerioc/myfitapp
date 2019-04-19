package com.rogerio.myfitapp.services.repository

import com.rogerio.myfitapp.services.models.Fit
import io.reactivex.Single

interface FitDataSource {
    fun getFitList(): Single<Fit>
}