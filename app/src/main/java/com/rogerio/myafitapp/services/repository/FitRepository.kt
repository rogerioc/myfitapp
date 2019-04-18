package com.rogerio.myafitapp.services.repository

import com.rogerio.myafitapp.services.FitApi
import com.rogerio.myafitapp.services.models.Fit
import io.reactivex.Single

class FitRepository(
        private val service: FitApi
) : FitDataSource {
    override fun getFitList(): Single<Fit> = service.getFitList()
}