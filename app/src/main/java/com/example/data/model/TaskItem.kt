package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskPriority(val label: String, val level: Int, val pointsReward: Int) {
    HIGH("Must Do 🔥", 1, 50),
    MEDIUM("Should Do ⚡", 2, 30),
    LOW("Nice to Do ☕", 3, 15)
}

enum class TaskCategory(val label: String, val iconName: String) {
    STUDY("Concept Study", "book"),
    PYQ_PRACTICE("PYQ Practice", "quiz"),
    REVISION("Formula & Notes", "refresh"),
    MOCK_TEST("Mock Test", "timer"),
    BREAK_MEAL("Meal & Refresh", "restaurant"),
    FITNESS("Fitness / Walk", "fitness")
}

@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subject: String = "General",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: TaskCategory = TaskCategory.STUDY,
    val startTime: String, // e.g. "09:00 AM"
    val endTime: String,   // e.g. "11:00 AM"
    val durationMinutes: Int = 60,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val pointsEarned: Int = 0, // Recorded persistent points when completed
    val isSkipped: Boolean = false,
    val dateString: String, // e.g. "2026-09-29"
    val notes: String = "",
    val topicForQuiz: String = "" // if study task, can launch PYQ quiz for this topic
)
