package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

class CyberAudioSynthesizer {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var musicJob: Job? = null
    var isPlayingMusic: Boolean = false
        private set
    var currentTrackTitle: String = "IDLE"
        private set

    fun playBeep(freq: Float = 580f, durationMs: Long = 120, volume: Float = 0.15f) {
        scope.launch {
            try {
                val sampleRate = 44100
                val numSamples = (sampleRate * (durationMs / 1000f)).toInt()
                val samples = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    // Apply smooth attack and decay envelope to prevent click
                    val progress = i.toFloat() / numSamples
                    val envelope = if (progress < 0.1f) progress / 0.1f else (1f - progress)
                    val wave = sin(2.0 * Math.PI * freq * time)
                    samples[i] = (wave * Short.MAX_VALUE * volume * envelope).toInt().toShort()
                }

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(numSamples * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(samples, 0, numSamples)
                track.play()
                delay(durationMs + 50)
                track.stop()
                track.release()
            } catch (e: Exception) {
                Log.w("AudioSynth", "Beep error: ${e.message}")
            }
        }
    }

    fun playDoubleChime() {
        scope.launch {
            playBeep(520f, 80, 0.12f)
            delay(90)
            playBeep(784f, 140, 0.16f)
        }
    }

    fun playBootChirp() {
        scope.launch {
            val tones = floatArrayOf(440f, 554f, 659f, 880f)
            for (t in tones) {
                playBeep(t, 90, 0.14f)
                delay(100)
            }
        }
    }

    fun startSynthTrack(title: String, baseFrequency: Float = 220f) {
        stopSynthMusic()
        currentTrackTitle = title
        isPlayingMusic = true

        musicJob = scope.launch {
            val sampleRate = 22050
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(4096)

            var track: AudioTrack? = null
            try {
                track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                track.play()

                // Generate looping cyber synthwave melody
                val chordNotes = floatArrayOf(
                    baseFrequency,
                    baseFrequency * 1.25f, // Major 3rd
                    baseFrequency * 1.5f,  // 5th
                    baseFrequency * 1.875f // Major 7th
                )

                val buffer = ShortArray(1024)
                var phase = 0.0
                var noteIndex = 0
                var sampleCounter = 0

                while (isActive) {
                    val currentFreq = chordNotes[noteIndex % chordNotes.size]
                    val phaseIncrement = 2.0 * Math.PI * currentFreq / sampleRate

                    for (i in buffer.indices) {
                        // Sub-bass drone + melodic sine + soft harmonic
                        val melody = sin(phase) * 0.4
                        val bass = sin(phase * 0.5) * 0.5
                        val shimmer = sin(phase * 2.0) * 0.1
                        val totalWave = (melody + bass + shimmer).coerceIn(-1.0, 1.0)

                        buffer[i] = (totalWave * Short.MAX_VALUE * 0.18).toInt().toShort()
                        phase += phaseIncrement
                        if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI
                    }

                    track.write(buffer, 0, buffer.size)

                    sampleCounter += buffer.size
                    // Change chord note every ~0.6 seconds
                    if (sampleCounter >= sampleRate * 0.6) {
                        sampleCounter = 0
                        noteIndex++
                    }
                }
            } catch (e: Exception) {
                Log.w("AudioSynth", "Synth track error: ${e.message}")
            } finally {
                try {
                    track?.stop()
                    track?.release()
                } catch (ignored: Exception) {}
            }
        }
    }

    fun stopSynthMusic() {
        musicJob?.cancel()
        musicJob = null
        isPlayingMusic = false
        currentTrackTitle = "STOPPED"
    }
}
