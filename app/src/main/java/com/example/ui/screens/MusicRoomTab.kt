package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MusicMood
import com.example.ui.components.CyberPanel
import com.example.ui.components.CyberStatusBadge
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState

@Composable
fun MusicRoomTab(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "VinylDisc")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (uiState.isPlayingMusic) 8000 else 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "DiscRot"
    )

    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(380, easing = LinearEasing), RepeatMode.Reverse),
        label = "w1"
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(tween(420, easing = LinearEasing), RepeatMode.Reverse),
        label = "w2"
    )
    val wave3 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(510, easing = LinearEasing), RepeatMode.Reverse),
        label = "w3"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Shadow Supporter Header
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFFFFD700), theme.primary)
                                )
                            )
                            .border(1.5.dp, Color(0xFFFFD700), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SS",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    Column {
                        Text(
                            text = "SHADOW SUPPORTER (SS)",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = Color(0xFFFFD700)
                        )
                        Text(
                            text = "Private Holographic Audio Matrix",
                            fontFamily = OrbitronFontFamily,
                            fontSize = 9.sp,
                            color = theme.secondary
                        )
                    }
                }

                CyberStatusBadge(
                    label = "AUDIO ENGINE",
                    statusText = if (uiState.isPlayingMusic) "ACTIVE" else "IDLE",
                    accentColor = if (uiState.isPlayingMusic) theme.secondary else CyberTextMuted
                )
            }
        }

        // Holographic Vinyl Player Card
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Rotating Holographic Disc with SS Core
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF020408),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                        .border(2.dp, theme.primary, CircleShape)
                        .rotate(discRotation),
                    contentAlignment = Alignment.Center
                ) {
                    // Grooves
                    Box(
                        modifier = Modifier
                            .size(105.dp)
                            .border(1.dp, Color(0x3300D2FF), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(75.dp)
                            .border(1.dp, Color(0x33FF003D), CircleShape)
                    )

                    // Center Label
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700))
                            .border(1.dp, Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SS",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }

                // Track Info
                Text(
                    text = uiState.currentTrack.title,
                    fontFamily = OrbitronFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = CyberTextPrimary
                )
                Text(
                    text = "${uiState.currentTrack.artist} • ${uiState.currentTrack.durationText}",
                    fontSize = 10.sp,
                    color = CyberTextMuted
                )

                // Equalizer Visualizer Bars
                if (uiState.isPlayingMusic) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.height(28.dp)
                    ) {
                        val heights = listOf(wave1, wave2, wave3, wave1 * 0.9f, wave2 * 1.1f, wave3 * 0.8f, wave1 * 0.7f)
                        heights.forEach { h ->
                            Box(
                                modifier = Modifier
                                    .width(6.dp)
                                    .height((28.dp * h.coerceIn(0.2f, 1f)))
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(theme.secondary)
                            )
                        }
                    }
                }

                // Controls: Previous, Play/Pause, Next, Favorite
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.previousTrack() },
                        modifier = Modifier.size(40.dp).testTag("music_prev_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            tint = theme.secondary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(theme.primary)
                            .clickable { viewModel.togglePlayPause() }
                            .testTag("music_main_play_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (uiState.isPlayingMusic) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.nextTrack() },
                        modifier = Modifier.size(40.dp).testTag("music_next_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = theme.secondary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.toggleFavorite(uiState.currentTrack.id) },
                        modifier = Modifier.size(40.dp).testTag("music_fav_btn")
                    ) {
                        Icon(
                            imageVector = if (uiState.currentTrack.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (uiState.currentTrack.isFavorite) Color(0xFFFF003D) else CyberTextMuted,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Mood Filter Chips
        Text(
            text = "MOOD PLAYLISTS (තත්ත්ව වර්ගීකරණය)",
            fontFamily = OrbitronFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = theme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MusicMood.entries.forEach { mood ->
                val isSelected = mood == uiState.activeMoodFilter
                NeonButton(
                    text = mood.label,
                    onClick = { viewModel.filterMood(mood) },
                    isActive = isSelected,
                    accentColor = mood.color,
                    testTag = "mood_filter_${mood.name}"
                )
            }
        }

        // Playlist Tracks List
        Text(
            text = "PLAYLIST (${viewModel.filteredPlaylist().size} TRACKS)",
            fontFamily = OrbitronFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = theme.secondary
        )

        viewModel.filteredPlaylist().forEach { track ->
            val isCurrent = track.id == uiState.currentTrack.id
            CyberPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.playTrack(track) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = if (isCurrent && uiState.isPlayingMusic) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (isCurrent) theme.primary else theme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = track.title,
                                fontFamily = OrbitronFontFamily,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp,
                                color = if (isCurrent) theme.primary else CyberTextPrimary
                            )
                            Text(
                                text = "${track.artist} • ${track.durationText} • ${track.playCount} plays",
                                fontSize = 9.sp,
                                color = CyberTextMuted
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(track.mood.color.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = track.mood.label.uppercase(),
                                fontFamily = OrbitronFontFamily,
                                fontSize = 8.sp,
                                color = track.mood.color
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleFavorite(track.id) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (track.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (track.isFavorite) Color(0xFFFF003D) else CyberTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
