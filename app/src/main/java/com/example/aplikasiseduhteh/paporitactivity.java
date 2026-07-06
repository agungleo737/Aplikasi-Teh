package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
        listPaporit = new ArrayList<>();
        adapter = new TehAdapter(this, listPaporit);
        rvPaporit.setAdapter(adapter);
        muatDataFavoritRealtime();

        findViewById(R.id.logohome).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
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
    }

    private void muatDataFavoritRealtime() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        SessionManager session = new SessionManager(paporitactivity.this);
        apiService.getFavorit(session.gettoken()).enqueue(new Callback<TehResponse>() {
            @Override
            public void onResponse(@NonNull Call<TehResponse> call, @NonNull Response<TehResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TehModel> dataTeh = response.body().getData();
                    if (dataTeh != null) {
                        FavoritManager.setFromModels(dataTeh);
                        listPaporit.clear();
                        for (TehModel model : dataTeh) {
                            String hargaFormat = "Rp " + String.format("%,.0f", model.getHarga());
                            String namaGambar     = model.getGambar()     != null ? model.getGambar()     : "";
                            String namaGambarFull = model.getGambarFull() != null ? model.getGambarFull() : "";

                            Teh tehFav = new Teh(
                                    model.getId(),
                                    model.getNamaTeh(),
                                    hargaFormat,
                                    model.getDeskripsi() != null ? model.getDeskripsi() : "Tidak ada deskripsi.",
                                    namaGambar,
                                    namaGambarFull,
                                    model.getStok(),
                                    model.getKategori()
                            );
                            tehFav.setSellerId(model.getSellerId());
                            listPaporit.add(tehFav);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                updateView();
            }

            @Override
            public void onFailure(@NonNull Call<TehResponse> call, @NonNull Throwable t) {
                Toast.makeText(paporitactivity.this, "Gagal memuat favorit", Toast.LENGTH_SHORT).show();
                updateView();
            }
        });
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