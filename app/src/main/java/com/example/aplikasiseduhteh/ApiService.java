package com.example.aplikasiseduhteh;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("api/produk")
    Call<TehResponse> getAllProduk();
    @FormUrlEncoded
    @POST("api/produk")
    Call<Void> tambahProduk(
            @Field("gambar") String gambar,
            @Field("nama_teh") String namaTeh,
            @Field("kategori") String kategori,
            @Field("harga") int harga,
            @Field("stok") int stok,
            @Field("deskripsi") String deskripsi
    );
}