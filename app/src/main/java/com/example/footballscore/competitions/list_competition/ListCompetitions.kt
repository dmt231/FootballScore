package com.example.footballscore.competitions.list_competition

data class ListCompetitions(
    val competitions: ArrayList<Competition>,
    val count: Int,
    val filters: Filters
)