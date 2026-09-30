package com.musheer360.swiftslate.clarity

import com.musheer360.swiftslate.clarity.model.ButtonSize
import com.musheer360.swiftslate.clarity.model.ClarityLanguage
import com.musheer360.swiftslate.clarity.model.CognitiveArchetype
import com.musheer360.swiftslate.clarity.model.CognitiveProfile
import com.musheer360.swiftslate.clarity.model.ComplexityLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CognitiveProfileTest {

    @Test
    fun testStudentPresetDefaults() {
        val student = CognitiveProfile.STUDENT_PRESET
        assertEquals(CognitiveArchetype.STUDENT, student.archetype)
        assertEquals(ClarityLanguage.ENGLISH, student.language)
        assertEquals(ComplexityLevel.BALANCED, student.complexity)
        assertEquals(3, student.complexity.maxBullets)
        assertEquals(ButtonSize.LARGE, student.buttonSize)
        assertTrue(student.autoHighlightTarget)
    }

    @Test
    fun testSeniorPresetDefaults() {
        val senior = CognitiveProfile.SENIOR_PRESET
        assertEquals(CognitiveArchetype.SENIOR, senior.archetype)
        assertEquals(ClarityLanguage.SIMPLE_ENGLISH, senior.language)
        assertEquals(ComplexityLevel.MINIMAL, senior.complexity)
        assertEquals(1, senior.complexity.maxBullets)
        assertEquals(ButtonSize.EXTRA_LARGE, senior.buttonSize)
        assertTrue(senior.readAloudNextAction)
    }

    @Test
    fun testADHDLowStimulationPreset() {
        val adhd = CognitiveProfile.ADHD_PRESET
        assertEquals(CognitiveArchetype.ADHD, adhd.archetype)
        assertEquals(1, adhd.maxChoices)
        assertEquals(ComplexityLevel.MINIMAL, adhd.complexity)
    }

    @Test
    fun testHindiNonNativePreset() {
        val hindi = CognitiveProfile.NON_NATIVE_PRESET
        assertEquals(CognitiveArchetype.NON_NATIVE, hindi.archetype)
        assertEquals(ClarityLanguage.HINDI, hindi.language)
        assertTrue(hindi.language.promptInstruction.contains("Hindi"))
    }
}
