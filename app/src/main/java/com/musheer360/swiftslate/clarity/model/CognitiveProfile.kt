package com.musheer360.swiftslate.clarity.model

import android.content.Context
import android.content.SharedPreferences

enum class ClarityLanguage(val displayName: String, val promptInstruction: String) {
    ENGLISH("Standard English", "Provide output in concise, clear English."),
    HINDI("Hindi (हिन्दी)", "Provide output in clear, everyday spoken Hindi (Devanagari script) with simple phrasing."),
    SIMPLE_ENGLISH("Simple Plain English", "Use elementary vocabulary (5th grade reading level), short sentences, and zero jargon."),
    SPANISH("Spanish (Español)", "Provide output in clear, accessible Spanish.")
}

enum class ComplexityLevel(val displayName: String, val maxBullets: Int) {
    MINIMAL("Ultra-Simple (1 Bullet)", 1),
    BALANCED("Standard Clarity (3 Bullets)", 3),
    DETAILED("Detailed Breakdown (5 Bullets)", 5)
}

enum class ButtonSize(val displayName: String, val scaleFactor: Float) {
    NORMAL("Normal", 1.0f),
    LARGE("Large (Accessible)", 1.25f),
    EXTRA_LARGE("Huge (Senior/Motor Assist)", 1.5f)
}

enum class ExplanationLevel(val displayName: String) {
    CONCISE("Direct & Action-Only"),
    PLAIN_LANGUAGE("Plain Everyday Words"),
    STEP_BY_STEP("Step-by-Step Guidance")
}

enum class CognitiveArchetype(val title: String, val subtitle: String) {
    STUDENT("Student Portal Mode", "Filters LMS/college clutter, highlights deadlines, fees, and next actions"),
    SENIOR("Senior & Banking Mode", "Large buttons, plain language, eliminates deceptive dark patterns"),
    ADHD("ADHD Focus Mode", "Low stimulation, hides decorative clutter, strictly isolates the next step"),
    NON_NATIVE("Non-Native Clarity (Hindi)", "Translates complex bureaucratic terms into natural everyday Hindi"),
    CUSTOM("Custom Profile", "Personalized configuration tailored to your specific cognitive needs")
}

data class CognitiveProfile(
    val archetype: CognitiveArchetype = CognitiveArchetype.STUDENT,
    val language: ClarityLanguage = ClarityLanguage.ENGLISH,
    val complexity: ComplexityLevel = ComplexityLevel.BALANCED,
    val maxChoices: Int = 3,
    val buttonSize: ButtonSize = ButtonSize.LARGE,
    val explanationLevel: ExplanationLevel = ExplanationLevel.STEP_BY_STEP,
    val autoHighlightTarget: Boolean = true,
    val readAloudNextAction: Boolean = false
) {
    companion object {
        val STUDENT_PRESET = CognitiveProfile(
            archetype = CognitiveArchetype.STUDENT,
            language = ClarityLanguage.ENGLISH,
            complexity = ComplexityLevel.BALANCED,
            maxChoices = 3,
            buttonSize = ButtonSize.LARGE,
            explanationLevel = ExplanationLevel.STEP_BY_STEP,
            autoHighlightTarget = true
        )

        val SENIOR_PRESET = CognitiveProfile(
            archetype = CognitiveArchetype.SENIOR,
            language = ClarityLanguage.SIMPLE_ENGLISH,
            complexity = ComplexityLevel.MINIMAL,
            maxChoices = 2,
            buttonSize = ButtonSize.EXTRA_LARGE,
            explanationLevel = ExplanationLevel.PLAIN_LANGUAGE,
            autoHighlightTarget = true,
            readAloudNextAction = true
        )

        val ADHD_PRESET = CognitiveProfile(
            archetype = CognitiveArchetype.ADHD,
            language = ClarityLanguage.ENGLISH,
            complexity = ComplexityLevel.MINIMAL,
            maxChoices = 1,
            buttonSize = ButtonSize.LARGE,
            explanationLevel = ExplanationLevel.CONCISE,
            autoHighlightTarget = true
        )

        val NON_NATIVE_PRESET = CognitiveProfile(
            archetype = CognitiveArchetype.NON_NATIVE,
            language = ClarityLanguage.HINDI,
            complexity = ComplexityLevel.BALANCED,
            maxChoices = 3,
            buttonSize = ButtonSize.LARGE,
            explanationLevel = ExplanationLevel.PLAIN_LANGUAGE,
            autoHighlightTarget = true
        )
    }
}

class CognitiveProfileManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getActiveProfile(): CognitiveProfile {
        val archetypeOrdinal = prefs.getInt(KEY_ARCHETYPE, CognitiveArchetype.STUDENT.ordinal)
        val archetype = CognitiveArchetype.entries.getOrElse(archetypeOrdinal) { CognitiveArchetype.STUDENT }
        
        val langOrdinal = prefs.getInt(KEY_LANGUAGE, ClarityLanguage.ENGLISH.ordinal)
        val language = ClarityLanguage.entries.getOrElse(langOrdinal) { ClarityLanguage.ENGLISH }

        val complexityOrdinal = prefs.getInt(KEY_COMPLEXITY, ComplexityLevel.BALANCED.ordinal)
        val complexity = ComplexityLevel.entries.getOrElse(complexityOrdinal) { ComplexityLevel.BALANCED }

        val maxChoices = prefs.getInt(KEY_MAX_CHOICES, 3)

        val btnSizeOrdinal = prefs.getInt(KEY_BUTTON_SIZE, ButtonSize.LARGE.ordinal)
        val buttonSize = ButtonSize.entries.getOrElse(btnSizeOrdinal) { ButtonSize.LARGE }

        val explainOrdinal = prefs.getInt(KEY_EXPLAIN_LEVEL, ExplanationLevel.STEP_BY_STEP.ordinal)
        val explanationLevel = ExplanationLevel.entries.getOrElse(explainOrdinal) { ExplanationLevel.STEP_BY_STEP }

        val autoHighlight = prefs.getBoolean(KEY_AUTO_HIGHLIGHT, true)
        val readAloud = prefs.getBoolean(KEY_READ_ALOUD, false)

        return CognitiveProfile(
            archetype = archetype,
            language = language,
            complexity = complexity,
            maxChoices = maxChoices,
            buttonSize = buttonSize,
            explanationLevel = explanationLevel,
            autoHighlightTarget = autoHighlight,
            readAloudNextAction = readAloud
        )
    }

    fun saveProfile(profile: CognitiveProfile) {
        prefs.edit()
            .putInt(KEY_ARCHETYPE, profile.archetype.ordinal)
            .putInt(KEY_LANGUAGE, profile.language.ordinal)
            .putInt(KEY_COMPLEXITY, profile.complexity.ordinal)
            .putInt(KEY_MAX_CHOICES, profile.maxChoices)
            .putInt(KEY_BUTTON_SIZE, profile.buttonSize.ordinal)
            .putInt(KEY_EXPLAIN_LEVEL, profile.explanationLevel.ordinal)
            .putBoolean(KEY_AUTO_HIGHLIGHT, profile.autoHighlightTarget)
            .putBoolean(KEY_READ_ALOUD, profile.readAloudNextAction)
            .apply()
    }

    fun applyPreset(archetype: CognitiveArchetype) {
        val preset = when (archetype) {
            CognitiveArchetype.STUDENT -> CognitiveProfile.STUDENT_PRESET
            CognitiveArchetype.SENIOR -> CognitiveProfile.SENIOR_PRESET
            CognitiveArchetype.ADHD -> CognitiveProfile.ADHD_PRESET
            CognitiveArchetype.NON_NATIVE -> CognitiveProfile.NON_NATIVE_PRESET
            CognitiveArchetype.CUSTOM -> getActiveProfile().copy(archetype = CognitiveArchetype.CUSTOM)
        }
        saveProfile(preset)
    }

    companion object {
        private const val PREFS_NAME = "clearpath_cognitive_profile"
        private const val KEY_ARCHETYPE = "profile_archetype"
        private const val KEY_LANGUAGE = "profile_language"
        private const val KEY_COMPLEXITY = "profile_complexity"
        private const val KEY_MAX_CHOICES = "profile_max_choices"
        private const val KEY_BUTTON_SIZE = "profile_button_size"
        private const val KEY_EXPLAIN_LEVEL = "profile_explain_level"
        private const val KEY_AUTO_HIGHLIGHT = "profile_auto_highlight"
        private const val KEY_READ_ALOUD = "profile_read_aloud"
    }
}
