package com.example.footballscore.teams.teamsModel

data class TeamsModel(
    val competition: Competition,
    val count: Int,
    val filters: Filters,
    val season: Season,
    val teams: List<Team>
)