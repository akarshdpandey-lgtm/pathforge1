package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.DailyLog
import com.example.data.model.DecayState
import com.example.data.model.EmergencyMissionBlock
import com.example.data.model.EmergencyMissionPlan
import com.example.data.model.ExamCalendarEvent
import com.example.data.model.ExamCatalog
import com.example.data.model.JourneyGraphPoint
import com.example.data.model.JourneyProfile
import com.example.data.model.KnowledgeDecayItem
import com.example.data.model.LeaderboardEntry
import com.example.data.model.LearningFingerprint
import com.example.data.model.MasteryTopic
import com.example.data.model.MistakeDna
import com.example.data.model.NextBestAction
import com.example.data.model.PlanVsRealityComparison
import com.example.data.model.PreparationDoctorCheckup
import com.example.data.model.PreparationHealth
import com.example.data.model.QuizQuestion
import com.example.data.model.QuizRecord
import com.example.data.model.SecondBrainAnswer
import com.example.data.model.StudentAccount
import com.example.data.model.StudyDebtRecord
import com.example.data.model.SyllabusPrerequisite
import com.example.data.model.TaskCategory
import com.example.data.model.TaskItem
import com.example.data.model.TaskPriority
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AspirantRepository(private val database: AppDatabase) {

    private val taskDao = database.taskDao()
    private val journeyDao = database.journeyDao()
    private val quizDao = database.quizDao()
    private val dailyLogDao = database.dailyLogDao()
    private val studyDebtDao = database.studyDebtDao()
    private val masteryDao = database.masteryDao()
    private val accountDao = database.accountDao()
    private val examCalendarDao = database.examCalendarDao()
    private val geminiService = GeminiQuizService()

    // Initialize Firestore instance for syncing study sessions, tasks, and journey data
    val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun syncTaskToFirestore(task: TaskItem, userEmail: String = "akarshdpandey@gmail.com") {
        try {
            val emailKey = userEmail.ifBlank { "anonymous_student" }
            val taskMap = hashMapOf(
                "id" to task.id,
                "title" to task.title,
                "subject" to task.subject,
                "priority" to task.priority.name,
                "category" to task.category.name,
                "durationMinutes" to task.durationMinutes,
                "isCompleted" to task.isCompleted,
                "completedAt" to (task.completedAt ?: 0L),
                "dateString" to task.dateString,
                "pointsEarned" to task.pointsEarned,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore?.collection("users")?.document(emailKey)
                ?.collection("tasks")?.document(task.id.toString())
                ?.set(taskMap, SetOptions.merge())
        } catch (_: Exception) {
            // Offline-first graceful fallback
        }
    }

    suspend fun syncJourneyProfileToFirestore(profile: JourneyProfile, userEmail: String = "akarshdpandey@gmail.com") {
        try {
            val emailKey = userEmail.ifBlank { "anonymous_student" }
            val profileMap = hashMapOf(
                "userName" to profile.userName,
                "targetExam" to profile.targetExam,
                "targetYear" to profile.targetYear,
                "targetRank" to profile.targetRank,
                "dailyHourGoal" to profile.dailyHourGoal,
                "totalStudyMinutes" to profile.totalStudyMinutes,
                "currentStreakDays" to profile.currentStreakDays,
                "totalQuestionsAttempted" to profile.totalQuestionsAttempted,
                "totalQuestionsCorrect" to profile.totalQuestionsCorrect,
                "totalTaskPoints" to profile.totalTaskPoints,
                "dreamCollege" to profile.dreamCollege,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore?.collection("users")?.document(emailKey)
                ?.collection("journey")?.document("profile")
                ?.set(profileMap, SetOptions.merge())
        } catch (_: Exception) {
            // Offline-first graceful fallback
        }
    }

    suspend fun syncDailyLogToFirestore(log: DailyLog, userEmail: String = "akarshdpandey@gmail.com") {
        try {
            val emailKey = userEmail.ifBlank { "anonymous_student" }
            val logMap = hashMapOf(
                "dateString" to log.dateString,
                "studyMinutes" to log.studyMinutes,
                "tasksCompleted" to log.tasksCompleted,
                "totalTasks" to log.totalTasks,
                "productivityScore" to log.productivityScore,
                "reflectionNotes" to log.reflectionNotes,
                "moodRating" to log.moodRating,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore?.collection("users")?.document(emailKey)
                ?.collection("daily_logs")?.document(log.dateString)
                ?.set(logMap, SetOptions.merge())
        } catch (_: Exception) {
            // Offline-first graceful fallback
        }
    }

    fun getTasksForDate(date: String): Flow<List<TaskItem>> = taskDao.getTasksForDate(date)

    suspend fun insertTask(task: TaskItem): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: TaskItem) = taskDao.updateTask(task)

    suspend fun deleteTask(task: TaskItem) = taskDao.deleteTask(task)

    suspend fun insertAllTasks(tasks: List<TaskItem>) = taskDao.insertAllTasks(tasks)

    fun getProfile(): Flow<JourneyProfile?> = journeyDao.getProfile()

    suspend fun updateProfile(profile: JourneyProfile) = journeyDao.insertOrUpdateProfile(profile)

    fun getAllQuizRecords(): Flow<List<QuizRecord>> = quizDao.getAllQuizRecords()

    suspend fun saveQuizRecord(record: QuizRecord): Long = quizDao.insertQuizRecord(record.copy(journeyId = 1))

    fun getDailyLog(date: String): Flow<DailyLog?> = dailyLogDao.getLogForDate(date)

    fun getRecentLogs(): Flow<List<DailyLog>> = dailyLogDao.getRecentLogs()

    suspend fun saveDailyLog(log: DailyLog) = dailyLogDao.insertOrUpdateLog(log)

    fun getAllDebtRecords(): Flow<List<StudyDebtRecord>> = studyDebtDao.getAllDebtRecords(1)

    fun getTotalPendingDebtMinutes(): Flow<Int?> = studyDebtDao.getTotalPendingDebtMinutes(1)

    suspend fun applyDebtRecovery(recoveredMinutes: Int) = studyDebtDao.applyDebtRecovery(recoveredMinutes, 1)

    fun getAllMasteryTopics(): Flow<List<MasteryTopic>> = masteryDao.getAllMasteryTopics(1)

    fun getActiveAccount(): Flow<StudentAccount?> = accountDao.getActiveAccount()

    fun getExamCalendarEvents(): Flow<List<ExamCalendarEvent>> = examCalendarDao.getAllEvents()

    suspend fun logout() {
        accountDao.logoutAll()
    }

    suspend fun loginWithCredentials(name: String, email: String, pin: String, exam: String) {
        accountDao.saveAccount(
            StudentAccount(
                email = email,
                fullName = name,
                pinHash = pin,
                authToken = "pf_token_${System.currentTimeMillis()}",
                isLoggedIn = true,
                subscriptionTier = "PRO",
                targetExam = exam
            )
        )
        val currentProfile = journeyDao.getProfile().firstOrNull() ?: JourneyProfile()
        val matched = ExamCatalog.allExams.firstOrNull { it.name == exam }
        journeyDao.insertOrUpdateProfile(
            currentProfile.copy(
                userName = name,
                targetExam = exam,
                targetExamId = matched?.id ?: currentProfile.targetExamId,
                targetExamCategory = matched?.category?.displayName ?: currentProfile.targetExamCategory
            )
        )
    }

    suspend fun loginOrSwitchAccount(name: String, email: String, pin: String) {
        accountDao.saveAccount(
            StudentAccount(
                email = email,
                fullName = name,
                pinHash = pin,
                authToken = "pf_token_${System.currentTimeMillis()}",
                isLoggedIn = true,
                subscriptionTier = "PRO"
            )
        )
    }

    suspend fun generate5Pyqs(topic: String, exam: String): List<QuizQuestion> {
        return geminiService.generate5Pyqs(topic, exam)
    }

    suspend fun completeTaskWithPoints(task: TaskItem): Int {
        val reward = task.priority.pointsReward
        val updatedTask = task.copy(
            isCompleted = true,
            completedAt = System.currentTimeMillis(),
            pointsEarned = reward
        )
        taskDao.updateTask(updatedTask)

        val currentProfile = journeyDao.getProfile().firstOrNull() ?: JourneyProfile()
        journeyDao.insertOrUpdateProfile(
            currentProfile.copy(
                totalTaskPoints = currentProfile.totalTaskPoints + reward,
                totalStudyMinutes = currentProfile.totalStudyMinutes + task.durationMinutes
            )
        )
        return reward
    }

    suspend fun uncompleteTask(task: TaskItem) {
        val deducted = task.pointsEarned
        val updatedTask = task.copy(
            isCompleted = false,
            completedAt = null,
            pointsEarned = 0
        )
        taskDao.updateTask(updatedTask)

        val currentProfile = journeyDao.getProfile().firstOrNull()
        if (currentProfile != null) {
            journeyDao.insertOrUpdateProfile(
                currentProfile.copy(
                    totalTaskPoints = maxOf(0, currentProfile.totalTaskPoints - deducted),
                    totalStudyMinutes = maxOf(0L, currentProfile.totalStudyMinutes - task.durationMinutes)
                )
            )
        }
    }

    suspend fun awardQuizPoints(correctCount: Int) {
        val earned = correctCount * 30
        val currentProfile = journeyDao.getProfile().firstOrNull() ?: JourneyProfile()
        journeyDao.insertOrUpdateProfile(
            currentProfile.copy(
                totalQuizPoints = currentProfile.totalQuizPoints + earned,
                totalQuestionsAttempted = currentProfile.totalQuestionsAttempted + 5,
                totalQuestionsCorrect = currentProfile.totalQuestionsCorrect + correctCount
            )
        )
    }

    // --- Execution OS: Learning Fingerprint ---
    fun getLearningFingerprint(): LearningFingerprint {
        return LearningFingerprint(
            bestStudyTimeWindow = "7:00 AM – 10:00 AM (84% peak completion)",
            averageProductiveSessionMins = 46,
            timedQuestionWeaknessLevel = "High (57% Timed vs 81% Untimed)",
            calculationMistakeFrequency = "Medium (24% of recorded errors)",
            revisionRetentionHalfLifeDays = 7,
            typicalStartDelayMinutes = 24
        )
    }

    // --- Execution OS: Preparation Doctor ---
    fun getPreparationDoctorCheckup(): PreparationDoctorCheckup {
        return PreparationDoctorCheckup(
            coveragePercentage = 68,
            retentionPercentage = 54,
            accuracyPercentage = 71,
            timeControlPercentage = 46,
            consistencyPercentage = 63,
            primaryBottleneck = "Time Pressure Degradation",
            untimedAccuracy = 81,
            timedAccuracy = 57,
            evidenceSummary = "When untimed, your conceptual accuracy is 81%. Under countdown timer, accuracy plummets to 57% due to formula recall panic.",
            prescribedTreatment = "7-Day Timed-Practice Intervention: Two 20-min timed sets of 8 questions daily to build time resilience.",
            verificationStatus = "Active Intervention (Day 2 of 7)"
        )
    }

    // --- Execution OS: Knowledge Decay Radar ---
    fun getKnowledgeDecayRadar(): List<KnowledgeDecayItem> {
        return listOf(
            KnowledgeDecayItem("Combinational Logic & K-Maps", "Digital Electronics", DecayState.LEARNED, 1, 94, "Oct 14", false),
            KnowledgeDecayItem("Linear Algebra & Eigenvalues", "Mathematics", DecayState.STABLE, 4, 82, "Oct 06", false),
            KnowledgeDecayItem("Op-Amp Feedback & Virtual Short", "Analog Circuits", DecayState.FADING, 6, 58, "Today (Urgent)", true),
            KnowledgeDecayItem("Transient Analysis (RC/RL)", "Network Theory", DecayState.FADING, 8, 51, "Tomorrow", true),
            KnowledgeDecayItem("Transmission Lines & Smith Chart", "Electromagnetics", DecayState.FORGOTTEN, 15, 29, "Overdue 5 days", true)
        )
    }

    // --- Execution OS: Syllabus Prerequisites ---
    fun getSyllabusPrerequisites(): List<SyllabusPrerequisite> {
        return listOf(
            SyllabusPrerequisite(
                topicName = "Analog Circuits (Op-Amps & Diodes)",
                prerequisites = listOf("Network Theorems (KCL/KVL)", "Semiconductor Physics"),
                downstreamTopicsAtRisk = listOf("Active Filters", "555 Timers", "ADC/DAC Converters"),
                mastery = 53,
                isFoundationSolid = false
            ),
            SyllabusPrerequisite(
                topicName = "Signals & Systems (Laplace Transform)",
                prerequisites = listOf("Differential Equations", "Complex Variables"),
                downstreamTopicsAtRisk = listOf("Control Systems Stability", "DSP Filters"),
                mastery = 61,
                isFoundationSolid = true
            )
        )
    }

    // --- Execution OS: Second Brain Query Engine ---
    fun querySecondBrain(userQuery: String): SecondBrainAnswer {
        val q = userQuery.lowercase()
        return when {
            q.contains("mistake") && (q.contains("pehle") || q.contains("repeat")) -> SecondBrainAnswer(
                query = userQuery,
                answer = "Haan, bilkul! You made this exact same mistake twice before: on Sep 22 in GATE 2021 EE and Sep 26 in Analog Quiz #2. In both cases, you assumed virtual ground holds even when the op-amp output saturated to ±12V.",
                evidencePoints = listOf("Sep 22: GATE 2021 EE (Q3) - Choice B selected instead of A", "Sep 26: Analog Practice (Q5) - Virtual ground in saturation selected")
            )
            q.contains("math") || q.contains("maths") || q.contains("strong") -> SecondBrainAnswer(
                query = userQuery,
                answer = "Aapka Mathematics sabse strongest Sep 15–20 ke dauraan tha! Linear Algebra and Eigenvalues me aapki continuous accuracy 92% thi across 18 PYQs with virtual calculator.",
                evidencePoints = listOf("Sep 15–20: 18 PYQs solved with 92% accuracy", "Average speed: 52 seconds/question (22s faster than peer average)")
            )
            q.contains("fourier") || q.contains("revise") -> SecondBrainAnswer(
                query = userQuery,
                answer = "Fourier Transform ko aapne abhi tak exactly 3 baar revise kiya hai (Day 1, Day 3, and Day 7 spaced intervals). Currently, ye 'Stable' state me hai with 78% mastery.",
                evidencePoints = listOf("First study: Sep 08", "Spaced Review 1: Sep 09 (+20m)", "Spaced Review 2: Sep 12 (+35m)", "Spaced Review 3: Sep 19 (+15m)")
            )
            else -> SecondBrainAnswer(
                query = userQuery,
                answer = "PathForge Memory: Your recent overall preparation shows 71% conceptual accuracy, but a sharp drop to 57% under strict time constraints. 31% of your recent mistakes are conceptual, primarily in feedback circuits.",
                evidencePoints = listOf("Total recorded PYQs: 145", "Logged study time: 44.0 hrs", "Active streak: 7 days")
            )
        }
    }

    // --- Execution OS: Next Best Action Engine ---
    fun getNextBestAction(): NextBestAction {
        return NextBestAction(
            subject = "Analog Circuits",
            topicName = "Diode Clippers & Clampers",
            estimatedMinutes = 42,
            reasonForSelection = "53% topic accuracy + spaced revision due today (Gap: 6 days)",
            accuracyBenchmark = 53,
            isRevisionOverdue = true,
            priorityLevel = TaskPriority.HIGH
        )
    }

    fun getPlanVsReality(): PlanVsRealityComparison {
        return PlanVsRealityComparison(
            plannedStudyHours = 28.0f,
            actualStudyHours = 21.67f,
            plannedTasksCount = 42,
            actualTasksCount = 34,
            plannedPyqsCount = 120,
            actualPyqsCount = 96,
            plannedRevisionHours = 8.0f,
            actualRevisionHours = 5.0f,
            plannedFocusSessions = 35,
            actualFocusSessions = 29,
            averageStartDelayMinutes = 24,
            behavioralDiagnosis = "Your problem isn't available time. Your average start delay is 24 minutes."
        )
    }

    fun getMistakeDna(): MistakeDna {
        return MistakeDna(
            conceptErrorPercentage = 31,
            calculationErrorPercentage = 24,
            formulaRecallPercentage = 18,
            questionReadingPercentage = 15,
            timePressurePercentage = 12,
            coreDiagnosisSummary = "You are not losing marks mainly because of calculation speed. 31% of your recent mistakes are conceptual."
        )
    }

    fun getPreparationHealth(): PreparationHealth {
        return PreparationHealth(
            overallHealthScore = 74,
            executionScore = 82,
            coverageScore = 67,
            retentionScore = 71,
            practiceScore = 76,
            consistencyScore = 64,
            currentReadinessPhase = "Building Phase"
        )
    }

    suspend fun getRealJourneyGraphPoints(): List<JourneyGraphPoint> {
        val totalMins = journeyDao.getProfile().firstOrNull()?.totalStudyMinutes ?: 2640L
        val currentHours = totalMins / 60.0f

        return listOf(
            JourneyGraphPoint("Sep 01", 1, 0.0f, 0, 0),
            JourneyGraphPoint("Sep 05", 5, 5.5f, 6, 70),
            JourneyGraphPoint("Sep 10", 10, 14.0f, 16, 74),
            JourneyGraphPoint("Sep 15", 15, 22.5f, 25, 78),
            JourneyGraphPoint("Sep 20", 20, 29.0f, 33, 81),
            JourneyGraphPoint("Sep 25", 25, 36.5f, 42, 84),
            JourneyGraphPoint("Today", 29, maxOf(currentHours, 44.0f), 52, 86)
        )
    }

    fun getMissionForDuration(minutes: Int): EmergencyMissionPlan {
        return when (minutes) {
            15 -> EmergencyMissionPlan(
                missionTitle = "15-Min 'No Zero Day' Quick Diagnostic",
                totalMinutes = 15,
                rationale = "Preserves continuity and concept retrieval without pretending 15 min equals 4 hours.",
                blocks = listOf(
                    EmergencyMissionBlock("0–15 min", "10-Question High-Yield Flash Drill", "Mixed Engineering", 15, "Speed diagnostic on active weak topics.")
                )
            )
            75 -> EmergencyMissionPlan(
                missionTitle = "Bad Day Mode: Minimum Viable Plan (75m)",
                totalMinutes = 75,
                rationale = "Instead of dropping the whole day, executes highest-value high-leverage work so momentum is never lost.",
                blocks = listOf(
                    EmergencyMissionBlock("0–35 min", "Most Critical Concept: Op-Amp Feedback", "Analog Circuits", 35, "Deep focus on highest weightage exam module."),
                    EmergencyMissionBlock("35–55 min", "10 Targeted High-Impact PYQs", "Analog Circuits", 20, "Precision retrieval practice."),
                    EmergencyMissionBlock("55–75 min", "Formula Revision & Summary", "Formula Notes", 20, "Spaced retrieval to lock memory.")
                )
            )
            else -> EmergencyMissionPlan(
                missionTitle = "Your 90-Minute Emergency Mission",
                totalMinutes = 90,
                rationale = "This combination gives the highest current preparation value from your recorded weaknesses and overdue revision.",
                blocks = listOf(
                    EmergencyMissionBlock("0–35 min", "Control Systems — Block Diagram Reduction", "Control Systems", 35, "Address #1 identified weakness (42% accuracy)."),
                    EmergencyMissionBlock("35–55 min", "8 Targeted Questions with Timer", "Control Systems", 20, "Standard GATE/IES numerical practice."),
                    EmergencyMissionBlock("55–70 min", "Review 3 Previous Mistakes", "Mistake DNA", 15, "Targeting conceptual slip-ups from past test."),
                    EmergencyMissionBlock("70–90 min", "Spaced Revision: Network Theorems", "Network Theory", 20, "Due for Day-7 spaced repetition review.")
                )
            )
        }
    }

    suspend fun executeAutomaticScheduleRecovery(todayDate: String): List<TaskItem> {
        val rebalancedTasks = listOf(
            TaskItem(
                title = "Recovered High-Impact: Analog Op-Amp Gain Derivations",
                subject = "Analog Electronics",
                priority = TaskPriority.HIGH,
                category = TaskCategory.STUDY,
                startTime = "09:00 AM",
                endTime = "10:30 AM",
                durationMinutes = 90,
                dateString = todayDate,
                pointsEarned = 50,
                notes = "Auto-recovered: highest weightage priority for upcoming mock.",
                topicForQuiz = "Analog Electronics - Op-Amp Circuits"
            ),
            TaskItem(
                title = "Recovered Practice: 10 Core Network Theorems PYQ",
                subject = "Network Theory",
                priority = TaskPriority.HIGH,
                category = TaskCategory.PYQ_PRACTICE,
                startTime = "10:45 AM",
                endTime = "12:15 PM",
                durationMinutes = 90,
                dateString = todayDate,
                pointsEarned = 50,
                notes = "Essential revision topic prioritized by Recovery Engine."
            ),
            TaskItem(
                title = "Study Debt Recovery: 45-Min Focused Speed Drill",
                subject = "Mathematics",
                priority = TaskPriority.MEDIUM,
                category = TaskCategory.PYQ_PRACTICE,
                startTime = "02:00 PM",
                endTime = "02:45 PM",
                durationMinutes = 45,
                dateString = todayDate,
                pointsEarned = 30,
                notes = "Clearing 45 mins of accumulated Study Debt."
            ),
            TaskItem(
                title = "Evening Revision: Diode Clippers & Clampers",
                subject = "Analog Electronics",
                priority = TaskPriority.MEDIUM,
                category = TaskCategory.REVISION,
                startTime = "06:00 PM",
                endTime = "07:00 PM",
                durationMinutes = 60,
                dateString = todayDate,
                pointsEarned = 30,
                notes = "Closing the 6-day revision overdue gap."
            )
        )
        taskDao.deleteTasksForDate(todayDate)
        taskDao.insertAllTasks(rebalancedTasks)
        applyDebtRecovery(45)
        return rebalancedTasks
    }

    suspend fun ensureInitialData(todayDate: String) {
        val existingProfile = journeyDao.getProfile().firstOrNull()
        if (existingProfile == null) {
            journeyDao.insertOrUpdateProfile(
                JourneyProfile(
                    id = 1,
                    userName = "Akarsh Pandey",
                    targetExam = "GATE (Electronics & Communication - EC)",
                    targetExamId = "gate_ec",
                    targetExamCategory = "Engineering & Tech",
                    targetYear = "2027",
                    targetExamDateMillis = System.currentTimeMillis() + (128L * 24 * 60 * 60 * 1000L),
                    targetRank = "AIR < 50",
                    dailyHourGoal = 8.0f,
                    currentStreakDays = 7,
                    totalStudyMinutes = 2640L,
                    totalQuestionsAttempted = 145,
                    totalQuestionsCorrect = 122,
                    totalTaskPoints = 380,
                    totalQuizPoints = 480,
                    streakBonusPoints = 140,
                    lastActiveDate = todayDate,
                    dreamCollege = "IISc Bangalore / IIT Bombay",
                    journeyQuote = "Your preparation has a memory. Plan → Study → Measure → Diagnose → Recover → Adapt."
                )
            )
        }

        // Student Account
        val activeAccount = accountDao.getActiveAccount().firstOrNull()
        if (activeAccount == null) {
            accountDao.saveAccount(
                StudentAccount(
                    email = "akarshdpandey@gmail.com",
                    fullName = "Akarsh Pandey",
                    pinHash = "1234",
                    authToken = "pf_token_live_984712",
                    isLoggedIn = true,
                    subscriptionTier = "PRO",
                    targetExam = "GATE (Electronics & Communication - EC)"
                )
            )
        }

        // Exam Calendar Events
        val existingEvents = examCalendarDao.getAllEvents().firstOrNull()
        if (existingEvents.isNullOrEmpty()) {
            val calendarEvents = listOf(
                ExamCalendarEvent(
                    examName = "GATE 2027 (EC/EE/CS)",
                    eventStage = "Application Form Deadline",
                    dateString = "October 15, 2026",
                    officialSourceUrl = "https://gate2027.iisc.ac.in",
                    daysRemaining = 16,
                    isCrucial = true
                ),
                ExamCalendarEvent(
                    examName = "GATE 2027 (EC/EE/CS)",
                    eventStage = "Admit Card Release",
                    dateString = "January 04, 2027",
                    officialSourceUrl = "https://gate2027.iisc.ac.in",
                    daysRemaining = 97,
                    isCrucial = false
                ),
                ExamCalendarEvent(
                    examName = "GATE 2027 (EC/EE/CS)",
                    eventStage = "Examination Day (Forenoon Session)",
                    dateString = "February 07, 2027",
                    officialSourceUrl = "https://gate2027.iisc.ac.in",
                    daysRemaining = 128,
                    isCrucial = true
                ),
                ExamCalendarEvent(
                    examName = "UPSC ESE / IES 2027",
                    eventStage = "Preliminary Examination",
                    dateString = "February 21, 2027",
                    officialSourceUrl = "https://upsc.gov.in",
                    daysRemaining = 142,
                    isCrucial = false
                )
            )
            examCalendarDao.insertEvents(calendarEvents)
        }

        // Seed initial Mastery Map topics
        val existingMastery = masteryDao.getAllMasteryTopics(1).firstOrNull()
        if (existingMastery.isNullOrEmpty()) {
            val initialMastery = listOf(
                MasteryTopic(
                    subject = "Signals & Systems",
                    topicName = "Fourier Transform & LTI Systems",
                    masteryPercentage = 78,
                    totalAttempts = 24,
                    averageAccuracy = 82,
                    secondsSlowerThanAverage = 4,
                    repeatedMistakesCount = 1,
                    daysRevisionOverdue = 2,
                    diagnosticReason = "Strong retention, minor time pressure in convolution integrals.",
                    spacedRevisionStage = 3
                ),
                MasteryTopic(
                    subject = "Network Theory",
                    topicName = "Transient Analysis & Two-Port Networks",
                    masteryPercentage = 61,
                    totalAttempts = 18,
                    averageAccuracy = 66,
                    secondsSlowerThanAverage = 18,
                    repeatedMistakesCount = 2,
                    daysRevisionOverdue = 4,
                    diagnosticReason = "Initial condition confusion in inductor/capacitor transitions.",
                    spacedRevisionStage = 2
                ),
                MasteryTopic(
                    subject = "Analog Circuits",
                    topicName = "Op-Amp Feedback & Slew Rate",
                    masteryPercentage = 53,
                    totalAttempts = 14,
                    averageAccuracy = 61,
                    secondsSlowerThanAverage = 38,
                    repeatedMistakesCount = 3,
                    daysRevisionOverdue = 6,
                    diagnosticReason = "Concept retention + revision gap (Virtual ground in saturation).",
                    spacedRevisionStage = 1
                ),
                MasteryTopic(
                    subject = "Digital Electronics",
                    topicName = "Combinational & Sequential Circuits",
                    masteryPercentage = 89,
                    totalAttempts = 30,
                    averageAccuracy = 92,
                    secondsSlowerThanAverage = 0,
                    repeatedMistakesCount = 0,
                    daysRevisionOverdue = 1,
                    diagnosticReason = "Mastered topic: speed and accuracy in K-maps & Flip-Flops.",
                    spacedRevisionStage = 5
                ),
                MasteryTopic(
                    subject = "Control Systems",
                    topicName = "Bode Plots & Nyquist Stability",
                    masteryPercentage = 42,
                    totalAttempts = 12,
                    averageAccuracy = 48,
                    secondsSlowerThanAverage = 45,
                    repeatedMistakesCount = 4,
                    daysRevisionOverdue = 8,
                    diagnosticReason = "Phase margin calculations and non-minimum phase system gap.",
                    spacedRevisionStage = 1
                ),
                MasteryTopic(
                    subject = "Electromagnetics",
                    topicName = "Transmission Lines & Waveguides",
                    masteryPercentage = 27,
                    totalAttempts = 8,
                    averageAccuracy = 35,
                    secondsSlowerThanAverage = 52,
                    repeatedMistakesCount = 5,
                    daysRevisionOverdue = 11,
                    diagnosticReason = "Needs complete concept re-study from fundamentals.",
                    spacedRevisionStage = 1
                )
            )
            masteryDao.insertAll(initialMastery)
        }

        // Seed initial Study Debt
        val debtRecords = studyDebtDao.getAllDebtRecords(1).firstOrNull()
        if (debtRecords.isNullOrEmpty()) {
            studyDebtDao.insertDebtRecord(
                StudyDebtRecord(
                    journeyId = 1,
                    dateString = "Yesterday",
                    plannedMinutes = 240,
                    actualMinutes = 150,
                    debtMinutes = 90,
                    recoveredMinutes = 0,
                    isResolved = false,
                    recoveryStrategyNotes = "Prioritize high-impact Analog & Maths recovery blocks."
                )
            )
        }

        val existingTasks = taskDao.getTasksForDate(todayDate).firstOrNull()
        if (existingTasks.isNullOrEmpty()) {
            loadDefaultAspirantSchedule(todayDate)
        }
    }

    suspend fun loadDefaultAspirantSchedule(date: String) {
        val defaultTasks = listOf(
            TaskItem(
                title = "Analog Circuits: Op-Amp Feedback & Virtual Ground",
                subject = "Analog Electronics",
                priority = TaskPriority.HIGH,
                category = TaskCategory.STUDY,
                startTime = "08:30 AM",
                endTime = "10:30 AM",
                durationMinutes = 120,
                dateString = date,
                isCompleted = true,
                completedAt = System.currentTimeMillis() - 4 * 3600 * 1000L,
                pointsEarned = 50,
                notes = "Focus on inverting/non-inverting gains, offset voltage, and slew rate.",
                topicForQuiz = "Analog Electronics - Op-Amp Circuits"
            ),
            TaskItem(
                title = "Short Break & Tea Recharge",
                subject = "Routine",
                priority = TaskPriority.LOW,
                category = TaskCategory.BREAK_MEAL,
                startTime = "10:30 AM",
                endTime = "11:00 AM",
                durationMinutes = 30,
                dateString = date,
                isCompleted = true,
                completedAt = System.currentTimeMillis() - 3 * 3600 * 1000L,
                pointsEarned = 15,
                notes = "Hydration + quick walk."
            ),
            TaskItem(
                title = "Analog PYQs: BJT Small Signal & Diodes (15 Questions)",
                subject = "Analog Electronics",
                priority = TaskPriority.HIGH,
                category = TaskCategory.PYQ_PRACTICE,
                startTime = "11:00 AM",
                endTime = "01:00 PM",
                durationMinutes = 120,
                dateString = date,
                isCompleted = true,
                completedAt = System.currentTimeMillis() - 1 * 3600 * 1000L,
                pointsEarned = 50,
                notes = "Solve GATE 2018-2023 questions on CE amplifiers & clippers.",
                topicForQuiz = "Analog Electronics - BJT and Diodes"
            ),
            TaskItem(
                title = "Lunch & Rest (Relaxation)",
                subject = "Routine",
                priority = TaskPriority.MEDIUM,
                category = TaskCategory.BREAK_MEAL,
                startTime = "01:00 PM",
                endTime = "02:00 PM",
                durationMinutes = 60,
                dateString = date,
                isCompleted = false,
                notes = "Eat nutritious food and rest eyesight."
            ),
            TaskItem(
                title = "Engineering Mathematics: Linear Algebra (Eigenvalues)",
                subject = "Mathematics",
                priority = TaskPriority.HIGH,
                category = TaskCategory.STUDY,
                startTime = "02:00 PM",
                endTime = "04:00 PM",
                durationMinutes = 120,
                dateString = date,
                isCompleted = false,
                notes = "Cayley-Hamilton theorem, rank of matrix, system of equations.",
                topicForQuiz = "Engineering Mathematics"
            ),
            TaskItem(
                title = "Maths PYQ Practice (10 Questions)",
                subject = "Mathematics",
                priority = TaskPriority.MEDIUM,
                category = TaskCategory.PYQ_PRACTICE,
                startTime = "04:15 PM",
                endTime = "05:30 PM",
                durationMinutes = 75,
                dateString = date,
                isCompleted = false,
                notes = "Speed drill with virtual calculator.",
                topicForQuiz = "Engineering Mathematics"
            ),
            TaskItem(
                title = "Evening Walk & Workout",
                subject = "Fitness",
                priority = TaskPriority.LOW,
                category = TaskCategory.FITNESS,
                startTime = "05:30 PM",
                endTime = "06:15 PM",
                durationMinutes = 45,
                dateString = date,
                isCompleted = false,
                notes = "Physical exercise to maintain stamina."
            ),
            TaskItem(
                title = "Network Theory: Transient Analysis (RC & RL)",
                subject = "Network Theory",
                priority = TaskPriority.HIGH,
                category = TaskCategory.STUDY,
                startTime = "06:30 PM",
                endTime = "08:30 PM",
                durationMinutes = 120,
                dateString = date,
                isCompleted = false,
                notes = "First-order differential equation methods and initial conditions.",
                topicForQuiz = "Network Theory"
            ),
            TaskItem(
                title = "Dinner & Family Time",
                subject = "Routine",
                priority = TaskPriority.LOW,
                category = TaskCategory.BREAK_MEAL,
                startTime = "08:30 PM",
                endTime = "09:30 PM",
                durationMinutes = 60,
                dateString = date,
                isCompleted = false,
                notes = "Unwind."
            ),
            TaskItem(
                title = "Daily Revision & 5-PYQ Rapid Test",
                subject = "Revision",
                priority = TaskPriority.HIGH,
                category = TaskCategory.MOCK_TEST,
                startTime = "09:30 PM",
                endTime = "10:30 PM",
                durationMinutes = 60,
                dateString = date,
                isCompleted = false,
                notes = "Revise short notes and check rank on leaderboard!",
                topicForQuiz = "Analog Electronics"
            )
        )
        taskDao.insertAllTasks(defaultTasks)
    }

    fun getLeaderboard(
        exam: String,
        userTotalPoints: Int,
        userName: String,
        taskPoints: Int,
        quizPoints: Int,
        streakDays: Int
    ): List<LeaderboardEntry> {
        val peers = when {
            exam.contains("UPSC", ignoreCase = true) || exam.contains("Civil", ignoreCase = true) -> listOf(
                LeaderboardEntry(1, "Ananya Sharma", exam, "Delhi (Old Rajinder Nagar)", 420, 93, 1680, 31, false, "AIR 5 Target"),
                LeaderboardEntry(2, "Vikramaditya Rao", exam, "Hyderabad (Ashok Nagar)", 390, 91, 1530, 26, false, "Polity Master"),
                LeaderboardEntry(3, "Ritesh Jha", exam, "Patna", 365, 88, 1420, 21, false, "GS Pro"),
                LeaderboardEntry(4, "Priyanka Meena", exam, "Jaipur", 340, 87, 1340, 18, false, "Daily Mains Writer"),
                LeaderboardEntry(5, "Sourabh Pandey", exam, "Prayagraj", 310, 86, 1220, 15, false, "CSAT Ace")
            )
            exam.contains("JEE", ignoreCase = true) -> listOf(
                LeaderboardEntry(1, "Chirag Agrawal", exam, "Kota (Rajasthan)", 450, 96, 1790, 35, false, "AIR < 10"),
                LeaderboardEntry(2, "Pranav Goyal", exam, "Hyderabad", 420, 94, 1640, 29, false, "Physics Olympiad"),
                LeaderboardEntry(3, "Tanvi Singhal", exam, "Delhi NCR", 380, 92, 1480, 24, false, "Maths Wizard"),
                LeaderboardEntry(4, "Aryan Bhardwaj", exam, "Indore", 350, 89, 1360, 19, false, "Chem Expert")
            )
            else -> listOf(
                LeaderboardEntry(1, "Aditya Rathore", exam, "IIT Bombay Aspirant", 380, 94, 1620, 30, false, "AIR 1 Target"),
                LeaderboardEntry(2, "Megha Kulkarni", exam, "IISc Aspirant (Pune)", 350, 92, 1490, 24, false, "Analog Pro"),
                LeaderboardEntry(3, "Tanmay Sen", exam, "IIT Delhi Aspirant", 320, 90, 1380, 21, false, "Maths Beast"),
                LeaderboardEntry(4, "Shreya Bansal", exam, "IIT Madras Aspirant", 295, 88, 1260, 17, false, "PYQ Master"),
                LeaderboardEntry(5, "Kunal Saxena", exam, "NIT Trichy", 270, 87, 1170, 15, false, "Rising Star")
            )
        }

        val userEntry = LeaderboardEntry(
            rank = 0,
            name = if (userName.isNotBlank()) "$userName (You)" else "You",
            targetExam = exam,
            collegeOrCity = "Task: $taskPoints pts • PYQ: $quizPoints pts",
            questionsSolved = 150 + (quizPoints / 30),
            accuracy = 86,
            scorePoints = userTotalPoints,
            streakDays = streakDays,
            isUser = true,
            badge = "🔥 ${streakDays}d Streak"
        )

        val combined = (peers + userEntry).sortedByDescending { it.scorePoints }
        return combined.mapIndexed { index, entry ->
            entry.copy(rank = index + 1)
        }
    }
}
