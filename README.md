# ⚡ ClearPath: Cognitive Clarity Layer for Android

> **"Transforming any overwhelming screen into the one simple action you need next."**

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-purple.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Platform-Android_6.0%2B-green.svg)](https://android.com)
[![RevenueCat](https://img.shields.io/badge/Monetization-RevenueCat-FF5C5C.svg)](https://www.revenuecat.com)
[![OneSignal](https://img.shields.io/badge/Retention-OneSignal-E53935.svg)](https://onesignal.com)
[![Devpost](https://img.shields.io/badge/Hackathon-Shipaton_2026-orange.svg)](https://revenuecat-shipaton-2026.devpost.com/)

---

## 🧭 Executive Summary

Every day, billions of mobile screens present users with cognitive overload: bureaucratic college portals with 17 confusing buttons, intimidating banking apps cluttered with disclaimers, medical billing portals, and forms filled with visual noise. For **college students, elderly seniors, ADHD individuals, and non-native speakers**, this creates decision paralysis and severe cognitive fatigue.

**ClearPath** is a system-wide Android cognitive clarity layer. Powered by an OS-level Accessibility Service, ClearPath analyzes whatever is currently on your screen, strips away the clutter, and instantly replaces the visual noise with a clean, high-contrast **Clarity Card** featuring:
1. **1-Sentence Overview:** Plain-language summary of what the screen is asking.
2. **Key Priorities:** Up to 3 essential items (e.g. deadline, fee amount, penalty warning).
3. **Primary Action:** One prominent, guided **"Do This Next"** button that directs or auto-taps the target element.

---

## 🏗️ Architectural Overview (The 70/30 Next Gen Blueprint)

ClearPath builds upon the production-grade foundation of **SwiftSlate** (650+ stars, MIT License), augmenting it with 6 new layers:

```
┌────────────────────────────────────────────────────────────────────────┐
│                        ANDROID SYSTEM FOREGROUND                       │
│     (College LMS, Mobile Banking, Govt Form, Flight Check-In)          │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ Accessibility Hierarchy / Events
┌───────────────────────────────────▼────────────────────────────────────┐
│                    CLEARPATH ACCESSIBILITY ENGINE                      │
│   • ScreenHierarchyExtractor: Parses live nodes, bounds & text         │
│   • NextActionEngine: Target element identification & assisted tap     │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ Structured Screen JSON
┌───────────────────────────────────▼────────────────────────────────────┐
│                  COGNITIVE CLARITY ADAPTATION LAYER                    │
│   • CognitiveProfileEngine: Student, Senior, ADHD, Hindi Presets       │
│   • PriorityExtractionEngine: Gemini Flash / Groq LLM prompts          │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ Priority Breakdown
┌───────────────────────────────────▼────────────────────────────────────┐
│                   TRANSFORMED SYSTEM-WIDE OVERLAY                      │
│   • Floating Clarity Pill: Accessible trigger anywhere                 │
│   • Clarity Card: 1 Headline + 3 Priorities + 1 Primary Tap Button     │
└───────────────────┬────────────────────────────────┬───────────────────┘
                    │                                │
┌───────────────────▼──────────────────┐   ┌─────────▼───────────────────┐
│     REVENUECAT MONETIZATION          │   │      ONESIGNAL RETENTION    │
│  • Free Tier: 5 screens/day          │   │  • Form Friction Follow-up  │
│  • Pro Tier: $4.99/mo | $39.99/yr    │   │  • Daily Cognitive Reset    │
│  • Judge Bypass: SHIPATON2026_JUDGE  │   │  • Contextual User Tags     │
└──────────────────────────────────────┘   └─────────────────────────────┘
```

### SwiftSlate Core Reuse (70%)
- **System-Wide Accessibility Service:** Background daemon monitoring active windows and screen events safely.
- **Keystore-Encrypted API Storage:** Hardware-backed Keystore encryption for zero-leak privacy.
- **Multi-Provider Neural Router:** Gemini, Groq, and OpenAI-compatible client infrastructure.
- **Production Jetpack Compose Shell:** Smooth Material 3 UI and accessibility contrast tuning.

### ClearPath Novel Innovation (30%)
1. **Cognitive Profile Engine (`CognitiveProfile.kt`):**
   - Personalizes interface complexity based on cognitive archetypes (Student, Senior, ADHD, Non-Native Hindi).
   - Dynamically scales button sizes (1.0x to 1.5x) and tunes reading complexity.
2. **Priority Extraction Engine (`PriorityExtractionEngine.kt`):**
   - Distills raw UI trees into deterministic JSON containing summary, priorities, nextAction, and noiseToIgnore.
3. **Next Action Engine (`NextActionEngine.kt`):**
   - Maps LLM-identified actions back to physical screen coordinates and executes safe assisted taps.
4. **Transformed System Overlay (`SimplifiedOverlayManager.kt`):**
   - Floating draggable trigger and responsive clarity card rendered over any host app via `SYSTEM_ALERT_WINDOW`.
5. **RevenueCat Monetization Engine (`RevenueCatManager.kt`):**
   - Free daily tier (5 screens) vs Pro subscription ($4.99/mo or $39.99/yr).
   - Dedicated zero-friction Devpost Judge Pass (`SHIPATON2026_JUDGE`).
6. **OneSignal Retention Engine (`OneSignalRetentionManager.kt`):**
   - Intelligent multi-step form re-engagement ("Still working on that form?") and morning focus reminders.

---

## 🎯 Target Categories & Judging Alignment

### 🏅 Next Gen Award (Student OSS)
- **Status:** 100% Student-authored open-source submission (`2023ugec092@nitjsr.ac.in`).
- **Open Source:** Full code public under permissive MIT License.
- **Quality:** Native Kotlin, Jetpack Compose, Coroutines, AndroidX, and zero-jank overlay architecture.

### 🕊️ RevenueCat Peace Prize (Social Impact)
- **The Problem:** The modern digital world excludes millions due to sensory and cognitive overload.
- **The Impact:** Empowers elderly users to bank safely without falling for deceptive dark patterns; enables students from non-native English backgrounds to navigate college portals with Hindi clarity; assists neurodivergent (ADHD) users in completing stressful bureaucratic forms.

### 🎨 RevenueCat Design Award (UX Innovation)
- **Interaction Model:** Tapping one floating pill causes a chaotic 17-button screen to cleanly dissolve into 1 obvious primary action.
- **Craft Quality:** Jetpack Compose Material 3, dynamic typography scaling, glassmorphic dark theme, and high-contrast accessible color tokens.

### 📡 Keep Them Coming Back Award — OneSignal
- **Thoughtful Journeys:**
  - *Journey 1: Form Friction Assistant* — When a user analyzes a college fee or KYC form, OneSignal tags `pending_form_app`. If uncompleted after 2 hours, a gentle notification prompts: *"Still working on that form? Tap to simplify the next step."*
  - *Journey 2: Daily Cognitive Reset* — Sends morning encouragement highlighting tasks completed yesterday.

---

## 💳 RevenueCat Monetization & Devpost Judge Pass

ClearPath implements a sustainable freemium model:
- **Free Tier:** 5 free screen simplifications per day (renews daily).
- **ClearPath Pro ($4.99/month or $39.99/year):**
  - Unlimited screen simplifications
  - Multi-profile Family Mode (3 saved profiles for kids, parents, elderly)
  - Assisted Auto-Tap Navigation
  - Ultra-Low-Latency AI Processing

### 🔑 Instant Judge Pass for Devpost Jury
Hackathon judges can test 100% of Pro features with zero payment:
1. Open ClearPath and navigate to the **Pro** tab.
2. In the **Devpost Judge Instant Bypass** card, enter:
   ```
   SHIPATON2026_JUDGE
   ```
3. Tap **"Unlock Pro for Evaluation"**. Pro entitlement will activate immediately!

---

## 🛠️ Tech Stack

- **Language:** Kotlin 2.1.0
- **UI Toolkit:** Jetpack Compose (Material 3, Material Icons Extended)
- **Android Target:** SDK 36 (Android 15), Min SDK 23 (Android 6.0 Marshmallow)
- **Architecture:** MVVM + Coroutines + StateFlow
- **In-App Purchases:** RevenueCat Android Purchases SDK `8.12.0`
- **Push & Messaging:** OneSignal SDK `5.1.28`
- **AI Inference:** Google Gemini 1.5 Flash / Groq Llama 3.3 70B
- **Security:** Android Keystore AES-256 GCM encryption

---

## 🚀 Building & Running

### Requirements
- Android device or emulator running Android 6.0+ (API 23+)
- Android SDK 36 + JDK 17

### Build Debug APK
```bash
./gradlew assembleDebug
```

### Run Unit Tests
```bash
./gradlew test
```

---

## 👨‍💻 Author & Contact

- **Author:** Ayush Anand ([@hackersclub111](https://github.com/hackersclub111))
- **Institution:** National Institute of Technology Jamshedpur (B.Tech, 2023–2027)
- **Contact:** `2023ugec092@nitjsr.ac.in`
- **Devpost:** `truthcodeexplorer`

---

## 📜 License

ClearPath is distributed under the [MIT License](LICENSE), honoring the upstream open-source license of SwiftSlate.
