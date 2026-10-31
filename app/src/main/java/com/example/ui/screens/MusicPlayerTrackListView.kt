package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MusicTrack
import com.example.ui.components.CyberPanel
import com.example.ui.components.CyberStatusBadge
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.MusicMood
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState
import java.util.Locale

/**
 * Dedicated Compose component for the 'Music Room' section featuring:
 * 1. An interactive Seek Bar with live mm:ss timestamps and draggable position.
 * 2. Prominent Play/Pause button with transport controls (Previous, Next, Favorite).
 * 3. Searchable and mood-filtered list of audio tracks for user selection.
 */
@Composable
fun MusicPlayerTrackListView(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    var searchQuery by remember { mutableStateOf("") }
    var isLooping by remember { mutableStateOf(false) }

    val currentTrack = uiState.currentTrack
    val totalSeconds = currentTrack.totalSeconds.toFloat().coerceAtLeast(1f)
    val currentPosition = uiState.currentPositionSeconds.coerceIn(0f, totalSeconds)

    // Equalizer animation
    val infiniteTransition = rememberInfiniteTransition(label = "PlayerEq")
    val eq1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(320, easing = LinearEasing), RepeatMode.Reverse),
        label = "eq1"
    )
    val eq2 by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(410, easing = LinearEasing), RepeatMode.Reverse),
        label = "eq2"
    )
    val eq3 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(270, easing = LinearEasing), RepeatMode.Reverse),
        label = "eq3"
    )

    // Filtered track list
    val displayedTracks = uiState.playlist.filter { track ->
        val matchesMood = uiState.activeMoodFilter == MusicMood.ALL || track.mood == uiState.activeMoodFilter
        val matchesFav = !uiState.isFavoriteOnly || track.isFavorite
        val matchesSearch = searchQuery.isBlank() ||
                track.title.contains(searchQuery, ignoreCase = true) ||
                track.artist.contains(searchQuery, ignoreCase = true)
        matchesMood && matchesFav && matchesSearch
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- 1. Now Playing & Interactive Seek Bar Card ---
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header with Shadow Supporter (SS) branding
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(theme.primary)
                                .border(1.dp, Color(0xFFFFD700), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SS",
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                color = Color.Black
                            )
                        }

                        Column {
                            Text(
                                text = "NOW PLAYING / දැන් වාදනය වේ",
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = theme.primary
                            )
                            Text(
                                text = currentTrack.title,
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = CyberTextPrimary,
                                maxLines = 1
                            )
                        }
                    }

                    CyberStatusBadge(
                        label = "MOOD",
                        statusText = currentTrack.mood.label.uppercase(),
                        accentColor = currentTrack.mood.color
                    )
                }

                // Subtitle: Artist & Synthesizer Frequency
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${currentTrack.artist} • Synth: ${String.format(Locale.US, "%.1f", currentTrack.baseFrequency)}Hz",
                        fontSize = 10.sp,
                        color = CyberTextMuted
                    )

                    // Mini live equalizer
                    if (uiState.isPlayingMusic) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.height(16.dp)
                        ) {
                            listOf(eq1, eq2, eq3, eq2 * 0.8f, eq1 * 1.1f).forEach { h ->
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height((16.dp * h.coerceIn(0.2f, 1f)))
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(theme.secondary)
                                )
                            }
                        }
                    }
                }

                // --- Seek Bar Component ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Slider(
                        value = currentPosition,
                        onValueChange = { newSec ->
                            viewModel.seekToPosition(newSec)
                        },
                        valueRange = 0f..totalSeconds,
                        colors = SliderDefaults.colors(
                            thumbColor = theme.primary,
                            activeTrackColor = theme.primary,
                            inactiveTrackColor = CyberDarkSurface,
                            activeTickColor = theme.secondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .testTag("music_seek_bar")
                    )

                    // Timestamps: Current Position vs Total Duration
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTime(currentPosition),
                            fontFamily = OrbitronFontFamily,
                            fontSize = 10.sp,
                            color = theme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "SEEK • ${((currentPosition / totalSeconds) * 100).toInt()}%",
                            fontFamily = OrbitronFontFamily,
                            fontSize = 8.sp,
                            color = CyberTextMuted
                        )
                        Text(
                            text = currentTrack.durationText,
                            fontFamily = OrbitronFontFamily,
                            fontSize = 10.sp,
                            color = CyberTextSecondary
                        )
                    }
                }

                // --- Transport Controls: Previous, Play/Pause, Next, Loop, Favorite ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Loop / Repeat Toggle
                    IconButton(
                        onClick = { isLooping = !isLooping },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("music_loop_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Loop Track",
                            tint = if (isLooping) theme.secondary else CyberTextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Previous Track Button
                    IconButton(
                        onClick = { viewModel.previousTrack() },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("music_previous_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous Track",
                            tint = theme.secondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Prominent Play/Pause Button
                    val playButtonBorderColor by animateColorAsState(
                        targetValue = if (uiState.isPlayingMusic) Color(0xFFFFD700) else theme.primary,
                        label = "playBtnBorder"
                    )

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        theme.primary,
                                        theme.primary.copy(alpha = 0.85f)
                                    )
                                )
                            )
                            .border(2.dp, playButtonBorderColor, CircleShape)
                            .clickable { viewModel.togglePlayPause() }
                            .testTag("music_play_pause_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (uiState.isPlayingMusic) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (uiState.isPlayingMusic) "Pause Music" else "Play Music",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Next Track Button
                    IconButton(
                        onClick = { viewModel.nextTrack() },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("music_next_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next Track",
                            tint = theme.secondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Favorite Toggle
                    IconButton(
                        onClick = { viewModel.toggleFavorite(currentTrack.id) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("music_fav_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (currentTrack.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle Favorite",
                            tint = if (currentTrack.isFavorite) Color(0xFFFF003D) else CyberTextMuted,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // --- 2. Track Filter & Search Section ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SELECT AUDIO TRACK (ධාවන පථයක් තෝරන්න)",
                    fontFamily = OrbitronFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = theme.secondary
                )

                Text(
                    text = "${displayedTracks.size} OF ${uiState.playlist.size}",
                    fontFamily = OrbitronFontFamily,
                    fontSize = 9.sp,
                    color = CyberTextMuted
                )
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("music_search_input"),
                placeholder = {
                    Text(
                        text = "Search tracks, artists, cyber themes...",
                        fontSize = 11.sp,
                        color = CyberTextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = theme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = theme.primary,
                    unfocusedBorderColor = theme.primary.copy(alpha = 0.4f),
                    focusedContainerColor = CyberDarkCard,
                    unfocusedContainerColor = CyberDarkCard,
                    focusedTextColor = CyberTextPrimary,
                    unfocusedTextColor = CyberTextPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Mood Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Favorites Only Filter Toggle
                NeonButton(
                    text = if (uiState.isFavoriteOnly) "★ FAVORITES (ON)" else "★ FAVORITES",
                    onClick = { viewModel.toggleFavoriteOnly() },
                    isActive = uiState.isFavoriteOnly,
                    accentColor = Color(0xFFFFD700),
                    testTag = "filter_favorites_chip"
                )

                MusicMood.entries.forEach { mood ->
                    val isSelected = mood == uiState.activeMoodFilter
                    NeonButton(
                        text = mood.label,
                        onClick = { viewModel.filterMood(mood) },
                        isActive = isSelected,
                        accentColor = mood.color,
                        testTag = "filter_mood_${mood.name}"
                    )
                }
            }
        }

        // --- 3. Audio Track Selector List ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (displayedTracks.isEmpty()) {
                CyberPanel(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NO TRACKS FOUND MATCHING FILTER / ගීත හමු නොවීය",
                            fontFamily = OrbitronFontFamily,
                            fontSize = 10.sp,
                            color = CyberTextMuted
                        )
                    }
                }
            } else {
                displayedTracks.forEach { track ->
                    val isSelected = track.id == currentTrack.id
                    val isPlaying = isSelected && uiState.isPlayingMusic

                    CyberPanel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (isSelected) 1.5.dp else 0.dp,
                                color = if (isSelected) theme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.playTrack(track) }
                            .testTag("track_item_${track.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: Play state button and track details
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                // Item Play/Pause indicator button (48dp min interactive size)
                                IconButton(
                                    onClick = {
                                        if (isSelected) {
                                            viewModel.togglePlayPause()
                                        } else {
                                            viewModel.playTrack(track)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("track_play_button_${track.id}")
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) theme.primary.copy(alpha = 0.25f)
                                                else CyberDarkSurface
                                            )
                                            .border(
                                                1.dp,
                                                if (isSelected) theme.primary else theme.secondary.copy(alpha = 0.4f),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = if (isPlaying) "Pause ${track.title}" else "Play ${track.title}",
                                            tint = if (isSelected) theme.primary else theme.secondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = track.title,
                                            fontFamily = OrbitronFontFamily,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 11.sp,
                                            color = if (isSelected) theme.primary else CyberTextPrimary,
                                            maxLines = 1
                                        )

                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(theme.primary)
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = if (isPlaying) "PLAYING" else "PAUSED",
                                                    fontFamily = OrbitronFontFamily,
                                                    fontSize = 7.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = "${track.artist} • ${track.durationText} • ${track.playCount} plays",
                                        fontSize = 9.sp,
                                        color = CyberTextMuted
                                    )
                                }
                            }

                            // Right: Mood Badge and Favorite Toggle
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(track.mood.color.copy(alpha = 0.18f))
                                        .border(0.5.dp, track.mood.color.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
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
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("track_fav_button_${track.id}")
                                ) {
                                    Icon(
                                        imageVector = if (track.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite ${track.title}",
                                        tint = if (track.isFavorite) Color(0xFFFF003D) else CyberTextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(seconds: Float): String {
    val total = seconds.toInt().coerceAtLeast(0)
    val mins = total / 60
    val secs = total % 60
    return String.format(Locale.US, "%02d:%02d", mins, secs)
}
