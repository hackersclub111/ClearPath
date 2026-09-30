import asyncio
import os
import edge_tts

VOICE = "en-US-ChristopherNeural"

SCENES = [
    {
        "id": "scene_1_problem",
        "text": "Every single day, millions of students, seniors, and neurodivergent individuals face a wall of digital friction: confusing university portals with 17 conflicting buttons, intimidating banking apps, and bureaucratic forms where finding the next step is overwhelming."
    },
    {
        "id": "scene_2_interaction",
        "text": "This is ClearPath: a system-wide cognitive clarity layer for Android. When a screen feels overwhelming, you don't read through the clutter. You tap 'Simplify'. Powered by an OS accessibility service, ClearPath analyzes whatever is currently on screen, strips the visual noise, and instantly slides up a high-contrast Clarity Card with the one action you need next."
    },
    {
        "id": "scene_3_profiles",
        "text": "Because clarity is personal, ClearPath features the Cognitive Profile Engine. A college student gets deadline isolation and LMS filtering. An elderly senior gets huge buttons, plain language, and zero financial jargon. For non-native speakers, Hindi mode translates complex bureaucratic clauses into natural everyday Hindi directly on the overlay."
    },
    {
        "id": "scene_4_monetization",
        "text": "Monetization is powered entirely by RevenueCat. Free users receive 5 daily screen simplifications. For $4.99 a month, ClearPath Pro unlocks unlimited simplifications, multi-profile family mode, and assisted auto-taps. For the Devpost jury, entering 'SHIPATON2026_JUDGE' instantly bypasses billing so you can test every Pro capability with zero friction."
    },
    {
        "id": "scene_5_retention",
        "text": "Retention is handled thoughtfully through OneSignal. When you start an intimidating multi-step form and leave it unfinished, ClearPath tags the session and follows up two hours later: 'Still working on that form? Tap to simplify the next step.' And in the morning, a gentle cognitive reset summarizes your completed tasks."
    },
    {
        "id": "scene_6_outro",
        "text": "Built by students for everyone who needs a simpler digital world. ClearPath is 100% open-source on GitHub under the MIT license. Reclaim your digital clarity."
    }
]

async def main():
    out_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), "audio_tracks")
    os.makedirs(out_dir, exist_ok=True)
    
    for s in SCENES:
        out_path = os.path.join(out_dir, f"{s['id']}.mp3")
        print(f"Synthesizing {s['id']}...")
        communicate = edge_tts.Communicate(s['text'], VOICE)
        await communicate.save(out_path)
        print(f"[OK] Saved {out_path}")

if __name__ == "__main__":
    asyncio.run(main())
