package com.example.aplikasiseduhteh;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.POST;

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
    Call<TehResponse> getAllProduk();

    @GET("api/produk/best-seller")
    Call<TehResponse> getbestseller();

    @Multipart
    @POST("api/produk")
    Call<Void> tambahProduk(
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
            @Part("nama_teh") RequestBody namaTeh,
            @Part("kategori") RequestBody kategori,
            @Part("harga") RequestBody harga,
            @Part("stok") RequestBody stok,
            @Part("deskripsi") RequestBody deskripsi
    );

    @FormUrlEncoded
    @POST("api/checkout")
    Call<ResponseBody> tambahorder(
            @Field("produk_id") int produkid,
            @Field("jumlah_beli") int jumlahbeli
    );
}