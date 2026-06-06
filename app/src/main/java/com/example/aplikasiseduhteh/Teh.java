package com.example.aplikasiseduhteh;

public class Teh {
    private String nama, harga, deskripsi, kategori;
    private int gambarKecil, gambarFull, stok;

    public Teh(String nama, String harga, String deskripsi, int gambarKecil, int gambarFull, int stok, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.gambarKecil = gambarKecil;
        this.gambarFull = gambarFull;
        this.stok = stok;
        this.kategori = kategori;
    }

    public String getNama() { return nama; }
    public String getHarga() { return harga; }
    public String getDeskripsi() { return deskripsi; }
    public int getGambarKecil() { return gambarKecil; }
    public int getGambarFull() { return gambarFull; }
    public int getStok() { return stok; }
    public String getKategori() { return kategori; }
}