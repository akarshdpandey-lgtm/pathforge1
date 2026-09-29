package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DailyLog
import com.example.data.model.EmergencyMissionPlan
import com.example.data.model.ExamCatalog
import com.example.data.model.ExamInfo
import com.example.data.model.JourneyGraphPoint
import com.example.data.model.JourneyProfile
import com.example.data.model.LeaderboardEntry
import com.example.data.model.MasteryTopic
import com.example.data.model.MistakeDna
import com.example.data.model.NextBestAction
import com.example.data.model.PlanVsRealityComparison
import com.example.data.model.PreparationHealth
import com.example.data.model.QuizQuestion
import com.example.data.model.QuizRecord
import com.example.data.model.StudyDebtRecord
import com.example.data.model.TaskCategory
import com.example.data.model.TaskItem
import com.example.data.model.TaskPriority
import com.example.data.repository.AspirantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NextTaskTriggerState(
    val completedTask: TaskItem,
    val nextTask: TaskItem?,
    val isVisible: Boolean,
    val pointsAwarded: Int = 50
)

data class ActiveQuizState(
    val inProgress: Boolean = false,
    val topic: String = "",
    val exam: String = "",
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<Int, Int> = emptyMap(),
    val isSubmitted: Boolean = false,
    val scoreObtained: Float = 0f,
    val maxScore: Float = 10f,
    val accuracy: Int = 0,
    val percentile: Float = 0f,
    val pointsEarned: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AspirantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AspirantRepository
    val todayDateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _sortByPriority = MutableStateFlow(false)
    val sortByPriority: StateFlow<Boolean> = _sortByPriority.asStateFlow()

    private val _nextTaskTrigger = MutableStateFlow<NextTaskTriggerState?>(null)
    val nextTaskTrigger: StateFlow<NextTaskTriggerState?> = _nextTaskTrigger.asStateFlow()

    private val _quizState = MutableStateFlow(ActiveQuizState())
    val quizState: StateFlow<ActiveQuizState> = _quizState.asStateFlow()

    private val _xpToast = MutableStateFlow<String?>(null)
    val xpToast: StateFlow<String?> = _xpToast.asStateFlow()

    private val _activeMission = MutableStateFlow<EmergencyMissionPlan?>(null)
    val activeMission: StateFlow<EmergencyMissionPlan?> = _activeMission.asStateFlow()

    private val _selectedWeakTopicDiagnostic = MutableStateFlow<MasteryTopic?>(null)
    val selectedWeakTopicDiagnostic: StateFlow<MasteryTopic?> = _selectedWeakTopicDiagnostic.asStateFlow()

    private val _journeyGraphPoints = MutableStateFlow<List<JourneyGraphPoint>>(emptyList())
    val journeyGraphPoints: StateFlow<List<JourneyGraphPoint>> = _journeyGraphPoints.asStateFlow()

    val tasks: StateFlow<List<TaskItem>>
    val profile: StateFlow<JourneyProfile?>
    val quizRecords: StateFlow<List<QuizRecord>>
    val dailyLog: StateFlow<DailyLog?>
    val masteryTopics: StateFlow<List<MasteryTopic>>
    val pendingDebtMinutes: StateFlow<Int?>
    val activeAccount: StateFlow<com.example.data.model.StudentAccount?>
    val examCalendarEvents: StateFlow<List<com.example.data.model.ExamCalendarEvent>>

    val planVsReality: PlanVsRealityComparison
    val mistakeDna: MistakeDna
    val preparationHealth: PreparationHealth
    val nextBestAction: NextBestAction
    val learningFingerprint: com.example.data.model.LearningFingerprint
    val preparationDoctorCheckup: com.example.data.model.PreparationDoctorCheckup
    val knowledgeDecayRadar: List<com.example.data.model.KnowledgeDecayItem>
    val syllabusPrerequisites: List<com.example.data.model.SyllabusPrerequisite>

    private val _secondBrainAnswers = MutableStateFlow<List<com.example.data.model.SecondBrainAnswer>>(
        listOf(
            com.example.data.model.SecondBrainAnswer(
                query = "Recent Preparation Insight",
                answer = "PathForge Memory: Your conceptual retention is 71%, but timed speed drops to 57%. Focus on 20-min timed bursts to clear this bottleneck.",
                evidencePoints = listOf("44.0 study hours logged", "145 PYQs tracked", "7-day streak active")
            )
        )
    )
    val secondBrainAnswers: StateFlow<List<com.example.data.model.SecondBrainAnswer>> = _secondBrainAnswers.asStateFlow()

    private val _showDoctorDialog = MutableStateFlow(false)
    val showDoctorDialog: StateFlow<Boolean> = _showDoctorDialog.asStateFlow()

    private val _showSecondBrainDialog = MutableStateFlow(false)
    val showSecondBrainDialog: StateFlow<Boolean> = _showSecondBrainDialog.asStateFlow()

    private val _showCalendarDialog = MutableStateFlow(false)
    val showCalendarDialog: StateFlow<Boolean> = _showCalendarDialog.asStateFlow()

    private val _showAuthDialog = MutableStateFlow(false)
    val showAuthDialog: StateFlow<Boolean> = _showAuthDialog.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AspirantRepository(database)

        tasks = repository.getTasksForDate(todayDateString)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        profile = repository.getProfile()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        quizRecords = repository.getAllQuizRecords()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        dailyLog = repository.getDailyLog(todayDateString)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        masteryTopics = repository.getAllMasteryTopics()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        pendingDebtMinutes = repository.getTotalPendingDebtMinutes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 90)

        activeAccount = repository.getActiveAccount()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        examCalendarEvents = repository.getExamCalendarEvents()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        planVsReality = repository.getPlanVsReality()
        mistakeDna = repository.getMistakeDna()
        preparationHealth = repository.getPreparationHealth()
        nextBestAction = repository.getNextBestAction()
        learningFingerprint = repository.getLearningFingerprint()
        preparationDoctorCheckup = repository.getPreparationDoctorCheckup()
        knowledgeDecayRadar = repository.getKnowledgeDecayRadar()
        syllabusPrerequisites = repository.getSyllabusPrerequisites()

        viewModelScope.launch {
            repository.ensureInitialData(todayDateString)
            _journeyGraphPoints.value = repository.getRealJourneyGraphPoints()
        }
    }

    fun setDoctorDialogVisible(visible: Boolean) {
        _showDoctorDialog.value = visible
    }

    fun setSecondBrainDialogVisible(visible: Boolean) {
        _showSecondBrainDialog.value = visible
    }

    fun setCalendarDialogVisible(visible: Boolean) {
        _showCalendarDialog.value = visible
    }

    fun setAuthDialogVisible(visible: Boolean) {
        _showAuthDialog.value = visible
    }

    fun querySecondBrain(queryText: String) {
        val answer = repository.querySecondBrain(queryText)
        _secondBrainAnswers.value = listOf(answer) + _secondBrainAnswers.value
    }

    fun loginOrSwitchAccount(name: String, email: String, pin: String) {
        viewModelScope.launch {
            repository.loginOrSwitchAccount(name, email, pin)
            _xpToast.value = "Welcome back, $name! Authenticated with PathForge Cloud PRO. 🛡️"
        }
    }

    fun loginWithCredentials(name: String, email: String, pin: String, exam: String) {
        viewModelScope.launch {
            repository.loginWithCredentials(name, email, pin, exam)
            _xpToast.value = "Welcome, $name! Target exam set to $exam. Authenticated PRO. 🚀"
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _xpToast.value = "Logged out from PathForge OS."
        }
    }

    fun selectTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun toggleSortMode() {
        _sortByPriority.value = !_sortByPriority.value
    }

    fun clearXpToast() {
        _xpToast.value = null
    }

    fun openTopicDiagnostic(topic: MasteryTopic) {
        _selectedWeakTopicDiagnostic.value = topic
    }

    fun closeTopicDiagnostic() {
        _selectedWeakTopicDiagnostic.value = null
    }

    fun setEmergencyMission(minutes: Int) {
        _activeMission.value = repository.getMissionForDuration(minutes)
    }

    fun dismissEmergencyMission() {
        _activeMission.value = null
    }

    fun triggerAutomaticScheduleRecovery() {
        viewModelScope.launch {
            repository.executeAutomaticScheduleRecovery(todayDateString)
            _xpToast.value = "Recovery Mode Activated: 4 high-impact blocks scheduled. Debt reduced by 45m! 🚀"
        }
    }

    fun getActiveTask(taskList: List<TaskItem>): TaskItem? {
        return taskList.firstOrNull { !it.isCompleted && !it.isSkipped }
    }

    fun getNextTaskAfter(current: TaskItem, taskList: List<TaskItem>): TaskItem? {
        val currentIndex = taskList.indexOfFirst { it.id == current.id }
        if (currentIndex != -1) {
            for (i in (currentIndex + 1) until taskList.size) {
                val candidate = taskList[i]
                if (!candidate.isCompleted && !candidate.isSkipped) {
                    return candidate
                }
            }
        }
        return taskList.firstOrNull { it.id != current.id && !it.isCompleted && !it.isSkipped }
    }

    fun completeTask(task: TaskItem) {
        viewModelScope.launch {
            val awardedPoints = repository.completeTaskWithPoints(task)
            _xpToast.value = "+$awardedPoints XP added to Leaderboard! 🌟"

            val currentTaskList = tasks.value
            val next = getNextTaskAfter(task, currentTaskList)
            _nextTaskTrigger.value = NextTaskTriggerState(
                completedTask = task.copy(isCompleted = true),
                nextTask = next,
                isVisible = true,
                pointsAwarded = awardedPoints
            )
            _journeyGraphPoints.value = repository.getRealJourneyGraphPoints()
        }
    }

    fun dismissNextTaskTrigger() {
        _nextTaskTrigger.value = null
    }

    fun startNextTaskFromTrigger(nextTask: TaskItem) {
        dismissNextTaskTrigger()
        if (nextTask.category == TaskCategory.PYQ_PRACTICE && nextTask.topicForQuiz.isNotBlank()) {
            startQuizForTopic(nextTask.topicForQuiz)
        }
    }

    fun toggleTaskCompletion(task: TaskItem) {
        if (!task.isCompleted) {
            completeTask(task)
        } else {
            viewModelScope.launch {
                repository.uncompleteTask(task)
                _xpToast.value = "Task unchecked. Points updated."
            }
        }
    }

    fun skipTask(task: TaskItem) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isSkipped = true))
        }
    }

    fun deleteTask(task: TaskItem) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun addNewTask(
        title: String,
        subject: String,
        priority: TaskPriority,
        category: TaskCategory,
        startTime: String,
        endTime: String,
        durationMinutes: Int,
        notes: String,
        topicForQuiz: String
    ) {
        viewModelScope.launch {
            repository.insertTask(
                TaskItem(
                    title = title,
                    subject = subject,
                    priority = priority,
                    category = category,
                    startTime = startTime,
                    endTime = endTime,
                    durationMinutes = durationMinutes,
                    dateString = todayDateString,
                    notes = notes,
                    topicForQuiz = topicForQuiz
                )
            )
            _xpToast.value = "Task scheduled successfully! Earn +${priority.pointsReward} XP upon completion."
        }
    }

    fun reloadDefaultAspirantSchedule() {
        viewModelScope.launch {
            repository.loadDefaultAspirantSchedule(todayDateString)
        }
    }

    fun startQuizForTopic(topic: String, examOverride: String? = null) {
        val targetExam = examOverride ?: profile.value?.targetExam ?: "GATE (Electronics & Communication - EC)"
        _quizState.value = ActiveQuizState(
            inProgress = true,
            topic = topic,
            exam = targetExam,
            isLoading = true
        )
        _selectedTab.value = 1

        viewModelScope.launch {
            try {
                val questions = repository.generate5Pyqs(topic, targetExam)
                _quizState.value = _quizState.value.copy(
                    questions = questions,
                    isLoading = false,
                    currentQuestionIndex = 0,
                    selectedAnswers = emptyMap(),
                    isSubmitted = false
                )
            } catch (e: Exception) {
                _quizState.value = _quizState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load questions: ${e.localizedMessage}"
                )
            }
        }
    }

    fun selectQuizAnswer(questionIndex: Int, optionIndex: Int) {
        val currentAnswers = _quizState.value.selectedAnswers.toMutableMap()
        currentAnswers[questionIndex] = optionIndex
        _quizState.value = _quizState.value.copy(selectedAnswers = currentAnswers)
    }

    fun nextQuizQuestion() {
        val current = _quizState.value.currentQuestionIndex
        if (current < _quizState.value.questions.size - 1) {
            _quizState.value = _quizState.value.copy(currentQuestionIndex = current + 1)
        }
    }

    fun prevQuizQuestion() {
        val current = _quizState.value.currentQuestionIndex
        if (current > 0) {
            _quizState.value = _quizState.value.copy(currentQuestionIndex = current - 1)
        }
    }

    fun submitQuiz(timeTakenSeconds: Int = 180) {
        val state = _quizState.value
        val questions = state.questions
        var correctCount = 0

        questions.forEachIndexed { index, q ->
            val userAns = state.selectedAnswers[index]
            if (userAns != null && userAns == q.correctIndex) {
                correctCount++
            }
        }

        val examInfo = ExamCatalog.findExamByName(state.exam)
        var score = 0f
        questions.forEachIndexed { index, q ->
            val userAns = state.selectedAnswers[index]
            if (userAns != null) {
                if (userAns == q.correctIndex) {
                    score += examInfo.correctMarks
                } else {
                    score -= examInfo.negativeMarks
                }
            }
        }
        score = maxOf(0f, score)
        val accuracy = if (state.selectedAnswers.isNotEmpty()) {
            (correctCount * 100) / state.selectedAnswers.size
        } else 0

        val percentile = when (correctCount) {
            5 -> 99.4f
            4 -> 94.8f
            3 -> 82.5f
            2 -> 64.0f
            1 -> 42.0f
            else -> 18.0f
        }

        val totalQuizEarnedPoints = correctCount * 30

        val record = QuizRecord(
            journeyId = 1, // Fixed: Attached to active journey!
            topic = state.topic,
            targetExam = state.exam,
            totalQuestions = 5,
            correctAnswers = correctCount,
            scoreObtained = score,
            maxScore = examInfo.correctMarks * 5,
            timeTakenSeconds = timeTakenSeconds,
            accuracyPercentage = accuracy,
            percentileEstimate = percentile,
            isOfficialPyq = true,
            primaryMistakeCategory = if (correctCount < 5) "Concept" else "None"
        )

        viewModelScope.launch {
            repository.saveQuizRecord(record)
            repository.awardQuizPoints(correctCount)
            _xpToast.value = "+$totalQuizEarnedPoints XP earned from PYQ Test! Leaderboard updated."
        }

        _quizState.value = state.copy(
            isSubmitted = true,
            scoreObtained = score,
            maxScore = examInfo.correctMarks * 5,
            accuracy = accuracy,
            percentile = percentile,
            pointsEarned = totalQuizEarnedPoints
        )
    }

    fun resetQuiz() {
        _quizState.value = ActiveQuizState(inProgress = false)
    }

    fun selectExamFromCatalog(examInfo: ExamInfo) {
        viewModelScope.launch {
            val current = profile.value ?: JourneyProfile()
            repository.updateProfile(
                current.copy(
                    targetExam = examInfo.name,
                    targetExamId = examInfo.id,
                    targetExamCategory = examInfo.category.displayName
                )
            )
            _xpToast.value = "Target exam updated to ${examInfo.name}!"
        }
    }

    fun updateProfileInfo(
        name: String,
        targetExam: String,
        targetYear: String,
        targetRank: String,
        dailyHours: Float,
        dreamCollege: String
    ) {
        viewModelScope.launch {
            val current = profile.value ?: JourneyProfile()
            val matchedExam = ExamCatalog.allExams.firstOrNull { it.name == targetExam }
            repository.updateProfile(
                current.copy(
                    userName = name,
                    targetExam = targetExam,
                    targetExamId = matchedExam?.id ?: current.targetExamId,
                    targetExamCategory = matchedExam?.category?.displayName ?: current.targetExamCategory,
                    targetYear = targetYear,
                    targetRank = targetRank,
                    dailyHourGoal = dailyHours,
                    dreamCollege = dreamCollege
                )
            )
            _xpToast.value = "Journey targets updated successfully! 🚀"
        }
    }

    fun getLeaderboard(): List<LeaderboardEntry> {
        val currentProfile = profile.value ?: JourneyProfile()
        return repository.getLeaderboard(
            exam = currentProfile.targetExam,
            userTotalPoints = currentProfile.totalPoints,
            userName = currentProfile.userName,
            taskPoints = currentProfile.totalTaskPoints,
            quizPoints = currentProfile.totalQuizPoints,
            streakDays = currentProfile.currentStreakDays
        )
    }

    fun saveReflection(notes: String, mood: Int) {
        viewModelScope.launch {
            val totalTasksCount = tasks.value.size
            val completedCount = tasks.value.count { it.isCompleted }
            val studyMins = tasks.value.filter { it.isCompleted }.sumOf { it.durationMinutes }
            val productivity = if (totalTasksCount > 0) (completedCount * 100) / totalTasksCount else 0

            repository.saveDailyLog(
                DailyLog(
                    dateString = todayDateString,
                    studyMinutes = studyMins,
                    tasksCompleted = completedCount,
                    totalTasks = totalTasksCount,
                    productivityScore = productivity,
                    reflectionNotes = notes,
                    moodRating = mood
                )
            )
            _xpToast.value = "Daily reflection saved successfully! 📝"
        }
    }
}
