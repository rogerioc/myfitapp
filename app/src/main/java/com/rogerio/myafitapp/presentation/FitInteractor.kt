package com.rogerio.myafitapp.presentation

import com.rogerio.myafitapp.helpers.ItemsToFitItemsViewEntityMapper
import com.rogerio.myafitapp.presentation.model.FitItemViewEntity
import com.rogerio.myafitapp.services.repository.FitDataSource
import io.reactivex.Single

class FitInteractor(
        private val repository: FitDataSource
) {
    fun getFitsList(): Single<List<FitItemViewEntity>> =
            repository.getFitList()
                    .map(ItemsToFitItemsViewEntityMapper())

}
