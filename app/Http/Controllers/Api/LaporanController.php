<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\Produk;
use Illuminate\Http\Request;

class LaporanController extends Controller
{
    public function penjualan(Request $request)
    {
        $request->validate([
            'tanggal_mulai' => 'nullable|date',
            'tanggal_akhir' => 'nullable|date|after_or_equal:tanggal_mulai',
        ]);

        $query = Order::with('user:id,name,email');

        if ($request->filled('tanggal_mulai')) {
            $query->whereDate('created_at', '>=', $request->tanggal_mulai);
        }
        if ($request->filled('tanggal_akhir')) {
            $query->whereDate('created_at', '<=', $request->tanggal_akhir);
        }

        $orders = $query->orderBy('created_at', 'desc')->get();

        $transaksi = $orders->map(function ($o) {
            return [
                'id_belanja'   => $o->id,               
                'tanggal'      => $o->created_at->format('Y-m-d H:i:s'), 
                'nama_produk'  => $o->nama_produk,                      
                'jumlah_beli'  => $o->jumlah_beli,
                'harga_satuan' => $o->harga_satuan,
                'total_harga'  => $o->total_harga,
                'pembeli_nama' => $o->user->name ?? '(tidak diketahui)',
            ];
        });

        $ringkasan = [
            'total_transaksi'    => $orders->count(),
            'total_item_terjual' => (int) $orders->sum('jumlah_beli'),
            'total_pendapatan'   => (int) $orders->sum('total_harga'),
        ];

        return response()->json([
            'success' => true,
            'message' => 'Laporan Penjualan',
            'data'    => [
                'periode' => [
                    'tanggal_mulai' => $request->tanggal_mulai,
                    'tanggal_akhir' => $request->tanggal_akhir,
                ],
                'ringkasan' => $ringkasan,
                'transaksi' => $transaksi,
            ],
        ]);
    }

    public function produkTerlaris()
    {
        $produk = Produk::with('kategoriRelasi:id,nama_kategori')
            ->withCount('orders')
            ->withSum('orders', 'jumlah_beli')
            ->orderByDesc('orders_sum_jumlah_beli')
            ->get()
            ->map(function ($p) {
                return [
                    'id'            => $p->id,
                    'nama_teh'      => $p->nama_teh,
                    'kategori'      => $p->kategori,
                    'harga'         => $p->harga,
                    'stok'          => $p->stok,
                    'jumlah_order'  => $p->orders_count,
                    'total_terjual' => (int) ($p->orders_sum_jumlah_beli ?? 0),
                ];
            });

        return response()->json([
            'success' => true,
            'message' => 'Laporan Produk Terlaris',
            'data'    => $produk,
        ]);
    }
}
