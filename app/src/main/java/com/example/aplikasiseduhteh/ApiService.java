package com.example.aplikasiseduhteh;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface ApiService {


    @FormUrlEncoded
    @POST("api/register")
    Call<ResponseBody> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/login")
    Call<ResponseBody> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @GET("api/produk")
    Call<TehResponse> getAllProduk(@Header("Authorization") String token);

    @GET("api/produk/best-seller")
    Call<TehResponse> getbestseller(@Header("Authorization") String token);

    @Multipart
    @POST("api/produk")
    Call<Void> tambahProduk(
            @Header("Authorization") String token,
            @Part MultipartBody.Part gambar,
            @Part MultipartBody.Part gambarFull,
            @Part("nama_teh") RequestBody namaTeh,
            @Part("kategori") RequestBody kategori,
            @Part("harga") RequestBody harga,
            @Part("stok") RequestBody stok,
            @Part("deskripsi") RequestBody deskripsi
    );

    @Multipart
    @POST("api/produk")
    Call<Void> tanpagambar(
            @Header("Authorization") String token,
            @Part("nama_teh") RequestBody namaTeh,
            @Part("kategori") RequestBody kategori,
            @Part("harga") RequestBody harga,
            @Part("stok") RequestBody stok,
            @Part("deskripsi") RequestBody deskripsi
    );

    @FormUrlEncoded
    @POST("api/checkout")
    Call<ResponseBody> tambahorder(
            @Header("Authorization") String token,
            @Field("produk_id") int produkid,
            @Field("jumlah_beli") int jumlahbeli
    );

    // Checkout item
    @POST("api/checkout/batch")
    Call<ResponseBody> checkoutBatch(
            @Header("Authorization") String token,
            @Body CheckoutRequest body
    );

    // Riwayat pesanan
    @GET("api/orders")
    Call<ResponseBody> getRiwayat(@Header("Authorization") String token);

    // Pembeli konfirmasi
    @POST("api/orders/{id}/terima")
    Call<ResponseBody> terimaPesanan(
            @Path("id") int id,
            @Header("Authorization") String token
    );

    // Pesanan masuk
    @GET("api/penjualan")
    Call<ResponseBody> getPenjualan(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/penjualan/{id}/proses")
    Call<ResponseBody> prosesPesanan(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Field("estimasi_siap") String estimasiSiap,
            @Field("estimasi_kirim") String estimasiKirim
    );

    @POST("api/penjualan/{id}/kirim")
    Call<ResponseBody> kirimPesanan(
            @Path("id") int id,
            @Header("Authorization") String token
    );

    // Rating
    @GET("api/produk/{id}/ulasan")
    Call<ResponseBody> getUlasan(@Path("id") int id);

    @FormUrlEncoded
    @POST("api/produk/{id}/ulasan")
    Call<ResponseBody> kirimUlasan(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Field("rating") int rating,
            @Field("komentar") String komentar
    );

    // Notifikasi server
    @GET("api/notifikasi")
    Call<ResponseBody> getNotifikasiServer(@Header("Authorization") String token);

    @POST("api/notifikasi/baca-semua")
    Call<ResponseBody> bacaSemuaNotif(@Header("Authorization") String token);

    @Multipart
    @POST("api/produk/{id}")
    Call<ResponseBody> updateProduk(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Part MultipartBody.Part gambar,
            @Part MultipartBody.Part gambarFull,
            @Part("nama_teh") RequestBody namaTeh,
            @Part("kategori") RequestBody kategori,
            @Part("harga") RequestBody harga,
            @Part("stok") RequestBody stok,
            @Part("deskripsi") RequestBody deskripsi
    );
    @Multipart
    @POST("api/produk/{id}")
    Call<ResponseBody> updateProdukTanpaGambar(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Part("nama_teh") RequestBody namaTeh,
            @Part("kategori") RequestBody kategori,
            @Part("harga") RequestBody harga,
            @Part("stok") RequestBody stok,
            @Part("deskripsi") RequestBody deskripsi
    );
    @DELETE("api/produk/{id}")
    Call<ResponseBody> hapusProduk(
            @Path("id") int id,
            @Header("Authorization") String token
    );

    @GET("api/profile")
    Call<ResponseBody> getProfile(@Header("Authorization") String token);
    @FormUrlEncoded
    @PUT("api/profile")
    Call<ResponseBody> updateProfile(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("panggilan") String panggilan,
            @Field("tanggal_lahir") String tanggalLahir,
            @Field("alamat") String alamat
    );

    @FormUrlEncoded
    @POST("api/change-password")
    Call<ResponseBody> changePassword(
            @Header("Authorization") String token,
            @Field("current_password") String currentPassword,
            @Field("new_password") String newPassword,
            @Field("new_password_confirmation") String newPasswordConfirmation
    );

    // kategori
    @GET("api/kategori")
    Call<ResponseBody> getKategori(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/kategori")
    Call<ResponseBody> tambahKategori(
            @Header("Authorization") String token,
            @Field("nama_kategori") String namaKategori,
            @Field("deskripsi") String deskripsi
    );

    @FormUrlEncoded
    @POST("api/kategori/{id}")
    Call<ResponseBody> updateKategori(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Field("nama_kategori") String namaKategori,
            @Field("deskripsi") String deskripsi
    );

    @DELETE("api/kategori/{id}")
    Call<ResponseBody> hapusKategori(
            @Path("id") int id,
            @Header("Authorization") String token
    );

    // laporan admin
    @GET("api/laporan/penjualan")
    Call<ResponseBody> laporanPenjualan(
            @Header("Authorization") String token,
            @Query("tanggal_mulai") String tanggalMulai,
            @Query("tanggal_akhir") String tanggalAkhir
    );

    @GET("api/laporan/produk-terlaris")
    Call<ResponseBody> laporanProdukTerlaris(@Header("Authorization") String token);

    // pantauan admin
    @GET("api/admin/users")
    Call<ResponseBody> adminDaftarUser(@Header("Authorization") String token);

    // tambah admin baru
    @FormUrlEncoded
    @POST("api/admin/users")
    Call<ResponseBody> tambahAdmin(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("api/admin/produk")
    Call<ResponseBody> adminDaftarProduk(@Header("Authorization") String token);

    @GET("api/admin/transaksi")
    Call<ResponseBody> adminDaftarTransaksi(@Header("Authorization") String token);

    //favorite
    @GET("api/favorit")
    Call<TehResponse> getFavorit(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/favorit")
    Call<ResponseBody> tambahFavorit(
            @Header("Authorization") String token,
            @Field("produk_id") int produkId
    );

    @DELETE("api/favorit/{produkId}")
    Call<ResponseBody> hapusFavorit(
            @Path("produkId") int produkId,
            @Header("Authorization") String token
    );
}
