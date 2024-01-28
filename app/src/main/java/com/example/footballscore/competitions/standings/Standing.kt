package com.example.footballscore.competitions.standings

data class Standing(
    val group: Any,
    val stage: String,
    val table: List<Table>,
    val type: String
)