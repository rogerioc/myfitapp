package com.rogerio.myafitapp.services

import com.rogerio.myafitapp.services.models.Fit
import io.reactivex.Single
import retrofit2.http.GET

interface FitApi {
    @GET("/_ah/api/myApi/v1/goals")
    fun getFitList(): Single<Fit>
}