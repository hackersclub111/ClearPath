import os
from PIL import Image, ImageDraw, ImageFont

ASSETS_DIR = os.path.join(os.path.dirname(__file__), "assets")
os.makedirs(ASSETS_DIR, exist_ok=True)

def create_app_icon():
    size = (1024, 1024)
    img = Image.new("RGBA", size, (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Rounded rectangle background with vibrant gradient
    # Deep indigo (#1E1B4B) to electric blue (#0284C7)
    for y in range(1024):
        t = y / 1024.0
        r = int(30 * (1 - t) + 2 * t)
        g = int(27 * (1 - t) + 132 * t)
        b = int(75 * (1 - t) + 199 * t)
        draw.line([(0, y), (1024, y)], fill=(r, g, b, 255))

    # Mask with rounded squircle
    mask = Image.new("L", size, 0)
    mask_draw = ImageDraw.Draw(mask)
    mask_draw.rounded_rectangle([(0, 0), (1024, 1024)], radius=220, fill=255)
    img.putalpha(mask)

    draw = ImageDraw.Draw(img)

    # Inner glowing path lines
    # Draw geometric "ClearPath" emblem: sleek stylized path cutting through noise
    # Outer circle guide
    center = (512, 512)
    draw.ellipse([(212, 212), (812, 812)], outline=(56, 189, 248, 80), width=16)

    # Stylized central Lightning / Forward Arrow
    # Points creating a modern sharp path
    polygon_points = [
        (560, 230),
        (380, 520),
        (500, 520),
        (460, 790),
        (660, 480),
        (540, 480),
        (620, 230)
    ]
    draw.polygon(polygon_points, fill=(56, 189, 248, 255))
    draw.polygon(polygon_points, outline=(255, 255, 255, 200), width=6)

    out_path = os.path.join(ASSETS_DIR, "icon_1024x1024.png")
    img.save(out_path, "PNG")
    print(f"[OK] Generated 1024x1024 Icon: {out_path}")

def create_screenshot_transformation():
    # 1179 x 2556 frameless screenshot (iPhone / modern Android standard)
    size = (1179, 2556)
    img = Image.new("RGB", size, (15, 23, 42)) # Slate 900
    draw = ImageDraw.Draw(img)

    # Header bar
    draw.rectangle([(0, 0), (1179, 140)], fill=(30, 41, 59))
    draw.text((60, 60), "Academic LMS — Student Fees & Registration", fill=(255, 255, 255))

    # Background cluttered app mock
    # Warning banner
    draw.rounded_rectangle([(60, 180), (1119, 320)], radius=20, fill=(254, 226, 226), outline=(248, 113, 113), width=3)
    draw.text((100, 210), "URGENT WARNING: Unpaid Semester 5 Tuition Fees", fill=(153, 27, 27))
    draw.text((100, 260), "Late fine of $50.00 will be charged after Oct 1, 5:00 PM EDT.", fill=(185, 28, 28))

    # Cluttered Grid of 14 secondary buttons
    btn_titles = [
        "View Syllabus PDF", "Mess Rebate", "Hostel Room Change", "Grade Card 2025",
        "Library Dues", "Sports Committee", "Elective Survey 1", "Elective Survey 2",
        "Club Nominations", "Bus Pass Renewal", "Anti-Ragging Affidavit", "Faculty Feedback",
        "WiFi Mac Address", "Career Fair Registration"
    ]
    y_start = 360
    for idx, title in enumerate(btn_titles):
        col = idx % 2
        row = idx // 2
        x1 = 60 + col * 540
        y1 = y_start + row * 130
        draw.rounded_rectangle([(x1, y1), (x1 + 510, y1 + 105)], radius=16, fill=(51, 65, 85))
        draw.text((x1 + 30, y1 + 38), title, fill=(203, 213, 225))

    # Target Payment button with focus beacon
    y_pay = y_start + 7 * 130 + 20
    draw.rounded_rectangle([(60, y_pay), (1119, y_pay + 140)], radius=20, fill=(37, 99, 235))
    draw.text((360, y_pay + 48), "PAY TUITION FEE: $420.00", fill=(255, 255, 255))

    # Pulsating Focus Beacon around target button
    draw.rounded_rectangle([(48, y_pay - 12), (1131, y_pay + 152)], radius=24, outline=(56, 189, 248), width=8)

    # Transformed Floating Clarity Card (Bottom Sheet Overlay)
    y_card = 1600
    draw.rounded_rectangle([(50, y_card), (1129, 2480)], radius=48, fill=(15, 23, 42), outline=(56, 189, 248), width=6)

    # Card Top Header
    draw.rounded_rectangle([(90, y_card + 40), (460, y_card + 100)], radius=20, fill=(30, 41, 59))
    draw.text((120, y_card + 55), "✨ CLEARPATH CLARITY", fill=(56, 189, 248))

    draw.text((900, y_card + 55), "PRO UNLIMITED", fill=(74, 222, 128))

    # Card Headline
    draw.text((90, y_card + 140), "Semester 5 tuition fee of $420.00 is due tomorrow at 5:00 PM.", fill=(255, 255, 255))
    draw.text((90, y_card + 200), "Pay now to prevent a $50 late registration surcharge.", fill=(148, 163, 184))

    # Priorities
    draw.text((90, y_card + 290), "• Pay $420.00 balance before deadline", fill=(226, 232, 240))
    draw.text((90, y_card + 350), "• Disregard 14 other elective links and surveys for now", fill=(226, 232, 240))
    draw.text((90, y_card + 410), "• Payment gateway verified: Instant receipt generated", fill=(226, 232, 240))

    # Big "Do This Next" Action Button
    y_act = y_card + 560
    draw.rounded_rectangle([(90, y_act), (1089, y_act + 160)], radius=28, fill=(56, 189, 248))
    draw.text((280, y_act + 55), "👉 PAY $420.00 TUITION NOW", fill=(15, 23, 42))

    out_path = os.path.join(ASSETS_DIR, "screenshot_1_transformation_1179x2556.png")
    img.save(out_path, "PNG")
    print(f"[OK] Generated Screenshot 1: {out_path}")

def create_screenshot_paywall():
    size = (1179, 2556)
    img = Image.new("RGB", size, (9, 13, 22))
    draw = ImageDraw.Draw(img)

    # Top Pro Badge
    draw.rounded_rectangle([(440, 180), (740, 250)], radius=24, fill=(99, 102, 241))
    draw.text((475, 205), "REVENUECAT PRO", fill=(255, 255, 255))

    # Title
    draw.text((360, 310), "ClearPath Pro", fill=(255, 255, 255))
    draw.text((220, 380), "Effortless cognitive clarity across every Android app", fill=(148, 163, 184))

    # Features
    features = [
        ("Unlimited Screen Simplifications", "Free tier limited to 5 screens/day"),
        ("Multi-Profile Family Mode", "3 saved profiles for seniors, students, & ADHD"),
        ("Assisted Auto-Tap & Focus Beacon", "Guides and performs physical taps safely"),
        ("Ultra-Low-Latency AI Engine", "Sub-800ms priority extraction on all apps")
    ]
    y_f = 500
    for title, desc in features:
        draw.rounded_rectangle([(80, y_f), (1099, y_f + 160)], radius=24, fill=(17, 24, 39), outline=(31, 41, 55), width=2)
        draw.text((130, y_f + 35), f"✓  {title}", fill=(56, 189, 248))
        draw.text((130, y_f + 95), desc, fill=(156, 163, 175))
        y_f += 190

    # Plans
    y_p = 1350
    draw.rounded_rectangle([(80, y_p), (570, y_p + 260)], radius=24, fill=(17, 24, 39), outline=(31, 41, 55), width=2)
    draw.text((120, y_p + 40), "Monthly Plan", fill=(255, 255, 255))
    draw.text((120, y_p + 110), "$4.99 / mo", fill=(56, 189, 248))

    draw.rounded_rectangle([(609, y_p), (1099, y_p + 260)], radius=24, fill=(30, 41, 59), outline=(56, 189, 248), width=4)
    draw.text((649, y_p + 40), "Annual Plan (SAVE 33%)", fill=(251, 191, 36))
    draw.text((649, y_p + 110), "$39.99 / yr", fill=(56, 189, 248))

    # Main CTA
    y_cta = 1680
    draw.rounded_rectangle([(80, y_cta), (1099, y_cta + 150)], radius=28, fill=(56, 189, 248))
    draw.text((320, y_cta + 50), "Start 7-Day Free Trial ($39.99/yr)", fill=(15, 23, 42))

    # Devpost Judge Bypass Card
    y_j = 1920
    draw.rounded_rectangle([(80, y_j), (1099, y_j + 380)], radius=28, fill=(30, 27, 75), outline=(99, 102, 241), width=4)
    draw.text((130, y_j + 40), "🔓 Devpost Judge Instant Bypass", fill=(255, 255, 255))
    draw.text((130, y_j + 100), "Hackathon judges can test all Pro features without billing constraints.", fill=(203, 213, 225))

    draw.rounded_rectangle([(130, y_j + 160), (1049, y_j + 250)], radius=16, fill=(15, 23, 42))
    draw.text((160, y_j + 190), "CODE: SHIPATON2026_JUDGE", fill=(56, 189, 248))

    draw.rounded_rectangle([(130, y_j + 270), (1049, y_j + 350)], radius=16, fill=(79, 70, 229))
    draw.text((360, y_j + 295), "✓ Pro Unlocked for Jury Review", fill=(255, 255, 255))

    out_path = os.path.join(ASSETS_DIR, "screenshot_2_paywall_1179x2556.png")
    img.save(out_path, "PNG")
    print(f"[OK] Generated Screenshot 2: {out_path}")

def create_thumbnail_1200x800():
    # 1200 x 800 (3:2 ratio required by Devpost project card grid)
    size = (1200, 800)
    img = Image.new("RGB", size, (11, 15, 25)) # Deep slate background
    draw = ImageDraw.Draw(img)

    # Ambient background glow
    for r in range(400, 0, -20):
        alpha = int(15 * (1 - r / 400.0))
        draw.ellipse([(100 - r, 100 - r), (100 + r, 100 + r)], fill=(99, 102, 241, alpha))

    # Top Brand / Header Bar
    # Icon Emblem
    draw.rounded_rectangle([(60, 50), (120, 110)], radius=16, fill=(56, 189, 248))
    draw.polygon([(90, 60), (75, 85), (87, 85), (80, 102), (102, 78), (90, 78), (98, 60)], fill=(15, 23, 42))

    draw.text((140, 62), "CLEARPATH", fill=(255, 255, 255))
    draw.text((290, 64), "FOR ANDROID", fill=(56, 189, 248))

    # Hackathon Badges
    draw.rounded_rectangle([(730, 55), (940, 105)], radius=12, fill=(30, 27, 75), outline=(99, 102, 241), width=2)
    draw.text((750, 72), "RevenueCat 2026", fill=(199, 210, 254))

    draw.rounded_rectangle([(955, 55), (1140, 105)], radius=12, fill=(6, 78, 59), outline=(16, 185, 129), width=2)
    draw.text((975, 72), "Student Next Gen", fill=(110, 231, 183))

    # Main Big Hero Title
    draw.text((60, 150), "Cognitive Clarity Layer for Android", fill=(255, 255, 255))
    draw.text((60, 210), "Transforming 17 Confusing Buttons into 1 Clear Action", fill=(148, 163, 184))

    # Left Card: The Cluttered App Screen (Before)
    x_l = 60
    y_b = 280
    w_card = 490
    h_card = 460
    draw.rounded_rectangle([(x_l, y_b), (x_l + w_card, y_b + h_card)], radius=24, fill=(17, 24, 39), outline=(239, 68, 68), width=3)
    
    # "BEFORE" Tag
    draw.rounded_rectangle([(x_l + 20, y_b + 20), (x_l + 180, y_b + 60)], radius=10, fill=(153, 27, 27))
    draw.text((x_l + 35, y_b + 32), "BEFORE: OVERLOAD", fill=(254, 202, 202))

    draw.text((x_l + 200, y_b + 32), "Cluttered College LMS", fill=(156, 163, 175))

    # Fake cluttered buttons inside Before
    btn_labels = [
        "View Syllabus", "Hostel Dues", "Mess Rebate", "Grade Card 2025",
        "WiFi MAC Form", "Club Elections", "Anti-Ragging", "Bus Pass Renewal",
        "Library Card", "Faculty Survey"
    ]
    for i, lbl in enumerate(btn_labels):
        r = i // 2
        c = i % 2
        bx = x_l + 25 + c * 225
        by = y_b + 80 + r * 65
        draw.rounded_rectangle([(bx, by), (bx + 215, by + 50)], radius=10, fill=(31, 41, 55))
        draw.text((bx + 15, by + 18), lbl, fill=(156, 163, 175))

    draw.rounded_rectangle([(x_l + 25, y_b + 400), (x_l + 465, y_b + 445)], radius=10, fill=(185, 28, 28))
    draw.text((x_l + 50, y_b + 412), "Late Fine: $50.00 After 5 PM Tomorrow!", fill=(255, 255, 255))

    # Right Card: The Transformed Clarity Card (After)
    x_r = 650
    draw.rounded_rectangle([(x_r, y_b), (x_r + w_card, y_b + h_card)], radius=24, fill=(15, 23, 42), outline=(56, 189, 248), width=3)
    
    # "AFTER" Tag
    draw.rounded_rectangle([(x_r + 20, y_b + 20), (x_r + 200, y_b + 60)], radius=10, fill=(2, 132, 199))
    draw.text((x_r + 35, y_b + 32), "AFTER: CLEARPATH", fill=(255, 255, 255))
    draw.text((x_r + 220, y_b + 32), "OS Accessibility Layer", fill=(56, 189, 248))

    # One sentence headline
    draw.text((x_r + 25, y_b + 85), "Semester 5 tuition fee ($420.00) is due tomorrow at 5PM.", fill=(255, 255, 255))
    draw.text((x_r + 25, y_b + 115), "Pay today to prevent a $50 late registration surcharge.", fill=(148, 163, 184))

    # Key Priorities
    draw.text((x_r + 25, y_b + 170), "• Pay $420.00 balance before deadline", fill=(226, 232, 240))
    draw.text((x_r + 25, y_b + 210), "• Disregard 14 other elective links for now", fill=(226, 232, 240))
    draw.text((x_r + 25, y_b + 250), "• Verified gateway: Instant digital receipt", fill=(226, 232, 240))

    # Big Glowing CTA Button
    draw.rounded_rectangle([(x_r + 25, y_b + 310), (x_r + 465, y_b + 380)], radius=16, fill=(56, 189, 248))
    draw.text((x_r + 85, y_b + 335), "👉 PAY $420.00 TUITION NOW", fill=(15, 23, 42))

    # Tech Stack Footer inside After card
    draw.text((x_r + 25, y_b + 415), "RevenueCat Pro  •  OneSignal Journeys  •  Android Compose", fill=(110, 231, 183))

    out_path = os.path.join(ASSETS_DIR, "thumbnail_1200x800.png")
    img.save(out_path, "PNG")
    print(f"[OK] Generated 1200x800 Devpost Thumbnail: {out_path}")

if __name__ == "__main__":
    create_app_icon()
    create_screenshot_transformation()
    create_screenshot_paywall()
    create_thumbnail_1200x800()
    print("All Devpost submission visual assets successfully generated.")
