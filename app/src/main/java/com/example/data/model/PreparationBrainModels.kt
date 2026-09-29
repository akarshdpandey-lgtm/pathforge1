package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_accounts")
data class StudentAccount(
    @PrimaryKey
    val email: String = "akarshdpandey@gmail.com",
    val fullName: String = "Akarsh Pandey",
    val pinHash: String = "1234",
    val authToken: String = "pf_token_live_984712",
    val isLoggedIn: Boolean = true,
    val subscriptionTier: String = "PRO", // FREE, PRO, INSTITUTE
    val targetExam: String = "GATE (Electronics & Communication - EC)",
    val createdAt: Long = System.currentTimeMillis() - (29L * 24 * 3600 * 1000L),
    val lastLoginAt: Long = System.currentTimeMillis()
)

// Learning Fingerprint
data class LearningFingerprint(
    val bestStudyTimeWindow: String = "7:00 AM – 10:00 AM",
    val averageProductiveSessionMins: Int = 46,
    val timedQuestionWeaknessLevel: String = "High (51% Timed vs 81% Untimed)",
    val calculationMistakeFrequency: String = "Medium (24% of errors)",
    val revisionRetentionHalfLifeDays: Int = 7,
    val typicalStartDelayMinutes: Int = 24
)

// Preparation Doctor Diagnostic System
data class PreparationDoctorCheckup(
    val coveragePercentage: Int = 68,
    val retentionPercentage: Int = 54,
    val accuracyPercentage: Int = 71,
    val timeControlPercentage: Int = 46,
    val consistencyPercentage: Int = 63,
    val primaryBottleneck: String = "Severe Time Pressure Degradation",
    val untimedAccuracy: Int = 81,
    val timedAccuracy: Int = 57,
    val evidenceSummary: String = "When untimed, your conceptual accuracy is 81%. Under countdown timer, accuracy plummets to 57% due to formula recall panic.",
    val prescribedTreatment: String = "7-Day Timed-Practice Intervention: Two 20-min timed sets of 8 questions daily to build time resilience.",
    val verificationStatus: String = "Active Intervention (Day 2 of 7)"
)

// Knowledge Decay Radar States
enum class DecayState(val label: String, val colorHex: Long) {
    LEARNED("Learned 🟢", 0xFF10B981),
    STABLE("Stable 🔵", 0xFF0284C7),
    FADING("Fading 🟡", 0xFFF59E0B),
    FORGOTTEN("Forgotten 🔴", 0xFFEF4444)
}

data class KnowledgeDecayItem(
    val topicName: String,
    val subject: String,
    val state: DecayState,
    val daysSinceStudy: Int,
    val estimatedRetention: Int, // 0 - 100%
    val nextReviewDate: String,
    val isUrgent: Boolean
)

// Syllabus Dependency Graph
data class SyllabusPrerequisite(
    val topicName: String,
    val prerequisites: List<String>,
    val downstreamTopicsAtRisk: List<String>,
    val mastery: Int,
    val isFoundationSolid: Boolean
)

// Exam Calendar OS Item
@Entity(tableName = "exam_calendar_events")
data class ExamCalendarEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val examName: String,
    val eventStage: String, // Application, Deadline, Admit Card, Exam, Answer Key, Result
    val dateString: String,
    val officialSourceUrl: String,
    val daysRemaining: Int,
    val isCrucial: Boolean = false
)

// Second Brain Memory Log
data class SecondBrainAnswer(
    val query: String,
    val answer: String,
    val evidencePoints: List<String>
)
