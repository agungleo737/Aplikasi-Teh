package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelolaProdukActivity extends AppCompatActivity
        implements KelolaProdukAdapter.OnAksiListener {

    private RecyclerView rvKelola;
    private KelolaProdukAdapter adapter;
    private final List<TehModel> listData = new ArrayList<>();
    private ApiService apiService;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_produk);

        apiService = ApiClient.getClient().create(ApiService.class);
        session    = new SessionManager(this);

        rvKelola = findViewById(R.id.rv_kelola);
        rvKelola.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KelolaProdukAdapter(this, listData, this);
        rvKelola.setAdapter(adapter);

        ImageView btnBack = findViewById(R.id.btn_back_kelola);
        btnBack.setOnClickListener(v -> finish());

        FloatingActionButton fab = findViewById(R.id.fab_tambah);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, InputProdukActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProduk();
    }

    private void loadProduk() {
        apiService.getAllProduk(session.gettoken()).enqueue(new Callback<TehResponse>() {
            @Override
            public void onResponse(Call<TehResponse> call, Response<TehResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getData() != null) {
                    listData.clear();
                    listData.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(KelolaProdukActivity.this,
                            "Gagal memuat produk.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TehResponse> call, Throwable t) {
                Toast.makeText(KelolaProdukActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onEdit(TehModel produk) {
        Intent i = new Intent(this, InputProdukActivity.class);
        i.putExtra("mode", "edit");
        i.putExtra("id", produk.getId());
        i.putExtra("nama", produk.getNamaTeh());
        i.putExtra("kategori", produk.getKategori());
        i.putExtra("harga", String.valueOf((long) produk.getHarga()));
        i.putExtra("stok", String.valueOf(produk.getStok()));
        i.putExtra("deskripsi", produk.getDeskripsi());
        startActivity(i);
    }
    @Override
    public void onHapus(TehModel produk) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Produk")
                .setMessage("Yakin hapus \"" + produk.getNamaTeh() + "\"?")
                .setPositiveButton("Hapus", (dialog, which) -> hapusProduk(produk))
                .setNegativeButton("Batal", null)
                .show();
    }

    private void hapusProduk(TehModel produk) {
        apiService.hapusProduk(produk.getId(), session.gettoken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(KelolaProdukActivity.this,
                                    "Produk dihapus", Toast.LENGTH_SHORT).show();
                            loadProduk();
                        } else {
                            Toast.makeText(KelolaProdukActivity.this,
                                    "Gagal hapus (cek login/akses).", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(KelolaProdukActivity.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
