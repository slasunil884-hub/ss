package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FlashcardItem
import com.example.data.FlashcardModule
import com.example.data.LanguageMode
import com.example.data.StarkFlashcardDatabase
import com.example.ui.components.CyberPanel
import com.example.ui.components.CyberStatusBadge
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberSuccess
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState

/**
 * Grades 1-13 Study Hub Flashcards View:
 * Features a Grade Selection Dropdown (Grades 1 to 13), subject filters,
 * interactive flipping flashcard studio with mastery tracking, and AI generation.
 */
@Composable
fun StudyHubFlashcardsView(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    val context = LocalContext.current
    val isSinhala = uiState.languageMode == LanguageMode.SINHALA

    // State for selected grade & filters
    var selectedGrade by remember { mutableIntStateOf(0) } // 0 = All Grades
    var selectedGradeBand by remember { mutableStateOf("ALL") }
    var selectedSubjectId by remember { mutableStateOf("all") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Dynamic modules list & mastery tracking
    val allModules = remember { StarkFlashcardDatabase.modules }
    var customAiTopicInput by remember { mutableStateOf("") }
    var masteredCardIds by remember { mutableStateOf(setOf<String>()) }

    // Filter modules by grade & subject
    val filteredModules = remember(selectedGrade, selectedGradeBand, selectedSubjectId) {
        allModules.filter { mod ->
            val matchGrade = if (selectedGrade == 0) {
                when (selectedGradeBand) {
                    "PRIMARY" -> mod.gradeBand == "PRIMARY"
                    "JUNIOR" -> mod.gradeBand == "JUNIOR"
                    "OL" -> mod.gradeBand == "OL"
                    "AL" -> mod.gradeBand == "AL"
                    else -> true
                }
            } else {
                mod.grade == selectedGrade || (selectedGrade in 10..11 && mod.gradeBand == "OL") || (selectedGrade in 12..13 && mod.gradeBand == "AL")
            }
            val matchSubject = selectedSubjectId == "all" || mod.subjectId == selectedSubjectId
            matchGrade && matchSubject
        }
    }

    var activeModuleId by remember { mutableStateOf(allModules.first().id) }
    val currentModule = filteredModules.find { it.id == activeModuleId } ?: filteredModules.firstOrNull() ?: allModules.first()

    // Active Card state in selected module
    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    // Ensure valid index when module changes
    val activeCards = currentModule.cards
    val safeCardIndex = currentCardIndex.coerceIn(0, (activeCards.size - 1).coerceAtLeast(0))
    val currentCard = if (activeCards.isNotEmpty()) activeCards[safeCardIndex] else null

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Grade Selection Dropdown & Grade Band Selector Card
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
                    Column {
                        Text(
                            text = if (isSinhala) "ශ්‍රේණි 1-13 ෆ්ලෑෂ් කාඩ්පත් මධ්‍යස්ථානය" else "GRADES 1-13 FLASHCARD VAULT",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = theme.primary
                        )
                        Text(
                            text = if (isSinhala) "සක්‍රීය ස්මරණය සහ සූත්‍ර ප්‍රගුණ කිරීම" else "Active Recall • Spaced Repetition • Formula Mastery",
                            fontSize = 9.sp,
                            fontFamily = OrbitronFontFamily,
                            color = theme.secondary
                        )
                    }
                    CyberStatusBadge(
                        label = "MASTERED",
                        statusText = "${masteredCardIds.size} CARDS",
                        accentColor = CyberSuccess
                    )
                }

                // Grade Selection Dropdown Box
                Text(
                    text = if (isSinhala) "ශ්‍රේණිය තෝරන්න (GRADE SELECTION)" else "SELECT GRADE (GRADES 1 - 13)",
                    fontFamily = OrbitronFontFamily,
                    fontSize = 9.sp,
                    color = Color(0xFFFFD700)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberDarkCard)
                        .border(1.dp, theme.secondary, RoundedCornerShape(8.dp))
                        .clickable { dropdownExpanded = !dropdownExpanded }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .testTag("grade_selection_dropdown_trigger")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val selectedLabel = StarkFlashcardDatabase.gradeDropdownList.find { it.first == selectedGrade }?.second
                            ?: "All Grades (1-13)"
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = theme.secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = selectedLabel,
                                fontFamily = OrbitronFontFamily,
                                fontSize = 11.sp,
                                color = CyberTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand grade menu",
                            tint = theme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Dropdown Options
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier
                            .background(Color(0xFF030712))
                            .border(1.dp, theme.borderGlow, RoundedCornerShape(8.dp))
                    ) {
                        StarkFlashcardDatabase.gradeDropdownList.forEach { (gradeNum, label) ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = label,
                                        fontFamily = OrbitronFontFamily,
                                        fontSize = 11.sp,
                                        color = if (gradeNum == selectedGrade) theme.primary else CyberTextSecondary
                                    )
                                },
                                onClick = {
                                    selectedGrade = gradeNum
                                    dropdownExpanded = false
                                    currentCardIndex = 0
                                    isFlipped = false
                                    viewModel.audioSynth.playBeep(520f, 40, 0.08f)
                                },
                                modifier = Modifier.testTag("grade_option_$gradeNum")
                            )
                        }
                    }
                }

                // Quick Grade Band Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    StarkFlashcardDatabase.gradeBandOptions.forEach { (key, label) ->
                        val isBandSelected = (selectedGrade == 0 && selectedGradeBand == key)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isBandSelected) theme.secondary.copy(alpha = 0.2f) else CyberDarkSurface)
                                .border(
                                    1.dp,
                                    if (isBandSelected) theme.secondary else Color(0xFF1E293B),
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    selectedGrade = 0
                                    selectedGradeBand = key
                                    currentCardIndex = 0
                                    isFlipped = false
                                    viewModel.audioSynth.playBeep(540f, 30, 0.06f)
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("grade_band_chip_$key")
                        ) {
                            Text(
                                text = label,
                                fontFamily = OrbitronFontFamily,
                                fontSize = 9.sp,
                                color = if (isBandSelected) theme.secondary else CyberTextMuted
                            )
                        }
                    }
                }

                // Subject Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        "all" to if (isSinhala) "සියලු විෂයන්" else "All Subjects",
                        "science" to if (isSinhala) "විද්‍යාව" else "Science",
                        "maths" to if (isSinhala) "ගණිතය" else "Mathematics",
                        "ict" to if (isSinhala) "තොරතුරු තාක්ෂණය" else "ICT",
                        "history" to if (isSinhala) "ඉතිහාසය" else "History",
                        "env" to if (isSinhala) "පරිසරය (5 ශිෂ්‍යත්වය)" else "Env. Studies",
                        "physics" to if (isSinhala) "භෞතික විද්‍යාව (A/L)" else "Physics A/L"
                    ).forEach { (id, label) ->
                        val isSubSelected = selectedSubjectId == id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSubSelected) theme.primary.copy(alpha = 0.25f) else Color(0xFF0F172A))
                                .border(
                                    1.dp,
                                    if (isSubSelected) theme.primary else Color(0xFF1E293B),
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    selectedSubjectId = id
                                    currentCardIndex = 0
                                    isFlipped = false
                                    viewModel.audioSynth.playBeep(580f, 30, 0.06f)
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("subject_filter_chip_$id")
                        ) {
                            Text(
                                text = label,
                                fontFamily = OrbitronFontFamily,
                                fontSize = 9.sp,
                                color = if (isSubSelected) theme.primary else CyberTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // 2. Interactive Flashcard Study Arena / Flipping Card Component
        if (currentCard != null) {
            val isCardMastered = masteredCardIds.contains(currentCard.id)
            val rotation by animateFloatAsState(
                targetValue = if (isFlipped) 180f else 0f,
                animationSpec = tween(durationMillis = 400),
                label = "CardFlipAnimation"
            )

            CyberPanel(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (isCardMastered) CyberSuccess else theme.secondary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Flashcard Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(theme.primary.copy(alpha = 0.2f))
                                    .border(1.dp, theme.primary, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = currentCard.gradeRangeLabel.uppercase(),
                                    fontFamily = OrbitronFontFamily,
                                    fontSize = 8.sp,
                                    color = theme.primary
                                )
                            }
                            Text(
                                text = currentCard.topic,
                                fontFamily = OrbitronFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                            )
                        }

                        Text(
                            text = "CARD ${safeCardIndex + 1}/${activeCards.size}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = CyberTextMuted
                        )
                    }

                    // The Flipping Card Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 190.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 12f * density
                            }
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isFlipped) Color(0xFF030D1E) else Color(0xFF0B1120))
                            .border(
                                1.dp,
                                if (isFlipped) theme.primary else theme.secondary.copy(alpha = 0.7f),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                isFlipped = !isFlipped
                                viewModel.audioSynth.playBeep(if (isFlipped) 680f else 480f, 40, 0.07f)
                            }
                            .padding(16.dp)
                            .testTag("flashcard_interactive_surface"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (rotation <= 90f) {
                            // FRONT OF CARD: Question / Prompt
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "QUESTION / RECALL PROMPT",
                                    fontFamily = OrbitronFontFamily,
                                    fontSize = 8.sp,
                                    color = theme.secondary
                                )

                                Text(
                                    text = currentCard.frontPrompt,
                                    fontFamily = OrbitronFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = CyberTextPrimary,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp
                                )

                                if (currentCard.frontPromptSinhala.isNotBlank()) {
                                    Text(
                                        text = currentCard.frontPromptSinhala,
                                        fontSize = 12.sp,
                                        color = CyberTextSecondary,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 17.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Tap to flip",
                                        tint = theme.primary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = if (isSinhala) "පිළිතුර බැලීමට ක්ලික් කරන්න (TAP TO FLIP)" else "TAP CARD TO REVEAL ANSWER",
                                        fontFamily = OrbitronFontFamily,
                                        fontSize = 8.sp,
                                        color = theme.primary
                                    )
                                }
                            }
                        } else {
                            // BACK OF CARD: Answer & Formula (Flipped)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer { rotationY = 180f },
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "EXPLANATION & ANSWER",
                                        fontFamily = OrbitronFontFamily,
                                        fontSize = 8.sp,
                                        color = CyberSuccess
                                    )
                                    if (isCardMastered) {
                                        Text(
                                            text = "✓ MASTERED",
                                            fontFamily = OrbitronFontFamily,
                                            fontSize = 8.sp,
                                            color = CyberSuccess,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Text(
                                    text = currentCard.backAnswer,
                                    fontSize = 11.sp,
                                    color = CyberTextPrimary,
                                    lineHeight = 16.sp
                                )

                                if (currentCard.backAnswerSinhala.isNotBlank()) {
                                    Text(
                                        text = currentCard.backAnswerSinhala,
                                        fontSize = 11.sp,
                                        color = Color(0xFF93C5FD),
                                        lineHeight = 15.sp
                                    )
                                }

                                if (!currentCard.keyFormulaOrNote.isNullOrBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFF030712))
                                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(4.dp))
                                            .padding(6.dp)
                                    ) {
                                        Text(
                                            text = "💡 ${currentCard.keyFormulaOrNote}",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 9.sp,
                                            color = Color(0xFFFFD700)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Card Action Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Got It / Mastered Button
                        NeonButton(
                            text = if (isCardMastered) "MASTERED" else "GOT IT ✓",
                            onClick = {
                                masteredCardIds = if (isCardMastered) {
                                    masteredCardIds - currentCard.id
                                } else {
                                    masteredCardIds + currentCard.id
                                }
                                viewModel.audioSynth.playDoubleChime()
                                Toast.makeText(
                                    context,
                                    if (!isCardMastered) "Card marked as Mastered! +20 PTS" else "Card returned to review queue",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            icon = Icons.Default.CheckCircle,
                            isActive = isCardMastered,
                            accentColor = CyberSuccess,
                            modifier = Modifier.weight(1f),
                            testTag = "flashcard_master_button"
                        )

                        // Next Card Button
                        NeonButton(
                            text = "NEXT ❯",
                            onClick = {
                                isFlipped = false
                                currentCardIndex = (safeCardIndex + 1) % activeCards.size
                                viewModel.audioSynth.playBeep(600f, 40, 0.07f)
                            },
                            icon = Icons.Default.NavigateNext,
                            accentColor = theme.primary,
                            modifier = Modifier.weight(1f),
                            testTag = "flashcard_next_button"
                        )
                    }

                    // AI Deep Explanation Button
                    NeonButton(
                        text = if (isSinhala) "RAVI AI මඟින් විස්තරාත්මකව ඉගෙන ගන්න" else "AI DEEP EXPLANATION (ASK RAVI)",
                        onClick = {
                            viewModel.askAiStudyTutor("${currentCard.topic}: ${currentCard.frontPrompt}. Explain step-by-step for Sri Lankan exam syllabus.")
                            Toast.makeText(context, "Consulting RAVI AI tutor for ${currentCard.topic}...", Toast.LENGTH_SHORT).show()
                        },
                        icon = Icons.Default.AutoAwesome,
                        accentColor = Color(0xFFFFB703),
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "flashcard_ai_explain_button"
                    )
                }
            }
        }

        // 3. Subject-based Flashcard Modules List
        Text(
            text = if (isSinhala) "විෂය පාදක මොඩියුල ලැයිස්තුව (${filteredModules.size})" else "SUBJECT FLASHCARD MODULES (${filteredModules.size})",
            fontFamily = OrbitronFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = theme.primary
        )

        filteredModules.forEach { mod ->
            val isCurrent = mod.id == currentModule.id
            val moduleMasteredCount = mod.cards.count { masteredCardIds.contains(it.id) }
            val progressFloat = if (mod.cards.isNotEmpty()) moduleMasteredCount.toFloat() / mod.cards.size else 0f

            CyberPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        activeModuleId = mod.id
                        currentCardIndex = 0
                        isFlipped = false
                        viewModel.audioSynth.playBeep(620f, 40, 0.08f)
                    }
                    .testTag("flashcard_module_card_${mod.id}"),
                borderColor = if (isCurrent) theme.primary else null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isSinhala && mod.titleSinhala.isNotBlank()) mod.titleSinhala else mod.title,
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (isCurrent) theme.primary else CyberTextPrimary
                            )
                            Text(
                                text = "${mod.subjectName} • ${mod.gradeBand}",
                                fontFamily = OrbitronFontFamily,
                                fontSize = 9.sp,
                                color = theme.secondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, theme.secondary, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "G-${mod.grade}",
                                fontFamily = OrbitronFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }

                    Text(
                        text = if (isSinhala && mod.descriptionSinhala.isNotBlank()) mod.descriptionSinhala else mod.description,
                        fontSize = 10.sp,
                        color = CyberTextSecondary,
                        lineHeight = 14.sp
                    )

                    // Module Mastery Progress Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { progressFloat },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (progressFloat >= 1.0f) CyberSuccess else theme.secondary,
                            trackColor = Color(0xFF1E293B)
                        )
                        Text(
                            text = "$moduleMasteredCount/${mod.cards.size} Mastered",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = if (progressFloat >= 1.0f) CyberSuccess else CyberTextMuted
                        )
                    }
                }
            }
        }

        // 4. AI Flashcard Generator Card
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
                        text = if (isSinhala) "RAVI AI ෆ්ලෑෂ් කාඩ් උත්පාදකය" else "AI CUSTOM FLASHCARD GENERATOR",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = theme.primary
                    )
                    CyberStatusBadge(
                        label = "POWERED BY",
                        statusText = "ARGON FLASH",
                        accentColor = Color(0xFFFF9100)
                    )
                }

                Text(
                    text = if (isSinhala)
                        "ඕනෑම ශ්‍රේණියක පාඩමක් හෝ සූත්‍රයක් ඇතුළත් කරන්න. RAVI AI විසින් සක්‍රීය ස්මරණ කාඩ්පතක් සාදනු ඇත."
                    else
                        "Enter any syllabus topic or formula (Grades 1-13). RAVI AI creates high-yield recall flashcards.",
                    fontSize = 11.sp,
                    color = CyberTextSecondary
                )

                OutlinedTextField(
                    value = customAiTopicInput,
                    onValueChange = { customAiTopicInput = it },
                    placeholder = {
                        Text(
                            text = if (isSinhala) "උදා: 11 ශ්‍රේණිය විද්‍යාව - ප්‍රභාසංශ්ලේෂණයේ ආලෝක ප්‍රතික්‍රියාව..." else "e.g., Grade 11 Science - Chemical bonding and electronegativity...",
                            fontSize = 10.sp,
                            color = CyberTextMuted
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_flashcard_topic_input"),
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

                NeonButton(
                    text = if (isSinhala) "AI ෆ්ලෑෂ් කාඩ්පතක් සාදන්න" else "GENERATE AI FLASHCARD",
                    onClick = {
                        if (customAiTopicInput.isNotBlank()) {
                            viewModel.askAiStudyTutor("Generate active recall flashcard for: $customAiTopicInput with front question, back answer, and Sri Lankan exam tips.")
                            Toast.makeText(context, "Flashcard query dispatched to RAVI!", Toast.LENGTH_SHORT).show()
                            customAiTopicInput = ""
                        }
                    },
                    icon = Icons.Default.AutoAwesome,
                    isActive = customAiTopicInput.isNotBlank(),
                    accentColor = theme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "generate_ai_flashcard_button"
                )
            }
        }
    }
}
