package com.example.aplikasiseduhteh;

public class UlasanModel {
    private final String namaUser;
    private final float rating;
    private final String komentar;
    private final String tanggal;

    public UlasanModel(String namaUser, float rating, String komentar, String tanggal) {
        this.namaUser = namaUser;
        this.rating = rating;
        this.komentar = komentar;
        this.tanggal = tanggal;
    }

    public String getNamaUser() { return namaUser; }
    public float getRating() { return rating; }
    public String getKomentar() { return komentar; }
    public String getTanggal() { return tanggal; }
}
