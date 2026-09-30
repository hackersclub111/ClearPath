import subprocess
import time
import os
import sys

ADB = r"C:\AndroidTools\platform-tools\adb.exe"

def run_adb(args):
    cmd = [ADB] + args
    print(f">> {' '.join(cmd)}")
    res = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    if res.stdout.strip():
        print(f"[out] {res.stdout.strip()[:200]}")
    if res.stderr.strip():
        print(f"[err] {res.stderr.strip()[:200]}")
    return res

def main():
    print("=== ClearPath Hardware Recording Flow ===")
    
    # 1. Bring app to front
    run_adb(["shell", "am", "start", "-n", "com.musheer360.swiftslate/.MainActivity"])
    time.sleep(2)
    
    # 2. Start screenrecord on device (asynchronous via Popen)
    rec_cmd = [ADB, "shell", "screenrecord", "--size", "1080x2400", "--bit-rate", "12000000", "--time-limit", "60", "/sdcard/clearpath_live.mp4"]
    print("Starting screenrecord on Infinix device...")
    rec_proc = subprocess.Popen(rec_cmd)
    
    try:
        # Give recorder 2 seconds to initialize frame buffer
        time.sleep(2)
        
        # Action 1: On Dashboard, tap on Settings tab
        print("Navigating to Settings...")
        run_adb(["shell", "input", "tap", "918", "2184"])
        time.sleep(3)
        
        # Action 2: Scroll down in Settings to reveal About section
        print("Scrolling to About section...")
        run_adb(["shell", "input", "swipe", "540", "1800", "540", "600", "400"])
        time.sleep(3)
        run_adb(["shell", "input", "swipe", "540", "1800", "540", "1000", "400"])
        time.sleep(4)
        
        # Action 3: Launch Chrome with the Judge Demo simulation
        print("Launching student portal demo in Chrome...")
        run_adb(["shell", "am", "start", "-a", "android.intent.action.VIEW", "-d", "http://localhost:8080/ClearPath_Judge_Demo.html"])
        time.sleep(4)
        
        # Action 4: Scroll down on the student portal
        print("Interacting with portal...")
        run_adb(["shell", "input", "swipe", "540", "1600", "540", "800", "500"])
        time.sleep(3)
        
        # Action 5: Tap floating pill on left margin
        print("Tapping floating pill...")
        run_adb(["shell", "input", "tap", "40", "560"])
        time.sleep(4)
        
        # Action 6: Return to ClearPath Dashboard
        print("Returning to ClearPath...")
        run_adb(["shell", "am", "start", "-n", "com.musheer360.swiftslate/.MainActivity"])
        time.sleep(2)
        run_adb(["shell", "input", "tap", "90", "2184"])
        time.sleep(3)
        
    finally:
        print("Stopping screenrecord...")
        # Send SIGINT to screenrecord on device
        run_adb(["shell", "pkill", "-2", "screenrecord"])
        time.sleep(2)
        rec_proc.terminate()
        try:
            rec_proc.wait(timeout=5)
        except Exception:
            rec_proc.kill()
            
    print("Pulling recorded video from phone...")
    time.sleep(2)
    run_adb(["pull", "/sdcard/clearpath_live.mp4", "clearpath_hardware_raw.mp4"])
    
    if os.path.exists("clearpath_hardware_raw.mp4"):
        sz = os.path.getsize("clearpath_hardware_raw.mp4")
        print(f"[SUCCESS] Pulled clearpath_hardware_raw.mp4 ({sz} bytes)")
    else:
        print("[ERROR] Failed to pull clearpath_hardware_raw.mp4")

if __name__ == "__main__":
    main()
