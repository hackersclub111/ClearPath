package com.musheer360.swiftslate.clarity.monetization

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.models.Offerings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RevenueCatManager private constructor(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isProActive: Boolean = false
        private set

    init {
        // Load initial state (persisted or judge unlocked)
        isProActive = prefs.getBoolean(KEY_IS_PRO, false) || isJudgeBypassed()
    }

    fun initialize(apiKey: String = DEFAULT_REVENUECAT_KEY) {
        try {
            Purchases.logLevel = com.revenuecat.purchases.LogLevel.DEBUG
            Purchases.configure(
                PurchasesConfiguration.Builder(context, apiKey)
                    .appUserID(getOrCreateAnonymousUserId())
                    .build()
            )
            refreshCustomerInfo()
            Log.i(TAG, "RevenueCat initialized successfully. Pro status: $isProActive")
        } catch (e: Exception) {
            Log.w(TAG, "RevenueCat initialization deferred or offline: ${e.message}")
        }
    }

    fun refreshCustomerInfo(onResult: ((Boolean) -> Unit)? = null) {
        if (isJudgeBypassed()) {
            isProActive = true
            onResult?.invoke(true)
            return
        }

        try {
            Purchases.sharedInstance.getCustomerInfo(object : ReceiveCustomerInfoCallback {
                override fun onReceived(customerInfo: CustomerInfo) {
                    val hasPro = customerInfo.entitlements[ENTITLEMENT_PRO]?.isActive == true
                    isProActive = hasPro
                    prefs.edit().putBoolean(KEY_IS_PRO, hasPro).apply()
                    Log.d(TAG, "CustomerInfo received. Entitlement '$ENTITLEMENT_PRO' active: $hasPro")
                    onResult?.invoke(hasPro)
                }

                override fun onError(error: PurchasesError) {
                    Log.w(TAG, "CustomerInfo fetch error: ${error.message}")
                    onResult?.invoke(isProActive)
                }
            })
        } catch (e: Exception) {
            Log.w(TAG, "Purchases instance error: ${e.message}")
            onResult?.invoke(isProActive)
        }
    }

    fun fetchOfferings(onSuccess: (Offerings) -> Unit, onError: (String) -> Unit) {
        try {
            Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
                override fun onReceived(offerings: Offerings) {
                    onSuccess(offerings)
                }

                override fun onError(error: PurchasesError) {
                    onError(error.message)
                }
            })
        } catch (e: Exception) {
            onError(e.message ?: "Failed to fetch offerings")
        }
    }

    /**
     * Daily free quota tracking.
     * Free users get 5 simplifications per day. Pro users get unlimited.
     */
    fun canPerformSimplification(): Boolean {
        if (isProActive) return true

        val today = getTodayDateString()
        val lastDate = prefs.getString(KEY_USAGE_DATE, "") ?: ""
        var count = prefs.getInt(KEY_USAGE_COUNT, 0)

        if (today != lastDate) {
            count = 0
            prefs.edit().putString(KEY_USAGE_DATE, today).putInt(KEY_USAGE_COUNT, 0).apply()
        }

        return count < FREE_DAILY_LIMIT
    }

    fun recordSimplificationUsed(): Int {
        val today = getTodayDateString()
        val lastDate = prefs.getString(KEY_USAGE_DATE, "") ?: ""
        var count = prefs.getInt(KEY_USAGE_COUNT, 0)

        if (today != lastDate) {
            count = 0
        }
        count++
        prefs.edit().putString(KEY_USAGE_DATE, today).putInt(KEY_USAGE_COUNT, count).apply()
        return count
    }

    fun getRemainingFreeSimplifications(): Int {
        if (isProActive) return Int.MAX_VALUE
        val today = getTodayDateString()
        val lastDate = prefs.getString(KEY_USAGE_DATE, "") ?: ""
        val count = if (today == lastDate) prefs.getInt(KEY_USAGE_COUNT, 0) else 0
        return (FREE_DAILY_LIMIT - count).coerceAtLeast(0)
    }

    /**
     * Devpost Judge Instant Pass:
     * Guarantees zero friction for judges evaluating the submission.
     */
    fun applyJudgePass(code: String): Boolean {
        if (code.trim().uppercase() == JUDGE_CODE) {
            prefs.edit().putBoolean(KEY_JUDGE_PASS, true).putBoolean(KEY_IS_PRO, true).apply()
            isProActive = true
            Log.i(TAG, "Devpost Judge Pass activated: Full Pro features unlocked.")
            return true
        }
        return false
    }

    fun isJudgeBypassed(): Boolean {
        return prefs.getBoolean(KEY_JUDGE_PASS, false)
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    private fun getOrCreateAnonymousUserId(): String {
        var id = prefs.getString(KEY_ANON_USER_ID, null)
        if (id == null) {
            id = "cp_usr_" + java.util.UUID.randomUUID().toString().take(12)
            prefs.edit().putString(KEY_ANON_USER_ID, id).apply()
        }
        return id
    }

    companion object {
        private const val TAG = "RevenueCatManager"
        private const val PREFS_NAME = "clearpath_revenuecat"
        private const val KEY_IS_PRO = "is_pro_active"
        private const val KEY_USAGE_DATE = "usage_date"
        private const val KEY_USAGE_COUNT = "usage_count"
        private const val KEY_ANON_USER_ID = "anon_user_id"
        private const val KEY_JUDGE_PASS = "judge_pass_unlocked"

        const val ENTITLEMENT_PRO = "clearpath_pro"
        const val FREE_DAILY_LIMIT = 5
        const val JUDGE_CODE = "SHIPATON2026_JUDGE"
        const val DEFAULT_REVENUECAT_KEY = "goog_pub_clearpath_shipaton2026"

        @Volatile
        private var instance: RevenueCatManager? = null

        fun getInstance(context: Context): RevenueCatManager {
            return instance ?: synchronized(this) {
                instance ?: RevenueCatManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
