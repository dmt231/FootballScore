package com.example.footballscore.competitions.top_score.topScoreModel

data class Scorer(
    val assists: Int,
    val goals: Int,
    val penalties: Int,
    val playedMatches: Int,
    val player: Player,
    val team: Team
)