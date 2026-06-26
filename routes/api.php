<?php
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\ProdukController;
use App\Http\Controllers\Api\AuthController;
<<<<<<< HEAD
use App\Http\Controllers\Api\ProfileController;
use App\Http\Controllers\Api\KategoriController;
use App\Http\Controllers\Api\LaporanController;
use App\Http\Controllers\Api\CheckoutController;
use App\Http\Controllers\Api\AdminController;
use App\Http\Controllers\Api\FavoritController;
use App\Http\Controllers\Api\OrderController;

// Batasi percobaan
Route::post('/register', [AuthController::class, 'register'])->middleware('throttle:10,1');
Route::post('/login',    [AuthController::class, 'login'])->middleware('throttle:10,1');
Route::middleware('auth:sanctum')->group(function () {
    Route::post('/logout', [AuthController::class, 'logout']);

    // Lihat menu & kategori
    Route::get('/produk',             [ProdukController::class, 'index']);
    Route::get('/produk/best-seller', [ProdukController::class, 'getBestSeller']);
    Route::get('/kategori',           [KategoriController::class, 'index']);
    Route::get('/kategori/{id}',      [KategoriController::class, 'show']);
    Route::post('/checkout',       [CheckoutController::class, 'store']);
    Route::post('/checkout/batch', [CheckoutController::class, 'storeBatch']);

    // Riwayat pesanan milik sendiri
    Route::get('/orders', [OrderController::class, 'index']);

    // Favorit
    Route::get('/favorit',                [FavoritController::class, 'index']);
    Route::post('/favorit',               [FavoritController::class, 'store']);
    Route::delete('/favorit/{produkId}',  [FavoritController::class, 'destroy']);

    Route::post('/produk',        [ProdukController::class, 'store']);
    Route::post('/tambah-teh',    [ProdukController::class, 'store']);
    Route::post('/produk/{id}',   [ProdukController::class, 'update']);
    Route::delete('/produk/{id}', [ProdukController::class, 'destroy']);

    // Akun sendiri
    Route::get('/user', function (Request $request) {
        return $request->user();
    });
    Route::get('/profile',          [ProfileController::class, 'show']);
    Route::put('/profile',          [ProfileController::class, 'update']);
    Route::post('/change-password', [ProfileController::class, 'changePassword']);
    
    //admin
    Route::middleware('admin')->group(function () {
        // Master kategori
        Route::post('/kategori',        [KategoriController::class, 'store']);
        Route::post('/kategori/{id}',   [KategoriController::class, 'update']);
        Route::delete('/kategori/{id}', [KategoriController::class, 'destroy']);

        // Laporan
        Route::get('/laporan/penjualan',       [LaporanController::class, 'penjualan']);
        Route::get('/laporan/produk-terlaris', [LaporanController::class, 'produkTerlaris']);
        Route::get('/admin/users',     [AdminController::class, 'daftarUser']);
        Route::post('/admin/users',    [AdminController::class, 'tambahAdmin']);
        Route::get('/admin/produk',    [AdminController::class, 'daftarProduk']);
        Route::get('/admin/transaksi', [AdminController::class, 'daftarTransaksi']);
    });
=======

Route::post('/register', [AuthController::class, 'register']);
Route::post('/login',    [AuthController::class, 'login']);

Route::get('/produk', [ProdukController::class, 'index']);
Route::get('/produk/best-seller', [ProdukController::class, 'getBestSeller']);

Route::middleware('auth:sanctum')->group(function () {
    Route::post('/logout',   [AuthController::class, 'logout']);
    Route::post('/produk',   [ProdukController::class, 'store']);
    Route::post('/tambah-teh', [ProdukController::class, 'store']);
    Route::post('/checkout', [App\Http\Controllers\Api\CheckoutController::class, 'store']);
    Route::get('/user', function (Request $request) {
        return $request->user();
    });
>>>>>>> 54adf99378b1f88c47561a8e1ebee2f44065be40
});
