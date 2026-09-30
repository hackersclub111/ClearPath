package com.musheer360.swiftslate.clarity.retention

import android.content.Context
import android.util.Log
import com.musheer360.swiftslate.clarity.model.CognitiveArchetype
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OneSignalRetentionManager private constructor(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.IO)

    fun initialize(appId: String = DEFAULT_ONESIGNAL_APP_ID) {
        try {
            OneSignal.Debug.logLevel = LogLevel.VERBOSE
            OneSignal.initWithContext(context, appId)
            OneSignal.Notifications.requestPermission(true)
            Log.i(TAG, "OneSignal initialized with App ID: $appId")
        } catch (e: Exception) {
            Log.w(TAG, "OneSignal initialization deferred: ${e.message}")
        }
    }

    /**
     * Tags user properties for OneSignal automated Journeys.
     */
    fun syncUserProfile(
        archetype: CognitiveArchetype,
        isPro: Boolean,
        totalSimplifications: Int
    ) {
        scope.launch {
            try {
                OneSignal.User.addTags(
                    mapOf(
                        TAG_ARCHETYPE to archetype.name,
                        TAG_IS_PRO to isPro.toString(),
                        TAG_SIMPLIFICATIONS_COUNT to totalSimplifications.toString(),
                        TAG_LAST_ACTIVE to System.currentTimeMillis().toString()
                    )
                )
                Log.d(TAG, "Synced profile tags to OneSignal: archetype=${archetype.name}, isPro=$isPro")
            } catch (e: Exception) {
                Log.w(TAG, "Failed to sync OneSignal tags: ${e.message}")
            }
        }
    }

    /**
     * Journey Trigger 1: Incomplete Form / Multi-step Task
     * Triggered when a user analyzes a complex screen (e.g., student fee portal).
     */
    fun trackFormSimplificationStarted(packageName: String, formTopic: String) {
        scope.launch {
            try {
                OneSignal.User.addTags(
                    mapOf(
                        TAG_PENDING_FORM_PKG to packageName,
                        TAG_PENDING_FORM_TOPIC to formTopic,
                        TAG_PENDING_FORM_TIMESTAMP to System.currentTimeMillis().toString()
                    )
                )
                Log.d(TAG, "Tracked form simplification: topic=$formTopic, pkg=$packageName")
            } catch (e: Exception) {
                Log.w(TAG, "Error tracking form event: ${e.message}")
            }
        }
    }

    /**
     * Journey Trigger 2: Task Completed
     * Clears pending form status when user completes the next action.
     */
    fun trackActionCompleted(actionLabel: String) {
        scope.launch {
            try {
                OneSignal.User.removeTags(listOf(TAG_PENDING_FORM_PKG, TAG_PENDING_FORM_TOPIC, TAG_PENDING_FORM_TIMESTAMP))
                OneSignal.User.addTag("last_completed_action", actionLabel)
                Log.d(TAG, "Action completed: $actionLabel, pending tags cleared.")
            } catch (e: Exception) {
                Log.w(TAG, "Error clearing pending form tags: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "OneSignalRetention"
        const val DEFAULT_ONESIGNAL_APP_ID = "clearpath-shipaton-2026-onesignal-app-id"

        const val TAG_ARCHETYPE = "cognitive_archetype"
        const val TAG_IS_PRO = "is_pro_subscriber"
        const val TAG_SIMPLIFICATIONS_COUNT = "simplifications_completed"
        const val TAG_LAST_ACTIVE = "last_clarity_timestamp"
        const val TAG_PENDING_FORM_PKG = "pending_form_app"
        const val TAG_PENDING_FORM_TOPIC = "pending_form_topic"
        const val TAG_PENDING_FORM_TIMESTAMP = "pending_form_time"

        @Volatile
        private var instance: OneSignalRetentionManager? = null

        fun getInstance(context: Context): OneSignalRetentionManager {
            return instance ?: synchronized(this) {
                instance ?: OneSignalRetentionManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
