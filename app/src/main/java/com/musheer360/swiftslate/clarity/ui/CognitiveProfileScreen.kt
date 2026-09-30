package com.musheer360.swiftslate.clarity.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Elderly
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musheer360.swiftslate.clarity.model.*

@Composable
fun CognitiveProfileScreen(
    profileManager: CognitiveProfileManager = CognitiveProfileManager(LocalContext.current),
    onProfileUpdated: () -> Unit = {}
) {
    var activeProfile by remember { mutableStateOf(profileManager.getActiveProfile()) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Cognitive Clarity Profiles",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Personalize how ClearPath transforms overwhelming screens into simple, bite-sized actions.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 1. Archetype Selection Section
        Text(
            text = "Select Persona Preset",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ArchetypeCard(
                title = "Student",
                subtitle = "College / LMS",
                icon = Icons.Default.School,
                isSelected = activeProfile.archetype == CognitiveArchetype.STUDENT,
                modifier = Modifier.weight(1f)
            ) {
                profileManager.applyPreset(CognitiveArchetype.STUDENT)
                activeProfile = profileManager.getActiveProfile()
                onProfileUpdated()
            }

            ArchetypeCard(
                title = "Senior",
                subtitle = "Large / Plain",
                icon = Icons.Default.Elderly,
                isSelected = activeProfile.archetype == CognitiveArchetype.SENIOR,
                modifier = Modifier.weight(1f)
            ) {
                profileManager.applyPreset(CognitiveArchetype.SENIOR)
                activeProfile = profileManager.getActiveProfile()
                onProfileUpdated()
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ArchetypeCard(
                title = "ADHD Focus",
                subtitle = "1-Step Only",
                icon = Icons.Default.Psychology,
                isSelected = activeProfile.archetype == CognitiveArchetype.ADHD,
                modifier = Modifier.weight(1f)
            ) {
                profileManager.applyPreset(CognitiveArchetype.ADHD)
                activeProfile = profileManager.getActiveProfile()
                onProfileUpdated()
            }

            ArchetypeCard(
                title = "Hindi Mode",
                subtitle = "हिन्दी सरलीकरण",
                icon = Icons.Default.Translate,
                isSelected = activeProfile.archetype == CognitiveArchetype.NON_NATIVE,
                modifier = Modifier.weight(1f)
            ) {
                profileManager.applyPreset(CognitiveArchetype.NON_NATIVE)
                activeProfile = profileManager.getActiveProfile()
                onProfileUpdated()
            }
        }

        // 2. Fine-Tuning Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Adaptive Parameters",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                // Language
                DropdownSetting(
                    label = "Language & Phrasing",
                    currentValue = activeProfile.language.displayName,
                    options = ClarityLanguage.entries.map { it.displayName }
                ) { index ->
                    val newProfile = activeProfile.copy(
                        language = ClarityLanguage.entries[index],
                        archetype = CognitiveArchetype.CUSTOM
                    )
                    profileManager.saveProfile(newProfile)
                    activeProfile = newProfile
                    onProfileUpdated()
                }

                // Complexity
                DropdownSetting(
                    label = "Information Density",
                    currentValue = activeProfile.complexity.displayName,
                    options = ComplexityLevel.entries.map { it.displayName }
                ) { index ->
                    val newProfile = activeProfile.copy(
                        complexity = ComplexityLevel.entries[index],
                        archetype = CognitiveArchetype.CUSTOM
                    )
                    profileManager.saveProfile(newProfile)
                    activeProfile = newProfile
                    onProfileUpdated()
                }

                // Button Size
                DropdownSetting(
                    label = "Action Button Size",
                    currentValue = activeProfile.buttonSize.displayName,
                    options = ButtonSize.entries.map { it.displayName }
                ) { index ->
                    val newProfile = activeProfile.copy(
                        buttonSize = ButtonSize.entries[index],
                        archetype = CognitiveArchetype.CUSTOM
                    )
                    profileManager.saveProfile(newProfile)
                    activeProfile = newProfile
                    onProfileUpdated()
                }
            }
        }

        // 3. Live Transformation Preview
        Text(
            text = "Live Clarity Preview",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
        )

        TransformationPreviewCard(activeProfile)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ArchetypeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
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
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
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
fun DropdownSetting(
    label: String,
    currentValue: String,
    options: List<String>,
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = currentValue,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelect(index)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TransformationPreviewCard(profile: CognitiveProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)) // Slate 900
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ClearPath Screen Overlay",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF38BDF8)
                )
                Text(
                    text = "College Fees Portal",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF94A3B8)
                )
            }

            Text(
                text = if (profile.language == ClarityLanguage.HINDI) {
                    "सेमेस्टर 5 की ट्यूशन फीस ₹42,500 जमा करने की अंतिम तिथि कल शाम 5:00 बजे है।"
                } else {
                    "Semester 5 tuition fee payment of \$420.00 is due tomorrow at 5:00 PM."
                },
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )

            // Dynamic priorities
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val p1 = if (profile.language == ClarityLanguage.HINDI) "• विलंब शुल्क से बचने के लिए अभी भुगतान करें" else "• Late fine of \$50 applies after 5:00 PM"
                val p2 = if (profile.language == ClarityLanguage.HINDI) "• सभी 16 अन्य लिंक्स और फॉर्म्स को अभी छोड़ दें" else "• Ignore 14 other elective course registration links"

                Text(p1, style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1))
                if (profile.complexity != ComplexityLevel.MINIMAL) {
                    Text(p2, style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1))
                }
            }

            // Big action button reflecting button size
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8))
            ) {
                Text(
                    text = if (profile.language == ClarityLanguage.HINDI) {
                        "👉 अभी ₹42,500 का भुगतान करें"
                    } else {
                        "👉 Pay \$420.00 Tuition Now"
                    },
                    color = Color(0xFF0F172A),
                    fontWeight = FontWeight.Bold,
                    fontSize = (14 * profile.buttonSize.scaleFactor).sp
                )
            }
        }
    }
}
