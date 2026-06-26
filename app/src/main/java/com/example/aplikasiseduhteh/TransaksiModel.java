package com.example.aplikasiseduhteh;

public class TransaksiModel {
    private final String invoice;
    private final String produk;
    private final String harga;
    private final String status;
    private final String tanggal;

    public TransaksiModel(String invoice, String produk, String harga, String status, String tanggal) {
        this.invoice = invoice;
        this.produk  = produk;
        this.harga   = harga;
        this.status  = status;
        this.tanggal = tanggal;
    }

    public String getInvoice() { return invoice; }
    public String getProduk()  { return produk; }
    public String getHarga()   { return harga; }
    public String getStatus()  { return status; }
    public String getTanggal() { return tanggal; }
}
