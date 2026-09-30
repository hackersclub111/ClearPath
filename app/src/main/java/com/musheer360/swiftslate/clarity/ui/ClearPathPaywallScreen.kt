package com.musheer360.swiftslate.clarity.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musheer360.swiftslate.clarity.monetization.RevenueCatManager

@Composable
fun ClearPathPaywallScreen(
    revenueCatManager: RevenueCatManager = RevenueCatManager.getInstance(LocalContext.current),
    onDismiss: () -> Unit = {}
) {
    var isProActive by remember { mutableStateOf(revenueCatManager.isProActive) }
    var selectedPlan by remember { mutableStateOf("annual") } // "monthly" or "annual"
    var judgeCode by remember { mutableStateOf(RevenueCatManager.JUDGE_CODE) }
    var judgeMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Pro Header Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF6366F1), Color(0xFF38BDF8))))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "REVENUECAT POWERED",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }

        Text(
            text = "ClearPath Pro",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Effortless cognitive clarity across your entire Android experience. Never feel overwhelmed by a complex screen again.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Features Checklist
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureRow("Unlimited Screen Simplifications", "Free tier limited to 5 screens/day")
                FeatureRow("Multi-Profile Family Mode", "3 saved profiles for seniors, students, & ADHD")
                FeatureRow("Assisted Auto-Tap & Focus Beacon", "Guides and performs physical taps safely")
                FeatureRow("Priority Neural AI Processing", "Ultra-fast <800ms response time on all apps")
            }
        }

        // Subscription Plans (Monthly vs Annual)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PlanCard(
                title = "Monthly",
                price = "$4.99",
                period = "/ month",
                badge = null,
                isSelected = selectedPlan == "monthly",
                modifier = Modifier.weight(1f)
            ) {
                selectedPlan = "monthly"
            }

            PlanCard(
                title = "Annual",
                price = "$39.99",
                period = "/ year",
                badge = "SAVE 33%",
                isSelected = selectedPlan == "annual",
                modifier = Modifier.weight(1f)
            ) {
                selectedPlan = "annual"
            }
        }

        // Primary Subscription CTA
        Button(
            onClick = {
                // In production triggers Purchases.sharedInstance.purchaseWith(...)
                // For demonstration / free trial flow:
                judgeMessage = "Connecting to Google Play Billing via RevenueCat…"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = if (selectedPlan == "annual") "Start 7-Day Free Trial ($39.99/yr)" else "Subscribe Monthly ($4.99/mo)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Devpost Judge Instant Pass Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF6366F1), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B).copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Devpost Judge Instant Bypass",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
                Text(
                    text = "Hackathon judges can test all ClearPath Pro features without billing constraints.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFCBD5E1)
                )

                OutlinedTextField(
                    value = judgeCode,
                    onValueChange = { judgeCode = it },
                    label = { Text("Judge Access Code") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val success = revenueCatManager.applyJudgePass(judgeCode)
                        if (success) {
                            isProActive = true
                            judgeMessage = "🎉 Pro Unlocked for Devpost Jury Review!"
                        } else {
                            judgeMessage = "Invalid code. Use SHIPATON2026_JUDGE"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                ) {
                    Text("Unlock Pro for Evaluation", fontWeight = FontWeight.Bold)
                }

                judgeMessage?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isProActive) Color(0xFF4ADE80) else Color(0xFFF87171),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Restore & Terms
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = { revenueCatManager.refreshCustomerInfo { isProActive = it } }) {
                Text("Restore Purchases", style = MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick = onDismiss) {
                Text("Dismiss", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun FeatureRow(title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF38BDF8),
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PlanCard(
    title: String,
    price: String,
    period: String,
    badge: String?,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(2.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            badge?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFF59E0B),
                    modifier = Modifier
                        .background(Color(0xFFFEF3C7), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = price,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = " $period",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
