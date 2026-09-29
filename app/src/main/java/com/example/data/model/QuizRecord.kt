package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_records")
data class QuizRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val journeyId: Int = 1, // Fixed: Attached to active journey!
    val topic: String,
    val targetExam: String,
    val totalQuestions: Int = 5,
    val correctAnswers: Int,
    val scoreObtained: Float,
    val maxScore: Float = 10f,
    val timeTakenSeconds: Int,
    val accuracyPercentage: Int,
    val percentileEstimate: Float,
    val isOfficialPyq: Boolean = true,
    val primaryMistakeCategory: String = "Concept", // Concept, Calculation, Formula, Reading, TimePressure
    val timestamp: Long = System.currentTimeMillis()
)
