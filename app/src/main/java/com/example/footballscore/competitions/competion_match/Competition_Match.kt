package com.example.footballscore.competitions.competion_match

data class Competition_Match(
    val competition: Competition,
    val filters: Filters,
    val matches: List<Matche>,
    val resultSet: ResultSet
)