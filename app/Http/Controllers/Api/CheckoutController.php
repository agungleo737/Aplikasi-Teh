<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Notifikasi;
use App\Models\Order;
use App\Models\Produk;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\DB;

class CheckoutController extends Controller
{    public function store(Request $request)
    {
        $request->validate([
            'produk_id'   => 'required|integer|exists:produks,id',
            'jumlah_beli' => 'required|integer|min:1',
        ]);
        $request->merge([
            'items' => [
                [
                    'produk_id' => $request->produk_id,
                    'jumlah_beli' => $request->jumlah_beli,
                ]
            ]
        ]);

        return $this->storeBatch($request);
    }
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

                    // Penjual tidak boleh membeli produk sendiri
                    if ($produk->user_id !== null && $produk->user_id === $userId) {
                        throw new \RuntimeException(
                            'Kamu tidak bisa membeli produk milikmu sendiri: ' . $produk->nama_teh
                        );
                    }

                    if ($produk->stok < $item['jumlah_beli']) {
                        throw new \RuntimeException(
                            'Stok "' . $produk->nama_teh . '" tidak mencukupi. Sisa: ' . $produk->stok
                        );
                    }

                    // Kurangi Stok
                    $produk->stok -= $item['jumlah_beli'];
                    $produk->save();

                    // Buat Order
                    $order = Order::create([
                        'user_id'      => $userId,
                        'produk_id'    => $produk->id,
                        'nama_produk'  => $produk->nama_teh,
                        'jumlah_beli'  => $item['jumlah_beli'],
                        'harga_satuan' => $produk->harga,
                        'total_harga'  => $produk->harga * $item['jumlah_beli'],
                    ]);

                    // Notifikasi ke penjual 
                    if ($produk->user_id !== null) {
                        Notifikasi::create([
                            'user_id'  => $produk->user_id,
                            'judul'    => 'Pesanan baru masuk',
                            'pesan'    => 'Pesanan baru: "' . $produk->nama_teh . '" (' . $item['jumlah_beli'] . ' unit). Segera verifikasi & kemas.',
                            'order_id' => $order->id,
                        ]);
                    }

                    $hasil[] = $order;
                }

                return $hasil;
            });

            // Hapus cache karena stok berubah 
            Cache::forget('produk_list');
            Cache::forget('produk_best_seller');

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
}
