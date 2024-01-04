package com.example.footballscore.competitions.competion_match

data class Competition_Match(
    val competition: Competition,
    val filters: Filters,
    val matches: ArrayList<Match_Of_Competition>,
    val resultSet: ResultSet
)