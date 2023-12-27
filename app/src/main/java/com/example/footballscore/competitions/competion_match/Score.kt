package com.example.footballscore.competitions.competion_match

data class Score(
    val duration: String,
    val fullTime: FullTime,
    val halfTime: HalfTime,
    val winner: Any
)