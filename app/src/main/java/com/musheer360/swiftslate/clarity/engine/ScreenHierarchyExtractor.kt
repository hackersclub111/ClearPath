package com.musheer360.swiftslate.clarity.engine

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo

data class ScreenElement(
    val id: String,
    val text: String,
    val contentDescription: String?,
    val className: String,
    val isClickable: Boolean,
    val isEditable: Boolean,
    val bounds: Rect,
    val depth: Int
)

data class ScreenSnapshot(
    val packageName: String,
    val elements: List<ScreenElement>,
    val interactiveCount: Int,
    val rawTextDump: String
)

object ScreenHierarchyExtractor {

    private const val MAX_ELEMENTS = 120
    private const val MAX_DEPTH = 15

    fun extract(rootNode: AccessibilityNodeInfo?): ScreenSnapshot {
        if (rootNode == null) {
            return ScreenSnapshot(
                packageName = "unknown",
                elements = emptyList(),
                interactiveCount = 0,
                rawTextDump = "Empty or inaccessible screen"
            )
        }

        val packageName = rootNode.packageName?.toString() ?: "unknown"
        val elementList = mutableListOf<ScreenElement>()
        val textBuilder = StringBuilder()

        traverseNode(
            node = rootNode,
            depth = 0,
            elementList = elementList,
            textBuilder = textBuilder
        )

        val interactiveCount = elementList.count { it.isClickable || it.isEditable }

        return ScreenSnapshot(
            packageName = packageName,
            elements = elementList,
            interactiveCount = interactiveCount,
            rawTextDump = textBuilder.toString().trim()
        )
    }

    private fun traverseNode(
        node: AccessibilityNodeInfo?,
        depth: Int,
        elementList: MutableList<ScreenElement>,
        textBuilder: StringBuilder
    ) {
        if (node == null || depth > MAX_DEPTH || elementList.size >= MAX_ELEMENTS) return

        val text = node.text?.toString()?.trim().orEmpty()
        val contentDesc = node.contentDescription?.toString()?.trim()
        val isClickable = node.isClickable
        val isEditable = node.isEditable
        val viewId = node.viewIdResourceName ?: "node_${elementList.size}"
        val className = node.className?.toString()?.substringAfterLast('.') ?: "View"

        val hasMeaningfulContent = text.isNotEmpty() || !contentDesc.isNullOrEmpty() || isClickable || isEditable

        if (hasMeaningfulContent && node.isVisibleToUser) {
            val bounds = Rect()
            node.getBoundsInScreen(bounds)

            val displayLabel = when {
                text.isNotEmpty() -> text
                !contentDesc.isNullOrEmpty() -> contentDesc
                else -> className
            }

            val element = ScreenElement(
                id = viewId,
                text = displayLabel,
                contentDescription = contentDesc,
                className = className,
                isClickable = isClickable,
                isEditable = isEditable,
                bounds = bounds,
                depth = depth
            )
            elementList.add(element)

            val tag = when {
                isEditable -> "[INPUT]"
                isClickable -> "[BUTTON]"
                else -> "[TEXT]"
            }
            textBuilder.appendLine("$tag $displayLabel (id: $viewId)")
        }

        val childCount = node.childCount
        for (i in 0 until childCount) {
            val child = try {
                node.getChild(i)
            } catch (e: Exception) {
                null
            }
            if (child != null) {
                traverseNode(child, depth + 1, elementList, textBuilder)
            }
        }
    }
}
