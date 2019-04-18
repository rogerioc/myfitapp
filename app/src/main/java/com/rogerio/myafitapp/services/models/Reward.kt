package com.rogerio.myafitapp.services.models

import com.squareup.moshi.Json

data class Reward(@Json(name = "trophy")
                  val trophy: String = "",
                  @Json(name = "points")
                  val points: Int = 0)