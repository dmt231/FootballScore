package com.example.footballscore.competitions.list_competition

data class Competition(
    val area: Area,
    val code: String,
    val currentSeason: CurrentSeason,
    val emblem: String,
    val id: Int,
    val lastUpdated: String,
    val name: String,
    val numberOfAvailableSeasons: Int,
    val plan: String,
    val type: String
)