package com.example.aplikasiseduhteh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class paporitactivity extends AppCompatActivity {
    private RecyclerView rvPaporit;
    private ImageView layoutKosong;
    private List<Teh> listPaporit;
    private TehAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paporitactivity);

        rvPaporit = findViewById(R.id.rv_paporit);
        layoutKosong = findViewById(R.id.layout_kosong);
        rvPaporit.setLayoutManager(new GridLayoutManager(this, 2));

        // Data
        List<Teh> semuaTeh = new ArrayList<>();
        semuaTeh.add(new Teh("Teh Hijau Sencha", "50.000", "Segar dan aromatik.", R.drawable.teh1, R.drawable.img_teh1, 50, "Hijau"));
        semuaTeh.add(new Teh("Teh Hijau Long Jing", "90.000", "Rasa lembut dan manis.", R.drawable.teh2, R.drawable.img_teh2, 50, "Hijau"));
        semuaTeh.add(new Teh("Teh Hijau Bancha", "40.000", "Kaya mineral.", R.drawable.teh3, R.drawable.img_teh3, 40, "Hijau"));
        semuaTeh.add(new Teh("Teh Buah", "50.000", "Rasa segar.", R.drawable.teh4, R.drawable.img_teh4, 70, "Buah"));
        semuaTeh.add(new Teh("Teh Herbal Rosehip", "55.000", "Antioksidan.", R.drawable.teh5, R.drawable.img_teh5, 90, "Herbal"));
        semuaTeh.add(new Teh("Teh Hitam Da Hong Pao", "150.000", "Aroma kuat.", R.drawable.teh6, R.drawable.img_teh6, 40, "Merah"));
        semuaTeh.add(new Teh("Teh Putih Shao Mei", "70.000", "Earthy.", R.drawable.teh7, R.drawable.img_teh7, 40, "Wangi"));
        semuaTeh.add(new Teh("Teh Hijau neddle", "270.000", "Lembut.", R.drawable.teh8, R.drawable.img_teh8, 30, "Hijau"));
        semuaTeh.add(new Teh("Teh Putih Shao Mei", "70.000", "Earthy.", R.drawable.teh9, R.drawable.img_teh9, 40, "Wangi"));
        semuaTeh.add(new Teh("Teh Bai Mu Dan", "100.000", "Manis.", R.drawable.teh10, R.drawable.img_teh10, 70, "Melati"));
        semuaTeh.add(new Teh("Teh Bai Mu Dan", "100.000", "Manis.", R.drawable.teh11, R.drawable.img_teh11, 50, "Melati"));
        semuaTeh.add(new Teh("Teh Buah Spesial", "50.000", "Segar.", R.drawable.teh12, R.drawable.img_teh12, 340, "Buah"));
        semuaTeh.add(new Teh("Teh Herbal Rosehip Plus", "55.000", "Antioksidan.", R.drawable.teh13, R.drawable.img_teh13, 6499, "Herbal"));
        semuaTeh.add(new Teh("Teh Hitam Premium", "150.000", "Kuat.", R.drawable.teh14, R.drawable.img_teh14, 30, "Pahit"));
        semuaTeh.add(new Teh("Teh Hijau Silver", "270.000", "Premium.", R.drawable.teh15, R.drawable.img_teh15, 39, "Hijau"));
        semuaTeh.add(new Teh("Teh Putih Klasik", "70.000", "Khas.", R.drawable.teh16, R.drawable.img_teh16, 347, "Wangi"));
        semuaTeh.add(new Teh("Teh Bai Mu Dan Ori", "100.000", "Original.", R.drawable.teh17, R.drawable.img_teh17, 48, "Melati"));
        semuaTeh.add(new Teh("Teh Herbal Rosehip Plus", "55.000", "Antioksidan.", R.drawable.teh18, R.drawable.img_teh18, 23, "Herbal"));
        semuaTeh.add(new Teh("Teh Bai Mu Dan Gold", "100.000", "Emas.", R.drawable.teh19, R.drawable.img_teh19, 24, "Melati"));
        semuaTeh.add(new Teh("Teh Hitam Earl Grey Special", "55.000", "Spesial.", R.drawable.teh20, R.drawable.img_teh20, 34, "Pahit"));

        listPaporit = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("FavoritTeh", Context.MODE_PRIVATE);
        for (Teh teh : semuaTeh) {
            if (sp.getBoolean(teh.getNama(), false)) {
                listPaporit.add(teh);
            }
        }

        adapter = new TehAdapter(this, listPaporit);
        rvPaporit.setAdapter(adapter);

        // Navigasi
        findViewById(R.id.logohome).setOnClickListener(v -> finish());
        findViewById(R.id.belanjaya).setOnClickListener(v -> {
            startActivity(new Intent(this, keranjang.class));
            finish();
        });
        findViewById(R.id.logoesplor).setOnClickListener(v -> {
            startActivity(new Intent(this, eksplorasi.class));
            finish();
        });
        findViewById(R.id.potopropil).setOnClickListener(v -> {
            startActivity(new Intent(this, profile.class));
            finish();
        });

        updateView();
    }

    public void updateView() {
        if (listPaporit == null || listPaporit.isEmpty()) {
            layoutKosong.setVisibility(View.VISIBLE);
            rvPaporit.setVisibility(View.GONE);
        } else {
            layoutKosong.setVisibility(View.GONE);
            rvPaporit.setVisibility(View.VISIBLE);
        }
    }
}