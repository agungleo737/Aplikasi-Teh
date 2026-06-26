package com.example.aplikasiseduhteh;

public class RiwayatModel {
    private final int id;
    private final String tanggal;
    private final String namaProduk;
    private final int jumlahBeli;
    private final long hargaSatuan;
    private final long totalHarga;
    private final String gambar;

    public RiwayatModel(int id, String tanggal, String namaProduk,
                        int jumlahBeli, long hargaSatuan, long totalHarga, String gambar) {
        this.id          = id;
        this.tanggal     = tanggal;
        this.namaProduk  = namaProduk;
        this.jumlahBeli  = jumlahBeli;
        this.hargaSatuan = hargaSatuan;
        this.totalHarga  = totalHarga;
        this.gambar      = gambar;
    }

    public int getId()           { return id; }
    public String getTanggal()   { return tanggal; }
    public String getNamaProduk(){ return namaProduk; }
    public int getJumlahBeli()   { return jumlahBeli; }
    public long getHargaSatuan() { return hargaSatuan; }
    public long getTotalHarga()  { return totalHarga; }
    public String getGambar()    { return gambar; }
}
