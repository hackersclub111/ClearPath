# 🏆 ClearPath: Devpost Submission Package

## 📋 Basic Information

- **Project Title:** ClearPath: Cognitive Clarity Layer for Android *(47 / 60 characters)*
- **Elevator Pitch:** A system-wide cognitive clarity layer for Android transforming overwhelming screens into the one simple action you need next, powered by RevenueCat subscriptions and OneSignal journeys. *(189 / 199 characters)*
- **Categories Selected:**
  1. `Next Gen Award` (Student open-source software submission)
  2. `RevenueCat Peace Prize` (Social good: cognitive fatigue & digital accessibility)
  3. `RevenueCat Design Award` (UX innovation: instant screen transformation & craft)
  4. `Keep Them Coming Back Award — OneSignal` (Thoughtful retention & context-aware notifications)
- **Primary Platform:** Android 6.0+ (Kotlin / Jetpack Compose)
- **License:** MIT License (Open Source)
- **GitHub Repository:** https://github.com/hackersclub111/ClearPath
- **GitHub Release v1.0.0:** https://github.com/hackersclub111/ClearPath/releases/tag/v1.0.0
- **Master Demo Video:** `ClearPath_Winner_Demo.mp4` (1920x1080 Full HD, 95s, Studio Neural Narration)
- **Download Demo Video:** https://github.com/hackersclub111/ClearPath/releases/download/v1.0.0/ClearPath_Winner_Demo.mp4
- **Interactive Browser Demo:** https://github.com/hackersclub111/ClearPath/releases/download/v1.0.0/ClearPath_Judge_Demo.html
- **Devpost Judge Bypass Code:** `SHIPATON2026_JUDGE`

---

## 📝 Submission Form Questions & Answers

### 💡 Inspiration
Modern software interfaces have become an exhausting cognitive battlefield. Open a university fee portal, and you are hit with 17 confusing buttons, conflicting drop-downs, and alarming penalty warnings. Open a banking app, and urgent KYC compliance notices are buried under promotional loan banners.

For college students navigating Byzantine academic systems, elderly seniors terrified of mis-tapping banking links, neurodivergent individuals experiencing sensory overload, and non-native English speakers grappling with legalistic jargon, modern mobile interfaces create severe anxiety and decision paralysis. 

We asked ourselves: **What if your phone had an OS-level cognitive clarity layer that could look at any overwhelming screen, strip away the noise, and distill everything down to the one single action you need to do next?** That question led to **ClearPath**.

---

### ⚡ What It Does
ClearPath is a system-wide cognitive clarity layer for Android. Operating unobtrusively via Android's Accessibility Service, ClearPath places a subtle floating **"⚡ Simplify"** pill on your screen across all third-party apps.

When a user taps "Simplify":
1. **Screen Distillation:** ClearPath captures the live UI hierarchy and parses all interactive elements, warnings, and fields.
2. **Cognitive Profile Adaptation:** The engine applies the user's chosen cognitive profile (e.g., Student, Senior, ADHD Focus, or Hindi Non-Native).
3. **The Clarity Card:** A high-contrast glassmorphic card slides over the screen, presenting:
   - **One-Sentence Overview:** Plain-language explanation of what this screen is asking.
   - **Key Priorities:** Up to 3 essential items (e.g., fee amounts, upcoming deadlines, penalty thresholds).
   - **Recommended Next Step:** A single, prominent **"Do This Next"** action button.
4. **Assisted Tap & Focus Beacon:** ClearPath renders a high-contrast pulsating beacon directly over the physical button on the underlying host app and can automatically perform an assisted tap upon confirmation.

---

### 🛠️ How We Built It
ClearPath is built on a **70/30 Next Gen Open-Source foundation**, adopting and modernizing the production-grade accessibility architecture of **SwiftSlate** (MIT License, 650+ GitHub stars):

1. **The 70% Production Base (SwiftSlate):**
   - High-reliability Android `AccessibilityService` background daemon.
   - Hardware-backed Android Keystore encryption for zero-leak local API credentials.
   - Multi-provider neural router supporting Google Gemini 1.5 Flash and Groq Llama 3.3.
   - Native Material 3 Jetpack Compose UI architecture.

2. **The 30% Novel Innovation (ClearPath):**
   - **Cognitive Profile Engine (`CognitiveProfile.kt`):** Dynamic parameter tuning adjusting reading complexity (1 to 5 bullets), button sizes (1.0x to 1.5x), and language modes (English, Hindi, Simple English).
   - **Priority Extraction Engine (`PriorityExtractionEngine.kt`):** Distills complex UI snapshots into strict, deterministic JSON containing concise summaries, priorities, and action targets.
   - **Next Action Engine (`NextActionEngine.kt`):** Traverses the live Android accessibility tree to match LLM target labels to physical node bounds and executes safe assisted taps.
   - **System-Wide Overlay (`SimplifiedOverlayManager.kt`):** Draggable floating trigger and animated bottom sheet rendered across any host application.
   - **RevenueCat Monetization Engine (`RevenueCatManager.kt`):** Integrated RevenueCat Android Purchases SDK powering a freemium model (5 free simplifications/day, $4.99/mo or $39.99/yr Pro tier) and an instant Devpost Judge Pass.
   - **OneSignal Retention Engine (`OneSignalRetentionManager.kt`):** Integrated OneSignal SDK orchestrating thoughtful retention journeys for incomplete forms and morning focus resets.

---

### 🚧 Challenges We Ran Into
- **Accessibility Tree Asynchrony:** Mobile apps frequently rebuild their view hierarchies mid-animation. Querying accessibility nodes during transitions could throw `IllegalStateException` on older Android versions. We solved this with safe node recycling, defensive null-handling, and hierarchical fallback traversal.
- **Ultra-Low Latency Inference:** A cognitive clarity overlay must feel instantaneous (<800ms) to avoid frustrating users. We optimized prompt tokens by stripping redundant container layouts and sending only semantic UI elements to Gemini 1.5 Flash.
- **Safe Tap Delegation:** Ensuring assisted taps only fire on the intended primary button without accidentally triggering destructive actions required a strict dual-confirmation threshold and view ID verification.

---

### 🌟 Accomplishments That We're Proud Of
- **Zero-Friction Screen Transformation:** Turning a 17-button bureaucratic portal into 1 clear action button in under 800 milliseconds.
- **Inclusive Social Good:** Empowering Hindi speakers and seniors with tailored plain-language overlays on apps that only ship in complex English.
- **Production-Grade Monetization & Retention:** Seamlessly integrating RevenueCat subscriptions and OneSignal journeys into an OS-level accessibility service.
- **Judge-First Polish:** Delivering both a native Android codebase and an interactive offline web demonstration harness (`ClearPath_Judge_Demo.html`) so judges can test every feature immediately.

---

### 📚 What We Learned
We learned that accessibility is far broader than screen readers for the visually impaired. Cognitive accessibility—reducing decision fatigue, sensory overload, and bureaucratic friction—is an enormous, underserved space that affects billions of people daily. We also learned how powerful Android's Accessibility framework is when paired with structured LLM prompt engineering.

---

### 🚀 What's Next For ClearPath
- **On-Device Edge SLM:** Integrating Google's on-device Gemini Nano via AICore for 100% offline, zero-latency screen transformations with zero cloud data transfer.
- **Family Caregiver Dashboard:** Allowing adult children to configure cognitive profiles for elderly parents and receive alerts if repetitive error screens are encountered.
- **Chrome Extension / Web Overlay:** Expanding the ClearPath cognitive clarity engine to desktop web browsers.

---

## 🎓 Next Gen Award (Student Eligibility Statement)

- **Student Status:** Active full-time undergraduate engineering student at National Institute of Technology Jamshedpur (NIT JSR), Batch of 2023–2027.
- **Verified University Email:** `2023ugec092@nitjsr.ac.in`
- **GitHub Account:** `https://github.com/hackersclub111`
- **Open Source Commitment:** ClearPath is fully open-source under the MIT License at `https://github.com/hackersclub111/ClearPath`. All new modules, tests, prompts, and architectures are freely available to the community.

---

## 💳 RevenueCat Monetization Details

- **SDK Used:** `com.revenuecat.purchases:purchases:8.12.0`
- **Entitlement ID:** `clearpath_pro`
- **Subscription Products:**
  - `clearpath_pro_monthly`: $4.99 / month
  - `clearpath_pro_annual`: $39.99 / year (33% discount)
- **Free Quota:** 5 free screen simplifications per calendar day.
- **Devpost Judge Bypass:**
  In the app's **Pro** tab, judges can enter:
  ```
  SHIPATON2026_JUDGE
  ```
  This immediately unlocks full Pro privileges for testing without credit card charges.

---

## 📡 OneSignal Retention & Campaigns

- **SDK Used:** `com.onesignal:OneSignal:5.1.28`
- **Campaign 1: Form Friction Assistant:**
  When a user analyzes an intimidating multi-step form (e.g., college fees, insurance KYC) and does not complete the action, OneSignal tags `pending_form_app`. After 2 hours of inactivity, a contextual notification prompts:
  *"Still working on that form? Tap ClearPath to guide you through the next required field."*
- **Campaign 2: Daily Cognitive Reset:**
  Every morning at 9:00 AM, users receive an encouraging summary of tasks completed the previous day, setting a clear, focused intention for the day ahead.
