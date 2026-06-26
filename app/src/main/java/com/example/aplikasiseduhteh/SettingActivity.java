package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    private EditText etNama, etEmail, etPassword;
    private Button btnTambah;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        session    = new SessionManager(this);
        etNama     = findViewById(R.id.etAdminNama);
        etEmail    = findViewById(R.id.etAdminEmail);
        etPassword = findViewById(R.id.etAdminPassword);
        btnTambah  = findViewById(R.id.btnTambahAdmin);

        findViewById(R.id.btnBackSetting).setOnClickListener(v -> finish());
        btnTambah.setOnClickListener(v -> tambahAdmin());
        findViewById(R.id.btnLogoutSetting).setOnClickListener(this::doLogout);
    }

    private void tambahAdmin() {
        String nama = etNama.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Lengkapi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        btnTambah.setEnabled(false);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.tambahAdmin(session.gettoken(), nama, email, pass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                btnTambah.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(SettingActivity.this, "Admin baru berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                    etNama.setText(""); etEmail.setText(""); etPassword.setText("");
                } else {
                    String pesan = "Gagal menambah admin";
                    if (response.code() == 422) pesan = "Email sudah dipakai / input salah";
                    Toast.makeText(SettingActivity.this, pesan, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btnTambah.setEnabled(true);
                Toast.makeText(SettingActivity.this, "Gagal konek: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doLogout(View v) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.logout(session.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { selesaiLogout(); }
            @Override public void onFailure(Call<ResponseBody> call, Throwable t) { selesaiLogout(); }
        });
    }

    private void selesaiLogout() {
        FavoritManager.clear();
        session.clearsession();
        Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
