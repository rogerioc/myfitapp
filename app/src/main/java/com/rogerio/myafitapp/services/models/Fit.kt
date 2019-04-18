package com.rogerio.myafitapp.services.models

import com.squareup.moshi.Json

data class Fit(@Json(name = "nextPageToken")
               val nextPageToken: String = "",
               @Json(name = "items")
               val items: List<ItemsItem>?)