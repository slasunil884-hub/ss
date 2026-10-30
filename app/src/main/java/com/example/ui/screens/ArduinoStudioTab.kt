package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ArduinoProjectDatabase
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
fun ArduinoStudioTab(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    val context = LocalContext.current
    var customSketchPrompt by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val project = uiState.activeArduinoProject

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Studio Header
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "ARDUINO & ROBOTICS STUDIO",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = theme.secondary
                    )
                    Text(
                        text = "Real C++ Sketches • BOM • Pin Mapping",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 9.sp,
                        color = theme.primary
                    )
                }
                CyberStatusBadge(
                    label = "BOARD",
                    statusText = project.board,
                    accentColor = Color(0xFFFFD700)
                )
            }
        }

        // Project Presets Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ArduinoProjectDatabase.projects.forEach { proj ->
                val isSelected = proj.id == project.id
                NeonButton(
                    text = proj.title,
                    onClick = { viewModel.selectArduinoProject(proj) },
                    isActive = isSelected,
                    icon = Icons.Default.Memory,
                    testTag = "arduino_proj_${proj.id}"
                )
            }
        }

        // Project Details & Components BOM
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.title.uppercase(),
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = theme.primary
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, theme.secondary, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = project.level.uppercase(),
                            fontFamily = OrbitronFontFamily,
                            fontSize = 8.sp,
                            color = theme.secondary
                        )
                    }
                }

                Text(
                    text = project.description,
                    fontSize = 11.sp,
                    color = CyberTextSecondary,
                    lineHeight = 16.sp
                )

                // Components List
                Text(
                    text = "BILL OF MATERIALS (BOM)",
                    fontFamily = OrbitronFontFamily,
                    fontSize = 10.sp,
                    color = Color(0xFFFFD700)
                )

                project.components.forEach { comp ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = theme.secondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = comp,
                            fontSize = 11.sp,
                            color = CyberTextPrimary
                        )
                    }
                }

                // Pin Mapping
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "PIN CONNECTIONS",
                    fontFamily = OrbitronFontFamily,
                    fontSize = 10.sp,
                    color = theme.secondary
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF030712))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    project.pinMap.forEach { (pin, connection) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = pin,
                                fontFamily = OrbitronFontFamily,
                                fontSize = 10.sp,
                                color = theme.primary
                            )
                            Text(
                                text = "➜ $connection",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
            }
        }

        // Arduino .ino Code Viewer with Copy Button
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SKETCH SOURCE (${project.board}.ino)",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = theme.secondary
                    )
                    NeonButton(
                        text = "COPY CODE",
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Arduino Sketch", project.code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Arduino code copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        icon = Icons.Default.ContentCopy,
                        accentColor = Color(0xFFFFD700),
                        testTag = "copy_arduino_code"
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF020408))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = project.code,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = Color(0xFF38BDF8),
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // AI Custom Sketch Generator Card
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI SKETCH & ROBOTICS GENERATOR",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = theme.primary
                    )
                    CyberStatusBadge(
                        label = "POWERED BY",
                        statusText = "ARGON THINKING",
                        accentColor = Color(0xFFFF9100)
                    )
                }

                Text(
                    text = "Describe your goal, sensors, or microcontroller. RAVI will generate complete C++ code, pin diagrams, and BOM.",
                    fontSize = 11.sp,
                    color = CyberTextSecondary
                )

                OutlinedTextField(
                    value = customSketchPrompt,
                    onValueChange = { customSketchPrompt = it },
                    placeholder = {
                        Text(
                            "e.g., Line follower robot using 2 IR sensors and L298N motor driver...",
                            fontSize = 11.sp,
                            color = CyberTextMuted
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_sketch_prompt_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.secondary,
                        unfocusedBorderColor = Color(0xFF1E293B),
                        focusedContainerColor = CyberDarkCard,
                        unfocusedContainerColor = CyberDarkCard,
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary
                    ),
                    maxLines = 3
                )

                NeonButton(
                    text = "GENERATE ARDUINO SKETCH",
                    onClick = {
                        viewModel.generateAiArduinoSketch(customSketchPrompt)
                        customSketchPrompt = ""
                    },
                    icon = Icons.Default.AutoAwesome,
                    isActive = customSketchPrompt.isNotBlank(),
                    accentColor = theme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "generate_sketch_button"
                )
            }
        }
    }
}
