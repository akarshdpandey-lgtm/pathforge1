package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Real historical plot point for Journey Graph (Day 1 to Today)
data class JourneyGraphPoint(
    val dateLabel: String,
    val dayIndex: Int,
    val cumulativeHours: Float,
    val tasksFinishedCount: Int,
    val accuracyPercentage: Int
)

// 2. Study Debt Model
@Entity(tableName = "study_debt_records")
data class StudyDebtRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val journeyId: Int = 1,
    val dateString: String,
    val plannedMinutes: Int,
    val actualMinutes: Int,
    val debtMinutes: Int, // maxOf(0, plannedMinutes - actualMinutes)
    val recoveredMinutes: Int = 0,
    val isResolved: Boolean = false,
    val recoveryStrategyNotes: String = ""
)

// 3. Plan vs Reality Engine Comparison
data class PlanVsRealityComparison(
    val plannedStudyHours: Float,
    val actualStudyHours: Float,
    val plannedTasksCount: Int,
    val actualTasksCount: Int,
    val plannedPyqsCount: Int,
    val actualPyqsCount: Int,
    val plannedRevisionHours: Float,
    val actualRevisionHours: Float,
    val plannedFocusSessions: Int,
    val actualFocusSessions: Int,
    val averageStartDelayMinutes: Int = 24,
    val behavioralDiagnosis: String = "Your problem isn't available time. Your average start delay is 24 minutes."
)

// 5. Next Best Action (What should I do right now?)
data class NextBestAction(
    val subject: String,
    val topicName: String,
    val estimatedMinutes: Int,
    val reasonForSelection: String,
    val accuracyBenchmark: Int,
    val isRevisionOverdue: Boolean,
    val priorityLevel: TaskPriority
)

// 6. Mastery Map & Weakness Diagnostic
@Entity(tableName = "mastery_topics")
data class MasteryTopic(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val journeyId: Int = 1,
    val subject: String,
    val topicName: String,
    val masteryPercentage: Int, // 0 - 100
    val totalAttempts: Int,
    val averageAccuracy: Int,
    val secondsSlowerThanAverage: Int,
    val repeatedMistakesCount: Int,
    val daysRevisionOverdue: Int,
    val diagnosticReason: String, // e.g. "Concept retention + revision gap"
    val spacedRevisionStage: Int = 1 // 1: Day 1, 2: Day 3, 3: Day 7, 4: Day 14, 5: Day 30
)

// 7. Mistake DNA
data class MistakeDna(
    val conceptErrorPercentage: Int = 31,
    val calculationErrorPercentage: Int = 24,
    val formulaRecallPercentage: Int = 18,
    val questionReadingPercentage: Int = 15,
    val timePressurePercentage: Int = 12,
    val coreDiagnosisSummary: String = "You are not losing marks mainly because of calculation speed. 31% of your recent mistakes are conceptual."
)

// 10. Preparation Health (Genuine execution score, no fake gamification)
data class PreparationHealth(
    val overallHealthScore: Int = 74, // 0 to 100
    val executionScore: Int = 82,
    val coverageScore: Int = 67,
    val retentionScore: Int = 71,
    val practiceScore: Int = 76,
    val consistencyScore: Int = 64,
    val currentReadinessPhase: String = "Building Phase"
)

// 11. Emergency Mission / "I Have Only 90 Mins" / Bad Day Mode
data class EmergencyMissionBlock(
    val timeRangeLabel: String,
    val title: String,
    val subject: String,
    val durationMins: Int,
    val description: String
)

data class EmergencyMissionPlan(
    val missionTitle: String,
    val totalMinutes: Int,
    val rationale: String,
    val blocks: List<EmergencyMissionBlock>
)
