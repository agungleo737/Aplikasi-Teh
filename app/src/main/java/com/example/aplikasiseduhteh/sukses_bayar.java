package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.NumberFormat;
import java.util.Locale;

public class sukses_bayar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukses_bayar);

        SessionManager session = new SessionManager(this);

        // Nama pembeli
        TextView tvNama = findViewById(R.id.tv_nama_pembeli);
        tvNama.setText(session.getname());
        String orderDetailsJson = getIntent().getStringExtra("order_details");
        LinearLayout containerItems = findViewById(R.id.container_order_items);
        TextView tvTotalSemua = findViewById(R.id.tv_total_semua);
        TextView tvOrderIds   = findViewById(R.id.tv_order_id_value);

        long grandTotal = 0;
        StringBuilder orderIdsBuilder = new StringBuilder();

        if (orderDetailsJson != null) {
            try {
                JSONArray details = new JSONArray(orderDetailsJson);
                for (int i = 0; i < details.length(); i++) {
                    JSONObject item = details.getJSONObject(i);

                    int    orderId    = item.optInt("order_id", -1);
                    String namaProduk = item.optString("nama_produk", "-");
                    int    jumlah     = item.optInt("jumlah_beli", 0);
                    long   hargaSat   = item.optLong("harga_satuan", 0);
                    long   totalItem  = item.optLong("total_harga", 0);
                    grandTotal += totalItem;

                    // Kumpulkan order IDs
                    if (orderId > 0) {
                        if (orderIdsBuilder.length() > 0) orderIdsBuilder.append(", ");
                        orderIdsBuilder.append("#").append(orderId);
                    }

                    // Inflate row item
                    View row = LayoutInflater.from(this)
                            .inflate(R.layout.item_sukses_bayar, containerItems, false);

                    ((TextView) row.findViewById(R.id.tv_item_nama)).setText(namaProduk);
                    ((TextView) row.findViewById(R.id.tv_item_qty))
                            .setText(jumlah + " x " + formatRupiah(hargaSat));
                    ((TextView) row.findViewById(R.id.tv_item_subtotal))
                            .setText(formatRupiah(totalItem));

                    containerItems.addView(row);
                }
            } catch (Exception e) {
                tvOrderIds.setText("-");
            }
        }

        tvOrderIds.setText(orderIdsBuilder.length() > 0 ? orderIdsBuilder.toString() : "-");
        tvTotalSemua.setText(formatRupiah(grandTotal));

        // Notifikasi lokal
        notifmanager.tambahnotif(this,
                "Pesanan Berhasil!",
                "Pesanan kamu sedang diproses. Terima kasih sudah berbelanja!");

        // Tombol Lacak Pesanan
        MaterialButton btnTrack = findViewById(R.id.btn_track);
        btnTrack.setOnClickListener(v -> {
            Intent intent = new Intent(sukses_bayar.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Tombol Kembali ke Beranda
        MaterialButton btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(sukses_bayar.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String formatRupiah(long angka) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp " + nf.format(angka);
    }
}