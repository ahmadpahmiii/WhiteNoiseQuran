# White Noise Quran — Ambient Sounds & Rewarded-Ad Unlock Research

## 1. Scope and owner's decisions

**Date:** 2026-10-03 · **Prepared for:** product owner · **Scope:** the ambient sounds that play
under or
after the recitation (fit with the Quran, sleep, SEA demand, sourcing and licenses, loop specs) and
unlocking them with rewarded ads. Quran packs are in `research_packs.md`. Competitors, the
noise-for-sleep science and the fatwas on background sound are in `research_white_noise_quran.md`
(cited as "earlier doc §n").

**Owner's decisions (fixed):**

- Market: Southeast Asia (Indonesia first, then Malaysia and Brunei). UI in Indonesian, Malay and
  English.
- Use cases: sleep, and calm/anxiety relief. Scope: Quran + ambient only.
- Money: freemium with **rewarded ads**. Exactly 3 of the current 6 sounds are free; the other 3,
  and
  every new sound, are unlocked by watching a rewarded ad.
- Hard rules: no music, instruments, musical pads/drones or beats; no shuffle; no health claims;
  recitation is never behind an ad; no streaks or progress counters; the app will not claim "tanpa
  iklan".
- This supersedes the earlier doc's "no ads, ever" recommendation.

**How to read:** `[n]` = source list in §12. "secondary:" = no primary source. "est." = my estimate,
not a measurement. "judgment" = a recommendation without direct evidence. All numbers about the
bundled loops are my own measurements [1].

---

## 2. TL;DR

1. **Free: Steady Rain, Gentle Drizzle, Ocean Waves. Ad-unlocked: Lush Forest, Rain & Songbirds,
   Night Train.** Rubric scores 12, 11, 10 against 8, 7, 6 (out of 12) [1]. Retire Night Train
   before launch and give its slot to **Kipas Angin** (electric fan).
2. **Tonality, rhythm and sudden events spoil the match, not frequency overlap.** At 18 LU under
   the reciter every loop keeps a simplified SII of 0.89–0.92 (train 0.99) [1][5]. But forest is
   78% tonal frames (dense birdsong), rain & birds has 5 calls >10 dB above the rain per 57-s loop,
   and train clatters every ~2 s (envelope autocorrelation 0.49) [1].
3. **Default bed: recitation −18 LU, never above −10 LU while recitation plays.** Broadcast
   research wants ≥15 LU for ambience [7]; non-native listeners need 1–7 dB more [9]. The loops
   differ by 14 LU (−16.7 to −30.5 LUFS), and the app's 0.50–0.70 defaults don't correct for it [1].
4. **Sleep: no thunder claps, bird calls or clatter in loops.** Arousal follows the jump above the
   background, not the peak [13]; fast rise and >3 kHz content drive it [15]. Users agree:
   "suara hujan **tanpa petir**" (without thunder) recurs in ID and MY autocomplete [30].
5. **Add generated white, pink and brown noise (0 KB in the APK).** At equal loudness brown masks
   speech least (SII 0.99 at −18 LU), then pink 0.91 and white 0.90 [1]; "white noise bayi" videos
   have 380M and 362M views [31]. Fade out rather than play all night (REM caution [17]).
6. **Top new recordings: Kipas Angin, Hujan di Atap Seng / atas zink, Air Terjun**; later Sungai,
   Hujan di Hutan, Hujan di Genteng, Hujan di Tenda. Fan videos have 124.7M and 51.1M views [31].
   Record the fan and tin roof yourself; Freesound CC0 candidates are verified (§6). Pixabay,
   Mixkit, Zapsplat, Sonniss and BBC terms forbid or endanger use as a sound app's main content
   [35]–[39].
7. **Unlock = 7 days per sound per ad**, any time of day, never expiring mid-playback, plus a free
   30–60 s preview. Precedents: "until morning" per ad [54], 3 days per video [55]; per-play would
   force a bright, loud video every bedtime. Mute ads with `setAppMuted(true)` (smaller ad pool)
   [44]; block dating, sex, gambling, alcohol, get-rich-quick, social-casino, religion and politics
   categories, and set content rating G [42][43][45].
8. **Ads change the listing and privacy:** "Contains ads" label [48]; Data safety must declare the
   Ads SDK's IP-based location, app interactions, diagnostics and device IDs [47]. "Murottal tidak
   pernah disela iklan" (never interrupted by ads) is a true claim; "tanpa iklan" is not [53].
   Rewarded eCPM for Indonesia/Malaysia is **unverified**.

---

## 3. The Quran-compatibility rubric

### 3a. How the bundled loops were measured [1]

- **Format:** `afinfo`. **Loudness:** Python; BS.1770 K-weighting and gating (libebur128
  coefficients) [2], and loudness range (LRA) per EBU Tech 3342 [3].
- **Spectrum:** ⅓-octave band energy and the slope from 100 Hz to 8 kHz. For reference, white noise
  is 0 dB/oct, pink −3 and brown −6.
- **Tonal frames:** the share of 46-ms frames in which the 1–8 kHz band holds ≥5% of the energy and
  spectral flatness is below 0.1.
- **Rhythm:** autocorrelation of the detrended 10-ms envelope at lags of 0.2–3 s.
- **Events:** 400-ms loudness more than 6 or 10 dB above the rolling 10-s median.
- **Simplified SII:** ANSI S3.5 ⅓-octave procedure: standard speech spectrum, Table 3 constants
  (checked against the CRAN `SII` package data), spread of masking, normal hearing, no level
  distortion. The noise is scaled so that its K-weighted level sits X LU below the speech [5].
- **Limits:**
    - SII assumes steady noise; fluctuating maskers need the extended SII (ESII) [12].
    - I did not measure the reciters' own loudness (no audio was downloaded).
    - The SII bands of 0.75+ = "good" and below 0.45 = "poor" are secondary [6].

### 3b. Criteria (0–2 points each, total /12)

| #     | Criterion                                            | 2                                                                      | 1                                                            | 0                                                    | Evidence                                                                                                                                                                                                                                                                                                                                                                                           |
|-------|------------------------------------------------------|------------------------------------------------------------------------|--------------------------------------------------------------|------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **A** | Energetic masking (bed at −18 LU)                    | SII ≥ 0.90                                                             | 0.85–0.89                                                    | < 0.85                                               | **59%** of SII band-importance weight is in the 1–4 kHz bands and 16% at or below 500 Hz [5]. So hiss, birdsong and crickets cost more than a low rumble. Raised vocal effort (projected recitation) adds more energy at 1 kHz than at 250 Hz (+8.9 vs +4.2 dB in the standard spectra) and slightly raises SII at the same offset [5][1]                                                          |
| **B** | Tonal or speech-like content (informational masking) | < 2% tonal frames, no voices                                           | 2–20% (intermittent chirps, whistles)                        | > 20%, or any voice, chant or singing                | Competing voices mask beyond their energy, most when they resemble the target voice [10]. Changing sounds disrupt attention; a repeated steady sound doesn't [11]                                                                                                                                                                                                                                  |
| **C** | Rhythm (beat-likeness)                               | Envelope autocorrelation < 0.2 at 0.2–3 s                              | Only slow swells (period > 3 s)                              | ≥ 0.3 at 0.2–3 s: clatter, clock, heartbeat          | IslamQA #229732: imitating instruments or percussion takes the ruling of music [21]. Regular pulses also make a loop "countable" (§8)                                                                                                                                                                                                                                                              |
| **D** | Sleep steadiness                                     | LRA ≤ 5 LU and no event > 6 dB above the background                    | LRA ≤ 12 LU, no event > 10 dB                                | Any event > 10 dB                                    | <ul><li>Arousal depended on the rise from baseline to peak (~17.5 dB), not the peak level. Added steady noise cut ICU-noise arousals from 48.4/h to 15.7/h [13].</li><li>Rise time and content above 3 kHz explain higher arousal for rail and road events [15].</li><li>40–70 dBA sounds caused arousals that varied by sound type [14].</li><li>Resilience varies between people [16].</li></ul> |
| **E** | Religious and cultural fit                           | Natural or neutral non-musical sound, positive or neutral associations | Mixed associations, or a sound named in a fatwa              | Gate concern (below)                                 | §3c                                                                                                                                                                                                                                                                                                                                                                                                |
| **F** | SEA demand                                           | Local phrasing in ID **and** MY autocomplete, plus ≥ 10M-view videos   | One market only, or mixed intent (bird-keeping, kids' songs) | Weak, or a different intent (e.g. fixing a noisy AC) | §3d                                                                                                                                                                                                                                                                                                                                                                                                |

**Gates (exclude whatever the score):**

- music, instruments, musical pads or drones, beats;
- bells, wind chimes, gongs, singing bowls, chanting;
- the adhan;
- voices: café, crowd, "shhh";
- thunder claps;
- dogs and donkeys [21][22][24][25].

A 0 on C or E means: don't play it **under** recitation, and get a scholar's view. The rubric is for
the "under" mode. In "after" mode (the ambient sound starts when the recitation ends; earlier doc
product rules 1–2, `research_packs.md` §5), A–C matter less and D matters more.

### 3c. Religious and cultural appropriateness (range of views; no side taken)

| Topic                                 | More permissive                                                                                                                                                                                             | More cautious                                                                                                                                                                                                                                                                                                                                                       | Design answer                                                                                      |
|---------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| Any sound behind recitation           | <ul><li>No SEA fatwa found: 2 searches here, plus earlier doc §4b.</li><li>A Quranify reviewer argues it's natural sound, not music.</li><li>Real rain during recitation is unobjectionable [23].</li></ul> | <ul><li>IslamQA #145931: birds, wind or thunder added behind recitation is at least makruh, and the Quran should be kept free of such sounds [22].</li><li>IslamQA #229732: natural sounds are fine in general, but not as a background to recitation [21].</li></ul>                                                                                               | Packs default to "after"; "under" is opt-in; "Quran only" is one tap away                          |
| Beats and instrument-like sounds      | —                                                                                                                                                                                                           | Imitating instruments or percussion counts as music [21]. Music with Quran is prohibited by all sources reviewed (earlier doc §4b)                                                                                                                                                                                                                                  | Gate: train clatter, heartbeat, clock, drums, binaural tones                                       |
| Bells, chimes, gongs, bowls, chanting | —                                                                                                                                                                                                           | <ul><li>The bell is called Satan's musical instrument (Muslim 2114).</li><li>Angels don't accompany travellers with a dog or a bell (Muslim 2113).</li><li>The bell was rejected as the call to prayer because it resembled Christian practice (Muslim 377) [24].</li><li>MUI 56/2016: using non-Muslim religious attributes is *haram* (secondary [28]).</li></ul> | Gate (`research_packs.md` §5 already bans bells and chimes)                                        |
| Animals                               | A rooster's crow is a cue to ask for Allah's bounty (Bukhari 3303) [25]                                                                                                                                     | <ul><li>Seek refuge when donkeys bray (Bukhari 3303); 31:19 calls theirs the harshest voice [26].</li><li>Angels don't enter a house with a dog (Bukhari 3322) [25].</li><li>Tokek (gecko) searches are about omens ("artinya", "pertanda") and fear ("seram") [30].</li></ul>                                                                                      | Exclude dogs, donkeys and tokek. Roosters are sudden dawn events, so exclude them for sleep anyway |
| Rain, water                           | <ul><li>The Prophet ﷺ made dua on seeing rain (Bukhari 1032) [25].</li><li>"suara sungai al kautsar" appears in ID autocomplete [30].</li></ul>                                                             | —                                                                                                                                                                                                                                                                                                                                                                   | The rain/water family is the core                                                                  |
| Wind, storm                           | Wind is from Allah's mercy; don't curse it (Abu Dawud 5097) [25]                                                                                                                                            | Wind brings mercy or punishment, and the Prophet's ﷺ face showed concern at dark clouds or wind (Muslim 899) [24]. Some IDs pair wind with "seram" (scary) [30]                                                                                                                                                                                                     | Gentle breeze only, no storms                                                                      |
| Thunder                               | Thunder glorifies Allah (13:13) [26]; huge demand for "hujan dan petir" [31]                                                                                                                                | Claps are sudden events (§3b D); users search "tanpa petir" [30]                                                                                                                                                                                                                                                                                                    | Exclude claps; label rain sounds "tanpa petir"                                                     |
| Adhan as "ambient"                    | —                                                                                                                                                                                                           | <ul><li>Listeners repeat after the muadhin (Bukhari 611) [25].</li><li>Mufti WP: cutting off the adhan or recitation abruptly is impermissible; the 77th Muzakarah (2007) allowed Quran ringtones only with adab [27].</li></ul>                                                                                                                                    | Gate. Also keep field recordings free of mosque loudspeakers                                       |
| Café or crowd                         | —                                                                                                                                                                                                           | Voices add informational masking [10], cafés usually play music, and they are entertainment venues                                                                                                                                                                                                                                                                  | Gate                                                                                               |
| Natural vs artificial                 | Natural sounds were linked to more restful brain and heart measures than artificial ones [29]. A steady fan or white noise is non-musical; none of the fatwas found addresses it                            | —                                                                                                                                                                                                                                                                                                                                                                   | Both are OK; natural is the default                                                                |

### 3d. SEA demand: autocomplete and YouTube

**Method:** Google and YouTube autocomplete via suggestqueries (`hl=id&gl=id`, `hl=ms&gl=my`) [30],
and
YouTube search pages [31], all on 2026-10-03. This is the same method as earlier doc §2c/§7.

- Views are global and lifetime.
- YouTube auto-translates foreign titles into Indonesian.
- So views show interest in a *type* of sound, not SEA search volume.

| Candidate                  | Indonesia (autocomplete examples)                                                                                                                                           | Malaysia                                                                        | Top YouTube views                                                                        |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| Rain (general)             | suara hujan untuk tidur · … **tanpa petir** · … tanpa iklan · suara rintik hujan pengantar tidur                                                                            | bunyi hujan untuk tidur · … tanpa petir · … **black screen**                    | 248.8M (3 h gentle night rain); 97.7M (rain + thunder)                                   |
| Rain on a tin roof         | suara hujan deras di atap seng tanpa petir · … pengantar tidur atap seng                                                                                                    | bunyi hujan lebat **atas zink** · … untuk tidur zink                            | 83.5M and 31.0M (metal roof, with thunder); 15.4M ("10 jam hujan keras pada atap logam") |
| Rain on roof tiles         | suara hujan di atas genteng · … untuk tidur di atas genteng (also the kids' song "tik tik tik")                                                                             | bunyi hujan atas bumbung                                                        | —                                                                                        |
| Electric fan               | suara kipas angin pengantar tidur · … untuk bayi · … untuk tidur                                                                                                            | bunyi kipas angin untuk tidur                                                   | **124.7M**, 51.1M, 16.3M                                                                 |
| Crickets/frogs, rice field | suara jangkrik dan katak di sawah · suara sawah malam hari · suara kodok jangkrik hujan malam hari. But the top "suara jangkrik" intent is "pengusir tikus" (rat repellent) | bunyi cengkerik malam · … dalam rumah menurut islam · bunyi katak selepas hujan | 14.8M (night swamp: frogs, crickets, drizzle); 13.1M; a local video 0.62M                |
| Rain in the forest         | suara hujan di hutan untuk tidur · … tanpa petir di hutan                                                                                                                   | bunyi hutan hujan                                                               | 50.4M (with thunder)                                                                     |
| River or stream            | suara sungai mengalir · suara air mengalir **tanpa musik** · … terapi burung                                                                                                | bunyi air sungai mengalir tanpa musik · **zikir bunyi sungai**                  | 16.2M, 15.8M                                                                             |
| Waterfall                  | suara air terjun untuk tidur (plus "terapi burung")                                                                                                                         | bunyi air terjun untuk tidur · zikir bunyi air terjun                           | 26.3M                                                                                    |
| Waves                      | suara ombak pantai tanpa musik · … pengantar tidur tanpa iklan                                                                                                              | bunyi ombak untuk tidur · zikir bunyi ombak                                     | 84.7M, 27.5M                                                                             |
| Wind                       | suara angin sepoi-sepoi · … malam (also "seram")                                                                                                                            | bunyi angin untuk tidur · … lembut                                              | weak (< 1M found)                                                                        |
| Rain on a tent             | suara hujan di tenda camping · … dalam tenda tanpa petir                                                                                                                    | —                                                                               | 25.8M; 21.7M (thunder)                                                                   |
| AC hum                     | troubleshooting only ("suara ac berisik")                                                                                                                                   | troubleshooting only ("bunyi aircond bising")                                   | —                                                                                        |
| Campfire                   | suara api unggun untuk tidur · … dan jangkrik                                                                                                                               | —                                                                               | 12.3M                                                                                    |
| White/brown/pink noise     | white noise bayi · brown noise untuk belajar; pink noise is pro-audio intent ("pink noise rta")                                                                             | white noise for babies **zikir** · white noise no ads                           | white **380.0M, 362.5M**; brown 56.9M, 30.2M                                             |
| Hair dryer (for babies)    | suara hair dryer untuk bayi                                                                                                                                                 | —                                                                               | 91.1M                                                                                    |
| Train                      | kids' songs dominate                                                                                                                                                        | —                                                                               | the top "untuk tidur" hit is a kids' song (54.7M)                                        |
| Quran + nature             | murottal suara hujan · murottal suara alam                                                                                                                                  | zikir bunyi hujan; EN "quran with rain sound no ads"                            | 6.5M, 4.2M (earlier doc §2c)                                                             |

**Signals worth acting on:**

- People ask for **tanpa petir** (no thunder), **tanpa musik** (no music) and **tanpa iklan** (no
  ads).
- They ask for **black screen** videos at bedtime, so the app should stay dark at night.
- Malaysians already pair **zikir with water sounds**.
- Demand for water sounds is inflated by bird-keepers ("terapi burung"), and for crickets by rat
  repellent ("pengusir tikus").
- Train shows no adult demand.

---

## 4. Current 6 sounds

### 4a. Measurements [1]

All files are AAC in M4A, stereo, with valid iTunSMPB gapless data (Apple encoder, 2112-sample
delay), which Media3 parses [59].

| Sound (file)                   | Len · Hz · kbps           | LUFS (I)  | LRA LU   | Max mom. − I | Peak dBFS       | Slope dB/oct | Energy <250 / 250–2k / 2–8k / >8k % | Rhythm: autocorr @ lag         | Tonal frames                      | Events >6 / >10 dB (max) | L/R corr | SII @ −18 LU |
|--------------------------------|---------------------------|-----------|----------|--------------|-----------------|--------------|-------------------------------------|--------------------------------|-----------------------------------|--------------------------|----------|--------------|
| Steady Rain (`calming_rain`)   | 57 s · 44.1k · 98         | −16.7     | 2.1      | 2.1          | 0.0 (9 clipped) | −2.0         | 12 / 41 / 33 / 15                   | 0.06 @ 0.9 s                   | 0%                                | 0 / 0 (1.4)              | 0.03     | 0.90         |
| Gentle Drizzle (`soft_rain`)   | 57 s · 44.1k · 94         | **−30.5** | 3.6      | 3.3          | 0.0 (1 clipped) | +1.8         | 0 / 14 / 58 / 28                    | 0.04 @ 0.8 s                   | 0%                                | 0 / 0 (3.7)              | 0.53     | 0.89         |
| Rain & Songbirds (`rain_bird`) | 57 s · 44.1k · 99         | −24.7     | 9.1      | **13.7**     | −5.0            | −1.5         | 28 / 18 / 51 / 4                    | 0.14 @ 0.9 s                   | 12% (≈2.5–3.5 kHz chirps)         | **9 / 5 (17.3)**         | 0.05     | 0.90         |
| Lush Forest (`forest`)         | 57 s · 44.1k · 98         | −18.0     | 4.9      | 5.9          | −5.2            | +2.6         | 0 / 0 / 72 / 27                     | 0.09 @ 1.0 s                   | **78%** (1.5–8 kHz, pitch varies) | 7 / 0 (8.8)              | 0.51     | 0.90         |
| Ocean Waves (`ocean`)          | 57 s · 48k · 94           | −18.5     | **10.5** | 8.1          | −1.1            | −3.1         | 10 / 67 / 20 / 3                    | 0.05; slow swell 0.21 @ 18.8 s | 0%                                | 4 / 0 (9.9)              | 0.82     | 0.92         |
| Night Train (`train`)          | **13.3 s** · **24k** · 94 | −18.1     | 1.2      | 3.1          | −1.5            | −8.3         | 27 / 72 / 0 / 0                     | **0.49 @ 2.0 s**               | n/a (nothing above 2 kHz)         | 0 / 0 (3.2)              | 0.12     | 0.99         |

**What the numbers mean:**

- **Gentle Drizzle is 14 LU quieter than Steady Rain.** At the app's default gains (0.60 vs 0.65),
  their playback levels are −34.9 and −20.4 LUFS, so the six beds span 14.5 LU [1].
- **Steady Rain is too loud at its default gain.** If the reciters' files sit around −16 LUFS (est.;
  not measured), Steady Rain at its default gain is only ~4 LU under the voice. That gives an SII of
  about 0.52, against 0.90 at −18 LU [1].
- **Steady Rain and Gentle Drizzle peak at 0 dBFS** and have clipped samples, so re-master them.
- **Train's loop seam has a −3 dB step every 13 s.**

### 4b. Rubric scores

| Sound              | A | B | C     | D | E     | F | Total  | Verdict                                                                                                                                                                      |
|--------------------|---|---|-------|---|-------|---|--------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Steady Rain**    | 2 | 2 | 2     | 2 | 2     | 2 | **12** | The ideal bed: steady, broadband, no events, wide stereo                                                                                                                     |
| **Gentle Drizzle** | 1 | 2 | 2     | 2 | 2     | 2 | **11** | Good. It's bright (58% of energy at 2–8 kHz), so keep it 18 LU under. Re-master it to the common loudness ("rintik hujan" has demand)                                        |
| **Ocean Waves**    | 2 | 2 | 1     | 1 | 2     | 2 | **10** | Slow swells (~19 s) aren't beats, but the LRA is large. Fine under recitation at −18 LU                                                                                      |
| Lush Forest        | 2 | 0 | 2     | 1 | 1     | 2 | 8      | Effectively a **birdsong** loop, and IslamQA #145931 names birds [22]. Retitle it honestly ("Hutan & Kicau Burung") and recommend "after"                                    |
| Rain & Songbirds   | 2 | 1 | 2     | 0 | 1     | 1 | 7      | 5 calls more than 10 dB above the rain in every loop: arousal risk, and they become a pattern you learn every 57 s (§8). Re-edit out the loudest calls, or recommend "after" |
| Night Train        | 2 | 2 | **0** | 2 | **0** | 0 | 6      | Beat-like clatter (the IslamQA "beats" concern [21]), no SEA demand, and a 13-s loop that is easy to spot [19]. **Retire it**                                                |

### 4c. Recommendation: 3 free, 3 ad-unlocked

- **Free (the "main" sounds): Steady Rain · Gentle Drizzle · Ocean Waves.**
    - These are the three best matches, so the free experience is the best one under recitation.
    - They cover every v1 pack default that `research_packs.md` sets: drizzle or steady rain for the
      bedtime sunnah, Ar-Rahman, grief and pregnancy packs; steady rain for baby; ocean for Penenang
      Hati.
    - Suggested names: Hujan Deras / Hujan Lebat · Gerimis / Hujan Renyai · Ombak Pantai.
- **Ad-unlocked: Lush Forest · Rain & Songbirds · Night Train**, as the owner's model requires.
    - Flag Night Train for **removal before launch** and give its slot to Kipas Angin (§6). If it
      stays, it is "after" only, never a default, and needs a scholar's view.
- **Fix in `research_packs.md`:**
    - Ayat Sakinah defaults to "forest after", which is now a locked sound. Switch it to Gerimis or
      Ombak.
    - Rule (judgment): **a pack's default ambient sound must be a free sound**, so a Quran pack
      never
      leads to an ad prompt.

---

## 5. Generated noise colors

The app is called *White Noise* Quran but has no noise. Procedural noise costs 0 KB in the APK and
has no events, tonality or rhythm.

| Color     | Spectrum                     | SNR (LU) for SII 0.75 / 0.90 | SII @ −18 LU | Demand [30][31]                                                        | Under recitation                                                                                                                                |
|-----------|------------------------------|------------------------------|--------------|------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| **Brown** | −6 dB/oct; 81% below 250 Hz  | 8.6 / 13.2                   | **0.99**     | 56.9M, 30.2M; "brown noise untuk belajar"                              | **Best** for intelligibility: its energy sits where speech importance is low. It sounds weak on phone speakers (judgment: little below ~200 Hz) |
| **Pink**  | −3 dB/oct (equal per octave) | 10.9 / 17.3                  | 0.91         | ID intent is mostly pro audio                                          | Good. Closest to rain (Steady Rain is −2.0 dB/oct)                                                                                              |
| **White** | 0 dB/oct; 60% above 8 kHz    | 9.5 / 17.7                   | 0.90         | **380M, 362M** ("white noise bayi"); MY "white noise for babies zikir" | OK at −18 LU, but hissy. It's the name users search, so offer it                                                                                |

**Recommendations:**

- **Defaults:** the same −18 LU under recitation as the other beds. Fade out after the recitation or
  timer ends; no all-night playback by default. Evidence for noise as a sleep aid is very low
  quality
  [18], and pink noise at 40–50 dBA cut REM sleep in a 2026 lab study [17] (earlier doc §3).
- **Implementation** (judgment, keeping the code small): generate a **≥ 60-s mono loop** on the
  device the first time it's unlocked. Crossfade the seam and save it to the cache as WAV (about
  5 MB per color). Then reuse the existing ExoPlayer `REPEAT_MODE_ALL` path. Noise that repeats in
  cycles is detectable up to about 20-s cycles [19], so 60 s leaves a margin.
- **Brown noise needs a ~20 Hz high-pass** so it doesn't drift and waste headroom. An unfiltered
  1/f²
  synthesis put nearly all its energy below the K-weighting range and measured −50.5 LUFS at
  −6 dBFS peak [1].
- **Monetization:** under the owner's rule all three are ad-unlocked. That clashes with the app's
  name, so see §11.

---

## 6. New sound shortlist

Scores for new sounds are **est.**: candidates were checked for license and metadata, not measured
or auditioned.

| Rank | Sound (ID / MS / EN)                                                | A | B | C | D | E | F | Total | Demand [30][31]                                      | Best source                 | Main risks                                                                                                             |
|------|---------------------------------------------------------------------|---|---|---|---|---|---|-------|------------------------------------------------------|-----------------------------|------------------------------------------------------------------------------------------------------------------------|
| 1    | White noise (generated)                                             | 2 | 2 | 2 | 2 | 2 | 2 | 12    | 380M / 362M                                          | generated                   | Hissy; baby use raises Families and safety issues                                                                      |
| 2    | **Kipas Angin / Bunyi Kipas / Electric Fan**                        | 2 | 1 | 2 | 2 | 2 | 2 | 11    | 124.7M, 51.1M; "pengantar tidur", "untuk bayi"       | **own recording**           | Motor hum (50 Hz mains harmonics) is tonal; an oscillating head makes a 10–20 s sweep; squeaks ("berdecit") are events |
| 3    | Brown noise (generated)                                             | 2 | 2 | 2 | 2 | 2 | 1 | 11    | 56.9M                                                | generated                   | Phone speakers                                                                                                         |
| 4    | Pink noise (generated)                                              | 2 | 2 | 2 | 2 | 2 | 1 | 11    | weak consumer intent in ID                           | generated                   | REM caution [17]                                                                                                       |
| 5    | **Air Terjun / Air Terjun / Waterfall**                             | 2 | 2 | 2 | 2 | 2 | 1 | 11    | 26.3M; MY "zikir bunyi air terjun"                   | CC0                         | Heavy low end (pick a distant falls); ID demand mixed with bird-keeping                                                |
| 6    | **Hujan di Atap Seng / Bunyi Hujan atas Zink / Rain on a Tin Roof** | 1 | 2 | 2 | 1 | 2 | 2 | 10    | 83.5M, 31.0M, 15.4M; ID "atap seng", MY "atas zink"  | **own recording** or CC0    | Metallic pings (bright), gutter drips, thunder                                                                         |
| 7    | Sungai / Bunyi Sungai / Stream                                      | 1 | 1 | 2 | 2 | 2 | 2 | 10    | 16.2M, 15.8M; "tanpa musik"; MY "zikir bunyi sungai" | CC0                         | Gurgles have pitch; birds appear in many recordings                                                                    |
| 8    | Hujan di Hutan / Hujan di Hutan / Rain in a Tropical Forest         | 2 | 1 | 2 | 1 | 2 | 2 | 10    | 50.4M; "… tanpa petir di hutan"                      | CC0 (edit)                  | Birds, cicadas, thunder, leaf drips                                                                                    |
| 9    | Hujan di Genteng / Hujan atas Genting / Rain on Roof Tiles          | 2 | 2 | 2 | 1 | 2 | 1 | 10    | strong YT completions, partly the kids' song         | own recording or CC0 (edit) | Traffic and drips                                                                                                      |
| 10   | Hujan di Tenda / Hujan atas Khemah / Rain on a Tent                 | 2 | 2 | 2 | 1 | 2 | 1 | 10    | 25.8M; camping is less local                         | CC0                         | Many recordings include thunder                                                                                        |
| 11   | Angin Sepoi / Angin Lembut / Gentle Breeze                          | 2 | 1 | 2 | 1 | 1 | 1 | 8     | weak                                                 | CC0                         | Whistling, gusts, wind's mixed associations [24][25]                                                                   |
| 12   | Malam di Sawah / Cengkerik & Katak / Night Crickets & Frogs         | 1 | 0 | 1 | 1 | 1 | 2 | 6     | 14.8M, 13.1M; "jangkrik dan katak di sawah"          | own recording               | Chirps are tonal (≈3–5 kHz) and periodic; croaks are events; omen and "seram" associations. **"After" only**           |

**Not shortlisted:**

- **Gate:** thunder; birdsong-led ("kicau burung"); café or crowd; bells, chimes, bowls and gongs;
  the adhan; heartbeat, clock or train; tokek, dogs, roosters and donkeys; humming, lullabies and
  "shhh"; binaural tones.
- **Low value:**
    - AC hum (troubleshooting intent);
    - campfire (crackle events, mixed fire symbolism);
    - hair dryer or vacuum: baby-targeted, so Families risk. Revisit with the baby pack.

### 6a. Candidate recordings (Freesound; license read on each sound's page, 2026-10-03) [34]

All are **CC0**: no attribution needed. Credit them anyway, and save a copy of the page as it looked
on the day you downloaded.

| Sound                      | ID · author · length                                                                                                                                                                                                                                                                               | Notes                                                                                                                                   |
|----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| Tin roof                   | [726357](https://freesound.org/people/lastraindrop/sounds/726357/) lastraindrop, 10 min, 48 kHz · [577507](https://freesound.org/people/Simon%20Spiers/sounds/577507/) Simon Spiers, 140 min (MP3 source) · [521773](https://freesound.org/people/MrFossy/sounds/521773/) MrFossy, 1.6 min, 96 kHz | 726357: iron-sheet house, louder first half. 577507: a cycle shelter at Gatwick; listen for aircraft. **Avoid 607074 (tagged thunder)** |
| Fan                        | [170870](https://freesound.org/people/unfa/sounds/170870/) unfa, 1 min · [455671](https://freesound.org/people/kyles/sounds/455671/) kyles, 0.7 min · [336387](https://freesound.org/people/moai15/sounds/336387/) moai15, 0.7 min                                                                 | All short; 170870 is close-miked and bassy. **Record your own**                                                                         |
| Waterfall                  | [458711](https://freesound.org/people/Fabrizio84/sounds/458711/) Fabrizio84, 6.3 min · [321886](https://freesound.org/people/nsmusic/sounds/321886/) nsmusic, 1.1 min                                                                                                                              | —                                                                                                                                       |
| Stream                     | [446019](https://freesound.org/people/BurghRecords/sounds/446019/) BurghRecords, 1.1 min, 96 kHz · [433589](https://freesound.org/people/jackthemurray/sounds/433589/) jackthemurray, 1.1 min · [469009](https://freesound.org/people/INNORECORDS/sounds/469009/) INNORECORDS, 5 min               | 469009 is tagged birds and its text makes health claims. Don't reuse the text                                                           |
| Rain in tropical forest    | [615747](https://freesound.org/people/nyoz/sounds/615747/) nyoz, 1 min (Réunion) · [615001](https://freesound.org/people/absent1010/sounds/615001/) absent1010, 1.7 min (Mulu, **Sarawak**) · [135821](https://freesound.org/people/cybergenic/sounds/135821/) cybergenic, 19.8 min                | 615001 has frogs and insects. Cut the thunder out of 135821                                                                             |
| Roof tiles                 | [466241](https://freesound.org/people/richwise/sounds/466241/) richwise, 8.8 min                                                                                                                                                                                                                   | Traffic, gusts and a plane pass; edit them out                                                                                          |
| Tent                       | [251233](https://freesound.org/people/pulswelle/sounds/251233/) pulswelle, 4.2 min · [484723](https://freesound.org/people/Breviceps/sounds/484723/) Breviceps, 1.4 min                                                                                                                            | Avoid 397916 (thunder)                                                                                                                  |
| Breeze                     | [181801](https://freesound.org/people/keweldog/sounds/181801/) keweldog, 1.1 min                                                                                                                                                                                                                   | Pines                                                                                                                                   |
| Night crickets/frogs       | [615011](https://freesound.org/people/absent1010/sounds/615011/) absent1010, 2.1 min (**Sarawak**) · [333221](https://freesound.org/people/hdfreema/sounds/333221/) hdfreema, 3.7 min · [32655](https://freesound.org/people/greysound/sounds/32655/) greysound, 1.4 min                           | For "after" only                                                                                                                        |
| Ocean (longer alternative) | [339517](https://freesound.org/people/pulswelle/sounds/339517/) pulswelle, 26 min · [463250](https://freesound.org/people/DylanTheFish/sounds/463250/) DylanTheFish, 5.9 min                                                                                                                       | For a 2–3 min re-loop                                                                                                                   |
| Malaysian rain             | [592485](https://freesound.org/people/Azrai93/sounds/592485/) Azrai93, 5.5 min (Ipoh)                                                                                                                                                                                                              | Tagged thunder; audition it                                                                                                             |

### 6b. Library terms for a free, ad-supported app

| Source                                                         | Commercial use?                             | Attribution?   | Can it be the main content of a sound app?                                                                                                                     | Verdict                |
|----------------------------------------------------------------|---------------------------------------------|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| Freesound **CC0** [32]                                         | yes                                         | no             | yes                                                                                                                                                            | **Use**                |
| Freesound **CC-BY 4.0** [32]                                   | yes                                         | yes            | yes, with credits in the app and listing                                                                                                                       | OK with credits        |
| Freesound **CC-BY-NC** [32][33]                                | **no**                                      | —              | no. CC defines NonCommercial as not primarily aimed at commercial advantage or payment, and an ad-supported app earns money                                    | **Unusable**           |
| Pixabay Content License (terms updated 18 Nov 2024) [35]       | yes                                         | no             | **No, if unmodified.** You can't distribute content "on a Standalone basis": substantially unchanged, where even filters or resizing still count as standalone | Avoid as a loop source |
| Mixkit Sound Effects Free License [36]                         | yes                                         | no             | **No.** The end product must be "larger in scope and different in nature"; no redistributing the item on its own or in a tool                                  | Avoid                  |
| ZapSplat Standard (8 May 2026) [37]                            | yes                                         | free tier: yes | **No.** Sounds must not be the "primary value of a product"; relaxation videos and soundboards are banned                                                      | Avoid                  |
| Sonniss GDC bundles (license v2.0, effective 27 Aug 2026) [38] | yes                                         | no             | **Not without written permission.** You may not supply the sounds to others as sound effects                                                                   | Ask, or avoid          |
| BBC Sound Effects (RemArc) [39]                                | **no** (personal, education, research only) | —              | No. It also bars ads next to BBC content; commercial licences go through Pro Sound Effects                                                                     | Avoid, or buy via PSE  |

### 6c. Field recording in Indonesia (better for fan, tin roof, roof tiles, rice-field nights)

Why: it's authentic, you own the copyright, and you can say "direkam di Indonesia". Tips (judgment):

- Record **≥ 20 min** continuously at 48 kHz / 24-bit. A dedicated recorder beats a phone; if you
  use
  a phone, put it in airplane mode.
- **Keep out:** mosque loudspeakers (record away from prayer times), motorbikes, roosters, dogs,
  tokek, voices and thunder.
- **Fan:** non-oscillating, low speed, mic 1–2 m away and out of the airflow.
- **Tin roof:** record indoors under the roof in the rainy season (Nov–Mar), away from gutters.

---

## 7. Mixing guidance

1. **Normalize every loop to −23 LUFS integrated, true peak ≤ −1 dBTP** (EBU R 128 target [4]).
    - Measure each reciter's files once, and store a loudness offset per reciter.
    - AES streaming guidance puts speech at about −18 LUFS (secondary [8]).
2. **Default bed: recitation −18 LU.**
    - That gives a simplified SII of 0.89–0.92 for the bundled natural beds [1].
    - It's above the ≥15 LU that normal-hearing natives preferred for ambience [7], with margin for
      non-native listeners (+1–7 dB [9]).
3. **Slider range while recitation plays: −40 to −10 LU.**
    - At −10 LU, SII falls to 0.71–0.80; at −4 LU it is 0.51–0.78 [1].
    - "Never louder than the recitation" (earlier doc rule 2) is far too loose.
4. **Mixing several sounds:**
    - uncorrelated beds add in power: two equal beds are +3 dB, three are +4.8 dB;
    - scale each bed by −10·log₁₀(n) dB so the mix stays at the target.
5. **Ducking (judgment):**
    - keep a **static** offset while recitation plays, and don't pump the bed up in the pauses
      between ayat, which draws attention to it;
    - use 1–2 s ramps when the recitation starts or stops;
    - in "after" mode, keep the same absolute level (no jump), then fade over 20–30 min as
      `research_packs.md` sets.
6. **Safe volume:**
    - WHO–ITU: 80 dB for 40 h/week (adults), 75 dB (children) [61];
    - infant sleep machines exceeded 50 dBA at 30 cm [62];
    - the app can't know the sound pressure level, so keep beds relative to the recitation, add a
      volume cap for the baby pack, and don't play all night by default [17].

---

## 8. Loop engineering and asset specs

| Topic             | Spec                                                                                                                                                                                                                                                                                                                                                                                          |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Loop length       | <ul><li>Naive listeners spot periodic noise up to **~20-s** cycles; most of the decline is between 5 and 10 s [19].</li><li>Listeners learn repeated noise quickly and remember it for weeks [20].</li><li>So: **≥ 60 s** for steady beds (the current 57-s rains are borderline OK) and **2–5 min** for textured beds (waves, stream, night).</li><li>Train's 13.3 s is too short.</li></ul> |
| Salient events    | One bird call, splash or car in a loop becomes a "clock" that ticks once per cycle: a learnable rhythm. Remove events more than 6 dB above the bed, or space them more than one loop apart                                                                                                                                                                                                    |
| Seam              | <ul><li>Cut in steady passages and **equal-power crossfade over 2–5 s** (judgment, standard practice).</li><li>Check: short-term loudness within ±0.5 LU across the seam.</li><li>The current seams change level by +1.5 to −3.0 dB [1].</li></ul>                                                                                                                                            |
| Loudness          | −23 LUFS integrated, ≤ −1 dBTP [4]; LRA ≤ 5 LU for "under" beds                                                                                                                                                                                                                                                                                                                               |
| Format            | AAC-LC in M4A with iTunSMPB. Media3 trims encoder delay and padding from that tag [59], and the current files already carry it. Keep 44.1/48 kHz: the train's 24 kHz rate cuts everything above 12 kHz. Ogg/Opus is supported [60] but would need its own gapless-loop test                                                                                                                   |
| Bitrate, channels | 96–128 kbps **stereo** for diffuse beds (rain, stream, forest: Steady Rain's L/R correlation is 0.03). 64 kbps **mono** for fan or near-mono sources                                                                                                                                                                                                                                          |
| Size budget       | <ul><li>**Bundled:** the 3 free sounds, about 3–6 MB at 2–3 min each.</li><li>**Premium:** download each sound when it's unlocked (you're online for the ad anyway), about 1.5–3 MB each.</li><li>**Noise colors:** generated, 0 KB in the APK.</li></ul>                                                                                                                                     |

---

## 9. Rewarded-ad unlock

### 9a. AdMob rules that bind the design

- **Before every ad**, state what the user does and what they get, and serve it only after an
  opt-in tap. Users must be able to skip without losing normal use: free sounds and all
  recitation keep working [40].
- **Rewards:** deliver the promised reward on completion; no cash-equivalent rewards [40]. The
  reward is granted at the skip time, up to 30 s [41].
- **Loading:** loaded rewarded ads **expire after one hour** [46], so load one only when the user
  opens a locked sound, not at app start.
- **Format:** standard opt-in rewarded ads only; avoid "rewarded interstitial" (no tap to start).
- **Play Ads policy [50]:** opted-in rewarded ads are exempt from the ban on unexpected full-screen
  ads; no ads on the lock screen or media notification; ads must suit the app's content rating.

### 9b. Ad settings

| Setting                   | Recommendation                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|---------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Sensitive categories [42] | <ul><li>Standard categories are **allowed by default**. Block: Dating, Reference to Sex, Sexual & Reproductive Health, Birth Control, Social Casino Games, Get Rich Quick, Religion (other faiths' or sectarian advocacy), Politics, Sensationalism. Weight Loss, Cosmetic Procedures and Drugs & Supplements are a judgment call.</li><li>**Restricted** categories (Gambling & Betting, Alcohol) are blocked by default. Keep them blocked.</li><li>Astrology & Esoteric and Consumer Loans are marked **deprecated**, so you can't rely on them. Use advertiser-URL blocks or the Ad review center for fortune-telling and online-loan ("pinjol") ads; riba is a concern for this audience.</li></ul> |
| Where [43]                | App level: Apps → your app → Blocking controls (general and sensitive categories). Account level: Blocking controls in the sidebar (advertiser URLs, Ad review center, ad networks, ad content rating)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| Content rating [45]       | `setMaxAdContentRating(MAX_AD_CONTENT_RATING_G)`, or PG                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| Volume [44]               | `MobileAds.setAppVolume(0.0–1.0)` tells the SDK the app's relative volume for video ads (app open, banner, interstitial, rewarded). `setAppMuted(true)` tells it the app is muted. Muted or 0 means video ads that can't play muted are **not returned**, so the ad pool is smaller. **Recommendation:** mute the ads, which also keeps music out of a Quran app at bedtime; fall back to `setAppVolume(0.2–0.3)` if too few ads fill                                                                                                                                                                                                                                                                    |
| Playback                  | Pause recitation and beds before the ad, then resume at the same ayah (judgment)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |

### 9c. Play Console, privacy and Families

- **Ads declaration:** SDK-served rewarded ads count as ads, so the listing shows "Contains ads";
  misdeclaring can get the app suspended [48].
- **Data safety:** the Google Mobile Ads SDK collects **and shares** IP address (approximate
  location), app interactions, diagnostics, and device/account IDs (advertising ID, app set ID),
  for advertising, analytics and fraud prevention, encrypted in transit [47]. The earlier doc's
  "no data collected" no longer holds, and the app needs a privacy policy.
- **Advertising ID:** apps targeting Android 13+ need the `AD_ID` permission (the Ads SDK merges
  it in); without it the ID reads as zeros [49]. Fill in the advertising-ID declaration. Removing
  `AD_ID` means non-personalized ads only (judgment: lower revenue, more privacy; earlier doc §2a).
- **Families** (only if the audience includes children; see the baby pack in `research_packs.md`):
  only self-certified ad SDKs (AdMob qualifies with `play-services-ads` 19.0.0+ [52]); no
  interest-based ads or remarketing to children; child-appropriate ads; rewarded ads closeable
  after 5 s; a neutral age screen for mixed audiences [51]. **Recommendation:** target adults
  (18+) and use no child-appealing artwork.

### 9d. Unlock duration

| Option                              | Ads per active user                | Bedtime friction                                  | Offline                   | Precedent                                                                  |
|-------------------------------------|------------------------------------|---------------------------------------------------|---------------------------|----------------------------------------------------------------------------|
| Per play                            | ≥ 1 every night                    | **High**: a bright, loud video right before sleep | Fails every night         | none found in sleep apps                                                   |
| Until morning / 24 h                | about 1 per night                  | High                                              | Fails if offline at night | "White Noise: Sleep Sound Mix": every sound open until morning per ad [54] |
| 3 days                              | 2–3 a week                         | Medium                                            | Survives short gaps       | "Sleep like a Baby": premium for 3 days per video [55]                     |
| **7 days, per sound (recommended)** | about 1 a week per favourite sound | Low: unlock in the daytime, use at night          | Survives a week           | —                                                                          |
| Permanent                           | 1 per sound, ever                  | None                                              | Fine                      | Common as a *paid* unlock (earlier doc §1)                                 |

Other ad-unlock apps say only "watch ads" without a duration [56].

**Recommended design:**

- **One ad unlocks one sound for 7 days.**
- Show the unlock as a plain date: "Terbuka sampai 10 Okt" (open until 10 Oct). No countdown
  badges, no "unlocked 5 sounds" counters, no push nags.
- **Never expire mid-session.** Let the user renew any time.
- Offer a **30–60 s preview** without an ad (Veil previews premium sounds for a minute [56]).
- If revenue is too thin after launch, test 3 days. Never go per-play.

### 9e. Bedtime risks and mitigations

| Risk                                    | Evidence                                                                                                                                                                       | Mitigation                                                                                                                                                                                                                         |
|-----------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Bright screen before sleep              | Reading on a light-emitting e-reader for ~4 h before bed suppressed melatonin and delayed sleep [58]. A 30-s ad is far shorter, and the effect of brief exposure is unverified | Daytime unlocking, 7-day windows, a dark UI; MY users already look for "black screen" videos [30]                                                                                                                                  |
| Loud audio, or music inside a Quran app | Ad audio follows the SDK settings [44]                                                                                                                                         | Mute the ads; pause the recitation first; never show an ad on the recitation screen                                                                                                                                                |
| Arousing ad content                     | —                                                                                                                                                                              | Category blocks and rating G (§9b)                                                                                                                                                                                                 |
| Offline at night                        | Ads need a network, and loaded ads expire in an hour [46]                                                                                                                      | Store unlocks locally and keep them valid offline. Offline prompt (judgment): "Butuh internet untuk membuka suara ini; 3 suara gratis tetap tersedia" (internet is needed to unlock this sound; 3 free sounds are still available) |
| An unlock lapses mid-session            | —                                                                                                                                                                              | Never cut a playing sound; it expires at the next stop                                                                                                                                                                             |

### 9f. What the listing may say (Play Metadata policy [53])

- **Title, icon and developer name:** no price or promotion text (earlier doc §7).
- **May say, in the description:**
    - "Murottal tidak pernah disela iklan" (recitation is never interrupted by ads);
  - "Iklan hanya muncul jika Anda memilih menonton untuk membuka suara tambahan"
    (ads appear only if you choose to watch one to unlock an extra sound);
    - "Tidak ada iklan banner atau pop-up" (no banner or pop-up ads; true if that holds);
    - "Suara hujan tanpa petir" (rain without thunder);
    - Malay: "Bacaan al-Quran tidak pernah diganggu iklan";
    - English: "Recitation is never interrupted by ads."
- **Must not say:** "tanpa iklan" or "bebas iklan" (the app shows "Contains ads" [48]);
  "bikin tidur nyenyak", "terapi", "insomnia" (health claims; earlier doc §7); anonymous
  testimonials [53].
- **Unlock prompt** (it must state the action and the reward [40]):
    - Indonesian: "Tonton 1 iklan singkat untuk membuka *Kipas Angin* selama 7 hari."
    - Malay: "Tonton 1 iklan pendek untuk membuka *Bunyi Kipas* selama 7 hari."
    - English: "Watch 1 short ad to unlock *Electric Fan* for 7 days."

### 9g. eCPM

- **Indonesia/Malaysia rewarded eCPM: unverified.** Appodeal, AnyMind, Tenjin and an Indonesian
  search (4 attempts) gave no public country figures. For scale only: the US games average for
  rewarded ads was $30.25 in Q2 2024 (secondary [57]); SEA is likely far lower (unverified).
- **Arithmetic:** one unlock = one impression, so revenue = eCPM × unlocks ÷ 1,000. Example:
  1,000 monthly users × 4 unlocks at a hypothetical $2 eCPM ≈ $8 a month. Treat rewarded ads as
  "support", and read real fill and eCPM from AdMob after a soft launch.

---

## 10. Recommended launch catalog

| #  | ID / MS / EN                                               | Source                                          | Access     | Notes                                       |
|----|------------------------------------------------------------|-------------------------------------------------|------------|---------------------------------------------|
| 1  | Hujan Deras / Hujan Lebat / Steady Rain                    | bundled (`calming_rain`), re-mastered           | **Free**   | "tanpa petir"; default for bedtime and baby |
| 2  | Gerimis / Hujan Renyai / Gentle Drizzle                    | bundled (`soft_rain`), re-mastered (+14 LU)     | **Free**   | Default for several packs                   |
| 3  | Ombak Pantai / Ombak Pantai / Ocean Waves                  | bundled (`ocean`)                               | **Free**   | Consider a 2–3 min re-loop                  |
| 4  | White Noise                                                | generated                                       | Ad, 7 days | 0 KB                                        |
| 5  | Pink Noise                                                 | generated                                       | Ad, 7 days | 0 KB                                        |
| 6  | Brown Noise                                                | generated                                       | Ad, 7 days | 0 KB; best under recitation                 |
| 7  | Kipas Angin / Bunyi Kipas / Electric Fan                   | **own recording**                               | Ad, 7 days | Replaces Night Train                        |
| 8  | Hujan di Atap Seng / Hujan atas Zink / Rain on a Tin Roof  | own recording, or CC0 726357 / 577507           | Ad, 7 days | Download on unlock                          |
| 9  | Air Terjun / Air Terjun / Waterfall                        | CC0 458711                                      | Ad, 7 days | Download on unlock                          |
| 10 | Hutan & Kicau Burung / Hutan & Kicauan / Forest & Birdsong | bundled (`forest`), retitled                    | Ad, 7 days | Best "after"                                |
| 11 | Hujan & Kicau Burung / Hujan & Kicauan / Rain & Songbirds  | bundled (`rain_bird`), loudest calls edited out | Ad, 7 days | Best "after"                                |
| —  | Night Train                                                | —                                               | **Retire** | Rhythm concern, no demand, 13-s loop        |

**Later (v1.1+):**

- Sungai / Stream;
- Hujan di Hutan / Rain in a Tropical Forest;
- Hujan di Genteng / Rain on Roof Tiles;
- Hujan di Tenda / Rain on a Tent;
- Angin Sepoi / Gentle Breeze;
- Malam di Sawah / Rice-field Night: own recording, "after" only;
- a 3–5 min re-recording of Steady Rain.

**Pack defaults:** use only sounds 1–3 (§4c).

---

## 11. Open questions / unverified

1. **Reciter loudness** for each of the six equran.id reciters wasn't measured (no audio was
   downloaded). You need it to set the −18 LU offset per reciter. The −4 LU figure for Steady Rain
   in §4a is an estimate.
2. **Scholar review:** does the train's clatter fall under the IslamQA "beats" reasoning [21]? Is a
   birdsong loop acceptable "after" the recitation? Still no SEA fatwa on nature sounds behind
   recitation (2 searches; earlier doc §4b).
3. **Rewarded eCPM in Indonesia/Malaysia:** unverified (4 attempts). The AES TD1008 speech target is
   secondary [8]; the AES site blocked access. The MUI 56/2016 ruling is secondary [28]. The SII
   0.75/0.45 bands are secondary [6].
4. **Noise colors behind an ad** follow the owner's rule, but the app's name promises white noise.
   Making one color free (white or pink) is the owner's call.
5. **Candidate recordings were not auditioned**; only metadata and licenses were checked. CC0 can't
   be revoked, but keep evidence of the license at download.
6. **Commercial status with equran.id and Islamic Network:** ads make the app commercial. Earlier
   doc §5 already advised asking equran.id for written permission; mention the ads (on ambient
   unlocks only) when you ask.
7. **Brown noise on phone speakers** (judgment that it sounds weak): test on low-end Oppo, Xiaomi
   and Samsung phones.
8. **A one-time "unlock all" purchase** (earlier doc §6) could sit beside the rewarded ads later.
9. **Ayat Sakinah's default** in `research_packs.md` should change from forest to a free sound (
   §4c).

---

## 12. Sources

1. Author's measurements on 2026-10-03 of `app/src/main/res/raw/*.m4a` and of synthesized
   white/pink/brown noise. Tools: `afinfo`; Python/numpy implementing ITU-R BS.1770 K-weighting with
   libebur128 coefficients, EBU Tech 3342 LRA, and the ANSI S3.5 ⅓-octave SII. Local computation; no
   URL.
2. ITU-R BS.1770 — Algorithms to measure audio programme loudness and true-peak audio
   level. https://www.itu.int/rec/R-REC-BS.1770
3. EBU Tech 3342 — Loudness Range. https://tech.ebu.ch/publications/tech3342
4. EBU R 128 — Loudness normalisation and permitted maximum level (v5.0, Nov 2023; −23
   LUFS). https://tech.ebu.ch/publications/r128 (accessed 2026-10-03)
5. ANSI/ASA S3.5-1997 (R2017), Methods for Calculation of the Speech Intelligibility Index. Table 3
   constants were read from the CRAN `SII` package data (
   `data/onethird.rda`). https://github.com/cran/SII · https://rdrr.io/rforge/SII/man/critical.html
6. secondary: MetricGate — Speech Intelligibility Index (interpretation
   bands). https://metricgate.com/docs/speech-intelligibility-index-sii/
7. Torcoli M, Freke-Morin A, Paulus J, Simon C, Shirley B. Preferred levels for background ducking
   to produce esthetically pleasing audio for TV with clear speech. J Audio Eng Soc. 2019;67(12):
   1003–1011. https://cris.fau.de/publications/234640600
8. secondary: Production Advice — summary of AES TD1008 (speech ≈ −18 LUFS, music ≈ −16
   LUFS). https://productionadvice.co.uk/td1008/
9. van Wijngaarden SJ, Steeneken HJ, Houtgast T. Quantifying the intelligibility of speech in noise
   for non-native listeners. J Acoust Soc Am. 2002;111(4):
   1906–16. https://doi.org/10.1121/1.1456928 (PMID 12002873)
10. Brungart DS, Simpson BD, Ericson MA, Scott KR. Informational and energetic masking effects in
    the perception of multiple simultaneous talkers. J Acoust Soc Am. 2001;110(5):
    2527–38. https://doi.org/10.1121/1.1408946 (PMID 11757942)
11. Tremblay S, Macken WJ, Jones DM. The impact of broadband noise on serial memory: changes in
    band-pass frequency increase disruption. Memory. 2001;9(4–6):
    323–31. https://doi.org/10.1080/09658210143000010 (PMID 11594355)
12. Rhebergen KS, Versfeld NJ, Dreschler WA. Extended speech intelligibility index for the
    prediction of the speech reception threshold in fluctuating noise. J Acoust Soc Am. 2006;120(6):
    3988–97. https://doi.org/10.1121/1.2358008 (PMID 17225425)
13. Stanchina ML, Abu-Hijleh M, Chaudhry BK, Carlisle CC, Millman RP. The influence of white noise
    on sleep in subjects exposed to ICU noise. Sleep Med. 2005;6(5):
    423–8. https://doi.org/10.1016/j.sleep.2004.12.004 (PMID 16139772)
14. Buxton OM, Ellenbogen JM, Wang W, et al. Sleep disruption due to hospital noises: a prospective
    evaluation. Ann Intern Med. 2012;157(3):
    170–9. https://doi.org/10.7326/0003-4819-157-3-201208070-00472 (PMID 22868834)
15. Basner M, Müller U, Elmenhorst EM. Single and combined effects of air, road, and rail traffic
    noise on sleep and recuperation. Sleep. 2011;34(1):
    11–23. https://doi.org/10.1093/sleep/34.1.11 (PMID 21203365)
16. Dang-Vu TT, McKinney SM, Buxton OM, Solet JM, Ellenbogen JM. Spontaneous brain rhythms predict
    sleep stability in the face of noise. Curr Biol. 2010;20(15):
    R626–7. https://doi.org/10.1016/j.cub.2010.06.032 (PMID 20692606)
17. Basner M, Smith MG, Cordoza M, et al. Efficacy of pink noise and earplugs for mitigating the
    effects of intermittent environmental noise exposure on sleep. Sleep.
    2026;zsag001. https://doi.org/10.1093/sleep/zsag001 (PMID 41627391; earlier doc [25])
18. Riedy SM, Smith MG, Rocha S, Basner M. Noise as a sleep aid: a systematic review. Sleep Med Rev.
    2021;55:101385. https://doi.org/10.1016/j.smrv.2020.101385 (earlier doc [24])
19. Kaernbach C. The memory of noise. Exp Psychol. 2004;51(4):
    240–8. https://doi.org/10.1027/1618-3169.51.4.240 (PMID 15620225)
20. Agus TR, Thorpe SJ, Pressnitzer D. Rapid formation of robust auditory memories: insights from
    noise. Neuron. 2010;66(4):610–8. https://doi.org/10.1016/j.neuron.2010.04.014 (PMID 20510864)
21. IslamQA — #229732 Ruling on vocal effects in da'wah productions (3 Apr
    2025). https://islamqa.info/en/answers/229732 (accessed 2026-10-03)
22. IslamQA (Arabic) — #145931, on bird, wind and music sounds as a background to recitation (28 Mar
    2010). https://islamqa.info/ar/answers/145931 (accessed 2026-10-03)
23. Islamweb — Fatwa #142023, Reciting the Quran during a thunderstorm (2 Nov
    2010). https://islamweb.net/en/fatwa/142023/reciting-the-quran-during-a-thunderstorm
24. Sahih Muslim (sunnah.com
    numbering): [377](https://sunnah.com/muslim:377) · [899](https://sunnah.com/muslim:899) · [2113](https://sunnah.com/muslim:2113) · [2114](https://sunnah.com/muslim:2114).
    Texts verified against the hadith-api dataset (Abdul-Baqi numbers 377, 899.03, 2113.01,
    2114). https://github.com/fawazahmed0/hadith-api
25. Sahih
    al-Bukhari [611](https://sunnah.com/bukhari:611) · [1032](https://sunnah.com/bukhari:1032) · [3303](https://sunnah.com/bukhari:3303) · [3322](https://sunnah.com/bukhari:3322);
    Sunan Abi Dawud [5097](https://sunnah.com/abudawud:5097) (graded sahih in the dataset). Verified
    via hadith-api (as [24]).
26. Quran 13:13 https://quran.com/13/13 · 31:19 https://quran.com/31/19
27. Pejabat Mufti Wilayah Persekutuan — Irsyad al-Fatwa Series 59: Quran recitation and adhan as
    ringtone or screen saver (31 Jul 2015), citing the 77th National Fatwa Muzakarah (10–12 Apr
    2007). Read via the Internet Archive copy
    of https://www.muftiwp.gov.my/en/artikel/irsyad-fatwa/irsyad-fatwa-umum-cat/1901-the-ruling-of-the-usage-of-quran-recitation-and-adhan-as-ringtone-or-screen-saver
28. secondary: analysis of MUI Fatwa No. 56/2016 on using non-Muslim religious attributes (Journal
    of Islamic Family Law, UIN Malang). https://urj.uin-malang.ac.id/index.php/jfs/article/view/199
29. Gould van Praag CD, Garfinkel SN, Sparasci O, et al. Mind-wandering and alterations to default
    mode network connectivity when listening to naturalistic versus artificial sounds. Sci Rep.
    2017;7:45273. https://doi.org/10.1038/srep45273 (earlier doc [65])
30. secondary signal: Google and YouTube autocomplete (suggestqueries.google.com, `client=firefox`,
    `ds=yt`; `hl=id&gl=id`, `hl=ms&gl=my`, plus some EN), queried 2026-10-03. Shows popular queries,
    not volumes.
    Example: https://suggestqueries.google.com/complete/search?client=firefox&ds=yt&hl=id&gl=id&q=suara%20hujan%20tanpa
31. YouTube search results, views as displayed on 2026-10-03 (`hl=id&gl=ID`, `hl=ms&gl=MY`). Video
    IDs: C5Gm8UvxKlU (124.7M) · qorkD6nPYQM (51.1M) · 1msm0-rXlo4 · KfYkzXTut1Y (83.5M) ·
    seoeYYzjGkw · s61TmfE3zY4 · MO1wJTMuHXg · q76bMs-NwRk (248.8M) · 74b3Zb18UDM · ih4_1FyVjaY ·
    g1w3IT5WnYw · mik6yI0SSpw · I3CHLiMYGic · HsQlpaYTE0s · IvjMgVS6kng · HAzZH6wccew ·
    HchoJcYNYlU · bn9F19Hi1Lk · vPhg6sc1Mk4 · -N9rb2QDqrw · -WrD5DwI6_4 · qsOUv9EzKsg ·
    nMfPqeZjc2c (380.0M) · oewj_XEM1js (362.5M) · RqzGzwTY-6w · Q6MemVxEquE · N5cD4opQLi8 ·
    bIg3j266IL8 · jrFI71IonJ0. Watch URLs: `https://www.youtube.com/watch?v=<ID>`
32. Freesound — Help/FAQ (licenses: CC0, CC-BY 4.0, CC-BY-NC 4.0, legacy
    Sampling+). https://freesound.org/help/faq/
33. Creative Commons — Attribution-NonCommercial 4.0 legal code, §1 (NonCommercial
    definition). https://creativecommons.org/licenses/by-nc/4.0/legalcode.en
34. Freesound sound pages listed in §6a, license link read on each page on 2026-10-03 (all point to
    creativecommons.org/publicdomain/zero/1.0). Pattern: `https://freesound.org/s/<ID>/`
35. Pixabay — Terms of Service / Content License (updated 18 Nov 2024) and license
    summary. https://pixabay.com/service/terms/ · https://pixabay.com/service/license-summary/
36. Mixkit — Sound Effects Free License (license modal). https://mixkit.co/license/#sfxFree (read
    2026-10-03)
37. ZapSplat — Standard License Agreement (last updated 8 May
    2026). https://www.zapsplat.com/license-type/standard-license/
38. Sonniss — #GameAudioGDC Bundle License v2.0 (effective 27 Aug 2026) and bundle
    page. https://sonniss.com/gdc-bundle-license/ · https://sonniss.com/gameaudiogdc
39. BBC Sound Effects — The BBC's Content Licence for
    RemArc. https://sound-effects.bbcrewind.co.uk/licensing
40. Google AdMob Help — Policies for ad units that offer
    rewards. https://support.google.com/admob/answer/7313578
41. Google AdMob Help — Create a rewarded ad unit. https://support.google.com/admob/answer/7311747
42. Google AdMob Help — List of sensitive
    categories. https://support.google.com/admob/answer/3150953
43. Google AdMob Help — Blocking controls. https://support.google.com/admob/answer/3150235
44. Google Mobile Ads SDK (Android) — Global settings: video ad volume
    control. https://developers.google.com/admob/android/global-settings
45. Google Mobile Ads SDK (Android) — Targeting (max ad content rating, child-directed
    treatment). https://developers.google.com/admob/android/targeting
46. Google Mobile Ads SDK (Android) — Rewarded ads (ads expire after an
    hour). https://developers.google.com/admob/android/rewarded
47. Google Mobile Ads SDK (Android) — Prepare for Google Play's Data safety
    section. https://developers.google.com/admob/android/privacy/play-data-disclosure
48. Play Console Help — Prepare your app for review (Ads declaration, "Contains
    ads"). https://support.google.com/googleplay/android-developer/answer/9859455
49. Play Console Help — Advertising
    ID. https://support.google.com/googleplay/android-developer/answer/6048248
50. Google Play Developer Policy —
    Ads. https://support.google.com/googleplay/android-developer/answer/9857753
51. Google Play Developer Policy — Families policies (ads and
    monetization). https://support.google.com/googleplay/android-developer/answer/9893335
52. Play Console Help — Families Self-Certified Ads SDK
    Program. https://support.google.com/googleplay/android-developer/answer/9283445
53. Google Play Developer Policy —
    Metadata. https://support.google.com/googleplay/android-developer/answer/9898842
54. App Store (Malaysia) — White Noise: Sleep Sound Mix (Karya Boyraz),
    description. https://apps.apple.com/my/app/id6767335946 (accessed 2026-10-03)
55. App Store (Indonesia) — Sleep like a Baby: White Noise App (Secret Box),
    description. https://apps.apple.com/id/app/id1239102281 (accessed 2026-10-03)
56. Google Play listings (gl=ID, accessed
    2026-10-03): https://play.google.com/store/apps/details?id=com.leanderoid.oceansoundsforsleep ("
    additional sounds… by watching ads", no
    duration) · https://play.google.com/store/apps/details?id=vn.fighttech.whitenoise (reward videos
    on tap) · https://play.google.com/store/apps/details?id=com.veilapp.sleep (1-minute premium
    preview).
57. secondary: GameDev Reports — summary of Tenjin "Ad Monetization in Mobile Games" Q2
    2024. https://gamedevreports.substack.com/p/tenjin-ad-monetization-in-mobile-00b
58. Chang AM, Aeschbach D, Duffy JF, Czeisler CA. Evening use of light-emitting eReaders negatively
    affects sleep, circadian timing, and next-morning alertness. PNAS. 2015;112(4):
    1232–7. https://doi.org/10.1073/pnas.1418490112 (PMID 25535358)
59. androidx/media — `GaplessInfoHolder.java` (parses the iTunSMPB encoder delay and
    padding). https://github.com/androidx/media/blob/release/libraries/extractor/src/main/java/androidx/media3/extractor/GaplessInfoHolder.java
60. Android Developers — Media3 ExoPlayer supported
    formats. https://developer.android.com/media/media3/exoplayer/supported-formats
61. WHO & ITU — Safe listening devices and systems (2019; ITU-T
    H.870). https://www.who.int/publications/i/item/9789241515276 (earlier doc [68])
62. Hugh SC, Wolter NE, Propst EJ, et al. Infant sleep machines and hazardous sound pressure levels.
    Pediatrics. 2014;133(4):677–81. https://doi.org/10.1542/peds.2013-3617 (earlier doc [69])
