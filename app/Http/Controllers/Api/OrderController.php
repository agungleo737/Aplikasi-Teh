<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Order;
use Illuminate\Http\Request;

class OrderController extends Controller
{
    public function index(Request $request)
    {
        $orders = Order::with('produk:id,gambar')
            ->where('user_id', $request->user()->id)
            ->orderByDesc('created_at')
            ->get()
            ->map(function ($o) {
                return [
                    'id'           => $o->id,
                    'tanggal'      => $o->created_at->format('Y-m-d H:i:s'),
                    'nama_produk'  => $o->nama_produk,
                    'jumlah_beli'  => $o->jumlah_beli,
                    'harga_satuan' => $o->harga_satuan,
                    'total_harga'  => $o->total_harga,
                    'gambar'       => $o->produk->gambar ?? null,
                ];
            });

        return response()->json([
            'success' => true,
            'message' => 'Riwayat Pesanan',
            'data'    => $orders,
        ]);
    }
}
