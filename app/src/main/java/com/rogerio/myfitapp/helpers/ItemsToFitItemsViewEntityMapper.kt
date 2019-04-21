package com.rogerio.myfitapp.helpers

import com.rogerio.myfitapp.presentation.model.FitItemViewEntity
import com.rogerio.myfitapp.presentation.model.RewardViewEntity
import com.rogerio.myfitapp.services.models.Fit
import com.rogerio.myfitapp.services.models.ItemsItem
import com.rogerio.myfitapp.services.models.Reward
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
        reward = rewardToRewardViewEntity(item.reward),
        title = item.title,
        type = item.type
    )

    fun rewardToRewardViewEntity(reward: Reward) = RewardViewEntity(
        trophy = reward.trophy,
        points = reward.points
    )
}
