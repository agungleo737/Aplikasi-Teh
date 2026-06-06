package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class keranjang extends AppCompatActivity {
    RecyclerView rvCart;
    CartAdapter adapter;
    TextView tvTotal;
    LinearLayout layoutKosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        // Inisialisasi View
        rvCart = findViewById(R.id.rv_cart);
        tvTotal = findViewById(R.id.tv_total_harga);
        layoutKosong = findViewById(R.id.layout_kosong);

        // Setup RecyclerView
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(CartManager.keranjangList, this::updateTotal);
        rvCart.setAdapter(adapter);

        // navigasi bawah
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

        // Tombol Checkout / Bayar
        findViewById(R.id.btn_checkout).setOnClickListener(v -> {
            // pindah ke halaman sukses bayar
            startActivity(new Intent(this, sukses_bayar.class));
        });

        updateTotal();
    }

    // untuk cek apakah list kosong atau tidak
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

    // update total harga
    public void updateTotal() {
        int total = 0;
        for (Teh teh : CartManager.keranjangList) {
            String clean = teh.getHarga().replaceAll("[^0-9]", "");
            if (!clean.isEmpty()) {
                total += Integer.parseInt(clean);
            }
        }
        // Format ke Rupiah
        tvTotal.setText("Rp " + String.format("%,d", total).replace(',', '.'));
        cekStatusKeranjang();
    }
}