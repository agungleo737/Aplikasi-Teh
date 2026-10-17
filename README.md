# 🍵 Aplikasi Seduh Teh

Aplikasi Seduh Teh adalah proyek aplikasi pemesanan minuman teh yang dikembangkan untuk mempermudah pelanggan melihat produk dan melakukan pemesanan secara digital.

Repository ini menggabungkan backend berbasis Laravel, frontend web, dan komponen proyek Android.

## ✨ Fitur

- Menampilkan informasi produk teh.
- Menampilkan harga dan deskripsi produk.
- Antarmuka pemesanan minuman.
- Backend untuk mendukung pengelolaan data aplikasi.
- Integrasi database untuk menyimpan data aplikasi.

*Catatan: Fitur di atas perlu disesuaikan dengan implementasi yang benar-benar sudah tersedia.*

## 🛠️ Teknologi

| Teknologi | Kegunaan |
|---|---|
| PHP | Bahasa pemrograman backend |
| Laravel | Framework backend |
| MySQL | Database, jika dikonfigurasi |
| JavaScript | Kebutuhan frontend |
| Vite | Build tool frontend |
| Gradle | Build system proyek Android |
| Git & GitHub | Version control |

## 📁 Struktur Repository

```text
Aplikasi-Teh/
├── app/                # Kode backend Laravel
├── bootstrap/          # Bootstrap aplikasi Laravel
├── config/             # Konfigurasi aplikasi
├── database/           # Migration dan seeder
├── frontend/           # Komponen frontend
├── public/             # Aset publik
├── resources/          # Resource aplikasi
├── routes/             # Routing Laravel
├── tests/              # Pengujian aplikasi
├── build.gradle        # Konfigurasi Gradle
├── composer.json       # Dependency PHP
├── package.json        # Dependency JavaScript
├── settings.gradle     # Pengaturan proyek Gradle
└── README.md
```

## ⚙️ Instalasi Backend

### Prasyarat

- PHP dengan ekstensi yang dibutuhkan Laravel.
- Composer.
- Database sesuai konfigurasi proyek.
- Node.js dan npm jika frontend memerlukannya.

### Langkah instalasi

Clone repository:

```bash
git clone https://github.com/agungleo737/Aplikasi-Teh.git
cd Aplikasi-Teh
```

Instal dependency PHP:

```bash
composer install
```

Siapkan konfigurasi environment:

```bash
cp .env.example .env
php artisan key:generate
```

Pada Windows PowerShell, perintah penyalinan file dapat menggunakan:

```powershell
Copy-Item .env.example .env
```

Sesuaikan konfigurasi database pada `.env`, kemudian jalankan migration jika diperlukan oleh aplikasi:

```bash
php artisan migrate
```

Jalankan server backend:

```bash
php artisan serve
```

## 🖥️ Instalasi Frontend

Periksa apakah frontend memiliki `package.json` sendiri di dalam folder `frontend/`. Jika ada, masuk ke folder tersebut:

```bash
cd frontend
npm install
npm run dev
```

Jika `package.json` hanya berada di direktori utama, jalankan perintah npm dari direktori utama.

## 📱 Aplikasi Android

Repository juga memiliki konfigurasi Gradle. Untuk menjalankan bagian Android, buka proyek menggunakan Android Studio dan periksa konfigurasi SDK serta modul aplikasi yang tersedia.

## 🔐 Keamanan

- Jangan mengunggah file `.env` yang berisi kredensial.
- Gunakan `.env.example` untuk mendokumentasikan variabel konfigurasi.
- Jangan menyimpan password database atau API key secara langsung di source code.

## 🚧 Status Proyek

Proyek ini dikembangkan dan dapat mengalami perubahan selama proses pengembangan.

## 👨‍💻 Author

**Agung Leo**

GitHub: https://github.com/agungleo737

## 📄 License

Lisensi proyek belum ditentukan.
