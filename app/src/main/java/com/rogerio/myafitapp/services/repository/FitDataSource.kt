package com.rogerio.myafitapp.services.repository

import com.rogerio.myafitapp.services.models.Fit
import io.reactivex.Single

interface FitDataSource {
    fun getFitList(): Single<Fit>
}