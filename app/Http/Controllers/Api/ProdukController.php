<?php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Produk;
use App\Models\Order;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class ProdukController extends Controller
{
    public function index()
    {
        $teh = Produk::orderBy('id', 'asc')->get();
        return response()->json([
            'success' => true,
            'message' => 'Daftar Menu',
            'data'    => $teh
        ]);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'nama_teh'    => 'required|max:50',
            'kategori'    => 'required|max:30',
            'harga'       => 'required|numeric|min:500',
            'deskripsi'   => 'nullable',
            'gambar'      => 'nullable|image|mimes:jpg,jpeg,png|max:2048',
            'gambar_full' => 'nullable|image|mimes:jpg,jpeg,png|max:2048',
            'stok'        => 'required|numeric'
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Inputan Salah',
                'errors'  => $validator->errors()
            ], 422);
        }

        $namaGambar = null;
        if ($request->hasFile('gambar')) {
            $file = $request->file('gambar');
            $namaGambar = time() . '_thumb_' . $file->getClientOriginalName();
            $file->move(public_path('gambar_teh'), $namaGambar);
        }

        $namaGambarFull = null;
        if ($request->hasFile('gambar_full')) {
            $file = $request->file('gambar_full');
            $namaGambarFull = time() . '_full_' . $file->getClientOriginalName();
            $file->move(public_path('gambar_teh'), $namaGambarFull);
        }

        $produk = Produk::create([
            'nama_teh'    => $request->nama_teh,
            'kategori'    => $request->kategori,
            'harga'       => $request->harga,
            'deskripsi'   => $request->deskripsi,
            'gambar'      => $namaGambar,
            'gambar_full' => $namaGambarFull,
            'stok'        => $request->stok,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Menu teh baru berhasil ditambahkan!',
            'data'    => $produk
        ], 201);
    }

    public function getBestSeller()
    {
        $bestSeller = Produk::withCount('orders')
            ->orderBy('orders_count', 'desc')
            ->take(5)
            ->get();

        return response()->json([
            'success' => true,
            'message' => 'Best Seller',
            'data'    => $bestSeller
        ]);
    }
}
