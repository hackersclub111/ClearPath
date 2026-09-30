package com.musheer360.swiftslate.clarity

import com.musheer360.swiftslate.clarity.monetization.RevenueCatManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RevenueCatManagerTest {

    @Test
    fun testJudgePassCodeValidation() {
        val validCode = RevenueCatManager.JUDGE_CODE
        val invalidCode = "RANDOM_PROMO_123"

        assertEquals("SHIPATON2026_JUDGE", validCode)
        assertFalse(invalidCode == RevenueCatManager.JUDGE_CODE)
    }

    @Test
    fun testFreeDailyLimitConstant() {
        assertEquals(5, RevenueCatManager.FREE_DAILY_LIMIT)
        assertEquals("clearpath_pro", RevenueCatManager.ENTITLEMENT_PRO)
    }
}
