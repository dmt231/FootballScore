package com.example.footballscore.competitions.list_competition

import com.example.footballscore.competitions.competion_match.Match_Of_Competition

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
    val type: String,
    var hasChildMatch: Boolean = false,
    var childMatch : ArrayList<Match_Of_Competition>
)