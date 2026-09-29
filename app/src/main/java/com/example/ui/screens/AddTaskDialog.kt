package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskCategory
import com.example.data.model.TaskPriority

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (
        title: String,
        subject: String,
        priority: TaskPriority,
        category: TaskCategory,
        startTime: String,
        endTime: String,
        durationMinutes: Int,
        notes: String,
        topicForQuiz: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Analog Electronics") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.HIGH) }
    var selectedCategory by remember { mutableStateOf(TaskCategory.STUDY) }
    var startTime by remember { mutableStateOf("02:00 PM") }
    var endTime by remember { mutableStateOf("04:00 PM") }
    var durationMins by remember { mutableIntStateOf(120) }
    var notes by remember { mutableStateOf("") }
    var topicForQuiz by remember { mutableStateOf("Analog Electronics - Op-Amp") }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("add_task_dialog"),
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Add Daily Task / Milestone",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    placeholder = { Text("e.g., Op-Amp Slew Rate & Feedback Analysis") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("task_title_input")
                )

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject / Module") },
                    placeholder = { Text("e.g. Analog Electronics, Maths, Control") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Priority Selection
                Text("Priority Level", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TaskPriority.values().forEach { priority ->
                        FilterChip(
                            selected = selectedPriority == priority,
                            onClick = { selectedPriority = priority },
                            label = { Text(priority.label, fontSize = 11.sp) }
                        )
                    }
                }

                // Time Slots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start Time") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End Time") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                // Category
                Text("Category", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(TaskCategory.STUDY, TaskCategory.PYQ_PRACTICE, TaskCategory.REVISION, TaskCategory.BREAK_MEAL).forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.label, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = topicForQuiz,
                    onValueChange = { topicForQuiz = it },
                    label = { Text("Topic for 5 PYQ Test (Optional)") },
                    placeholder = { Text("e.g. Analog Electronics") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes / Target Objective") },
                    placeholder = { Text("e.g. Solve 10 PYQs with speed drill") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddTask(
                            title.trim(),
                            subject.trim(),
                            selectedPriority,
                            selectedCategory,
                            startTime.trim(),
                            endTime.trim(),
                            durationMins,
                            notes.trim(),
                            topicForQuiz.trim()
                        )
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.testTag("submit_task_button")
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
