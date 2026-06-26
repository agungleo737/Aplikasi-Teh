<?php
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\ProdukController;
use App\Http\Controllers\Api\AuthController;

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
});
