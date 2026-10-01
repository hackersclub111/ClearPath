import asyncio
import os
import edge_tts
import subprocess
import json

VOICE = "en-US-ChristopherNeural"
PROJECT_DIR = os.path.dirname(os.path.abspath(__file__))
AUDIO_DIR = os.path.join(PROJECT_DIR, "audio_tracks_v2")
os.makedirs(AUDIO_DIR, exist_ok=True)

SCRIPTS = {
    "scene_1_wow": (
        "Modern mobile apps have become an exhausting cognitive battlefield. "
        "Seventeen confusing buttons, conflicting deadlines, and sensory overload. "
        "Watch what happens when you tap the ClearPath floating pill. "
        "ClearPath scans the live Android accessibility tree, filters out fourteen non-urgent items, "
        "and slides up a crystal-clear Clarity Card. "
        "It locks a pulsating focus beacon onto the exact tuition button, waits for user confirmation, "
        "and safely executes the tap."
    ),
    "scene_2_profiles": (
        "Because no two minds are identical, ClearPath dynamically transforms the same interface across cognitive profiles: "
        "Senior mode with large high-contrast targets, Hindi mode with native Devanagari clarity, "
        "and ADHD focus mode with zero visual distraction."
    ),
    "scene_3_chatgpt": (
        "Traditional AI asks you to take a screenshot and read paragraphs of advice. "
        "ClearPath inspects the live Android UI hierarchy, detects target node bounds, and executes safe assisted actions directly on screen. "
        "ChatGPT can explain your screen. ClearPath operates on the screen."
    ),
    "scene_4_revenuecat_onesignal": (
        "Monetization is powered by RevenueCat: five free daily simplifications, or unlimited Pro access. "
        "When a user abandons a complex multi-step form, OneSignal triggers a smart background journey, "
        "delivering a contextual push reminder that brings the user directly back to the next required action."
    ),
    "scene_5_architecture_outro": (
        "Built on Android's Accessibility framework, Google Gemini, RevenueCat, and OneSignal. "
        "Created by Ayush Anand at NIT Jamshedpur, fully open-source under the MIT license on GitHub."
    )
}

async def generate():
    for name, text in SCRIPTS.items():
        out_path = os.path.join(AUDIO_DIR, f"{name}.mp3")
        print(f"Generating {name}...")
        communicate = edge_tts.Communicate(text, VOICE, rate="+6%", pitch="+0Hz")
        await communicate.save(out_path)
        
        # probe duration
        cmd = ["ffprobe", "-v", "quiet", "-show_entries", "format=duration", "-of", "json", out_path]
        res = subprocess.check_output(cmd).decode()
        d = float(json.loads(res)["format"]["duration"])
        print(f"  -> {name}: {d:.2f}s")

if __name__ == "__main__":
    asyncio.run(generate())
