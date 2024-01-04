package com.example.footballscore.competitions.competion_match

data class Match_Of_Competition(
    val area: Area,
    val awayTeam: AwayTeam,
    val competition: Competition,
    val group: Any,
    val homeTeam: HomeTeam,
    val id: Int,
    val lastUpdated: String,
    val matchday: Int,
    val odds: Odds,
    val referees: List<Any>,
    val score: Score,
    val season: Season,
    val stage: String,
    val status: String,
    val utcDate: String
)