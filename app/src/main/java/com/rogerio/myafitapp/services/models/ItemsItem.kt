package com.rogerio.myafitapp.services.models

import com.squareup.moshi.Json

data class ItemsItem(@Json(name = "reward")
                     val reward: Reward,
                     @Json(name = "goal")
                     val goal: Int = 0,
                     @Json(name = "description")
                     val description: String = "",
                     @Json(name = "id")
                     val id: String = "",
                     @Json(name = "title")
                     val title: String = "",
                     @Json(name = "type")
                     val type: String = "")