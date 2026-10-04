# Concept: Packs, Favorite Mixes and Sound Unlocks

**Date:** 2026-10-04 · **Status:** draft for the owner's decisions (§9)
**Builds on:**

- `research_packs.md` (packs, sources, sizes)
- `research_sounds.md` (sound catalog, mixing, rewarded ads)
- `research_white_noise_quran.md` (market, scholarly views)

**Fixed rules:**

- Southeast Asia first (ID, then MS, then EN); sleep and calm; focused on Quran listening plus
  ambient sound.
- Freemium with rewarded ads. **The Quran is always free and never interrupted by an ad.**
- No shuffle. No streaks or counters. No health or "therapy" claims.

---

## 1. The idea in one paragraph

Tonight, the user opens the app and taps **one pack**, for example "Bacaan Sebelum Tidur".

1. The pack plays a short, ordered set of recitations, each with its source shown.
2. When the recitation ends, their **favorite mix** of rain carries on and fades out.
3. If they want a new sound, such as the electric fan, they watch **one muted ad** in the daytime.
   That keeps it open for 7 days.

The flow is calm: one tap. Nothing counts, nags, or interrupts the Quran.

## 2. Principles

| Principle                 | What it means in the product                                                                                  |
|---------------------------|---------------------------------------------------------------------------------------------------------------|
| Quran first               | Recitation is never paywalled, never preceded or interrupted by an ad                                         |
| Honest sources            | Every pack shows **where its practice comes from** and never promises an outcome                              |
| Respect the cautious view | Packs default to ambient **after** the recitation; playing ambient **under** it is the user's choice          |
| Bedtime-safe              | Nothing loud or bright at night: ads only on an explicit tap, muted; sounds fade instead of playing all night |
| Small and offline         | A pack downloads in seconds (0.8–20 MB), not gigabytes                                                        |
| Calm                      | No streaks, badges, "nights kept", or nag notifications                                                       |

---

## 3. Packs (Bacaan)

### 3.1 Launch lineup: 7 free packs, 44 MB in total (Alafasy)

| # | Pack (ID)                         | Moment            | Contents, in fixed order                                                                            | Size                                                          | Source badge | Default ambient                | Default timer                                   |
|---|-----------------------------------|-------------------|-----------------------------------------------------------------------------------------------------|---------------------------------------------------------------|--------------|--------------------------------|-------------------------------------------------|
| 1 | **Bacaan Sebelum Tidur (Sunnah)** | lying down        | As-Sajdah → Al-Mulk → Ayat Kursi → Al-Baqarah 285–286 → Al-Ikhlas, Al-Falaq, An-Nas ×3 → Al-Kafirun | 20.5 MB (short version without As-Sajdah and Al-Mulk: 4.8 MB) | Sunnah       | Hujan Deras, **after**         | end of pack, then the ambient fades over 30 min |
| 2 | **Ayat Kursi Sebelum Tidur**      | quick bedtime     | 2:255 ×1 (user can pick ×3 or ×7)                                                                   | 0.8 MB                                                        | Sunnah       | Hujan Deras, after             | end of pack, then 30 min                        |
| 3 | **Terjaga di Malam Hari**         | waking at night   | Ali 'Imran 190–200, plus the waking dua as text                                                     | 4.8 MB                                                        | Sunnah       | none                           | end of pack; hint to dim the screen             |
| 4 | **Ayat Sakinah**                  | anxious, restless | 2:248 → 9:26 → 9:40 → 48:4 → 48:18 → 48:26                                                          | 3.1 MB                                                        | Amalan ulama | Gerimis, after, 10 min         | end of pack, then 10 min                        |
| 5 | **Penenang Hati**                 | heavy heart       | Ad-Duha → Ash-Sharh → 13:28 → 12:86 → 21:87 → 3:173 → 65:2–3 → 2:286                                | 4.8 MB                                                        | Pilihan ayat | Ombak Laut, after (optional)   | end of pack                                     |
| 6 | **Surah Ar-Rahman**               | calm evening      | Ar-Rahman                                                                                           | 10.9 MB                                                       | Pilihan ayat | Gerimis, after                 | end of surah                                    |
| 7 | **Saat Berduka**                  | grief, loss       | 2:155–157 → 12:86 → Ash-Sharh → 2:286, plus the istirja' dua as text                                | 2.7 MB                                                        | Sunnah       | none (Gerimis after, optional) | none                                            |

- **Ayat Sakinah's default sound changes.** Its research default was forest, which is now a locked
  sound, so it uses Gerimis. Pack defaults only use the 3 free sounds.
- **Malaysia:** the label "4 Qul" (Al-Kafirun + the 3 Quls) is shown in MY/BN as a quick option
  inside pack 1.

**Source badges.** Each pack shows exactly one, with the reference one tap away:

- **Sunnah**: the practice is in a sahih or hasan hadith (e.g. "HR Bukhari 2311").
- **Amalan ulama**: a scholars' practice, not a sunnah (Ayat Sakinah: Ibn Taymiyyah, reported by Ibn
  al-Qayyim).
- **Pilihan ayat**: verses chosen for their meaning; there is no special virtue to promise.
- **Tradisi**: a regional custom. Kept for later packs such as pregnancy or Yasin on Thursday night.

**Later packs:**

- **v1.1, for Ramadan 1448 (ship by mid-January 2027):**
    - Satu Juz Semalam (one juz a night) + Lailatul Qadar;
    - Al-Kahfi Hari Jumat (Friday).
- **v1.2 and later:**
    - Murottal Bayi (needs a volume cap and safety text);
    - Perlindungan (ruqyah: Ibn Baz's set, with "lebih utama dibaca sendiri", "it's better to recite
      it yourself");
    - Ibu Hamil (pregnancy); Tenang Sebelum Ujian (before an exam); Al-Baqarah di Rumah (Al-Baqarah
      at home); Yasin Malam Jumat (Yasin on Thursday night).
- **Never as packs:** Ayat Syifa (a healing claim), Al-Waqi'ah "for rizq" (weak hadith),
  Al-Ma'tsurat (its dzikir audio isn't available).

### 3.2 Where packs live: the main screen

```
┌──────────────────────────────────────┐
│ ☾ White Noise Quran              ☰   │
│            (artwork)                 │
│        الملك                          │
│  Bacaan Sebelum Tidur · 3/7 · Al-Mulk│  ← while a pack plays: pack name + step
│  ⏮      ⏸      ⏭      ⏱ end of pack  │     (⏮ ⏭ move between steps)
├──────────────────────────────────────┤
│ 🌙 Bacaan Malam             Lihat semua│  ← horizontal pack cards
│ ┌─────────┐ ┌─────────┐ ┌─────────┐  │
│ │Sebelum  │ │Ayat     │ │Ayat     │  │
│ │Tidur    │ │Kursi    │ │Sakinah  │  │
│ │Sunnah ✓ │ │Sunnah ✓ │ │Amalan   │  │  ← badge + ✓ = downloaded
│ │20 MB    │ │0.8 MB   │ │3 MB     │  │
│ └─────────┘ └─────────┘ └─────────┘  │
├──────────────────────────────────────┤
│ ||| Suara Latar   ⏸ 2 suara   Matikan│
│ Favorit: [＋ Simpan] [Malam Tenang]  │  ← favorite mixes (§4)
│          [Gerimis & Ombak]           │
│ ┌────────┐ ┌────────┐                │
│ │Hujan   │ │Gerimis │                │
│ │Deras   │ │        │                │
│ └────────┘ └────────┘                │
│ ┌────────┐ ┌────────┐                │
│ │Kipas 🔒│ │Brown 🔒│                │  ← locked sounds (§5)
│ └────────┘ └────────┘                │
└──────────────────────────────────────┘
```

"Lihat semua" opens the full pack list, grouped by moment: Tidur · Terjaga · Tenang · Berduka.

### 3.3 Pack sheet (tap a pack card)

```
┌──────────────────────────────────────┐
│ Bacaan Sebelum Tidur        [Sunnah] │
│ Bacaan yang diamalkan Nabi ﷺ sebelum │
│ tidur, lengkap dengan sumbernya.     │
│ 1. As-Sajdah            HR Tirmidzi ›│
│ 2. Al-Mulk              HR Tirmidzi ›│
│ 3. Ayat Kursi           HR Bukhari  ›│
│ 4. Al-Baqarah 285–286   HR Bukhari  ›│
│ 5. 3 Qul  ×3            HR Bukhari  ›│
│ 6. Al-Kafirun           HR Abu Dawud›│
│ ○ Versi pendek (tanpa 1–2) · 4.8 MB  │
│ Suara latar: Hujan Deras · setelah ▾ │
│ Timer: akhir bacaan + 30 mnt     ▾   │
│ ☐ Jeda untuk mengulang (ikuti qari)  │  ← "repeat after the reciter"
│ ⓘ Mendengarkan itu baik; membaca     │
│   sendiri lebih utama.               │  ← listening note
│ [ Unduh 20 MB ]      [ ▶ Putar ]     │
└──────────────────────────────────────┘
```

- **Putar before downloading** streams. **Unduh** downloads only this pack. A file shared with
  another pack (e.g. Ayat Kursi) is stored once.
- **The "›" on each step** opens the hadith reference and its grade, with a sunnah.com link.
- **"Jeda untuk mengulang"** pauses after each ayah about as long as the ayah took, so the listener
  can recite after the reciter. Scholars tie the virtue to *reciting*.

### 3.4 Playback rules

- **Order:** always the pack's order; ×3 repeats are honored. No shuffle.
- **⏮ / ⏭** move between steps. When the pack ends, it **doesn't run on into the next surah**.
- **Ambient modes**, per pack, with the user's last choice remembered:
    - **after** (default): rain starts when the recitation ends;
    - **under**: the mix plays during the recitation, as today;
    - **off**.
- **Timer:** the sleep sheet gains an **"Akhir bacaan"** (end of pack) option, followed by "rain
  after" for +15 / +30 / +60 minutes or all night.
    - Pack steps that use per-ayah files stop **exactly at the end of an ayah**.
    - Full surahs use the pause detector that's already built.
- **The same rule outside packs:** in the normal surah player, the ambient choice (under / after)
  works the same way.

### 3.5 Wording rules (ship only after a scholar reviews them)

- Each pack has one **"may say"** line and a **"must not say"** checklist, taken from
  `research_packs.md` §4.
    - **Never:** "dijamin terlindungi" (guaranteed protection), "tidur pasti nyenyak" (guaranteed
      sound sleep), "penghapus dosa" (erases sins), "100x untuk…", "obat cemas" (cure for
      anxiety), "terapi".
- Views that differ, such as Ayat Sakinah's status, get one neutral line, never a ruling.

---

## 4. Favorite mixes (Campuran Favorit)

**What:**

- Save the current ambient mix (sounds + volumes) under a name; tap later to bring it back.
- It sits on top of what already exists: the last mix is remembered between launches.

**Flow:**

1. Set up the mix as today: tap sounds, adjust volumes.
2. Tap **＋ Simpan**. A small dialog appears: "Nama campuran" with a suggested name such as "Hujan
   Deras + Gerimis". Tap **Simpan**.
3. The chip appears in the **Favorit** row. **Tap** applies the mix and plays it (the same as
   turning a sound on). **Long-press** offers rename or delete.

**Built-in favorites** (free sounds only, can't be deleted; they show users what a mix is):

- "Malam Tenang": Hujan Deras 50 %
- "Gerimis & Ombak": Gerimis 40 % + Ombak Laut 30 %

**Rules:**

- **Free and unlimited.** BetterSleep got 1★ reviews for putting mixes behind a paywall, and
  favorites are a retention tool, not a revenue tool.
- **Locked sounds in a favorite:** if a saved mix contains a sound whose 7 days have ended, the free
  sounds still play. The chip shows a small 🔒, with "Buka Kipas Angin 7 hari" offered, never forced.
- **Packs can use a favorite** as their "after" sound: the pack sheet's ambient picker lists
  favorites first.
- **Nothing is counted:** no "most played" list and no stats.

---

## 5. Sound catalog and unlocks

### 5.1 Catalog at launch (from `research_sounds.md` §10)

| Sound (ID)                              | Access     | Notes                                                  |
|-----------------------------------------|------------|--------------------------------------------------------|
| Hujan Deras                             | **Free**   | re-master: today it's far louder than the others       |
| Gerimis                                 | **Free**   | re-master (+14 LU)                                     |
| Ombak Laut                              | **Free**   |                                                        |
| Hutan & Kicau Burung                    | Ad, 7 days | best as "after" (birdsong is tonal)                    |
| Hujan & Kicau Burung                    | Ad, 7 days | loudest bird calls edited out                          |
| White / Pink / **Brown** noise          | Ad, 7 days | generated in-app (0 MB); brown masks the reciter least |
| **Kipas Angin** (electric fan)          | Ad, 7 days | own recording; **replaces Kereta Malam**               |
| Hujan di Atap Seng (rain on a tin roof) | Ad, 7 days | download on unlock                                     |
| Air Terjun (waterfall)                  | Ad, 7 days | download on unlock                                     |
| ~~Kereta Malam~~                        | retired    | rhythmic clatter sounds like a beat; no demand         |

Later: Sungai (river), Hujan di Hutan (rain in the forest), Hujan di Genteng (rain on roof tiles),
Hujan di Tenda (rain on a tent), Angin Sepoi (gentle breeze), Malam di Sawah (rice field at night).

**Engineering:**

- Normalize every loop to −23 LUFS.
- The default bed sits **18 LU under the recitation** and is never louder than −10 LU while
  recitation plays.
- Scale the mix down as more sounds are added.

### 5.2 Unlock flow

```
Tap 🔒 Kipas Angin
   │
   ▼  preview plays at once (45 s), card shows "Pratinjau 0:38"
┌──────────────────────────────────────┐
│ Buka Kipas Angin selama 7 hari?      │
│ Tonton 1 iklan singkat (tanpa suara).│
│ Murottal tidak pernah disela iklan.  │
│ [ Nanti ]          [ Tonton iklan ]  │
└──────────────────────────────────────┘
   │ (the recitation and mix pause, the ad plays muted, then they resume)
   ▼
Card: "Terbuka sampai 11 Okt"  ← a plain date, no countdown
```

**Rules:**

- **When ads may appear:** only after an explicit tap. Never at app start, never during or right
  before the recitation, and never on the notification or lock screen.
- **Ad settings:** muted, content rating G. Blocked categories: dating, sexual content, gambling,
  alcohol, social casino, get-rich-quick, religion, politics, sensationalism. Fortune-telling and
  online-loan ("pinjol") advertisers are blocked by URL.
- **Loading:** the ad loads only when a locked sound is tapped, since loaded ads expire after 1 h.
- **Offline:** "Butuh internet untuk membuka suara ini; 3 suara gratis tetap tersedia" (internet is
  needed to unlock this sound; the 3 free sounds are still available).
- **Expiry:** an unlock **never ends mid-playback**; it lapses at the next stop. The user can renew
  any time. There are no push reminders.
- **If ad revenue is thin after launch:** test 3 days per unlock. Never per play.

### 5.3 What ads change outside the app

- The Play listing shows **"Contains ads"**.
    - It may say: "Murottal tidak pernah disela iklan" (recitation is never interrupted by ads).
    - It must never say: "tanpa iklan" (no ads).
- **Data safety:** declare what the Ads SDK collects (approximate location, app interactions,
  diagnostics, device IDs).
- **Paperwork:** a privacy policy and the AD_ID declaration. Target audience: adults (18+), to stay
  out of Families rules.

---

## 6. Information architecture

```
Main screen (one scroll)
├── Player: surah, or pack + step
├── Bacaan Malam: pack cards → Pack sheet → (Unduh / Putar)
│                 └── Lihat semua → Pack list by moment
├── Suara Latar
│   ├── Favorit chips → ＋ Simpan dialog · long-press: rename / delete
│   └── Sound grid → 🔒 card → preview → Unlock sheet
└── Sheets that already exist: Surah list · Qari · Timer (+ "Akhir bacaan")
```

## 7. Data (sketch)

| Data                | Where                          | Shape                                                                                                                                                                                     |
|---------------------|--------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Pack definitions    | bundled in the app (versioned) | id, names {id, ms, en}, moment, badge, sources [ref, grade, url], steps [surah, fromAyah, toAyah, repeat], defaults {ambientMode, sound/mix, afterMinutes, timer}, mayNote, listeningNote |
| Pack download state | derived from files on disk     | per-ayah files in `audio/{slug}/ayah/{SSSAAA}.mp3`; full surahs reuse `audio/{slug}/{SSS}.mp3`                                                                                            |
| Favorite mixes      | Room `saved_mixes`             | id, name, builtIn, sounds [soundId, volume]                                                                                                                                               |
| Unlocks             | Room or DataStore              | soundId → unlockedUntil (date)                                                                                                                                                            |
| Last pack choices   | DataStore                      | ambient mode, after-minutes, short/long version                                                                                                                                           |

- **Room changes need no migration**: the app is unpublished and already falls back to a destructive
  migration.
- **Packs play as one ExoPlayer playlist**, with repeats expanded. The "3/7" in the player is
  position only, never a tally.

## 8. Phasing

| Phase                             | Ships                                                                                                                                               | Why first                                                           |
|-----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------|
| **1 (v1)**                        | 7 packs (stream + download, after-mode, "Akhir bacaan" timer, repeat-after-the-reciter) · favorite mixes · Kereta Malam retired · loops re-mastered | The core bedtime ritual; no ad SDK or privacy work needed yet       |
| **2 (v1.1, by mid-January 2027)** | Rewarded-ad unlocks + generated noise + Kipas Angin, Atap Seng, Air Terjun · Ramadan packs                                                          | Needs the recordings, AdMob setup, a privacy policy and Data safety |
| **3**                             | Baby, protection, pregnancy, exam and Yasin packs; more sounds                                                                                      | Each needs extra safety work, copy review or recordings             |

**Phase 1 without ads:** the 3 locked sounds stay free until phase 2 rather than showing a lock with
no way to open it.

## 9. Decisions for the owner

1. **Pack placement:** a row on the main screen (recommended) or a separate tab?
2. **Pack ambient default:** "after" (recommended: respects the cautious view) or "under"?
3. **Favorite mixes:** free and unlimited (recommended)?
4. **Ads:** in v1.1 with the new sounds (recommended), or already in v1?
5. **Retire Kereta Malam?** (recommended)
6. **Reviewers:**
    - Who checks the pack wording? An ustadz or lembaga before release.
    - Who checks the Malay strings?
