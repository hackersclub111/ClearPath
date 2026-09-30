package com.musheer360.swiftslate.clarity.engine

import com.musheer360.swiftslate.clarity.model.CognitiveProfile
import org.json.JSONArray
import org.json.JSONObject

data class ClarityAnalysisResult(
    val summary: String,
    val priorities: List<String>,
    val nextAction: String,
    val targetButtonText: String,
    val noiseToIgnore: List<String>,
    val rawModelResponse: String? = null
)

object PriorityExtractionEngine {

    fun buildSystemPrompt(profile: CognitiveProfile): String {
        return """
You are ClearPath, an elite cognitive clarity engine for Android.
Your mission: Transform overwhelming, cluttered mobile screens (banking portals, college fees, government forms) into the single most important action the user must take next.

COGNITIVE USER PROFILE:
- Target Language: ${profile.language.displayName} (${profile.language.promptInstruction})
- Maximum Priorities: ${profile.complexity.maxBullets} bullet points
- Explanation Style: ${profile.explanationLevel.displayName}
- Max Visual Choices: ${profile.maxChoices}

INSTRUCTIONS:
1. Identify the screen's core purpose in ONE simple sentence.
2. Extract the ${profile.complexity.maxBullets} most critical things the user needs to know (e.g. Due dates, amounts, mandatory warnings).
3. Identify the EXACT NEXT physical action the user needs to take (e.g. "Tap 'Pay ₹12,500 Now'", "Select 'Fall 2026 Semester'").
4. Provide the exact text of the button or field to interact with for this action.
5. Identify noisy or secondary elements that the user should completely ignore.

CRITICAL: Return ONLY a valid JSON object with this exact structure (no markdown fences, no explanatory preambles):
{
  "summary": "1 clear sentence explaining what this screen is.",
  "priorities": ["Priority 1", "Priority 2"],
  "nextAction": "1 clear imperative action statement",
  "targetButtonText": "Exact text or id of the button/field to tap",
  "noiseToIgnore": ["Distraction 1", "Distraction 2"]
}
        """.trimIndent()
    }

    fun buildUserPrompt(snapshot: ScreenSnapshot): String {
        return """
Active App: ${snapshot.packageName}
Total Interactive Elements: ${snapshot.interactiveCount}

Screen Content Hierarchy:
${snapshot.rawTextDump}
        """.trimIndent()
    }

    fun parseResponse(rawResponse: String): ClarityAnalysisResult {
        return try {
            val cleanJson = cleanJsonResponse(rawResponse)
            val json = JSONObject(cleanJson)

            val summary = json.optString("summary", "Screen analyzed.")
            val prioritiesArray = json.optJSONArray("priorities") ?: JSONArray()
            val priorities = mutableListOf<String>()
            for (i in 0 until prioritiesArray.length()) {
                priorities.add(prioritiesArray.getString(i))
            }

            val nextAction = json.optString("nextAction", "Review screen details.")
            val targetButtonText = json.optString("targetButtonText", "")
            val noiseArray = json.optJSONArray("noiseToIgnore") ?: JSONArray()
            val noise = mutableListOf<String>()
            for (i in 0 until noiseArray.length()) {
                noise.add(noiseArray.getString(i))
            }

            ClarityAnalysisResult(
                summary = summary,
                priorities = priorities,
                nextAction = nextAction,
                targetButtonText = targetButtonText,
                noiseToIgnore = noise,
                rawModelResponse = rawResponse
            )
        } catch (e: Exception) {
            // Fallback for unstructured text
            fallbackParsing(rawResponse)
        }
    }

    private fun cleanJsonResponse(raw: String): String {
        var text = raw.trim()
        if (text.startsWith("```json")) {
            text = text.removePrefix("```json")
        } else if (text.startsWith("```")) {
            text = text.removePrefix("```")
        }
        if (text.endsWith("```")) {
            text = text.removeSuffix("```")
        }
        return text.trim()
    }

    private fun fallbackParsing(raw: String): ClarityAnalysisResult {
        val lines = raw.lines().map { it.trim() }.filter { it.isNotEmpty() }
        val summary = lines.firstOrNull() ?: "ClearPath screen clarity analysis"
        val remaining = lines.drop(1)
        val priorities = remaining.take(3)
        val nextAction = remaining.firstOrNull { it.contains("Tap", ignoreCase = true) || it.contains("Click", ignoreCase = true) }
            ?: "Proceed with the main highlighted action on screen"

        return ClarityAnalysisResult(
            summary = summary,
            priorities = if (priorities.isNotEmpty()) priorities else listOf("Complete required screen fields"),
            nextAction = nextAction,
            targetButtonText = "",
            noiseToIgnore = emptyList(),
            rawModelResponse = raw
        )
    }
}
