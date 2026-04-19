
# OceanMinerGrow

Plugin addon Slimefun untuk Minecraft yang menambahkan mesin penambang otomatis bawah laut dengan sistem tier bertingkat. Dioptimasi untuk server besar (150–200+ player).

## Fitur

- 4 tier mesin penambang (MK1 → MK4) dengan peningkatan kecepatan, energi, dan storage
- 8 item unik yang hanya bisa didapatkan dari mesin
- Drop table berbasis kedalaman (zona Y) — semakin dalam semakin bagus
- Konsumsi energi tinggi — dirancang berat bahkan untuk endgame player
- Integrasi penuh dengan jaringan energi Slimefun
- Auto-export item ke container yang berdekatan
- Dioptimasi untuk ratusan mesin aktif secara bersamaan

---

## Mesin

| Tier | Item | Energy | Output | Storage | Kapasitas Baterai |
|------|------|--------|--------|---------|-------------------|
| MK1 | Ocean Miner | 500 J/tick | 1 item / 2 det | 9 slot | 5.000 J |
| MK2 | Ocean Miner MkII | 2.000 J/tick | 2 item / 1.5 det | 18 slot | 20.000 J |
| MK3 | Ocean Miner MkIII | 8.000 J/tick | 3 item / 1 det | 26 slot | 80.000 J |
| MK4 | Ocean Miner MkIV | 32.000 J/tick | 5 item / 0.75 det | 26 slot | 320.000 J |

> Sebagai referensi: Nuclear Reactor Slimefun menghasilkan ~1.024 J/tick.
> MK4 membutuhkan setara ~32 Nuclear Reactor untuk berjalan penuh.

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

## Item Drop

### Common
| Item | Deskripsi |
|------|-----------|
| Coral Dust | Serbuk halus dari koral kuno |
| Pearlstone | Batu dengan kilau mutiara |

### Uncommon
| Item | Deskripsi |
|------|-----------|
| Abyssite | Kristal dari kedalaman jurang |
| Tridentite Shard | Pecahan senjata laut kuno |

### Rare
| Item | Tersedia di | Deskripsi |
|------|-------------|-----------|
| Pressure Gem | MK1+ (Y < 0) | Terkristalisasi oleh tekanan samudra |
| Tidestone Fragment | MK2+ | Resonansi energi pasang surut |

### Epic
| Item | Tersedia di | Deskripsi |
|------|-------------|-----------|
| Abyssal Core | MK3+ | Inti berdenyut dari jurang terdalam |

### Legendary
| Item | Tersedia di | Deskripsi |
|------|-------------|-----------|
| Void Crystal | MK4 saja | Kristal lahir dari kekosongan bawah samudra |

---

## Zona Drop (berdasarkan Y)

| Zona | Koordinat Y | Keterangan |
|------|-------------|------------|
| A | Y 25 – 44 | Drop umum saja |
| B | Y 0 – 24 | Drop uncommon mulai tersedia |
| C | Y < 0 | Drop rare & legendary paling tinggi |

Semakin tinggi tier mesin, semakin besar peluang drop langka di semua zona.

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
    └── ItemSetup.java         # Registrasi item & mesin ke Slimefun
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
