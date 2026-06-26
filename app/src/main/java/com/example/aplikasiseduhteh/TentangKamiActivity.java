package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TentangKamiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_kami);

        // Tombol back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        LinearLayout navBeranda   = findViewById(R.id.navBeranda);
        LinearLayout navKoleksi   = findViewById(R.id.navKoleksi);
        LinearLayout navKomunitas = findViewById(R.id.navKomunitas);
        LinearLayout navTentang   = findViewById(R.id.navTentang);

        // Beranda
        navBeranda.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        navKoleksi.setOnClickListener(v ->
                startActivity(new Intent(this, paporitactivity.class)));

        navKomunitas.setOnClickListener(v ->
                Toast.makeText(this, "Komunitas segera hadir", Toast.LENGTH_SHORT).show());
        navTentang.setOnClickListener(v -> {});
    }
}
