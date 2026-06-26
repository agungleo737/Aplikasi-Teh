<?php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Produk;
use App\Models\Order;
use App\Models\Kategori;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\File;
use Illuminate\Support\Facades\Validator;

class ProdukController extends Controller
{
    public function index()
    {
        $teh = Produk::with('kategoriRelasi:id,nama_kategori')->orderBy('id', 'asc')->get();
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
            'harga'       => 'required|integer|min:500',
            'deskripsi'   => 'nullable',
            'gambar'      => 'nullable|image|mimes:jpg,jpeg,png|max:2048',
            'gambar_full' => 'nullable|image|mimes:jpg,jpeg,png|max:2048',
            'stok'        => 'required|integer|min:0'
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

        // Cari kategori dari nama yang dibuat
        $kategori = Kategori::firstOrCreate(['nama_kategori' => $request->kategori]);

        $produk = Produk::create([
            'user_id'     => $request->user()->id,
            'nama_teh'    => $request->nama_teh,
            'kategori_id' => $kategori->id,
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

    public function update(Request $request, $id)
    {
        $produk = Produk::find($id);

        if (!$produk) {
            return response()->json([
                'success' => false,
                'message' => 'Produk tidak ditemukan.',
            ], 404);
        }

        $user = $request->user();
        if ($produk->user_id !== null && $produk->user_id !== $user->id && $user->role !== 'admin') {
            return response()->json([
                'success' => false,
                'message' => 'Kamu hanya bisa mengubah produk milikmu sendiri.',
            ], 403);
        }

        $validator = Validator::make($request->all(), [
            'nama_teh'    => 'sometimes|required|max:50',
            'kategori'    => 'sometimes|required|max:30',
            'harga'       => 'sometimes|required|integer|min:500',
            'deskripsi'   => 'nullable',
            'gambar'      => 'nullable|image|mimes:jpg,jpeg,png|max:2048',
            'gambar_full' => 'nullable|image|mimes:jpg,jpeg,png|max:2048',
            'stok'        => 'sometimes|required|integer|min:0',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Inputan Salah',
                'errors'  => $validator->errors()
            ], 422);
        }

        $produk->fill($request->only(['nama_teh', 'harga', 'deskripsi', 'stok']));

        // Update kategori
        if ($request->filled('kategori')) {
            $kategori = Kategori::firstOrCreate(['nama_kategori' => $request->kategori]);
            $produk->kategori_id = $kategori->id;
        }

        if ($request->hasFile('gambar')) {
            $this->hapusFileGambar($produk->gambar);
            $file = $request->file('gambar');
            $namaGambar = time() . '_thumb_' . $file->getClientOriginalName();
            $file->move(public_path('gambar_teh'), $namaGambar);
            $produk->gambar = $namaGambar;
        }

        if ($request->hasFile('gambar_full')) {
            $this->hapusFileGambar($produk->gambar_full);
            $file = $request->file('gambar_full');
            $namaGambarFull = time() . '_full_' . $file->getClientOriginalName();
            $file->move(public_path('gambar_teh'), $namaGambarFull);
            $produk->gambar_full = $namaGambarFull;
        }

        $produk->save();

        return response()->json([
            'success' => true,
            'message' => 'Produk berhasil diperbarui!',
            'data'    => $produk
        ]);
    }

    public function destroy(Request $request, $id)
    {
        $produk = Produk::find($id);

        if (!$produk) {
            return response()->json([
                'success' => false,
                'message' => 'Produk tidak ditemukan.',
            ], 404);
        }

        $user = $request->user();
        if ($produk->user_id !== null && $produk->user_id !== $user->id && $user->role !== 'admin') {
            return response()->json([
                'success' => false,
                'message' => 'Kamu hanya bisa menghapus produk milikmu sendiri.',
            ], 403);
        }

        $this->hapusFileGambar($produk->gambar);
        $this->hapusFileGambar($produk->gambar_full);

        $produk->delete();

        return response()->json([
            'success' => true,
            'message' => 'Produk berhasil dihapus!',
        ]);
    }

    private function hapusFileGambar($namaFile)
    {
        if (!$namaFile) {
            return;
        }
        $path = public_path('gambar_teh/' . $namaFile);
        if (File::exists($path)) {
            File::delete($path);
        }
    }

    public function getBestSeller()
    {
        $bestSeller = Produk::with('kategoriRelasi:id,nama_kategori')
            ->withCount('orders')
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
