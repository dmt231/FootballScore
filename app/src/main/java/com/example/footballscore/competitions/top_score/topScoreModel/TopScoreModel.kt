package com.example.footballscore.competitions.top_score.topScoreModel

data class TopScoreModel(
    val competition: Competition,
    val count: Int,
    val filters: Filters,
    val scorers: List<Scorer>,
    val season: Season
)