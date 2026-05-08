
# OceanMinerGrow

Plugin addon Slimefun untuk Minecraft yang menambahkan mesin penambang otomatis bawah laut dengan sistem tier bertingkat. Dioptimasi untuk server besar (150–200+ player).

## Fitur

- 4 tier mesin penambang (MK1 → MK4) dengan peningkatan kecepatan, energi, dan storage
- 8 item unik yang hanya bisa didapatkan dari mesin
- Drop table berbasis kedalaman (zona Y) — semakin dalam semakin bagus
- Konsumsi energi sangat tinggi — dirancang berat bahkan untuk endgame player
- Sistem research bertingkat — tiap tier membutuhkan XP penelitian untuk membuka kunci
- Integrasi penuh dengan jaringan energi Slimefun
- Auto-export item ke container yang berdekatan
- Daftar possible drops ditampilkan di baris terakhir inventori mesin
- Informasi drop ditampilkan di Slimefun Guide (buku resep)
- Dioptimasi untuk ratusan mesin aktif secara bersamaan

---

## Mesin

| Tier | Item | Energy | Output | Storage | Kapasitas Baterai |
|------|------|--------|--------|---------|-------------------|
| MK1 | Ocean Miner | 75.000 J/tick | 1 item / 40 tick (2 det) | 9 slot (1 baris) | 750.000 J |
| MK2 | Ocean Miner MkII | 300.000 J/tick | 2 item / 30 tick (1,5 det) | 18 slot (2 baris) | 3.000.000 J |
| MK3 | Ocean Miner MkIII | 1.200.000 J/tick | 3 item / 20 tick (1 det) | 36 slot (4 baris) | 12.000.000 J |
| MK4 | Ocean Miner MkIV | 4.800.000 J/tick | 5 item / 15 tick (0,75 det) | 45 slot (5 baris) | 48.000.000 J |

> Sebagai referensi: Nuclear Reactor Slimefun menghasilkan ~1.024 J/tick.
> MK4 membutuhkan setara ~4.688 Nuclear Reactor untuk berjalan penuh.

### Syarat Penempatan
- Harus diletakkan **di dalam air** (blok Water atau Bubble Column)
- Koordinat Y **≤ 44**
- Minimal **1 blok air** di sisi yang bersebelahan agar beroperasi
- Mesin berhenti produksi otomatis saat storage penuh (energi tidak terbuang)

---

## Resep Craft

Semua mesin dibuat di **Enhanced Crafting Table**.

### Ocean Miner (MK1)
```
[Nautilus Shell]  [Nautilus Shell]    [Nautilus Shell]
[Prismarine Shard][Heart of the Sea]  [Prismarine Shard]
[Reinf. Alloy]    [Basic Circuit]     [Reinf. Alloy]
```

### Ocean Miner MkII
```
[Prismarine Brick][Prismarine Brick]  [Prismarine Brick]
[Tridentite Shard][Ocean Miner MK1]   [Tridentite Shard]
[Reinf. Alloy]    [Advanced Circuit]  [Reinf. Alloy]
```

### Ocean Miner MkIII
```
[Tidestone Frag]  [Tidestone Frag]    [Tidestone Frag]
[Pressure Gem]    [Ocean Miner MK2]   [Pressure Gem]
[Reinf. Alloy]    [Electric Motor]    [Reinf. Alloy]
```

### Ocean Miner MkIV
```
[Abyssal Core]    [Abyssal Core]      [Abyssal Core]
[Void Crystal]    [Ocean Miner MK3]   [Void Crystal]
[Reinf. Alloy]    [Cargo Motor]       [Reinf. Alloy]
```

---

## Research (Unlock)

Setiap tier memerlukan research untuk bisa digunakan. Research dibeli dengan XP level di Slimefun Guide.

| Research | Item yang Dibuka | XP |
|----------|------------------|----|
| Ocean Miner MK1 | Ocean Miner · Coral Dust · Pearlstone | 100 XP |
| Ocean Miner MkII | Ocean Miner MkII · Abyssite · Tridentite Shard | 150 XP |
| Ocean Miner MkIII | Ocean Miner MkIII · Pressure Gem · Tidestone Fragment | 200 XP |
| Ocean Miner MkIV | Ocean Miner MkIV · Abyssal Core · Void Crystal | 250 XP |

---

## Item Drop

### Ringkasan Ketersediaan

| Item | Rarity | Tersedia di |
|------|--------|-------------|
| Coral Dust | Common | MK1–MK4, semua zona (Y ≤ 44) |
| Pearlstone | Common | MK1–MK4, semua zona (Y ≤ 44) |
| Abyssite | Uncommon | MK1–MK2 (Y ≤ 24) · MK3–MK4 (Y ≤ 44) |
| Tridentite Shard | Uncommon | MK1–MK4 (Y ≤ 24) |
| Tidestone Fragment | Rare | MK2–MK4, semua zona (Y ≤ 44) |
| Pressure Gem | Rare | MK1–MK3 (Y < 0) · MK4 (Y ≤ 24) |
| Abyssal Core | Epic | MK3 (Y ≤ 24) · MK4 (Y ≤ 44) |
| Void Crystal | Legendary | MK4, semua zona (Y ≤ 44) |

---

## Probabilitas Drop per Zona

Semua angka adalah persentase (%) per roll saat tick produksi. *(no drop)* artinya roll menghasilkan tidak ada item untuk tick tersebut.

### MK1

| Item | Zona A (Y 25–44) | Zona B (Y 0–24) | Zona C (Y < 0) |
|------|:----------------:|:---------------:|:--------------:|
| Coral Dust | 45% | 45% | 45% |
| Pearlstone | 30% | 30% | 30% |
| Abyssite | — | 15% | 15% |
| Tridentite Shard | — | 8% | 8% |
| Pressure Gem | — | — | 2% |
| *(no drop)* | **25%** | **2%** | — |

### MK2

| Item | Zona A (Y 25–44) | Zona B (Y 0–24) | Zona C (Y < 0) |
|------|:----------------:|:---------------:|:--------------:|
| Coral Dust | 40% | 35% | 35% |
| Pearlstone | 30% | 25% | 25% |
| Tidestone Fragment | 20% | 8% | 10% |
| Abyssite | — | 18% | 15% |
| Tridentite Shard | — | 12% | 10% |
| Pressure Gem | — | — | 5% |
| *(no drop)* | **10%** | **2%** | — |

### MK3

| Item | Zona A (Y 25–44) | Zona B (Y 0–24) | Zona C (Y < 0) |
|------|:----------------:|:---------------:|:--------------:|
| Coral Dust | 35% | 30% | 25% |
| Pearlstone | 25% | 20% | 15% |
| Abyssite | 20% | 20% | 18% |
| Tidestone Fragment | 15% | 10% | 12% |
| Tridentite Shard | — | 15% | 12% |
| Abyssal Core | — | 5% | 8% |
| Pressure Gem | — | — | 10% |
| *(no drop)* | **5%** | — | — |

### MK4

| Item | Zona A (Y 25–44) | Zona B (Y 0–24) | Zona C (Y < 0) |
|------|:----------------:|:---------------:|:--------------:|
| Coral Dust | 30% | 25% | 20% |
| Pearlstone | 20% | 15% | 10% |
| Abyssite | 20% | 18% | 15% |
| Tidestone Fragment | 20% | 12% | 12% |
| Tridentite Shard | — | 12% | 10% |
| Abyssal Core | 8% | 12% | 15% |
| Pressure Gem | — | 4% | 8% |
| Void Crystal | 2% | 2% | 10% |

---

## Zona Drop (berdasarkan Y)

| Zona | Koordinat Y | Keterangan |
|------|-------------|------------|
| A | Y 25 – 44 | Drop umum; item langka mulai muncul di tier tinggi |
| B | Y 0 – 24 | Uncommon & epic mulai tersedia |
| C | Y < 0 | Semua item tersedia, persentase rare/legendary tertinggi |

Semakin tinggi tier mesin, semakin besar peluang drop langka di semua zona.

---

## Slimefun Guide (Buku Resep)

### Tampilan Mesin
Saat membuka halaman mesin di Slimefun Guide, pemain dapat melihat:
1. **Resep craft** — bahan yang dibutuhkan di Enhanced Crafting Table
2. **Daftar drop** — semua item yang bisa dihasilkan mesin tersebut (ditampilkan via custom RecipeType `ocean_miner_drop`)

### Tampilan di Inventori Mesin
Baris terakhir inventori mesin (slot 45–53) menampilkan daftar item yang bisa dihasilkan tier tersebut:

| Tier | Item yang Ditampilkan |
|------|-----------------------|
| MK1 | Coral Dust · Pearlstone · Abyssite · Tridentite Shard · Pressure Gem |
| MK2 | + Tidestone Fragment |
| MK3 | + Abyssal Core |
| MK4 | + Void Crystal |

### Tampilan Material Drop
Saat membuka halaman item drop (misal Coral Dust) di Slimefun Guide, pemain dapat melihat ikon mesin dan resep kosong (custom RecipeType `ocean_miner_drop`).

---

## Dependensi

- [Paper](https://papermc.io/) 1.21+
- [Slimefun4](https://github.com/Slimefun/Slimefun4) 2024.3+
- [GuizhanLibPlugin](https://github.com/ybw0014/GuizhanLibs) 1.8+ *(opsional)*

---

## Build

```bash
mvn package
```

Output JAR ada di folder `target/`. Letakkan di folder `plugins/` server dan restart.

---

## Struktur Kode

```
src/main/java/com/github/Syaaddd/oceanMinerGrow/
├── OceanMinerGrow.java        # Entry point plugin
├── items/
│   └── OceanMinerItems.java   # Definisi semua SlimefunItemStack
├── machines/
│   └── OceanMiner.java        # Logika mesin (ticker, drop, energi)
└── setup/
    ├── ItemSetup.java         # Registrasi item, mesin, dan custom RecipeType
    └── ResearchSetup.java     # Registrasi research unlock per tier
```

---

## Catatan Performa

Plugin ini dioptimasi untuk server dengan banyak pemain dan mesin:

- **Satu static map** untuk semua tier — menggantikan 4 map terpisah
- **Koordinat dikodekan sebagai `long`** — menghindari alokasi `Location` object di setiap tick
- **`ThreadLocalRandom`** — lebih cepat dari `Random` biasa
- **Tidak konsumsi energi saat chest penuh** — mencegah energi terbuang sia-sia
- **Pembersihan otomatis** saat mesin dipecah — mencegah memory leak jangka panjang

Diuji untuk ~1.000 mesin aktif bersamaan (200 player × 5 mesin/player).

---

## Lisensi

MIT License — bebas digunakan dan dimodifikasi.
