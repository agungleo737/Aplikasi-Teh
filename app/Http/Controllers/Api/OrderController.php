<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\Notifikasi;
use Illuminate\Http\Request;

class OrderController extends Controller
{
    // Riwayat pesanan milik pembeli 
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
                    'status'       => $o->status,
                    'estimasi_siap'  => $o->estimasi_siap,
                    'estimasi_kirim' => $o->estimasi_kirim,
                    'gambar'       => $o->produk->gambar ?? null,
                ];
            });

        return response()->json([
            'success' => true,
            'message' => 'Riwayat Pesanan',
            'data'    => $orders,
        ]);
    }

    // Pesanan masuk untuk penjual
    public function penjualan(Request $request)
    {
        $penjualId = $request->user()->id;

        $orders = Order::with(['produk:id,nama_teh,gambar,user_id', 'user:id,name'])
            ->whereHas('produk', fn ($q) => $q->where('user_id', $penjualId))
            ->orderByDesc('created_at')
            ->get()
            ->map(function ($o) {
                return [
                    'id'           => $o->id,
                    'tanggal'      => $o->created_at->format('Y-m-d H:i:s'),
                    'nama_produk'  => $o->nama_produk,
                    'nama_pembeli' => $o->user->name ?? '-',
                    'jumlah_beli'  => $o->jumlah_beli,
                    'harga_satuan' => $o->harga_satuan,
                    'total_harga'  => $o->total_harga,
                    'status'       => $o->status,
                    'estimasi_siap'  => $o->estimasi_siap,
                    'estimasi_kirim' => $o->estimasi_kirim,
                    'gambar'       => $o->produk->gambar ?? null,
                ];
            });

        return response()->json([
            'success' => true,
            'message' => 'Pesanan Masuk',
            'data'    => $orders,
        ]);
    }

    // Penjual memverifikasi
    public function proses(Request $request, $id)
    {
        $data = $request->validate([
            'estimasi_siap'  => 'required|date|after_or_equal:today',
            'estimasi_kirim' => 'required|date|after_or_equal:estimasi_siap',
        ]);

        $order = Order::with('produk:id,user_id')->find($id);

        if (!$order) {
            return response()->json([
                'success' => false,
                'message' => 'Pesanan tidak ditemukan.',
            ], 404);
        }

        // pemilik produk yang memproses
        if (($order->produk->user_id ?? null) !== $request->user()->id) {
            return response()->json([
                'success' => false,
                'message' => 'Kamu hanya bisa memproses pesanan produkmu sendiri.',
            ], 403);
        }

        if ($order->status !== 'menunggu') {
            return response()->json([
                'success' => false,
                'message' => 'Pesanan ini tidak bisa diproses (status: ' . $order->status . ').',
            ], 422);
        }

        $order->status         = 'dikemas';
        $order->estimasi_siap  = $data['estimasi_siap'];
        $order->estimasi_kirim = $data['estimasi_kirim'];
        $order->save();

        // Notifikasi ke pembeli
        Notifikasi::create([
            'user_id'  => $order->user_id,
            'judul'    => 'Pesanan sedang dikemas',
            'pesan'    => 'Pesanan "' . $order->nama_produk . '" sedang dikemas. '
                . 'Estimasi siap ' . $data['estimasi_siap'] . ', dikirim ' . $data['estimasi_kirim'] . '.',
            'order_id' => $order->id,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Pesanan ditandai sedang dikemas.',
            'data'    => [
                'id'             => $order->id,
                'status'         => $order->status,
                'estimasi_siap'  => $order->estimasi_siap,
                'estimasi_kirim' => $order->estimasi_kirim,
            ],
        ]);
    }

    // Penjual mengirim pesanan
    public function kirim(Request $request, $id)
    {
        $order = Order::with('produk:id,user_id')->find($id);

        if (!$order) {
            return response()->json([
                'success' => false,
                'message' => 'Pesanan tidak ditemukan.',
            ], 404);
        }

        // Hanya pemilik produk yang boleh mengirim
        if (($order->produk->user_id ?? null) !== $request->user()->id) {
            return response()->json([
                'success' => false,
                'message' => 'Kamu hanya bisa mengirim pesanan produkmu sendiri.',
            ], 403);
        }

        if ($order->status !== 'dikemas') {
            return response()->json([
                'success' => false,
                'message' => 'Pesanan harus dikemas dulu sebelum dikirim (status: ' . $order->status . ').',
            ], 422);
        }

        $order->status = 'dikirim';
        $order->save();

        // Notifikasi ke pembeli
        Notifikasi::create([
            'user_id'  => $order->user_id,
            'judul'    => 'Pesanan dalam perjalanan',
            'pesan'    => 'Pesanan "' . $order->nama_produk . '" sedang dalam perjalanan.',
            'order_id' => $order->id,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Pesanan ditandai dalam pengiriman.',
            'data'    => ['id' => $order->id, 'status' => $order->status],
        ]);
    }

    // Pembeli mengonfirmasi
    public function terima(Request $request, $id)
    {
        $order = Order::with('produk:id,user_id')->find($id);

        if (!$order) {
            return response()->json([
                'success' => false,
                'message' => 'Pesanan tidak ditemukan.',
            ], 404);
        }

        // Hanya pembeli pemilik pesanan yang boleh konfirmasi
        if ($order->user_id !== $request->user()->id) {
            return response()->json([
                'success' => false,
                'message' => 'Kamu hanya bisa mengonfirmasi pesananmu sendiri.',
            ], 403);
        }

        if ($order->status !== 'dikirim') {
            return response()->json([
                'success' => false,
                'message' => 'Pesanan belum dikirim, tidak bisa dikonfirmasi diterima.',
            ], 422);
        }

        $order->status = 'selesai';
        $order->save();

        // Notifikasi ke penjual
        $penjualId = $order->produk->user_id ?? null;
        if ($penjualId !== null) {
            Notifikasi::create([
                'user_id'  => $penjualId,
                'judul'    => 'Pesanan selesai',
                'pesan'    => 'Pesanan "' . $order->nama_produk . '" telah diterima pembeli.',
                'order_id' => $order->id,
            ]);
        }

        return response()->json([
            'success' => true,
            'message' => 'Pesanan selesai. Terima kasih!',
            'data'    => ['id' => $order->id, 'status' => $order->status],
        ]);
    }
}
