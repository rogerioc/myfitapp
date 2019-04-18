package com.rogerio.myafitapp.helpers

import com.rogerio.myafitapp.presentation.model.FitItemViewEntity
import com.rogerio.myafitapp.services.models.Fit
import com.rogerio.myafitapp.services.models.ItemsItem
import io.reactivex.functions.Function

class ItemsToFitItemsViewEntityMapper : Function<Fit, List<FitItemViewEntity>> {

     override fun apply(t: Fit): List<FitItemViewEntity> = t.items.let {
        itemsToFitItemsViewEntity(it!!)
    }


    fun itemsToFitItemsViewEntity(items: List<ItemsItem>) = items.map(::itemToFitItemViewEntity)

    fun itemToFitItemViewEntity(item: ItemsItem) = FitItemViewEntity(
        description = item.description,
        goal = item.goal,
        id = item.id,
        reward = item.reward,
        title = item.title,
        type = item.type
    )
}
