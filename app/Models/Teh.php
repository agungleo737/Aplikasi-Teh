<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Teh extends Model
{
    protected $table = 'tehs';
    protected $fillable = [
        'gambar', 
        'nama_teh', 
        'kategori', 
        'harga', 
        'stok', 
        'deskripsi'
    ];
}
