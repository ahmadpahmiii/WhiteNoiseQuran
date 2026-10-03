# White Noise Quran — Pack Research (Quran packs for sleep & calm · SEA)

**Date:** 2026-10-03 · **Prepared for:** product owner · **Builds on:**
`docs/research_white_noise_quran.md`, cited here as "earlier doc §n" (it covers competitors, noise
science, scholarly views, the bedtime sunnah table, equran.id facts, the SEA market and ASO).

**Scope:** which ready-to-play Quran **packs** to offer. For each pack: exact contents, religious
basis, audio size and duration on equran.id, SEA demand, competitors, default ambient and timer, and
risks. **Ambient sound packs and rewarded ads are covered in `docs/research_sounds.md`, not here.**

**Owner's decisions (fixed):**

- **Market:** Indonesia, Malaysia, Brunei.
- **Use cases:** sleep, and calm/anxiety relief.
- **Monetization:** freemium with ads planned (see research_sounds.md).
- **Scope:** focused on Quran listening plus ambient sound.
- **Licensing:** no contact with equran.id about licensing.
- **Hard rules:**
    - Quran recitation stays free.
    - Not a music app: no shuffle, no music or instruments, and the Quran is never presented as
      songs or playlists.
    - No health or medical claims.
    - **No streaks, progress counters or "nights kept" counters.**

**How to read this doc:**

- **Citations:** `[n]` refers to the source list in §11. "secondary:" marks a source that isn't
  primary.
- **Sizes:** MB means 10^6 bytes, taken from the HTTP `Content-Length` of each file on
  cdn.equran.id [1].
- **Play times:**
    - Given for **Alafasy**, whose files are constant-bitrate (CBR), so the times are exact. They
      include repeats (e.g. the Quls ×3).
    - **Sudais** is given in MB only. His files mix 128 and 192 kbps, so a duration estimated from
      size could be off by about ±33%.
- **Hadith:** numbering follows sunnah.com. Text and grades were checked against the hadith-api
  dataset [3], because sunnah.com blocks bots (earlier doc §5).
- **Status:** this is not a fatwa. The "may say / must not say" lines are product-copy guidance to
  be reviewed by a scholar.

---

## 2. TL;DR

1. **23 candidates assessed (including variants): 9 core, 10 stretch, 4 out.**
    - Recommended **v1 = 7 small packs, all free:** Bedtime Sunnah, Ayat al-Kursi, Waking in the
      night, Ayat Sakinah, Penenang Hati, Ar-Rahman, Saat Berduka.
    - Together they are **38 unique files: 44.1 MB (Alafasy) or 54.6 MB (Sudais)**. Files shared
      between packs are stored once [1].
2. **Strong basis and strong demand overlap at bedtime.**
    - The bedtime set rests on sahih hadith (Bukhari 2311, 5009, 5017; Abu Dawud 5055).
    - Its formats are huge on YouTube: "Ayat Kursi 100X" has 56.1M views, and "Al-Fatihah, Ayat
      Kursi, 3 Qul" has 12.8M in Malaysia [31].
    - Autocomplete even pairs ruqyah with sleep ("ruqyah susah tidur", "ruqyah pengantar
      tidur") [32]. The sunnah bedtime pack is the honest answer to that need.
3. **The biggest "calm" item is a scholars' practice, not a sunnah.**
    - Ayat Sakinah (top video 58.9M views) rests on Ibn al-Qayyim's report that Ibn Taymiyyah
      recited the six *sakinah* verses in hardship, and that he tried it himself [5].
    - IslamQA allows it but says not to attribute it to the Shari'ah [6]. The copy must say "
      practice of scholars".
4. **Several high-demand packs are claim traps.** Autocomplete is full of outcome promises [32], and
   the narrations behind them are weak or absent:

   | Pack | Promise users search for | Basis |
      |---|---|---|
   | Ayat Syifa | "penyembuh segala penyakit" (cure for all illness) | no hadith [7] |
   | Al-Waqi'ah / "Ayat Seribu Dinar" | rizq (livelihood) | weak [24]; Ibn Majah 4220 weak |
   | Baby | "rewel / demam" (fussy / fever) | no specific hadith |
   | Pregnancy | "agar janin sehat" (healthy fetus) | cultural practice |
   | Ruqyah | "pengusir jin" (drives out jinn) | wording risk |

   Keep Syifa and Waqi'ah out as packs. Ship the baby, pregnancy and protection packs later, with
   strict copy.
5. **For several virtues, listening isn't the same as reciting.**
    - Scholars tie these virtues to *reading*: the light of Al-Kahf on Friday, Al-Baqarah protecting
      the house (Ibn 'Uthaymin), and Al-Mulk's intercession [17][18] (earlier doc §4b).
    - Recorded ruqyah is "beneficial" per IslamQA but "not ruqyah" per Islamweb [10][11].
    - So every pack's "why" text needs a one-line listening note.
6. **Audio facts that change the design** [1]:
    - Alafasy full-surah files are 128 kbps, **except Al-Baqarah (64 kbps, 2 h 6 min)**. His
      per-ayah files are 128 kbps.
    - **Sudais files mix 128 and 192 kbps, and each of his full-surah files carries a ~0.88 MB cover
      image.** That is about 100 MB of his 1,840.8 MB total.
    - All 133 per-ayah files checked (95 Alafasy, 38 Sudais) returned HTTP 200.
7. **Ramadan "one juz a night" works but is heavy.**
    - Each juz is 50–64 MB and 52–66 min (Alafasy).
    - 25 of 30 juz start or end mid-surah, so the month needs 2,285 per-ayah files [1][4].
    - It is a seasonal stretch for Ramadan 1448 (about 8 Feb 2027; earlier doc §6), with no
      completion tracking.
8. **"tanpa iklan" (no ads) shows up for 14 of 28 pack queries in autocomplete.** Examples: penenang
   hati, ayat kursi, murottal bayi, juz amma, yasin malam jumat, murottal ibu hamil [32]. Ad
   placement is decided in research_sounds.md. These queries show users want recitation itself to
   stay free of ads.

---

## 3. Pack catalogue

**Fit labels:**

- **core:** sleep or calm, with a strong basis and low risk.
- **stretch:** valid, but off the core use cases, seasonal, or needing safety work.
- **out:** don't ship as a pack.

**Table conventions:**

- Contents use surah:ayah. A bare number means the whole surah, and "→" gives the play order.
- MB is Alafasy / Sudais. Play time is Alafasy.
- "n.m." means not measured.

| #  | Pack (ID · EN)                                             | Moment                           | Contents (order · repeats)                                                                         | Basis (grade)                                                                                                                                    | Audio · MB · play time                                                                           | SEA demand [31][32]                                                                                     | Fit                  | Main risk                                                             |
|----|------------------------------------------------------------|----------------------------------|----------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|----------------------|-----------------------------------------------------------------------|
| 1  | Bacaan Sebelum Tidur (Sunnah) · Bedtime Sunnah             | lying down                       | 32 → 67 → 2:255 → 2:285–286 → 112, 113, 114 ×3 → 109 last                                          | Bukhari 2311, 5009, 5017; Tirmidhi 2891 (hasan); Abu Dawud 5055 (sahih); Tirmidhi 2892 (disputed)                                                | 6 full + 3 per-ayah · 20.5 / 27.3 · ~25 min. Short variant without 32 and 67: 4.8 / 8.6 · ~8 min | "Ayat Kursi 100X" 56.1M; MY "Al-Fatihah, Ayat Kursi, 3 Qul" 12.8M; "bacaan sebelum tidur sesuai sunnah" | core                 | promising protection or sleep                                         |
| 1b | + Al-Isra & Az-Zumar (optional long)                       | bedtime, long                    | 17 → 39                                                                                            | Tirmidhi 2920 (sahih, Albani)                                                                                                                    | full · 59.9 / 65.4 · ~62 min                                                                     | low                                                                                                     | core (option)        | length                                                                |
| 2  | Ayat Kursi Sebelum Tidur · Ayat al-Kursi                   | quick bedtime                    | 2:255 ×1 (×3 / ×7 by user choice)                                                                  | Bukhari 2311                                                                                                                                     | per-ayah · 0.8 / 0.9 · 52 s per play                                                             | 56.1M; "ayat kursi pengantar tidur"; "6 jam tanpa iklan"                                                | core                 | count-based promises ("100x")                                         |
| 3  | 4 Qul (MY label) · The four Quls                           | bedtime                          | 109, 112, 113, 114 (Quls ×3 optional)                                                              | Abu Dawud 5055; Bukhari 5017                                                                                                                     | full · 2.6 / 6.0 · ~3 min                                                                        | "4 Quls" 48.3M; MY "4 qul dan ayat kursi"                                                               | core (variant of #1) | none                                                                  |
| 4  | Terjaga di Malam Hari · Waking in the night                | night waking                     | 3:190–200 (11 ayat) + text: waking duas, bad-dream sunnah                                          | Bukhari 183, 4569; Muslim 763                                                                                                                    | per-ayah · 4.8 / 4.9 · 5 min                                                                     | Sudais 3:190–200 2.8M; "surah ali imran 190-200" (ID & MY)                                              | core                 | low                                                                   |
| 5  | Al-Baqarah di Rumah · Al-Baqarah at home                   | evening, long                    | 2 (full)                                                                                           | Muslim 780; Tirmidhi 2877 (sahih); Muslim 804                                                                                                    | full · 60.3 / 88.1 · 2 h 6 min                                                                   | Alafasy 84.6M; Muzammil 58.1M; "surah al baqarah tanpa iklan"                                           | stretch              | "pengusir jin"; a tape isn't recitation (Ibn 'Uthaymin)               |
| 6  | Murottal Pengantar Tidur Bayi · Baby bedtime (for parents) | baby's sleep                     | 1 → 2:255 → 112–114 ×3; optional duas 3:38, 14:40, 21:89, 25:74                                    | no specific hadith; Muslim 2192; Mufti Selangor dua list                                                                                         | full + per-ayah · 3.4 / 7.1 (+1.0 / 1.1 for duas) · ~7 min                                       | 18.3M (10 h video); MY 23.0M; "murottal bayi rewel / demam"                                             | stretch              | infant noise and crib safety; Families policy; jinn and health claims |
| 7  | Juz Amma Anak · Kids' bedtime surahs                       | child's bedtime                  | 93 → 114 (22 surahs), or all of 78–114                                                             | Bukhari 5035/5036 (learning the short surahs young)                                                                                              | full · 19.2 / 39.8 · 20 min. All of 78–114: 61.0 / 94.0 · 63 min                                 | Riko kids' video 162M; "metode ummi"; Juz Amma + Suara app 1M+ installs                                 | stretch              | Families policy if kids are targeted                                  |
| 8  | Ayat Sakinah · Verses of tranquility                       | anxiety, distress                | 2:248 → 9:26 → 9:40 → 48:4 → 48:18 → 48:26                                                         | Madarij al-Salikin (scholars' practice); IslamQA 141318: permissible, not sunnah                                                                 | per-ayah · 3.1 / 3.2 · 3.2 min                                                                   | 58.9M (al-Nufais); 15.3M; Muzammil 7.0M (3 h)                                                           | core                 | presenting it as sunnah or as therapy                                 |
| 9  | Penenang Hati · When the heart is heavy                    | worry, sadness                   | 93 → 94 → 13:28 → 12:86 → 21:87 → 3:173 → 65:2–3 → 2:286                                           | Bukhari 4950 / Muslim 1797; Tirmidhi 3505 (sahih); Bukhari 4563; Muslim 126                                                                      | 2 full + 7 per-ayah · 4.8 / 7.1 · 5 min                                                          | MY "penenang hati" 33.7M; "Doa Nabi Yunus 1000x" 14.1M                                                  | core                 | rizq ("seribu dinar") and stress-cure claims                          |
| 10 | Saat Berduka · In grief                                    | loss                             | 2:155–157 → 12:86 → 94 → 2:286 (+ istirja' dua as text)                                            | Muslim 918; Bukhari 1283                                                                                                                         | 1 full + 5 per-ayah · 2.7 / 3.7 · 2.8 min                                                        | weak (generic queries only)                                                                             | core                 | tone; avoid "for the deceased" disputes                               |
| 11 | Surah Ar-Rahman                                            | calm evening                     | 55                                                                                                 | Tirmidhi 3291 (hasan, Albani); "bride of the Quran" is very weak                                                                                 | full · 10.9 / 12.3 · 11.3 min                                                                    | 158.4M; 90.9M                                                                                           | core                 | citing the weak "bride" narration                                     |
| 12 | Perlindungan (Ruqyah Syar'iyyah) · Protection verses       | fear, protection                 | Ibn Baz set: 1 → 2:255 → 7:117–119 → 10:79–82 → 20:65–69 → 109 → 112–114 ×3. Extended set optional | Bukhari 5736, 5016; Muslim 2192, 2200; Tirmidhi 2058 (sahih); Ibn Baz fatwas                                                                     | mixed · 6.6 / n.m. · ~10 min. Extended set: +10.5 MB, 11 min                                     | Faizar 37.0M / 18.3M; "Offline Ruqyah Alafasy" 500K+ installs; "ruqyah susah tidur"                     | stretch              | therapy and jinn claims; fear framing                                 |
| 13 | Ayat Syifa · Verses mentioning *shifa*                     | illness                          | 9:14, 10:57, 16:69, 17:82, 26:80, 41:44                                                            | no hadith; dream attributed to al-Qushayri (unverified); IslamQA 123155 is against fixing verses for illnesses                                   | per-ayah · 2.3 / n.m. · 2.4 min                                                                  | Muzammil "Ayat Penyembuh" 28.6M; "penyembuh segala penyakit"                                            | **out**              | the name itself is a healing claim                                    |
| 14 | Tenang Sebelum Ujian · Before an exam                      | exam nerves                      | 20:25–28 → 20:114 → 94                                                                             | Quranic duas; no exam-specific hadith                                                                                                            | per-ayah + full · 1.2 / 2.1 · 1.3 min                                                            | low                                                                                                     | stretch              | "lulus / nilai bagus" (pass / good marks) promises                    |
| 15 | Al-Kahfi Hari Jumat · Al-Kahf on Friday                    | Thursday sunset to Friday sunset | 18 (or 18:1–10)                                                                                    | al-Hakim / al-Bayhaqi (sahih per Albani, Sahih al-Jami' 6470); Muslim 809 (first ten verses)                                                     | full · 31.9 / 35.0 · 33 min                                                                      | Muzammil 98.6M; Alafasy 50.2M                                                                           | stretch              | the reward is for reading                                             |
| 16 | Yasin Malam Jumat · Yasin on Thursday night                | Thursday night                   | 36                                                                                                 | Tirmidhi 2887 (mawdu', Albani); Abu Dawud 3121 and Ibn Majah 1448 (da'if). NU recommends it; Muhammadiyah and IslamQA object to fixing the night | full · 17.0 / 19.0 · 17.7 min                                                                    | Sudais Yasin 699.4M; three Yasin & Tahlil apps at 1M+ each                                              | stretch              | sectarian; "heart of the Quran" and "pengusir setan" claims           |
| 17 | Al-Waqi'ah (for rizq)                                      | evening                          | 56                                                                                                 | poverty narration weak (Ibn Baz); NU practises it as a virtuous deed                                                                             | full · 11.5 / 10.0 · 11.9 min                                                                    | 26.7M; "Al Waqiah 7x … hutang lunas" (debts paid off) 22.5M                                             | **out** (as a pack)  | rizq promises                                                         |
| 18 | Satu Juz Semalam · A juz a night (Ramadan)                 | Ramadan nights                   | juz N on night N, mushaf order                                                                     | Bukhari 1902, 4998, 5054; the 30-part division is a convention                                                                                   | per juz: 50–64 MB (median 57) · 52–66 min. Sudais ~37–94 MB (rough)                              | "murottal juz 1" 10.4M; "tadarus"                                                                       | stretch (seasonal)   | 2,285 per-ayah files; mobile data use                                 |
| 19 | Lailatul Qadar · Al-Qadr                                   | last ten nights                  | 97                                                                                                 | Bukhari 2017, 1901                                                                                                                               | full · 0.7 / 1.7 · <1 min                                                                        | Alafasy 7.2M                                                                                            | stretch (inside #18) | "you'll catch Lailatul Qadar" claims                                  |
| 20 | Al-Ma'tsurat (Quran parts only)                            | morning, evening                 | 1; 2:1–5; 2:255–257; 2:284–286; 112–114 ×3; 3:26–27                                                | compiled by Hasan al-Banna (secondary); Quls ×3 from Abu Dawud 5082 and Tirmidhi 3575 (hasan)                                                    | Quran parts: 8.1 MB · ~12 min. **The dzikir parts aren't on equran.id**                          | Hanan Attaki 19.1M; Al Ma'tsurat app 500K+                                                              | **out** (for v1)     | incomplete without dzikir audio                                       |
| 21 | Murottal Ibu Hamil · During pregnancy                      | pregnancy                        | 12, 19, 31. The Selangor list adds 1, 2:255, 7:189, 9, 16:78, 36                                   | cultural practice (*amalan*); suggested by the Mufti Selangor guideline; Muhammadiyah: no strong basis                                           | full · 72.4 / 76.7 · 75 min. Selangor list: 150.0 MB · 2 h 36 min                                | 25.6M; 19.3M; "agar janin sehat", "cepat lahir"                                                         | stretch              | "anak tampan", "janin cerdas" (handsome baby, smart fetus) promises   |
| 22 | Ayat 7 (*pendinding*, MY)                                  | protection                       | verse list unverified                                                                              | unverified                                                                                                                                       | —                                                                                                | —                                                                                                       | **out**              | amulet-like use; unverified                                           |

### 3a. How the numbers were measured

- **Requests:** 367 HTTP HEAD requests plus 11 small range reads (each ≤ 16 KB) to cdn.equran.id, at
  most 4 in parallel. Every HEAD returned 200 [1]. That covers:
    - all 114 full-surah files for both Alafasy and Sudais;
    - 95 Alafasy and 38 Sudais per-ayah files.
- **Alafasy bitrates:**
    - Al-Fatihah full file: MPEG-1 Layer III, 128 kbps CBR, 44.1 kHz, with an 8.9 KB ID3 tag.
    - **Al-Baqarah full file: MPEG-2, 64 kbps, 24 kHz, 314,154 frames = 125.7 min.**
    - Per-ayah 2:255: 128 kbps, 1,989 frames = 52.0 s.
    - A size-versus-letter-count check over all 114 surahs found only Al-Baqarah off-pattern [1][4].
- **Sudais bitrates and tags:**
    - Full files: Al-Fatihah and Al-Mulk are 192 kbps; Al-Baqarah is 128 kbps.
    - Per-ayah: 2:255 is 192 kbps; 48:4 is 128 kbps.
    - Each full file has an ~881 KB ID3 tag, almost all of it an embedded cover image.
    - The per-ayah files' tags read "(C) VerseByVerseQuran.com", which shows their provenance (
      earlier doc §5).
- **Spread across reciters (Al-Fatihah full file):** Yasser 0.78 MB, Alafasy 0.84, Ibrahim
  Al-Dossari 1.42, Al-Juhany 1.54, Al-Qasim 1.77, Sudais 1.80. The other four probably also carry
  cover art or use higher bitrates (**unverified**).
- **Juz estimates:**
    - Juz boundaries come from Tanzil's metadata [4].
    - For surahs split across juz, the per-ayah portion is estimated from that portion's share of
      the surah's letters. Checked against measured per-ayah groups, this is within ±10% for
      mid-size groups (10:79–82, 20:65–69). It is up to 2× off for very short openings such as 2:
      1–5, because *Alif-Lam-Mim* is drawn out.
    - **Treat juz figures as ±20%.**
- **Whole Quran:**
    - Alafasy: 1,658 MB, ~29.8 h (matches earlier doc §5).
    - Sudais: 1,840.8 MB, ~20–30 h (the range comes from his mixed bitrates).

---

## 4. Pack detail cards

Each card covers:

- **Order:** the contents in play order.
- **Basis:** hadith links go to sunnah.com, verified via [3].
- **Views:** where scholars differ.
- **Weak or overstated:** narrations and claims that circulate.
- **Wording:** what the app **may say** and **must not say**.
- **Defaults:** suggested ambient sound and timer.
- **Seen elsewhere:** competitors that offer it.

Ambient names are the bundled loops: steady rain, drizzle, rain & birds, forest, ocean, night
train. "After" means the ambient sound starts only when the recitation ends (earlier doc, product
rules 1–2).

### 4.1 Bacaan Sebelum Tidur (Sunnah) · MS "Amalan Sebelum Tidur" · Bedtime Sunnah — core

- **Order:**
    - As-Sajdah (32) → Al-Mulk (67) → Ayat al-Kursi (2:255) → 2:285–286 → Al-Ikhlas, Al-Falaq,
      An-Nas ×3 (the blow-and-wipe moment) → Al-Kafirun (109) last.
    - Al-Kafirun goes last because the hadith says to sleep "at its end".
    - **Short variant:** drop 32 and 67. **Long add-on (1b):** Al-Isra (17) + Az-Zumar (39).
    - **"4 Qul" (#3)** is the Malaysian label for 109 + 112–114.
- **Basis:**
    - Ayat al-Kursi as a guard until morning: [Bukhari 2311](https://sunnah.com/bukhari:2311).
    - The last two verses of Al-Baqarah "suffice" at
      night: [Bukhari 5009](https://sunnah.com/bukhari:5009), [Muslim 807](https://sunnah.com/muslim:807).
    - The 3 Quls, blowing into the hands and wiping the
      body: [Bukhari 5017](https://sunnah.com/bukhari:5017), [Bukhari 5748](https://sunnah.com/bukhari:5748).
    - Al-Mulk intercedes: [Tirmidhi 2891](https://sunnah.com/tirmidhi:2891), hasan per Albani.
    - As-Sajdah with Al-Mulk: [Tirmidhi 2892](https://sunnah.com/tirmidhi:2892). Grading is
      disputed: the dataset lists Albani as "da'if maqtu'", while IslamQA cites him authenticating
      it.
    - Al-Kafirun last: [Abu Dawud 5055](https://sunnah.com/abudawud:5055), sahih per Albani.
    - Al-Isra and Az-Zumar: [Tirmidhi 2920](https://sunnah.com/tirmidhi:2920), sahih per Albani (
      earlier doc §5).
- **Views:** these range from "listening at bedtime is fine" to "better to switch it off once you're
  asleep" (earlier doc §4b). Al-Mulk's virtue is tied to reciting; people who can't read can repeat
  after the reciter (earlier doc §4b).
- **Weak or overstated:**
    - Counts like "100x" or "1000x" (no number is reported).
    - "Al-Ikhlas ×3 = a full khatam", extrapolated from "equals a third of the
      Quran" ([Bukhari 5015](https://sunnah.com/bukhari:5015)).
    - Autocomplete promises such as "penghapus dosa" (erases sins) and "agar rezeki lancar" (for
      smooth livelihood) [32].
- **May say:**
    - "Bacaan yang diamalkan Nabi ﷺ sebelum tidur, lengkap dengan sumber hadisnya."
    - "Mendengarkan itu baik; membaca sendiri lebih utama."
- **Must not say:**
    - "Dijamin terlindungi dari jin/sihir", "tidur pasti nyenyak", "penghapus dosa".
    - Any promise based on a number of repeats.
- **Defaults:**
    - Ambient: drizzle or steady rain, **after** the recitation.
    - Timer: end of pack, then the ambient sound fades over 20–30 min.
    - No all-night playback by default (REM caution; earlier doc §3).
- **Seen elsewhere:**
    - YouTube compilations labelled "Pelindung Diri" (self-protection) or "Bikin Adem" (calms
      you) [31].
    - Quranify's "sleep playlists" [33].
    - Muslim Pro's Doa & Dzikir library [33].
    - The equran.id Doa API has a text-only group "Doa Sebelum dan Sesudah Tidur" (7 items) that can
      supply the dua text [2].
- **Risk:** low. Treat ×3 as a repeat count, not three downloads.

### 4.2 Ayat Kursi Sebelum Tidur · MS "Ayatul Kursi Sebelum Tidur" — core

- **Order:** 2:255. Default ×1; the user can choose ×3 or ×7.
- **Basis:** [Bukhari 2311](https://sunnah.com/bukhari:2311): reciting it when going to bed keeps a
  guard from Allah with you until morning.
- **Weak or overstated:**
    - "Ayat Kursi 100X/1000X" loops (56.1M views) and "pengusir setan dan jin di rumah dan di
      tubuh" (drives devils and jinn from the house and body) [31][32].
    - No number is reported in the hadith.
- **May say:** "Sunnah: sekali sebelum tidur (HR Bukhari 2311). Pengulangan adalah pilihan Anda."
- **Must not say:** "100x untuk …", "pengusir jin".
- **Defaults:** same as 4.1. The 52 s file shares storage with 4.1.
- **Seen elsewhere:** the "Ayat Kursi 10 Hour Nonstop" Play app (earlier doc §2b), and YouTube loops
  by Neurotic Studio and Muzammil Hasballah [31].

### 4.3 Terjaga di Malam Hari · MS "Terjaga Waktu Malam" · Waking in the night — core

- **Order:** 3:190–200 (11 ayat). Text-only companions:
    - the waking dua ([Bukhari 6312](https://sunnah.com/bukhari:6312));
    - the dhikr for someone who wakes at night ([Bukhari 1154](https://sunnah.com/bukhari:1154));
    - after a bad dream: spit lightly to the left ×3, seek refuge, turn
      over ([Muslim 2261](https://sunnah.com/muslim:2261), [Muslim 2262](https://sunnah.com/muslim:2262), [Bukhari 6995](https://sunnah.com/bukhari:6995)).
    - The bad-dream note answers the autocomplete query "bacaan sebelum tidur agar tidak mimpi
      buruk" (bedtime reading against nightmares) [32].
- **Basis:**
    - [Bukhari 183](https://sunnah.com/bukhari:183): on waking, the Prophet ﷺ recited "the last ten
      verses of Al 'Imran".
    - [Bukhari 4569](https://sunnah.com/bukhari:4569)
      and [Muslim 763](https://sunnah.com/muslim:763) name 3:190 as the start and run "to the end of
      the surah", so the pack plays 190–200.
- **Weak:** none prominent.
- **May say:** "Ayat yang dibaca Nabi ﷺ ketika bangun malam."
- **Must not say:** "Bangun tahajud otomatis", "mimpi buruk hilang". Don't add alarms by default.
- **Defaults:** no ambient; stop at the end; hint to dim the screen.
- **Seen elsewhere:** YouTube only (Sudais 2.8M, Alafasy 0.8M) [31]. No app found offering it as a
  pack.

### 4.4 Al-Baqarah di Rumah · MS "Al-Baqarah di Rumah" · Al-Baqarah at home — stretch

- **Order:** Al-Baqarah (2), full. It may be played across several evenings: scholars allow reciting
  it "in stages" [18]. Use normal player resume, with no completion tally.
- **Basis:**
    - Satan flees a house where Al-Baqarah is
      recited: [Muslim 780](https://sunnah.com/muslim:780), [Tirmidhi 2877](https://sunnah.com/tirmidhi:2877) (
      sahih per Albani).
    - Taking it up is a blessing, and "the magicians cannot confront
      it": [Muslim 804](https://sunnah.com/muslim:804).
- **Views:**
    - Ibn 'Uthaymin: a tape doesn't achieve this virtue; the household should recite it. The answer
      leaves hope where no one in the house can recite [18].
    - NU's 1979 ruling holds that listening to a recording is *mubah* (permissible) (earlier doc
      §4b).
- **Weak or overstated:** "pengusir jin dalam rumah" loops (e.g., a 3.0M-view video titled that
  way) [31].
- **May say:** "Hadis: setan lari dari rumah yang dibacakan Al-Baqarah (HR Muslim 780). Banyak ulama
  menekankan keutamaan ini bagi yang membacanya."
- **Must not say:** "Usir jin dari rumah", "rumah aman dari sihir".
- **Audio:**
    - 60.3 MB for Alafasy (the 64 kbps file, 2 h 6 min) or 88.1 MB for Sudais.
    - A per-ayah version would be about 2× larger for Alafasy, because his per-ayah files are 128
      kbps.
    - With the full file, the timer can only stop at the end of the surah or mid-verse.
- **Defaults:** ambient off; timer off.
- **Seen elsewhere:** Alafasy's official upload (84.6M) and Muzammil Hasballah (58.1M) [31].

### 4.5 Murottal Pengantar Tidur Bayi · MS "Bacaan al-Quran untuk Bayi Tidur" · Baby bedtime (for parents) — stretch

- **Order:** Al-Fatihah → Ayat al-Kursi → 112–114 ×3.
    - **Optional:** the Quranic duas for children listed by the Selangor guideline: 3:38, 14:40, 21:
      89, 25:74 [27].
    - **Text only:** the Prophet's refuge dua for Hasan and
      Husain ([Bukhari 3371](https://sunnah.com/bukhari:3371)) and the equran.id Doa API group "Doa
      Kepada Anak Yang Baru Lahir" [2].
- **Basis:**
    - No hadith addresses playing Quran to help infants sleep.
    - General basis: the Prophet ﷺ recited the mu'awwidhat over household
      members ([Muslim 2192](https://sunnah.com/muslim:2192)), plus the Selangor dua list [27].
    - No SEA fatwa specific to this was found (Mufti WP searched).
- **Weak or overstated:**
    - A competitor app frames fussiness and nightmares as "jinn interference" [33].
    - Autocomplete: "murottal bayi demam/sakit" (fever / sick), "rewel" (fussy) [32].
- **May say:** "Bacaan pendek untuk menemani orang tua menidurkan bayi. Letakkan ponsel jauh dari
  bayi, volume rendah, durasi singkat."
- **Must not say:**
    - "Bayi cepat/nyenyak tidur", "menyembuhkan demam/rewel", "melindungi dari jin".
    - Cartoon-baby artwork.
- **Defaults:**
    - Steady rain **after** the recitation, at a capped low volume.
    - Hard stop at 30 min. Never all night.
- **Risks:**
    - Infant sleep machines exceeded 50 dBA at 30 cm (earlier doc §3).
    - AAP: "nothing else should be in the crib" [38]. That means no phone or charging cable there.
    - Play: "youthful animation" or "young characters" in the listing can pull the app into Families
      review [37].
- **Seen elsewhere:**
    - YouTube: "… FULL 10 JAM" (18.3M) [31].
    - Play: "Ruqyah Bayi Rewel Susah Tidur" (10K+ installs, ads) [33]; "Quran White Noise – Baby
      Sleep" (earlier doc §2b).

### 4.6 Juz Amma Anak · MS "Juz Amma Kanak-kanak" · Kids' bedtime surahs — stretch

- **Order:** 93 → 114 in mushaf order (22 surahs), or all of 78–114. A memorization (reverse) order
  is acceptable only as an explicitly chosen learning mode, never as random play.
- **Basis:** the tradition of learning the short (*mufassal*) surahs young: Ibn 'Abbas had learned
  them by age
  ten ([Bukhari 5035](https://sunnah.com/bukhari:5035), [5036](https://sunnah.com/bukhari:5036)).
- **May say:** "Surat-surat pendek untuk menemani anak sebelum tidur."
- **Must not say:** "Anak cepat hafal/cerdas".
- **Defaults:** rain & birds **after** the recitation; timer at end of pack.
- **Audio:**
    - About 19 MB of Sudais's 39.8 MB is cover art (22 files).
    - equran.id has adult reciters only. There's no child-voice repeat-after-me (*talaqqi*) format
      like "metode ummi".
- **Risk:** Families policy applies if children are a target audience [37] (earlier doc §7).
- **Seen elsewhere:** Riko The Series (animated, 162M); the Play app "Juz Amma + Suara" (1M+, ads,
  IAP) [33]; Quran.com Radio's "Juz Amma" station [36].

### 4.7 Ayat Sakinah · MS "Ayat Sakinah" · Verses of tranquility — core

- **Order:** 2:248 → 9:26 → 9:40 → 48:4 → 48:18 → 48:26. These are exactly the six verses with
  *sakinah*: a search of all 6,236 verses finds no others [4][39].
- **Basis:**
    - Ibn al-Qayyim, *Madarij al-Salikin*, chapter on *sakinah* (vol. 2, pp. 471–472 in the islamweb
      edition; IslamQA cites 2/502–504 in another edition) [5]:
        - Ibn Taymiyyah would recite these verses "when matters became severe";
        - once, when gravely ill, he asked those around him to recite them, and the state lifted;
        - Ibn al-Qayyim: "I too have tried reciting these verses" when the heart is troubled, and
          found a great effect.
    - IslamQA #141318: one may recite them for calm, but should not attribute the practice to the
      Shari'ah [6].
- **Views:** treat this as the scholars' own experience (*tajriba*), not sunnah. The same
  authorities warn against fixing verses as cures for specific conditions [7].
- **May say:** "Enam ayat yang menyebut *sakinah* (ketenangan). Ibnul Qayyim menceritakan gurunya,
  Ibnu Taimiyah, membacanya saat menghadapi kesulitan (Madarij as-Salikin)."
- **Must not say:** "Sunnah Nabi", "obat cemas/anxiety/depresi", "terapi".
- **Defaults:** ambient off during the recitation; forest **after** for 10 min. Repeat ×1, with ×3
  optional.
- **Seen elsewhere:** al-Nufais (58.9M, Arabic title "to repel anxiety and worries"); Omar Hisham (
  15.3M); Muzammil Hasballah "PENENANG HATI & PIKIRAN (Ayat Sakinah)" (7.0M, 3 h) [31].

### 4.8 Penenang Hati · MS "Penenang Hati" · When the heart is heavy — core

- **Order:** Ad-Duha (93) → Ash-Sharh (94) → 13:28 → 12:86 → 21:87 → 3:173 → 65:2–3 → 2:286.
- **Basis:**
    - Ad-Duha was revealed when revelation paused and the Prophet ﷺ was
      taunted ([Bukhari 4950](https://sunnah.com/bukhari:4950), [Muslim 1797](https://sunnah.com/muslim:1797)).
    - Yunus's dua (21:87) is answered for any
      Muslim ([Tirmidhi 3505](https://sunnah.com/tirmidhi:3505), sahih per Albani).
    - 3:173 was said by Ibrahim and by Muhammad ﷺ ([Bukhari 4563](https://sunnah.com/bukhari:4563)).
    - For 2:286, Allah answers "I have done so" ([Muslim 126](https://sunnah.com/muslim:126)).
    - 13:28 and 12:86 are included for their meaning.
- **Weak or overstated:**
    - "This verse would suffice people" for 65:
      2 ([Ibn Majah 4220](https://sunnah.com/ibnmajah:4220), da'if).
    - The "Ayat Seribu Dinar" label for 65:2–3: no hadith basis found. It's sold as "pembuka
      rezeki" (opens livelihood) in videos with 12.7M views [31].
    - "Doa Nabi Yunus 1000x" (14.1M).
- **May say:** "Ayat-ayat penenang hati, beserta kisah turunnya."
- **Must not say:** "Penghilang stres/depresi", "pembuka rezeki", "hutang lunas" (debts paid off).
- **Defaults:** ambient off during; ocean **after**, optional.
- **Seen elsewhere:** Muzammil "Dzikir Penghilang Stress" (5.7M); "Hilangkan Stress – Zikir Penenang
  Hati" (33.7M, MY) [31].

### 4.9 Saat Berduka · MS "Ketika Berduka" · In grief — core

- **Order:** 2:155–157 → 12:86 → Ash-Sharh (94) → 2:286.
- **Text companions:**
    - the istirja' dua ([Muslim 918](https://sunnah.com/muslim:918));
    - "patience is at the first stroke" ([Bukhari 1283](https://sunnah.com/bukhari:1283));
    - the equran.id Doa API groups "Doa Saat Mendapat Kabar" and "Doa Saat Sedih dan Sulit" [2].
- **Views:** keep the focus on the bereaved. Stay out of Yasin/tahlil "for the deceased", where NU
  and Muhammadiyah differ [20][22].
- **May say:** "Ayat dan doa untuk saat kehilangan."
- **Must not say:** "Kirim pahala untuk almarhum" (send reward to the deceased; disputed), "
  menghapus duka".
- **Defaults:** ambient off; drizzle **after**, optional. No timer.
- **Seen elsewhere:** no dedicated pack found. This is a gap.

### 4.10 Surah Ar-Rahman · MS "Surah Ar-Rahman" — core

- **Order:** 55, full.
- **Basis:** the Prophet ﷺ recited it to his companions and said the jinn had responded better, at
  each refrain ([Tirmidhi 3291](https://sunnah.com/tirmidhi:3291), hasan per Albani; Zubair Ali Zai
  grades it da'if).
- **Weak:** "Everything has a bride; the Quran's bride is Ar-Rahman" (al-Bayhaqi, *Shu'ab* 2265) is
  very weak [26].
- **May say:** "Surah dengan pengulangan tentang nikmat Allah."
- **Must not say:** "'Arus al-Qur'an' (hadis)", "terapi", "menyembuhkan".
- **Defaults:** drizzle **after**; timer at end of surah.
- **Seen elsewhere:** Zikrullah TV (158.4M) and Muzammil Hasballah (90.9M) on YouTube [31].

### 4.11 Perlindungan (Ruqyah Syar'iyyah) · MS "Ayat Pendinding / Ruqyah" · Protection verses — stretch

- **Order (Ibn Baz's set):** Al-Fatihah → Ayat al-Kursi → 7:117–119 → 10:79–82 → 20:65–69 →
  Al-Kafirun → Al-Ikhlas, Al-Falaq, An-Nas ×3 [9].
    - **Extended set** used by Indonesian practitioners: 2:1–5, 2:255–257, 2:284–286, 7:54–56, 17:
      110–111, 37:1–11, 55:33–35, 59:21–24, 72:1–4 [12].
- **Basis:**
    - Al-Fatihah used as
      ruqyah: [Bukhari 5736](https://sunnah.com/bukhari:5736), [Bukhari 5007](https://sunnah.com/bukhari:5007).
    - The
      mu'awwidhat: [Bukhari 5016](https://sunnah.com/bukhari:5016), [Bukhari 5735](https://sunnah.com/bukhari:5735), [Muslim 2192](https://sunnah.com/muslim:2192).
    - Once revealed, the mu'awwidhat replaced other
      formulas: [Tirmidhi 2058](https://sunnah.com/tirmidhi:2058) (sahih per Albani).
    - Ruqyah is permitted if it involves no shirk: [Muslim 2200](https://sunnah.com/muslim:2200).
    - Ibn Baz: the whole Quran is a cure [8].
- **Compilations used in SEA:**
    - Wahid Abdussalam Bali's *Wiqayat al-Insan* and *al-Sarim al-Battar*, in Indonesian as "Ruqyah:
      Jin, Sihir & Terapinya" (Ummul Qura, 2017) [12].
    - Nuruddin Marbu, *al-Mujarrabat al-Makkiyyah* (Bogor, 2011) [12].
    - M.B. Tambusai, "Halal-Haram Ruqyah" (Pustaka al-Kautsar, 2013) [12].
    - Ruqyah Learning Center [12].
    - Jam'iyyah Ruqyah Aswaja, under NU's da'wah body [13].
    - Darussyifa', founded by Dato' Haron Din (MY) [14].
- **Views on recordings:**
    - IslamQA #132384: listening is beneficial and doesn't count as "asking for ruqyah", but
      reciting yourself is best [10].
    - Islamweb #27039: a recording isn't valid ruqyah (there's no person performing it), though
      listening is good [11].
- **May say:** "Ayat-ayat perlindungan yang biasa dibaca dalam ruqyah syar'iyyah. Lebih utama dibaca
  sendiri."
- **Must not say:**
    - "Terapi/pengobatan ruqyah", "penghancur sihir" (destroys magic), "pengusir jin".
    - Symptom lists ("tanda gangguan jin": signs of jinn interference).
- **Defaults:** ambient off; no timer.
- **Seen elsewhere:**
    - YouTube: "CEK ADAKAH JIN DI TUBUHMU?" (37.0M) [31].
    - Play: Offline Ruqyah Alafasy (500K+), Ayat Ruqyah Ampuh (100K+), Ayat Pendinding Diri (100K+),
      all with ads [33].
- **Risks:**
    - Health claims (earlier doc §7).
    - Fear framing.
    - Strong practitioner sub-cultures (NU vs Salafi-leaning) with competing lists.

### 4.12 Ayat Syifa · MS "Ayat Syifa'" — out (as a pack)

- **Contents:** 9:14, 10:57, 16:69, 17:82, 26:80, 41:44. These are exactly the six verses with
  *shifa* forms [4].
- **Basis:**
    - No hadith. Popularly traced to a dream of Abu al-Qasim al-Qushayri; only secondary retellings
      were found (**unverified**).
    - IslamQA #123155, citing the Saudi Permanent Committee: fixing specific verses for specific
      illnesses without evidence is not allowed and "closer to innovation" [7].
    - Ibn Baz: the whole Quran is *shifa* [8].
- **Demand:** Muzammil "Ayat Penyembuh dari Al-Qur'an" (28.6M); autocomplete "penyembuh segala
  penyakit", "untuk sakit saraf" (for nerve pain), "demam panas" (high fever) [31][32].
- **Recommendation:** don't ship. The name itself is a healing claim under Play's health policy (
  earlier doc §7). The verses stay reachable as ordinary ayat.

### 4.13 Tenang Sebelum Ujian · MS "Tenang Sebelum Peperiksaan" · Before an exam — stretch

- **Order:** 20:25–28 (Musa's dua) → 20:114 ("Rabbi zidni 'ilma") → Ash-Sharh (94).
- **Basis:** these are Quranic duas; no hadith ties them to exams. The equran.id Doa API group "Doa
  Memohon Ilmu" can supply text [2].
- **May say:** "Doa Nabi Musa dan doa memohon ilmu."
- **Must not say:** "Lulus", "nilai bagus", "cepat hafal" (pass, good marks, memorize fast).
- **Defaults:** ambient off.
- **Demand:** low. NU Online's short "Doa Sebelum Ujian" video has 94K views [31].

### 4.14 Al-Kahfi Hari Jumat · MS "Al-Kahfi Hari Jumaat" · Al-Kahf on Friday — stretch

- **Order:** Al-Kahf (18), full. Option: 18:1–10 only,
  after [Muslim 809](https://sunnah.com/muslim:809) on memorizing the first ten verses.
- **Basis:**
    - "Light between the two Fridays" (al-Hakim 2/399, al-Bayhaqi 3/249): sahih per Albani, *Sahih
      al-Jami'* 6470.
    - A night-of-Friday version (al-Darimi 3407; *Sahih al-Jami'* 6471).
    - Window: Thursday sunset to Friday sunset [16].
    - Both NU and Muhammadiyah writers cite it [21][23].
- **Views:** the one who only listens isn't counted as having read it. Someone who can't read may
  hope for the reward through intention [17].
- **May say:** "Sunnah membaca Al-Kahfi pada hari Jumat (HR al-Hakim; sahih menurut al-Albani).
  Dengarkan sambil mengikuti bacaannya."
- **Must not say:** "Dengan mendengarkan, Anda mendapat cahaya / terlindung dari Dajjal."
- **Defaults:** ambient off; no timer.
- **Seen elsewhere:**
    - Muzammil (98.6M) and Alafasy (50.2M) on YouTube [31].
    - Quran.com Radio's "Surah Al-Kahf — on repeat" station [36].
    - Tarteel's Smart Goals ("Surah Al-Kahf every Friday") [33]. We won't copy its tracking.

### 4.15 Yasin Malam Jumat · MS "Yasin Malam Jumaat" · Yasin on Thursday night — stretch

- **Order:** Yasin (36), full.
- **Basis and grades:**
    - "The heart of the Quran is Yasin": [Tirmidhi 2887](https://sunnah.com/tirmidhi:2887), mawdu' (
      fabricated) per Albani.
    - "Recite Yasin over your
      dying": [Abu Dawud 3121](https://sunnah.com/abudawud:3121), [Ibn Majah 1448](https://sunnah.com/ibnmajah:1448),
      da'if.
- **Views:**
    - **NU:** recommends Yasin on Thursday night as a virtuous deed (*fadha'il al-a'mal*), even with
      weak hadith [20]. Kiai Ma'ruf Khozin: "Yasin on Thursday night, Al-Kahf on Friday" [21].
    - **Muhammadiyah (Tarjih):** doing it together every Thursday night "bukan sunnah Rasulullah" (
      not the Prophet's sunnah). Yasin may be read on any night [22].
    - **IslamQA #2237:** reading Yasin in congregation on Friday nights is bid'ah [19].
- **Unverified:** NU Jatim cites "Yasin + As-Saffat on Friday night" as "HR Abu Daud". It isn't in
  Sunan Abu Dawud: a search of four Sunan finds only Abu Dawud 3121, Tirmidhi 2887 and Ibn Majah
  1448 on Yasin [3].
- **May say:** "Surah Yasin. Banyak keluarga membacanya pada malam Jumat; ulama berbeda pendapat
  tentang pengkhususan malamnya."
- **Must not say:** "Jantung Al-Qur'an (hadis)", "pengusir setan", "penyembuh segala penyakit", "
  untuk arwah" (for the dead).
- **Defaults:** ambient off.
- **Seen elsewhere:** YouTube "Surah Yasin dan Ayat Kursi 7X Pengusir Setan …" (14.7M) [31]; three
  Yasin & Tahlil apps at 1M+ each, with ads [33]; Quran.com Radio's "Yaseen, Al-Waqiah, Al-Mulk"
  station [36].

### 4.16 Al-Waqi'ah (for rizq) — out (as a pack)

- **Basis:** "Whoever recites Al-Waqi'ah every night will not be afflicted by poverty" is weak (Ibn
  Baz [24]; also graded weak by Albani — secondary, via search). NU practises it as a virtuous
  deed [25].
- **Demand:** 26.7M views; also "Surat Al WAQIAH 7x, dengarkan hutang lunas, Rejeki datang …" (
  listen and your debts are paid, livelihood comes; 22.5M) [31].
- **Recommendation:** no pack, and no "rezeki" framing. The surah stays in the normal library.

### 4.17 Satu Juz Semalam + Lailatul Qadar · MS "Satu Juz Satu Malam" · A juz a night (Ramadan) — stretch, seasonal

- **Order:** juz N on night N, in mushaf order.
    - The night is picked by date (Hijri; local announcements can shift it by a day — earlier doc
      §6) or manually.
    - **No completion tracking** (owner rule).
    - Al-Qadr (97) is offered on the odd nights of the last ten.
- **Basis:**
    - Jibril met the Prophet ﷺ every night of Ramadan to go over the
      Quran ([Bukhari 1902](https://sunnah.com/bukhari:1902)).
    - He reviewed it with him yearly, and twice in his last
      year ([Bukhari 4998](https://sunnah.com/bukhari:4998)).
    - "Recite the whole Quran in a month" ([Bukhari 5054](https://sunnah.com/bukhari:5054)).
    - Seek Laylat al-Qadr in the odd nights of the last
      ten ([Bukhari 2017](https://sunnah.com/bukhari:2017)); standing in prayer on
      it ([Bukhari 1901](https://sunnah.com/bukhari:1901)).
    - The 30-juz division itself is a later convention, not a hadith.
- **Audio** [1][4]:
    - Each juz is 50–64 MB and 52–66 min (Alafasy); the whole cycle is about 1.72 GB.
    - Juz 14, 17, 28, 29 and 30 are whole surahs only. The other 25 need per-ayah files for split
      surahs: 2,285 in total, up to about 170 for one juz.
- **May say:** "Mendengarkan satu juz setiap malam Ramadan."
- **Must not say:** "Khatam" for listening alone; "dapat Lailatul Qadar".
- **Defaults:** ambient off; timer at end of juz.
- **Risks:**
    - The CDN request volume per user against equran.id's rate limits (earlier doc §5).
    - Downloads on mobile data.
- **Seen elsewhere:** YouTube "Murottal Juz 1" (Abu Usamah 10.4M, Alafasy 7.4M) [31]; Greentech's
  Quran Planner (29- or 30-day khatmah) [33].

### 4.18 Al-Ma'tsurat (Quran parts) · MS "Al-Mathurat" — out (for v1)

- **Contents (sughra, per CNN Indonesia)** [15]:
    - ta'awwudh → Al-Fatihah → 2:1–5 → 2:255–257 → 2:284–286 → Al-Ikhlas, Al-Falaq, An-Nas ×3;
    - then about 20 dzikir/dua items, most ×3;
    - 3:26–27 is listed near the end, followed by closing duas, including "doa rabithah".
- **Audio:**
    - The Quran parts exist on equran.id: 8.1 MB, ~12 min (Alafasy).
    - **The dzikir and duas don't.** The equran.id Doa API is text-only (fields `ar`, `tr`, `idn`,
      `tentang`; 227 entries; no Ma'thurat group) [2]. They would need separately licensed audio.
- **Basis:** compiled by Hasan al-Banna (secondary [15]). Its Quls ×3 morning and evening
  match [Abu Dawud 5082](https://sunnah.com/abudawud:5082)
  and [Tirmidhi 3575](https://sunnah.com/tirmidhi:3575) (both hasan per Albani).
- **Demand:**
    - Hanan Attaki "Al Matsurat Pagi" (19.1M) and Neurotic Studio (19.6M, MY) [31].
    - Play: Al Ma'tsurat (500K+) and Al-Ma'thurat Sughra & Kubra (100K+), both with ads [33].
- **Recommendation:** out for v1, because it's incomplete without the dzikir audio. A smaller "
  Pagi & Petang (Al-Qur'an)" mini-pack (Ayat al-Kursi + Quls ×3) could come later.

### 4.19 Murottal Ibu Hamil · MS "Bacaan Ibu Mengandung" · During pregnancy — stretch

- **Order:** Yusuf (12) → Maryam (19) → Luqman (31).
    - The **Selangor guideline list** also suggests Al-Fatihah, 2:255, 7:189, At-Tawbah (9), 16:78
      and Yasin (36), and says any surah may be read [27].
- **Basis:**
    - A cultural practice (*amalan*).
    - The Jabatan Mufti Negeri Selangor guideline, approved by the state fatwa committee, recommends
      the surahs above and a dua to say "especially after reciting Yusuf, Luqman and Maryam" [27].
    - A Suara Muhammadiyah writer asks whether any strong Quran or hadith reference exists, and
      urges reading for the meaning [28].
    - Muhammadiyah calls the 7-month ceremony (*tingkeban*) not prescribed (*ghairu masyru'*) [29].
    - A field study documents the motivations people attach to these surahs: a handsome or beautiful
      child, an easy birth (secondary [30]).
- **May say:** "Surah yang biasa dibaca keluarga Muslim selama kehamilan (tradisi; disarankan dalam
  panduan Jabatan Mufti Selangor)."
- **Must not say:** "Anak tampan/cantik", "janin sehat/cerdas", "cepat/lancar melahirkan".
- **Defaults:** drizzle **after**; timer 30–60 min.
- **Seen elsewhere:** YouTube "Surat Yusuf Dan Maryam Untuk Ibu Hamil dan Perkembangan Janin" (for
  fetal development; 25.6M) and Muzammil Hasballah (19.3M) [31].

### 4.20 Ayat 7 (*pendinding*, MY) — out

- The verse list couldn't be verified in two searches.
- The usage is amulet-like (*pendinding* means a protective shield).
- If users ask for it, point them to 4.11.

---

## 5. Pack anatomy (fields a pack needs — no code)

**Identity and copy**

- `id`, `version`, `status` (draft · scholar-reviewed · live), `reviewedBy`, `reviewedOn`.
- `names` {id, ms, en}, `subtitle` per locale, `moment` (bedtime · night waking · anytime calm ·
  Friday · Ramadan · pregnancy · baby), internal `fit` (core/stretch).
- `why` per locale (≤ 2 sentences), plus a visible `basisLevel` badge:
    - "Sunnah (hadis sahih/hasan)";
    - "Amalan ulama" (scholars' practice);
    - "Tradisi" (tradition);
    - "Tanpa dalil khusus" (no specific evidence).
- `sources[]`: {type: hadith | quran | scholar | fatwa | guideline, ref (e.g. "Bukhari 2311"),
  grade + grader, url, note}.
- `listeningNote`: shown where a virtue is tied to *reciting* (Al-Kahf, Al-Mulk, Al-Baqarah,
  ruqyah) [17][18][10].
- `viewsNote`: one neutral line where scholars differ (Yasin night, Ayat Sakinah, ambient behind
  recitation).
- `forbiddenClaims`: a checklist for copy review. It covers healing, rizq, baby looks or health,
  guaranteed protection, and count-based promises.

**Contents**

- `items[]` in order: {kind: fullSurah | ayahRange, surah, fromAyah, toAyah, repeat (1–7),
  pauseAfterSec (0, or a repeat-after-me gap; earlier doc rule 5), label}.
- `variants[]` (short / full / long add-on), reusing the same items, with one `defaultVariant`.
- `orderLocked`: always true (no shuffle). An optional `learningOrder` exists only for the kids'
  memorization mode, and only when the user explicitly chooses it.
- `companions[]`: text-only steps or duas, each with its source. These can reference equran.id Doa
  API ids (text only) [2].

**Audio and download**

- `fileSet` per reciter, derived from the items:
    - `audio-full/{slug}/{SSS}.mp3` or `audio-partial/{slug}/{SSSAAA}.mp3`;
    - stored once even when several packs use the same file (e.g. 2:255, 2:286, 94).
- `sizeBytes` and `durationSec` per reciter: precomputed (see §3), shown before download, and
  refreshed if the CDN changes.
- `stopPoints`:
    - per-ayah items allow stopping at the end of an ayah;
    - full-surah items allow stopping only at the end of the surah, because equran.id has no verse
      timings;
    - long full-surah items get flagged (Al-Baqarah is 2 h).
- `offlineRequired`: true for bedtime packs, meaning download before night (offline users).
- `attribution`: EQuran.id, the reciter name, and file provenance where a tag shows it (e.g.
  VerseByVerseQuran.com).

**Mixer and timer (these drive the existing mixer and timer services)**

- `ambient.mode`:
    - off;
    - **after** (starts when the recitation ends);
    - under (ducked below the recitation).
    - Each pack has a default, and the user's "Quran only" choice always wins (earlier doc rules
      1–2).
- `ambient.mix[]`: {sound: steady rain · drizzle · rain & birds · forest · ocean, level 0–1}.
    - Night train is never a default (rhythm concern; earlier doc rule 3).
    - Never bells or chimes: "the bell is the musical instrument of
      Satan" ([Muslim 2114](https://sunnah.com/muslim:2114)).
- `ambient.maxLevelVsRecitation` (e.g. ≤ 0.5). The baby pack also gets an absolute volume cap.
- `timer.preset`: endOfPack · endOfPack + ambient for N min · N minutes, stopping at the next stop
  point.
    - `fadeSec`: the current controller fades over the last 60 s.
    - `allNightAllowed`: false by default; never for the baby pack.
- `screenHint`: dim/off for bedtime and waking packs.

**Availability and audience**

- `suggestWindow` (optional), a suggestion only that never locks content:
    - Friday: Thursday maghrib to Friday maghrib [16];
    - Ramadan nights by Hijri date.
- `audience`:
    - adults;
    - parents (baby, pregnancy);
    - kids, only if Families-compliant [37].
    - The audience drives artwork and listing rules.
- `locales`: e.g. show the "4 Qul" label in MY/BN.
- `safetyNote`: for the baby pack: far away, low volume, short, nothing in the crib [38] (earlier
  doc §3).
- **Deliberately absent:** progress, completion, streak and "nights kept" counters (owner rule).
    - The Ramadan pack just picks tonight's juz.
    - Playback may resume where it stopped, like any player, but nothing is tallied.

---

## 6. How other apps structure packs and collections, and what's free

| App                          | How content is grouped                                                                                                                                                                                           | Free vs paid                                                                        | Note                                                                 |
|------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|----------------------------------------------------------------------|
| Calm [34]                    | 7- and 21-day programs, "Dailies", Sleep Stories, soundscapes; padlocked items                                                                                                                                   | "some of the programs and features are free forever"; no ads; optional subscription | sells content it owns                                                |
| Headspace [34]               | courses and collections                                                                                                                                                                                          | free trial, then subscription (US$12.99/mo, $69.99/yr)                              | earlier doc §1                                                       |
| BetterSleep [34][35]         | 300+ sounds, user mixes, a "Sleep Moves" series, user playlists                                                                                                                                                  | **no longer free since 2025**: trial, then monthly, annual or Lifetime              | earlier doc §1: backlash when free mixes became paid                 |
| Rain Rain [34]               | 50 free and 60+ premium sounds, mixes, favorites                                                                                                                                                                 | mixer, favorites, timer and reminder are free; premium sounds by subscription       | earlier doc §1 also notes packs and a "One Sound Token"              |
| Tide [34]                    | sleep/focus scenes and sounds                                                                                                                                                                                    | localized subscriptions                                                             | earlier doc §1                                                       |
| Quranify [33]                | modes (Study / Work / Sleep), each with curated "playlists"; custom playlists; ambient "focus modes"; ayah by ayah                                                                                               | no ads; IAP                                                                         | uses "playlist" wording and concentration/memory claims. Avoid both  |
| Quran.com Radio [36]         | curated stations: "Yaseen, Al-Waqiah, Al-Mulk", "Surah Al-Kahf (on repeat)", "Juz Amma", "Popular Recitations"; plus reciter stations                                                                            | free                                                                                | the closest precedent for Quran packs                                |
| Greentech Al Quran [33]      | user "Collections" (bookmarks); Quran Planner (29- or 30-day khatmah)                                                                                                                                            | free, no ads                                                                        |                                                                      |
| Tarteel [33]                 | Smart Goals such as "Surah Al-Kahf every Friday"                                                                                                                                                                 | listening free; premium for AI                                                      | goal tracking: not for us (no counters)                              |
| Muslim Pro [33]              | Quran with audio; Doa & Dzikir library; Qalbox video                                                                                                                                                             | ads; **offline Quran listening and ad-free are premium**                            | conflicts with our "recitation stays free" rule                      |
| Pillars [33]                 | prayer-first; "No Ads … Period"                                                                                                                                                                                  | no ads                                                                              |                                                                      |
| SEA single-purpose apps [33] | Al Ma'tsurat (500K+); Yasin dan Tahlil NU Lengkap (1M+); Yasin & Tahlil (1M+); Surah Yaseen & Tahlil Offline (1M+); Offline Ruqyah Alafasy (500K+); Juz Amma + Suara (1M+); Ruqyah Bayi Rewel Susah Tidur (10K+) | all "Contains ads"; several sell ad removal                                         | the baby-ruqyah app treats fussiness and nightmares as signs of jinn |

**Takeaways**

- **Secular apps sell content they own** (programs, stories, sounds). Their "collection" is a
  curated sequence that promises a state (sleep, calm). The Quran equivalent is curation plus
  sourcing, not paywalled recitation.
- **Quran-native precedents keep recitation free:** Quran.com Radio's curated stations, Greentech,
  and Tarteel's listening. Muslim Pro's paid offline listening is the exception (earlier doc §2a).
- **SEA single-purpose apps prove demand for exactly these moments** (Ma'thurat, Yasin, ruqyah, Juz
  Amma). But they are ad-heavy and claim-heavy. The way to stand out is a sourced "why", honest
  wording and a calm UX.
- **Naming:** don't call Quran packs "playlists". In Indonesian/Malay, use "Bacaan …" or "Rangkaian
  bacaan". Use "Amalan" only where there's a sunnah basis. "Pack" is fine internally.

---

## 7. Ambient sound packs for SEA

Moved: see `docs/research_sounds.md`. This doc only suggests a default from the bundled ambient
sounds for each pack (§4, §5).

## 8. Rewarded ads for unlocking sound packs

Moved: see `docs/research_sounds.md`.

---

## 9. Recommended lineup

**v1 (at launch).** Seven packs, all free and never gated. A user who downloads all of them gets 38
unique files: **44.1 MB (Alafasy) or 54.6 MB (Sudais)** [1].

| # | Pack                                                                           | MB (Alafasy / Sudais)          | Why v1                                                                |
|---|--------------------------------------------------------------------------------|--------------------------------|-----------------------------------------------------------------------|
| 1 | Bacaan Sebelum Tidur (Sunnah), with short variant and the "4 Qul" label for MY | 20.5 / 27.3 (short: 4.8 / 8.6) | sahih basis; top demand; it is the app's core promise                 |
| 2 | Ayat Kursi Sebelum Tidur                                                       | 0.8 / 0.9                      | the biggest single format (56.1M); shares its file with #1            |
| 3 | Terjaga di Malam Hari                                                          | 4.8 / 4.9                      | sahih; completes the night; tiny                                      |
| 4 | Ayat Sakinah                                                                   | 3.1 / 3.2                      | huge calm demand (58.9M); clear sourcing story                        |
| 5 | Penenang Hati                                                                  | 4.8 / 7.1                      | core calm pack; sahih for the Yunus dua; Ad-Duha's revelation context |
| 6 | Surah Ar-Rahman                                                                | 10.9 / 12.3                    | the calm favourite (158.4M); simple                                   |
| 7 | Saat Berduka                                                                   | 2.7 / 3.7                      | sahih (Muslim 918); a gap no competitor serves; tiny                  |

**Later**

- **v1.1, by mid-January 2027 (ahead of Ramadan 1448):**
    - Satu Juz Semalam + Lailatul Qadar — the seasonal peak (earlier doc §6).
    - Al-Kahfi Hari Jumat, with the reading note.
- **v1.2 and after:**
    - Murottal Bayi, once a volume cap, safety copy and non-childlike visuals are in.
    - Perlindungan, using only Ibn Baz's set, with "lebih utama dibaca sendiri" (better to recite it
      yourself).
    - Murottal Ibu Hamil, framed by the Selangor guideline, with no outcome claims.
    - Tenang Sebelum Ujian.
    - Al-Baqarah di Rumah.
    - The 1b long add-on (Al-Isra & Az-Zumar).
    - Yasin Malam Jumat, with neutral copy about the difference of views.
    - Juz Amma Anak, only after a Families decision.
- **Don't ship as packs:** Ayat Syifa (healing claim), Al-Waqi'ah or "Ayat Seribu Dinar" (rizq
  claims), Al-Ma'thurat (dzikir audio missing), Ayat 7 (unverified). The surahs and ayat themselves
  stay in the normal library.

---

## 10. Open questions / unverified

1. **Scholar review.** The may/must-not lines need a reviewer, especially the Ayat Sakinah
   attribution, the protection card, the pregnancy card and the Yasin note (earlier doc: "Adab
   sheet").
2. **Al-Ma'thurat:**
    - The Kubra version's Quran list is **unverified** (3 searches).
    - CNN Indonesia's placement of 3:26–27 in the sughra should be checked against a printed
      edition [15].
3. **"Ayat 7 (pendinding)":** verse list **unverified** (2 searches).
4. **Ayat Syifa origin:** al-Qushayri's dream is known only from secondary retellings; no primary
   source was found.
5. **NU Jatim's "Yasin + As-Saffat" narration** is labelled "HR Abu Daud" but isn't in Sunan Abu
   Dawud [3][20]. The article itself calls its chain broken (*terputus*).
6. **Tirmidhi 2892 grading conflict:** hadith-api lists Albani as "da'if maqtu'", while IslamQA
   cites him authenticating it (earlier doc §5).
7. **Reciter files:**
    - Sudais's per-file bitrate varies (128/192 kbps), so his durations are ranges.
    - The other four reciters weren't measured beyond Al-Fatihah (1.4–1.8 MB versus 0.8 MB for
      Alafasy and Yasser).
8. **No verse timings on equran.id.** The timer can't stop at an ayah boundary inside full-surah
   files (Al-Baqarah, Al-Kahf, Yasin). The per-ayah alternative doubles Alafasy's Al-Baqarah size (
   64 → 128 kbps).
9. **Ramadan request volume:** up to about 170 per-ayah requests per juz per user, against
   equran.id's rate limits (earlier doc §5). Test on a throttled network.
10. **Families policy:** whether a parent-facing baby pack triggers Families review depends on
    visuals and targeting [37]. Decide before commissioning artwork.
11. **No SEA fatwa** was found on playing murottal to babies for sleep (Mufti WP searched). The
    Selangor guideline covers pregnancy and newborn duas only [27].
12. **Demand measures:** the numbers are lifetime YouTube views (including re-uploads) and
    autocomplete presence, not search volume (earlier doc §7). Muzammil Hasballah dominates the
    Indonesian results, but his audio isn't on equran.id (earlier doc §6).

---

## 11. Sources

1. cdn.equran.id — author's HTTP HEAD requests (367) and range reads (11, each ≤ 16 KB) on
   2026-10-03:
    - all 114 full-surah files for Misyari-Rasyid-Al-Afasi and Abdurrahman-as-Sudais;
    - 95 Alafasy and 38 Sudais per-ayah files;
    - Al-Fatihah for all 6 reciters.
    - MP3 frame and ID3 headers parsed locally.
    -
    Examples: https://cdn.equran.id/audio-full/Misyari-Rasyid-Al-Afasi/002.mp3 · https://cdn.equran.id/audio-partial/Misyari-Rasyid-Al-Afasi/002255.mp3 · https://cdn.equran.id/audio-full/Abdurrahman-as-Sudais/001.mp3 · https://cdn.equran.id/audio-partial/Abdurrahman-as-Sudais/048004.mp3
2. EQuran.id — Doa API documentation and responses (227 entries; fields
   `id, grup, nama, ar, tr, idn, tentang, tag`; no audio), accessed
   2026-10-03: https://equran.id/apidev/doa · https://equran.id/api/doa · https://equran.id/api/doa/1
3. fawazahmed0/hadith-api — English editions with Arabic (Abdul-Baqi) numbering and grades (Albani,
   Zubair Ali Zai, Bashar Awad, Shuaib al-Arnaut, Ahmad Shakir). Used to verify every hadith number,
   text and grade cited here; links in the text point to
   sunnah.com. https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/
4. Verse text and juz boundaries:
    - fawazahmed0/quran-api, `ara-quransimple` (6,236
      verses): https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/ara-quransimple.min.json
    - Tanzil metadata (`QuranData.Juz`): https://tanzil.net/res/text/metadata/quran-data.js (
      accessed 2026-10-03)
5. Ibn Qayyim al-Jawziyya, *Madarij al-Salikin*, "manzilat al-sakinah" — al-sakinah fi al-Kitab wa
   al-Sunnah, vol. 2 pp. 471–472 (Islamweb library
   edition): https://www.islamweb.net/ar/library/content/119/458/%D8%A7%D9%84%D8%B3%D9%83%D9%8A%D9%86%D8%A9-%D9%81%D9%8A-%D8%A7%D9%84%D9%83%D8%AA%D8%A7%D8%A8-%D9%88%D8%A7%D9%84%D8%B3%D9%86%D8%A9
6. IslamQA (Arabic) #141318 — Ayat al-sakinah (21 Nov 2009): https://islamqa.info/ar/answers/141318
7. IslamQA (Arabic) #123155 — Ruling on assigning specific verses to treat specific illnesses (5 Nov
   2008; cites the Permanent Committee): https://islamqa.info/ar/answers/123155
8. Ibn Baz — Fatwa 16666, verses and surahs recited over the
   sick: https://binbaz.org.sa/fatwas/16666/
9. Ibn Baz — Fatwa 1694, al-dawa' al-shar'i lil-sihr (Majmu'
   Fatawa): https://binbaz.org.sa/fatwas/1694/%D8%A7%D9%84%D8%AF%D9%88%D8%A7%D8%A1-%D8%A7%D9%84%D8%B4%D8%B1%D8%B9%D9%8A-%D9%84%D9%84%D8%B3%D8%AD%D8%B1
10. IslamQA #132384 — Does listening to a recorded ruqyah come under the heading of seeking
    ruqyah? (14 Oct 2019): https://islamqa.info/en/answers/132384
11. Islamweb — Fatwa #27039, Ruqyah using a recorded tape is invalid (8 Sep
    2011): https://www.islamweb.net/en/fatwa/27039/ruqyah-using-a-recorded-tape-is-invalid
12. secondary: UIN Antasari Banjarmasin repository, eprint 9948, Bab IV "Pemahaman Surah dan Ayat
    Ruqyah serta Penerapannya dalam Praktek Ruqyah Syar'iyyah" (thesis; interviews with
    practitioners at Pondok Sehat Al Wahida, January 2018; lists the references and verse sets
    practitioners use): https://idr.uin-antasari.ac.id/9948/7/BAB%20IV.pdf
13. NU Online — Jam'iyyah Ruqyah Aswaja
    reports: https://nu.or.id/daerah/ruqyah-aswaja-bukan-sekedar-pengobatan-alternatif-vkwwP · https://nu.or.id/daerah/ruqyah-cara-maksimalkan-kandungan-al-quran-untuk-penyembuhan-WIQt4 (
    accessed 2026-10-03)
14. secondary: Malay Mail — Obituary: Haron Din, the healer (17 Sep
    2016): https://www.malaymail.com/news/malaysia/2016/09/17/obituary-haron-din-the-healer/1207553
15. secondary: CNN Indonesia — Zikir Al Matsurat Sore Sugro Lengkap: Arab, Latin, dan Artinya (17
    Jun
    2025): https://www.cnnindonesia.com/edukasi/20250617143810-561-1240660/zikir-al-matsurat-sore-sugro-lengkap-arab-latin-dan-artinya
16. IslamQA #10700 — When to read Surat al-Kahf on Friday (3 Mar 2010; al-Hakim 2/399, al-Bayhaqi
    3/249, al-Darimi 3407; Sahih al-Jami' 6470/6471): https://islamqa.info/en/answers/10700
17. IslamQA #197900 — Is listening to Surat al-Kahf the same as reading it? (19 Feb
    2015): https://islamqa.info/en/answers/197900
18. IslamQA #69963 — Reciting Surat al-Baqarah in the home (4 Jun 2019; quotes Ibn 'Uthaymin, *Liqa'
    al-Bab al-Maftuh* no. 986): https://islamqa.info/en/answers/69963
19. IslamQA #2237 — Reading Soorat Yaa-Seen in congregation on Friday
    nights: https://islamqa.info/en/answers/2237
20. NU Online Jatim — Dalil Dianjurkannya Membaca Surat Yasin saat Malam Jumat (Syaifullah, 7 Jul
    2022): https://jatim.nu.or.id/keislaman/dalil-dianjurkannya-membaca-surat-yasin-saat-malam-jumat-YHMNu
21. NU Online Jatim — Waktu Membaca Surat Yasin dan Al-Kahfi menurut Kiai Ma'ruf Khozin (Sa'dullah,
    2 Sep
    2022): https://jatim.nu.or.id/madura/waktu-membaca-surat-yasin-dan-al-kahfi-menurut-kiai-ma-ruf-khozin-gU7n7
22. Majelis Tarjih dan Tajdid PP Muhammadiyah — Penjelasan mengenai Kegiatan Membaca Surat Yaasiin
    Bersama-sama Setiap Malam Jum'at (21 Apr
    2016): https://tarjih.or.id/penjelasan-mengenai-kegiatan-membaca-surat-yaasiin-bersama-sama-setiap-malam-jumat/
23. Suara Muhammadiyah — Fadhilah Berkaitan dengan Hari Jumat (Tito Yuwono, 3 Feb
    2023): https://web.suaramuhammadiyah.id/2023/02/03/fadhilah-berkaitan-dengan-hari-jumat/
24. Ibn Baz — Fatwa 17530, authenticity of "whoever recites Surat al-Waqi'ah every
    night": https://binbaz.org.sa/fatwas/17530/
25. NU Online — Keutamaan Membaca Surat Al-Waqi'ah: Selamat dari
    Kefakiran: https://islam.nu.or.id/syariah/keutamaan-membaca-surat-al-waqi-ah-selamat-dari-kefakiran-a9FNN
26. secondary: HadithAnswers — A Narration Regarding Surah Rahman (23 Oct 2018; Bayhaqi, *Shu'ab
    al-Iman* 2265, "very weak"): https://hadithanswers.com/?p=33984
27. Jabatan Mufti Negeri Selangor — Buku Garis Panduan Menyambut Kelahiran Anak (endorsed by the
    Selangor Fatwa Committee; PDF uploaded Aug 2023), §1.1.1 (suggested surahs) and §2.1.9 (duas for
    babies): https://www.muftiselangor.gov.my/wp-content/uploads/2023/08/GP-Kelahiran-Anak.pdf —
    read via the Internet Archive copy (the host didn't resolve on 2026-10-03)
28. Suara Muhammadiyah — Anak Saleh (5) (Mohammad Fakhrudin, 22 Aug
    2024): https://web.suaramuhammadiyah.id/2024/08/22/anak-saleh-5/
29. Muhammadiyah.or.id — Hukum Tujuh Bulanan Ibu Hamil dan Membacakan Talqin Saat Pemakaman (20 Mar
    2022): https://muhammadiyah.or.id/2022/03/hukum-tujuh-bulanan-ibu-hamil-dan-membacakan-talqin-saat/
30. secondary: Maulida R, Dasuki A, Faridatunnisa N. Surah dan Ayat Amalan Ibu Hamil: Studi Analisis
    Living Qur'an. *Syams: Jurnal Kajian Keislaman*,
    2021: https://jurnal.uin-palangkaraya.ac.id/syams/article/view/3090
31. YouTube search result pages (`hl=id&gl=ID` or `hl=ms&gl=MY`); view counts as displayed on
    2026-10-03 (lifetime, including re-uploads). Each video is
    at https://www.youtube.com/watch?v=ID:
    - Bedtime and Ayat al-Kursi: 8acT7arp_B8 · GJXvwsnZ-ww · lXA-9zpgdjE · ZcQaFWibnZo · qpULob0M-XY
    - Al-Baqarah: X2YnP50cwNU · z-C5dcw8Cd0 · W-5iFc8Y5qM
    - Baby and kids: YTSsRs3NXSU · BQQa7IgZQME · -M91zIG4Z24
    - Sakinah and comfort: jfZ_tCupttk · EBec54KMu58 · MTRU6DncwZA · B2tWlP3gXuc · Hy7Sk2k8zy8 ·
      cfnPYJcp2UU · TayaquS_7Q8
    - Ruqyah and Syifa: yDC4EpfpTY8 · HejfkigxjxA · rX_ybTce1K0 · 6H52-brhfi4
    - Ar-Rahman: tQHAwV9B8hQ · m_47v5-7l9s
    - Al-Kahf: HB1sQUmMUXE · -FxEYa8joK8
    - Yasin: Q--H5uqHP5s · od0zuvIJC5k
    - Al-Waqi'ah: I0GtsN9rIyk · 4DU2WeO0WHM
    - Juz and Al-Qadr: 3SGDKvAjpsQ · aR8jbCC5DJc · 3RLNnB4-0aQ
    - Al-Ma'thurat: MWDuKjgRIs0 · ynT6Zwgsz-M
    - Pregnancy: uBU30B75YTI · Lr6AuYuqheE
    - Exam: OYZ5tTnS07o
32. secondary signal: Google and YouTube autocomplete (suggestqueries.google.com, `client=firefox`,
    `ds=yt` for YouTube, `hl=id&gl=id` and `hl=ms&gl=my`); 28 pack seeds × 4 queries on 2026-10-03.
    Shows popular queries, not volumes.
    Example: https://suggestqueries.google.com/complete/search?client=firefox&ds=yt&hl=id&gl=id&q=murottal%20bayi
33. Google Play listings (`hl=en`, `gl=ID` unless noted; installs, rating, "Contains ads" and
    description as shown on 2026-10-03). Each listing is
    at https://play.google.com/store/apps/details?id=PACKAGE:
    - Quran and Islamic apps: Quranify `com.mchutov.Quranify` · Muslim Pro
      `com.bitsmedia.android.muslimpro` · Al Quran (Greentech) `com.greentech.quran` · Quran for
      Android `com.quran.labs.androidquran` · Pillars `com.pillars.pillars` · Tarteel
      `com.mmmoussa.iqra`
    - Al-Ma'thurat (gl=MY): Al Ma'tsurat `com.walukustudio.almatsurat` · Al-Ma'thurat Sughra & Kubra
      `com.dynoboyz.al_mathurat`
    - Yasin & Tahlil: Yasin dan Tahlil NU Lengkap `com.mungmedia.tahlil` · Yasin & Tahlil
      `com.madanistudio.yasintahlil` · Surah Yaseen & Tahlil Offline `com.beoke.suratyasindantahlil`
    - Ruqyah: Offline Ruqyah Mishary AlAfasy `com.ihsanapps.ruqyahalafasy` · Ayat Ruqyah Ampuh MP3
      `com.kakimestudio.MP3ayatayatruqyahampuh` · RUQYAH – Ayat Pendinding Diri
      `com.MegaApp.RUQYAH_PendindingDiri.Worldroid` · Ruqyah Bayi Rewel Susah Tidur
      `com.kopimantan.ruqyahbayi`
    - Kids: Juz Amma + Suara `com.solitekids.seciljuzamma` · Murottal Al Quran Anak 30 Juz
      `com.leforsa.murottalalqurananak`
34. Google Play listings (gl=ID, accessed 2026-10-03): Calm `com.calm.android` · Headspace
    `com.getsomeheadspace.android` · Rain Rain `com.timgostony.rainrain` · Tide `io.moreless.tide` ·
    BetterSleep `ipnossoft.rma.free`
35. BetterSleep Support — Is BetterSleep free? (updated 23 Jun
    2026): https://www.bettersleep.com/support/en/articles/15602562-is-bettersleep-free
36. Quran.com — Radio: curated and reciter stations (accessed 2026-10-03): https://quran.com/radio
37. Google Play Console Help — Manage target audience and app content
    settings: https://support.google.com/googleplay/android-developer/answer/9867159 (accessed
    2026-10-03)
38. HealthyChildren.org (American Academy of Pediatrics) — How to Keep Your Sleeping Baby Safe: AAP
    Policy Explained (updated 11 Sep
    2026): https://www.healthychildren.org/English/ages-stages/baby/sleep/Pages/a-parents-guide-to-safe-sleep.aspx
39. Quran.com verse pages for the sets above,
    e.g. https://quran.com/2/248 · https://quran.com/9/26 · https://quran.com/9/40 · https://quran.com/48/4 · https://quran.com/48/18 · https://quran.com/48/26 · https://quran.com/3/190-200 · https://quran.com/13/28 · https://quran.com/21/87 · https://quran.com/65/2-3 · https://quran.com/20/25-28 · https://quran.com/17/82
40. Earlier doc: `docs/research_white_noise_quran.md` (2026-10-03), cited by section.
