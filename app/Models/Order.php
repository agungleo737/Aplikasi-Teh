<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Order extends Model
{
    protected $table = 'orders';

    protected $fillable = [
        'user_id',
        'produk_id',
        'nama_produk',
        'jumlah_beli',
        'harga_satuan',
        'total_harga',
    ];

    public function produk()
    {
        return $this->belongsTo(Produk::class, 'produk_id');
    }

    // Pembeli
    public function user()
    {
        return $this->belongsTo(User::class, 'user_id');
    }
}
