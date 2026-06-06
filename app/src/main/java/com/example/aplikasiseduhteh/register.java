package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);

        EditText etPassword = findViewById(R.id.et_password);
        TextView tvKeLogin = findViewById(R.id.tv_ke_login);
        final boolean[] isPasswordVisible = {false};

        // PINDAH BALIK KE LOGIN
        tvKeLogin.setOnClickListener(v -> {
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
            // halaman Regis ini ditutup pas pindah
        });

        // ANIMASI MATA
        etPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (etPassword.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                    int drawableWidth = etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    if (event.getX() >= (etPassword.getWidth() - etPassword.getPaddingEnd() - drawableWidth)) {
                        if (isPasswordVisible[0]) {
                            // Sembunyikan Password
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password, 0, R.drawable.icon_matatutup, 0);
                            isPasswordVisible[0] = false;
                        } else {
                            // Tampilkan Password
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password, 0, R.drawable.icon_matabuka, 0);
                            isPasswordVisible[0] = true;
                        }
                        // Biar kursor tetep di ujung teks
                        etPassword.setSelection(etPassword.getText().length());
                        return true;
                    }
                }
            }
            return false;
        });
    }
}