<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Kategori extends Model
{
    protected $table = 'kategoris';

    protected $fillable = ['nama_kategori', 'deskripsi'];

    // Relasi ke produk foreign key kategori_id
    public function produks()
    {
        return $this->hasMany(Produk::class, 'kategori_id');
    }
}
