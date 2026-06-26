<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Produk extends Model
{
    protected $table = 'produks';
    protected $fillable = ['nama_teh', 'harga', 'kategori', 'deskripsi', 'gambar', 'gambar_full', 'stok'];

    public function orders()
    {
        return $this->hasMany(Order::class, 'produk_id');
    }
}
