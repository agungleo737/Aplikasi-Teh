<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\Produk;
use App\Models\Ulasan;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Validator;

class UlasanController extends Controller
{
    // daftar ulasan produk
    public function index($id)
    {
        $produk = Produk::find($id);

        if (!$produk) {
            return response()->json([
                'success' => false,
                'message' => 'Produk tidak ditemukan.',
            ], 404);
        }

        $ulasan = Ulasan::with('user:id,name')
            ->where('produk_id', $id)
            ->orderByDesc('created_at')
            ->get()
            ->map(function ($u) {
                return [
                    'id'        => $u->id,
                    'nama_user' => $u->user->name ?? '-',
                    'rating'    => $u->rating,
                    'komentar'  => $u->komentar,
                    'tanggal'   => $u->created_at->format('Y-m-d H:i:s'),
                ];
            });

        return response()->json([
            'success' => true,
            'message' => 'Daftar Ulasan',
            'data'    => [
                'rata_rata' => round((float) $ulasan->avg('rating'), 1),
                'jumlah'    => $ulasan->count(),
                'ulasan'    => $ulasan,
            ],
        ]);
    }

    // kirim ulasan
    public function store(Request $request, $id)
    {
        $produk = Produk::find($id);

        if (!$produk) {
            return response()->json([
                'success' => false,
                'message' => 'Produk tidak ditemukan.',
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'rating'   => 'required|integer|min:1|max:5',
            'komentar' => 'nullable|string|max:500',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Inputan Salah',
                'errors'  => $validator->errors(),
            ], 422);
        }

        $userId = $request->user()->id;

        // Hak ulasan
        $bolehUlas = Order::where('user_id', $userId)
            ->where('produk_id', $id)
            ->where('status', 'selesai')
            ->exists();

        if (!$bolehUlas) {
            return response()->json([
                'success' => false,
                'message' => 'Kamu hanya bisa menilai produk yang sudah selesai kamu beli.',
            ], 403);
        }

        $ulasan = Ulasan::updateOrCreate(
            ['produk_id' => $id, 'user_id' => $userId],
            ['rating' => $request->rating, 'komentar' => $request->komentar]
        );

        // Rata-rata rating
        Cache::forget('produk_list');
        Cache::forget('produk_best_seller');

        return response()->json([
            'success' => true,
            'message' => 'Ulasan berhasil disimpan!',
            'data'    => $ulasan,
        ], 201);
    }
}
