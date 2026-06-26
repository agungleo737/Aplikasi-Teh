package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class category extends AppCompatActivity {
    private RecyclerView rvCategory;
    private TehAdapter adapter;
    private List<Teh> filteredList = new ArrayList<>();
    private List<Teh> allList = new ArrayList<>();
    private ImageView imgNotFound, btnBack;
    private TextView tvTitle;
    private String kategoriPilihan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        kategoriPilihan = getIntent().getStringExtra("FILTER_KEY");
        tvTitle = findViewById(R.id.tv_title_category);
        rvCategory = findViewById(R.id.rv_category_result);
        imgNotFound = findViewById(R.id.img_not_found_cat);
        btnBack = findViewById(R.id.btn_back_category);
        SearchView searchView = findViewById(R.id.search_bar_category);

        if (kategoriPilihan != null) {
            tvTitle.setText("Teh " + kategoriPilihan);
        }

        rvCategory.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new TehAdapter(this, filteredList);
        rvCategory.setAdapter(adapter);
        datalaravel();
        btnBack.setOnClickListener(v -> finish());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    adapter.filterList(new ArrayList<>(allList));
                    rvCategory.setVisibility(View.VISIBLE);
                    imgNotFound.setVisibility(View.GONE);
                    return true;
                }

                List<Teh> searchResult = new ArrayList<>();
                for (Teh item : allList) {
                    if (item.getNama().toLowerCase().contains(newText.toLowerCase())) {
                        searchResult.add(item);
                    }
                }

                if (searchResult.isEmpty()) {
                    rvCategory.setVisibility(View.GONE);
                    imgNotFound.setVisibility(View.VISIBLE);
                } else {
                    rvCategory.setVisibility(View.VISIBLE);
                    imgNotFound.setVisibility(View.GONE);
                    adapter.filterList(searchResult);
                }
                return true;
            }
        });
    }

    private void datalaravel() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        SessionManager session = new SessionManager(category.this);
        Call<TehResponse> call = apiService.getAllProduk(session.gettoken());

        call.enqueue(new Callback<TehResponse>() {
            @Override
            public void onResponse(Call<TehResponse> call, Response<TehResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TehModel> tehserver = response.body().getData();
                    filteredList.clear();
                    allList.clear();

                    if (kategoriPilihan != null) {
                        for (TehModel model : tehserver) {
                            if (model.getKategori().equalsIgnoreCase(kategoriPilihan)) {
                                String hargaFormat = "Rp " + (int) model.getHarga();
                                String deskripsiFormat = model.getDeskripsi() != null ? model.getDeskripsi() : "Tidak ada deskripsi.";

                                String namaGambar     = model.getGambar()     != null ? model.getGambar()     : "";
                                String namaGambarFull = model.getGambarFull() != null ? model.getGambarFull() : "";

                                Teh teh = new Teh(
                                        model.getId(),
                                        model.getNamaTeh(),
                                        hargaFormat,
                                        deskripsiFormat,
                                        namaGambar,
                                        namaGambarFull,
                                        model.getStok(),
                                        model.getKategori()
                                );
                                filteredList.add(teh);
                                allList.add(teh);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    // Sinkron status favorit
                    FavoritManager.refresh(category.this, () -> adapter.notifyDataSetChanged());

                    if (filteredList.isEmpty()) {
                        rvCategory.setVisibility(View.GONE);
                        imgNotFound.setVisibility(View.VISIBLE);
                    } else {
                        rvCategory.setVisibility(View.VISIBLE);
                        imgNotFound.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(category.this, "Server Laravel gagal merespon data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TehResponse> call, Throwable t) {
                Toast.makeText(category.this, "Gagal konek ke Laravel: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}