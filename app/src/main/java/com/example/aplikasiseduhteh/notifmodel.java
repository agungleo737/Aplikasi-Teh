package com.example.aplikasiseduhteh;

public class notifmodel {
    private String judul;
    private String pesan;
    public notifmodel(String judul, String pesan) {
        this.judul = judul;
        this.pesan = pesan;
    }

    public String getJudul() { return judul; }
    public String getPesan() { return pesan; }
}