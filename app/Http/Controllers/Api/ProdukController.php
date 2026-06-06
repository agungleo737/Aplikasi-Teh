<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Produk;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator; 

class ProdukController extends Controller
{
    public function index()
    {
         $teh = Produk::all();

         return response()->json([
             'success' => true,
             'message' => 'Daftar Menu',
             'data'    => $teh
         ]);
    }

     public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'nama_teh'  => 'required|max:50',
            'kategori'  => 'required|max:30',      
            'harga'     => 'required|numeric|min:500',
            'deskripsi' => 'nullable', 
            'gambar'    => 'nullable',
            'stok'      => 'required|numeric' 
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Inputan Salah',
                'errors'  => $validator->errors()
            ], 422);
        }

        $produk = Produk::create([
            'nama_teh'  => $request->nama_teh,
            'kategori'  => $request->kategori,   
            'harga'     => $request->harga,
            'deskripsi' => $request->deskripsi,
            'gambar'    => $request->gambar,
            'stok'      => $request->stok, 
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Menu teh baru berhasil ditambahkan!',
            'data'    => $produk
        ], 201);
    }
}
