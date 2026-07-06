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

public class login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        if (sessionManager.isloggedin()) {
            Class<?> tujuan = sessionManager.isAdmin()
                    ? DaftarPenggunaActivity.class : MainActivity.class;
            startActivity(new Intent(this, tujuan));
            finish();
            return;
        }

        etEmail    = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);

        TextView tvKeRegister = findViewById(R.id.tv_ke_register);
        Button   btnLogin     = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v -> doLogin());
        tvKeRegister.setOnClickListener(v ->
                startActivity(new Intent(login.this, register.class)));

        // Toggle visibilitas password
        final boolean[] isVisible = {false};
        etPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (etPassword.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                    if (event.getRawX() >= (etPassword.getRight()
                            - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (isVisible[0]) {
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.icon_password, 0, R.drawable.icon_matatutup, 0);
                            isVisible[0] = false;
                        } else {
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.icon_password, 0, R.drawable.icon_matabuka, 0);
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

    private void doLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.login(email, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String rawBody = (response.body() != null)
                            ? response.body().string()
                            : (response.errorBody() != null ? response.errorBody().string() : "{}");

                    JSONObject obj = new JSONObject(rawBody);

                    if (response.isSuccessful() && obj.optBoolean("success")) {
                        String token = obj.getString("token");
                        JSONObject user = obj.getJSONObject("user");

                        String name  = user.optString("name", "");
                        String uEmail = user.optString("email", "");
                        int    userId = user.optInt("id", -1);
                        String role  = user.optString("role", "user");

                        // Simpan token, name, email, user_id, role
                        sessionManager.savesession(token, name, uEmail, userId, role);

                        notifmanager.tambahnotif(login.this,
                                "Selamat Datang",
                                "Kamu berhasil masuk ke akun.");

                        Toast.makeText(login.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                        Class<?> tujuan = "admin".equalsIgnoreCase(role)
                                ? DaftarPenggunaActivity.class : MainActivity.class;
                        startActivity(new Intent(login.this, tujuan));
                        finish();
                    } else {
                        Toast.makeText(login.this,
                                obj.optString("message", "Login gagal"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(login.this,
                        "Gagal konek ke server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}