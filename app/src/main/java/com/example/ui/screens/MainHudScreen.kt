package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LanguageMode
import com.example.ui.components.CyberPanel
import com.example.ui.components.GlowingKarenOrb
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.KarenOsTheme
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.HudTab
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState

@Composable
fun MainHudScreen(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState
) {
    val theme = LocalKarenTheme.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(CyberDarkBg)) {
        val isWideScreen = maxWidth >= 720.dp
        val isSmartWatch = uiState.isWatchCompactMode || maxWidth <= 280.dp

        if (isSmartWatch) {
            // Ultra-Compact Smart Watch HUD View
            SmartWatchHudView(viewModel = viewModel, uiState = uiState)
        } else if (isWideScreen) {
            // Desktop / PC / Smart Board Layout: Left Side Navigation Rail + Right Viewport
            DesktopBoardHudView(viewModel = viewModel, uiState = uiState)
        } else {
            // Mobile Handheld Layout: Top Bar + Viewport + Persistent Music + Bottom Rooms Bar
            MobileHudView(viewModel = viewModel, uiState = uiState)
        }
    }
}

// 1. Mobile Handheld Layout
@Composable
private fun MobileHudView(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState
) {
    val theme = LocalKarenTheme.current
    val isSinhala = uiState.languageMode == LanguageMode.SINHALA

    Scaffold(
        containerColor = CyberDarkBg,
        topBar = {
            TopHudHeader(viewModel = viewModel, uiState = uiState)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                PersistentMusicBar(viewModel = viewModel, uiState = uiState)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HudTab.entries.forEach { tab ->
                        val isSelected = tab == uiState.activeTab
                        val icon = getTabIcon(tab)

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) theme.primary.copy(alpha = 0.25f) else CyberDarkSurface)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) theme.primary else theme.borderGlow,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.selectTab(tab) }
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                                .testTag("nav_tab_${tab.name}"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) theme.primary else theme.secondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (isSinhala) tab.titleSinhala else tab.title,
                                fontFamily = OrbitronFontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 10.sp,
                                color = if (isSelected) Color.White else CyberTextMuted
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            RoomContent(viewModel = viewModel, uiState = uiState)
        }
    }
}

// 2. Desktop, PC & Smart Board Wide Layout (Left Side Navigation Rail)
@Composable
private fun DesktopBoardHudView(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState
) {
    val theme = LocalKarenTheme.current
    val isSinhala = uiState.languageMode == LanguageMode.SINHALA

    Scaffold(
        containerColor = CyberDarkBg,
        topBar = {
            TopHudHeader(viewModel = viewModel, uiState = uiState, isWide = true)
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Left Navigation Rail for PC / Smart Boards
            Column(
                modifier = Modifier
                    .width(220.dp)
                    .fillMaxHeight()
                    .background(CyberDarkSurface)
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(topEnd = 12.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "COMMAND ROOMS",
                    fontFamily = OrbitronFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = theme.secondary,
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp)
                )

                HudTab.entries.forEach { tab ->
                    val isSelected = tab == uiState.activeTab
                    val icon = getTabIcon(tab)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) theme.primary.copy(alpha = 0.25f) else Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) theme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.selectTab(tab) }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .testTag("nav_rail_${tab.name}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) theme.primary else theme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isSinhala) tab.titleSinhala else tab.title,
                            fontFamily = OrbitronFontFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp,
                            color = if (isSelected) Color.White else CyberTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Left mini player for desktop
                PersistentMusicBar(viewModel = viewModel, uiState = uiState)
            }

            // Right Main Content Viewport
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                RoomContent(viewModel = viewModel, uiState = uiState)
            }
        }
    }
}

// 3. Smart Watch (Ultra-Compact Wrist Mode)
@Composable
private fun SmartWatchHudView(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState
) {
    val theme = LocalKarenTheme.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Watch Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SS WATCH",
                    fontFamily = OrbitronFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    color = Color(0xFFFFD700)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF1E293B))
                        .clickable { viewModel.toggleWatchCompactMode() }
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "EXIT WATCH",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 8.sp,
                        color = theme.secondary
                    )
                }
            }

            // Central Animated Orb
            GlowingKarenOrb(
                isListening = uiState.isListeningMic,
                isThinking = uiState.isAiStreaming,
                onClick = { viewModel.speakResponse("Smart watch assistant active. Ready for voice.") }
            )

            // Focus Timer status
            val m = (uiState.focusSecondsRemaining / 60).toString().padStart(2, '0')
            val s = (uiState.focusSecondsRemaining % 60).toString().padStart(2, '0')
            Text(
                text = "$m:$s",
                fontFamily = OrbitronFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFFFB703)
            )

            // Quick Music Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.togglePlayPause() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(theme.primary)
                ) {
                    Icon(
                        imageVector = if (uiState.isPlayingMusic) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { viewModel.nextTrack() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CyberDarkSurface)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = null,
                        tint = theme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = uiState.currentTrack.title,
                fontFamily = OrbitronFontFamily,
                fontSize = 8.sp,
                color = CyberTextMuted,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun RoomContent(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState
) {
    Crossfade(
        targetState = uiState.activeTab,
        modifier = Modifier.fillMaxSize(),
        label = "TabCrossfade"
    ) { tab ->
        when (tab) {
            HudTab.COMMAND_CENTER -> CommandCenterTab(viewModel, uiState)
            HudTab.RAVI_CHAT -> NeuralTerminalTab(viewModel, uiState)
            HudTab.MUSIC_ROOM -> MusicRoomTab(viewModel, uiState)
            HudTab.ARDUINO_STUDIO -> ArduinoStudioTab(viewModel, uiState)
            HudTab.STUDY_HUB -> StudyHubTab(viewModel, uiState)
        }
    }
}

@Composable
fun TopHudHeader(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    isWide: Boolean = false
) {
    val theme = LocalKarenTheme.current

    CyberPanel(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        cornerRounding = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Title & Arc Reactor / SS Emblem
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(theme.primary)
                        .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(8.dp)),
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "SHADOW SUPPORTER",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(theme.primary.copy(alpha = 0.2f))
                                .border(1.dp, theme.primary.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "RAVI CORE",
                                fontFamily = OrbitronFontFamily,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = theme.primary
                            )
                        }
                    }
                    Text(
                        text = "Universal Core: Desktop • PC • Mobile • Board • Watch",
                        fontSize = 8.sp,
                        color = CyberTextMuted
                    )
                }
            }

            // Right Controls: Watch Mode Toggle + Language + Themes + Online Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Smart Watch Toggle Icon
                IconButton(
                    onClick = { viewModel.toggleWatchCompactMode() },
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Watch,
                        contentDescription = "Smart Watch Mode",
                        tint = if (uiState.isWatchCompactMode) Color(0xFFFFD700) else CyberTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Language Switcher Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, theme.secondary, RoundedCornerShape(6.dp))
                        .clickable { viewModel.toggleLanguage() }
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                        .testTag("header_language_toggle")
                ) {
                    Text(
                        text = uiState.languageMode.code,
                        fontFamily = OrbitronFontFamily,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.secondary
                    )
                }

                // 9 Theme Selector Dots
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF030712))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
                        .padding(horizontal = 5.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KarenOsTheme.entries.forEach { t ->
                        val isCurrent = t == uiState.activeTheme
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(t.primary)
                                .border(
                                    width = if (isCurrent) 1.5.dp else 0.dp,
                                    color = if (isCurrent) Color.White else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { viewModel.selectTheme(t) }
                                .testTag("theme_dot_${t.name}")
                        )
                    }
                }

                // Core online indicator
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF030712))
                        .border(1.dp, theme.primary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 5.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FiberManualRecord,
                        contentDescription = null,
                        tint = theme.secondary,
                        modifier = Modifier.size(5.dp)
                    )
                    Text(
                        text = "ONLINE",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.secondary
                    )
                }
            }
        }
    }
}

private fun getTabIcon(tab: HudTab): ImageVector = when (tab) {
    HudTab.COMMAND_CENTER -> Icons.Default.Home
    HudTab.RAVI_CHAT -> Icons.Default.Terminal
    HudTab.MUSIC_ROOM -> Icons.Default.MusicNote
    HudTab.ARDUINO_STUDIO -> Icons.Default.Memory
    HudTab.STUDY_HUB -> Icons.Default.School
}
