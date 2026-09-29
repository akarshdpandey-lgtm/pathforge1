package com.example.data.model

data class QuizQuestion(
    val id: Int,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val examYearTag: String, // e.g. "GATE 2023 EC", "GATE 2021 EE"
    val explanation: String,
    val topic: String
)
