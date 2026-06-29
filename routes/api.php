<?php
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\ProdukController;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\ProfileController;
use App\Http\Controllers\Api\KategoriController;
use App\Http\Controllers\Api\LaporanController;
use App\Http\Controllers\Api\CheckoutController;
use App\Http\Controllers\Api\AdminController;
use App\Http\Controllers\Api\FavoritController;
use App\Http\Controllers\Api\OrderController;
use App\Http\Controllers\Api\UlasanController;
use App\Http\Controllers\Api\NotifikasiController;

// Auth Routes
Route::post('/register', [AuthController::class, 'register'])->middleware('throttle:10,1');
Route::post('/login',    [AuthController::class, 'login'])->middleware('throttle:10,1');

// Public Product Routes
Route::get('/produk',             [ProdukController::class, 'index']);
Route::get('/produk/best-seller', [ProdukController::class, 'getBestSeller']);
Route::get('/kategori',           [KategoriController::class, 'index']);
Route::get('/kategori/{id}',      [KategoriController::class, 'show']);
Route::get('/produk/{id}/ulasan', [UlasanController::class, 'index']);

// Authenticated Routes
Route::middleware('auth:sanctum')->group(function () {
    Route::post('/logout', [AuthController::class, 'logout']);

    // Produk & Checkout
    Route::post('/checkout',       [CheckoutController::class, 'store']);
    Route::post('/checkout/batch', [CheckoutController::class, 'storeBatch']);
    Route::post('/produk',         [ProdukController::class, 'store']);
    Route::post('/tambah-teh',     [ProdukController::class, 'store']);
    Route::post('/produk/{id}',    [ProdukController::class, 'update']);
    Route::delete('/produk/{id}',  [ProdukController::class, 'destroy']);

    // Order & Favorit
    Route::get('/orders',                   [OrderController::class, 'index']);
    Route::post('/orders/{id}/terima',      [OrderController::class, 'terima']);
    Route::get('/favorit',                  [FavoritController::class, 'index']);
    Route::post('/favorit',                 [FavoritController::class, 'store']);
    Route::delete('/favorit/{produkId}',    [FavoritController::class, 'destroy']);

    // Pesanan masuk
    Route::get('/penjualan',                [OrderController::class, 'penjualan']);
    Route::post('/penjualan/{id}/proses',   [OrderController::class, 'proses']);
    Route::post('/penjualan/{id}/kirim',    [OrderController::class, 'kirim']);

    // Notifikasi
    Route::get('/notifikasi',               [NotifikasiController::class, 'index']);
    Route::post('/notifikasi/baca-semua',   [NotifikasiController::class, 'bacaSemua']);

    // Rating
    Route::post('/produk/{id}/ulasan',      [UlasanController::class, 'store']);

    // User Profile
    Route::get('/user', function (Request $request) {
        return $request->user();
    });
    Route::get('/profile',          [ProfileController::class, 'show']);
    Route::put('/profile',          [ProfileController::class, 'update']);
    Route::post('/change-password', [ProfileController::class, 'changePassword']);
    
    // Admin Routes
    Route::middleware('admin')->group(function () {
        Route::post('/kategori',        [KategoriController::class, 'store']);
        Route::post('/kategori/{id}',   [KategoriController::class, 'update']);
        Route::delete('/kategori/{id}', [KategoriController::class, 'destroy']);

        Route::get('/laporan/penjualan',       [LaporanController::class, 'penjualan']);
        Route::get('/laporan/produk-terlaris', [LaporanController::class, 'produkTerlaris']);
        Route::get('/admin/users',             [AdminController::class, 'daftarUser']);
        Route::post('/admin/users',            [AdminController::class, 'tambahAdmin']);
        Route::get('/admin/produk',            [AdminController::class, 'daftarProduk']);
        Route::get('/admin/transaksi',         [AdminController::class, 'daftarTransaksi']);
    });
});
