<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\Produk;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class CheckoutController extends Controller
{
    public function store(Request $request)
    {
        $request->validate([
            'produk_id'   => 'required|integer|exists:produks,id',
            'jumlah_beli' => 'required|integer|min:1',
        ]);

        $produkId   = $request->produk_id;
        $jumlahBeli = $request->jumlah_beli;
        return DB::transaction(function () use ($produkId, $jumlahBeli) {
            $produk = Produk::lockForUpdate()->find($produkId);

            if (!$produk) {
                return response()->json([
                    'success' => false,
                    'message' => 'Produk tidak ditemukan.',
                ], 404);
            }

            // Cek stok mencukupi
            if ($produk->stok < $jumlahBeli) {
                return response()->json([
                    'success' => false,
                    'message' => 'Stok tidak mencukupi. Stok tersedia: ' . $produk->stok,
                ], 422);
            }

            // Kurangi stok
            $produk->stok -= $jumlahBeli;
            $produk->save();

            // Simpan order
            $order = Order::create([
                'produk_id'    => $produkId,
                'nama_produk'  => $produk->nama_teh,
                'jumlah_beli'  => $jumlahBeli,
                'harga_satuan' => $produk->harga,
                'total_harga'  => $produk->harga * $jumlahBeli,
            ]);

            return response()->json([
                'success' => true,
                'message' => 'Checkout berhasil! Stok berkurang ' . $jumlahBeli . ' unit.',
                'data'    => [
                    'order'     => $order,
                    'sisa_stok' => $produk->stok,
                ],
            ], 201);
        });
    }
}
