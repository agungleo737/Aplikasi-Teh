package com.example.aplikasiseduhteh;

public class TransaksiModel {
    private final int id;
    private final String invoice;
    private final String produk;
    private final String namaPembeli;
    private final String harga;
    private final String status;
    private final String tanggal;
    private final String estimasiSiap;
    private final String estimasiKirim;

    public TransaksiModel(int id, String invoice, String produk, String namaPembeli,
                          String harga, String status, String tanggal,
                          String estimasiSiap, String estimasiKirim) {
        this.id = id;
        this.invoice = invoice;
        this.produk  = produk;
        this.namaPembeli = namaPembeli;
        this.harga   = harga;
        this.status  = status;
        this.tanggal = tanggal;
        this.estimasiSiap = estimasiSiap;
        this.estimasiKirim = estimasiKirim;
    }

    public int getId() { return id; }
    public String getInvoice() { return invoice; }
    public String getProduk()  { return produk; }
    public String getNamaPembeli() { return namaPembeli; }
    public String getHarga()   { return harga; }
    public String getStatus()  { return status; }
    public String getTanggal() { return tanggal; }
    public String getEstimasiSiap() { return estimasiSiap; }
    public String getEstimasiKirim() { return estimasiKirim; }
}
