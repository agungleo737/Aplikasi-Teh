<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Teh;
use Illuminate\Http\Request;

class TehController extends Controller
{
    public function store(Request $request)
    {
        $request->validate([
            'gambar' => 'required',
            'nama_teh' => 'required',
            'kategori' => 'required',
            'harga' => 'required|numeric',
            'stok' => 'required|numeric',
            'deskripsi' => 'required',
        ]);

        $teh = Teh::create($request->all());

        return response()->json([
            'success' => true,
            'message' => 'Produk Teh Berhasil Ditambahkan!',
            'data' => $teh
        ], 201);
    }
}
