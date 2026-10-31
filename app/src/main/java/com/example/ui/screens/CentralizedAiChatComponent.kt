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
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
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
import com.example.viewmodel.HudTab
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Hub context targets for centralized routing
 */
enum class ChatHubContext(
    val id: String,
    val label: String,
    val labelSinhala: String,
    val accentColor: Color,
    val targetTab: HudTab?
) {
    ALL("ALL", "ALL HUBS", "සියලු මධ්‍යස්ථාන", Color(0xFF00D2FF), null),
    STUDY_HUB("STUDY_HUB", "STUDY HUB (1-13)", "අධ්‍යයන මධ්‍යස්ථානය", Color(0xFF06D6A0), HudTab.STUDY_HUB),
    ARDUINO_STUDIO("ARDUINO_STUDIO", "ARDUINO & ROBOTICS", "ආර්ඩුයිනෝ විද්‍යාගාරය", Color(0xFFFFB703), HudTab.ARDUINO_STUDIO),
    MUSIC_ROOM("MUSIC_ROOM", "SS MUSIC ROOM", "සංගීත මැදිරිය", Color(0xFFFF003D), HudTab.MUSIC_ROOM),
    COMMAND_CENTER("COMMAND_CENTER", "COMMAND & SUIT", "ප්‍රධාන මධ්‍යස්ථානය", Color(0xFFFFD700), HudTab.COMMAND_CENTER)
}

/**
 * Centralized AI Chat component for communicating with the Shadow Supporter (RAVI)
 * assistant, integrating the Gemini API to handle queries across the different hubs.
 */
@Composable
fun CentralizedAiChatComponent(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier,
    initialHubContext: ChatHubContext = ChatHubContext.ALL
) {
    val theme = LocalKarenTheme.current
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    var selectedHub by remember { mutableStateOf(initialHubContext) }
    val listState = rememberLazyListState()

    val isSinhala = uiState.languageMode == LanguageMode.SINHALA

    // Voice recognition launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                inputText = spokenText
                viewModel.sendAiPrompt(
                    prompt = spokenText,
                    hubContext = if (selectedHub == ChatHubContext.ALL) null else selectedHub.id
                )
            }
        }
    }

    // Auto-scroll on new message or streaming chunk
    LaunchedEffect(uiState.chatMessages.size, uiState.currentAiStreamText) {
        val totalItems = uiState.chatMessages.size + if (uiState.isAiStreaming) 1 else 0
        if (totalItems > 0) {
            listState.animateScrollToItem(totalItems - 1)
        }
    }

    // Hub query suggestions
    val suggestions = when (selectedHub) {
        ChatHubContext.ALL -> listOf(
            "What can Shadow Supporter do for me today?",
            "Explain multi-device sync across PC, Mobile, and Watch",
            "Bilingual voice command guide in Sinhala & English"
        )
        ChatHubContext.STUDY_HUB -> listOf(
            "Explain Newton's Laws with Sinhala & English notes",
            "Give 5 Grade 11 Science exam revision tips",
            "Key formulas for G.C.E. O/L Mathematics"
        )
        ChatHubContext.ARDUINO_STUDIO -> listOf(
            "Write an Arduino ultrasonic sensor sketch with buzzer",
            "Pin mapping for ESP32 and I2C OLED display",
            "Troubleshoot servo motor jitter in Arduino C++"
        )
        ChatHubContext.MUSIC_ROOM -> listOf(
            "Explain 432Hz binaural focus frequencies",
            "Recommend music mood for late-night programming",
            "How does Shadow Supporter audio synthesis work?"
        )
        ChatHubContext.COMMAND_CENTER -> listOf(
            "Suit nanotech diagnostics and power rig status",
            "Calculate web-fluid tensile strength for Spidey suit",
            "Switch to Spider-Man cyber neon color mode"
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // --- 1. AI Telemetry & Multi-Hub Banner ---
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GlowingKarenOrb(
                            isThinking = uiState.isAiStreaming,
                            modifier = Modifier.size(52.dp)
                        )
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "SHADOW SUPPORTER",
                                    fontFamily = OrbitronFontFamily,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp,
                                    color = Color(0xFFFFD700)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(theme.primary)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "SS AI",
                                        fontFamily = OrbitronFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                            Text(
                                text = "Centralized Gemini Multi-Hub Intelligence",
                                fontFamily = OrbitronFontFamily,
                                fontSize = 9.sp,
                                color = theme.secondary
                            )
                        }
                    }

                    // Model and Clear Actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CyberStatusBadge(
                            label = "MODEL",
                            statusText = uiState.selectedAiModel.badge,
                            accentColor = theme.secondary
                        )

                        IconButton(
                            onClick = { viewModel.clearChatHistory() },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("centralized_ai_clear_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear Chat History",
                                tint = CyberTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Controls row: Model, Language, and Depth Toggles
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gemini Model selector chips
                    AiModelType.entries.forEach { model ->
                        val isSelected = model == uiState.selectedAiModel
                        NeonButton(
                            text = model.label,
                            onClick = { viewModel.setAiModel(model) },
                            isActive = isSelected,
                            accentColor = if (model == AiModelType.ARGON_THINKING) Color(0xFFFFB703) else theme.primary,
                            testTag = "model_chip_${model.name}"
                        )
                    }

                    // Language toggle
                    NeonButton(
                        text = "🌐 ${uiState.languageMode.label}",
                        onClick = { viewModel.toggleLanguage() },
                        isActive = false,
                        accentColor = theme.secondary,
                        testTag = "lang_toggle_chip"
                    )

                    // Answer depth toggle
                    AnswerDepth.entries.forEach { depth ->
                        val isSelected = depth == uiState.answerDepth
                        NeonButton(
                            text = depth.label,
                            onClick = { viewModel.setAnswerDepth(depth) },
                            isActive = isSelected,
                            accentColor = Color(0xFF00D2FF),
                            testTag = "depth_chip_${depth.name}"
                        )
                    }
                }
            }
        }

        // --- 2. Cross-Hub Context Selector ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ChatHubContext.entries.forEach { hub ->
                val isSelected = hub == selectedHub
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) hub.accentColor.copy(alpha = 0.22f) else CyberDarkSurface)
                        .border(
                            width = if (isSelected) 1.5.dp else 0.5.dp,
                            color = if (isSelected) hub.accentColor else theme.borderGlow,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedHub = hub }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("hub_filter_${hub.id}")
                ) {
                    Text(
                        text = if (isSinhala) hub.labelSinhala else hub.label,
                        fontFamily = OrbitronFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 9.sp,
                        color = if (isSelected) hub.accentColor else CyberTextMuted
                    )
                }
            }
        }

        // --- 3. Hub Quick Prompt Suggestions ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            suggestions.forEachIndexed { idx, suggestion ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(CyberDarkCard)
                        .border(0.5.dp, theme.secondary.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .clickable {
                            inputText = suggestion
                            viewModel.sendAiPrompt(
                                prompt = suggestion,
                                hubContext = if (selectedHub == ChatHubContext.ALL) null else selectedHub.id
                            )
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("suggestion_chip_$idx")
                ) {
                    Text(
                        text = "⚡ $suggestion",
                        fontSize = 9.sp,
                        color = CyberTextSecondary,
                        maxLines = 1
                    )
                }
            }
        }

        // --- 4. Chat Messages Conversation List ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("centralized_ai_chat_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.chatMessages, key = { it.id }) { msg ->
                    CentralizedChatMessageBubble(
                        message = msg,
                        isSpeaking = uiState.isSpeakingTts,
                        onSpeak = { text -> viewModel.speakResponse(text) },
                        onStopSpeak = { viewModel.stopSpeaking() },
                        onNavigateToHub = { targetTab -> viewModel.selectTab(targetTab) },
                        context = context
                    )
                }

                // Streaming in-progress response bubble
                if (uiState.isAiStreaming) {
                    item {
                        CyberPanel(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 24.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(14.dp),
                                        strokeWidth = 2.dp,
                                        color = theme.primary
                                    )
                                    Text(
                                        text = "RAVI AI STREAMING • ${uiState.selectedAiModel.badge}",
                                        fontFamily = OrbitronFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = theme.primary
                                    )
                                }

                                Text(
                                    text = uiState.currentAiStreamText.ifBlank { "Thinking and reasoning across hubs..." } + " █",
                                    fontSize = 11.sp,
                                    color = CyberTextPrimary,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }

                // Error Banner
                if (uiState.aiError != null) {
                    item {
                        CyberPanel(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, CyberError, RoundedCornerShape(8.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "SYSTEM ADVISORY / පද්ධති දැන්වීම",
                                    fontFamily = OrbitronFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = CyberError
                                )
                                Text(
                                    text = uiState.aiError,
                                    fontSize = 10.sp,
                                    color = CyberTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 5. Centralized Chat Input Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Voice Input (Speech-to-Text) Button
            IconButton(
                onClick = {
                    try {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Shadow Supporter (SS)...")
                        }
                        speechLauncher.launch(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Voice recognition not supported on this device.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(CyberDarkSurface)
                    .border(1.dp, theme.secondary, CircleShape)
                    .testTag("centralized_ai_mic_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Input",
                    tint = theme.secondary,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Input Text Field
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("centralized_ai_input_field"),
                placeholder = {
                    Text(
                        text = when (selectedHub) {
                            ChatHubContext.STUDY_HUB -> "Ask Grade 1-13 syllabus questions..."
                            ChatHubContext.ARDUINO_STUDIO -> "Ask for Arduino sketches or pin maps..."
                            ChatHubContext.MUSIC_ROOM -> "Ask about frequencies, playlists..."
                            ChatHubContext.COMMAND_CENTER -> "Ask suit telemetry or system commands..."
                            ChatHubContext.ALL -> "Ask Shadow Supporter across all hubs..."
                        },
                        fontSize = 11.sp,
                        color = CyberTextMuted,
                        maxLines = 1
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
                shape = RoundedCornerShape(10.dp)
            )

            // Send Prompt Button
            IconButton(
                onClick = {
                    if (inputText.isNotBlank() && !uiState.isAiStreaming) {
                        val promptToSend = inputText
                        inputText = ""
                        viewModel.sendAiPrompt(
                            prompt = promptToSend,
                            hubContext = if (selectedHub == ChatHubContext.ALL) null else selectedHub.id
                        )
                    }
                },
                enabled = inputText.isNotBlank() && !uiState.isAiStreaming,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (inputText.isNotBlank()) theme.primary else CyberDarkSurface)
                    .testTag("centralized_ai_send_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Prompt",
                    tint = if (inputText.isNotBlank()) Color.White else CyberTextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Renders individual chat bubbles with rich formatting, code copy, voice synthesis,
 * and direct actionable hub navigation.
 */
@Composable
private fun CentralizedChatMessageBubble(
    message: ChatMessage,
    isSpeaking: Boolean,
    onSpeak: (String) -> Unit,
    onStopSpeak: () -> Unit,
    onNavigateToHub: (HudTab) -> Unit,
    context: Context
) {
    val theme = LocalKarenTheme.current
    val isUser = message.sender == "USER"
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(message.timestamp) { timeFormat.format(Date(message.timestamp)) }

    // Detect actionable hub mentions
    val hasArduinoCode = message.text.contains("void setup", ignoreCase = true) ||
            message.text.contains("pinMode", ignoreCase = true) ||
            message.text.contains("#include <", ignoreCase = true)

    val hasStudyTopic = message.text.contains("Grade ", ignoreCase = true) ||
            message.text.contains("Formula:", ignoreCase = true) ||
            message.text.contains("G.C.E.", ignoreCase = true)

    val hasMusicReference = message.text.contains("Binaural", ignoreCase = true) ||
            message.text.contains("Hz", ignoreCase = true) ||
            message.text.contains("Playlist", ignoreCase = true)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        CyberPanel(
            modifier = Modifier
                .fillMaxWidth(if (isUser) 0.85f else 0.95f)
                .border(
                    width = 1.dp,
                    color = if (isUser) theme.secondary.copy(alpha = 0.4f) else theme.primary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Header: Sender Badge + Model Badge + Hub Tag + Timestamp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isUser) theme.secondary.copy(alpha = 0.2f) else theme.primary.copy(alpha = 0.25f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isUser) "HERO" else "RAVI AI",
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = if (isUser) theme.secondary else theme.primary
                            )
                        }

                        if (!isUser && message.modelBadge != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = message.modelBadge,
                                    fontFamily = OrbitronFontFamily,
                                    fontSize = 7.sp,
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }

                        if (message.hubTag != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFF0F172A))
                                    .border(0.5.dp, theme.secondary, RoundedCornerShape(3.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = message.hubTag,
                                    fontFamily = OrbitronFontFamily,
                                    fontSize = 7.sp,
                                    color = theme.secondary
                                )
                            }
                        }
                    }

                    Text(
                        text = formattedTime,
                        fontSize = 9.sp,
                        color = CyberTextMuted
                    )
                }

                // Message Body
                Text(
                    text = message.text,
                    fontSize = 11.sp,
                    color = if (isUser) CyberTextSecondary else CyberTextPrimary,
                    lineHeight = 17.sp
                )

                // Assistant Message Actions (Copy, Speak, Hub Navigation)
                if (!isUser) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Actionable Hub routing buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (hasArduinoCode) {
                                NeonButton(
                                    text = "→ ARDUINO STUDIO",
                                    onClick = { onNavigateToHub(HudTab.ARDUINO_STUDIO) },
                                    accentColor = Color(0xFFFFB703),
                                    testTag = "nav_to_arduino_btn"
                                )
                            }
                            if (hasStudyTopic) {
                                NeonButton(
                                    text = "→ STUDY HUB",
                                    onClick = { onNavigateToHub(HudTab.STUDY_HUB) },
                                    accentColor = Color(0xFF06D6A0),
                                    testTag = "nav_to_study_btn"
                                )
                            }
                            if (hasMusicReference) {
                                NeonButton(
                                    text = "→ MUSIC ROOM",
                                    onClick = { onNavigateToHub(HudTab.MUSIC_ROOM) },
                                    accentColor = Color(0xFFFF003D),
                                    testTag = "nav_to_music_btn"
                                )
                            }
                        }

                        // Utility Buttons: Copy and Text-to-Speech
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("RAVI Response", message.text))
                                    Toast.makeText(context, "Copied response to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp).testTag("copy_msg_${message.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Response",
                                    tint = CyberTextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (isSpeaking) {
                                        onStopSpeak()
                                    } else {
                                        onSpeak(message.text)
                                    }
                                },
                                modifier = Modifier.size(32.dp).testTag("tts_msg_${message.id}")
                            ) {
                                Icon(
                                    imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                    contentDescription = if (isSpeaking) "Stop Speaking" else "Speak Message",
                                    tint = if (isSpeaking) theme.primary else CyberTextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
