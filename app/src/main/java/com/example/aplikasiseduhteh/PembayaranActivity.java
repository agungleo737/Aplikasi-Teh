package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PembayaranActivity extends AppCompatActivity {

    private static final int BIAYA_ADMIN = 2000;
    private Spinner spinnerMetode;
    private Button btnBayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        TextView txtProduk   = findViewById(R.id.txtProduk);
        TextView txtJumlah   = findViewById(R.id.txtJumlah);
        TextView txtRingkasanSubtotal = findViewById(R.id.txtRingkasanSubtotal);
        TextView txtTotal    = findViewById(R.id.txtTotal);
        spinnerMetode = findViewById(R.id.spinnerMetode);
        btnBayar      = findViewById(R.id.btnBayar);

        findViewById(R.id.btnBackBayar).setOnClickListener(v -> finish());

        // Isi pilihan metode pembayaran
        String[] metode = {"Dana", "OVO", "GoPay", "Transfer BCA", "Transfer BRI", "Mandiri", "COD (Bayar di Tempat)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, metode);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMetode.setAdapter(adapter);

        // Hitung dari keranjang
        int subtotal = 0, totalItem = 0;
        for (Teh teh : CartManager.keranjangList) {
            String clean = teh.getHarga().replaceAll("[^0-9]", "");
            if (!clean.isEmpty()) subtotal += Integer.parseInt(clean) * teh.getQtyDibeli();
            totalItem += teh.getQtyDibeli();
        }
        int total = subtotal + BIAYA_ADMIN;

        txtProduk.setText(CartManager.keranjangList.size() + " jenis produk");
        txtJumlah.setText(totalItem + " Item");
        txtRingkasanSubtotal.setText(rupiah(subtotal));
        txtTotal.setText(rupiah(total));

        btnBayar.setOnClickListener(v -> {
            if (CartManager.keranjangList.isEmpty()) {
                Toast.makeText(this, "Keranjang kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            prosesBayar();
        });
    }

    private String rupiah(int n) {
        return "Rp " + String.format("%,d", n).replace(',', '.');
    }

    private void prosesBayar() {
        btnBayar.setEnabled(false);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        SessionManager session = new SessionManager(this);

        List<CheckoutRequest.Item> items = new ArrayList<>();
        for (Teh teh : CartManager.keranjangList) {
            items.add(new CheckoutRequest.Item(teh.getid(), teh.getQtyDibeli()));
        }

        apiService.checkoutBatch(session.gettoken(), new CheckoutRequest(items))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            btnBayar.setEnabled(true);
                            String pesan = "Gagal memproses pembayaran";
                            if (response.code() == 401) pesan = "Sesi habis, login ulang";
                            Toast.makeText(PembayaranActivity.this, pesan, Toast.LENGTH_LONG).show();
                            return;
                        }

                        JSONArray orderDetails = new JSONArray();
                        try {
                            String raw = response.body() != null ? response.body().string() : "{}";
                            JSONArray orders = new JSONObject(raw).getJSONObject("data").getJSONArray("orders");
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject o = orders.getJSONObject(i);
                                JSONObject d = new JSONObject();
                                d.put("order_id",     o.getInt("id"));
                                d.put("nama_produk",  o.getString("nama_produk"));
                                d.put("jumlah_beli",  o.getInt("jumlah_beli"));
                                d.put("harga_satuan", o.getLong("harga_satuan"));
                                d.put("total_harga",  o.getLong("total_harga"));
                                orderDetails.put(d);
                            }
                        } catch (Exception ignored) {}

                        Toast.makeText(PembayaranActivity.this,
                                "Pembayaran via " + spinnerMetode.getSelectedItem() + " berhasil!",
                                Toast.LENGTH_SHORT).show();
                        CartManager.keranjangList.clear();

                        Intent intent = new Intent(PembayaranActivity.this, sukses_bayar.class);
                        intent.putExtra("order_details", orderDetails.toString());
                        intent.putExtra("nama_pembeli", session.getname());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        btnBayar.setEnabled(true);
                        Toast.makeText(PembayaranActivity.this,
                                "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
