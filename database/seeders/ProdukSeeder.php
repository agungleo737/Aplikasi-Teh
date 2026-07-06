<?php

namespace Database\Seeders;

use App\Models\Kategori;
use App\Models\Produk;
use App\Models\User;
use Illuminate\Database\Seeder;

class ProdukSeeder extends Seeder
{
    public function run(): void
    {
        $penjual = User::where('email', 'user@seduhteh.com')->first() ?? User::first();
        $penjualId = $penjual?->id;

        $kategoris = ['Hijau', 'Merah', 'Buah', 'Wangi', 'Pahit', 'Herbal', 'Melati', 'Susu'];
        $kategoriId = [];
        foreach ($kategoris as $nama) {
            $kategoriId[$nama] = Kategori::firstOrCreate(['nama_kategori' => $nama])->id;
        }

        $data = [
            ['Teh Hijau Melati',   'Hijau',  12000, 60, 'Teh hijau segar dengan aroma melati yang menenangkan.'],
            ['Teh Hijau Matcha',   'Hijau',  18000, 45, 'Matcha premium, pekat dan creamy khas Jepang.'],
            ['Teh Hijau Sencha',   'Hijau',  15000, 50, 'Sencha klasik dengan rasa rumput segar.'],
            ['Teh Hijau Tubruk',   'Hijau',   9000, 80, 'Teh hijau tubruk pekat, cocok diseduh panas.'],
            ['Teh Hijau Lemon',    'Hijau',  13000, 55, 'Perpaduan teh hijau dan lemon yang menyegarkan.'],

            ['Teh Merah Rosella',  'Merah',  14000, 40, 'Teh rosella merah dengan rasa asam menyegarkan.'],
            ['Teh Hitam Pekat',    'Merah',  10000, 70, 'Teh hitam pekat klasik, kuat dan harum.'],
            ['Teh Merah Manis',    'Merah',  11000, 65, 'Teh merah manis siap minum kapan saja.'],
            ['Teh Tarik Merah',    'Merah',  16000, 35, 'Teh tarik dengan warna merah pekat dan creamy.'],
            ['Teh Merah Original', 'Merah',   9500, 75, 'Teh merah original tanpa tambahan gula.'],

            ['Teh Apel',           'Buah',   13000, 50, 'Teh dengan potongan apel kering yang harum.'],
            ['Teh Leci',           'Buah',   14000, 48, 'Teh manis dengan aroma leci yang segar.'],
            ['Teh Stroberi',       'Buah',   14500, 42, 'Teh buah stroberi, manis dan wangi.'],
            ['Teh Mangga',         'Buah',   13500, 46, 'Teh tropis rasa mangga matang.'],
            ['Teh Markisa',        'Buah',   12500, 52, 'Teh markisa asam manis yang menyegarkan.'],

            ['Teh Wangi Bunga',    'Wangi',  12000, 58, 'Teh dengan campuran bunga kering wangi.'],
            ['Teh Wangi Pandan',   'Wangi',  11500, 60, 'Aroma pandan harum di setiap seduhan.'],
            ['Teh Wangi Vanila',   'Wangi',  13000, 50, 'Teh lembut dengan sentuhan vanila.'],
            ['Teh Wangi Sereh',    'Wangi',  10500, 64, 'Teh sereh hangat menenangkan badan.'],
            ['Teh Wangi Kayu Manis','Wangi', 12500, 47, 'Teh dengan aroma kayu manis yang khas.'],

            ['Teh Pahit Original', 'Pahit',   8000, 90, 'Teh pahit murni tanpa gula, mantap.'],
            ['Teh Daun Sirsak',    'Pahit',  11000, 44, 'Teh daun sirsak yang menyehatkan.'],
            ['Teh Pahit Tawar',    'Pahit',   8500, 85, 'Teh tawar klasik untuk teman makan.'],
            ['Teh Pahit Herbal',   'Pahit',  12000, 40, 'Racikan teh pahit herbal alami.'],
            ['Teh Pahit Pekat',    'Pahit',   9000, 78, 'Teh pahit pekat untuk pecinta teh kuat.'],

            ['Teh Jahe',           'Herbal', 13000, 55, 'Teh jahe hangat penghangat tubuh.'],
            ['Teh Chamomile',      'Herbal', 17000, 38, 'Teh chamomile menenangkan untuk relaksasi.'],
            ['Teh Peppermint',     'Herbal', 16000, 41, 'Teh peppermint segar dan menyegarkan napas.'],
            ['Teh Kunyit',         'Herbal', 12000, 49, 'Teh kunyit alami kaya manfaat.'],
            ['Teh Serai',          'Herbal', 11000, 53, 'Teh serai harum untuk relaksasi.'],

            ['Teh Melati Premium', 'Melati', 15000, 50, 'Teh melati premium pilihan kualitas terbaik.'],
            ['Teh Melati Klasik',  'Melati', 11000, 66, 'Teh melati klasik favorit keluarga.'],
            ['Teh Melati Madu',    'Melati', 14000, 44, 'Teh melati dengan tambahan madu alami.'],
            ['Teh Melati Hangat',  'Melati', 10000, 72, 'Teh melati hangat cocok untuk pagi hari.'],
            ['Teh Melati Dingin',  'Melati', 12000, 60, 'Teh melati dingin segar untuk siang hari.'],

            ['Teh Susu Original',  'Susu',   16000, 48, 'Teh susu creamy original yang lembut.'],
            ['Teh Susu Boba',      'Susu',   20000, 35, 'Teh susu dengan boba kenyal favorit.'],
            ['Teh Tarik Susu',     'Susu',   15000, 50, 'Teh tarik susu khas dengan busa lembut.'],
            ['Teh Susu Karamel',   'Susu',   21000, 30, 'Teh susu rasa karamel manis legit.'],
            ['Teh Susu Taro',      'Susu',   21000, 32, 'Teh susu taro ungu yang creamy.'],
        ];

        foreach ($data as $i => $row) {
            [$nama, $kategori, $harga, $stok, $deskripsi] = $row;

            $nomorFoto = ($i % 20) + 1;

            Produk::create([
                'user_id'     => $penjualId,
                'nama_teh'    => $nama,
                'kategori_id' => $kategoriId[$kategori],
                'harga'       => $harga,
                'deskripsi'   => $deskripsi,
                'gambar'      => "teh{$nomorFoto}.png",
                'gambar_full' => "img_teh{$nomorFoto}.png",
                'stok'        => $stok,
            ]);
        }
    }
}
