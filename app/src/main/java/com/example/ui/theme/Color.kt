package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Stark Core Cyber Palettes
val CyberDarkBg = Color(0xFF03050A)
val CyberDarkSurface = Color(0xFF070C18)
val CyberDarkSurfaceVariant = Color(0xFF0F172A)
val CyberDarkCard = Color(0xFF091122)

// Theme Colors
val SpiderRedPrimary = Color(0xFFFF003D)
val SpiderCyanSecondary = Color(0xFF00D2FF)
val ShadowGoldPrimary = Color(0xFFFFD700)
val ShadowSilverSecondary = Color(0xFF94A3B8)
val IronGoldPrimary = Color(0xFFF59E0B)
val IronRedSecondary = Color(0xFFEF4444)
val FuryRedPrimary = Color(0xFFDC2626)
val FuryOrangeSecondary = Color(0xFFFB923C)
val AzureBluePrimary = Color(0xFF2563EB)
val AzureCyanSecondary = Color(0xFF06B6D4)
val NeonPurplePrimary = Color(0xFFA855F7)
val NeonPinkSecondary = Color(0xFFEC4899)
val MatrixEmeraldPrimary = Color(0xFF10B981)
val MatrixGreenSecondary = Color(0xFF22C55E)
val RgbNeonPrimary = Color(0xFFFF007F)
val RgbNeonSecondary = Color(0xFF00F0FF)
val JetWhitePrimary = Color(0xFFF8FAFC)
val JetSlateSecondary = Color(0xFF64748B)

// UI Feedback
val CyberSuccess = Color(0xFF10B981)
val CyberWarning = Color(0xFFF59E0B)
val CyberError = Color(0xFFEF4444)
val CyberTextPrimary = Color(0xFFF1F5F9)
val CyberTextSecondary = Color(0xFF94A3B8)
val CyberTextMuted = Color(0xFF64748B)

enum class KarenOsTheme(
    val title: String,
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val borderGlow: Color
) {
    SPIDERMAN_MIX(
        title = "Spider-Man Mix",
        primary = SpiderRedPrimary,
        secondary = SpiderCyanSecondary,
        background = CyberDarkBg,
        surface = CyberDarkSurface,
        borderGlow = Color(0x66FF003D)
    ),
    SHADOW_SUPPORTER(
        title = "Shadow Supporter (SS)",
        primary = ShadowGoldPrimary,
        secondary = ShadowSilverSecondary,
        background = Color(0xFF030407),
        surface = Color(0xFF0A0D14),
        borderGlow = Color(0x66FFD700)
    ),
    IRON_SOLAR(
        title = "Iron Solar",
        primary = IronGoldPrimary,
        secondary = IronRedSecondary,
        background = Color(0xFF0A0604),
        surface = Color(0xFF160F09),
        borderGlow = Color(0x66F59E0B)
    ),
    FURY_RED(
        title = "Fury Red",
        primary = FuryRedPrimary,
        secondary = FuryOrangeSecondary,
        background = Color(0xFF0B0304),
        surface = Color(0xFF1A0709),
        borderGlow = Color(0x66DC2626)
    ),
    AZURE_CYBER(
        title = "Azure Cyber",
        primary = AzureBluePrimary,
        secondary = AzureCyanSecondary,
        background = Color(0xFF020713),
        surface = Color(0xFF061127),
        borderGlow = Color(0x662563EB)
    ),
    NEON_PURPLE(
        title = "Neon Purple",
        primary = NeonPurplePrimary,
        secondary = NeonPinkSecondary,
        background = Color(0xFF08040F),
        surface = Color(0xFF140A22),
        borderGlow = Color(0x66A855F7)
    ),
    MATRIX(
        title = "Matrix Green",
        primary = MatrixEmeraldPrimary,
        secondary = MatrixGreenSecondary,
        background = Color(0xFF020905),
        surface = Color(0xFF06180C),
        borderGlow = Color(0x6610B981)
    ),
    RGB_NEON(
        title = "RGB Neon Shift",
        primary = RgbNeonPrimary,
        secondary = RgbNeonSecondary,
        background = Color(0xFF07030C),
        surface = Color(0xFF130820),
        borderGlow = Color(0x66FF007F)
    ),
    JET_MONO(
        title = "Jet Stealth",
        primary = JetWhitePrimary,
        secondary = JetSlateSecondary,
        background = Color(0xFF020203),
        surface = Color(0xFF0D0F13),
        borderGlow = Color(0x6694A3B8)
    )
}

enum class MusicMood(val label: String, val color: Color) {
    ALL("All", Color(0xFF00D2FF)),
    FOCUS("Focus", Color(0xFF10B981)),
    CHILL("Chill", Color(0xFF38BDF8)),
    HYPE("Hype", Color(0xFFFF003D)),
    STUDY("Study", Color(0xFFF59E0B)),
    NIGHT("Night", Color(0xFFA855F7)),
    FURY("Fury", Color(0xFFEF4444))
}
