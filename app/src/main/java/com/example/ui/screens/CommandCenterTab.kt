package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LanguageMode
import com.example.ui.components.CyberPanel
import com.example.ui.components.GlowingKarenOrb
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.MusicMood
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.HudTab
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState

@Composable
fun CommandCenterTab(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    var quickQuery by remember { mutableStateOf("") }
    val isSinhala = uiState.languageMode == LanguageMode.SINHALA
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero Greeting Banner
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(theme.primary)
                                .border(1.dp, Color(0xFFFFD700), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SS",
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                        Column {
                            Text(
                                text = if (isSinhala) "ආයුබෝවන්, වීරයා!" else "GREETINGS, HERO!",
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = theme.primary
                            )
                            Text(
                                text = if (isSinhala) "රවී සහ ෂැඩෝ සපෝටර් සූදානම්" else "RAVI & SHADOW SUPPORTER READY",
                                fontFamily = OrbitronFontFamily,
                                fontSize = 9.sp,
                                color = theme.secondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isSinhala)
                            "සියලු උපාංග සඳහා සකසන ලදී (Desktop, PC, Mobile, Smart Boards & Watches). ආර්ඩුයිනෝ සහ ශ්‍රේණි 1-13 පාඩම් සහායක සක්‍රීයයි."
                        else
                            "Cross-Device Adaptive Core (Desktop, PC, Mobile, Smart Boards, Smart Watches). Arduino Studio and Grade 1-13 Study Hub online.",
                        fontSize = 11.sp,
                        color = CyberTextSecondary,
                        lineHeight = 16.sp
                    )
                }

                GlowingKarenOrb(
                    isListening = uiState.isListeningMic,
                    isThinking = uiState.isAiStreaming,
                    onClick = {
                        viewModel.speakResponse("RAVI command telemetry fully operational.")
                    }
                )
            }
        }

        // Quick Ask Prompt Bar
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (isSinhala) "රවී ගෙන් විමසන්න (QUICK ASK)" else "QUICK ASK RAVI",
                    fontFamily = OrbitronFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = theme.secondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = quickQuery,
                        onValueChange = { quickQuery = it },
                        placeholder = {
                            Text(
                                if (isSinhala) "ඕනෑම පැනයක් හෝ විද්‍යාගාර අණක් මෙහි ලියන්න..." else "Ask anything or write invention request...",
                                fontSize = 11.sp,
                                color = CyberTextMuted
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_ask_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = theme.secondary,
                            unfocusedBorderColor = Color(0xFF1E293B),
                            focusedContainerColor = CyberDarkCard,
                            unfocusedContainerColor = CyberDarkCard,
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (quickQuery.isNotBlank()) {
                                viewModel.selectTab(HudTab.RAVI_CHAT)
                                viewModel.sendAiPrompt(quickQuery)
                                quickQuery = ""
                            }
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.primary)
                            .size(44.dp)
                            .testTag("quick_ask_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // System Telemetry Meters Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TelemetryTile(
                title = "NEURAL CORE",
                value = uiState.selectedAiModel.badge,
                icon = Icons.Default.Memory,
                accent = theme.secondary,
                modifier = Modifier.weight(1f)
            )
            TelemetryTile(
                title = "STUDY TASKS",
                value = "${uiState.studyTasks.count { it.isCompleted }}/${uiState.studyTasks.size} DONE",
                icon = Icons.Default.School,
                accent = Color(0xFFFFD700),
                modifier = Modifier.weight(1f)
            )
            TelemetryTile(
                title = "MUSIC MOOD",
                value = uiState.activeMoodFilter.label.uppercase(),
                icon = Icons.Default.MusicNote,
                accent = theme.primary,
                modifier = Modifier.weight(1f)
            )
        }

        // Quick Room Navigation Shortcuts
        Text(
            text = if (isSinhala) "විශේෂිත මැදිරි (COMMAND ROOMS)" else "TACTICAL ROOM SHORTCUTS",
            fontFamily = OrbitronFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = theme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoomCard(
                title = "RAVI AI CHAT",
                subtitle = "Bilingual assistant (EN/SI)",
                icon = Icons.Default.Terminal,
                accent = theme.primary,
                onClick = { viewModel.selectTab(HudTab.RAVI_CHAT) },
                modifier = Modifier.weight(1f)
            )
            RoomCard(
                title = "ARDUINO STUDIO",
                subtitle = "Robotics, circuits & code",
                icon = Icons.Default.Memory,
                accent = theme.secondary,
                onClick = { viewModel.selectTab(HudTab.ARDUINO_STUDIO) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoomCard(
                title = "STUDY HUB (1-13)",
                subtitle = "Sri Lankan school syllabi",
                icon = Icons.Default.School,
                accent = Color(0xFFFFB703),
                onClick = { viewModel.selectTab(HudTab.STUDY_HUB) },
                modifier = Modifier.weight(1f)
            )
            RoomCard(
                title = "SS MUSIC ROOM",
                subtitle = "Synthwave & Focus Audio",
                icon = Icons.Default.MusicNote,
                accent = Color(0xFFA855F7),
                onClick = { viewModel.selectTab(HudTab.MUSIC_ROOM) },
                modifier = Modifier.weight(1f)
            )
        }

        // Pomodoro Focus Protocol Quick Box
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "FOCUS PROTOCOL (25 MIN)",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color(0xFFFFB703)
                    )
                    val m = (uiState.focusSecondsRemaining / 60).toString().padStart(2, '0')
                    val s = (uiState.focusSecondsRemaining % 60).toString().padStart(2, '0')
                    Text(
                        text = "$m:$s",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = if (uiState.isTimerRunning) Color(0xFFFFB703) else CyberTextPrimary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    NeonButton(
                        text = if (uiState.isTimerRunning) "PAUSE" else "START",
                        onClick = {
                            if (uiState.isTimerRunning) viewModel.pauseFocusTimer()
                            else viewModel.startFocusTimer()
                        },
                        icon = Icons.Default.PlayArrow,
                        isActive = uiState.isTimerRunning,
                        accentColor = Color(0xFFFFB703)
                    )
                }
            }
        }
    }
}

@Composable
fun TelemetryTile(
    title: String,
    value: String,
    icon: ImageVector,
    accent: Color,
    modifier: Modifier = Modifier
) {
    CyberPanel(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = title,
                    fontFamily = OrbitronFontFamily,
                    fontSize = 8.sp,
                    color = CyberTextMuted
                )
            }
            Text(
                text = value,
                fontFamily = OrbitronFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = accent,
                maxLines = 1
            )
        }
    }
}

@Composable
fun RoomCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CyberPanel(
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontFamily = OrbitronFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = CyberTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = CyberTextMuted
            )
        }
    }
}
