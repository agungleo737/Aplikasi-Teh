<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Produk extends Model
{
    protected $table = 'produks';
<<<<<<< HEAD
    protected $fillable = ['user_id', 'nama_teh', 'harga', 'kategori_id', 'deskripsi', 'gambar', 'gambar_full', 'stok'];

    // Sisipkan nama kategori 
    protected $appends = ['kategori'];

=======
    protected $fillable = ['nama_teh', 'harga', 'kategori', 'deskripsi', 'gambar', 'gambar_full', 'stok'];

>>>>>>> 54adf99378b1f88c47561a8e1ebee2f44065be40
    public function orders()
    {
        return $this->hasMany(Order::class, 'produk_id');
    }
<<<<<<< HEAD

    // Penjual / pemilik produk
    public function user()
    {
        return $this->belongsTo(User::class, 'user_id');
    }

    // Kategori 
    public function kategoriRelasi()
    {
        return $this->belongsTo(Kategori::class, 'kategori_id');
    }
    public function getKategoriAttribute(): ?string
    {
        return $this->kategoriRelasi?->nama_kategori;
    }
=======
>>>>>>> 54adf99378b1f88c47561a8e1ebee2f44065be40
}
