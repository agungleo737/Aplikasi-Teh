package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvTeh;
    private TehAdapter adapter;
    private ImageView imgNotFound;
    private View bannerTeh;
    private List<Teh> listData;
    private TextView tvTopPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        rvTeh = findViewById(R.id.tabelbarang1);
        imgNotFound = findViewById(R.id.notfound);
        bannerTeh = findViewById(R.id.teh_banner);
        tvTopPopular = findViewById(R.id.top_popular_txt);
        EditText searchBar = findViewById(R.id.search_bar);

        rvTeh.setLayoutManager(new GridLayoutManager(this, 2));

        listData = new ArrayList<>();
        adapter = new TehAdapter(this, listData);
        rvTeh.setAdapter(adapter);
        muatdata();
        setupNavigation();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if (newText.isEmpty()) {
                    bannerTeh.setVisibility(View.VISIBLE);
                    tvTopPopular.setVisibility(View.VISIBLE);
                } else {
                    bannerTeh.setVisibility(View.GONE);
                    tvTopPopular.setVisibility(View.GONE);
                }
                filter(newText);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void muatdata() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        SessionManager session = new SessionManager(MainActivity.this);
        Call<TehResponse> call = apiService.getAllProduk(session.gettoken());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TehResponse> call, @NonNull Response<TehResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TehModel> dataTeh = response.body().getData();
                    if (dataTeh != null && !dataTeh.isEmpty()) {
                        listData.clear();
                        for (TehModel model : dataTeh) {
                            String hargaFormat = "Rp " + String.format("%,.0f", model.getHarga());
                            String namaGambar     = model.getGambar()     != null ? model.getGambar()     : "";
                            String namaGambarFull = model.getGambarFull() != null ? model.getGambarFull() : "";

                            Teh tehBaru = new Teh(
                                    model.getId(),
                                    model.getNamaTeh(),
                                    hargaFormat,
                                    model.getDeskripsi() != null ? model.getDeskripsi() : "Tidak ada deskripsi.",
                                    namaGambar,
                                    namaGambarFull,
                                    model.getStok(),
                                    model.getKategori()
                            );
                            listData.add(tehBaru);
                        }
                        adapter.notifyDataSetChanged();
                        FavoritManager.refresh(MainActivity.this, () -> adapter.notifyDataSetChanged());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Gagal memuat daftar menu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TehResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal terhubung, periksa koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String text) {
        List<Teh> filtered = new ArrayList<>();
        for (Teh item : listData) {
            if (item.getNama().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(item);
            }
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
            bannerTeh.setVisibility(View.VISIBLE);
            adapter.filterList(listData);
            rvTeh.smoothScrollToPosition(0);
        });
        findViewById(R.id.logopaporit).setOnClickListener(v -> startActivity(new Intent(this, paporitactivity.class)));
        findViewById(R.id.belanjaya).setOnClickListener(v -> startActivity(new Intent(this, keranjang.class)));
        findViewById(R.id.logoesplor).setOnClickListener(v -> startActivity(new Intent(this, eksplorasi.class)));
        findViewById(R.id.potopropil).setOnClickListener(v -> startActivity(new Intent(this, profile.class)));
    }
}