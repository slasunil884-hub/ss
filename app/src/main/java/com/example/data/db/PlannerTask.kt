package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Database Entity representing a task in the Study Hub Daily Planner.
 */
@Entity(tableName = "planner_tasks")
data class PlannerTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subject: String = "General",
    val grade: String = "Grade 11",
    val priority: String = "Medium", // High, Medium, Low
    val dueDate: String = "Today",
    val notes: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
