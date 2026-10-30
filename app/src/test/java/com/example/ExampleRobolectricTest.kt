package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.ArduinoProjectDatabase
import com.example.data.StarkMusicDatabase
import com.example.data.StarkStudyDatabase
import com.example.data.db.AppDatabase
import com.example.data.db.PlannerRepository
import com.example.data.db.PlannerTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var db: AppDatabase
  private lateinit var repository: PlannerRepository

  @Before
  fun setUp() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    repository = PlannerRepository(db.plannerTaskDao())
  }

  @After
  fun tearDown() {
    db.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Shadow Supporter", appName)
  }

  @Test
  fun `verify arduino projects database`() {
    assertTrue(ArduinoProjectDatabase.projects.isNotEmpty())
    val spiderBot = ArduinoProjectDatabase.projects.first()
    assertEquals("Spider-Bot Quadruped Walker", spiderBot.title)
  }

  @Test
  fun `verify music database`() {
    assertTrue(StarkMusicDatabase.initialPlaylist.isNotEmpty())
    val heroTheme = StarkMusicDatabase.initialPlaylist.first()
    assertEquals("Far From Home Hero Theme", heroTheme.title)
  }

  @Test
  fun `verify study database`() {
    assertTrue(StarkStudyDatabase.subjects.isNotEmpty())
    val scienceSubject = StarkStudyDatabase.subjects.first { it.id == "science" }
    assertTrue(scienceSubject.quiz.isNotEmpty())
  }

  @Test
  fun `verify room planner insert, flow query, toggle completion and delete`() = runBlocking {
    val dao = db.plannerTaskDao()

    // 1. Insert task
    val task = PlannerTask(
      title = "Physics Quantum Mechanics Revision",
      subject = "Physics",
      grade = "Grade 12",
      priority = "High",
      dueDate = "Today 17:00",
      notes = "Revise Planck relation and photoelectric effect",
      isCompleted = false
    )
    val taskId = dao.insertTask(task)
    assertTrue(taskId > 0)

    // 2. Query flow
    val tasks = dao.getAllTasks().first()
    assertEquals(1, tasks.size)
    assertEquals("Physics Quantum Mechanics Revision", tasks[0].title)
    assertFalse(tasks[0].isCompleted)

    // 3. Mark completed
    dao.updateCompletion(taskId, true)
    val updatedTasks = dao.getAllTasks().first()
    assertTrue(updatedTasks[0].isCompleted)

    // 4. Delete task
    dao.deleteTaskById(taskId)
    val remainingTasks = dao.getAllTasks().first()
    assertTrue(remainingTasks.isEmpty())
  }

  @Test
  fun `verify planner repository seed and operations`() = runBlocking {
    // Verify initial seed
    repository.seedDefaultsIfEmpty()
    val seeded = repository.allTasks.first()
    assertTrue(seeded.isNotEmpty())

    // Add a new task via repository
    val newId = repository.insertTask(
      title = "Calculus Derivatives Practice",
      subject = "Mathematics",
      grade = "Grade 11",
      priority = "High",
      dueDate = "Tonight",
      notes = "Solve 10 chain rule problems"
    )
    assertTrue(newId > 0)

    val afterInsert = repository.allTasks.first()
    val insertedTask = afterInsert.first { it.id == newId }
    assertEquals("Calculus Derivatives Practice", insertedTask.title)
    assertFalse(insertedTask.isCompleted)

    // Toggle completion via repository
    repository.toggleTaskCompletion(insertedTask)
    val afterToggle = repository.allTasks.first()
    val toggledTask = afterToggle.first { it.id == newId }
    assertTrue(toggledTask.isCompleted)

    // Delete task via repository
    repository.deleteTask(toggledTask)
    val afterDelete = repository.allTasks.first()
    assertFalse(afterDelete.any { it.id == newId })
  }
}
