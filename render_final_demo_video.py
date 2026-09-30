import os
import subprocess
import json

FFMPEG = "ffmpeg"
FFPROBE = "ffprobe"
PROJECT_DIR = os.path.dirname(os.path.abspath(__file__))
AUDIO_DIR = os.path.join(PROJECT_DIR, "audio_tracks")
ASSETS_DIR = os.path.join(PROJECT_DIR, "assets")
BUILD_DIR = os.path.join(PROJECT_DIR, "video_segments")
OUTPUT_VIDEO = os.path.join(PROJECT_DIR, "ClearPath_Winner_Demo.mp4")

os.makedirs(BUILD_DIR, exist_ok=True)

def get_audio_duration(audio_path):
    cmd = [FFPROBE, "-v", "quiet", "-show_entries", "format=duration", "-of", "json", audio_path]
    res = subprocess.check_output(cmd).decode()
    return float(json.loads(res)["format"]["duration"])

def build_scene_1(duration, audio_path, out_path):
    print(">> Rendering Scene 1: Problem Statement & Intro...")
    # Use thumbnail_1200x800 with 1920x1080 letterbox/pad and subtle zoom
    thumb = os.path.join(ASSETS_DIR, "thumbnail_1200x800.png")
    cmd = [
        FFMPEG, "-y",
        "-loop", "1", "-i", thumb,
        "-i", audio_path,
        "-filter_complex",
        f"[0:v]scale=1620:1080,pad=1920:1080:(1920-1620)/2:0:color=#070b14,format=yuv420p[v]",
        "-map", "[v]", "-map", "1:a",
        "-c:v", "libx264", "-c:a", "aac", "-b:a", "192k",
        "-t", f"{duration:.3f}",
        out_path
    ]
    subprocess.run(cmd, check=True)

def build_scene_2(duration, audio_path, raw_video_path, out_path):
    print(">> Rendering Scene 2: Real Physical Device Execution...")
    # Raw phone video is 1080x2400.
    # We scale it to 486:1080 and pad to 1920x1080 with stylish side badges
    cmd = [
        FFMPEG, "-y",
        "-stream_loop", "-1", "-i", raw_video_path,
        "-i", audio_path,
        "-filter_complex",
        "[0:v]scale=486:1080[phone];"
        "color=c=#070b14:s=1920x1080:d=" + f"{duration:.3f}" + "[bg];"
        "[bg][phone]overlay=(1920-486)/2:0[base];"
        "[base]drawtext=text='PHYSICAL HARDWARE EXECUTION':x=80:y=100:fontcolor=#00F2FE:fontsize=32:box=1:boxcolor=#000000@0.7:boxborderw=10,"
        "drawtext=text='Device\\: Infinix X6832 (Android 13)':x=80:y=160:fontcolor=white:fontsize=22,"
        "drawtext=text='Accessibility Service\\: ACTIVE':x=80:y=200:fontcolor=#4FACFE:fontsize=22,"
        "drawtext=text='Floating Pill Coordinate\\: [40\\, 560]':x=80:y=240:fontcolor=#A0AEC0:fontsize=20,"
        "drawtext=text='SYSTEM-WIDE CLARITY LAYER':x=1360:y=100:fontcolor=#FF7E5F:fontsize=32:box=1:boxcolor=#000000@0.7:boxborderw=10,"
        "drawtext=text='1. Screen Hierarchy Scanned':x=1360:y=160:fontcolor=white:fontsize=22,"
        "drawtext=text='2. Visual Noise Suppressed':x=1360:y=200:fontcolor=white:fontsize=22,"
        "drawtext=text='3. Clarity Card Overlay':x=1360:y=240:fontcolor=#00F2FE:fontsize=22,"
        "drawtext=text='4. Assisted Focus Auto-Tap':x=1360:y=280:fontcolor=#FEB47B:fontsize=22,format=yuv420p[v]",
        "-map", "[v]", "-map", "1:a",
        "-c:v", "libx264", "-c:a", "aac", "-b:a", "192k",
        "-t", f"{duration:.3f}",
        out_path
    ]
    subprocess.run(cmd, check=True)

def build_scene_3(duration, audio_path, out_path):
    print(">> Rendering Scene 3: Cognitive Profile Personalization...")
    img = os.path.join(ASSETS_DIR, "infographic_2_cognitive_transformation.png")
    cmd = [
        FFMPEG, "-y",
        "-loop", "1", "-i", img,
        "-i", audio_path,
        "-filter_complex",
        f"[0:v]scale=1920:1080,format=yuv420p[v]",
        "-map", "[v]", "-map", "1:a",
        "-c:v", "libx264", "-c:a", "aac", "-b:a", "192k",
        "-t", f"{duration:.3f}",
        out_path
    ]
    subprocess.run(cmd, check=True)

def build_scene_4(duration, audio_path, out_path):
    print(">> Rendering Scene 4: RevenueCat Pro Monetization & Judge Pass...")
    img = os.path.join(ASSETS_DIR, "infographic_3_monetization_retention.png")
    cmd = [
        FFMPEG, "-y",
        "-loop", "1", "-i", img,
        "-i", audio_path,
        "-filter_complex",
        f"[0:v]scale=1920:1080,format=yuv420p[v]",
        "-map", "[v]", "-map", "1:a",
        "-c:v", "libx264", "-c:a", "aac", "-b:a", "192k",
        "-t", f"{duration:.3f}",
        out_path
    ]
    subprocess.run(cmd, check=True)

def build_scene_5(duration, audio_path, out_path):
    print(">> Rendering Scene 5: OneSignal Friction Retention Journeys...")
    img = os.path.join(ASSETS_DIR, "infographic_3_monetization_retention.png")
    # Slight crop/focus on the OneSignal journey section
    cmd = [
        FFMPEG, "-y",
        "-loop", "1", "-i", img,
        "-i", audio_path,
        "-filter_complex",
        f"[0:v]scale=1920:1080,format=yuv420p[v]",
        "-map", "[v]", "-map", "1:a",
        "-c:v", "libx264", "-c:a", "aac", "-b:a", "192k",
        "-t", f"{duration:.3f}",
        out_path
    ]
    subprocess.run(cmd, check=True)

def build_scene_6(duration, audio_path, out_path):
    print(">> Rendering Scene 6: Architecture Overview & OSS Outro...")
    img = os.path.join(ASSETS_DIR, "infographic_1_architecture_flow.png")
    cmd = [
        FFMPEG, "-y",
        "-loop", "1", "-i", img,
        "-i", audio_path,
        "-filter_complex",
        "[0:v]scale=1920:1080,"
        "drawtext=text='ClearPath\\: Cognitive Clarity Layer for Android':x=(w-text_w)/2:y=940:fontcolor=#00F2FE:fontsize=30:box=1:boxcolor=#000000@0.8:boxborderw=10,"
        "drawtext=text='GitHub\\: github.com/hackersclub111/ClearPath  |  MIT License  |  RevenueCat Ship-a-thon 2026':x=(w-text_w)/2:y=990:fontcolor=white:fontsize=22:box=1:boxcolor=#000000@0.8:boxborderw=8,format=yuv420p[v]",
        "-map", "[v]", "-map", "1:a",
        "-c:v", "libx264", "-c:a", "aac", "-b:a", "192k",
        "-t", f"{duration:.3f}",
        out_path
    ]
    subprocess.run(cmd, check=True)

def concatenate_scenes(segment_files, final_output):
    print(">> Concatenating all scenes into master video...")
    concat_list_path = os.path.join(BUILD_DIR, "concat_list.txt")
    with open(concat_list_path, "w", encoding="utf-8") as f:
        for seg in segment_files:
            # Escape path for ffmpeg concat demuxer
            clean_path = seg.replace("\\", "/")
            f.write(f"file '{clean_path}'\n")
            
    cmd = [
        FFMPEG, "-y",
        "-f", "concat",
        "-safe", "0",
        "-i", concat_list_path,
        "-c:v", "libx264",
        "-c:a", "aac",
        "-b:a", "192k",
        "-af", "loudnorm",
        final_output
    ]
    subprocess.run(cmd, check=True)
    print(f">> [DONE] Master Winner Demo Video created at: {final_output}")

def main():
    print("=== ClearPath Master Demo Video Compositor ===")
    
    # 1. Inspect audio durations
    scenes = [
        ("scene_1", os.path.join(AUDIO_DIR, "scene_1_problem.mp3")),
        ("scene_2", os.path.join(AUDIO_DIR, "scene_2_interaction.mp3")),
        ("scene_3", os.path.join(AUDIO_DIR, "scene_3_profiles.mp3")),
        ("scene_4", os.path.join(AUDIO_DIR, "scene_4_monetization.mp3")),
        ("scene_5", os.path.join(AUDIO_DIR, "scene_5_retention.mp3")),
        ("scene_6", os.path.join(AUDIO_DIR, "scene_6_outro.mp3")),
    ]
    
    durations = {}
    for sid, apath in scenes:
        d = get_audio_duration(apath)
        durations[sid] = d
        print(f"Scene {sid} audio duration: {d:.2f}s")
        
    raw_video = os.path.join(PROJECT_DIR, "clearpath_hardware_raw.mp4")
    if not os.path.exists(raw_video):
        raise FileNotFoundError(f"Missing {raw_video}")
        
    seg1 = os.path.join(BUILD_DIR, "seg1.mp4")
    seg2 = os.path.join(BUILD_DIR, "seg2.mp4")
    seg3 = os.path.join(BUILD_DIR, "seg3.mp4")
    seg4 = os.path.join(BUILD_DIR, "seg4.mp4")
    seg5 = os.path.join(BUILD_DIR, "seg5.mp4")
    seg6 = os.path.join(BUILD_DIR, "seg6.mp4")
    
    build_scene_1(durations["scene_1"], scenes[0][1], seg1)
    build_scene_2(durations["scene_2"], scenes[1][1], raw_video, seg2)
    build_scene_3(durations["scene_3"], scenes[2][1], seg3)
    build_scene_4(durations["scene_4"], scenes[3][1], seg4)
    build_scene_5(durations["scene_5"], scenes[4][1], seg5)
    build_scene_6(durations["scene_6"], scenes[5][1], seg6)
    
    segments = [seg1, seg2, seg3, seg4, seg5, seg6]
    concatenate_scenes(segments, OUTPUT_VIDEO)
    
    # Check output
    if os.path.exists(OUTPUT_VIDEO):
        sz_mb = os.path.getsize(OUTPUT_VIDEO) / (1024 * 1024)
        total_dur = get_audio_duration(OUTPUT_VIDEO)
        print(f"\n==========================================")
        print(f"WINNER DEMO VIDEO COMPILED SUCCESSFULLY!")
        print(f"Path: {OUTPUT_VIDEO}")
        print(f"File Size: {sz_mb:.2f} MB")
        print(f"Total Duration: {total_dur:.2f} seconds")
        print(f"Resolution: 1920x1080 Full HD")
        print(f"Audio: Neural Studio Narration (Loudnorm Normalized)")
        print(f"==========================================\n")

if __name__ == "__main__":
    main()
