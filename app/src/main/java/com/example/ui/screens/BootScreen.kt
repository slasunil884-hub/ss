package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.KarenOSViewModel

@Composable
fun BootScreen(
    viewModel: KarenOSViewModel,
    progress: Float,
    statusText: String
) {
    val theme = LocalKarenTheme.current
    val infiniteTransition = rememberInfiniteTransition(label = "BootSpin")

    val rotateOuter by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotOuter"
    )

    val rotateInner by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotInner"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Rotating Hologram Reactor Rings
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer ring
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotateOuter)
                ) {
                    drawCircle(
                        color = theme.secondary.copy(alpha = 0.4f),
                        radius = size.minDimension / 2f - 4f,
                        style = Stroke(
                            width = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f), 0f)
                        )
                    )
                }

                // Inner ring
                Canvas(
                    modifier = Modifier
                        .size(150.dp)
                        .rotate(rotateInner)
                ) {
                    drawCircle(
                        color = theme.primary.copy(alpha = 0.5f),
                        radius = size.minDimension / 2f - 4f,
                        style = Stroke(
                            width = 2.5f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 14f), 0f)
                        )
                    )
                }

                // Core Spider Arc Reactor
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    theme.primary.copy(alpha = 0.9f),
                                    theme.secondary.copy(alpha = 0.4f),
                                    Color(0xFF020408)
                                )
                            )
                        )
                        .border(2.dp, theme.secondary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SS",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "SHADOW SUPPORTER",
                fontFamily = OrbitronFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                letterSpacing = 2.sp,
                color = theme.primary
            )

            Text(
                text = "AI COMMAND CENTRE • RAVI v5.0",
                fontFamily = OrbitronFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
                color = theme.secondary
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = statusText,
                fontFamily = OrbitronFontFamily,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = CyberTextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(260.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = theme.primary,
                trackColor = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(36.dp))

            if (progress >= 1f) {
                Button(
                    onClick = { viewModel.launchOS() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = theme.primary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .testTag("launch_os_button")
                        .border(1.dp, theme.secondary, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ENGAGE UNIFIED CORE",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
