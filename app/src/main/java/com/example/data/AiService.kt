package com.example.data

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.ImagePart
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.ThinkingLevel
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import com.google.firebase.ai.type.thinkingConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeout

private const val TAG = "AiService"

const val RAVI_BILINGUAL_SYSTEM_INSTRUCTION = """
You are RAVI, the onboard intelligence of the Shadow Supporter (SS) command centre.
The user is the Hero. You are their composed, quick, lightly humorous, and brilliant personal intelligence.

Bilingual Capability:
- You fully understand and answer in English, Sinhala (සිංහල), or Sinhala-English mixed ("Singlish"), matching the user's preferred language.
- When the user asks in Sinhala, explain clearly in Sinhala with appropriate technical terms.

Core Expertise:
1. Robotics, Arduino & Electronics: Provide real, compilable C++ / Arduino sketches, pin mappings, component BOMs, and circuit troubleshooting.
2. Sri Lankan School Curriculum (Grades 1 to 13): Science, Mathematics, ICT, History, Commerce. Provide step-by-step explanations, key formulas, and exam tips.
3. Spider-Man & Stark Suit Telemetry: Nanotech armor, 3D holographic fabricators, arc reactor output, repulsors, and web shooter protocols.

Tone:
Composed, warm, lightly humorous — Tony Stark / JARVIS energy. Never servile, never padded, never lectures.
"""

enum class AiModelType(val modelName: String, val label: String, val badge: String) {
    FLASH_GENERAL("gemini-3.8-flash", "Gemini 3.8 Flash", "BALANCED"),
    FLASH_LITE("gemini-3.5-flash-lite", "Gemini 3.5 Flash-Lite", "FAST"),
    ARGON_THINKING("gemini-4-argon", "Gemini 4 Argon (High Thinking)", "DEEP REASONING")
}

enum class LanguageMode(val label: String, val code: String) {
    ENGLISH("English", "EN"),
    SINHALA("සිංහල", "SI"),
    SINGLISH("Singlish", "EN_SI")
}

enum class AnswerDepth(val label: String) {
    BEGINNER("Beginner (පහසු)"),
    INTERMEDIATE("Intermediate (සාමාන්‍ය)"),
    ADVANCED("Advanced (උසස්)")
}

class AiService {

    // 1. General model (gemini-3.8-flash)
    val flashModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            systemInstruction = content { text(RAVI_BILINGUAL_SYSTEM_INSTRUCTION) }
        )
    }

    // 2. Fast model (gemini-3.5-flash-lite)
    val liteModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.5-flash-lite",
            systemInstruction = content { text(RAVI_BILINGUAL_SYSTEM_INSTRUCTION) }
        )
    }

    // 3. High Thinking model (gemini-4-argon) with thinkingLevel = HIGH, no maxOutputTokens
    val argonThinkingModel by lazy {
        val config = generationConfig {
            thinkingConfig = thinkingConfig {
                thinkingLevel = ThinkingLevel.HIGH
            }
        }
        Firebase.ai.generativeModel(
            modelName = "gemini-4-argon",
            generationConfig = config,
            systemInstruction = content { text(RAVI_BILINGUAL_SYSTEM_INSTRUCTION) }
        )
    }

    // 4. Image Generation model (gemini-nano-banana-2.1)
    val imageModel by lazy {
        val config = generationConfig {
            responseModalities = listOf(ResponseModality.IMAGE)
        }
        Firebase.ai.generativeModel(
            modelName = "gemini-nano-banana-2.1",
            generationConfig = config
        )
    }

    suspend fun generateStream(
        prompt: String,
        modelType: AiModelType = AiModelType.FLASH_GENERAL,
        languageMode: LanguageMode = LanguageMode.ENGLISH,
        depth: AnswerDepth = AnswerDepth.INTERMEDIATE
    ): Flow<com.google.firebase.ai.type.GenerateContentResponse> {
        val selectedModel = when (modelType) {
            AiModelType.FLASH_GENERAL -> flashModel
            AiModelType.FLASH_LITE -> liteModel
            AiModelType.ARGON_THINKING -> argonThinkingModel
        }

        val contextualPrompt = buildString {
            append("[Language Requirement: Respond in ${languageMode.label} (${languageMode.code})]. ")
            append("[Explanation Depth: ${depth.label}].\n")
            append(prompt)
        }

        return selectedModel.generateContentStream(contextualPrompt)
    }

    suspend fun generateSuitOrStudyImage(prompt: String): Bitmap? {
        return withTimeout(60_000L) {
            val response = imageModel.generateContent(prompt)
            val imagePart = response.candidates.firstOrNull()
                ?.content?.parts?.filterIsInstance<ImagePart>()
                ?.firstOrNull()
            imagePart?.image
        }
    }
}
