package com.rogerio.myfitapp.presentation

import com.rogerio.myfitapp.helpers.ItemsToFitItemsViewEntityMapper
import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import com.rogerio.myfitapp.services.repository.FitDataSource
import io.reactivex.Single

class FitInteractor(
        private val repository: FitDataSource
) {
    fun getFitsList(): Single<List<FitItemViewEntity>> =
            repository.getFitList()
                    .map(ItemsToFitItemsViewEntityMapper())

}
