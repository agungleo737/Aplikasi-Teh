package com.example.aplikasiseduhteh;

import com.google.gson.annotations.SerializedName;

public class Teh {
    @SerializedName("id")
    private int id;
    @SerializedName("nama_teh")
    private String nama;
    @SerializedName("harga")
    private String harga;
    @SerializedName("deskripsi")
    private String deskripsi;
    @SerializedName("kategori")
    private String kategori;
    @SerializedName("stok")
    private int stok;

    private String gambarnama;
    private String gambarfullnama;
    private int qtyDibeli = 1;
    private int sellerId = 0;

    public Teh(int id, String nama, String harga, String deskripsi,
               String gambarNama, String gambarFullNama, int stok, String kategori) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.gambarnama = gambarNama;
        this.gambarfullnama = gambarFullNama;
        this.stok = stok;
        this.kategori = kategori;
        this.qtyDibeli = 1;
    }

    public int getid() { return id; }
    public String getNama() { return nama; }
    public String getHarga() { return harga; }
    public String getDeskripsi() { return deskripsi; }
    public String getGambarNama() { return gambarnama != null ? gambarnama : ""; }
    public String getGambarFullNama() {
        return (gambarfullnama != null && !gambarfullnama.isEmpty()) ? gambarfullnama : getGambarNama();
    }
    public int getStok() { return stok; }
    public String getKategori() { return kategori; }
    public int getQtyDibeli() { return qtyDibeli; }
    public void setQtyDibeli(int qty) { this.qtyDibeli = qty; }
    public void setStok(int stok) { this.stok = stok; }
    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
}