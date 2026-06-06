package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvTeh;
    private TehAdapter adapter;
    private ImageView imgNotFound, backTehHeader;
    private List<Teh> listData;
    private TextView tvTopPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Binding
        rvTeh = findViewById(R.id.tabelbarang1);
        imgNotFound = findViewById(R.id.notfound);
        backTehHeader = findViewById(R.id.backteh);
        tvTopPopular = findViewById(R.id.top_popular_txt);
        SearchView searchView = findViewById(R.id.search_bar);

        rvTeh.setLayoutManager(new GridLayoutManager(this, 2));

        // Inisialisasi Data
        initData();

        adapter = new TehAdapter(this, listData);
        rvTeh.setAdapter(adapter);

        setupNavigation();

        // Search Bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    backTehHeader.setVisibility(View.VISIBLE);
                    tvTopPopular.setVisibility(View.VISIBLE);
                } else {
                    backTehHeader.setVisibility(View.GONE);
                    tvTopPopular.setVisibility(View.GONE);
                }
                filter(newText);
                return true;
            }
        });
    }

    private void initData() {
        listData = new ArrayList<>();
        listData.add(new Teh("Teh Hijau Sencha", "50.000", "Segar dan aromatik.", R.drawable.teh1, R.drawable.img_teh1, 50, "Hijau"));
        listData.add(new Teh("Teh Hijau Long Jing", "90.000", "Rasa lembut dan manis.", R.drawable.teh2, R.drawable.img_teh2, 50, "Hijau"));
        listData.add(new Teh("Teh Hijau Bancha", "40.000", "Kaya mineral.", R.drawable.teh3, R.drawable.img_teh3, 40, "Hijau"));
        listData.add(new Teh("Teh Buah", "50.000", "Rasa segar.", R.drawable.teh4, R.drawable.img_teh4, 70, "Buah"));
        listData.add(new Teh("Teh Herbal Rosehip", "55.000", "Antioksidan.", R.drawable.teh5, R.drawable.img_teh5, 90, "Herbal"));
        listData.add(new Teh("Teh Hitam Da Hong Pao", "150.000", "Aroma kuat.", R.drawable.teh6, R.drawable.img_teh6, 40, "Merah"));
        listData.add(new Teh("Teh Putih Shao Mei", "70.000", "Earthy.", R.drawable.teh7, R.drawable.img_teh7, 40, "Wangi"));
        listData.add(new Teh("Teh Hijau neddle", "270.000", "Lembut.", R.drawable.teh8, R.drawable.img_teh8, 30, "Hijau"));
        listData.add(new Teh("Teh Putih Shao Mei", "70.000", "Earthy.", R.drawable.teh9, R.drawable.img_teh9, 40, "Wangi"));
        listData.add(new Teh("Teh Bai Mu Dan", "100.000", "Manis.", R.drawable.teh10, R.drawable.img_teh10, 70, "Melati"));
        listData.add(new Teh("Teh Bai Mu Dan", "100.000", "Manis.", R.drawable.teh11, R.drawable.img_teh11, 50, "Melati"));
        listData.add(new Teh("Teh Buah Spesial", "50.000", "Segar.", R.drawable.teh12, R.drawable.img_teh12, 340, "Buah"));
        listData.add(new Teh("Teh Herbal Rosehip Plus", "55.000", "Antioksidan.", R.drawable.teh13, R.drawable.img_teh13, 6499, "Herbal"));
        listData.add(new Teh("Teh Hitam Premium", "150.000", "Kuat.", R.drawable.teh14, R.drawable.img_teh14, 30, "Pahit"));
        listData.add(new Teh("Teh Hijau Silver", "270.000", "Premium.", R.drawable.teh15, R.drawable.img_teh15, 39, "Hijau"));
        listData.add(new Teh("Teh Putih Klasik", "70.000", "Khas.", R.drawable.teh16, R.drawable.img_teh16, 347, "Wangi"));
        listData.add(new Teh("Teh Bai Mu Dan Ori", "100.000", "Original.", R.drawable.teh17, R.drawable.img_teh17, 48, "Melati"));
        listData.add(new Teh("Teh Herbal Rosehip Plus", "55.000", "Antioksidan.", R.drawable.teh18, R.drawable.img_teh18, 23, "Herbal"));
        listData.add(new Teh("Teh Bai Mu Dan Gold", "100.000", "Emas.", R.drawable.teh19, R.drawable.img_teh19, 24, "Melati"));
        listData.add(new Teh("Teh Hitam Earl Grey Special", "55.000", "Spesial.", R.drawable.teh20, R.drawable.img_teh20, 34, "Pahit"));
    }

    private void filter(String text) {
        List<Teh> filtered = new ArrayList<>();
        for (Teh item : listData) {
            if (item.getNama().toLowerCase().contains(text.toLowerCase())) filtered.add(item);
        }
        if (filtered.isEmpty()) {
            rvTeh.setVisibility(View.GONE);
            imgNotFound.setVisibility(View.VISIBLE);
        } else {
            rvTeh.setVisibility(View.VISIBLE);
            imgNotFound.setVisibility(View.GONE);
            adapter.filterList(filtered);
        }
    }

    private void setupNavigation() {
        findViewById(R.id.logohome).setOnClickListener(v -> {
            backTehHeader.setVisibility(View.VISIBLE);
            adapter.filterList(listData);
            rvTeh.smoothScrollToPosition(0);
        });
        findViewById(R.id.logopaporit).setOnClickListener(v -> startActivity(new Intent(this, paporitactivity.class)));
        findViewById(R.id.belanjaya).setOnClickListener(v -> startActivity(new Intent(this, keranjang.class)));
        findViewById(R.id.logoesplor).setOnClickListener(v -> startActivity(new Intent(this, eksplorasi.class)));
        findViewById(R.id.potopropil).setOnClickListener(v -> startActivity(new Intent(this, profile.class)));
    }
}