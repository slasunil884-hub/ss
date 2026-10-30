package com.example.ui.screens

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AiModelType
import com.example.data.AnswerDepth
import com.example.data.LanguageMode
import com.example.ui.components.CyberPanel
import com.example.ui.components.CyberStatusBadge
import com.example.ui.components.GlowingKarenOrb
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberError
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.ChatMessage
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState

@Composable
fun NeuralTerminalTab(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val isSinhala = uiState.languageMode == LanguageMode.SINHALA

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                inputText = spokenText
                viewModel.sendAiPrompt(spokenText)
            }
        }
    }

    LaunchedEffect(uiState.chatMessages.size, uiState.currentAiStreamText) {
        if (uiState.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.chatMessages.size)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // AI Core Hologram Orb & Telemetry Card
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GlowingKarenOrb(
                    isListening = uiState.isListeningMic,
                    isThinking = uiState.selectedAiModel == AiModelType.ARGON_THINKING || uiState.isAiStreaming,
                    onClick = {
                        viewModel.speakResponse("RAVI neural link online. Awaiting your instructions, Hero.")
                    }
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "RAVI / SHADOW SUPPORTER CORE",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = theme.primary
                    )
                    Text(
                        text = if (uiState.isAiStreaming) "SYNTHESIZING ANSWER..." else if (uiState.isSpeakingTts) "SPEAKING..." else "READY (සූදානම්)",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 9.sp,
                        color = if (uiState.isAiStreaming) theme.secondary else CyberTextMuted
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CyberStatusBadge(
                            label = "LANG",
                            statusText = uiState.languageMode.code,
                            accentColor = theme.secondary
                        )
                        CyberStatusBadge(
                            label = "MODEL",
                            statusText = uiState.selectedAiModel.badge,
                            accentColor = if (uiState.selectedAiModel == AiModelType.ARGON_THINKING) Color(0xFFFF9100) else theme.primary
                        )
                        if (uiState.isSpeakingTts) {
                            IconButton(
                                onClick = { viewModel.stopSpeaking() },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = "Stop",
                                    tint = CyberError,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Language & Depth Controls Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeonButton(
                text = "LANG: ${uiState.languageMode.label}",
                onClick = { viewModel.toggleLanguage() },
                icon = Icons.Default.Language,
                accentColor = theme.secondary,
                testTag = "toggle_language_btn"
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                AnswerDepth.entries.forEach { d ->
                    val isSel = d == uiState.answerDepth
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) theme.primary.copy(alpha = 0.25f) else CyberDarkSurface)
                            .border(1.dp, if (isSel) theme.primary else Color(0xFF1E293B), RoundedCornerShape(6.dp))
                            .clickable { viewModel.setAnswerDepth(d) }
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = d.name.take(3),
                            fontFamily = OrbitronFontFamily,
                            fontSize = 8.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) Color.White else CyberTextMuted
                        )
                    }
                }
            }
        }

        // Model Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AiModelType.entries.forEach { model ->
                val isSelected = model == uiState.selectedAiModel
                NeonButton(
                    text = model.label,
                    onClick = { viewModel.setAiModel(model) },
                    isActive = isSelected,
                    icon = if (model == AiModelType.ARGON_THINKING) Icons.Default.Psychology else null,
                    accentColor = if (model == AiModelType.ARGON_THINKING) Color(0xFFFF9100) else null,
                    testTag = "model_select_${model.name}"
                )
            }
        }

        // Chat History List
        CyberPanel(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.chatMessages, key = { it.id }) { msg ->
                    ChatBubble(
                        message = msg,
                        onSpeak = { viewModel.speakResponse(msg.text) },
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("RAVI Answer", msg.text))
                            Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                if (uiState.isAiStreaming) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberDarkSurface)
                                .border(1.dp, theme.secondary, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = theme.secondary,
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    text = "[RAVI - STREAMING]:",
                                    fontFamily = OrbitronFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = theme.secondary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.currentAiStreamText.ifEmpty { "Thinking..." },
                                fontSize = 12.sp,
                                color = CyberTextPrimary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                if (uiState.aiError != null) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyberError.copy(alpha = 0.15f))
                                .border(1.dp, CyberError, RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = uiState.aiError ?: "AI Error",
                                fontSize = 11.sp,
                                color = CyberError
                            )
                        }
                    }
                }
            }
        }

        // Input Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = {
                    try {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to RAVI AI...")
                        }
                        speechLauncher.launch(intent)
                    } catch (e: Exception) {
                        viewModel.speakResponse("Speech recognition unavailable on this device.")
                    }
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberDarkSurface)
                    .border(1.dp, theme.primary, RoundedCornerShape(8.dp))
                    .size(44.dp)
                    .testTag("mic_input_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Input",
                    tint = theme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        if (isSinhala) "විමසුමක් හෝ පාඩමක් ලියන්න..." else "Query, invention, or study problem...",
                        fontSize = 11.sp,
                        color = CyberTextMuted
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_text_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = theme.secondary,
                    unfocusedBorderColor = Color(0xFF1E293B),
                    focusedContainerColor = CyberDarkCard,
                    unfocusedContainerColor = CyberDarkCard,
                    focusedTextColor = CyberTextPrimary,
                    unfocusedTextColor = CyberTextPrimary
                ),
                maxLines = 2
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendAiPrompt(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.primary)
                    .size(44.dp)
                    .testTag("send_chat_button")
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

@Composable
fun ChatBubble(
    message: ChatMessage,
    onSpeak: () -> Unit,
    onCopy: () -> Unit
) {
    val theme = LocalKarenTheme.current
    val isUser = message.sender == "USER"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (isUser) "[HERO]" else "[RAVI AI]",
                fontFamily = OrbitronFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                color = if (isUser) Color(0xFFFFB703) else theme.primary
            )
            if (message.modelBadge != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF0F172A))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = message.modelBadge,
                        fontFamily = OrbitronFontFamily,
                        fontSize = 7.sp,
                        color = theme.secondary
                    )
                }
            }
            if (!isUser) {
                IconButton(
                    onClick = onSpeak,
                    modifier = Modifier.size(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Read aloud",
                        tint = theme.secondary,
                        modifier = Modifier.size(12.dp)
                    )
                }
                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = CyberTextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isUser) Color(0xFF0F1D38) else CyberDarkSurface)
                .border(
                    width = 1.dp,
                    color = if (isUser) Color(0x6600D2FF) else theme.primary.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(10.dp)
        ) {
            Text(
                text = message.text,
                fontSize = 12.sp,
                color = CyberTextPrimary,
                lineHeight = 17.sp
            )
        }
    }
}
