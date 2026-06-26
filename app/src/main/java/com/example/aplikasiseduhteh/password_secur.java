package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class password_secur extends AppCompatActivity {

    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnSavePassword;
    private ImageView btnBack;
    private SessionManager session;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_secur);

        session    = new SessionManager(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSavePassword = findViewById(R.id.btn_save_password);
        btnBack = findViewById(R.id.btn_back);

        // Tombol Back
        btnBack.setOnClickListener(v -> finish());

        // Tombol Simpan
        btnSavePassword.setOnClickListener(v -> validateAndSave());
    }

    private void validateAndSave() {
        String currentPwd = etCurrentPassword.getText().toString().trim();
        String newPwd = etNewPassword.getText().toString().trim();
        String confirmPwd = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmPwd)) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPwd.length() < 8) {
            etNewPassword.setError("Password minimal 8 karakter!");
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            etConfirmPassword.setError("Konfirmasi password tidak cocok!");
            return;
        }

        btnSavePassword.setEnabled(false);
        apiService.changePassword(session.gettoken(), currentPwd, newPwd, confirmPwd)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        btnSavePassword.setEnabled(true);
                        if (response.isSuccessful()) {
                            notifmanager.tambahnotif(password_secur.this,
                                    "Password Diperbarui",
                                    "Password akun kamu berhasil diubah.");
                            Toast.makeText(password_secur.this,
                                    "Password berhasil diperbarui!", Toast.LENGTH_LONG).show();
                            finish();
                        } else if (response.code() == 422) {
                            // Password lama salah
                            etCurrentPassword.setError("Password lama salah!");
                            Toast.makeText(password_secur.this,
                                    "Password lama salah.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(password_secur.this,
                                    "Gagal ganti password (cek login).", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        btnSavePassword.setEnabled(true);
                        Toast.makeText(password_secur.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
