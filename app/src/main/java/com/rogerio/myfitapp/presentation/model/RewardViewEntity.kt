package com.rogerio.myfitapp.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RewardViewEntity(
    val trophy: String = "",
    val points: Int = 0
) : Parcelable
