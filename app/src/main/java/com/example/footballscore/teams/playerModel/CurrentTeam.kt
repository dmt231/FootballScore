package com.example.footballscore.teams.playerModel

data class CurrentTeam(
    val address: String,
    val area: Area,
    val clubColors: String,
    val contract: Contract,
    val crest: String,
    val founded: Int,
    val id: Int,
    val name: String,
    val runningCompetitions: List<RunningCompetition>,
    val shortName: String,
    val tla: String,
    val venue: String,
    val website: String
)