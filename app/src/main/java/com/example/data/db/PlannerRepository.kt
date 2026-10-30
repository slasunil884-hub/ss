package com.example.data.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repository for Room-backed Study Hub Daily Planner tasks.
 */
class PlannerRepository(private val dao: PlannerTaskDao) {

    val allTasks: Flow<List<PlannerTask>> = dao.getAllTasks()

    suspend fun insertTask(
        title: String,
        subject: String = "General",
        grade: String = "Grade 11",
        priority: String = "Medium",
        dueDate: String = "Today",
        notes: String = ""
    ): Long = withContext(Dispatchers.IO) {
        val task = PlannerTask(
            title = title.trim(),
            subject = subject.trim(),
            grade = grade.trim(),
            priority = priority.trim(),
            dueDate = dueDate.trim(),
            notes = notes.trim(),
            isCompleted = false
        )
        dao.insertTask(task)
    }

    suspend fun insertRawTask(task: PlannerTask): Long = withContext(Dispatchers.IO) {
        dao.insertTask(task)
    }

    suspend fun toggleTaskCompletion(task: PlannerTask) = withContext(Dispatchers.IO) {
        dao.updateCompletion(task.id, !task.isCompleted)
    }

    suspend fun setTaskCompletion(taskId: Long, isCompleted: Boolean) = withContext(Dispatchers.IO) {
        dao.updateCompletion(taskId, isCompleted)
    }

    suspend fun deleteTask(task: PlannerTask) = withContext(Dispatchers.IO) {
        dao.deleteTask(task)
    }

    suspend fun deleteTaskById(taskId: Long) = withContext(Dispatchers.IO) {
        dao.deleteTaskById(taskId)
    }

    suspend fun deleteCompletedTasks() = withContext(Dispatchers.IO) {
        dao.deleteCompletedTasks()
    }

    /**
     * Seed starter tasks if database is empty on initial run.
     */
    suspend fun seedDefaultsIfEmpty() = withContext(Dispatchers.IO) {
        if (dao.getTaskCount() == 0) {
            val starterTasks = listOf(
                PlannerTask(
                    title = "Review Newton's Laws & Calculus Integrals",
                    subject = "Physics / Math",
                    grade = "Grade 11-12",
                    priority = "High",
                    dueDate = "Today 16:00",
                    notes = "Memorize kinematic equations and solve 3 projectile motion problems."
                ),
                PlannerTask(
                    title = "Complete 2023 Science Past Paper Part 1",
                    subject = "Combined Science",
                    grade = "Grade 11",
                    priority = "High",
                    dueDate = "Today 18:30",
                    notes = "Target 40/40 score on MCQ section."
                ),
                PlannerTask(
                    title = "Write Sinhala Literature Essay Outline",
                    subject = "Sinhala",
                    grade = "Grade 11",
                    priority = "Medium",
                    dueDate = "Tomorrow",
                    notes = "Focus on literary techniques and poetic devices."
                ),
                PlannerTask(
                    title = "Arduino Sensor Code Simulation in Studio",
                    subject = "ICT / Arduino",
                    grade = "Tech Lab",
                    priority = "Medium",
                    dueDate = "Tomorrow 20:00",
                    notes = "Test HC-SR04 ultrasonic distance interrupt sketch."
                )
            )
            dao.insertAll(starterTasks)
        }
    }
}
