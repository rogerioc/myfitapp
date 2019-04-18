package com.rogerio.myafitapp.presentation.model

import com.rogerio.myafitapp.services.models.Reward

data class FitItemViewEntity(
        val reward: Reward,
        val goal: Int = 0,
        val description: String = "",
        val id: String = "",
        val title: String = "",
        val type: String = ""
)