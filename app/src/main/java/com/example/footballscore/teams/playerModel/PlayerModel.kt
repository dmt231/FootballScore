package com.example.footballscore.teams.playerModel

data class PlayerModel(
    val currentTeam: CurrentTeam,
    val dateOfBirth: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val lastUpdated: String,
    val name: String,
    val nationality: String,
    val position: String,
    val section: String,
    val shirtNumber: Int
)