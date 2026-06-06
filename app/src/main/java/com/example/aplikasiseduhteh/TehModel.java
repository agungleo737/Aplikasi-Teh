package com.example.aplikasiseduhteh;

import com.google.gson.annotations.SerializedName;

public class TehModel {
    @SerializedName("id")
    private int id;

    @SerializedName("nama_teh")
    private String namaTeh;

    @SerializedName("kategori")
    private String kategori;

    @SerializedName("harga")
    private double harga;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("gambar")
    private String gambar;

    // Getter
    public int getId() { return id; }
    public String getNamaTeh() { return namaTeh; }
    public String getKategori() { return kategori; }
    public double getHarga() { return harga; }
    public String getDeskripsi() { return deskripsi; }
    public String getGambar() { return gambar; }
}