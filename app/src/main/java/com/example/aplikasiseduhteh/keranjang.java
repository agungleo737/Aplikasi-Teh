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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class keranjang extends AppCompatActivity {

    private RecyclerView  rvCart;
    private CartAdapter   adapter;
    private TextView      tvTotal;
    private LinearLayout  layoutKosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        rvCart       = findViewById(R.id.rv_cart);
        tvTotal      = findViewById(R.id.tv_total_harga);
        layoutKosong = findViewById(R.id.layout_kosong);

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(CartManager.keranjangList, this::updateTotal);
        rvCart.setAdapter(adapter);

        findViewById(R.id.logohome).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class)); finish();
        });
        findViewById(R.id.logopaporit).setOnClickListener(v -> {
            startActivity(new Intent(this, paporitactivity.class)); finish();
        });
        findViewById(R.id.logoesplor).setOnClickListener(v -> {
            startActivity(new Intent(this, eksplorasi.class)); finish();
        });
        findViewById(R.id.potopropil).setOnClickListener(v -> {
            startActivity(new Intent(this, profile.class)); finish();
        });

        findViewById(R.id.btn_checkout).setOnClickListener(v -> {
            if (CartManager.keranjangList.isEmpty()) {
                Toast.makeText(this, "Keranjang belanja kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            // Lanjut ke pembayaran
            startActivity(new Intent(keranjang.this, PembayaranActivity.class));
        });

        updateTotal();
    }

    private void cekStatusKeranjang() {
        boolean kosong = CartManager.keranjangList == null || CartManager.keranjangList.isEmpty();
        layoutKosong.setVisibility(kosong ? View.VISIBLE : View.GONE);
        rvCart.setVisibility(kosong ? View.GONE : View.VISIBLE);
        findViewById(R.id.card_total_bayar).setVisibility(kosong ? View.GONE : View.VISIBLE);
    }

    public void updateTotal() {
        int totalSemua = 0;
        for (Teh teh : CartManager.keranjangList) {
            String clean = teh.getHarga().replaceAll("[^0-9]", "");
            if (!clean.isEmpty()) {
                totalSemua += Integer.parseInt(clean) * teh.getQtyDibeli();
            }
        }
        tvTotal.setText("Rp " + String.format("%,d", totalSemua).replace(',', '.'));
        cekStatusKeranjang();
    }
}