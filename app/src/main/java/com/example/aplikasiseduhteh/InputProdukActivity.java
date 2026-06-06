package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputProdukActivity extends AppCompatActivity {

    private EditText etGambar, etNama, etKategori, etHarga, etStok, etDeskripsi;
    private Button btnSimpan;
    private ImageView btnBackInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_produk);
        etGambar = findViewById(R.id.et_gambar_teh);
        etNama = findViewById(R.id.et_nama_teh);
        etKategori = findViewById(R.id.et_kategori_teh);
        etHarga = findViewById(R.id.et_harga_teh);
        etStok = findViewById(R.id.et_stok_teh);
        etDeskripsi = findViewById(R.id.et_deskripsi_teh);
        btnSimpan = findViewById(R.id.btn_simpan_teh);
        btnBackInput = findViewById(R.id.btn_back_input);
        if (btnBackInput != null) {
            btnBackInput.setOnClickListener(v -> finish());
        }
        if (btnSimpan != null) {
            btnSimpan.setOnClickListener(v -> {
                String gambar = etGambar.getText().toString().trim();
                String nama = etNama.getText().toString().trim();
                String kategori = etKategori.getText().toString().trim();
                String hargaStr = etHarga.getText().toString().trim();
                String stokStr = etStok.getText().toString().trim();
                String deskripsi = etDeskripsi.getText().toString().trim();


                if (gambar.isEmpty() || nama.isEmpty() || kategori.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty() || deskripsi.isEmpty()) {
                    Toast.makeText(InputProdukActivity.this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int harga = Integer.parseInt(hargaStr);
                int stok = Integer.parseInt(stokStr);
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<Void> call = apiService.tambahProduk(gambar, nama, kategori, harga, stok, deskripsi);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InputProdukActivity.this, "Produk Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(InputProdukActivity.this, "Gagal menyimpan ke server!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(InputProdukActivity.this, "Error Jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}