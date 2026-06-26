<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Produk extends Model
{
    protected $table = 'produks';
    protected $fillable = ['user_id', 'nama_teh', 'harga', 'kategori_id', 'deskripsi', 'gambar', 'gambar_full', 'stok'];

    // Sisipkan nama kategori 
    protected $appends = ['kategori'];

    public function orders()
    {
        return $this->hasMany(Order::class, 'produk_id');
    }

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
}
