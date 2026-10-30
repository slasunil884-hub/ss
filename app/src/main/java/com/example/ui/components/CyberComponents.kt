package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.OrbitronFontFamily

@Composable
fun CyberPanel(
    modifier: Modifier = Modifier,
    borderColor: Color? = null,
    cornerRounding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    val theme = LocalKarenTheme.current
    val strokeColor = borderColor ?: theme.borderGlow

    Surface(
        modifier = modifier
            .border(
                border = BorderStroke(1.dp, strokeColor),
                shape = RoundedCornerShape(cornerRounding)
            ),
        shape = RoundedCornerShape(cornerRounding),
        color = CyberDarkCard.copy(alpha = 0.92f)
    ) {
        content()
    }
}

@Composable
fun GlowingKarenOrb(
    isListening: Boolean = false,
    isThinking: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    val infiniteTransition = rememberInfiniteTransition(label = "OrbAnim")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isThinking) 700 else 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    val rotateCw by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isThinking) 6000 else 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotCw"
    )

    val rotateCcw by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isThinking) 4500 else 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotCcw"
    )

    val primaryColor = if (isListening) Color(0xFF00E5FF) else if (isThinking) Color(0xFFFF9100) else theme.primary
    val secondaryColor = if (isListening) Color(0xFFFF1744) else theme.secondary

    Box(
        modifier = modifier
            .size(110.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 55.dp),
                onClick = onClick
            )
            .testTag("karen_hologram_orb"),
        contentAlignment = Alignment.Center
    ) {
        // Outer dashed rotating ring
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .rotate(rotateCw)
        ) {
            drawCircle(
                color = primaryColor.copy(alpha = 0.35f),
                radius = size.minDimension / 2f - 4f,
                style = Stroke(
                    width = 1.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                )
            )
        }

        // Inner reverse dashed ring
        Canvas(
            modifier = Modifier
                .size(86.dp)
                .rotate(rotateCcw)
        ) {
            drawCircle(
                color = secondaryColor.copy(alpha = 0.45f),
                radius = size.minDimension / 2f - 3f,
                style = Stroke(
                    width = 1.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f)
                )
            )
        }

        // Glowing Core Sphere
        Box(
            modifier = Modifier
                .size(62.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.85f),
                            secondaryColor.copy(alpha = 0.45f),
                            CyberDarkSurface
                        )
                    )
                )
                .border(2.dp, primaryColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isThinking) "ARGON" else if (isListening) "LIVE" else "KAREN",
                    fontFamily = OrbitronFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    color = Color.White
                )
                Text(
                    text = "v5.0",
                    fontFamily = OrbitronFontFamily,
                    fontSize = 7.sp,
                    color = primaryColor
                )
            }
        }
    }
}

@Composable
fun CyberStatusBadge(
    label: String,
    statusText: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF030712))
            .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = Icons.Default.FiberManualRecord,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(8.dp)
        )
        if (label.isNotEmpty()) {
            Text(
                text = "$label: ",
                fontSize = 9.sp,
                fontFamily = OrbitronFontFamily,
                color = CyberTextMuted
            )
        }
        Text(
            text = statusText,
            fontSize = 9.sp,
            fontFamily = OrbitronFontFamily,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
    }
}

@Composable
fun CyberStatBar(
    name: String,
    value: Int,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name.uppercase(),
                fontFamily = OrbitronFontFamily,
                fontSize = 9.sp,
                color = CyberTextPrimary
            )
            Text(
                text = "$value%",
                fontFamily = OrbitronFontFamily,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF0F172A))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (value / 100f).coerceIn(0f, 1f))
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(accentColor.copy(alpha = 0.6f), accentColor)
                        )
                    )
            )
        }
    }
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isActive: Boolean = false,
    accentColor: Color? = null,
    testTag: String = "neon_button"
) {
    val theme = LocalKarenTheme.current
    val color = accentColor ?: if (isActive) theme.primary else theme.secondary

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = if (isActive) color else color.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            )
            .testTag(testTag),
        color = if (isActive) color.copy(alpha = 0.2f) else CyberDarkSurface.copy(alpha = 0.85f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = text,
                fontFamily = OrbitronFontFamily,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                fontSize = 10.sp,
                color = if (isActive) Color.White else CyberTextPrimary
            )
        }
    }
}
