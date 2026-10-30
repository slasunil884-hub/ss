package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LanguageMode
import com.example.data.StarkStudyDatabase
import com.example.ui.components.CyberPanel
import com.example.ui.components.CyberStatusBadge
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberDarkCard
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberError
import com.example.ui.theme.CyberSuccess
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.LocalKarenTheme
import com.example.ui.theme.OrbitronFontFamily
import com.example.viewmodel.KarenOSViewModel
import com.example.viewmodel.KarenOsUiState

@Composable
fun StudyHubTab(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    val scrollState = rememberScrollState()
    val subject = uiState.activeSubject
    val isSinhala = uiState.languageMode == LanguageMode.SINHALA

    var groupChatText by remember { mutableStateOf("") }
    var joinKeyInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isSinhala) "ශ්‍රේණි 1-13 පාඩම් සහායක" else "GRADES 1-13 STUDY HUB",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = theme.primary
                    )
                    Text(
                        text = "${subject.name} • ${subject.gradeRange}",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 9.sp,
                        color = theme.secondary
                    )
                }
                CyberStatusBadge(
                    label = "SCORE",
                    statusText = "${uiState.quizScore} PTS",
                    accentColor = Color(0xFFFFB703)
                )
            }
        }

        // Subject Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StarkStudyDatabase.subjects.forEach { s ->
                val isSelected = s.id == subject.id
                NeonButton(
                    text = if (isSinhala) s.nameSinhala else s.name,
                    onClick = { viewModel.selectSubject(s) },
                    isActive = isSelected,
                    accentColor = if (isSelected) theme.secondary else null,
                    icon = Icons.Default.School,
                    testTag = "subject_select_${s.id}"
                )
            }
        }

        // Study Hub Sub-Navigation (Daily Planner [Room DB], Notes, Quiz, Groups)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Triple("PLANNER", if (isSinhala) "දෛනික සැලැස්ම (ROOM DB)" else "DAILY PLANNER (ROOM DB)", Icons.Default.CalendarToday),
                Triple("NOTES", if (isSinhala) "සටහන් සහ සූත්‍ර" else "NOTES & FORMULAS", Icons.Default.School),
                Triple("QUIZ", if (isSinhala) "ප්‍රශ්නාවලිය" else "QUIZ CHALLENGE", Icons.Default.CheckCircle),
                Triple("GROUPS", if (isSinhala) "අධ්‍යයන කණ්ඩායම්" else "STUDY GROUPS", Icons.Default.Group)
            ).forEach { (key, label, icon) ->
                val isSelected = uiState.activeStudySubTab == key
                NeonButton(
                    text = label,
                    onClick = { viewModel.setActiveStudySubTab(key) },
                    isActive = isSelected,
                    accentColor = if (isSelected) theme.primary else null,
                    icon = icon,
                    testTag = "study_subtab_$key"
                )
            }
        }

        // 1. Daily Planner (Room DB Powered)
        if (uiState.activeStudySubTab == "PLANNER") {
            DailyPlannerView(
                viewModel = viewModel,
                uiState = uiState
            )
        }

        // 2. Revision Topics & Formula Sheets
        if (uiState.activeStudySubTab == "NOTES") {
            subject.topics.forEach { topic ->
            CyberPanel(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isSinhala && topic.titleSinhala.isNotBlank()) topic.titleSinhala else topic.title.uppercase(),
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = theme.secondary
                        )
                        NeonButton(
                            text = "AI TUTOR",
                            onClick = { viewModel.askAiStudyTutor(topic.title) },
                            icon = Icons.Default.AutoAwesome,
                            accentColor = Color(0xFFFFB703),
                            testTag = "ai_tutor_topic_${topic.title.take(6)}"
                        )
                    }

                    Text(
                        text = if (isSinhala && topic.summarySinhala.isNotBlank()) topic.summarySinhala else topic.summary,
                        fontSize = 11.sp,
                        color = CyberTextSecondary,
                        lineHeight = 16.sp
                    )

                    // Formula Sheet
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF030712))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        topic.keyFormulas.forEach { formula ->
                            Text(
                                text = "• $formula",
                                fontSize = 10.sp,
                                fontFamily = OrbitronFontFamily,
                                color = theme.primary
                            )
                        }
                    }
                }
            }
        }
        }

        // 3. Study Groups with Join Key
        if (uiState.activeStudySubTab == "GROUPS") {
            CyberPanel(modifier = Modifier.fillMaxWidth()) {
                Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = theme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isSinhala) "අධ්‍යයන කණ්ඩායම (STUDY GROUP)" else "COLLABORATIVE STUDY GROUP",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = theme.secondary
                        )
                    }
                    Text(
                        text = "KEY: ${uiState.activeGroup.joinKey}",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 9.sp,
                        color = Color(0xFFFFD700)
                    )
                }

                Text(
                    text = "${uiState.activeGroup.name} (${uiState.activeGroup.memberCount} members) • ${uiState.activeGroup.description}",
                    fontSize = 10.sp,
                    color = CyberTextMuted
                )

                // Group Chat Feed
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF030712))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    uiState.activeGroup.messages.forEach { msg ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (msg.sender == "Hero") Arrangement.End else Arrangement.Start
                        ) {
                            Column {
                                Text(
                                    text = if (msg.isAi) "[RAVI AI TUTOR]" else msg.sender,
                                    fontFamily = OrbitronFontFamily,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (msg.isAi) theme.primary else theme.secondary
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (msg.sender == "Hero") Color(0xFF0F1E36) else CyberDarkSurface)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = msg.text,
                                        fontSize = 10.sp,
                                        color = CyberTextPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // Group chat message input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = groupChatText,
                        onValueChange = { groupChatText = it },
                        placeholder = {
                            Text(
                                "Ask group or AI study tutor...",
                                fontSize = 10.sp,
                                color = CyberTextMuted
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = theme.secondary,
                            unfocusedBorderColor = Color(0xFF1E293B),
                            focusedContainerColor = CyberDarkCard,
                            unfocusedContainerColor = CyberDarkCard,
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary
                        ),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (groupChatText.isNotBlank()) {
                                viewModel.sendGroupChatMessage(groupChatText)
                                groupChatText = ""
                            }
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.secondary)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Join with key
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = joinKeyInput,
                        onValueChange = { joinKeyInput = it },
                        placeholder = {
                            Text(
                                "Enter Group Key (e.g. STARK-BOTS)...",
                                fontSize = 10.sp,
                                color = CyberTextMuted
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFD700),
                            unfocusedBorderColor = Color(0xFF1E293B),
                            focusedContainerColor = CyberDarkCard,
                            unfocusedContainerColor = CyberDarkCard,
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary
                        ),
                        singleLine = true
                    )
                    NeonButton(
                        text = "JOIN GROUP",
                        onClick = {
                            if (joinKeyInput.isNotBlank()) {
                                viewModel.joinGroupWithKey(joinKeyInput)
                                joinKeyInput = ""
                            }
                        },
                        accentColor = Color(0xFFFFD700)
                    )
                }
            }
        }
        }

        // 4. Interactive Quiz Challenge Card
        if (uiState.activeStudySubTab == "QUIZ") {
            val question = subject.quiz.getOrNull(uiState.currentQuizIndex)
            if (question != null) {
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
                            text = if (isSinhala) "ප්‍රශ්නාවලි අභියෝගය (QUIZ)" else "QUICK QUIZ CHALLENGE",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFFFFB703)
                        )
                        Text(
                            text = "Q ${uiState.currentQuizIndex + 1} OF ${subject.quiz.size}",
                            fontFamily = OrbitronFontFamily,
                            fontSize = 9.sp,
                            color = CyberTextMuted
                        )
                    }

                    Text(
                        text = if (isSinhala && question.questionSinhala.isNotBlank()) question.questionSinhala else question.question,
                        fontFamily = OrbitronFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CyberTextPrimary,
                        lineHeight = 16.sp
                    )

                    // 4 Options
                    question.options.forEachIndexed { idx, opt ->
                        val isSelected = uiState.selectedOptionIndex == idx
                        val isCorrectChoice = idx == question.correctIndex

                        val borderCol = when {
                            !uiState.isQuizAnswered -> Color(0xFF1E293B)
                            isCorrectChoice -> CyberSuccess
                            isSelected && !isCorrectChoice -> CyberError
                            else -> Color(0xFF1E293B)
                        }

                        val bgCol = when {
                            !uiState.isQuizAnswered -> CyberDarkCard
                            isCorrectChoice -> CyberSuccess.copy(alpha = 0.15f)
                            isSelected && !isCorrectChoice -> CyberError.copy(alpha = 0.15f)
                            else -> CyberDarkCard
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(bgCol)
                                .border(1.dp, borderCol, RoundedCornerShape(8.dp))
                                .clickable(enabled = !uiState.isQuizAnswered) {
                                    viewModel.selectQuizOption(idx)
                                }
                                .padding(10.dp)
                                .testTag("quiz_opt_$idx"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${('A' + idx)}. $opt",
                                fontSize = 11.sp,
                                color = if (isSelected || (uiState.isQuizAnswered && isCorrectChoice)) Color.White else CyberTextSecondary,
                                modifier = Modifier.weight(1f)
                            )
                            if (uiState.isQuizAnswered) {
                                if (isCorrectChoice) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = CyberSuccess,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = "Incorrect",
                                        tint = CyberError,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Explanation Feedback
                    if (uiState.isQuizAnswered) {
                        val wasCorrect = uiState.selectedOptionIndex == question.correctIndex
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (wasCorrect) CyberSuccess.copy(alpha = 0.1f) else CyberError.copy(alpha = 0.1f))
                                .border(1.dp, if (wasCorrect) CyberSuccess else CyberError, RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = if (wasCorrect) "✓ CORRECT ANSWER!" else "✗ INCORRECT",
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = if (wasCorrect) CyberSuccess else CyberError
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isSinhala && question.explanationSinhala.isNotBlank()) question.explanationSinhala else question.explanation,
                                fontSize = 11.sp,
                                color = CyberTextPrimary
                            )
                        }

                        NeonButton(
                            text = "NEXT QUESTION",
                            onClick = { viewModel.nextQuizQuestion() },
                            icon = Icons.Default.NavigateNext,
                            accentColor = theme.secondary,
                            modifier = Modifier.align(Alignment.End),
                            testTag = "quiz_next_button"
                        )
                    }
                }
            }
        }
        }
    }
}
