package com.rogerio.myfitapp.presentation.model

import android.os.Parcelable
import com.rogerio.myfitapp.services.models.Reward
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FitItemViewEntity(
        val reward: Reward,
        val goal: Int = 0,
        val description: String = "",
        val id: String = "",
        val title: String = "",
        val type: String = ""
): Parcelable