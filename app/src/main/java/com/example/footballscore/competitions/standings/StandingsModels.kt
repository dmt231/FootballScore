package com.example.footballscore.competitions.standings

data class StandingsModels(
    val area: Area,
    val competition: Competition,
    val filters: Filters,
    val season: Season,
    val standings: List<Standing>
)