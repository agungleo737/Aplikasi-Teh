package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        View btnTambahProduk = findViewById(R.id.btn_tambah_produk);
        if (btnTambahProduk != null) {
            btnTambahProduk.setOnClickListener(v ->
                    startActivity(new Intent(profile.this, KelolaProdukActivity.class)));
        }

        View btnPesananMasuk = findViewById(R.id.btn_pesanan_masuk);
        if (btnPesananMasuk != null) {
            btnPesananMasuk.setOnClickListener(v ->
                    startActivity(new Intent(profile.this, TransaksiActivity.class)));
        }

        View btnManageAccount = findViewById(R.id.btn_manage_account);
        if (btnManageAccount != null) {
            btnManageAccount.setOnClickListener(v ->
                    startActivity(new Intent(profile.this, manage_profile.class)));
        }

        View btnNotifikasi = findViewById(R.id.btn_notifications);
        if (btnNotifikasi != null) {
            btnNotifikasi.setOnClickListener(v ->
                    startActivity(new Intent(profile.this, notifuy.class)));
        }

        View btnPasswordSecurity = findViewById(R.id.btn_password_security);
        if (btnPasswordSecurity != null) {
            btnPasswordSecurity.setOnClickListener(v ->
                    startActivity(new Intent(profile.this, password_secur.class)));
        }

        View btnRiwayat = findViewById(R.id.btn_riwayat);
        if (btnRiwayat != null) {
            btnRiwayat.setOnClickListener(v ->
                    startActivity(new Intent(profile.this, RiwayatActivity.class)));
        }

        View btnAboutUs = findViewById(R.id.btn_about_us);
        if (btnAboutUs != null) {
            btnAboutUs.setOnClickListener(v ->
                    startActivity(new Intent(profile.this, TentangKamiActivity.class)));
        }

        View btnLogout = findViewById(R.id.menu_logout_manual);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> doLogout(v));
        }

        setupBottomNav();
    }

    private void doLogout(View btnLogout) {
        btnLogout.setEnabled(false);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.logout(sessionManager.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                selesaiLogout();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                selesaiLogout();
            }
        });
    }

    private void selesaiLogout() {
        FavoritManager.clear();
        sessionManager.clearsession();
        Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(profile.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tvNama  = findViewById(R.id.tv_profile_nama);
        TextView tvEmail = findViewById(R.id.tv_profile_email);
        if (tvNama != null)  tvNama.setText(sessionManager.getname());
        if (tvEmail != null) tvEmail.setText(sessionManager.getemail());
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