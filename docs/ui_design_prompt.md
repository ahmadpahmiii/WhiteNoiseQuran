# 🎨 White Noise Quran — UI Design Prompt

> Ready-to-use prompts for AI design tools (Figma AI, v0.dev, Galileo AI, Midjourney, etc.)

---

## 🎯 Design Brief (Master Prompt)

```
Design a premium dark-themed Android mobile app called "White Noise Quran".
The app combines Quran audio recitation with ambient nature sounds (white noise,
rain, ocean, forest) for relaxation, focus, and sleep.

Design language:
- Deep dark Islamic aesthetic — feel of a quiet night sky
- Primary background: deep navy #0A0E1F
- Surface cards: #111827 and #1A2235
- Primary accent: gold/amber #C9A84C (for Islamic luxury feel)
- Secondary accent: teal #2DD4BF
- Text: near-white #F1F5F9 (primary), slate #94A3B8 (secondary)
- Font: Arabic names use system Arabic font (RTL), Latin uses a rounded
  modern sans-serif (e.g. Nunito or Inter)
- Corners: 20–28dp rounded, generous padding
- Style: Modern music player meets Islamic art — think Spotify dark mode
  meets a mosque at night. Subtle star/geometric Islamic patterns as
  texture overlays (very low opacity, 5–8%)
- Shadows: soft glow instead of hard drop shadows (gold glow on active
  elements, teal glow on interactive buttons)

App has 5 key screens:
1. Onboarding — Choose your reciter
2. Downloading — Progress screen
3. Main Player — Primary experience (Quran player + ambient mixer)
4. Surah List — Bottom sheet
5. Sleep Timer — Bottom sheet
```

---

## 📱 Screen 1 — Onboarding: Choose Reciter

```
Design an onboarding screen for a dark Islamic mobile app called
"White Noise Quran".

Screen purpose: User picks their preferred Quran reciter before the app
downloads audio files.

Layout:
- Top: App logo (crescent moon icon + "White Noise Quran" wordmark in gold)
- Headline: "اختر القارئ" (Arabic, centered) below subtitle "Choose Your Reciter"
  in smaller slate text
- Main content: vertical scrollable list of 6 reciter cards
- Bottom: "Continue" button (disabled until selection made) — gold filled,
  full width, rounded pill shape

Each reciter card:
- Dark card #1A2235 with 20dp radius
- Left: circular avatar placeholder with Arabic initial letter in gold
- Center: Reciter name in white bold, subtitle "Tap to select" in slate
- Right: radio circle (unselected = outlined, selected = gold filled checkmark)
- Selected state: card gets thin gold border + subtle gold inner glow
- Unselected: no border, neutral

6 reciters:
- Abdullah Al-Juhany
- Abdul Muhsin Al-Qasim
- Abdurrahman As-Sudais
- Ibrahim Al-Dossari
- Misyari Rasyid Al-Afasy (most popular — show "Popular" badge in teal)
- Yasser Al-Dosari

Background: deep navy #0A0E1F with very faint Islamic geometric star pattern
at 5% opacity as texture

Style: dark, premium, calm, Islamic aesthetic. No harsh colors.
Font: modern rounded sans-serif (Inter or Nunito).
```

---

## 📱 Screen 2 — Download Progress

```
Design a download progress screen for a dark Islamic mobile app.

Screen purpose: Show the user that 114 Quran surah audio files are being
downloaded for offline use. Should feel calm and not anxiety-inducing.

Layout:
- Top bar: back arrow + "Downloading Al-Afasy" title + subtitle
  "Preparing offline mode..."
- Large centered illustration: a crescent moon with a subtle download
  animation (gentle arc/orbit of small stars moving toward the center),
  or a peaceful mosque silhouette at night
- Progress section below illustration:
  - Reciter name and "61 / 114 Surahs" counter in gold
  - Linear progress bar: rounded, gradient from teal to gold, full width
  - Below bar: small label "~3 minutes remaining"
- Surah grid: 19 rows × 6 columns = 114 small surah tiles
  - Each tile: small rounded square showing surah number (1–114)
  - State colors: DONE = soft gold with checkmark, IN_PROGRESS = teal
    pulse animation, PENDING = dark grey, FAILED = red with retry icon
- Bottom: "Play Available Surahs" outlined button (teal text, teal border)
- Very bottom: "This only happens once per reciter" hint text in slate

Style: reassuring, calm progress. Subtle animated shimmer on pending tiles.
Colors: navy background, gold + teal accents.
```

---

## 📱 Screen 3 — Main Player (Primary Screen)

```
Design the main screen of a premium dark Islamic music player app called
"White Noise Quran". This is the most important screen.

The screen has TWO sections stacked vertically (scrollable):

━━━━━━━━━━━━━━━━━━━━━━
SECTION 1 — QURAN PLAYER (top ~60% of screen)
━━━━━━━━━━━━━━━━━━━━━━

Top bar (minimal):
- Left: crescent icon logo
- Center: "White Noise Quran" in gold
- Right: settings gear icon

Artwork area (large, centered):
- Large circular artwork: deep gradient (navy → dark purple)
- Elegant glowing crescent moon in gold at center
- Small star particles around the crescent
- Outer ring: thin animated rotating arc in gold while playing
- Soft radial gold glow behind the circle

Surah info (below artwork):
- Arabic text: "الفاتحة" — large, white, centered, RTL
- "Al-Fatihah · Surah 1 · 7 Verses" in slate
- Reciter pill: avatar initial + "Mishary Al-Afasy" + chevron

Seekbar:
- Custom gradient: dark → gold fill for played portion
- Gold circle thumb, time labels on both sides

Player controls:
- Large gold Play/Pause button (center) with soft gold glow shadow
- Previous (left) and Next (right) icons in white
- Bottom row: Shuffle (far left) + Sleep timer pill "45m" (far right)

━━━━━━━━━━━━━━━━━━━━━━
SECTION 2 — AMBIENT SOUND MIXER (scrollable below)
━━━━━━━━━━━━━━━━━━━━━━

Header: "🎵 Ambient Mix" in gold

2×2 grid of ambient sound cards (frosted glass style):
Each card:
- Background: #1A2235 at 85% opacity, thin border
- Large icon centered (🌬 🌧 🌊 🌿)
- Sound name below icon
- Vertical volume slider in center (teal-to-gold gradient track, gold thumb)
- Percentage label below slider
- ON/OFF toggle chip at bottom (teal=ON, grey=OFF)
- Active state: glowing teal/gold border
- Inactive: darker, greyed icon

4 sounds: White Noise, Rain, Ocean, Forest

Below grid: "✨ More sounds coming soon" in slate italic

Overall: premium Spotify + Islamic art fusion. Deep navy background,
subtle star texture at 5% opacity, soft glows, generous spacing.
```

---

## 📱 Screen 4 — Surah List Bottom Sheet

```
Design a bottom sheet modal for selecting a Quran surah in a dark Islamic app.

Sheet: slides up, covers ~85% of screen
- Background: #111827
- Top: drag handle pill
- Header: "Select Surah" + ✕ close
- Search bar: dark #1A2235 background, gold search icon

Surah list rows:
- Left: surah number in small gold rounded badge
- Center-left: Arabic surah name (RTL, white, bold)
- Center-right: Latin name in slate
- Far right: verse count "7 ayat" in slate
- Currently playing: gold left border highlight
- Subtle dividers between rows

114 items total.
```

---

## 📱 Screen 5 — Sleep Timer Bottom Sheet

```
Design a sleep timer bottom sheet for a dark calming mobile app.

Sheet: ~50% screen height, #111827 background

Header: 🌙 "Sleep Timer" + ✕

Quick chips: "15 min" "30 min" "45 min" "60 min" "90 min"
- Unselected: dark outlined slate
- Selected: gold filled

Custom input: minus circle + large number display + plus circle

If timer active: "Stops in 23:41" in teal with pulsing dot
+ "Cancel Timer" in rose

CTA: "Set Timer" gold filled pill, full width

Style: calm, minimal. Faint moon illustration as background decoration.
```

---

## 🎨 Design Tokens

```
Colors:
  Background:   #0A0E1F
  Surface:      #111827
  Card:         #1A2235
  Gold:         #C9A84C
  GoldLight:    #E8C97A
  Teal:         #2DD4BF
  White:        #F1F5F9
  Slate:        #94A3B8
  Error:        #F87171

Typography:
  Arabic:    system Arabic, 28–36sp, Bold
  Title:     Inter/Nunito, 20sp, SemiBold
  Body:      Inter/Nunito, 14sp, Regular
  Caption:   Inter/Nunito, 12sp, Regular

Spacing: base 8dp | Card padding: 16dp | Margins: 20dp
Radius: Cards 20dp | Buttons/Chips: pill (50dp) | Sheets: 28dp top
Glow:  Gold: rgba(201,168,76,0.3) | Teal: rgba(45,212,191,0.25)
```

---

## 🔧 Tool-Specific Tips

### For v0.dev:
Paste Screen 3 prompt + add: "Use Tailwind CSS dark mode, React, shadcn/ui for sliders."

### For Figma AI:
Paste any screen prompt. Add: "Mobile frame 390×844px, portrait orientation."

### For Midjourney / Image AI:
```
Dark Islamic mobile app UI, Quran music player, deep navy background,
gold crescent moon artwork, ambient sound mixer with frosted glass cards,
modern minimalist design, Dribbble style, high fidelity mockup,
8K UI design --ar 9:16 --style raw
```

### For ChatGPT / Claude:
Paste Master Prompt + one Screen prompt and ask:
"Generate a Jetpack Compose Kotlin implementation for this screen."
