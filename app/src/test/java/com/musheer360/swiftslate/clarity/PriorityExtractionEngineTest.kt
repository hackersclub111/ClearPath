package com.musheer360.swiftslate.clarity

import com.musheer360.swiftslate.clarity.engine.PriorityExtractionEngine
import com.musheer360.swiftslate.clarity.engine.ScreenSnapshot
import com.musheer360.swiftslate.clarity.model.CognitiveProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PriorityExtractionEngineTest {

    @Test
    fun testParseValidCleanJsonResponse() {
        val jsonPayload = """
        {
          "summary": "Semester tuition fee payment deadline is tomorrow.",
          "priorities": ["Pay ${'$'}420.00 before 5PM", "Avoid ${'$'}50 late penalty fee"],
          "nextAction": "Tap 'Pay Now'",
          "targetButtonText": "Pay Now",
          "noiseToIgnore": ["14 elective links", "Faculty directory"]
        }
        """.trimIndent()

        val result = PriorityExtractionEngine.parseResponse(jsonPayload)
        assertEquals("Semester tuition fee payment deadline is tomorrow.", result.summary)
        assertEquals(2, result.priorities.size)
        assertEquals("Tap 'Pay Now'", result.nextAction)
        assertEquals("Pay Now", result.targetButtonText)
        assertEquals(2, result.noiseToIgnore.size)
    }

    @Test
    fun testParseMarkdownFencedJsonResponse() {
        val markdownJson = """
        ```json
        {
          "summary": "Verify KYC documents for bank account.",
          "priorities": ["Upload Aadhaar/PAN Card", "Ensure name matches passbook"],
          "nextAction": "Tap 'Upload Document'",
          "targetButtonText": "Upload Document",
          "noiseToIgnore": ["Credit card offers"]
        }
        ```
        """.trimIndent()

        val result = PriorityExtractionEngine.parseResponse(markdownJson)
        assertEquals("Verify KYC documents for bank account.", result.summary)
        assertEquals("Upload Document", result.targetButtonText)
    }

    @Test
    fun testFallbackUnstructuredResponse() {
        val rawText = """
        This screen is the student portal exam enrollment form.
        Submit before October 5th.
        Tap Submit Registration to finish.
        """.trimIndent()

        val result = PriorityExtractionEngine.parseResponse(rawText)
        assertEquals("This screen is the student portal exam enrollment form.", result.summary)
        assertTrue(result.nextAction.contains("Submit Registration") || result.nextAction.contains("Tap"))
    }

    @Test
    fun testSystemPromptAdheresToProfileConstraints() {
        val profile = CognitiveProfile.STUDENT_PRESET
        val prompt = PriorityExtractionEngine.buildSystemPrompt(profile)

        assertTrue(prompt.contains("Standard English"))
        assertTrue(prompt.contains("3 bullet points"))
        assertTrue(prompt.contains("ClearPath"))
    }
}
