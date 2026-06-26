package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TextView tvKosong;
    private TransaksiAdapter adapter;
    private final ArrayList<TransaksiModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        rv = findViewById(R.id.rvLaporan);
        tvKosong = findViewById(R.id.tvLaporanKosong);
        View back = findViewById(R.id.btnBackLaporan);
        if (back != null) back.setOnClickListener(v -> finish());

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransaksiAdapter(list);
        rv.setAdapter(adapter);

        loadLaporan();
    }

    private void loadLaporan() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        SessionManager session = new SessionManager(this);

        api.adminDaftarTransaksi(session.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(LaporanActivity.this,
                            "Gagal memuat laporan (khusus admin).", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray data  = root.optJSONArray("data");
                    list.clear();
                    if (data != null) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject o = data.getJSONObject(i);
                            String invoice = "#" + o.optInt("id_belanja")
                                    + "  •  " + o.optString("pembeli_nama");
                            String produk  = o.optString("nama_produk")
                                    + "  (x" + o.optInt("jumlah_beli") + ")";
                            String harga   = "Rp " + o.optLong("total_harga");
                            list.add(new TransaksiModel(
                                    invoice, produk, harga, "Berhasil", o.optString("tanggal")));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    tvKosong.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                } catch (Exception e) {
                    Toast.makeText(LaporanActivity.this,
                            "Format data salah: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LaporanActivity.this,
                        "Gagal konek: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
