package com.example.aplikasiseduhteh;

public class User {
    private final String nama;
    private final String email;
    private final String alamat;
    private final String role;

    public User(String nama, String email, String alamat, String role) {
        this.nama   = nama;
        this.email  = email;
        this.alamat = alamat;
        this.role   = role;
    }

    public String getNama()   { return nama; }
    public String getEmail()  { return email; }
    public String getAlamat() { return alamat; }
    public String getRole()   { return role; }
}
