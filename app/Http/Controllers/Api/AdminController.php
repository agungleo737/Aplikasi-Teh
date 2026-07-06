<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\Produk;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class AdminController extends Controller
{
    // Tambah akun admin baru
    public function tambahAdmin(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'name'     => 'required|string|max:255',
            'email'    => 'required|email|unique:users,email',
            'password' => 'required|string|min:6',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Inputan salah',
                'errors'  => $validator->errors(),
            ], 422);
        }

        $user = User::create([
            'name'     => $request->name,
            'email'    => $request->email,
            'password' => Hash::make($request->password),
        ]);
        $user->forceFill(['role' => 'admin'])->save();

        return response()->json([
            'success' => true,
            'message' => 'Admin baru berhasil ditambahkan!',
        ], 201);
    }

    public function daftarUser()
    {
        $users = User::orderBy('id', 'asc')
            ->get(['id', 'name', 'email', 'role', 'panggilan', 'alamat', 'created_at']);

        return response()->json([
            'success' => true,
            'message' => 'Daftar User',
            'data'    => $users,
        ]);
    }

    public function daftarProduk()
    {
        $produk = Produk::with(['user:id,name,email', 'kategoriRelasi:id,nama_kategori'])
            ->orderBy('id', 'asc')
            ->get()
            ->map(function ($p) {
                return [
                    'id'            => $p->id,
                    'nama_teh'      => $p->nama_teh,
                    'kategori'      => $p->kategori,
                    'harga'         => $p->harga,
                    'stok'          => $p->stok,
                    'penjual_id'    => $p->user_id,
                    'penjual_nama'  => $p->user->name ?? '(tidak diketahui)',
                    'penjual_email' => $p->user->email ?? null,
                ];
            });

        return response()->json([
            'success' => true,
            'message' => 'Daftar Semua Produk + Penjual',
            'data'    => $produk,
        ]);
    }

    public function daftarTransaksi()
    {
        $orders = Order::with('user:id,name,email')
            ->orderByDesc('created_at')
            ->get()
            ->map(function ($o) {
                return [
                    'id_belanja'    => $o->id,
                    'tanggal'       => $o->created_at->format('Y-m-d H:i:s'),
                    'nama_produk'   => $o->nama_produk,
                    'jumlah_beli'   => $o->jumlah_beli,
                    'harga_satuan'  => $o->harga_satuan,
                    'total_harga'   => $o->total_harga,
                    'pembeli_id'    => $o->user_id,
                    'pembeli_nama'  => $o->user->name ?? '(tidak diketahui)',
                    'pembeli_email' => $o->user->email ?? null,
                ];
            });

        return response()->json([
            'success' => true,
            'message' => 'Daftar Semua Transaksi + Pembeli',
            'data'    => $orders,
        ]);
    }
}
