# SIX CLIENT UI (Fabric, client-side)

Custom splash, ESC menu, dan mod menu bertema hitam-merah.

## STATUS JUJUR
- **BELUM DIKOMPILASI. BELUM ADA JAR. BELUM DIUJI DI GAME.**
  Dibuat di sandbox tanpa internet, tanpa `javac`, tanpa Gradle.
- Versi Loom / Loader / Fabric API / id plugin / Java di `build.gradle` & `gradle.properties`
  adalah **tebakan** (ditandai `(?)`). Ambil nilai resmi: https://fabricmc.net/develop (pilih 26.2).
- Nama class/method Minecraft ditulis dari pengetahuan 1.21.x/26.1 (Mojang names).
  26.2 mungkin mengubahnya (lihat daftar risiko). Yang sudah terverifikasi hanya: JSON valid, asset valid.

## Build
```
# Java 25 (atau sesuai kebutuhan MC 26.2), lalu:
./gradlew build        # (jalankan `gradle wrapper` dulu bila belum ada gradlew)
# hasil: build/libs/sixclientui-0.1.0.jar  (jangan pakai *-sources.jar)
```
Salin jar ke `.minecraft/mods/` bersama Fabric API.

## Titik risiko compile (kirim error-nya, mudah diperbaiki karena terpusat)
1. `util/Gfx.java` - `GuiGraphics`, `fill`, `drawString`, `blit(RenderPipelines.GUI_TEXTURED, ...)`, `Identifier`
   (di 26.2 mungkin `GuiGraphicsExtractor`, `text`, dll).
2. Override render: `SixButton.renderWidget`, `SixPauseScreen.render/renderBackground`,
   `SixModMenuScreen.render/renderBackground/mouseScrolled` (mungkin jadi `extract*`).
3. `SixPauseScreen.leave()` - alur disconnect meniru vanilla 1.21.x; `OptionsScreen` & package-nya.
4. `LoadingOverlayMixin` - `render` pada `LoadingOverlay`. `defaultRequire=0` -> kalau gagal match,
   splash kustom dilewati tanpa crash.
5. `build.gradle` - id plugin Loom, `release = 25`.

## Cara kerja
- **Splash**: mixin menggambar layar hitam + logo + bar *indeterminate* di atas `LoadingOverlay` vanilla.
  Logika reload vanilla tidak dibatalkan (startup tidak terkunci). Tidak ada persentase palsu.
- **ESC menu**: tick handler mengganti `PauseScreen` dengan `SixPauseScreen` (tanpa mixin) hanya saat
  `mc.level != null`. Tombol "Vanilla Menu" membuka menu asli (Advancements, Statistics, Open to LAN, dll.).
  Disconnect (server) / Save and Quit to Title (singleplayer) dipisah berdasar `isLocalServer()`.
- **Mod menu**: tab, search (memfilter), Reset, grid responsif, scroll mouse, Tab/arrow bawaan Minecraft.
  Hanya 2 kartu fungsional: Custom ESC Menu & Custom Splash. Sisanya **Coming Soon** (disabled).
  Belum ada keybind pembuka; saat ini dibuka dari ESC menu -> Mods.
- **Config**: `config/sixclientui.json`.

## Asset (ganti file dengan nama & ukuran piksel sama, atau ubah `util/Theme.java`)
`src/main/resources/assets/sixclientui/textures/gui/`
- `logo.png` (800x525, transparan) - dari upload Anda, background putih dihapus otomatis
- `background.png` (1600x1003) - belum dipakai kode; disiapkan untuk title/panel. Hapus bila ingin jar lebih kecil.
- `../icon.png` ikon mod (128x128)
Tidak ada asset FASTCLIENT yang dipakai.

## Belum diimplementasikan
Ikon utilitas di header ESC, tombol Folder/Screenshot, semua kartu HUD/Render/Movement/Player,
keybind mod menu, fade-out splash, integrasi progress nyata.
