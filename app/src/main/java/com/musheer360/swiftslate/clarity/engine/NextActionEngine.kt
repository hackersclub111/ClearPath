package com.musheer360.swiftslate.clarity.engine

import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo

data class TargetMatch(
    val node: AccessibilityNodeInfo,
    val bounds: Rect,
    val text: String,
    val isClickable: Boolean
)

object NextActionEngine {
    private const val TAG = "NextActionEngine"

    fun findTargetNode(
        rootNode: AccessibilityNodeInfo?,
        targetText: String
    ): TargetMatch? {
        if (rootNode == null || targetText.isBlank()) return null

        val query = targetText.trim().lowercase()

        // 1. Exact or prefix match via framework findAccessibilityNodeInfosByText
        val candidates = try {
            rootNode.findAccessibilityNodeInfosByText(targetText)
        } catch (e: Exception) {
            emptyList()
        }

        for (node in candidates) {
            if (node.isVisibleToUser) {
                val bounds = Rect()
                node.getBoundsInScreen(bounds)
                return TargetMatch(
                    node = node,
                    bounds = bounds,
                    text = node.text?.toString() ?: targetText,
                    isClickable = node.isClickable
                )
            }
        }

        // 2. Fuzzy traversal if standard lookup misses
        return fuzzySearch(rootNode, query)
    }

    private fun fuzzySearch(node: AccessibilityNodeInfo?, query: String): TargetMatch? {
        if (node == null) return null

        val text = node.text?.toString()?.lowercase() ?: ""
        val desc = node.contentDescription?.toString()?.lowercase() ?: ""

        if ((text.contains(query) || desc.contains(query)) && node.isVisibleToUser) {
            val bounds = Rect()
            node.getBoundsInScreen(bounds)
            return TargetMatch(
                node = node,
                bounds = bounds,
                text = node.text?.toString() ?: desc,
                isClickable = node.isClickable
            )
        }

        for (i in 0 until node.childCount) {
            val child = try {
                node.getChild(i)
            } catch (e: Exception) {
                null
            }
            val match = fuzzySearch(child, query)
            if (match != null) return match
        }

        return null
    }

    fun executeAssistedTap(target: TargetMatch): Boolean {
        return try {
            var current: AccessibilityNodeInfo? = target.node
            var clicked = false

            // Bubble up to find clickable ancestor if node itself isn't directly flagged clickable
            while (current != null && !clicked) {
                if (current.isClickable) {
                    clicked = current.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }
                if (!clicked) {
                    current = current.parent
                }
            }
            clicked
        } catch (e: Exception) {
            Log.e(TAG, "Assisted tap failed", e)
            false
        }
    }
}
