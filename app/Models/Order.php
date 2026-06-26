<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Order extends Model
{
    protected $table = 'orders';

    protected $fillable = [
<<<<<<< HEAD
        'user_id',
=======
>>>>>>> 54adf99378b1f88c47561a8e1ebee2f44065be40
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
<<<<<<< HEAD

    // Pembeli
    public function user()
    {
        return $this->belongsTo(User::class, 'user_id');
    }
=======
>>>>>>> 54adf99378b1f88c47561a8e1ebee2f44065be40
}
