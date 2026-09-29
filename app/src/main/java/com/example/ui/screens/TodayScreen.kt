package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskItem
import com.example.ui.components.ActiveTaskBanner
import com.example.ui.components.EmergencyMissionDialog
import com.example.ui.components.NextBestActionCard
import com.example.ui.components.NextTaskTriggerDialog
import com.example.ui.components.StudyDebtCard
import com.example.ui.components.TaskCard
import com.example.ui.viewmodel.AspirantViewModel
import java.util.concurrent.TimeUnit

@Composable
fun TodayScreen(
    viewModel: AspirantViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val sortByPriority by viewModel.sortByPriority.collectAsState()
    val nextTaskTrigger by viewModel.nextTaskTrigger.collectAsState()
    val xpToast by viewModel.xpToast.collectAsState()
    val pendingDebtMinutes by viewModel.pendingDebtMinutes.collectAsState()
    val activeMission by viewModel.activeMission.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val sortedTasks = remember(tasks, sortByPriority) {
        if (sortByPriority) {
            tasks.sortedWith(compareBy({ it.isCompleted }, { it.priority.level }, { it.startTime }))
        } else {
            tasks.sortedWith(compareBy({ it.isCompleted }, { it.startTime }))
        }
    }

    val activeTask = remember(tasks) {
        viewModel.getActiveTask(tasks)
    }

    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size

    val daysLeft = remember(profile?.targetExamDateMillis) {
        val targetMillis = profile?.targetExamDateMillis ?: (System.currentTimeMillis() + 128L * 86400000L)
        val diff = targetMillis - System.currentTimeMillis()
        maxOf(0L, TimeUnit.MILLISECONDS.toDays(diff))
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("today_screen_task_list"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // XP Notification Toast
            if (xpToast != null) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.clearXpToast() },
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF10B981)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = xpToast!!,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Header: Exam Countdown & Streak Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = profile?.targetExam ?: "GATE 2027 Aspirant",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$daysLeft Days to D-Day",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Streak Pill
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFEF4444).copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${profile?.currentStreakDays ?: 7} Days",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFEF4444)
                                )
                            }
                        }
                    }
                }
            }

            // Personal Preparation Brain & Digital Twin
            item {
                com.example.ui.components.DigitalTwinCard(
                    fingerprint = viewModel.learningFingerprint,
                    onOpenDoctor = { viewModel.setDoctorDialogVisible(true) },
                    onOpenSecondBrain = { viewModel.setSecondBrainDialogVisible(true) },
                    onOpenCalendar = { viewModel.setCalendarDialogVisible(true) },
                    onOpenAuth = { viewModel.setAuthDialogVisible(true) }
                )
            }

            // Knowledge Decay Radar
            item {
                com.example.ui.components.KnowledgeDecayCard(
                    decayItems = viewModel.knowledgeDecayRadar,
                    onLaunchRevision = { topic -> viewModel.startQuizForTopic(topic) }
                )
            }

            // Next Best Action (What should I do right now?)
            item {
                NextBestActionCard(
                    action = viewModel.nextBestAction,
                    onStartAction = { action ->
                        viewModel.startQuizForTopic("${action.subject} - ${action.topicName}")
                    },
                    onLaunchQuiz = { topic -> viewModel.startQuizForTopic(topic) },
                    onSelectMissionMinutes = { mins -> viewModel.setEmergencyMission(mins) }
                )
            }

            // Study Debt Tracker & Automatic Recovery Engine
            item {
                StudyDebtCard(
                    debtMinutes = pendingDebtMinutes ?: 90,
                    onActivateRecovery = { viewModel.triggerAutomaticScheduleRecovery() }
                )
            }

            // Interactive Active Task Card (Directly addresses user prompt!)
            item {
                ActiveTaskBanner(
                    activeTask = activeTask,
                    onCompleteTask = { task ->
                        viewModel.completeTask(task)
                    },
                    onTestTopic = { topic ->
                        viewModel.startQuizForTopic(topic)
                    }
                )
            }

            // Section Header: Tasks Overview, Sorting, and Preset Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Today's Routine ($completedCount/$totalCount Done)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (sortByPriority) "Sorted: High Priority First 🔥" else "Sorted: Time Schedule 🕒",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.toggleSortMode() },
                            modifier = Modifier.testTag("toggle_sort_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Toggle Sort Mode",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (tasks.isEmpty()) {
                            OutlinedButton(
                                onClick = { viewModel.reloadDefaultAspirantSchedule() },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Load 10 Tasks", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Task List
            items(sortedTasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onToggleCompleted = { viewModel.toggleTaskCompletion(it) },
                    onDeleteTask = { viewModel.deleteTask(it) },
                    onTestTopic = { topic -> viewModel.startQuizForTopic(topic) }
                )
            }
        }

        // Floating Action Button to Add Task
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_task_fab"),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }
    }

    // Add Task Dialog
    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onAddTask = { title, subject, priority, category, start, end, duration, notes, topic ->
                viewModel.addNewTask(
                    title = title,
                    subject = subject,
                    priority = priority,
                    category = category,
                    startTime = start,
                    endTime = end,
                    durationMinutes = duration,
                    notes = notes,
                    topicForQuiz = topic
                )
                showAddDialog = false
            }
        )
    }

    // Next Task Trigger Sheet / Dialog (Interactive feature requested by user!)
    nextTaskTrigger?.let { trigger ->
        if (trigger.isVisible) {
            NextTaskTriggerDialog(
                completedTask = trigger.completedTask,
                nextTask = trigger.nextTask,
                pointsAwarded = trigger.pointsAwarded,
                onDismiss = { viewModel.dismissNextTaskTrigger() },
                onStartNextTask = { next -> viewModel.startNextTaskFromTrigger(next) },
                onLaunchTopicTest = { topic ->
                    viewModel.dismissNextTaskTrigger()
                    viewModel.startQuizForTopic(topic)
                }
            )
        }
    }

    // Emergency Mission Dialog (90m mission / Bad Day Mode / 15m No Zero Day)
    activeMission?.let { mission ->
        EmergencyMissionDialog(
            mission = mission,
            onDismiss = { viewModel.dismissEmergencyMission() },
            onApplyToToday = { appliedMission ->
                viewModel.dismissEmergencyMission()
                appliedMission.blocks.firstOrNull()?.let { firstBlock ->
                    viewModel.startQuizForTopic("${firstBlock.subject} - ${firstBlock.title}")
                }
            }
        )
    }
}
