package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journey_profile")
data class JourneyProfile(
    @PrimaryKey
    val id: Int = 1,
    val userName: String = "Akarsh Pandey",
    val targetExam: String = "GATE (Electronics & Communication - EC)",
    val targetExamId: String = "gate_ec",
    val targetExamCategory: String = "Engineering & Tech",
    val targetYear: String = "2027",
    val targetExamDateMillis: Long = System.currentTimeMillis() + (128L * 24 * 60 * 60 * 1000L),
    val targetRank: String = "AIR < 50",
    val dailyHourGoal: Float = 8.0f,
    val currentStreakDays: Int = 7,
    val totalStudyMinutes: Long = 2640L,
    val totalQuestionsAttempted: Int = 145,
    val totalQuestionsCorrect: Int = 122,
    val totalTaskPoints: Int = 380, // Real persistent points earned from completing tasks
    val totalQuizPoints: Int = 480, // Real persistent points earned from PYQ tests
    val streakBonusPoints: Int = 140, // Bonus points for maintaining daily streak
    val lastActiveDate: String = "2026-09-29",
    val dreamCollege: String = "IISc Bangalore / IIT Bombay",
    val journeyQuote: String = "Consistent execution beats raw talent every single time."
) {
    val totalPoints: Int
        get() = totalTaskPoints + totalQuizPoints + streakBonusPoints

    val userLevel: Int
        get() = (totalPoints / 250) + 1
}
