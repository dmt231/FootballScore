package com.example.footballscore.matches.matchModel

data class Match(
    val filters: Filters,
    val matches: ArrayList<Matche>,
    val resultSet: ResultSet
)