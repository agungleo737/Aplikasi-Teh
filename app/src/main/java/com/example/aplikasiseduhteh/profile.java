package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        View btnTambahProduk = findViewById(R.id.btn_tambah_produk);
        if (btnTambahProduk != null) {
            btnTambahProduk.setOnClickListener(v -> {
                Intent intent = new Intent(profile.this, InputProdukActivity.class);
                startActivity(intent);
            });
        }

        // 1. PINDAH KE MANAGE ACCOUNT
        View btnManageAccount = findViewById(R.id.btn_manage_account);
        if (btnManageAccount != null) {
            btnManageAccount.setOnClickListener(v -> {
                Intent intent = new Intent(profile.this, manage_profile.class);
                startActivity(intent);
            });
        }

        // 2. PINDAH KE NOTIFIKASI
        View btnNotifikasi = findViewById(R.id.btn_notifications);
        if (btnNotifikasi != null) {
            btnNotifikasi.setOnClickListener(v -> {
                Intent intent = new Intent(profile.this, notifuy.class);
                startActivity(intent);
            });
        }

        // 3. PINDAH KE PASSWORD & SECURITY
        View btnPasswordSecurity = findViewById(R.id.btn_password_security);
        if (btnPasswordSecurity != null) {
            btnPasswordSecurity.setOnClickListener(v -> {
                // Nama class harus sesuai dengan file password_secur.java lo
                Intent intent = new Intent(profile.this, password_secur.class);
                startActivity(intent);
            });
        }

        // 4. LOGOUT
        View btnLogout = findViewById(R.id.menu_logout_manual);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(profile.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        setupBottomNav();
    }

    private void setupBottomNav() {
        View nav = findViewById(R.id.bottom_navigation);
        if (nav != null) {
            nav.findViewById(R.id.logohome).setOnClickListener(v ->
                    startActivity(new Intent(profile.this, MainActivity.class)));

            nav.findViewById(R.id.logoesplor).setOnClickListener(v ->
                    startActivity(new Intent(profile.this, eksplorasi.class)));

            nav.findViewById(R.id.logopaporit).setOnClickListener(v ->
                    startActivity(new Intent(profile.this, paporitactivity.class)));

            nav.findViewById(R.id.belanjaya).setOnClickListener(v ->
                    startActivity(new Intent(profile.this, keranjang.class)));
        }
    }
}