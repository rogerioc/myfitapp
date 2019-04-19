package com.rogerio.myfitapp.services.repository

import com.rogerio.myfitapp.services.FitApi
import com.rogerio.myfitapp.services.models.Fit
import io.reactivex.Single

class FitRepository(
        private val service: FitApi
) : FitDataSource {
    override fun getFitList(): Single<Fit> = service.getFitList()
}