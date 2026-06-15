package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class keranjang extends AppCompatActivity {
    private RecyclerView rvCart;
    private CartAdapter adapter;
    private TextView tvTotal;
    private LinearLayout layoutKosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        rvCart      = findViewById(R.id.rv_cart);
        tvTotal     = findViewById(R.id.tv_total_harga);
        layoutKosong = findViewById(R.id.layout_kosong);

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(CartManager.keranjangList, this::updateTotal);
        rvCart.setAdapter(adapter);

        findViewById(R.id.logohome).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        findViewById(R.id.logopaporit).setOnClickListener(v -> {
            startActivity(new Intent(this, paporitactivity.class));
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

        findViewById(R.id.btn_checkout).setOnClickListener(v -> {
            if (CartManager.keranjangList.isEmpty()) {
                Toast.makeText(this, "Keranjang belanja kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            laravelbayar();
        });

        updateTotal();
    }

    private void laravelbayar() {
        final int[] suksesCount = {0};
        final int totalItem = CartManager.keranjangList.size();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        for (Teh tehItem : CartManager.keranjangList) {
            int qtyBeli  = tehItem.getQtyDibeli();
            int idProduk = tehItem.getid();

            Call<ResponseBody> call = apiService.tambahorder(idProduk, qtyBeli);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        suksesCount[0]++;
                        if (suksesCount[0] == totalItem) {
                            Toast.makeText(keranjang.this, "Pembayaran Sukses!", Toast.LENGTH_SHORT).show();
                            CartManager.keranjangList.clear();
                            Intent intent = new Intent(keranjang.this, sukses_bayar.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        try {
                            String errormsg = response.errorBody() != null ? response.errorBody().string() : "";
                            if (errormsg.contains("stok") || errormsg.contains("stock") || errormsg.contains("insufficient")) {
                                Toast.makeText(keranjang.this, "Stok tidak mencukupi", Toast.LENGTH_LONG).show();
                            } else if (errormsg.contains("route") || errormsg.contains("404")) {
                                Toast.makeText(keranjang.this, "Gagal: jalur API tidak ditemukan!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(keranjang.this, "Gagal: " + errormsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(keranjang.this, "Gagal memproses pembayaran", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(keranjang.this, "Gagal terhubung dengan server, periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void cekStatusKeranjang() {
        if (CartManager.keranjangList == null || CartManager.keranjangList.isEmpty()) {
            layoutKosong.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
            findViewById(R.id.card_total_bayar).setVisibility(View.GONE);
        } else {
            layoutKosong.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);
            findViewById(R.id.card_total_bayar).setVisibility(View.VISIBLE);
        }
    }

    public void updateTotal() {
        int totalSemua = 0;
        for (Teh teh : CartManager.keranjangList) {
            String clean = teh.getHarga().replaceAll("[^0-9]", "");
            if (!clean.isEmpty()) {
                int hargasatuan = Integer.parseInt(clean);
                int qty = teh.getQtyDibeli();
                totalSemua += (hargasatuan * qty);
            }
        }
        tvTotal.setText("Rp " + String.format("%,d", totalSemua).replace(',', '.'));
        cekStatusKeranjang();
    }
}