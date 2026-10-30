package com.example.viewmodel

import android.app.Application
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.CyberAudioSynthesizer
import com.example.data.AiModelType
import com.example.data.AiService
import com.example.data.AnswerDepth
import com.example.data.ArduinoProject
import com.example.data.ArduinoProjectDatabase
import com.example.data.GroupMessage
import com.example.data.LanguageMode
import com.example.data.MusicTrack
import com.example.data.StarkMusicDatabase
import com.example.data.StarkStudyDatabase
import com.example.data.StudyGroup
import com.example.data.StudySubject
import com.example.data.StudyTask
import com.example.data.db.AppDatabase
import com.example.data.db.PlannerRepository
import com.example.data.db.PlannerTask
import com.example.ui.theme.KarenOsTheme
import com.example.ui.theme.MusicMood
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "USER" or "RAVI"
    val text: String,
    val modelBadge: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class HudTab(val title: String, val titleSinhala: String, val iconName: String) {
    COMMAND_CENTER("COMMAND CENTER", "ප්‍රධාන මධ්‍යස්ථානය", "home"),
    RAVI_CHAT("RAVI AI CHAT", "රවී කතාබහ", "terminal"),
    MUSIC_ROOM("SS MUSIC ROOM", "සංගීත මැදිරිය", "play"),
    ARDUINO_STUDIO("ARDUINO STUDIO", "ආර්ඩුයිනෝ විද්‍යාගාරය", "microchip"),
    STUDY_HUB("STUDY HUB (1-13)", "අධ්‍යයන මධ්‍යස්ථානය", "school")
}

data class KarenOsUiState(
    val isBooted: Boolean = false,
    val bootProgress: Float = 0f,
    val bootStatusText: String = "INITIALIZING SHADOW SUPPORTER (SS) / RAVI CORE...",
    val activeTab: HudTab = HudTab.COMMAND_CENTER,
    val activeTheme: KarenOsTheme = KarenOsTheme.SPIDERMAN_MIX,
    val languageMode: LanguageMode = LanguageMode.ENGLISH,
    val answerDepth: AnswerDepth = AnswerDepth.INTERMEDIATE,
    val isWatchCompactMode: Boolean = false,

    // Arduino Studio
    val activeArduinoProject: ArduinoProject = ArduinoProjectDatabase.projects.first(),
    val arduinoPrompt: String = "",
    val isGeneratingSketch: Boolean = false,

    // Study Hub (Grades 1-13) & Daily Planner (Room DB)
    val activeSubject: StudySubject = StarkStudyDatabase.subjects.first(),
    val currentQuizIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isQuizAnswered: Boolean = false,
    val quizScore: Int = 0,
    val studyTasks: List<StudyTask> = StarkStudyDatabase.defaultTasks,
    val plannerTasks: List<PlannerTask> = emptyList(),
    val plannerFilter: String = "ALL", // ALL, PENDING, COMPLETED, HIGH
    val activeStudySubTab: String = "PLANNER", // PLANNER, NOTES_QUIZ, GROUPS
    val studyGroups: List<StudyGroup> = StarkStudyDatabase.defaultStudyGroups,
    val activeGroup: StudyGroup = StarkStudyDatabase.defaultStudyGroups.first(),
    val enteredJoinKey: String = "",

    // Neural Terminal & AI
    val chatMessages: List<ChatMessage> = listOf(
        ChatMessage(
            sender = "RAVI",
            text = "SHADOW SUPPORTER online. Greetings, Hero! Shadow Supporter (SS) core v5.0 ready. Multi-device responsive engine active across Desktop, PC, Mobile, Smart Boards, and Smart Watches. Arduino Studio and Grade 1-13 Study Hub operational. How can I assist you today? / ආයුබෝවන්! මම රවී. අද ඔබට උදවු කිරීමට මම සූදානම්.",
            modelBadge = "SS CORE"
        )
    ),
    val selectedAiModel: AiModelType = AiModelType.FLASH_GENERAL,
    val isAiStreaming: Boolean = false,
    val currentAiStreamText: String = "",
    val aiError: String? = null,
    val isListeningMic: Boolean = false,
    val isSpeakingTts: Boolean = false,

    // Focus Timer
    val focusSecondsRemaining: Int = 25 * 60,
    val isTimerRunning: Boolean = false,
    val completedPomodoros: Int = 0,

    // Music Room & App-Wide Persistent Player
    val playlist: List<MusicTrack> = StarkMusicDatabase.initialPlaylist,
    val currentTrack: MusicTrack = StarkMusicDatabase.initialPlaylist.first(),
    val isPlayingMusic: Boolean = false,
    val activeMoodFilter: MusicMood = MusicMood.ALL,
    val isFavoriteOnly: Boolean = false
)

class KarenOSViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(KarenOsUiState())
    val uiState: StateFlow<KarenOsUiState> = _uiState.asStateFlow()

    val audioSynth = CyberAudioSynthesizer()
    private val aiService = AiService()
    private var ttsEngine: TextToSpeech? = null
    private var timerJob: Job? = null
    private val vibrator = application.getSystemService(Vibrator::class.java)

    private val plannerDb = AppDatabase.getInstance(application)
    val plannerRepository = PlannerRepository(plannerDb.plannerTaskDao())

    init {
        initTts()
        runBootAnimation()
        initRoomPlanner()
    }

    private fun initRoomPlanner() {
        viewModelScope.launch {
            try {
                plannerRepository.seedDefaultsIfEmpty()
            } catch (e: Exception) {
                Log.e("KarenOSViewModel", "Error seeding Room defaults", e)
            }
            plannerRepository.allTasks.collect { tasks ->
                _uiState.update { it.copy(plannerTasks = tasks) }
            }
        }
    }

    private fun initTts() {
        ttsEngine = TextToSpeech(getApplication()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsEngine?.language = Locale.US
                ttsEngine?.setPitch(1.02f)
                ttsEngine?.setSpeechRate(1.0f)
            }
        }
    }

    private fun triggerHaptic(durationMs: Long = 40) {
        try {
            vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } catch (ignored: Exception) {}
    }

    // --- Boot Sequence ---
    private fun runBootAnimation() {
        viewModelScope.launch {
            val stages = listOf(
                0.20f to "OPTIMIZING FOR DESKTOP, MOBILE, SMART BOARDS & WATCHES...",
                0.45f to "INITIALIZING ARDUINO & ROBOTICS STUDIO...",
                0.70f to "CONNECTING GRADES 1-13 STUDY HUB & GROUPS...",
                0.90f to "BOOTING SHADOW SUPPORTER (SS) MUSIC ROOM...",
                1.00f to "SHADOW SUPPORTER ONLINE."
            )

            for ((progress, status) in stages) {
                delay(350)
                _uiState.update { it.copy(bootProgress = progress, bootStatusText = status) }
                audioSynth.playBeep(440f + (progress * 380f), 70, 0.10f)
            }
        }
    }

    fun launchOS() {
        audioSynth.playDoubleChime()
        triggerHaptic(60)
        _uiState.update { it.copy(isBooted = true) }
        speakResponse("Shadow Supporter AI Core active. All invention, music, and study systems online.")
    }

    fun selectTab(tab: HudTab) {
        audioSynth.playBeep(640f, 60, 0.08f)
        triggerHaptic(20)
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun selectTheme(theme: KarenOsTheme) {
        audioSynth.playBeep(720f, 80, 0.12f)
        _uiState.update { it.copy(activeTheme = theme) }
    }

    fun toggleLanguage() {
        val nextLang = when (_uiState.value.languageMode) {
            LanguageMode.ENGLISH -> LanguageMode.SINHALA
            LanguageMode.SINHALA -> LanguageMode.SINGLISH
            LanguageMode.SINGLISH -> LanguageMode.ENGLISH
        }
        audioSynth.playBeep(580f, 70, 0.10f)
        _uiState.update { it.copy(languageMode = nextLang) }
        val msg = if (nextLang == LanguageMode.SINHALA) "භාෂාව සිංහලට මාරු කරන ලදී." else "Language set to ${nextLang.label}."
        speakResponse(msg)
    }

    fun setAnswerDepth(depth: AnswerDepth) {
        audioSynth.playBeep(520f, 60, 0.08f)
        _uiState.update { it.copy(answerDepth = depth) }
    }

    fun toggleWatchCompactMode() {
        audioSynth.playBeep(500f, 50, 0.10f)
        _uiState.update { it.copy(isWatchCompactMode = !it.isWatchCompactMode) }
    }

    // --- Music Player (Shadow Supporter SS) ---
    fun playTrack(track: MusicTrack) {
        audioSynth.startSynthTrack(track.title, track.baseFrequency)
        val updatedPlaylist = _uiState.value.playlist.map {
            if (it.id == track.id) it.copy(playCount = it.playCount + 1) else it
        }
        _uiState.update {
            it.copy(
                currentTrack = track,
                isPlayingMusic = true,
                playlist = updatedPlaylist
            )
        }
    }

    fun togglePlayPause() {
        if (_uiState.value.isPlayingMusic) {
            audioSynth.stopSynthMusic()
            _uiState.update { it.copy(isPlayingMusic = false) }
        } else {
            playTrack(_uiState.value.currentTrack)
        }
    }

    fun nextTrack() {
        val list = filteredPlaylist()
        val currentIndex = list.indexOfFirst { it.id == _uiState.value.currentTrack.id }
        val nextIndex = if (currentIndex != -1 && currentIndex + 1 < list.size) currentIndex + 1 else 0
        val next = list.getOrNull(nextIndex) ?: _uiState.value.currentTrack
        playTrack(next)
    }

    fun previousTrack() {
        val list = filteredPlaylist()
        val currentIndex = list.indexOfFirst { it.id == _uiState.value.currentTrack.id }
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else list.size - 1
        val prev = list.getOrNull(prevIndex) ?: _uiState.value.currentTrack
        playTrack(prev)
    }

    fun toggleFavorite(trackId: String) {
        val updated = _uiState.value.playlist.map {
            if (it.id == trackId) it.copy(isFavorite = !it.isFavorite) else it
        }
        _uiState.update { it.copy(playlist = updated) }
    }

    fun filterMood(mood: MusicMood) {
        audioSynth.playBeep(480f, 50, 0.08f)
        _uiState.update { it.copy(activeMoodFilter = mood) }
    }

    fun toggleFavoriteOnly() {
        audioSynth.playBeep(500f, 50, 0.08f)
        _uiState.update { it.copy(isFavoriteOnly = !it.isFavoriteOnly) }
    }

    fun filteredPlaylist(): List<MusicTrack> {
        return _uiState.value.playlist.filter {
            (if (_uiState.value.activeMoodFilter == MusicMood.ALL) true else it.mood == _uiState.value.activeMoodFilter) &&
            (if (_uiState.value.isFavoriteOnly) it.isFavorite else true)
        }
    }

    // --- Arduino Studio ---
    fun selectArduinoProject(project: ArduinoProject) {
        audioSynth.playBeep(540f, 60, 0.08f)
        _uiState.update { it.copy(activeArduinoProject = project) }
    }

    fun generateAiArduinoSketch(prompt: String) {
        if (prompt.isBlank()) return
        selectTab(HudTab.RAVI_CHAT)
        val query = "Write a complete, working Arduino C++ sketch for: $prompt. Provide: 1. Components List (BOM) 2. Pin Mapping 3. Step-by-step wiring instructions 4. Complete compilable Arduino .ino code with clear comments 5. Troubleshooting tips."
        sendAiPrompt(query, AiModelType.ARGON_THINKING)
    }

    // --- Study Hub (Grades 1-13) ---
    fun selectSubject(subject: StudySubject) {
        audioSynth.playBeep(560f, 70, 0.10f)
        _uiState.update {
            it.copy(
                activeSubject = subject,
                currentQuizIndex = 0,
                selectedOptionIndex = null,
                isQuizAnswered = false
            )
        }
    }

    fun selectQuizOption(index: Int) {
        if (_uiState.value.isQuizAnswered) return
        val currentSubject = _uiState.value.activeSubject
        val question = currentSubject.quiz.getOrNull(_uiState.value.currentQuizIndex) ?: return

        val isCorrect = index == question.correctIndex
        if (isCorrect) {
            audioSynth.playDoubleChime()
            triggerHaptic(50)
        } else {
            audioSynth.playBeep(320f, 150, 0.15f)
            triggerHaptic(90)
        }

        _uiState.update {
            it.copy(
                selectedOptionIndex = index,
                isQuizAnswered = true,
                quizScore = if (isCorrect) it.quizScore + 1 else it.quizScore
            )
        }
    }

    fun nextQuizQuestion() {
        val currentSubject = _uiState.value.activeSubject
        val nextIdx = (_uiState.value.currentQuizIndex + 1) % currentSubject.quiz.size
        audioSynth.playBeep(540f, 60, 0.08f)
        _uiState.update {
            it.copy(
                currentQuizIndex = nextIdx,
                selectedOptionIndex = null,
                isQuizAnswered = false
            )
        }
    }

    // --- Daily Planner (Powered by Room Database) ---
    fun setActiveStudySubTab(subTab: String) {
        audioSynth.playBeep(520f, 40, 0.08f)
        _uiState.update { it.copy(activeStudySubTab = subTab) }
    }

    fun setPlannerFilter(filter: String) {
        audioSynth.playBeep(560f, 40, 0.08f)
        _uiState.update { it.copy(plannerFilter = filter) }
    }

    fun createPlannerTask(
        title: String,
        subject: String = _uiState.value.activeSubject.name,
        grade: String = "Grade 11",
        priority: String = "Medium",
        dueDate: String = "Today",
        notes: String = ""
    ) {
        if (title.isBlank()) return
        audioSynth.playBeep(640f, 60, 0.08f)
        triggerHaptic(50)
        viewModelScope.launch {
            plannerRepository.insertTask(
                title = title,
                subject = subject,
                grade = grade,
                priority = priority,
                dueDate = dueDate,
                notes = notes
            )
        }
    }

    fun togglePlannerTask(task: PlannerTask) {
        audioSynth.playBeep(if (!task.isCompleted) 720f else 480f, 50, 0.08f)
        triggerHaptic(40)
        viewModelScope.launch {
            plannerRepository.toggleTaskCompletion(task)
        }
    }

    fun deletePlannerTask(task: PlannerTask) {
        audioSynth.playBeep(320f, 70, 0.10f)
        triggerHaptic(60)
        viewModelScope.launch {
            plannerRepository.deleteTask(task)
        }
    }

    fun deleteCompletedPlannerTasks() {
        audioSynth.playBeep(400f, 80, 0.10f)
        triggerHaptic(70)
        viewModelScope.launch {
            plannerRepository.deleteCompletedTasks()
        }
    }

    fun seedSubjectAiPlannerTasks(subject: StudySubject) {
        audioSynth.playDoubleChime()
        triggerHaptic(80)
        viewModelScope.launch {
            val smartTasks = listOf(
                PlannerTask(
                    title = "Solve 5 practice problems: ${subject.name}",
                    subject = subject.name,
                    grade = subject.gradeRange,
                    priority = "High",
                    dueDate = "Today",
                    notes = "Cover key formulas and exam tips from ${subject.topics.firstOrNull()?.title ?: "module"}."
                ),
                PlannerTask(
                    title = "Revise ${subject.topics.firstOrNull()?.title ?: subject.name} Core Notes",
                    subject = subject.name,
                    grade = subject.gradeRange,
                    priority = "Medium",
                    dueDate = "Tomorrow",
                    notes = "Write 1-page summary sheet with active recall questions."
                ),
                PlannerTask(
                    title = "Take ${subject.name} Timed Quiz & Review Marks",
                    subject = subject.name,
                    grade = subject.gradeRange,
                    priority = "High",
                    dueDate = "This Week",
                    notes = "Aim for 100% mastery on quiz questions."
                )
            )
            smartTasks.forEach { plannerRepository.insertRawTask(it) }
        }
    }

    fun toggleTask(taskId: String) {
        audioSynth.playBeep(560f, 50, 0.08f)
        val updated = _uiState.value.studyTasks.map {
            if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it
        }
        _uiState.update { it.copy(studyTasks = updated) }
    }

    fun addTask(title: String, subject: String) {
        if (title.isBlank()) return
        createPlannerTask(title = title, subject = subject)
        val newTask = StudyTask(title = title, subject = subject, grade = "Grade 11", priority = "Normal")
        _uiState.update { it.copy(studyTasks = it.studyTasks + newTask) }
    }

    fun sendGroupChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = GroupMessage(sender = "Hero", text = text)
        val currentGroup = _uiState.value.activeGroup
        val updatedGroup = currentGroup.copy(messages = currentGroup.messages + userMsg)
        _uiState.update { it.copy(activeGroup = updatedGroup) }

        // AI study tutor bot reply
        viewModelScope.launch {
            delay(1200)
            val aiMsg = GroupMessage(
                sender = "RAVI AI",
                text = "Great point! Let's remember to relate this to the past paper marking scheme.",
                isAi = true
            )
            val withAi = updatedGroup.copy(messages = updatedGroup.messages + aiMsg)
            _uiState.update { it.copy(activeGroup = withAi) }
        }
    }

    fun joinGroupWithKey(key: String) {
        val found = _uiState.value.studyGroups.find { it.joinKey.equals(key.trim(), ignoreCase = true) }
        if (found != null) {
            audioSynth.playDoubleChime()
            _uiState.update { it.copy(activeGroup = found) }
            speakResponse("Joined ${found.name} successfully.")
        } else {
            audioSynth.playBeep(300f, 120, 0.12f)
        }
    }

    fun askAiStudyTutor(topicTitle: String) {
        val prompt = "Hero here. Explain the concept: '$topicTitle' in depth with key formulas, step-by-step logic, and past paper exam questions in ${_uiState.value.languageMode.label}."
        selectTab(HudTab.RAVI_CHAT)
        sendAiPrompt(prompt, AiModelType.ARGON_THINKING)
    }

    // --- Neural Terminal & AI Chat ---
    fun setAiModel(modelType: AiModelType) {
        audioSynth.playBeep(680f, 70, 0.10f)
        _uiState.update { it.copy(selectedAiModel = modelType) }
    }

    fun sendAiPrompt(prompt: String, forcedModel: AiModelType? = null) {
        if (prompt.isBlank()) return
        val modelToUse = forcedModel ?: _uiState.value.selectedAiModel

        val userMsg = ChatMessage(sender = "USER", text = prompt)
        _uiState.update {
            it.copy(
                chatMessages = it.chatMessages + userMsg,
                isAiStreaming = true,
                currentAiStreamText = "",
                aiError = null
            )
        }
        audioSynth.playBeep(520f, 80, 0.10f)

        viewModelScope.launch {
            val accumulated = StringBuilder()
            try {
                aiService.generateStream(
                    prompt = prompt,
                    modelType = modelToUse,
                    languageMode = _uiState.value.languageMode,
                    depth = _uiState.value.answerDepth
                ).collect { chunk ->
                    val text = chunk.text.orEmpty()
                    accumulated.append(text)
                    _uiState.update { it.copy(currentAiStreamText = accumulated.toString()) }
                }

                val fullResponse = accumulated.toString()
                if (fullResponse.isNotBlank()) {
                    val aiMsg = ChatMessage(
                        sender = "RAVI",
                        text = fullResponse,
                        modelBadge = modelToUse.badge
                    )
                    _uiState.update {
                        it.copy(
                            chatMessages = it.chatMessages + aiMsg,
                            isAiStreaming = false,
                            currentAiStreamText = ""
                        )
                    }
                    speakResponse(fullResponse.take(180))
                } else {
                    _uiState.update {
                        it.copy(
                            isAiStreaming = false,
                            aiError = "Model returned empty response."
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("KarenVM", "AI stream failed", e)
                val isQuotaOrBilling = e.message?.contains("402") == true || e.message?.contains("quota") == true
                val userFriendlyMessage = if (isQuotaOrBilling) {
                    "Workspace integration credits are currently reached or rate-limited. RAVI local offline knowledge and telemetry remaining active."
                } else {
                    e.localizedMessage ?: "AI generation failed."
                }
                _uiState.update {
                    it.copy(
                        isAiStreaming = false,
                        aiError = userFriendlyMessage
                    )
                }
            }
        }
    }

    fun speakResponse(text: String) {
        try {
            _uiState.update { it.copy(isSpeakingTts = true) }
            ttsEngine?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ravi_utterance")
            viewModelScope.launch {
                delay((text.length * 50L).coerceIn(1200L, 8000L))
                _uiState.update { it.copy(isSpeakingTts = false) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isSpeakingTts = false) }
        }
    }

    fun stopSpeaking() {
        ttsEngine?.stop()
        _uiState.update { it.copy(isSpeakingTts = false) }
    }

    // --- Pomodoro Focus Timer ---
    fun startFocusTimer() {
        if (_uiState.value.isTimerRunning) return
        audioSynth.playBeep(660f, 90, 0.12f)
        _uiState.update { it.copy(isTimerRunning = true) }

        timerJob = viewModelScope.launch {
            while (isActive && _uiState.value.focusSecondsRemaining > 0) {
                delay(1000)
                _uiState.update { it.copy(focusSecondsRemaining = it.focusSecondsRemaining - 1) }
            }
            if (_uiState.value.focusSecondsRemaining <= 0) {
                audioSynth.playBootChirp()
                _uiState.update {
                    it.copy(
                        isTimerRunning = false,
                        focusSecondsRemaining = 25 * 60,
                        completedPomodoros = it.completedPomodoros + 1
                    )
                }
                speakResponse("Focus session completed, Hero. Take a short 5-minute breather.")
            }
        }
    }

    fun pauseFocusTimer() {
        timerJob?.cancel()
        timerJob = null
        audioSynth.playBeep(440f, 80, 0.10f)
        _uiState.update { it.copy(isTimerRunning = false) }
    }

    fun resetFocusTimer() {
        timerJob?.cancel()
        timerJob = null
        _uiState.update { it.copy(isTimerRunning = false, focusSecondsRemaining = 25 * 60) }
    }

    override fun onCleared() {
        super.onCleared()
        audioSynth.stopSynthMusic()
        ttsEngine?.stop()
        ttsEngine?.shutdown()
    }
}
