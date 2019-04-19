package com.rogerio.myfitapp.services.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Reward(@Json(name = "trophy")
                  val trophy: String = "",
                  @Json(name = "points")
                  val points: Int = 0): Parcelable