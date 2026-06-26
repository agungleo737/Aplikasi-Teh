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
<<<<<<< HEAD
        $userId     = $request->user()->id;   
        return DB::transaction(function () use ($produkId, $jumlahBeli, $userId) {
=======
        return DB::transaction(function () use ($produkId, $jumlahBeli) {
>>>>>>> 54adf99378b1f88c47561a8e1ebee2f44065be40
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
<<<<<<< HEAD
                'user_id'      => $userId,
=======
>>>>>>> 54adf99378b1f88c47561a8e1ebee2f44065be40
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
<<<<<<< HEAD

    public function storeBatch(Request $request)
    {
        $data = $request->validate([
            'items'                => 'required|array|min:1',
            'items.*.produk_id'    => 'required|integer|exists:produks,id',
            'items.*.jumlah_beli'  => 'required|integer|min:1',
        ]);

        $userId = $request->user()->id;

        try {
            $orders = DB::transaction(function () use ($data, $userId) {
                $hasil = [];

                foreach ($data['items'] as $item) {
                    $produk = Produk::lockForUpdate()->find($item['produk_id']);

                    if (!$produk) {
                        throw new \RuntimeException('Produk tidak ditemukan.');
                    }

                    if ($produk->stok < $item['jumlah_beli']) {
                        throw new \RuntimeException(
                            'Stok "' . $produk->nama_teh . '" tidak mencukupi. Sisa: ' . $produk->stok
                        );
                    }

                    $produk->stok -= $item['jumlah_beli'];
                    $produk->save();

                    $hasil[] = Order::create([
                        'user_id'      => $userId,
                        'produk_id'    => $produk->id,
                        'nama_produk'  => $produk->nama_teh,
                        'jumlah_beli'  => $item['jumlah_beli'],
                        'harga_satuan' => $produk->harga,
                        'total_harga'  => $produk->harga * $item['jumlah_beli'],
                    ]);
                }

                return $hasil;
            });
        } catch (\RuntimeException $e) {
            return response()->json([
                'success' => false,
                'message' => $e->getMessage(),
            ], 422);
        }

        return response()->json([
            'success' => true,
            'message' => 'Checkout berhasil!',
            'data'    => [
                'orders' => $orders,
            ],
        ], 201);
    }
=======
>>>>>>> 54adf99378b1f88c47561a8e1ebee2f44065be40
}
