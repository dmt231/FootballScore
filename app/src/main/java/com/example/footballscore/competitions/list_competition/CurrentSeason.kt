package com.example.footballscore.competitions.list_competition

data class CurrentSeason(
    val currentMatchday: Int,
    val endDate: String,
    val id: Int,
    val startDate: String,
    val winner: Winner
)