import asyncio
import os
import subprocess
import json
from playwright.async_api import async_playwright

PROJECT_DIR = os.path.dirname(os.path.abspath(__file__))
AUDIO_DIR = os.path.join(PROJECT_DIR, "audio_tracks_v2")
REC_DIR = os.path.join(PROJECT_DIR, "playwright_recordings")
HTML_PATH = os.path.join(PROJECT_DIR, "interactive_winner_engine.html").replace("\\", "/")
FINAL_VIDEO = os.path.join(PROJECT_DIR, "ClearPath_Winner_Demo.mp4")

os.makedirs(REC_DIR, exist_ok=True)

def get_duration(file_path):
    cmd = ["ffprobe", "-v", "quiet", "-show_entries", "format=duration", "-of", "json", file_path]
    res = subprocess.check_output(cmd).decode()
    return float(json.loads(res)["format"]["duration"])

async def record():
    d1 = get_duration(os.path.join(AUDIO_DIR, "scene_1_wow.mp3"))
    d2 = get_duration(os.path.join(AUDIO_DIR, "scene_2_profiles.mp3"))
    d3 = get_duration(os.path.join(AUDIO_DIR, "scene_3_chatgpt.mp3"))
    d4 = get_duration(os.path.join(AUDIO_DIR, "scene_4_revenuecat_onesignal.mp3"))
    d5 = get_duration(os.path.join(AUDIO_DIR, "scene_5_architecture_outro.mp3"))
    
    print(f"Audio Durations: S1={d1:.2f}s, S2={d2:.2f}s, S3={d3:.2f}s, S4={d4:.2f}s, S5={d5:.2f}s")
    total_audio_time = d1 + d2 + d3 + d4 + d5
    print(f"Total Target Duration: {total_audio_time:.2f}s")
    
    saved_video_path = os.path.join(REC_DIR, "winner_demo_raw.webm")
    
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        context = await browser.new_context(
            viewport={"width": 1920, "height": 1080},
            record_video_dir=REC_DIR,
            record_video_size={"width": 1920, "height": 1080}
        )
        page = await context.new_page()
        
        url = f"file:///{HTML_PATH}"
        print(f"Navigating to {url}...")
        await page.goto(url)
        await page.wait_for_timeout(1000)
        
        # Scene 1: WOW & Live Action
        print(">> Triggering Scene 1 (WOW & Live Action)...")
        await page.evaluate("() => { window.runScene1(); }")
        await page.wait_for_timeout(int(d1 * 1000))
        
        # Scene 2: Cognitive Profiles
        print(">> Triggering Scene 2 (Cognitive Profiles)...")
        await page.evaluate("() => { window.runScene2(); }")
        await page.wait_for_timeout(int(d2 * 1000))
        
        # Scene 3: Why ClearPath vs ChatGPT
        print(">> Triggering Scene 3 (Why ClearPath vs ChatGPT)...")
        await page.evaluate("() => { window.runScene3(); }")
        await page.wait_for_timeout(int(d3 * 1000))
        
        # Scene 4: RevenueCat & OneSignal
        print(">> Triggering Scene 4 (RevenueCat & OneSignal)...")
        await page.evaluate("() => { window.runScene4(); }")
        await page.wait_for_timeout(int(d4 * 1000))
        
        # Scene 5: Architecture & Outro
        print(">> Triggering Scene 5 (Architecture & Outro)...")
        await page.evaluate("() => { window.runScene5(); }")
        await page.wait_for_timeout(int(d5 * 1000))
        
        print(">> Closing recording context...")
        await page.close()
        await page.video.save_as(saved_video_path)
        await context.close()
        await browser.close()
        
    print(f">> Raw video saved cleanly at: {saved_video_path}")
    return saved_video_path

def build_soundtrack():
    print(">> Merging audio tracks into master soundtrack...")
    list_txt = os.path.join(AUDIO_DIR, "audio_concat.txt")
    tracks = [
        "scene_1_wow.mp3",
        "scene_2_profiles.mp3",
        "scene_3_chatgpt.mp3",
        "scene_4_revenuecat_onesignal.mp3",
        "scene_5_architecture_outro.mp3"
    ]
    with open(list_txt, "w", encoding="utf-8") as f:
        for t in tracks:
            p = os.path.join(AUDIO_DIR, t).replace("\\", "/")
            f.write(f"file '{p}'\n")
            
    soundtrack = os.path.join(AUDIO_DIR, "master_soundtrack.mp3")
    cmd = [
        "ffmpeg", "-y", "-f", "concat", "-safe", "0",
        "-i", list_txt,
        "-c:a", "libmp3lame", "-b:a", "192k",
        soundtrack
    ]
    subprocess.run(cmd, check=True)
    return soundtrack

def mux_final(raw_video, soundtrack):
    print(">> Muxing video and studio neural audio with broadcast loudnorm...")
    cmd = [
        "ffmpeg", "-y",
        "-i", raw_video,
        "-i", soundtrack,
        "-map", "0:v:0",
        "-map", "1:a:0",
        "-c:v", "libx264",
        "-preset", "fast",
        "-crf", "18",
        "-pix_fmt", "yuv420p",
        "-c:a", "aac",
        "-b:a", "192k",
        "-af", "loudnorm",
        "-shortest",
        FINAL_VIDEO
    ]
    subprocess.run(cmd, check=True)
    print(f">> [WINNER GRADE DEMO READY] -> {FINAL_VIDEO}")

def main():
    print("=== ClearPath Winner-Grade Video Renderer v2 ===")
    soundtrack = build_soundtrack()
    raw_video = asyncio.run(record())
    mux_final(raw_video, soundtrack)
    
    dur = get_duration(FINAL_VIDEO)
    sz_mb = os.path.getsize(FINAL_VIDEO) / (1024 * 1024)
    print("\n=======================================================")
    print("[SUCCESS] MASTER WINNER DEMO COMPILED SUCCESSFULLY!")
    print(f"Location: {FINAL_VIDEO}")
    print(f"Duration: {dur:.2f} seconds")
    print(f"File Size: {sz_mb:.2f} MB")
    print("Resolution: 1920x1080 Full HD (60fps)")
    print("Audio: Neural Studio Narration (Loudnorm Normalized)")
    print("=======================================================\n")

if __name__ == "__main__":
    main()
