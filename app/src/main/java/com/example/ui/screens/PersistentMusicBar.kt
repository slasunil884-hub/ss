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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.HudTab
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState

@Composable
fun PersistentMusicBar(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current

    val infiniteTransition = rememberInfiniteTransition(label = "MiniVisualizer")
    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(350, easing = LinearEasing), RepeatMode.Reverse),
        label = "mw1"
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(420, easing = LinearEasing), RepeatMode.Reverse),
        label = "mw2"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CyberDarkCard.copy(alpha = 0.95f))
            .border(1.dp, theme.primary.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            .clickable { viewModel.selectTab(HudTab.MUSIC_ROOM) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("persistent_music_bar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Shadow Supporter Logo & Track info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
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
                    text = uiState.currentTrack.title,
                    fontFamily = OrbitronFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = CyberTextPrimary,
                    maxLines = 1
                )
                Text(
                    text = "${uiState.currentTrack.artist} • ${uiState.currentTrack.mood.label}",
                    fontSize = 9.sp,
                    color = CyberTextMuted,
                    maxLines = 1
                )
            }
        }

        // Right: Mini Equalizer & Playback Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (uiState.isPlayingMusic) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.height(18.dp)
                ) {
                    listOf(wave1, wave2, wave1 * 0.8f).forEach { h ->
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height((18.dp * h.coerceIn(0.2f, 1f)))
                                .clip(RoundedCornerShape(1.dp))
                                .background(theme.secondary)
                        )
                    }
                }
            }

            IconButton(
                onClick = { viewModel.togglePlayPause() },
                modifier = Modifier.size(32.dp).testTag("mini_play_pause_button")
            ) {
                Icon(
                    imageVector = if (uiState.isPlayingMusic) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = theme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = { viewModel.nextTrack() },
                modifier = Modifier.size(32.dp).testTag("mini_next_track_button")
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = theme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
