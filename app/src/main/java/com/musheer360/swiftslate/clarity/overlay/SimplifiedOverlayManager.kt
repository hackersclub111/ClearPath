package com.musheer360.swiftslate.clarity.overlay

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import com.musheer360.swiftslate.R
import com.musheer360.swiftslate.clarity.engine.ClarityAnalysisResult
import com.musheer360.swiftslate.clarity.engine.NextActionEngine
import com.musheer360.swiftslate.clarity.engine.PriorityExtractionEngine
import com.musheer360.swiftslate.clarity.engine.ScreenHierarchyExtractor
import com.musheer360.swiftslate.clarity.engine.TargetMatch
import com.musheer360.swiftslate.clarity.model.CognitiveProfile
import com.musheer360.swiftslate.clarity.model.CognitiveProfileManager
import com.musheer360.swiftslate.clarity.monetization.RevenueCatManager
import com.musheer360.swiftslate.clarity.retention.OneSignalRetentionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SimplifiedOverlayManager(
    private val service: AccessibilityService,
    private val scope: CoroutineScope,
    private val onRunAiInference: suspend (systemPrompt: String, userPrompt: String) -> String
) {
    private val windowManager = service.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val profileManager = CognitiveProfileManager(service)
    private val revenueCatManager = RevenueCatManager.getInstance(service)
    private val oneSignalManager = OneSignalRetentionManager.getInstance(service)
    private val mainHandler = Handler(Looper.getMainLooper())

    private var floatingPillView: View? = null
    private var clarityCardView: View? = null
    private var highlightBeaconView: View? = null

    private var lastTargetMatch: TargetMatch? = null
    private var isExpanded = false

    @SuppressLint("ClickableViewAccessibility")
    fun showFloatingTrigger() {
        if (floatingPillView != null) return

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
            x = 24
            y = 0
        }

        val pillLayout = LinearLayout(service).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dpToPx(16), dpToPx(10), dpToPx(16), dpToPx(10))
            elevation = dpToPx(8).toFloat()

            background = GradientDrawable().apply {
                setColor(Color.parseColor("#1E1B4B")) // Deep Indigo
                cornerRadius = dpToPx(24).toFloat()
                setStroke(dpToPx(2), Color.parseColor("#6366F1")) // Indigo border
            }

            val icon = TextView(service).apply {
                text = "⚡"
                textSize = 18f
                setTextColor(Color.WHITE)
            }
            val label = TextView(service).apply {
                text = " Simplify"
                textSize = 14f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.WHITE)
            }

            addView(icon)
            addView(label)
        }

        // Draggable floating pill
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        pillLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX - (event.rawX - initialTouchX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()
                    try {
                        windowManager.updateViewLayout(pillLayout, params)
                    } catch (e: Exception) { }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val dx = Math.abs(event.rawX - initialTouchX)
                    val dy = Math.abs(event.rawY - initialTouchY)
                    if (dx < 10 && dy < 10) {
                        triggerScreenSimplification()
                    }
                    true
                }
                else -> false
            }
        }

        floatingPillView = pillLayout
        try {
            windowManager.addView(floatingPillView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideFloatingTrigger() {
        floatingPillView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) { }
            floatingPillView = null
        }
        dismissClarityCard()
    }

    private fun triggerScreenSimplification() {
        if (!revenueCatManager.canPerformSimplification()) {
            showQuotaExceededCard()
            return
        }

        showLoadingOverlay()

        scope.launch {
            val rootNode = service.rootInActiveWindow
            val snapshot = ScreenHierarchyExtractor.extract(rootNode)
            val profile = profileManager.getActiveProfile()

            val systemPrompt = PriorityExtractionEngine.buildSystemPrompt(profile)
            val userPrompt = PriorityExtractionEngine.buildUserPrompt(snapshot)

            val rawAiResponse = try {
                onRunAiInference(systemPrompt, userPrompt)
            } catch (e: Exception) {
                "Error analyzing screen: ${e.message}"
            }

            val result = PriorityExtractionEngine.parseResponse(rawAiResponse)

            // Locate target node for next action
            val targetMatch = NextActionEngine.findTargetNode(rootNode, result.targetButtonText)
            lastTargetMatch = targetMatch

            // Record usage
            revenueCatManager.recordSimplificationUsed()
            oneSignalManager.trackFormSimplificationStarted(snapshot.packageName, result.summary)

            withContext(Dispatchers.Main) {
                displayClarityCard(result, profile, snapshot.packageName)
            }
        }
    }

    private fun showLoadingOverlay() {
        dismissClarityCard()

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM
            y = dpToPx(32)
        }

        val card = LinearLayout(service).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(24), dpToPx(20), dpToPx(24), dpToPx(20))
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#F10F172A")) // Slate 900 Glass
                cornerRadius = dpToPx(20).toFloat()
                setStroke(dpToPx(1), Color.parseColor("#38BDF8"))
            }

            val row = LinearLayout(service).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL

                val spinner = ProgressBar(service).apply {
                    isIndeterminate = true
                }
                val label = TextView(service).apply {
                    text = "  ClearPath: Distilling screen essentials…"
                    setTextColor(Color.WHITE)
                    textSize = 15f
                    typeface = Typeface.DEFAULT_BOLD
                }
                addView(spinner)
                addView(label)
            }
            addView(row)
        }

        clarityCardView = card
        try {
            windowManager.addView(card, params)
        } catch (e: Exception) { }
    }

    private fun displayClarityCard(
        result: ClarityAnalysisResult,
        profile: CognitiveProfile,
        packageName: String
    ) {
        dismissClarityCard()

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM
            y = dpToPx(16)
        }

        val rootCard = LinearLayout(service).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(20), dpToPx(18), dpToPx(20), dpToPx(18))
            elevation = dpToPx(16).toFloat()

            background = GradientDrawable().apply {
                setColor(Color.parseColor("#FA0F172A")) // Dark Obsidian/Navy
                cornerRadius = dpToPx(24).toFloat()
                setStroke(dpToPx(2), Color.parseColor("#38BDF8")) // Light Sky Blue border
            }
        }

        // 1. Header with Archetype pill & Dismiss
        val headerRow = LinearLayout(service).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL

            val titlePill = TextView(service).apply {
                text = "✨ ${profile.archetype.title}"
                textSize = 12f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.parseColor("#38BDF8"))
                setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4))
                background = GradientDrawable().apply {
                    setColor(Color.parseColor("#1E293B"))
                    cornerRadius = dpToPx(12).toFloat()
                }
            }

            val quotaPill = TextView(service).apply {
                text = if (revenueCatManager.isProActive) "PRO" else "${revenueCatManager.getRemainingFreeSimplifications()} left"
                textSize = 11f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(if (revenueCatManager.isProActive) Color.parseColor("#4ADE80") else Color.parseColor("#FBBF24"))
                setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    leftMargin = dpToPx(8)
                    weight = 1f
                }
            }

            val closeBtn = TextView(service).apply {
                text = "✕"
                textSize = 18f
                setTextColor(Color.parseColor("#94A3B8"))
                setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4))
                setOnClickListener { dismissClarityCard() }
            }

            addView(titlePill)
            addView(quotaPill)
            addView(closeBtn)
        }
        rootCard.addView(headerRow)

        // 2. Summary Headline (1 sentence)
        val summaryView = TextView(service).apply {
            text = result.summary
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dpToPx(12)
                bottomMargin = dpToPx(8)
            }
        }
        rootCard.addView(summaryView)

        // 3. Priorities (up to 3 bullets)
        val prioritiesContainer = LinearLayout(service).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(14)
            }
        }

        for (priority in result.priorities) {
            val bulletRow = LinearLayout(service).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = dpToPx(4)
                }

                val bulletIcon = TextView(service).apply {
                    text = "• "
                    textSize = 14f
                    setTextColor(Color.parseColor("#38BDF8"))
                }
                val bulletText = TextView(service).apply {
                    text = priority
                    textSize = 13.5f
                    setTextColor(Color.parseColor("#E2E8F0"))
                }
                addView(bulletIcon)
                addView(bulletText)
            }
            prioritiesContainer.addView(bulletRow)
        }
        rootCard.addView(prioritiesContainer)

        // 4. Primary "Do This Next" Action Button
        val actionButton = Button(service).apply {
            text = "👉 ${result.nextAction}"
            textSize = 15f * profile.buttonSize.scaleFactor
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#0F172A"))
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#38BDF8")) // Sky Blue Accent
                cornerRadius = dpToPx(14).toFloat()
            }
            elevation = dpToPx(4).toFloat()

            setOnClickListener {
                lastTargetMatch?.let { target ->
                    NextActionEngine.executeAssistedTap(target)
                    oneSignalManager.trackActionCompleted(result.nextAction)
                }
                dismissClarityCard()
            }
        }
        rootCard.addView(actionButton)

        clarityCardView = rootCard
        try {
            windowManager.addView(rootCard, params)
        } catch (e: Exception) { }
    }

    private fun showQuotaExceededCard() {
        dismissClarityCard()

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM
            y = dpToPx(20)
        }

        val card = LinearLayout(service).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(20), dpToPx(18), dpToPx(20), dpToPx(18))
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#1E1B4B"))
                cornerRadius = dpToPx(20).toFloat()
                setStroke(dpToPx(2), Color.parseColor("#F59E0B"))
            }

            val title = TextView(service).apply {
                text = "⚡ Daily Free Limit Reached (5/5)"
                textSize = 16f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.parseColor("#FBBF24"))
            }
            val desc = TextView(service).apply {
                text = "Upgrade to ClearPath Pro for unlimited cognitive clarity, multi-profile family mode, and assisted auto-tap."
                textSize = 13.5f
                setTextColor(Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = dpToPx(6)
                    bottomMargin = dpToPx(12)
                }
            }

            val judgePassBtn = Button(service).apply {
                text = "Devpost Judge? Tap for Instant Pro Pass"
                textSize = 13f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.WHITE)
                background = GradientDrawable().apply {
                    setColor(Color.parseColor("#4F46E5"))
                    cornerRadius = dpToPx(12).toFloat()
                }
                setOnClickListener {
                    revenueCatManager.applyJudgePass(RevenueCatManager.JUDGE_CODE)
                    dismissClarityCard()
                    triggerScreenSimplification()
                }
            }

            addView(title)
            addView(desc)
            addView(judgePassBtn)
        }

        clarityCardView = card
        try {
            windowManager.addView(card, params)
        } catch (e: Exception) { }
    }

    fun dismissClarityCard() {
        clarityCardView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) { }
            clarityCardView = null
        }
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            service.resources.displayMetrics
        ).toInt()
    }
}
