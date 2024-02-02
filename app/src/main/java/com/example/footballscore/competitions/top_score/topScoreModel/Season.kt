package com.example.footballscore.competitions.top_score.topScoreModel

data class Season(
    val currentMatchday: Int,
    val endDate: String,
    val id: Int,
    val startDate: String,
    val winner: Any
)