package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class eksplorasi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eksplorasi);
        setupCategoryButtons();
        setupBottomNavigation();
    }

    private void setupCategoryButtons() {
        findViewById(R.id.cat_hijau).setOnClickListener(v -> pindahKeCategory("Hijau"));
        findViewById(R.id.cat_merah).setOnClickListener(v -> pindahKeCategory("Merah"));
        findViewById(R.id.cat_buah).setOnClickListener(v -> pindahKeCategory("Buah"));
        findViewById(R.id.cat_wangi).setOnClickListener(v -> pindahKeCategory("Wangi"));
        findViewById(R.id.cat_pahit).setOnClickListener(v -> pindahKeCategory("Pahit"));
        findViewById(R.id.cat_herbal).setOnClickListener(v -> pindahKeCategory("Herbal"));
        findViewById(R.id.cat_melati).setOnClickListener(v -> pindahKeCategory("Melati"));
        findViewById(R.id.cat_susu).setOnClickListener(v -> pindahKeCategory("Susu"));
    }

    private void setupBottomNavigation() {
        findViewById(R.id.logopaporit).setOnClickListener(v -> startActivity(new Intent(this, paporitactivity.class)));
        findViewById(R.id.belanjaya).setOnClickListener(v -> startActivity(new Intent(this, keranjang.class)));
        findViewById(R.id.logohome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        findViewById(R.id.potopropil).setOnClickListener(v -> startActivity(new Intent(this, profile.class)));
    }

    private void pindahKeCategory(String kategori) {
        Intent intent = new Intent(this, category.class);
        intent.putExtra("FILTER_KEY", kategori);
        startActivity(intent);
    }
}