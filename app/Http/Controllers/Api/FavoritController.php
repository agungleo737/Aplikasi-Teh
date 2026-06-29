<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Favorit;
use App\Models\Produk;
use Illuminate\Http\Request;

class FavoritController extends Controller
{
    public function index(Request $request)
    {
        $userId = $request->user()->id;

        $produkIds = Favorit::where('user_id', $userId)->pluck('produk_id');
        $produk    = Produk::whereIn('id', $produkIds)->get();

        return response()->json([
            'success' => true,
            'message' => 'Daftar produk favorit',
            'data'    => $produk,
        ]);
    }

    public function store(Request $request)
    {
        $request->validate([
            'produk_id' => 'required|integer|exists:produks,id',
        ]);

        $favorit = Favorit::firstOrCreate([
            'user_id'   => $request->user()->id,
            'produk_id' => $request->produk_id,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Ditambahkan ke favorit',
            'data'    => $favorit,
        ], 201);
    }
    public function destroy(Request $request, $produkId)
    {
        Favorit::where('user_id', $request->user()->id)
            ->where('produk_id', $produkId)
            ->delete();

        return response()->json([
            'success' => true,
            'message' => 'Dihapus dari favorit',
        ]);
    }
}
