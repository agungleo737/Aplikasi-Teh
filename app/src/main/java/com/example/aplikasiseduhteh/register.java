package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class register extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        sessionManager = new SessionManager(this);

        etName     = findViewById(R.id.username);
        etEmail    = findViewById(R.id.email);
        etPassword = findViewById(R.id.et_password);
        TextView tvKeLogin = findViewById(R.id.tv_ke_login);
        Button btnRegister = findViewById(R.id.btn_signup);

        btnRegister.setOnClickListener(v -> doRegister());

        tvKeLogin.setOnClickListener(v -> {
            startActivity(new Intent(register.this, login.class));
        });

        // password visibility
        final boolean[] isVisible = {false};
        etPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (etPassword.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                    int drawableWidth = etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    if (event.getX() >= (etPassword.getWidth() - etPassword.getPaddingEnd() - drawableWidth)) {
                        if (isVisible[0]) {
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password, 0, R.drawable.icon_matatutup, 0);
                            isVisible[0] = false;
                        } else {
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password, 0, R.drawable.icon_matabuka, 0);
                            isVisible[0] = true;
                        }
                        etPassword.setSelection(etPassword.getText().length());
                        return true;
                    }
                }
            }
            return false;
        });
    }

    private void doRegister() {
        String name     = etName.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.register(name, email, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body() != null ? response.body().string() : response.errorBody().string();
                    JSONObject obj = new JSONObject(json);

                    if (response.isSuccessful() && obj.optBoolean("success")) {
                        if (response.isSuccessful() && obj.optBoolean("success")) {
                            notifmanager.tambahnotif(register.this,
                                    "Registrasi Berhasil",
                                    "Akun kamu berhasil dibuat. Silakan login.");
                            Toast.makeText(register.this, "Registrasi berhasil.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(register.this, login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(register.this, obj.optString("message", "Registrasi gagal"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(register.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(register.this, "Gagal konek ke server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}