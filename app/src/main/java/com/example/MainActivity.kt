package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AuthDialog
import com.example.ui.components.ExamCalendarSheet
import com.example.ui.components.PreparationDoctorDialog
import com.example.ui.components.SecondBrainSheet
import com.example.ui.screens.JourneyScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MasteryScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AspirantViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PathForgeApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PathForgeApp(viewModel: AspirantViewModel = viewModel()) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val quizState by viewModel.quizState.collectAsState()
    val activeAccount by viewModel.activeAccount.collectAsState()
    val calendarEvents by viewModel.examCalendarEvents.collectAsState()
    val secondBrainAnswers by viewModel.secondBrainAnswers.collectAsState()

    val showDoctorDialog by viewModel.showDoctorDialog.collectAsState()
    val showSecondBrainDialog by viewModel.showSecondBrainDialog.collectAsState()
    val showCalendarDialog by viewModel.showCalendarDialog.collectAsState()
    val showAuthDialog by viewModel.showAuthDialog.collectAsState()

    // Handle back button when quiz is open or on other tabs
    BackHandler(enabled = selectedTab != 0) {
        if (selectedTab == 3 && quizState.inProgress) {
            viewModel.resetQuiz()
        } else {
            viewModel.selectTab(0)
        }
    }

    if (activeAccount == null || activeAccount?.isLoggedIn != true) {
        LoginScreen(
            onLoginSuccess = { name, email, pin, exam ->
                viewModel.loginWithCredentials(name, email, pin, exam)
            }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "PathForge",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = profile?.targetExam?.substringBefore(" ") ?: "GATE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    },
                    actions = {
                        // Doctor Shortcut
                        IconButton(onClick = { viewModel.setDoctorDialogVisible(true) }) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = "Preparation Doctor",
                                tint = Color(0xFFDC2626)
                            )
                        }

                        // Second Brain Memory Shortcut
                        IconButton(onClick = { viewModel.setSecondBrainDialogVisible(true) }) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Second Brain",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Account & Pro Tier
                        Surface(
                            modifier = Modifier
                                .clickable { viewModel.setAuthDialogVisible(true) }
                                .padding(end = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFD1FAE5)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF059669))
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = activeAccount?.subscriptionTier ?: "PRO",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    color = Color(0xFF047857)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { viewModel.selectTab(0) },
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Today") },
                        label = { Text("Today", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_tab_today")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { viewModel.selectTab(1) },
                        icon = { Icon(Icons.Default.Timeline, contentDescription = "Journey") },
                        label = { Text("Journey", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_tab_journey")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { viewModel.selectTab(2) },
                        icon = { Icon(Icons.Default.Biotech, contentDescription = "Mastery") },
                        label = { Text("Mastery", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_tab_mastery")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { viewModel.selectTab(3) },
                        icon = { Icon(Icons.Default.Psychology, contentDescription = "Practice") },
                        label = { Text("Practice", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_tab_quiz")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { viewModel.selectTab(4) },
                        icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Ranks") },
                        label = { Text("Ranks", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_tab_leaderboard")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> TodayScreen(viewModel = viewModel)
                    1 -> JourneyScreen(viewModel = viewModel)
                    2 -> MasteryScreen(viewModel = viewModel)
                    3 -> QuizScreen(viewModel = viewModel)
                    4 -> LeaderboardScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Executive Dialogs & Sheets
    if (showDoctorDialog) {
        PreparationDoctorDialog(
            checkup = viewModel.preparationDoctorCheckup,
            onDismiss = { viewModel.setDoctorDialogVisible(false) },
            onStartIntervention = {
                viewModel.setDoctorDialogVisible(false)
                viewModel.startQuizForTopic("Timed Set: Analog & Network Diagnostics")
            }
        )
    }

    if (showSecondBrainDialog) {
        SecondBrainSheet(
            answers = secondBrainAnswers,
            onQuery = { q -> viewModel.querySecondBrain(q) },
            onDismiss = { viewModel.setSecondBrainDialogVisible(false) }
        )
    }

    if (showCalendarDialog) {
        ExamCalendarSheet(
            events = calendarEvents,
            onDismiss = { viewModel.setCalendarDialogVisible(false) }
        )
    }

    if (showAuthDialog) {
        AuthDialog(
            currentAccount = activeAccount,
            onDismiss = { viewModel.setAuthDialogVisible(false) },
            onLogin = { name, email, pin -> viewModel.loginOrSwitchAccount(name, email, pin) },
            onLogout = { viewModel.logout() }
        )
    }
}
