package com.example.aplikasiseduhteh;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etPasswordLogin = findViewById(R.id.et_password_login);
        TextView tvKeRegister = findViewById(R.id.tv_ke_register);
        final boolean[] isVisible = {false};

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v -> {
            //pindah ke Home
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        //PINDAH HALAMAN
        tvKeRegister.setOnClickListener(v -> {
            //pindah dari login ke register
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
        });
        etPasswordLogin.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (etPasswordLogin.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                    if (event.getRawX() >= (etPasswordLogin.getRight() - etPasswordLogin.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (isVisible[0]) {
                            etPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password, 0, R.drawable.icon_matatutup, 0);
                            isVisible[0] = false;
                        } else {
                            etPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password, 0, R.drawable.icon_matabuka, 0);
                            isVisible[0] = true;
                        }
                        etPasswordLogin.setSelection(etPasswordLogin.getText().length());
                        return true;
                    }
                }
            }
            return false;
        });
    }
}