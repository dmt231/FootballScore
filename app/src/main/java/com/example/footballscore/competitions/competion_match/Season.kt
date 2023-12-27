package com.example.footballscore.competitions.competion_match

data class Season(
    val currentMatchday: Int,
    val endDate: String,
    val id: Int,
    val startDate: String,
    val winner: Any
)