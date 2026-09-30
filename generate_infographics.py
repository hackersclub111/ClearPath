"""High-Density Winner-Grade Infographics Generator for ClearPath.

Generates 3 professional 1920x1080 architectural infographics & submission posters:
  1. infographic_1_architecture_flow.png: 70/30 Foundation & Accessibility Pipeline
  2. infographic_2_cognitive_transformation.png: Before vs After Cognitive Distillation
  3. infographic_3_monetization_retention.png: RevenueCat & OneSignal Retention Loop
"""
from __future__ import annotations

import os
from PIL import Image, ImageDraw, ImageFont

ROOT_DIR = os.path.abspath(os.path.dirname(__file__))
ASSETS_DIR = os.path.join(ROOT_DIR, "assets")
os.makedirs(ASSETS_DIR, exist_ok=True)


def get_fonts():
    """Returns system fonts if available, or default."""
    try:
        font_title = ImageFont.truetype("C:/Windows/Fonts/arialbd.ttf", 36)
        font_sub = ImageFont.truetype("C:/Windows/Fonts/segoeui.ttf", 20)
        font_h2 = ImageFont.truetype("C:/Windows/Fonts/arialbd.ttf", 24)
        font_body = ImageFont.truetype("C:/Windows/Fonts/segoeui.ttf", 16)
        font_body_bold = ImageFont.truetype("C:/Windows/Fonts/arialbd.ttf", 16)
        font_badge = ImageFont.truetype("C:/Windows/Fonts/arialbd.ttf", 14)
        font_code = ImageFont.truetype("C:/Windows/Fonts/consola.ttf", 15)
    except Exception:
        font_title = font_sub = font_h2 = font_body = font_body_bold = font_badge = font_code = ImageFont.load_default()
    return {
        "title": font_title,
        "sub": font_sub,
        "h2": font_h2,
        "body": font_body,
        "body_bold": font_body_bold,
        "badge": font_badge,
        "code": font_code,
    }


def draw_header(draw: ImageDraw.ImageDraw, title: str, subtitle: str, badge_text: str, badge_color: tuple[int, int, int], f: dict):
    # Top banner background
    draw.rectangle([(0, 0), (1920, 130)], fill=(15, 23, 42))
    draw.line([(0, 130), (1920, 130)], fill=(56, 189, 248), width=3)

    # Logo / Icon
    draw.rounded_rectangle([(60, 30), (130, 100)], radius=18, fill=(56, 189, 248))
    draw.polygon([(95, 42), (78, 68), (92, 68), (84, 88), (110, 60), (96, 60), (105, 42)], fill=(15, 23, 42))

    # Title & Subtitle
    draw.text((155, 32), title, fill=(255, 255, 255), font=f["title"])
    draw.text((155, 80), subtitle, fill=(148, 163, 184), font=f["sub"])

    # Badge on right
    draw.rounded_rectangle([(1520, 42), (1860, 92)], radius=14, fill=badge_color, outline=(255, 255, 255), width=1)
    draw.text((1550, 56), badge_text, fill=(255, 255, 255), font=f["badge"])


def create_infographic_1():
    """Infographic 1: End-to-End System-Wide Accessibility Architecture."""
    img = Image.new("RGB", (1920, 1080), (9, 13, 22))
    draw = ImageDraw.Draw(img)
    f = get_fonts()

    draw_header(draw, "CLEARPATH: SYSTEM-WIDE COGNITIVE ARCHITECTURE", 
                "70% SwiftSlate Production Daemon + 30% Cognitive Clarity Engine & Assisted Action",
                "REVENUECAT SHIP-A-THON 2026", (30, 27, 75), f)

    # 4 Architecture Columns / Stages
    cols = [
        {
            "num": "01",
            "title": "HOST APPS & SENSING",
            "subtitle": "Unmodified Android Apps",
            "color": (239, 68, 68),
            "items": [
                ("Target Apps", "University LMS, Mobile Banking, KYC, Medical Co-Pay, Airline Check-in"),
                ("Sensor", "Android AccessibilityService Daemon (Background Lifecycle)"),
                ("Extraction", "Live AccessibilityNodeInfo tree parsing & node bounds (Rect)"),
                ("Optimization", "Token-lean UI snapshot: strips layout noise (<1,000 tokens)")
            ]
        },
        {
            "num": "02",
            "title": "COGNITIVE PROFILES",
            "subtitle": "Adaptive User Constraints",
            "color": (99, 102, 241),
            "items": [
                ("Student Mode", "Filters 14+ secondary links, isolates payment & fee deadlines"),
                ("Senior Friendly", "Extra-large buttons (1.5x), plain language, zero financial jargon"),
                ("ADHD Focus", "Strict single-choice isolation; removes all peripheral stimulation"),
                ("Hindi Clarity", "Native Devanagari translation & plain-Hindi bureaucratic simplification")
            ]
        },
        {
            "num": "03",
            "title": "CLARITY ADAPTATION",
            "subtitle": "AI Priority Extraction",
            "color": (56, 189, 248),
            "items": [
                ("Neural Engine", "Multi-Provider Router: Gemini 1.5 Flash / Groq Llama 3.3"),
                ("Deterministic Output", "Strict JSON: summary, priorities, nextAction, targetButtonText"),
                ("Focus Beacon", "Dynamic pulsating screen highlighter over target button node"),
                ("Assisted Auto-Tap", "NextActionEngine executes physical node tap on confirmation")
            ]
        },
        {
            "num": "04",
            "title": "MONETIZE & RETAIN",
            "subtitle": "RevenueCat + OneSignal",
            "color": (16, 185, 129),
            "items": [
                ("RevenueCat SDK", "Free Tier: 5 screens/day | Pro Tier: $4.99/mo, $39.99/yr"),
                ("Judge Bypass", "Code: SHIPATON2026_JUDGE unlocks unlimited Pro testing"),
                ("OneSignal Journeys", "Form Friction Follow-up: 2-hour reminder on unfinished forms"),
                ("Cognitive Reset", "Daily 9:00 AM encouragement summarizing completed tasks")
            ]
        }
    ]

    col_w = 420
    gap = 25
    x_start = 60
    y_start = 160

    for idx, c in enumerate(cols):
        cx = x_start + idx * (col_w + gap)
        # Outer Card
        draw.rounded_rectangle([(cx, y_start), (cx + col_w, y_start + 760)], radius=20, fill=(17, 24, 39), outline=c["color"], width=2)

        # Stage Number & Header
        draw.rounded_rectangle([(cx + 20, y_start + 20), (cx + 80, y_start + 65)], radius=10, fill=c["color"])
        draw.text((cx + 32, y_start + 30), c["num"], fill=(255, 255, 255), font=f["body_bold"])

        draw.text((cx + 95, y_start + 24), c["title"], fill=(255, 255, 255), font=f["h2"])
        draw.text((cx + 95, y_start + 55), c["subtitle"], fill=(148, 163, 184), font=f["sub"])

        draw.line([(cx + 20, y_start + 85), (cx + col_w - 20, y_start + 85)], fill=(31, 41, 55), width=2)

        # Content blocks inside column
        by = y_start + 110
        for title, desc in c["items"]:
            draw.rounded_rectangle([(cx + 20, by), (cx + col_w - 20, by + 130)], radius=14, fill=(11, 15, 25), outline=(31, 41, 55), width=1)
            draw.text((cx + 35, by + 16), title, fill=c["color"], font=f["body_bold"])
            # Word-wrap desc
            words = desc.split()
            lines = []
            curr = ""
            for w in words:
                if len(curr + " " + w) < 36:
                    curr = (curr + " " + w).strip()
                else:
                    lines.append(curr)
                    curr = w
            if curr:
                lines.append(curr)

            for l_idx, line in enumerate(lines[:3]):
                draw.text((cx + 35, by + 46 + l_idx * 24), line, fill=(203, 213, 225), font=f["body"])

            by += 150

    # Bottom Footer Bar
    draw.rectangle([(0, 960), (1920, 1080)], fill=(15, 23, 42))
    draw.line([(0, 960), (1920, 960)], fill=(31, 41, 55), width=2)
    draw.text((60, 995), "70/30 ARCHITECTURE PRINCIPLE: Battle-tested SwiftSlate Android daemon (650+ stars, MIT) delivers rock-solid OS stability.", fill=(148, 163, 184), font=f["body"])
    draw.text((60, 1025), "ClearPath's novel 30% layer injects cognitive ergonomics, RevenueCat monetization, and OneSignal retention journeys.", fill=(56, 189, 248), font=f["body_bold"])

    out_file = os.path.join(ASSETS_DIR, "infographic_1_architecture_flow.png")
    img.save(out_file, "PNG")
    print(f"[OK] Generated Infographic 1: {out_file}")


def create_infographic_2():
    """Infographic 2: The Cognitive Transformation Breakdown (Before vs After)."""
    img = Image.new("RGB", (1920, 1080), (9, 13, 22))
    draw = ImageDraw.Draw(img)
    f = get_fonts()

    draw_header(draw, "COGNITIVE CLARITY: BEFORE VS AFTER SCREEN TRANSFORMATION",
                "How ClearPath Distills 17 Bureaucratic Buttons into 1 Action in <800 Milliseconds",
                "UX INNOVATION AWARD", (14, 116, 144), f)

    w_box = 870
    h_box = 760
    y_box = 160

    # Left: The Cluttered App Screen (BEFORE)
    x_l = 60
    draw.rounded_rectangle([(x_l, y_box), (x_l + w_box, y_box + h_box)], radius=24, fill=(17, 24, 39), outline=(239, 68, 68), width=3)
    
    # Header Inside Before
    draw.rounded_rectangle([(x_l + 25, y_box + 25), (x_l + 320, y_box + 75)], radius=12, fill=(153, 27, 27))
    draw.text((x_l + 45, y_box + 38), "❌ BEFORE CLEARPATH", fill=(255, 255, 255), font=f["body_bold"])
    draw.text((x_l + 345, y_box + 38), "Academic LMS Fee & Registration Portal", fill=(156, 163, 175), font=f["sub"])

    # Penalty Banner
    draw.rounded_rectangle([(x_l + 25, y_box + 95), (x_l + w_box - 25, y_box + 175)], radius=14, fill=(254, 226, 226), outline=(248, 113, 113), width=2)
    draw.text((x_l + 45, y_box + 110), "⚠️ CRITICAL NOTIFICATION: Unpaid Semester 5 Tuition Fees", fill=(153, 27, 27), font=f["body_bold"])
    draw.text((x_l + 45, y_box + 138), "A penalty surcharge of $50.00 will be added if not submitted before 5:00 PM tomorrow. Please clear dues.", fill=(185, 28, 28), font=f["body"])

    # Cluttered Grid of buttons
    fake_buttons = [
        "View Syllabus PDF", "Mess Rebate Application", "Hostel Room Change", "Grade Card 2025",
        "Library Dues Clearance", "Sports Complex Pass", "Elective Survey 1", "Elective Survey 2",
        "Club Nominations", "Bus Pass Renewal", "Anti-Ragging Affidavit", "Faculty Feedback",
        "WiFi MAC Registration", "Career Fair Registration", "Hostel Maintenance", "Alumni Directory"
    ]
    for i, btn in enumerate(fake_buttons):
        r = i // 2
        c = i % 2
        bx = x_l + 25 + c * 415
        by = y_box + 195 + r * 54
        draw.rounded_rectangle([(bx, by), (bx + 400, by + 44)], radius=10, fill=(31, 41, 55))
        draw.text((bx + 20, by + 12), btn, fill=(156, 163, 175), font=f["body"])

    # Buried Target Payment Button
    draw.rounded_rectangle([(x_l + 25, y_box + 650), (x_l + w_box - 25, y_box + 725)], radius=14, fill=(37, 99, 235))
    draw.text((x_l + 260, y_box + 672), "PAY TUITION FEE: $420.00 (GATEWAY 1)", fill=(255, 255, 255), font=f["body_bold"])


    # Right: The ClearPath Transformed Experience (AFTER)
    x_r = 990
    draw.rounded_rectangle([(x_r, y_box), (x_r + w_box, y_box + h_box)], radius=24, fill=(15, 23, 42), outline=(56, 189, 248), width=3)

    # Header Inside After
    draw.rounded_rectangle([(x_r + 25, y_box + 25), (x_r + 340, y_box + 75)], radius=12, fill=(2, 132, 199))
    draw.text((x_r + 45, y_box + 38), "✨ AFTER: CLEARPATH", fill=(255, 255, 255), font=f["body_bold"])
    draw.text((x_r + 365, y_box + 38), "Cognitive Clarity Layer (Overlay)", fill=(56, 189, 248), font=f["sub"])

    # High Contrast Clarity Card
    draw.rounded_rectangle([(x_r + 30, y_box + 95), (x_r + w_box - 30, y_box + 725)], radius=20, fill=(11, 15, 25), outline=(56, 189, 248), width=2)

    # Header Badges inside card
    draw.rounded_rectangle([(x_r + 60, y_box + 125), (x_r + 240, y_box + 165)], radius=10, fill=(30, 41, 59))
    draw.text((x_r + 75, y_box + 136), "✨ Student Mode", fill=(56, 189, 248), font=f["body_bold"])

    draw.rounded_rectangle([(x_r + w_box - 240, y_box + 125), (x_r + w_box - 60, y_box + 165)], radius=10, fill=(6, 78, 59))
    draw.text((x_r + w_box - 215, y_box + 136), "PRO UNLIMITED", fill=(52, 211, 153), font=f["body_bold"])

    # 1 Headline Overview
    draw.text((x_r + 60, y_box + 195), "1-Sentence Plain Language Overview:", fill=(148, 163, 184), font=f["body"])
    draw.text((x_r + 60, y_box + 225), "\"Semester 5 tuition fee of $420.00 is due tomorrow at 5:00 PM.\"", fill=(255, 255, 255), font=f["h2"])
    draw.text((x_r + 60, y_box + 265), "Late penalty surcharge of $50 will be charged if unpaid after the deadline.", fill=(248, 113, 113), font=f["body"])

    # 3 Distilled Priorities
    draw.text((x_r + 60, y_box + 320), "Distilled Action Priorities:", fill=(148, 163, 184), font=f["body"])
    priorities = [
        "1. Pay $420.00 tuition balance immediately before the 5:00 PM cutoff",
        "2. Disregard 14 other elective surveys & hostel links for this session",
        "3. Instant digital receipt generated and archived upon payment confirmation"
    ]
    for p_idx, p_text in enumerate(priorities):
        draw.rounded_rectangle([(x_r + 60, y_box + 355 + p_idx * 65), (x_r + w_box - 60, y_box + 405 + p_idx * 65)], radius=10, fill=(30, 41, 59))
        draw.text((x_r + 80, y_box + 368 + p_idx * 65), p_text, fill=(226, 232, 240), font=f["body"])

    # Assisted Action Button
    draw.rounded_rectangle([(x_r + 60, y_box + 580), (x_r + w_box - 60, y_box + 675)], radius=18, fill=(56, 189, 248))
    draw.text((x_r + 210, y_box + 612), "👉 PAY $420.00 TUITION NOW", fill=(15, 23, 42), font=f["h2"])

    out_file = os.path.join(ASSETS_DIR, "infographic_2_cognitive_transformation.png")
    img.save(out_file, "PNG")
    print(f"[OK] Generated Infographic 2: {out_file}")


def create_infographic_3():
    """Infographic 3: Sustainable Monetization & OneSignal Retention Journeys."""
    img = Image.new("RGB", (1920, 1080), (9, 13, 22))
    draw = ImageDraw.Draw(img)
    f = get_fonts()

    draw_header(draw, "SUSTAINABLE MONETIZATION & THOUGHTFUL RETENTION",
                "RevenueCat Subscription Economics & OneSignal Mindful User Journeys",
                "KEEP THEM COMING BACK AWARD", (190, 24, 93), f)

    w_box = 870
    h_box = 760
    y_box = 160

    # Left: RevenueCat Monetization Architecture
    x_l = 60
    draw.rounded_rectangle([(x_l, y_box), (x_l + w_box, y_box + h_box)], radius=24, fill=(17, 24, 39), outline=(99, 102, 241), width=3)
    
    draw.rounded_rectangle([(x_l + 25, y_box + 25), (x_l + 380, y_box + 75)], radius=12, fill=(49, 46, 129))
    draw.text((x_l + 45, y_box + 38), "💳 REVENUECAT MONETIZATION", fill=(255, 255, 255), font=f["body_bold"])
    draw.text((x_l + 400, y_box + 38), "SDK v8.12.0 • Entitlement: clearpath_pro", fill=(165, 180, 252), font=f["sub"])

    # Freemium Tiers Cards
    draw.rounded_rectangle([(x_l + 25, y_box + 95), (x_l + w_box - 25, y_box + 235)], radius=16, fill=(11, 15, 25), outline=(31, 41, 55), width=1)
    draw.text((x_l + 50, y_box + 115), "FREE TIER: Daily Cognitive Allowance", fill=(56, 189, 248), font=f["h2"])
    draw.text((x_l + 50, y_box + 155), "• 5 Free Screen Simplifications per calendar day", fill=(203, 213, 225), font=f["body"])
    draw.text((x_l + 50, y_box + 185), "• Standard reading speed & priority extraction on all third-party Android apps", fill=(203, 213, 225), font=f["body"])

    draw.rounded_rectangle([(x_l + 25, y_box + 255), (x_l + w_box - 25, y_box + 445)], radius=16, fill=(30, 27, 75), outline=(99, 102, 241), width=2)
    draw.text((x_l + 50, y_box + 275), "PRO TIER: Unlimited Cognitive Clarity", fill=(255, 255, 255), font=f["h2"])
    draw.text((x_l + 50, y_box + 315), "• Monthly ($4.99/mo) | Annual ($39.99/yr — Save 33%)", fill=(251, 191, 36), font=f["body_bold"])
    draw.text((x_l + 50, y_box + 345), "• Unlimited screen simplifications across unlimited apps", fill=(226, 232, 240), font=f["body"])
    draw.text((x_l + 50, y_box + 375), "• Multi-Profile Family Sync (Senior Mode for parents, Student for kids)", fill=(226, 232, 240), font=f["body"])
    draw.text((x_l + 50, y_box + 405), "• Assisted Auto-Tap with double-confirmation safety beacon", fill=(226, 232, 240), font=f["body"])

    # Devpost Judge Instant Pass
    draw.rounded_rectangle([(x_l + 25, y_box + 465), (x_l + w_box - 25, y_box + 720)], radius=16, fill=(15, 23, 42), outline=(16, 185, 129), width=2)
    draw.text((x_l + 50, y_box + 490), "🔓 DEVPOST JUDGE INSTANT BYPASS", fill=(52, 211, 153), font=f["h2"])
    draw.text((x_l + 50, y_box + 530), "Hackathon jury can unlock full Pro privileges instantly without credit cards.", fill=(203, 213, 225), font=f["body"])
    
    draw.rounded_rectangle([(x_l + 50, y_box + 570), (x_l + 550, y_box + 630)], radius=10, fill=(31, 41, 55))
    draw.text((x_l + 75, y_box + 588), "PASS CODE: SHIPATON2026_JUDGE", fill=(56, 189, 248), font=f["body_bold"])

    draw.rounded_rectangle([(x_l + 50, y_box + 645), (x_l + 550, y_box + 695)], radius=10, fill=(6, 78, 59))
    draw.text((x_l + 75, y_box + 660), "✓ Verified: Full Entitlement Granted for Jury", fill=(110, 231, 183), font=f["body"])


    # Right: OneSignal Retention Journeys
    x_r = 990
    draw.rounded_rectangle([(x_r, y_box), (x_r + w_box, y_box + h_box)], radius=24, fill=(15, 23, 42), outline=(236, 72, 153), width=3)

    draw.rounded_rectangle([(x_r + 25, y_box + 25), (x_r + 370, y_box + 75)], radius=12, fill=(131, 24, 67))
    draw.text((x_r + 45, y_box + 38), "📡 ONESIGNAL USER JOURNEYS", fill=(255, 255, 255), font=f["body_bold"])
    draw.text((x_r + 390, y_box + 38), "SDK v5.1.28 • Context-Aware Retention", fill=(244, 114, 182), font=f["sub"])

    # Campaign 1: Form Friction Assistant
    draw.rounded_rectangle([(x_r + 25, y_box + 95), (x_r + w_box - 25, y_box + 395)], radius=16, fill=(11, 15, 25), outline=(31, 41, 55), width=1)
    draw.text((x_r + 50, y_box + 120), "CAMPAIGN 1: Form Friction Assistant", fill=(244, 114, 182), font=f["h2"])
    draw.text((x_r + 50, y_box + 160), "Trigger: User simplifies a multi-step form but leaves without completing.", fill=(203, 213, 225), font=f["body"])
    draw.text((x_r + 50, y_box + 190), "Action: OneSignal tags session with pending_form_app.", fill=(148, 163, 184), font=f["body"])
    
    # Notification Toast mockup
    draw.rounded_rectangle([(x_r + 50, y_box + 230), (x_r + w_box - 50, y_box + 355)], radius=14, fill=(30, 41, 59), outline=(236, 72, 153), width=2)
    draw.text((x_r + 75, y_box + 248), "🔔 OneSignal Push (Delivered after 2 hours)", fill=(244, 114, 182), font=f["body_bold"])
    draw.text((x_r + 75, y_box + 280), "\"Still working on that college portal form? Tap ClearPath to guide", fill=(255, 255, 255), font=f["body"])
    draw.text((x_r + 75, y_box + 308), "you through the next required field.\"", fill=(255, 255, 255), font=f["body"])

    # Campaign 2: Daily Cognitive Reset
    draw.rounded_rectangle([(x_r + 25, y_box + 415), (x_r + w_box - 25, y_box + 720)], radius=16, fill=(11, 15, 25), outline=(31, 41, 55), width=1)
    draw.text((x_r + 50, y_box + 440), "CAMPAIGN 2: Daily Cognitive Reset", fill=(52, 211, 153), font=f["h2"])
    draw.text((x_r + 50, y_box + 480), "Schedule: Delivered every morning at 9:00 AM local time.", fill=(203, 213, 225), font=f["body"])
    draw.text((x_r + 50, y_box + 510), "Purpose: Thoughtful re-engagement celebrating yesterday's focus.", fill=(148, 163, 184), font=f["body"])

    # Notification Toast mockup 2
    draw.rounded_rectangle([(x_r + 50, y_box + 550), (x_r + w_box - 50, y_box + 675)], radius=14, fill=(30, 41, 59), outline=(16, 185, 129), width=2)
    draw.text((x_r + 75, y_box + 568), "☀️ OneSignal Push (Daily 9:00 AM)", fill=(52, 211, 153), font=f["body_bold"])
    draw.text((x_r + 75, y_box + 600), "\"Daily Cognitive Reset: You completed 3 multi-step tasks yesterday.", fill=(255, 255, 255), font=f["body"])
    draw.text((x_r + 75, y_box + 628), "Tap to review today's clean priority queue.\"", fill=(255, 255, 255), font=f["body"])

    out_file = os.path.join(ASSETS_DIR, "infographic_3_monetization_retention.png")
    img.save(out_file, "PNG")
    print(f"[OK] Generated Infographic 3: {out_file}")


if __name__ == "__main__":
    create_infographic_1()
    create_infographic_2()
    create_infographic_3()
    print("All 3 Devpost submission infographics generated successfully.")
