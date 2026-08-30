# White Noise Quran — Android App (Jetpack Compose)
### v2 — Offline-First + Dynamic Sounds + Modern Player UI

---

## What Changed from v1

| Feature | v1 | v2 (this plan) |
|---|---|---|
| Quran audio | Stream live | **Download once, play offline** |
| Reciter selection | Settings | **Onboarding screen → bulk download** |
| Download state | ❌ | **WorkManager + per-surah Room tracking** |
| Sounds | Hardcoded enum | **DB-driven: add new sounds without code changes** |
| Reciters | Hardcoded enum | **DB-driven: add new reciters from API/config** |
| Player UI | Basic controls | **Modern full-screen music player** |

---

## Offline-First Strategy

### First Launch Flow
```
App opens
    │
    ▼
OnboardingScreen
    ├── Show 6 reciters with beautiful cards
    ├── User taps reciter
    │
    ▼
DownloadScreen
    ├── Enqueue WorkManager bulk download (114 MP3s)
    ├── Show overall progress bar
    ├── Show per-surah download grid (live updates via Flow)
    ├── "Ready: X / 114 surahs"
    │
    ▼ (all downloaded OR user taps "Play Available")
MainScreen (fully offline)
```

### Subsequent Launches
- All audio served from local files → **zero network dependency**
- User can switch reciter → triggers new download batch for that reciter
- Old reciter files kept (user can delete in Settings to free space)
- SoundPool ambient loops are bundled in `res/raw/` → always offline

---

## Architecture Overview

```
Clean Architecture + MVI + Offline-First

┌─────────────────────────────────────────────────┐
│                  UI Layer                        │
│  Onboarding → Download → Main (Player + Mixer)  │
│  ViewModel → UiState (sealed) → Compose         │
├─────────────────────────────────────────────────┤
│                 Domain Layer                     │
│  UseCases — pure Kotlin, no Android deps        │
│  Repository interfaces                          │
├─────────────────────────────────────────────────┤
│                  Data Layer                      │
│  Remote: Retrofit (metadata only after setup)   │
│  Local: Room (surah metadata + download state)  │
│  Files: Internal storage (downloaded MP3s)      │
│  Prefs: DataStore (reciter, volumes, timer)     │
├─────────────────────────────────────────────────┤
│               Background Layer                   │
│  WorkManager (bulk download per reciter)        │
│  AudioService (foreground, Media3 + SoundPool)  │
└─────────────────────────────────────────────────┘
```

**DI:** Hilt | **Async:** Coroutines + Flow | **Navigation:** Compose Navigation

---

## Dynamic Sound & Reciter System

### Why DB-driven?

Instead of hardcoded `enum class AmbientSound`, sounds and reciters are stored in Room. To add a new sound or reciter **in the future**, you only need to:
- Insert a new row into the database (via migration or remote config)
- Drop the MP3 into `res/raw/` (for sounds) or it streams from CDN slug (for reciters)
- **Zero code changes** to add either

### `AmbientSoundEntity` (Room)
```
id | nameDisplay   | rawResName     | iconName     | isEnabled | sortOrder
---|---------------|----------------|--------------|-----------|----------
1  | White Noise   | white_noise    | air          | true      | 1
2  | Rain          | rain           | thunderstorm | true      | 2
3  | Ocean Waves   | ocean          | waves        | true      | 3
4  | Forest Birds  | forest         | forest       | true      | 4
-- future rows added via Room migration --
```

### `ReciterEntity` (Room — pre-seeded from API)
```
id | displayName              | slug                     | apiKey
---|--------------------------|--------------------------|-------
1  | Abdullah Al-Juhany       | Abdullah-Al-Juhany       | 01
2  | Abdul Muhsin Al-Qasim    | Abdul-Muhsin-Al-Qasim    | 02
3  | Abdurrahman As-Sudais    | Abdurrahman-as-Sudais    | 03
4  | Ibrahim Al-Dossari       | Ibrahim-Al-Dossari       | 04
5  | Misyari Rasyid Al-Afasy  | Misyari-Rasyid-Al-Afasi  | 05
6  | Yasser Al-Dosari         | Yasser-Al-Dosari         | 06
-- future reciters added via DB migration or remote config --
```

### `SurahEntity` (Room)
```
nomor | nama | namaLatin | jumlahAyat | tempatTurun | arti
      | downloadState: NONE / DOWNLOADING / DONE / FAILED
      | localFilePath: String?   (per reciter — stored per reciter slug)
      | reciterId: Int
```

---

## Download System (WorkManager)

### `BulkDownloadWorker`
- Receives `reciterId` as input
- For each of 114 surahs:
  1. Check if file already exists locally → skip
  2. Download from CDN: `https://cdn.equran.id/audio-full/{slug}/{num3}.mp3`
  3. Save to: `filesDir/audio/{reciterSlug}/{num3digit}.mp3`
  4. Update `SurahEntity.downloadState = DONE` in Room
  5. Emit progress via `setProgress(workDataOf("completed" to count))`
- Runs in parallel batches of 5 (configurable)
- Supports **resume**: already-downloaded files are skipped on retry
- Constraints: Network required, but no Wifi-only (user choice)

### Download Progress UI
```
┌──────────────────────────────────┐
│  Downloading Al-Afasy...         │
│  ████████████░░░░░░░░  61 / 114  │
│                                  │
│  ┌──┬──┬──┬──┬──┬──┐            │
│  │✓ │✓ │✓ │⬇ │  │  │  ...      │  ← 19x6 surah grid
│  │ 1│ 2│ 3│ 4│ 5│ 6│            │    ✓=done ⬇=active □=pending
│  └──┴──┴──┴──┴──┴──┘            │
│                                  │
│  [Play Available Surahs]         │  ← Don't block user
└──────────────────────────────────┘
```

---

## Modern Player UI Design

### Screen Structure (Single Screen, Scrollable)

```
┌────────────────────────────────────────┐
│                                        │
│                                        │  ← Full-bleed
│   ✨ Beautiful animated visual         │     gradient art
│   (crescent + stars, pulsing glow      │     area (60% height)
│    synced to playback)                 │
│                                        │
│                                        │
│                                        │
├────────────────────────────────────────┤
│  ─────  DRAG HANDLE  ─────            │
│                                        │
│  الفاتحة                 ︎              │  ← Arabic (RTL), heart
│  Al-Fatihah · 7 Ayat                  │
│  Mishary Al-Afasy      ✎ Change       │
│                                        │
│  ─────────────────────────────────    │
│  0:00 ──●──────────────────── 4:31   │  ← Seek bar
│                                        │
│        ⏮    ⏸    ⏭                  │  ← Controls (large)
│   🔀                          ⏱ 45m  │  ← Shuffle + Timer
│                                        │
├────────────────────────────────────────┤
│  🎵 AMBIENT MIX                        │
│                                        │
│  ┌─────────┐ ┌─────────┐             │
│  │ 🌬      │ │ 🌧      │             │  ← Sound cards
│  │ White   │ │ Rain    │             │     (2 per row)
│  │ Noise   │ │         │             │
│  │ ████░░  │ │ ██████  │             │  ← Vertical sliders
│  │ 65%     │ │ 80%     │             │
│  └─────────┘ └─────────┘             │
│                                        │
│  ┌─────────┐ ┌─────────┐             │
│  │ 🌊      │ │ 🌿      │             │
│  │ Ocean   │ │ Forest  │             │
│  │         │ │         │             │
│  │  OFF    │ │ ████░░  │             │
│  │         │ │ 50%     │             │
│  └─────────┘ └─────────┘             │
│                                        │
│  [+ More sounds coming soon]           │  ← Teaser for future
└────────────────────────────────────────┘
```

### UI Details
- **Player artwork area**: Animated `Canvas` — crescent moon + particles + radial glow that pulses at ~60BPM when playing, freezes when paused
- **Seek bar**: Custom `Slider` with gradient track (navy → gold), large draggable thumb
- **Sound cards**: Frosted glass effect (`background.copy(alpha=0.3f)` + border), tap to toggle, long-press for fine volume control
- **Volume sliders**: Vertical orientation inside each card, shows % label
- **Ambient card state**: OFF = dark card + greyed icon; ON = glowing border + colored icon
- **Sleep timer display**: Pill badge on timer button showing remaining time
- **Transition animations**: Surah change → slide out old name left, slide in new right

---

## Complete Project Structure

```
app/src/main/java/com/whitenoisequran/
│
├── di/
│   ├── NetworkModule.kt
│   ├── DatabaseModule.kt
│   ├── RepositoryModule.kt
│   └── WorkerModule.kt
│
├── data/
│   ├── remote/
│   │   ├── QuranApiService.kt
│   │   └── dto/
│   │       └── SurahListDto.kt
│   ├── local/
│   │   ├── WhiteNoiseQuranDatabase.kt
│   │   ├── dao/
│   │   │   ├── SurahDao.kt
│   │   │   ├── ReciterDao.kt
│   │   │   └── AmbientSoundDao.kt
│   │   ├── entity/
│   │   │   ├── SurahDownloadEntity.kt  # per (surahId, reciterId)
│   │   │   ├── SurahMetadataEntity.kt
│   │   │   ├── ReciterEntity.kt
│   │   │   └── AmbientSoundEntity.kt
│   │   └── AppPreferences.kt          # DataStore
│   ├── worker/
│   │   └── BulkDownloadWorker.kt
│   └── repository/
│       ├── QuranRepositoryImpl.kt
│       ├── DownloadRepositoryImpl.kt
│       └── AmbientRepositoryImpl.kt
│
├── domain/
│   ├── model/
│   │   ├── Surah.kt
│   │   ├── Reciter.kt
│   │   ├── AmbientSound.kt
│   │   └── DownloadStatus.kt
│   ├── repository/
│   │   ├── QuranRepository.kt
│   │   ├── DownloadRepository.kt
│   │   └── AmbientRepository.kt
│   └── usecase/
│       ├── GetSurahListUseCase.kt
│       ├── GetRecitersUseCase.kt
│       ├── StartBulkDownloadUseCase.kt
│       ├── GetDownloadProgressUseCase.kt  # Flow<DownloadProgress>
│       ├── GetAmbientSoundsUseCase.kt
│       └── GetLocalAudioPathUseCase.kt
│
├── service/
│   ├── AudioService.kt
│   ├── AudioServiceConnection.kt
│   └── SleepTimerManager.kt
│
├── ui/
│   ├── theme/
│   │   ├── Theme.kt
│   │   ├── Color.kt
│   │   └── Type.kt
│   ├── onboarding/
│   │   ├── OnboardingScreen.kt     # Reciter selection
│   │   └── OnboardingViewModel.kt
│   ├── download/
│   │   ├── DownloadScreen.kt       # Progress grid
│   │   └── DownloadViewModel.kt
│   ├── main/
│   │   ├── MainScreen.kt
│   │   ├── MainViewModel.kt
│   │   └── MainUiState.kt
│   ├── components/
│   │   ├── PlayerArtwork.kt        # Animated canvas area
│   │   ├── SeekBar.kt              # Custom gradient seek bar
│   │   ├── PlayerControls.kt       # Prev/Play/Next + shuffle/timer
│   │   ├── AmbientSoundCard.kt     # Card with vertical slider
│   │   ├── AmbientMixSection.kt    # 2-column grid of sound cards
│   │   ├── SurahListSheet.kt       # Modal bottom sheet
│   │   ├── ReciterSheet.kt         # Modal bottom sheet
│   │   └── SleepTimerSheet.kt      # Modal bottom sheet
│   └── navigation/
│       └── AppNavHost.kt           # Onboarding → Download → Main
│
└── MainActivity.kt
```

---

## Key Technical Decisions

### Audio File Storage
```
filesDir/
└── audio/
    ├── Misyari-Rasyid-Al-Afasi/
    │   ├── 001.mp3
    │   ├── 002.mp3
    │   └── ...114.mp3
    └── Abdurrahman-as-Sudais/
        ├── 001.mp3
        └── ...
```

### Audio Playback (Offline)
- ExoPlayer uses `Uri.fromFile(localFile)` instead of streaming URL
- Falls back to CDN URL if file missing (graceful degradation)
- Auto-advance: `ExoPlayer.setMediaItems(playlist)` for all 114 surahs

### WorkManager Download
- `EXPEDITED` priority during active download
- Progress via `WorkInfo.progress` observed as `Flow`
- Retry policy: `BackoffPolicy.LINEAR`, 3 attempts per surah
- Download check: `File.exists() && File.length() > 10_000` (not corrupt)

---

## Navigation Graph

```
ONBOARDING ──→ DOWNLOAD ──→ MAIN
     │                        ↑
     └── (already set up) ────┘
```

---

## Verification Plan

### Automated
- `BulkDownloadWorkerTest` — verify file saved, Room updated, progress emitted
- `QuranRepositoryTest` — local-first path when file exists
- `MainViewModelTest` — ambient state management
- `GetAudioUrlUseCaseTest` — returns local path when downloaded

### Manual
1. Fresh install → Onboarding shows 6 reciter cards
2. Select reciter → Download screen with animated progress
3. "Play Available" works before download finishes
4. Airplane mode after download → all 114 surahs play offline
5. Switch reciter → new download batch starts for new reciter
6. All 4 ambient sounds play simultaneously with Quran
7. Each sound slider is independent (0–100%)
8. Sleep timer counts down and fades audio gracefully
9. Lock screen notification with controls works
10. Kill + reopen app → last surah + all volumes restored
