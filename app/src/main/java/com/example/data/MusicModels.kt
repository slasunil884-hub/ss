package com.example.data

import com.example.ui.theme.MusicMood

data class MusicTrack(
    val id: String,
    val title: String,
    val artist: String,
    val durationText: String,
    val mood: MusicMood,
    val baseFrequency: Float,
    val isFavorite: Boolean = false,
    val playCount: Int = 0
) {
    val totalSeconds: Int
        get() {
            val parts = durationText.split(":")
            return if (parts.size == 2) {
                val m = parts[0].trim().toIntOrNull() ?: 0
                val s = parts[1].trim().toIntOrNull() ?: 0
                (m * 60 + s).coerceAtLeast(1)
            } else 210
        }
}

object StarkMusicDatabase {
    val initialPlaylist = listOf(
        MusicTrack(
            id = "trk_spider_hero",
            title = "Far From Home Hero Theme",
            artist = "Stark Records / Michael G.",
            durationText = "3:42",
            mood = MusicMood.HYPE,
            baseFrequency = 220f,
            isFavorite = true,
            playCount = 14
        ),
        MusicTrack(
            id = "trk_cyber_synth",
            title = "Cyberpunk Neo-Tokyo Mix",
            artist = "Shadow Supporter SS Beats",
            durationText = "4:15",
            mood = MusicMood.CHILL,
            baseFrequency = 164.8f,
            isFavorite = true,
            playCount = 22
        ),
        MusicTrack(
            id = "trk_lab_focus",
            title = "Stark Lab Neural Focus Beats (432Hz)",
            artist = "RAVI AI Binaural Engine",
            durationText = "5:30",
            mood = MusicMood.FOCUS,
            baseFrequency = 216f,
            isFavorite = false,
            playCount = 35
        ),
        MusicTrack(
            id = "trk_iron_heavy",
            title = "Mark 85 Heavy Nanotech Pulse",
            artist = "AC/DC Stark Tribute Synthesizer",
            durationText = "3:58",
            mood = MusicMood.FURY,
            baseFrequency = 246.9f,
            isFavorite = false,
            playCount = 8
        ),
        MusicTrack(
            id = "trk_sl_chill",
            title = "Colombo Cyber Monsoon Lo-Fi",
            artist = "Shadow Supporter SS Lounge",
            durationText = "4:45",
            mood = MusicMood.NIGHT,
            baseFrequency = 174.6f,
            isFavorite = true,
            playCount = 19
        ),
        MusicTrack(
            id = "trk_exam_study",
            title = "GCE O/L High Concentration Alpha Wave",
            artist = "RAVI Study Tutor",
            durationText = "6:00",
            mood = MusicMood.STUDY,
            baseFrequency = 196f,
            isFavorite = false,
            playCount = 41
        ),
        MusicTrack(
            id = "trk_spider_swing",
            title = "Spidey Web-Shooter Neon Groove",
            artist = "Peter Parker & Karen Synth",
            durationText = "3:15",
            mood = MusicMood.HYPE,
            baseFrequency = 261.6f,
            isFavorite = true,
            playCount = 28
        ),
        MusicTrack(
            id = "trk_sigiriya_chill",
            title = "Sigiriya Sky Fortress Ambient Waves",
            artist = "Shadow Supporter Cyber Ensemble",
            durationText = "5:10",
            mood = MusicMood.CHILL,
            baseFrequency = 185.0f,
            isFavorite = false,
            playCount = 16
        )
    )
}
