# White Noise Quran — Product Research (SEA · sleep & calm · freemium)

**Date:** 2026-10-03 · **Prepared for:** product owner · **Scope:** desk research from primary
sources (store listings, papers, fatwa authorities, official statistics, Google policy pages,
equran.id docs). Store listings, prices, YouTube views and autocomplete were captured on
2026-10-03 (Google Play with `gl=ID`; App Store Indonesia `/id/` and Malaysia `/my/` storefronts).

**Owner's strategic answers (fixed):** market = Southeast Asia (Indonesia, Malaysia, Brunei) · core
use cases = sleep, calm/anxiety relief · monetization = freemium · scope = Quran listening + ambient
sound only (no prayer times, qibla, etc.) · standing rule: it's a Quran app, not a music app (no
shuffle, and the Quran is never treated as a playlist of songs).

**How to read this:** `[n]` = source number (list at the end). "secondary:" marks claims with no
primary source available. Nothing here is medical advice. Where Quran-listening studies are
described, the doc reports what was measured and how strong the evidence is.

---

## TL;DR

1. **The demand is real, it's in Bahasa, and people want no ads.** On YouTube, "Ayat Kursi 100X" has
   56M views, "Surah Al-Mulk (bikin tenang sebelum tidur)" 22M and "Murottal pengantar tidur bayi 10
   jam" 18M [1]. Google/YouTube autocomplete keeps adding **"tanpa iklan"** (no ads) to these
   queries [2]. The Play apps for "murottal pengantar tidur" are small (10K–100K installs), mostly
   carry ads and are badly translated [3]. The only polished direct competitor is **Quranify**:
   500K+ installs, 4.93★, a paid tier at Rp 269–399k per year, English-first [4][5].
2. **Keep all Quran recitation free and ad-free, and get permission for the audio.** The best-rated
   Quran apps are free with no ads (Quran for Android 4.78★, Greentech 4.90★) [6][7]. Tarteel keeps
   listening free and charges only for AI tools [8]. equran.id's terms allow app development but
   forbid **selling** data obtained through the API, and say audio (along with translations and
   tafsir) is copyright-protected [9]. Islamic Network, which equran.id lists as a partner, says
   recitations are licensed for free non-commercial redistribution and that reciters keep the right
   to ask for removal [10].
3. **"Play Ayat al-Kursi only" is feasible today.** equran.id API v2 has per-ayah audio for all 6
   current reciters [11][12]. Files for 2:255 and 2:285–286 return HTTP 200 for all 6 reciters, as
   do spot checks in Al-Mulk, As-Sajdah, Al-Kafirun and Al-Ikhlas [13]. A sunnah bedtime pack (Ayat
   al-Kursi, last 2 ayat of Al-Baqarah, Al-Mulk, As-Sajdah, Al-Kafirun, the 3 Quls) is about **20 MB
   per reciter**. The full Quran is about **1.66 GB per reciter** (Alafasy) [13].
4. **Scholars differ, so design for the cautious view.**
    - Playing Quran in the background while busy is permitted by Mufti WP [14], by Dar al-Ifta
      Egypt [15], and by NU's 1979 Muktamar, which held that listening to a recording is *mubah* (
      permissible) [16].
    - On falling asleep with Quran playing, views range from fine [17], to better to switch it off
      once sleep takes over [18], to don't [19].
    - On nature sounds behind recitation, IslamQA says **at least makruh** (disliked) and "should
      not be used" [20][21]. I found no Southeast Asian fatwa on this.
    - Mixing Quran with **music** is prohibited by all sources reviewed [22][23][20].
    - **Design answer:** "Quran first, then rain". The sleep timer ends the recitation at a natural
      stopping point, the ambient sound carries on and fades out, and a "Quran only" mode is one tap
      away.
5. **The noise science is weak, so make no health claims.**
    - Continuous noise as a sleep aid: the quality of evidence is very low (GRADE, 38 studies) [24].
    - A 2026 sleep-lab study (n=25) found that pink noise at 40–50 dBA **reduced REM sleep** [25].
    - Nature sounds lower stress modestly (meta-analysis effect size g = −0.60) [26].
    - Quran-listening studies consistently report less anxiety, but the evidence quality is low: 28
      studies, mostly Iranian, none reporting blinding. The two meta-analyses are unreviewed
      preprints [27][28][29].
    - Google Play bans misleading health claims and requires health apps that aren't medical devices
      to say they are "not a medical device" [30].
6. **Many Southeast Asian payment methods can't pay for subscriptions.**
    - Indonesia: convenience-store cash, bank virtual accounts and QRIS can't pay subscriptions.
    - Malaysia: FPX online banking can't, and Play gift cards and balance expired on 1 Feb
      2026 [31].
    - So sell a **one-time lifetime unlock** next to (or instead of) a subscription.
    - Price bands: Muslim Pro's yearly plans are Rp 55–129k; Quranify charges Rp 269–399k a year;
      Calm charges Rp 209–959k [5].
7. **Ramadan 1448 starts around Monday 8 Feb 2027** (Umm al-Qura via ICU; Muhammadiyah's KHGT
   calendar) [32][33]. Islamic apps spike at the start of Ramadan; Muslim Pro was #1 on both stores
   in Malaysia in 2019 [34]. Ship the localized listing and the bedtime routine **by mid-January
   2027**.
8. **Market size:** about 246M Muslims in Indonesia (87.1%) [35], 20.6M in Malaysia (63.5%) [36] and
   0.36M in Brunei (82.1%) [37]. Internet use in 2024 was 72.8% in Indonesia, 98.0% in Malaysia and
   96.3% in Brunei [38].

---

## 1. Competitors — sleep / white-noise apps

All rows: Google Play listing (installs, rating, store text) and App Store in-app purchase lists for
Indonesia and Malaysia, captured 2026-10-03. App Store purchase lists show several legacy price
points with vague labels, so prices are given as ranges.

| App                       | Positioning · key features                                                                                                                | Monetization (store text)                                   | IAP price points ID / MY (App Store)                                                                                | Play installs · rating                                                                                            | Complaints seen (sampled reviews)                                                                    |
|---------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------|
| **Calm** [39]             | 100+ Sleep Stories (celebrity narrators), meditations, soundscapes, Daily Streaks, Wear OS tiles                                          | No ads; some content "free forever"; optional subscription  | Rp 209k–959k · RM59.90–284.90 [5]                                                                                   | 50M+ · 4.48★ (616K)                                                                                               | Hard to navigate; crashes; pop-ups even when paying                                                  |
| **Headspace** [40]        | Meditation, sleep, coaching                                                                                                               | Free trial; US$12.99/mo, US$69.99/yr                        | Monthly Rp 29k–209k; yearly Rp 249k · RM12.90–57.90/mo; RM309.90–399.90/yr                                          | 10M+ · 4.01★ (340K)                                                                                               | —                                                                                                    |
| **BetterSleep** [41]      | Sound mixer, stories, sleep tracker, bedtime reminder, favorites, playlists                                                               | Auto-renewing subscriptions                                 | Rp 59k–1.999 juta · RM122.90–499.90                                                                                 | 10M+ · 4.73★ (397K)                                                                                               | Long-time users angry that free sounds and saved mixes became paid                                   |
| **Endel** [42]            | Adaptive generative soundscapes; offline; Wear OS                                                                                         | 1 month / 12 months / **Lifetime**                          | 1 mo Rp 15k–109k; 12 mo Rp 199k–699k · RM5.90–59.90 / RM89.90–299.90                                                | 1M+ · 4.55★ (22K)                                                                                                 | —                                                                                                    |
| **Noisli** [5]            | Focus/relax mixer (official app is iOS and web)                                                                                           | Paid iOS app, Rp 39k                                        | —                                                                                                                   | No official Noisli app found in a Play (ID) search; a "noisli" listing by another developer has 5K+ installs [43] | —                                                                                                    |
| **myNoise** [44]          | Real-recording soundscapes, "animate" mode, offline, "no AI"                                                                              | Free download; **one-time** unlock; no ads, no subscription | Full Access Rp 399k / RM99.90; All You Can Hear Rp 299k / RM69.90                                                   | 500K+ · 4.47★ (6.6K)                                                                                              | —                                                                                                    |
| **Rain Rain** [45]        | 50 free sounds + 60+ premium; mixer, favorites, **fade-out timer** and **bedtime reminder free forever**; premium sounds can be previewed | Subscription with 1-week trial; ads in free tier            | Premium Rp 55k–99k; yearly Rp 419k–599k; packs Rp 99k · RM17.90–29.90; RM119.90–179.90/yr; "One Sound Token" RM9.90 | 500K+ · 4.68★ (11K); last update Jul 2024                                                                         | —                                                                                                    |
| **White Noise Lite** [46] | Mixing with pitch and balance per sound, alarm with fade-in/out                                                                           | Ads (paid Pro version exists)                               | —                                                                                                                   | 5M+ · 4.48★ (68K)                                                                                                 | **Playback stops in the middle of the night**; battery drain; starts playing when Bluetooth connects |
| **Atmosphere** [47]       | Mix and save, import your own sounds, timer                                                                                               | Ads + **one-time** ad-free purchase                         | Rp 99k / RM22.90                                                                                                    | 1M+ · 4.94★ (77K)                                                                                                 | —                                                                                                    |
| **Tide** [48]             | Sleep/nap, focus timer, breathing, gentle wake-up alarm, single sounds sold separately                                                    | Subscriptions + individual sounds                           | Rp 25k–199k/mo; Rp 599k/yr; single sounds Rp 19k–39k · "Sound Pass" RM4.90–5.90/mo                                  | 1M+ · 4.80★ (24K)                                                                                                 | —                                                                                                    |
| **Loóna** [49]            | Bedtime "escapes" and games, stories, gentle alarm                                                                                        | Subscriptions incl. weekly plan                             | Rp 269k–599k/yr; Rp 129k/week; Rp 1.699 juta (likely lifetime) · RM84.90–164.90/yr                                  | 1M+ · 4.61★ (39K); last update Apr 2024                                                                           | —                                                                                                    |

**What seems to drive downloads and retention:**

- **Retention is low across the category.** Across 93 Android mental-health apps, median 30-day
  retention was **3.3%**; meditation apps 4.7%, breathing apps 0%. Use peaks in the evening and at
  night [50].
- **Narrative sleep content helped Calm scale:** 63M Sleep Story listens in about 18 months after
  the November 2016 launch [51]. Calm's listing now cites 180M downloads [39]. Sleep stories are *
  *out of scope** for us, but the lesson carries over: a bedtime *ritual* is the product.
- **The accepted freemium shape is "free core, paid extras."**
    - Rain Rain keeps the mixer, timer, reminder and favorites free forever and sells extra
      sounds [45].
    - myNoise and Atmosphere sell one-time unlocks [44][47].
    - Taking back free features earns 1★ reviews (BetterSleep) [41].
- **Common habit features:**
    - streaks (Calm) [39]
    - bedtime reminders (BetterSleep, Rain Rain)
    - saved mixes (BetterSleep, Atmosphere, Rain Rain)
    - fade-out timers (Rain Rain, White Noise)
    - gentle alarms (Loóna, Tide, White Noise)
    - wearable tiles (Calm)
- **Reliability is part of retention.** White Noise Lite's top complaint is sound cutting out
  overnight [46].

---

## 2. Competitors — Islamic, Quran and "Quran for sleep" apps, plus demand

### 2a. Large Islamic/Quran apps (Play, `gl=ID`)

| App                                                                         | Positioning                                                                                 | Monetization                                                                      | IAP price points ID / MY                                                           | Installs · rating       | Complaints / notes                                                                                                                                                                                                                                                                                    |
|-----------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|------------------------------------------------------------------------------------|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Muslim Pro** [52]                                                         | All-in-one (prayer, Quran, adhan)                                                           | Ads + subscriptions                                                               | Yearly Rp 55k–129k, monthly Rp 13k–25k · RM24.90–69.90/yr [5]                      | 100M+ · 4.33★ (1.92M)   | Ads; widgets behind the subscription; reading progress wiped by updates. **2020:** Motherboard reported that US Special Operations Command bought location data through the broker X-Mode, sourced from apps including Muslim Pro. Muslim Pro called this untrue and ended all data partnerships [53] |
| **Quran for Android** (quran.com) [6]                                       | Free reader, 15+ recitations, gapless audio, repeat                                         | Free (no ads or IAP shown)                                                        | —                                                                                  | 50M+ · 4.78★ (619K)     | —                                                                                                                                                                                                                                                                                                     |
| **Al Quran (Greentech)** [7]                                                | Tafsir, word-by-word, 80+ reciters, streak badges                                           | "Completely free & no ads"                                                        | —                                                                                  | 10M+ · 4.90★ (402K)     | —                                                                                                                                                                                                                                                                                                     |
| **Tarteel** [8]                                                             | AI memorization                                                                             | **Listening, follow-along and voice search free**; Premium AI features with trial | Rp 99k/mo, Rp 699k/yr (also Rp 349k/mo, Rp 3.999 juta/yr tiers) · RM29.90–99.90/mo | 10M+ · 4.66★ (121K)     | Reliability after updates                                                                                                                                                                                                                                                                             |
| **Pillars** [54]                                                            | Prayer app; "no ads… period"; privacy-first; widget; streak that pauses during menstruation | Premium subscription                                                              | Rp 249k–299k/yr; Rp 58k/mo; family Rp 435k–520k · RM99.90–119.90/yr                | 500K+ · 4.46★ (4.4K)    | —                                                                                                                                                                                                                                                                                                     |
| **umma** [55]                                                               | All-in-one                                                                                  | Ads + IAP                                                                         | —                                                                                  | 10M+ · **2.93★** (178K) | Full-screen ads on launch; ads interrupting recitation                                                                                                                                                                                                                                                |
| **Qur'an Kemenag** (Lajnah Pentashihan, Ministry of Religious Affairs) [56] | Official Indonesian Standard Mushaf, translation, tafsir, selected murattal                 | Free                                                                              | —                                                                                  | 500K+ · 4.75★ (8.4K)    | Bookmarks lost; location-bound features                                                                                                                                                                                                                                                               |
| **Wemu (WeMuslim)** [57]                                                    | All-in-one                                                                                  | Ads + IAP                                                                         | —                                                                                  | 100M+ · 4.65★ (751K)    | —                                                                                                                                                                                                                                                                                                     |

### 2b. The "Quran for sleep / calm" niche

| App                                                | What it does                                                                                                                                                                     | Installs · rating                  | Notes                                                                                                                                                                                                              |
|----------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Quranify** [4]                                   | "Quran for study, sleep & deep focus"; ambient "focus modes" and soundscapes; sleep playlists; ayah-by-ayah; Middle-Eastern reciters (Sudais, Maher, Alafasy, Yasser Al-Dosari…) | 500K+ · **4.93★ (83K)**; iOS 4.93★ | Rp 269k–399k/yr, Rp 99k/mo · RM54.90–79.90/yr [5]. Reviews praise no ads. One reviewer says others rated it down over the **background sound**, and argues it is natural sound, not music, and can be switched off |
| Al Quran Merdu Pengantar Tidur (Salma Kreatif) [3] | Offline murottal + duas                                                                                                                                                          | 50K+ · 4.54★ (192)                 | Ads; English listing calls recitation "music"/"songs"; ringtone feature                                                                                                                                            |
| Quran for sleeping & relaxing (Mohanbakriapp) [3]  | Quran + sound mixes, timer, bedtime reminder                                                                                                                                     | 50K+                               | Ads; claims help with "depression" (policy risk)                                                                                                                                                                   |
| Murottal Al-Quran 30 Juz Merdu (Kareema) [3]       | Full 30-juz murottal                                                                                                                                                             | 100K+ · 4.34★                      | —                                                                                                                                                                                                                  |
| Al Quran Pengantar Tidur Merdu (Wenake) [3]        | Murottal + duas                                                                                                                                                                  | 10K+ · 4.69★                       | Ads; has a **shuffle** button (the "playlist" pattern we avoid)                                                                                                                                                    |
| Quran Rain Thunder Sounds (Huma) [3]               | Recitation with rain/thunder background                                                                                                                                          | 10K+ · 4.49★                       | Ads; ringtone/alarm use                                                                                                                                                                                            |
| Ayat Kursi 10 Hour Nonstop (Kopi Mantan) [3]       | Ayat al-Kursi looped 10 h                                                                                                                                                        | 10K+ · 4.41★                       | Ads                                                                                                                                                                                                                |
| Quran White Noise – Baby Sleep (Anafiya) [3]       | Quran + white noise for babies; 9 reciters, 9 sounds, timer                                                                                                                      | 5K+                                | Same core concept as ours, aimed at infants                                                                                                                                                                        |
| Sakinah / Sakeenah / Sakeena [58]                  | "A verse for a feeling"; privacy-first; explicitly **no streaks**                                                                                                                | 50+ to 1K+                         | Tiny so far                                                                                                                                                                                                        |

**Takeaways:** the niche is fragmented. There is no ad-free, Bahasa-first, well-made "Quran + rain"
sleep app. Quranify shows people will pay for the polished English-language version. The recurring
complaints across the category are ads (worst in umma), paywalled features (Muslim Pro), privacy (
Muslim Pro 2020), and lost data or reliability problems (Muslim Pro, Kemenag, White
Noise) [52][55][56][46][53].

### 2c. YouTube demand signals (views on 2026-10-03; snapshot, includes re-uploads) [1]

| Video (channel)                                                    | Views | Length · age  |
|--------------------------------------------------------------------|-------|---------------|
| Ayat Kursi 100X FULL… (Neurotic Studio)                            | 56.1M | 2:28 h · 4 y  |
| SURAH AL MULK (Bikin tenang sebelum tidur) (Furqan fawwaz)         | 22.0M | 1:05 h · 5 y  |
| Murottal Pengantar Tidur Bayi… FULL 10 JAM (Qiroah Daily)          | 18.3M | 10 h · 5 y    |
| MUROTTAL MERDU PENGANTAR TIDUR… Alaa Aqel (Leli Quran)             | 8.5M  | 3:17 h · 1 y  |
| Quran with rain & thunder sounds — Surah Yunus (Sahl Quran Studio) | 6.5M  | 2:22 h · 4 y  |
| 10 jam ayat kursi merdu pemandangan sawah (Nafas Doa)              | 5.5M  | 10:38 h · 3 y |
| Quran + Rain 10 hours… (Sahl Quran Studio)                         | 4.2M  | 10:22 h · 4 y |
| "Lofi Quran"… with Rain/Wind (Lofi Quran)                          | 1.0M  | 53 min · 2 y  |
| *Ambient only:* SUARA MENENANGKAN KIPAS ANGIN (electric fan)       | 51.1M | 11 h · 13 y   |

What these show: long-form (2–10 h) **Ayat al-Kursi, Al-Mulk and baby-sleep murottal** are the
biggest formats. "Quran + rain" is a solid secondary format. A local *fan noise* video has 51M
views.

---

## 3. Science of noise for sleep, calm and focus

| Question                                          | Best evidence                                                                                                                                                                                                                                                                                                                                      | Strength             | Product implication                                                    |
|---------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------|------------------------------------------------------------------------|
| Does continuous white/broadband noise help sleep? | Systematic review of 38 articles: results range from improving to disrupting sleep; **GRADE very low**; may also harm sleep and hearing [24]                                                                                                                                                                                                       | Very low             | No "sleep better" claims; keep volume modest                           |
| All-night pink noise?                             | Lab study, n=25, 7 nights polysomnography: pink noise at 40/50 dBA **reduced REM**; adding it on top of traffic noise worsened sleep structure; earplugs worked better [25]. Pilot n=12: continuous 45 dB pink noise reduced traffic-noise fragmentation [59]. Older study (n=40 + 10 nap): steady pink noise increased "stable sleep" on ECG [60] | Small, mixed         | **Default to fading out after sleep onset**; offer all-night as opt-in |
| Pink-noise "deep sleep boost"?                    | Works only as **short pulses timed to EEG slow waves** (closed-loop), not as a background loop [61][62]. Meta-analysis on memory: 10 studies, 177 people, g=0.25 (p=0.07) [63]. A 2022 meta-analysis was **retracted in 2026** [64]                                                                                                                | Lab-only             | Don't market a phone loop as "deep sleep"                              |
| Nature sounds and stress                          | Systematic review + meta-analysis (36 publications; 18 pooled): stress/annoyance **g = −0.60**, health/positive affect g = 1.63 (wide CI) [26]. Natural vs artificial sounds raised parasympathetic (heart-rate variability) markers [65]. n=40: faster stress recovery with nature sounds [66]                                                    | Moderate, short-term | Nature beds (rain, forest, waves) are the right default palette        |
| Focus                                             | Meta-analysis in ADHD/attention problems (k=13, N=335): white/pink noise **g = 0.249**; no brown-noise studies [67]                                                                                                                                                                                                                                | Small effect         | Not core to sleep, but a valid "calm/focus" secondary use              |

**Safety:**

- **WHO–ITU safe listening standard (2019):** reference doses of **80 dB for 40 h/week (adults)**
  and **75 dB for 40 h/week (children)**. It recommends dosimetry, volume limiting and parental
  volume control [68].
- **Infant sleep machines:** of 14 machines tested, all exceeded 50 dBA (the hospital-nursery limit)
  at 30 cm, and 3 exceeded 85 dBA at maximum volume [69]. Authors' advice (secondary): place the
  device far from the infant, at low volume, for a short time [70].
- **AAP 2023 noise policy:** pediatricians should counsel parents on safe use of sleep/noise
  machines [71].
- **Relevance to us:** "murottal bayi" (baby) searches are big (§7), so any baby use case needs a
  volume cap and safety copy.

---

## 4. Quran listening — (a) research, (b) scholarly views

### 4a. Research on listening to recitation (what was measured, and evidence quality)

| Review                                                                   | What it covers                                                                                                                                                   | Quality notes                                                                                                                                                                                             |
|--------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Ghiasi & Keramat 2018, systematic review [27]                            | 28 trials/quasi-trials, 2,108 participants (24–180 each), mostly Iran; anxiety (STAI in 22) before procedures, exams, ICU, dialysis                              | Cochrane risk of bias: 8 trials described randomization adequately, 3 had low-risk allocation concealment, **none reported blinding**; no meta-analysis; "low methodological quality, high heterogeneity" |
| Che Wan Mohd Rozali et al. 2022, systematic review (Malaysian team) [72] | 20 studies (7 RCT, 5 quasi-experimental, 8 observational), 2,566 participants; depression, anxiety, **sleep (PSQI, older adults)**, vital signs, quality of life | Randomization and blinding poorly reported; no meta-analysis; authors call the field "understudied"                                                                                                       |
| Majidi & Rajabi-Tavakkol 2025, EEG review [73]                           | 22 EEG studies: more alpha/theta power while listening, including non-Muslims                                                                                    | Many studies at high risk of bias; varied designs                                                                                                                                                         |
| Gavgani et al. 2020 (**preprint**) [28]                                  | Pre-operative anxiety: 12 RCTs, 9 pooled; pooled mean difference ≈ −8.9 anxiety-scale points; I²=65%                                                             | Not peer-reviewed                                                                                                                                                                                         |
| Abd-alrazaq et al. 2020 (**preprint**) [29]                              | Mental health: 11 studies, 7 pooled; improvements in anxiety, depression, stress                                                                                 | Authors rate evidence **very low to low**                                                                                                                                                                 |

**Honest summary:** results consistently point toward less anxiety and more relaxation. But the
studies are small, unblinded (blinding is close to impossible here), usually compared with no
intervention, run among believers (so expectations play a part), and come mostly from Iran.
Sleep-specific trials are scarce in MEDLINE: a Europe PMC title search found one relevant item [74].
**Use this for motivation, never for claims.**

### 4b. Scholarly views (range, not a ruling)

Key verse: **Al-A'raf 7:204**, which tells believers to listen attentively and keep silent when the
Qur'an is recited [75]. Mufti WP notes that classical commentators (al-Qurtubi; Ibn Kathir citing
Ibn 'Abbas) tie it mainly to recitation in prayer [14].

| Issue                                             | More permissive                                                                                                                                                                                                                                                                                                                                                                                                                                           | More cautious                                                                                                                                                                                                                                                                                                                                  | Sources              |
|---------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------|
| Quran playing in the background while busy        | Mufti WP (Al-Kafi #1709, 2020): permitted; being busy with work (market, cooking) is a valid excuse; keep surroundings calm, avoid loud talk, return to listening afterward. Dar al-Ifta Egypt (fatwa #3517, 2004, Ali Gomaa): no objection if you're not deliberately turning away. **NU 26th Muktamar (Semarang, 5–11 Jun 1979):** a recording is treated like sound from an inanimate object, not "Qur'an" in the legal sense, so listening is *mubah* | Al-Kafi #1709 also cites Wahbah al-Zuhaili (not listening at a recitation gathering is strongly disliked) and the Kuwaiti fiqh encyclopedia (listening is obligatory absent an excuse). Muhammadiyah Tarjih: loudspeaker recitation should avoid sleep and rest times                                                                          | [14][15][16][76][77] |
| Falling asleep while Quran plays                  | IslamQA #50010: listening before sleep is fine and brings tranquility (citing 13:28). Mufti WP #1708: **reciting while lying down** is permissible                                                                                                                                                                                                                                                                                                        | SeekersGuidance: fine while you're attentive; **once sleep takes over, better to switch it off**. Darul Ifta Birmingham: you can't attend while asleep, so don't play it                                                                                                                                                                       | [17][78][18][19]     |
| Listening vs reciting the bedtime surahs          | Mufti WP #1232: a listener is rewarded when listening with adab                                                                                                                                                                                                                                                                                                                                                                                           | IslamQA #228366: the specific virtue of Al-Mulk needs **reciting**; those who can't read can **repeat after a reciter**                                                                                                                                                                                                                        | [79][80]             |
| Nature sounds / white noise mixed with recitation | No SEA fatwa found. A Quranify reviewer argues the background is natural sound, not music [4]. Real rain or thunder during recitation is unobjectionable (Islamweb #142023)                                                                                                                                                                                                                                                                               | IslamQA (Arabic) #145931, 2010: adding wind, thunder or bird sounds is **at least makruh**; the Quran should be kept free of accompanying sounds. IslamQA #229732, 2025: natural sounds are fine in general but **should not be a background to recitation**. Islamic Network's terms ask that the text not be mixed with non-Quranic material | [20][21][81][10]     |
| Music / instruments with Quran                    | — (no permissive source found)                                                                                                                                                                                                                                                                                                                                                                                                                            | Dar al-Ifta Egypt 2014: prohibited. NU Online 2024: "Quranic songs" with music are *haram* (disrespect). IslamQA: sounds that imitate instruments or beats take the same ruling. MUI (Oct 2024) demanded an apology after Al-Fatihah was recited over a band's music (secondary)                                                               | [22][23][21][82]     |
| Cutting recitation off mid-verse                  | Mufti WP Irsyad #59 (2015), citing Malaysia's 77th National Fatwa Muzakarah (2007): Quran ringtones are allowed with adab and not in places like toilets                                                                                                                                                                                                                                                                                                  | **Cutting a verse off abruptly** (and so changing its meaning) should be avoided                                                                                                                                                                                                                                                               | [83]                 |

---

## 5. Islamic sleep sunnah, and whether equran.id can serve it

Hadith links use sunnah.com numbering. sunnah.com blocked automated access, so the numbers and
wording were cross-checked against the open hadith-api dataset [84]. All descriptions are
paraphrased.

| Practice                                                                  | Source                                                                                                                                                                                                     | In-app use                                        | Audio on equran.id                         |
|---------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|--------------------------------------------|
| Ayat al-Kursi (2:255) at bedtime: protection until morning                | [Bukhari 2311](https://sunnah.com/bukhari:2311)                                                                                                                                                            | "Ayat al-Kursi" button; repeat ×1/×3              | Per-ayah `002255.mp3`, all 6 reciters ✔    |
| Last two ayat of Al-Baqarah (2:285–286) at night "suffice"                | [Bukhari 5009](https://sunnah.com/bukhari:5009), [Muslim 807](https://sunnah.com/muslim:807)                                                                                                               | Routine step                                      | Per-ayah `002285/002286`, all 6 reciters ✔ |
| Al-Ikhlas, Al-Falaq, An-Nas: blow into hands, wipe body, ×3               | [Bukhari 5017](https://sunnah.com/bukhari:5017)                                                                                                                                                            | Step with a "×3" counter                          | Full surahs 112–114 ✔                      |
| Al-Mulk intercedes; the Prophet ﷺ didn't sleep before As-Sajdah + Al-Mulk | [Tirmidhi 2891](https://sunnah.com/tirmidhi:2891) (hasan); [Tirmidhi 2892](https://sunnah.com/tirmidhi:2892) (grading varies: weak in the Darussalam edition, authenticated by al-Albani per IslamQA [80]) | "Al-Mulk tonight"                                 | Full surahs 67, 32 ✔ (≈7.3 MB for Al-Mulk) |
| Al-Kafirun before sleeping                                                | [Abu Dawud 5055](https://sunnah.com/abudawud:5055), [Tirmidhi 3403](https://sunnah.com/tirmidhi:3403)                                                                                                      | Optional step                                     | Surah 109 ✔                                |
| Al-Isra and Az-Zumar nightly                                              | [Tirmidhi 2920](https://sunnah.com/tirmidhi:2920)                                                                                                                                                          | Optional long option                              | Surahs 17, 39 ✔ (~60 MB)                   |
| Wudu, lie on the right side, bedtime dua                                  | [Bukhari 247](https://sunnah.com/bukhari:247), [Muslim 2710](https://sunnah.com/muslim:2710)                                                                                                               | Checklist text (not audio)                        | —                                          |
| "Bismika Allahumma amutu wa ahya"                                         | [Bukhari 6312](https://sunnah.com/bukhari:6312)                                                                                                                                                            | Final screen before the screen dims               | —                                          |
| Dust off the bed, then dua                                                | [Bukhari 6320](https://sunnah.com/bukhari:6320), [Muslim 2714](https://sunnah.com/muslim:2714)                                                                                                             | Checklist                                         | —                                          |
| Tasbih 33 / tahmid 33 / takbir 33–34 (narrations vary)                    | [Bukhari 6318](https://sunnah.com/bukhari:6318), [Muslim 2727](https://sunnah.com/muslim:2727)                                                                                                             | Silent tap counter (later)                        | —                                          |
| Small but consistent deeds are most beloved                               | [Bukhari 6464](https://sunnah.com/bukhari:6464)                                                                                                                                                            | Basis for gentle consistency, not streak pressure | —                                          |

**equran.id API v2 facts** [11][12][13]:

- `GET /api/v2/surat/{n}` returns `audioFull` and, for every `ayat[i]`, an `audio` map. Both use
  keys `"01"`–`"06"`: Abdullah-Al-Juhany, Abdul-Muhsin-Al-Qasim, Abdurrahman-as-Sudais,
  Ibrahim-Al-Dossari, Misyari-Rasyid-Al-Afasi, Yasser-Al-Dosari.
- Per-ayah file pattern: `https://cdn.equran.id/audio-partial/{slug}/{SSS}{AAA}.mp3` (e.g.
  `002255`). Full-surah pattern: `…/audio-full/{slug}/{SSS}.mp3`.
- Sizes (Alafasy): the full Quran is **1,658 MB** across 114 files; Al-Baqarah alone is 60 MB. A
  bedtime core (67, 32, 109, 112–114) is 18.3 MB, plus 2.2 MB for 2:255 and 2:285–286.

**Terms and licensing** (important for freemium):

- **equran.id ToS v1.0, effective 15 Jun 2025** [9]:
    - allows personal and educational use, sharing *with attribution*, and **using the API to build
      apps**;
    - says the API is free for "reasonable use" and is **rate-limited**; bots and excessive
      automated scripts are banned;
    - says data obtained **"tidak boleh dijual atau diperjualbelikan"** (may not be sold or traded);
    - states that translations, tafsir, **audio** and app features are copyright-protected, while
      the Quran text itself is free to use.
- equran.id credits Kemenag as its data source and lists Islamic Network and EveryAyah ("Verse By
  Verse Quran") as partners [11][9].
- **Islamic Network terms (updated 14 Jun 2026)** [10]:
    - recitations are licensed to them for **free, non-commercial redistribution**;
    - bundling them in a commercial product is tolerated, but **reciters keep copyright and may
      request removal**.
- **Implication:** charging for access to recitation sourced this way is the riskiest possible
  model. Charge for our own features and sounds, attribute equran.id, and **ask equran.id for
  written permission** before launch.

---

## 6. SEA market

| Metric                               | Indonesia                                                                                                                                                    | Malaysia                                                                                      | Brunei                                     |
|--------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|--------------------------------------------|
| Muslims                              | 245.97M = 87.08% (Dukcapil, H1 2024; secondary via Databoks) [35]                                                                                            | 20.6M = 63.5% of 32.4M (Census 2020) [36]                                                     | 362,035 = 82.1% of 440,715 (BPP 2021) [37] |
| Internet use, % of population (2024) | 72.78% (BPS Susenas) [85], same in World Bank data [38]; 68.65% own a mobile phone (BPS figure, **unverified**: page blocked, seen only in a search snippet) | 98.0% (World Bank) [38]; households: 96.8% have internet, 97.9% a smartphone (DOSM 2024) [86] | 96.3% (World Bank) [38]                    |
| Secondary cross-check                | 230M internet users, 80.5% at end-2025 (DataReportal; different method) [87]                                                                                 | —                                                                                             | —                                          |

**Reciters popular in SEA** (YouTube subscribers, 2026-10-03) [88]: Muzammil Hasballah **4.11M**,
Salim Bahanan 747K, Taqy Malik 661K (all Indonesian). Also widely used: Mishary Alafasy 12.2M (
already in the app) and Alaa Aqel 422K (frequent in Indonesian "pengantar tidur" compilations [1]).
Hanan Attaki (3.03M) is a preacher, not a reciter, but appears in autocomplete [2]. **Licensing:** I
found no public license for any of them. YouTube "Topic" channels suggest commercial distribution,
so assume all rights reserved and a direct deal is needed (**unverified**).

**Google Play payment methods** [31]:

- **Indonesia:**
    - cards: JCB, Mastercard, Visa;
    - carrier billing: Indosat, Smartfren, Telkomsel, XL/AXIS, Tri ("some carriers can't be used to
      pay for subscriptions");
    - e-wallets: DANA, DOKU, GoPay, OVO, ShopeePay;
    - cash at Alfamart, Alfamidi, Dan+Dan, Indomaret, Lawson; virtual accounts at BCA, BRI, BSI,
      Mandiri, Permata; QRIS — **cash, virtual accounts and QRIS can't pay subscriptions**.
- **Malaysia:**
    - cards: Amex, Mastercard, Visa;
    - carrier billing: CelcomDigi, Maxis, TuneTalk, U Mobile, Unifi Mobile;
    - e-wallets: Boost, Touch 'n Go, ShopeePay;
    - **FPX online banking (16 banks) can't pay subscriptions**;
    - Play gift cards, prepaid balance and cash top-up **ended 1 Feb 2026**.
- **Brunei:** no country-specific section (**unverified**).

**Price points to anchor on (App Store ID/MY)** [5]:

- Islamic all-in-ones are cheap: Muslim Pro Rp 55–129k/yr (RM24.90–69.90).
- Mid tier: Pillars Rp 249–299k/yr; Quranify Rp 269–399k/yr.
- Western sleep apps: Rp 249k–959k/yr.
- One-time unlocks: Atmosphere Rp 99k, myNoise Rp 299–399k.

**Ramadan 1448 AH:**

- **1 Ramadan = Monday 8 Feb 2027** and 1 Syawal = Tuesday 9 Mar 2027, computed with the ICU Umm
  al-Qura calendar [32].
- Muhammadiyah's KHGT gives the same dates (Nuzulul Qur'an 17 Ramadan = 24 Feb 2027) [33].
- The Indonesian government sighting session (*isbat*) and Malaysia's moon-sighting (*rukyah*) may
  land a day later.
- Ramadan 1449 ≈ 28 Jan 2028 (ICU).
- **Seasonality evidence:** Muslim Pro was the most-downloaded app on both stores in Malaysia at the
  start of Ramadan 2019 (secondary: NST citing Sensor Tower) [34]. A widely repeated "+120%
  Quran-app downloads in Ramadan (data.ai)" figure could **not** be traced to a primary source.

---

## 7. Retention, growth, ASO and policy

| Tactic                         | Evidence                                                                                                                                   | Fit for WNQ                                                          | Effort |
|--------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------|--------|
| Sleep timer that **fades out** | Rain Rain and White Noise ship it [45][46]; supports the REM caution [25]                                                                  | Core                                                                 | S      |
| Saved mixes / presets          | BetterSleep favorites, Atmosphere "mix & save", Rain Rain favorites [41][47][45]                                                           | Ambient presets only (never a Quran "playlist")                      | S      |
| Bedtime reminder               | BetterSleep and Rain Rain [41][45]; push prompts raised next-24 h engagement by only **~3.9%** in a 1,255-user micro-randomized trial [89] | Opt-in, user-set time, gentle wording                                | S      |
| Home-screen widget             | Gratitude: **25% higher retention** among widget users (company case study, correlational) [90]                                            | "Tonight: Ayat al-Kursi · Al-Mulk" widget                            | M      |
| Streaks                        | Calm, Greentech, Tarteel use them; Pillars pauses during menstruation; Sakinah refuses them [39][7][8][54][58]                             | Private "nights kept" counter, no loss-aversion (Bukhari 6464 ethic) | S      |
| Gentle wake alarm              | Loóna, Tide, White Noise [49][48][46]                                                                                                      | Later; user-set time only (no prayer-time engine)                    | M      |
| Sleep stories                  | Calm's growth driver [51]                                                                                                                  | **Out of scope** (owner)                                             | —      |
| Localized listing (ID/MS)      | Google: localization and listing text drive discovery [91]; all demand evidence is in Bahasa [2][1]                                        | High                                                                 | S      |
| Shareable content / referral   | No strong evidence found                                                                                                                   | Low priority (e.g., share an ayah card)                              | S      |

**ASO keyword ideas (Bahasa Indonesia / Melayu)**, evidenced by autocomplete [2] and YouTube
views [1]:

| Cluster               | Queries seen                                                                                                                                                                          | Evidence                                                         |
|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------|
| Murottal for sleep    | murottal pengantar tidur · quran pengantar tidur · quran untuk tidur · quran tidur nyenyak · quran susah tidur                                                                        | Autocomplete ID; 18.3M-view video                                |
| Specific surah / ayat | ayat kursi pengantar tidur · ayat kursi sebelum tidur · 3 qul dan ayat kursi sebelum tidur (MY) · surah al mulk sebelum tidur · murottal al mulk                                      | 56M and 22M-view videos                                          |
| Sunnah routine        | bacaan sebelum tidur sesuai sunnah · bacaan sebelum tidur menurut rasulullah · bacaan sebelum tidur al mulk · zikir sebelum tidur (MY)                                                | Autocomplete ID/MY                                               |
| Ambient               | suara hujan pengantar tidur · suara hujan untuk tidur · suara hujan **di atap seng** · suara hujan tanpa petir · bunyi hujan untuk tidur (MY) · **suara kipas angin** pengantar tidur | Autocomplete; 51M-view fan video                                 |
| Baby / parents        | murottal pengantar tidur bayi · murottal bayi rewel · white noise bayi · quran for sleep baby                                                                                         | Autocomplete; triggers Families-policy and safety questions [92] |
| No ads                | "… **tanpa iklan**" attached to murottal, ayat kursi, zikir, quran pengantar tidur; "quran for sleep no ads"                                                                          | Autocomplete ID/MY/EN                                            |

**Google Play policy guardrails:**

- **Health claims:** no misleading claims that contradict medical consensus. Non-device health apps
  must state they are "not a medical device" and don't diagnose or treat. **All** developers must
  fill in the Health apps declaration [30]. Sleep trackers and stress-management apps are listed as
  health and fitness examples [93].
- **Metadata:** title ≤ 30 characters. No "#1", "Best", "Top", "Free" or price/promo text in the
  title, icon or developer name. No emoji or ALL CAPS. No keyword stuffing or anonymous
  testimonials [94]. Practical result: put "tanpa iklan" in the short description, not the title.
- **Families:** applies if children are among the target audiences, so don't target babies or kids
  in Play Console unless you're ready to comply [92].
- **Background audio:** Android 14+ requires the `mediaPlayback` foreground-service type and
  permission [95]. Overnight reliability is a known pain point [46].

---

## Implications for White Noise Quran

### Free vs Premium (recommended split)

| Free forever                                                                                 | Premium (one-time **Lifetime** + optional yearly)                                                                                                                      | Rationale                                                                                                      |
|----------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| **All recitation**: 6 reciters × 114 surahs, per-ayah bedtime set, downloads                 | —                                                                                                                                                                      | Religious expectation (top Quran apps free [6][7]); equran.id no-sale clause [9]; Islamic Network license [10] |
| Current 6 ambient loops, mixer, sleep timer with fade, lock-screen controls, Quran-only mode | **New original soundscapes** recorded or licensed by us, SEA-specific: rain on a tin roof (*atap seng*), electric fan (*kipas*), night crickets, rain on banana leaves | Pays for content that is ours; mirrors Rain Rain and Tide [45][48]; demand evidence [2][1]                     |
| Bedtime sunnah routine, bedtime reminder, basic widget                                       | Unlimited saved mixes, advanced fade curves, gentle wake alarm, extra widget styles, themes                                                                            | Convenience upsell; never take free features back (BetterSleep backlash [41])                                  |
| **No ads, ever**; no tracking                                                                | "Support the app" framing                                                                                                                                              | "tanpa iklan" demand [2]; umma 2.93★ [55]; privacy (Muslim Pro 2020 [53], Pillars [54])                        |

Pricing (judgment, to validate):

- **Lifetime ≈ Rp 149–249k / RM29.90–49.90** as a one-time product, which works with QRIS, cash,
  virtual accounts and FPX [31].
- **Yearly ≈ Rp 99–149k / RM19.90–29.90**, between Muslim Pro and Quranify [5].
- No weekly plans.

### Product rules for the scholarly concerns (bake these into design)

1. **Quran first, then rain:**
    - the sleep timer ends recitation **at the end of the current ayah** (bedtime routine, per-ayah
      files) or **the current surah** (full-surah files), never mid-verse [83];
    - the ambient sound then carries on and fades out;
    - this respects "switch it off when sleep takes over" [18] and the REM caution [25].
2. **"Quran only" is one tap away and remembered.** The ambient sound ducks under the recitation and
   is never louder than it. Consider a default bedtime mode where ambient plays only *after* the
   recitation ends, for users who follow the cautious view [20][21].
3. **No music-like sounds:**
    - no instruments, pads, drones or beats, and never "lofi Quran";
    - **have a scholar review the current "night train" loop** (rhythmic clatter) against the "
      imitates beats" reasoning [21];
    - never ship pre-mixed Quran+sound files.
4. **Wording:** murottal / tilawah, never "music", "lagu" or "songs" (unlike some competitor
   listings [3]). No shuffle, ever.
5. **Encourage reciting, not just listening:** add a "repeat after the reciter" pause mode in the
   routine [80][79].
6. **An "Adab" sheet** that briefly presents the range of views (§4b) and suggests asking a local
   ustadz. Have the copy reviewed by a scholar (see open questions).

### Prioritized recommendations

**Now — before launch (target mid-Jan 2027, ahead of Ramadan on 8 Feb [32])**

1. **Email equran.id for written permission** (commercial freemium app, offline caching, bulk
   downloads). Add attribution (EQuran.id; data from Kemenag) and throttle downloads with backoff.
   *Why:* no-sale clause, rate limits, audio copyright [9]. **S**
2. **Keep recitation free with no ads, and say so in the listing.** *Why:* §2 and the autocomplete
   evidence. **S**
3. **"Quran first, then rain" timer** (surah-boundary stop, then ambient fade-out; not all-night by
   default). *Why:* rules 1–2 [83][25][24]. **S**
4. **Bedtime Sunnah routine with per-ayah audio:** Ayat al-Kursi, 2:285–286, the 3 Quls,
   Al-Mulk/As-Sajdah, plus a repeat-after-reciter mode. Offer a **~20 MB "bedtime pack"** first,
   instead of pushing the 1.66 GB bulk download. *Why:* §5 [13]. **M**
5. **Audio-respect rules** (Quran-only toggle, ducking, sound-set review, wording). *Why:* §4b. **S
   **
6. **Bahasa Indonesia and Melayu listings** with the keyword clusters above, then translate the UI.
   *Why:* every demand signal is in Bahasa [2][91]. **S** (listing) / **M** (UI)
7. **Compliance copy:**
    - no "cures insomnia/anxiety" or "terapi" claims; the disclaimer if any wellness wording is
      used;
    - fill in the Health apps declaration;
    - a Data safety form saying "no data collected" (true today);
    - title ≤ 30 characters with no promo words.
      *Why:* [30][94]. **S**
8. **Overnight reliability pass:** `mediaPlayback` foreground service, battery-optimization prompts,
   8-hour soak tests on Samsung, Oppo, Xiaomi and Vivo. *Why:* [95][46]. **S–M**

**Next — Ramadan 1448 window (Jan–Mar 2027)**

9. **Play Billing: lifetime one-time + yearly**, with premium = new SEA soundscapes and convenience
   features. *Why:* payment constraints [31], price bands [5], the Rain Rain model [45]. **M** (+
   content production)
10. **Saved ambient mixes** ("Hujan + kipas", etc.). **S**
11. **Opt-in bedtime reminder** at a user-chosen time, with calm wording and no guilt. *Why:* small
    but real effect [89]. **S**
12. **Glance home-screen widget** ("Tonight's routine", one-tap Ayat al-Kursi). *Why:* [90]. **M**
13. **Ramadan listing refresh and a "Juz each night" listening plan.** Sequential mushaf order,
    using per-ayah audio for mid-surah juz boundaries. Framed as listening, not a khatam, per §4b. *
    *M**
14. **Safe-volume cap and guidance** for ambient (an optional max-level limiter, plus a "for babies:
    far, low, short" tip). Don't target children in Play Console. *Why:* [68][69][92]. **S**

**Later**

15. **Ayah-accurate stop and highlighting for all surahs** (per-ayah gapless playback or timing
    data). **L**
16. **Licensed SEA reciters** (Muzammil Hasballah, Salim Bahanan, Taqy Malik) — keep them **free to
    listen**, funded by premium. *Why:* §6 demand; licensing is unknown. **L** (business
    development)
17. **Private consistency counter** ("nights with Ayat al-Kursi"), no public badges. *Why:* Bukhari
    6464. **S**
18. **Gentle wake alarm** (user-set time). **M**
19. **Arabic text and recite-along display.** First check Indonesian LPMQ *tashih* and Malaysian KDN
    certification (*perakuan*) under Act 326. Malaysian enforcement reportedly covers digital
    Quran (secondary) [96][97]. **L**

**Don't:** shuffle or random play, music or "lofi", ads, paywalled recitation, sleep stories, prayer
times or qibla, health or "therapy" claims, "#1/terbaik/gratis" in the title.

---

## Open questions / unverified

- **equran.id permission and audio provenance:** does their audio license allow a paid app that
  caches files offline? Which upstream source and license applies to each reciter? (No public
  answer; ask them.)
- **No Southeast Asian fatwa found on nature sounds or white noise behind recitation.**
  muftiwp.gov.my refused connections on 2026-10-03; its pages were read via Internet Archive copies.
  NU, MUI and Tarjih were searched without a direct hit. Consider asking Mufti WP (e-fatwa), NU's
  Lembaga Bahtsul Masail or a local ustadz, and get the "night train" sound reviewed.
- **Regulation:** does an **audio-only** Quran app need LPMQ *tashih* (Indonesia) or KDN
  *perakuan* (Malaysia, Act 326)? Malaysian press says digital versions are covered (secondary). Not
  verified from primary legal texts.
- The **"+120% Quran-app downloads in Ramadan (data.ai)"** claim could not be traced; Ramadan
  seasonality rests on secondary NST/Sensor Tower (2019).
- **Brunei Google Play payment methods:** no country section found.
- **SEA reciter licensing terms:** not public.
- **App Store IAP labels** (Calm, Headspace, BetterSleep, Loóna) are ambiguous, and Play shows no
  prices. Treat the price bands as indicative.
- **Indonesian Muslim population** comes via Databoks (secondary) citing Dukcapil; the Dukcapil
  dashboard itself wasn't reachable.
- **Tirmidhi 2892** grading differs between editions.
- Whether "tanpa iklan" in the **short description** passes Play review (judgment, based on the
  metadata policy).
- **YouTube views and autocomplete** show interest, not search volume. Google Trends or Play Console
  search-term data should confirm before finalizing the title.
- **Indonesian internet penetration** differs by source (BPS 72.8% for 2024 vs DataReportal 80.5%
  for end-2025).

---

## Sources

1. YouTube video pages, view counts as displayed on
   2026-10-03: https://www.youtube.com/watch?v=8acT7arp_B8 · https://www.youtube.com/watch?v=XUvxLu5FIQA · https://www.youtube.com/watch?v=YTSsRs3NXSU · https://www.youtube.com/watch?v=IOhZBGQwRAA · https://www.youtube.com/watch?v=GE5cJBlvVCc · https://www.youtube.com/watch?v=Z_d4UW0Expc · https://www.youtube.com/watch?v=w09-nqkc0GA · https://www.youtube.com/watch?v=2_GG1q85tzo · https://www.youtube.com/watch?v=qorkD6nPYQM
2. secondary signal: Google and YouTube autocomplete (suggestqueries.google.com, `client=firefox`,
   `ds=yt` for YouTube; `hl=id&gl=id` and `hl=ms&gl=my`), queried 2026-10-03 — shows popular
   queries, not volumes.
   Example: https://suggestqueries.google.com/complete/search?client=firefox&ds=yt&hl=id&gl=id&q=murottal%20pengantar%20tidur
3. Google Play listings, gl=ID (accessed
   2026-10-03): https://play.google.com/store/apps/details?id=com.salmakreatif.alquranmerdupengantartidur · https://play.google.com/store/apps/details?id=com.nssba.quran_for_sleep · https://play.google.com/store/apps/details?id=com.kareemastudio.murottalalquran30juzmerdu · https://play.google.com/store/apps/details?id=com.wenake.alquranmerdupengantartidur · https://play.google.com/store/apps/details?id=com.humakreatif.quranrainthundersounds · https://play.google.com/store/apps/details?id=com.kopimantan.ayatkursi10jam · https://play.google.com/store/apps/details?id=com.anafiya.quranwhitenoise
4. Quran Audio Player: Quranify (Media Content Labs) — Google
   Play. https://play.google.com/store/apps/details?id=com.mchutov.Quranify (accessed 2026-10-03)
5. Apple App Store listings, “In-App Purchases” sections,
   Indonesia (https://apps.apple.com/id/app/id…) and Malaysia (https://apps.apple.com/my/app/id…)
   storefronts (accessed 2026-10-03). IDs: Calm 571800810 · Headspace 493145008 · BetterSleep
   314498713 · Endel 1346247457 · Tide 1077776989 · Atmosphere 1259186300 · myNoise 1523675125 ·
   Rain Rain 478687481 · Loóna 1465238901 · Noisli 862773459 · Quranify 6756227351 · Muslim Pro
   388389451 · Tarteel 1391009396 · Pillars 1559086853.
   Example: https://apps.apple.com/id/app/id6756227351
6. Quran for Android (quran.com) — Google
   Play. https://play.google.com/store/apps/details?id=com.quran.labs.androidquran (accessed
   2026-10-03)
7. Al Quran (Tafsir & by Word), Greentech Apps Foundation — Google
   Play. https://play.google.com/store/apps/details?id=com.greentech.quran (accessed 2026-10-03)
8. Tarteel: AI Quran Memorization — Google
   Play. https://play.google.com/store/apps/details?id=com.mmmoussa.iqra (accessed 2026-10-03)
9. EQuran.id — Terms of Service v1.0, effective 15 Jun 2025 (§3 permitted use, §4 prohibited use, §5
   API, §7 IP; partner list in footer). https://equran.id/terms (accessed 2026-10-03)
10. Al Quran Cloud / Islamic Network — Terms & Conditions, last updated 14 Jun 2026 (§II text, §III
    rate limits, §IV recitations). https://alquran.cloud/terms-and-conditions (accessed 2026-10-03)
11. EQuran.id — API v2.0 developer documentation (6 qari; per-ayah audio; data source credited to
    Kemenag; partner links). https://equran.id/apidev/v2 (accessed 2026-10-03)
12. EQuran.id — API v2 response `GET /api/v2/surat/1` (`audioFull` and per-ayah `audio` objects,
    keys 01–06). https://equran.id/api/v2/surat/1 (accessed 2026-10-03)
13. EQuran.id CDN — author's HTTP HEAD checks on 2026-10-03: per-ayah files 002255, 002285, 002286
    and first ayat of surahs 67, 32, 109, 112 returned 200 OK for all 6 reciters (067030/112004
    spot-checked for two reciters) and all 114 full-surah files for Misyari-Rasyid-Al-Afasi (
    Content-Length summed).
    Example: https://cdn.equran.id/audio-partial/Misyari-Rasyid-Al-Afasi/002255.mp3
14. Pejabat Mufti Wilayah Persekutuan — Al-Kafi #1709: Hukum memasang ayat al-Quran dan tidak
    mendengarnya ketika berniaga (Muhammad Fathi Noordin, 22 Apr
    2020). https://www.muftiwp.gov.my/artikel/al-kafi-li-al-fatawi/4462-al-kafi-1709-hukum-memasang-ayat-al-quran-dan-tidak-mendengarnya-ketika-berniaga —
    read via Internet Archive copy (muftiwp.gov.my refused connections on 2026-10-03)
15. Dar al-Ifta al-Misriyyah — Fatwa no. 3517, “ruling on playing the Quran during official working
    hours” (Ali Gomaa, 11 Feb 2004). https://www.dar-alifta.org/ar/Fatwa/Details/13220 (accessed
    2026-10-03)
16. NU Online (Bahtsul Masail) — Menyimak Al-Qur’an Lewat Kaset (A. Khoirul Anam, 29 Apr 2014),
    citing the 26th NU Muktamar
    decision. https://nu.or.id/bahtsul-masail/menyimak-al-qurrsquoan-lewat-kaset-5Pjyx (accessed
    2026-10-03)
17. IslamQA — #50010 Can You Listen to the Quran while Sleeping? (14 Feb
    2004). https://islamqa.info/en/answers/50010 (accessed 2026-10-03)
18. SeekersGuidance (Shaykh Yusuf Weltch; approved by Shaykh Faraz Rabbani) — “Is it permissible to
    listen to the Quran while falling asleep?”, as republished by
    IslamQA.org. https://islamqa.org/?p=168491 (accessed 2026-10-03)
19. Darul Ifta Birmingham — Is It Allowed To Have the Quran Being Recited on a Speaker Whilst
    Sleeping? https://daruliftabirmingham.co.uk/is-it-allowed-to-have-the-quran-being-recited-on-a-speaker-whilst-sleeping/ (
    accessed 2026-10-03)
20. IslamQA (Arabic) — #145931, ruling on making bird, wind and music sounds a background to Quran
    recitation (28 Mar 2010). https://islamqa.info/ar/answers/145931 (accessed 2026-10-03)
21. IslamQA — #229732 Ruling on vocal effects in da`wah productions (3 Apr
    2025). https://islamqa.info/en/answers/229732 (accessed 2026-10-03)
22. Dar al-Ifta al-Misriyyah — “Reciting the Quran accompanied with musical instruments is
    prohibited in Islamic law” (3 Nov
    2014). https://www.dar-alifta.org/en/article/details/498/dar-al-ifta-reciting-the-quran-accompanied-with-musical-instruments-is-prohibit (
    accessed 2026-10-03)
23. NU Online — Hukum Quranic Song: Menggabungkan Musik dengan Ayat Al-Quran (Muhamad Hanif Rahman,
    16 Nov
    2024). https://islam.nu.or.id/syariah/hukum-quranic-song-menggabungkan-musik-dengan-ayat-al-quran-IhABP (
    accessed 2026-10-03)
24. Riedy SM, Smith MG, Rocha S, Basner M. Noise as a sleep aid: a systematic review. Sleep Med Rev.
    2021;55:101385. https://doi.org/10.1016/j.smrv.2020.101385 (PMID 33007706)
25. Basner M, Smith MG, Cordoza M, et al. Efficacy of pink noise and earplugs for mitigating the
    effects of intermittent environmental noise exposure on sleep. Sleep.
    2026;zsag001. https://doi.org/10.1093/sleep/zsag001 (PMID 41627391)
26. Buxton RT, Pearson AL, Allou C, Fristrup K, Wittemyer G. A synthesis of health benefits of
    natural sounds and their distribution in national parks. PNAS. 2021;118(14):
    e2013097118. https://doi.org/10.1073/pnas.2013097118 (PMID 33753555)
27. Ghiasi A, Keramat A. The effect of listening to Holy Quran recitation on anxiety: a systematic
    review. Iran J Nurs Midwifery Res. 2018;23(6):
    411–420. https://doi.org/10.4103/ijnmr.ijnmr_173_17 (PMID 30386389)
28. Gavgani VZ, Ghojazadeh M, Sadeghi-Ghyassi F, Khodapanah T. Effects of Quran recitation on the
    reduction of preoperative anxiety in elective surgery: a systematic review and meta-analysis of
    RCTs. Research Square preprint, 2020 (not
    peer-reviewed). https://doi.org/10.21203/rs.3.rs-48044/v2
29. Abd-alrazaq A, Malkawi AA, Maabreh AH, et al. The effectiveness of listening to the Holy Quran
    to improve mental disorders and psychological well-being: systematic review and meta-analysis.
    Research Square preprint, 2020 (not peer-reviewed). https://doi.org/10.21203/rs.3.rs-44376/v1
30. Google Play Console Help — Health Content and Services (Developer Program
    Policy). https://support.google.com/googleplay/android-developer/answer/16679511 (accessed
    2026-10-03)
31. Google Play Help — Accepted payment methods on Google Play, Indonesia and Malaysia country
    views (no Brunei-specific section
    found). https://support.google.com/googleplay/answer/2651410?co=GENIE.CountryCode%3DID · https://support.google.com/googleplay/answer/2651410?co=GENIE.CountryCode%3DMY (
    accessed 2026-10-03)
32. Author's computation (2026-10-03) with Apple Foundation
    `Calendar(identifier: .islamicUmmAlQura)` — the ICU implementation of Saudi Arabia's Umm al-Qura
    calendar (time zone Asia/Riyadh). Local computation; no URL. Cross-checked: it returns 1 Ramadan
    1447 = 18 Feb 2026.
33. secondary: Metro TV News — Kapan Ramadan 2027? Ini Perkiraan Awal Puasa 1448 H (2 Sep 2026),
    reporting Muhammadiyah KHGT
    dates. https://www.metrotvnews.com/read/kBVCM5Xl-kapan-ramadan-2027-ini-perkiraan-awal-puasa-1448-h
34. secondary: New Straits Times — Most downloaded app during Ramadan (17 May 2019), citing Sensor
    Tower. https://nst.com.my/amp/lifestyle/bots/2019/05/489159/most-downloaded-app-ramadan
35. secondary: Databoks (Katadata) — Mayoritas Penduduk Indonesia Beragama Islam pada Semester I
    2024 (8 Aug 2024), citing Ditjen Dukcapil,
    Kemendagri. https://databoks.katadata.co.id/datapublish/2024/08/08/mayoritas-penduduk-indonesia-beragama-islam-pada-semester-i-2024
36. Department of Statistics Malaysia — Key Findings, Population and Housing Census of Malaysia
    2020 (released 29 May
    2022). https://www.dosm.gov.my/portal-main/release-content/key-findings-population-and-housing-census-of-malaysia-2020-administrative-district (
    accessed 2026-10-03)
37. Department of Economic Planning and Statistics, Brunei — Media release, Report of the Population
    and Housing Census (BPP) 2021 (paras 4.1.1,
    4.1.7). https://www.deps.gov.bn/wp-content/uploads/2025/11/PR-2.pdf (accessed 2026-10-03)
38. World Bank — Individuals using the Internet (% of population), indicator IT.NET.USER.ZS, 2024
    values for IDN/MYS/BRN via
    API. https://api.worldbank.org/v2/country/IDN;MYS;BRN/indicator/IT.NET.USER.ZS?format=json · https://data.worldbank.org/indicator/IT.NET.USER.ZS (
    accessed 2026-10-03)
39. Calm — Google Play listing, description and sampled
    reviews. https://play.google.com/store/apps/details?id=com.calm.android (accessed 2026-10-03)
40. Headspace: Sleep & Meditation — Google
    Play. https://play.google.com/store/apps/details?id=com.getsomeheadspace.android (accessed
    2026-10-03)
41. BetterSleep — Google Play listing and sampled
    reviews. https://play.google.com/store/apps/details?id=ipnossoft.rma.free (accessed 2026-10-03)
42. Endel — Google Play. https://play.google.com/store/apps/details?id=com.endel.endel (accessed
    2026-10-03)
43. “noisli: Sleep & White Noise” (developer Dev.CrsCaballero; not verified as the official
    Noisli) — Google Play. https://play.google.com/store/apps/details?id=dev.crscaballero.noisli (
    accessed 2026-10-03)
44. myNoise — Google Play. https://play.google.com/store/apps/details?id=com.mynoise.mynoise (
    accessed 2026-10-03)
45. Rain Rain Sleep Sounds — Google
    Play. https://play.google.com/store/apps/details?id=com.timgostony.rainrain (accessed
    2026-10-03)
46. White Noise Lite (TMSOFT) — Google Play listing and sampled
    reviews. https://play.google.com/store/apps/details?id=com.tmsoft.whitenoise.lite (accessed
    2026-10-03)
47. Atmosphere: Relaxing Sounds — Google
    Play. https://play.google.com/store/apps/details?id=com.peakpocketstudios.atmosphere (accessed
    2026-10-03)
48. Tide – Sleep & Meditation — Google
    Play. https://play.google.com/store/apps/details?id=io.moreless.tide (accessed 2026-10-03)
49. Loóna: Bedtime Relax & Sleep — Google
    Play. https://play.google.com/store/apps/details?id=co.loona (accessed 2026-10-03)
50. Baumel A, Muench F, Edan S, Kane JM. Objective user engagement with mental health apps:
    systematic search and panel-based usage analysis. J Med Internet Res. 2019;21(9):
    e14567. https://doi.org/10.2196/14567 (PMID 31573916)
51. Calm Blog — Celebrating 100 Sleep Stories (c. mid-2018; first story recorded Nov
    2016). https://www.calm.com/blog/celebrating-100-sleep-stories (accessed 2026-10-03)
52. Muslim Pro — Google Play listing and sampled
    reviews. https://play.google.com/store/apps/details?id=com.bitsmedia.android.muslimpro (accessed
    2026-10-03)
53. Al Jazeera — “US military buys location data of popular Muslim apps: Report” (17 Nov 2020) and
    “Muslim Pro app denies selling user data to US military” (18 Nov
    2020). https://www.aljazeera.com/news/2020/11/17/report-us-military-buying-location-data-on-popular-muslim-apps · https://www.aljazeera.com/amp/news/2020/11/18/muslim-pro-app-denies-selling-user-data-to-us-military
54. Pillars: Prayer Times & Qibla — Google
    Play. https://play.google.com/store/apps/details?id=com.pillars.pillars (accessed 2026-10-03)
55. umma: Muslim Azan Prayer Quran — Google Play listing and sampled
    reviews. https://play.google.com/store/apps/details?id=com.muslim.android (accessed 2026-10-03)
56. Qur'an Kemenag (Lajnah Pentashihan Mushaf Al-Qur'an) — Google Play listing and sampled
    reviews. https://play.google.com/store/apps/details?id=com.quran.kemenag (accessed 2026-10-03)
57. Wemu: WeMuslim, Athan&Qibla — Google
    Play. https://play.google.com/store/apps/details?id=com.fyxtech.muslim (accessed 2026-10-03)
58. Google Play listings (accessed
    2026-10-03): https://play.google.com/store/apps/details?id=com.sakinah.app · https://play.google.com/store/apps/details?id=com.sakeenah.app · https://play.google.com/store/apps/details?id=app.getsakeena.android
59. Vincens N, Nause A, Basner M, et al. Pink noise reduces impact of traffic noise on sleep and the
    blood metabolome: a cross-over pilot study. Commun Med.
    2026. https://doi.org/10.1038/s43856-026-01380-5 (PMID 41513961)
60. Zhou J, Liu D, Li X, Ma J, Zhang J, Fang J. Pink noise: effect on complexity synchronization of
    brain activity and sleep consolidation. J Theor Biol. 2012;306:
    68–72. https://doi.org/10.1016/j.jtbi.2012.04.006 (PMID 22726808)
61. Ngo HV, Martinetz T, Born J, Mölle M. Auditory closed-loop stimulation of the sleep slow
    oscillation enhances memory. Neuron. 2013;78(3):
    545–553. https://doi.org/10.1016/j.neuron.2013.03.006 (PMID 23583623)
62. Papalambros NA, Santostasi G, Malkani RG, et al. Acoustic enhancement of sleep slow oscillations
    and concomitant memory improvement in older adults. Front Hum Neurosci. 2017;11:
    109. https://doi.org/10.3389/fnhum.2017.00109 (PMID 28337134)
63. Wunderlin M, Züst MA, Hertenstein E, et al. Modulating overnight memory consolidation by
    acoustic stimulation during slow-wave sleep: a systematic review and meta-analysis. Sleep.
    2021;44(7):zsaa296. https://doi.org/10.1093/sleep/zsaa296 (PMID 33406249)
64. Retraction of Stanyer EC et al., “The impact of acoustic stimulation during sleep on memory and
    sleep architecture: a meta-analysis” (J Sleep Res 2022;31:e13385). J Sleep Res.
    2026. https://doi.org/10.1111/jsr.70111 (PMID 40519131)
65. Gould van Praag CD, Garfinkel SN, Sparasci O, et al. Mind-wandering and alterations to default
    mode network connectivity when listening to naturalistic versus artificial sounds. Sci Rep.
    2017;7:45273. https://doi.org/10.1038/srep45273 (PMID 28345604)
66. Alvarsson JJ, Wiens S, Nilsson ME. Stress recovery during exposure to nature sound and
    environmental noise. Int J Environ Res Public Health. 2010;7(3):
    1036–1046. https://doi.org/10.3390/ijerph7031036 (PMID 20617017)
67. Nigg JT, Bruton A, Kozlowski MB, Johnstone JM, Karalunas SL. Systematic review and
    meta-analysis: do white noise or pink noise help with task performance in youth with ADHD or
    with elevated attention problems? J Am Acad Child Adolesc Psychiatry.
    2024. https://doi.org/10.1016/j.jaac.2023.12.014 (PMID 38428577)
68. WHO & ITU — Safe listening devices and systems: a WHO-ITU standard (18 Sep 2019; ITU-T H.870)
    and its 2-page summary (
    WHO/NMH/NVI/19.4). https://www.who.int/publications/i/item/9789241515276 · https://cdn.who.int/media/docs/default-source/documents/health-topics/deafness-and-hearing-loss/standard-summary-make-listening-safe.pdf
69. Hugh SC, Wolter NE, Propst EJ, Gordon KA, Cushing SL, Papsin BC. Infant sleep machines and
    hazardous sound pressure levels. Pediatrics. 2014;133(4):
    677–681. https://doi.org/10.1542/peds.2013-3617 (PMID 24590753)
70. secondary: Sleep Review — report on the SickKids infant white-noise machine study, with the
    authors' recommendations (6 Mar
    2014). https://sleepreviewmag.com/uncategorized/sleep-white-noise-machines-dangerous-decibel/
71. American Academy of Pediatrics via HealthyChildren.org — “AAP Sounds Alarm on Excessive Noise
    Risks to Children” (21 Oct 2023), on the policy statement “Preventing Excessive Noise Exposure
    in Infants, Children, and Adolescents” (Pediatrics, Nov
    2023). https://www.healthychildren.org/English/news/Pages/sounds-the-alarm-on-excessive-noise-and-risks-to-children.aspx
72. Che Wan Mohd Rozali WNA, Ishak I, Mat Ludin AF, et al. The impact of listening to, reciting, or
    memorizing the Quran on physical and mental health of Muslims: evidence from systematic review.
    Int J Public Health. 2022;67:1604998. https://doi.org/10.3389/ijph.2022.1604998 (PMID 36119448)
73. Majidi H, Rajabi-Tavakkol A. A systematic review of EEG studies on the neural effects of Quran
    listening. Iran J Psychiatry. 2025;20(2). https://doi.org/10.18502/ijps.v20i2.18206 (PMID
    40521283)
74. Europe PMC title search (Quran OR Koran OR murottal) AND (sleep OR insomnia), run 2026-10-03 via
    the Europe PMC REST
    API. https://europepmc.org/search?query=%28TITLE%3A%22Quran%22%20OR%20TITLE%3A%22Koran%22%20OR%20TITLE%3A%22murottal%22%29%20AND%20%28TITLE%3A%22sleep%22%20OR%20TITLE%3A%22insomnia%22%29
75. Quran.com verse pages: Al-A'raf 7:204 https://quran.com/7/204 · Ar-Ra'd 13:
    28 https://quran.com/13/28 · Al-Baqarah 2:255 https://quran.com/2/255 · 2:
    285–286 https://quran.com/2/285-286 · Al-Mulk https://quran.com/67 ·
    As-Sajdah https://quran.com/32 · Al-Kafirun https://quran.com/109
76. NU Online (Fragmen) — Muktamar NU di Bulan Juni (1): Ke-12 di Malang dan Ke-26 di Semarang (
    dates of the 26th
    Muktamar). https://nu.or.id/fragmen/muktamar-nu-di-bulan-juni-1-ke-12-di-malang-dan-ke-26-di-semarang-phtxs (
    accessed 2026-10-03)
77. Majelis Tarjih dan Tajdid PP Muhammadiyah — Membaca Al-Qur'an dengan Pengeras Suara (fatwa
    session 22 Nov 2013; Suara Muhammadiyah No.
    14/2014). https://fatwatarjih.or.id/membaca-al-quran-dengan-pengeras-suara/ (accessed
    2026-10-03)
78. Pejabat Mufti Wilayah Persekutuan — Al-Kafi #1708: The ruling of reciting the Quran while lying
    down (Muhammad Fahmi Rusli, 22 Apr
    2020). https://muftiwp.gov.my/en/artikel/al-kafi-li-al-fatawi/4460-al-kafi-1708-the-ruling-of-reciting-the-quran-while-lying-down —
    read via Internet Archive copy
79. Pejabat Mufti Wilayah Persekutuan — Al-Kafi #1232: Is the reward for someone who listens to the
    recitation of the Quran the same as the one who recites it? (Wan Ahmad Naqiuddin, 19 Apr
    2019). https://muftiwp.gov.my/en/artikel/al-kafi-li-al-fatawi/3329-al-kafi-1232-is-the-reward-for-someone-who-listen-to-the-recitation-of-al-quran-the-same-as-those-who-recite-it —
    read via Internet Archive copy
80. IslamQA — #228366 Ruling on Listening to Surat Al-Mulk before Sleeping (16 Sep
    2024). https://islamqa.info/en/answers/228366 (accessed 2026-10-03)
81. Islamweb — Fatwa #142023: Reciting the Quran during a thunderstorm (2 Nov
    2010). https://islamweb.net/en/fatwa/142023/reciting-the-quran-during-a-thunderstorm (accessed
    2026-10-03)
82. secondary: RMOL — MUI Tuntut Ahmad Dhani Minta Maaf (2 Oct 2024), quoting KH Jeje Zaenudin (MUI
    commission on Islamic arts, culture and
    civilization). https://rmol.id/politik/read/2024/10/02/639162/mui-tuntut-ahmad-dhani-minta-maaf
83. Pejabat Mufti Wilayah Persekutuan — Irsyad al-Fatwa Series 59: The ruling of the usage of Quran
    recitation and adhan as ringtone or screen saver (31 Jul 2015), citing the 77th Muzakarah of the
    National Fatwa Committee (10–12 Apr
    2007). https://www.muftiwp.gov.my/en/artikel/irsyad-fatwa/irsyad-fatwa-umum-cat/1901-the-ruling-of-the-usage-of-quran-recitation-and-adhan-as-ringtone-or-screen-saver —
    read via Internet Archive copy
84. fawazahmed0/hadith-api — open hadith dataset (English editions with Arabic/Abdul-Baqi
    numbering), used to verify the hadith numbers and wording because sunnah.com returned HTTP 403
    to automated requests. https://github.com/fawazahmed0/hadith-api (
    data: https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/)
85. Badan Pusat Statistik — Statistik Telekomunikasi Indonesia 2024 (published 29 Aug
    2025). https://www.bps.go.id/id/publication/2025/08/29/beaa2be400eda6ce6c636ef8/statistik-telekomunikasi-indonesia-2024.html (
    page returned 403 to automated access; the 72.78% figure matches the World Bank series)
86. Department of Statistics Malaysia — ICT Use and Access by Individuals and Households Survey
    Report 2024, infographic (released 24 Apr
    2025). https://www.dosm.gov.my/uploads/release-content/file_20250423190242.pdf
87. secondary: DataReportal — Digital 2026:
    Indonesia. https://datareportal.com/reports/digital-2026-indonesia
88. YouTube channel pages, subscriber counts as shown on
    2026-10-03: https://www.youtube.com/@muzammilhb · https://www.youtube.com/@SalimBahananofficial · https://www.youtube.com/@TaqyMalik1 · https://www.youtube.com/@alafasy · https://www.youtube.com/@alaaaqel54 · https://www.youtube.com/@HananAttaki
89. Bidargaddi N, Almirall D, Murphy S, et al. To prompt or not to prompt? A microrandomized trial
    of time-varying push notifications to increase proximal engagement with a mobile health app.
    JMIR Mhealth Uhealth. 2018;6(11):e10123. https://doi.org/10.2196/10123 (PMID 30497999)
90. Android Developers Blog — Gratitude saw 25% higher retention for widget users (8 May 2026;
    company-reported case
    study). https://developer.android.com/blog/posts/gratitude-saw-25-higher-retention-for-widget-users
91. Google Play Console Help — Get discovered on Google Play
    search. https://support.google.com/googleplay/android-developer/answer/4448378 (accessed
    2026-10-03)
92. Google Play Console Help — Google Play Families
    Policies. https://support.google.com/googleplay/android-developer/answer/9893335 (accessed
    2026-10-03)
93. Google Play Console Help — Health app categories and additional
    information. https://support.google.com/googleplay/android-developer/answer/13996367 (accessed
    2026-10-03)
94. Google Play Console Help — Metadata (store listing
    policy). https://support.google.com/googleplay/android-developer/answer/9898842 (accessed
    2026-10-03)
95. Android Developers — Foreground service types (
    mediaPlayback). https://developer.android.com/develop/background-work/services/fgs/service-types (
    accessed 2026-10-03)
96. secondary: Sinar Harian — KDN tidak sekat penerbitan al-Quran, hanya perlu perakuan (15 Aug
    2023). https://www.sinarharian.com.my/article/273942/berita/nasional/kdn-tidak-sekat-penerbitan-al-quran-hanya-perlu-perakuan
97. secondary: Utusan Malaysia — 22,414 naskhah, bahan al-Quran tanpa kelulusan dirampas (15 Jul
    2024). https://www.utusan.com.my/nasional/2024/07/22414-naskhah-bahan-al-quran-tanpa-kelulusan-dirampas/
