package com.rogerio.myfitapp.detailgoal.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailViewEntity(
        val steps: String
) : Parcelable
