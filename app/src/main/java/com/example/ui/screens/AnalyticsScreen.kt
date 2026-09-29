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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.components.ProductivityGaugeCard
import com.example.ui.components.StatMetricCardsRow
import com.example.ui.components.SubjectDistributionCard
import com.example.ui.viewmodel.AspirantViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AspirantViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val dailyLog by viewModel.dailyLog.collectAsState()

    val completedTasks = tasks.count { it.isCompleted }
    val totalTasks = tasks.size
    val productivityPercentage = if (totalTasks > 0) (completedTasks * 100) / totalTasks else 0
    val studyMinsToday = tasks.filter { it.isCompleted }.sumOf { it.durationMinutes }

    var reflectionText by remember(dailyLog) {
        mutableStateOf(dailyLog?.reflectionNotes ?: "Solved Op-Amp feedback circuits and 5 PYQs with 80%+ accuracy. Need to revise slew-rate derivation tomorrow.")
    }
    var selectedMood by remember(dailyLog) {
        mutableIntStateOf(dailyLog?.moodRating ?: 4)
    }
    var savedFeedbackMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("analytics_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Behtareen Daily Analytics 📊",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Track your discipline, subject distribution & study output",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Productivity Gauge Card
        item {
            ProductivityGaugeCard(
                percentage = productivityPercentage,
                completedTasks = completedTasks,
                totalTasks = totalTasks,
                studyMinutes = studyMinsToday,
                goalHours = profile?.dailyHourGoal ?: 8.0f
            )
        }

        // 3 Key Metrics Row: Streak, PYQs Solved, Focus Time
        item {
            StatMetricCardsRow(
                streakDays = profile?.currentStreakDays ?: 7,
                questionsAttempted = profile?.totalQuestionsAttempted ?: 145,
                accuracy = if ((profile?.totalQuestionsAttempted ?: 1) > 0) {
                    ((profile?.totalQuestionsCorrect ?: 122) * 100) / (profile?.totalQuestionsAttempted ?: 145)
                } else 85,
                studyMinutesToday = studyMinsToday
            )
        }

        // Subject Breakdown Visualizer
        item {
            SubjectDistributionCard(tasks = tasks)
        }

        // Daily Reflection Journal Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Daily Reflection & Learning Log",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "How was your focus and understanding today?",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 5-Star Focus Rating
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = if (star <= selectedMood) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Star $star",
                                tint = Color(0xFFFFB703),
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable { selectedMood = star }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = reflectionText,
                        onValueChange = { reflectionText = it },
                        label = { Text("What did you learn today? Any bottlenecks?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reflection_text_input"),
                        minLines = 3,
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.saveReflection(reflectionText, selectedMood)
                            savedFeedbackMessage = "Reflection saved successfully! 🌟"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_reflection_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Today's Reflection")
                    }

                    if (savedFeedbackMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = savedFeedbackMessage!!,
                            color = Color(0xFF10B981),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
