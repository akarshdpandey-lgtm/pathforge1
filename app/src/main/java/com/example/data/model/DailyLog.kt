package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_logs")
data class DailyLog(
    @PrimaryKey
    val dateString: String, // e.g. "2026-09-29"
    val studyMinutes: Int = 0,
    val tasksCompleted: Int = 0,
    val totalTasks: Int = 0,
    val productivityScore: Int = 0, // 0 to 100%
    val reflectionNotes: String = "",
    val moodRating: Int = 4 // 1 to 5
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val targetExam: String,
    val collegeOrCity: String,
    val questionsSolved: Int,
    val accuracy: Int,
    val scorePoints: Int,
    val streakDays: Int,
    val isUser: Boolean = false,
    val badge: String = ""
)
