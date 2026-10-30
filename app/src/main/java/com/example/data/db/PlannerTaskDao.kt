package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for PlannerTask table in Room.
 */
@Dao
interface PlannerTaskDao {

    @Query("SELECT * FROM planner_tasks ORDER BY isCompleted ASC, CASE priority WHEN 'High' THEN 1 WHEN 'Medium' THEN 2 ELSE 3 END, id DESC")
    fun getAllTasks(): Flow<List<PlannerTask>>

    @Query("SELECT * FROM planner_tasks WHERE isCompleted = :completed ORDER BY id DESC")
    fun getTasksByStatus(completed: Boolean): Flow<List<PlannerTask>>

    @Query("SELECT * FROM planner_tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Long): PlannerTask?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: PlannerTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<PlannerTask>): List<Long>

    @Update
    suspend fun updateTask(task: PlannerTask)

    @Query("UPDATE planner_tasks SET isCompleted = :completed WHERE id = :id")
    suspend fun updateCompletion(id: Long, completed: Boolean)

    @Delete
    suspend fun deleteTask(task: PlannerTask)

    @Query("DELETE FROM planner_tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    @Query("DELETE FROM planner_tasks WHERE isCompleted = 1")
    suspend fun deleteCompletedTasks()

    @Query("SELECT COUNT(*) FROM planner_tasks")
    suspend fun getTaskCount(): Int
}
