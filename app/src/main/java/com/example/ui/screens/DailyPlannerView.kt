package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LanguageMode
import com.example.data.StarkStudyDatabase
import com.example.data.db.PlannerTask
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

/**
 * High-tech Daily Planner screen powered by persistent Room Database.
 */
@Composable
fun DailyPlannerView(
    viewModel: KarenOSViewModel,
    uiState: KarenOsUiState,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current
    val isSinhala = uiState.languageMode == LanguageMode.SINHALA

    var showAddDialog by remember { mutableStateOf(false) }
    var quickTaskTitle by remember { mutableStateOf("") }

    // Dialog inputs
    var dialogTitle by remember { mutableStateOf("") }
    var dialogSubject by remember { mutableStateOf(uiState.activeSubject.name) }
    var dialogPriority by remember { mutableStateOf("High") }
    var dialogDueDate by remember { mutableStateOf("Today 18:00") }
    var dialogNotes by remember { mutableStateOf("") }

    val allTasks = uiState.plannerTasks
    val totalCount = allTasks.size
    val completedCount = allTasks.count { it.isCompleted }
    val pendingCount = totalCount - completedCount
    val highPriorityCount = allTasks.count { it.priority.equals("High", ignoreCase = true) && !it.isCompleted }
    val progressRatio = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = progressRatio, label = "ProgressAnim")

    val filteredTasks = remember(allTasks, uiState.plannerFilter) {
        when (uiState.plannerFilter) {
            "PENDING" -> allTasks.filter { !it.isCompleted }
            "COMPLETED" -> allTasks.filter { it.isCompleted }
            "HIGH" -> allTasks.filter { it.priority.equals("High", ignoreCase = true) }
            else -> allTasks
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Planner Overview & Room DB Status Card
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = theme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isSinhala) "දෛනික පාඩම් සැලැස්ම (DAILY PLANNER)" else "STUDENT DAILY PLANNER",
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = theme.primary
                            )
                        }
                        Text(
                            text = "ROOM DATABASE PERSISTENCE ENGINE • AUTO-SYNC FLOW",
                            fontFamily = OrbitronFontFamily,
                            fontSize = 8.sp,
                            color = theme.secondary
                        )
                    }

                    CyberStatusBadge(
                        label = "STORAGE",
                        statusText = "ROOM DB",
                        accentColor = CyberSuccess
                    )
                }

                // Progress Bar & Percentage
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isSinhala) "සම්පූර්ණ ප්‍රගතිය: ${(progressRatio * 100).toInt()}%" else "COMPLETION: ${(progressRatio * 100).toInt()}%",
                            fontFamily = OrbitronFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CyberTextSecondary
                        )
                        Text(
                            text = "$completedCount / $totalCount TASKS DONE",
                            fontFamily = OrbitronFontFamily,
                            fontSize = 10.sp,
                            color = if (completedCount == totalCount && totalCount > 0) CyberSuccess else theme.secondary
                        )
                    }

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = theme.primary,
                        trackColor = Color(0xFF1E293B)
                    )
                }

                // Metric Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricPill(label = "TOTAL", count = totalCount, color = theme.secondary)
                    MetricPill(label = "PENDING", count = pendingCount, color = Color(0xFFFFB703))
                    MetricPill(label = "DONE", count = completedCount, color = CyberSuccess)
                    MetricPill(label = "HIGH PRIORITY", count = highPriorityCount, color = Color(0xFFFF3366))
                }

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NeonButton(
                        text = if (isSinhala) "+ නව ඉලක්කය" else "+ NEW TASK",
                        onClick = {
                            dialogSubject = uiState.activeSubject.name
                            showAddDialog = true
                        },
                        icon = Icons.Default.Add,
                        accentColor = theme.primary,
                        modifier = Modifier.weight(1f),
                        testTag = "planner_open_add_dialog_btn"
                    )

                    NeonButton(
                        text = if (isSinhala) "AI සැලැස්ම" else "AI SUGGEST",
                        onClick = { viewModel.seedSubjectAiPlannerTasks(uiState.activeSubject) },
                        icon = Icons.Default.AutoAwesome,
                        accentColor = Color(0xFFFFB703),
                        modifier = Modifier.weight(1f),
                        testTag = "planner_ai_suggest_btn"
                    )

                    if (completedCount > 0) {
                        NeonButton(
                            text = "CLEAR DONE",
                            onClick = { viewModel.deleteCompletedPlannerTasks() },
                            icon = Icons.Default.CleaningServices,
                            accentColor = CyberError,
                            modifier = Modifier.weight(1f),
                            testTag = "planner_clear_completed_btn"
                        )
                    }
                }
            }
        }

        // Quick Add Input Card
        CyberPanel(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = quickTaskTitle,
                    onValueChange = { quickTaskTitle = it },
                    placeholder = {
                        Text(
                            text = if (isSinhala) "ඉක්මන් ඉලක්කයක් එක් කරන්න (${uiState.activeSubject.name})..." else "Quick add task for ${uiState.activeSubject.name}...",
                            fontSize = 11.sp,
                            color = CyberTextMuted
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("planner_quick_add_input"),
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
                        if (quickTaskTitle.isNotBlank()) {
                            viewModel.createPlannerTask(
                                title = quickTaskTitle,
                                subject = uiState.activeSubject.name,
                                grade = uiState.activeSubject.gradeRange,
                                priority = "Medium",
                                dueDate = "Today"
                            )
                            quickTaskTitle = ""
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(theme.primary)
                        .size(44.dp)
                        .testTag("planner_quick_add_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Quick Add",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Filter Tabs Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                "ALL" to "ALL ($totalCount)",
                "PENDING" to "PENDING ($pendingCount)",
                "COMPLETED" to "COMPLETED ($completedCount)",
                "HIGH" to "HIGH ($highPriorityCount)"
            ).forEach { (key, label) ->
                val isSelected = uiState.plannerFilter == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) theme.secondary.copy(alpha = 0.2f) else CyberDarkSurface)
                        .border(
                            1.dp,
                            if (isSelected) theme.secondary else Color(0xFF1E293B),
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { viewModel.setPlannerFilter(key) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("planner_filter_$key")
                ) {
                    Text(
                        text = label,
                        fontFamily = OrbitronFontFamily,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) theme.secondary else CyberTextMuted
                    )
                }
            }
        }

        // Tasks List
        if (filteredTasks.isEmpty()) {
            CyberPanel(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = CyberTextMuted,
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = if (isSinhala) "මෙම කාණ්ඩයේ ඉලක්ක නොමැත" else "NO TASKS FOUND FOR THIS FILTER",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 11.sp,
                        color = CyberTextMuted
                    )
                    NeonButton(
                        text = if (isSinhala) "නව ඉලක්කයක් එක් කරන්න" else "ADD FIRST TASK",
                        onClick = { showAddDialog = true },
                        icon = Icons.Default.Add,
                        accentColor = theme.primary,
                        testTag = "planner_empty_add_first_btn"
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredTasks.forEach { task ->
                    PlannerTaskItemCard(
                        task = task,
                        onToggle = { viewModel.togglePlannerTask(task) },
                        onDelete = { viewModel.deletePlannerTask(task) }
                    )
                }
            }
        }
    }

    // Detailed Add Task Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSinhala) "නව දෛනික පාඩම් ඉලක්කය" else "NEW DAILY STUDY TASK",
                        fontFamily = OrbitronFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = theme.primary
                    )
                    IconButton(
                        onClick = { showAddDialog = false },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = CyberTextMuted
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Title
                    OutlinedTextField(
                        value = dialogTitle,
                        onValueChange = { dialogTitle = it },
                        label = { Text("Task Title *", fontSize = 11.sp) },
                        placeholder = { Text("e.g. Solve Physics past paper 2023", fontSize = 10.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("planner_dialog_title_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = theme.primary,
                            unfocusedBorderColor = Color(0xFF1E293B),
                            focusedContainerColor = CyberDarkCard,
                            unfocusedContainerColor = CyberDarkCard,
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary
                        ),
                        singleLine = true
                    )

                    // Subject Selector Chips
                    Text(
                        text = "SUBJECT:",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 9.sp,
                        color = CyberTextMuted
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Mathematics", "Physics", "Chemistry", "Biology", "Science", "Sinhala", "English", "ICT").forEach { subj ->
                            val isSel = dialogSubject == subj
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSel) theme.primary.copy(alpha = 0.2f) else CyberDarkSurface)
                                    .border(1.dp, if (isSel) theme.primary else Color(0xFF1E293B), RoundedCornerShape(4.dp))
                                    .clickable { dialogSubject = subj }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = subj,
                                    fontSize = 9.sp,
                                    color = if (isSel) theme.primary else CyberTextSecondary
                                )
                            }
                        }
                    }

                    // Priority Selector
                    Text(
                        text = "PRIORITY LEVEL:",
                        fontFamily = OrbitronFontFamily,
                        fontSize = 9.sp,
                        color = CyberTextMuted
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("High" to Color(0xFFFF3366), "Medium" to Color(0xFFFFB703), "Low" to Color(0xFF00F0FF)).forEach { (p, col) ->
                            val isSel = dialogPriority == p
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) col.copy(alpha = 0.25f) else CyberDarkSurface)
                                    .border(1.dp, if (isSel) col else Color(0xFF1E293B), RoundedCornerShape(6.dp))
                                    .clickable { dialogPriority = p }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = p.uppercase(),
                                    fontFamily = OrbitronFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) col else CyberTextMuted
                                )
                            }
                        }
                    }

                    // Due Time & Notes
                    OutlinedTextField(
                        value = dialogDueDate,
                        onValueChange = { dialogDueDate = it },
                        label = { Text("Due Date / Time", fontSize = 11.sp) },
                        placeholder = { Text("e.g. Today 20:00", fontSize = 10.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("planner_dialog_due_input"),
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

                    OutlinedTextField(
                        value = dialogNotes,
                        onValueChange = { dialogNotes = it },
                        label = { Text("Study Notes / Objectives", fontSize = 11.sp) },
                        placeholder = { Text("Specific formula review or questions...", fontSize = 10.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("planner_dialog_notes_input"),
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
                }
            },
            confirmButton = {
                NeonButton(
                    text = "SAVE TO ROOM DB",
                    onClick = {
                        if (dialogTitle.isNotBlank()) {
                            viewModel.createPlannerTask(
                                title = dialogTitle,
                                subject = dialogSubject,
                                grade = "Grade 11",
                                priority = dialogPriority,
                                dueDate = dialogDueDate,
                                notes = dialogNotes
                            )
                            dialogTitle = ""
                            dialogNotes = ""
                            showAddDialog = false
                        }
                    },
                    icon = Icons.Default.Check,
                    accentColor = theme.primary,
                    testTag = "planner_dialog_save_btn"
                )
            },
            dismissButton = {
                NeonButton(
                    text = "CANCEL",
                    onClick = { showAddDialog = false },
                    accentColor = CyberTextMuted
                )
            },
            containerColor = Color(0xFF0F172A),
            tonalElevation = 6.dp
        )
    }
}

@Composable
fun PlannerTaskItemCard(
    task: PlannerTask,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalKarenTheme.current

    val priorityColor = when (task.priority.lowercase()) {
        "high" -> Color(0xFFFF3366)
        "medium" -> Color(0xFFFFB703)
        else -> Color(0xFF00F0FF)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (task.isCompleted) Color(0x2210B981) else CyberDarkSurface)
            .border(
                width = 1.dp,
                color = if (task.isCompleted) CyberSuccess.copy(alpha = 0.5f) else Color(0xFF1E293B),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .testTag("planner_task_item_${task.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox with minimum 48dp touch target
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = CyberSuccess,
                    uncheckedColor = CyberTextMuted,
                    checkmarkColor = Color.Black
                ),
                modifier = Modifier.testTag("planner_task_checkbox_${task.id}")
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Task Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = task.title,
                fontSize = 12.sp,
                fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
                color = if (task.isCompleted) CyberTextMuted else CyberTextPrimary,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                lineHeight = 16.sp
            )

            // Chips Row: Subject, Priority, Due Date
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Priority Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(priorityColor.copy(alpha = 0.15f))
                        .border(1.dp, priorityColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = task.priority.uppercase(),
                        fontFamily = OrbitronFontFamily,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = priorityColor
                    )
                }

                // Subject Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF030712))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = task.subject,
                        fontSize = 8.sp,
                        color = theme.secondary
                    )
                }

                // Due Date Tag
                if (task.dueDate.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = CyberTextMuted,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = task.dueDate,
                            fontSize = 8.sp,
                            color = CyberTextMuted
                        )
                    }
                }
            }

            // Optional Notes snippet
            if (task.notes.isNotBlank()) {
                Text(
                    text = task.notes,
                    fontSize = 9.sp,
                    color = CyberTextSecondary,
                    maxLines = 1
                )
            }
        }

        // Delete button
        IconButton(
            onClick = { onDelete() },
            modifier = Modifier
                .size(44.dp)
                .testTag("planner_task_delete_${task.id}")
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task",
                tint = CyberError.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun MetricPill(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF0B1120))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontFamily = OrbitronFontFamily,
                fontSize = 8.sp,
                color = CyberTextMuted
            )
            Text(
                text = count.toString(),
                fontFamily = OrbitronFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
