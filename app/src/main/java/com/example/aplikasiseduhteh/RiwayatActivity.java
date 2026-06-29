package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

public class RiwayatActivity extends AppCompatActivity {

    private RecyclerView rvRiwayat;
    private View emptyView;
    private RiwayatAdapter adapter;
    private final List<RiwayatModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        rvRiwayat = findViewById(R.id.rv_riwayat);
        emptyView = findViewById(R.id.empty_riwayat);
        findViewById(R.id.btn_back_riwayat).setOnClickListener(v -> finish());

        rvRiwayat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RiwayatAdapter(this, list, this::loadRiwayat);
        rvRiwayat.setAdapter(adapter);

        loadRiwayat();
    }

    private void loadRiwayat() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        SessionManager session = new SessionManager(this);

        apiService.getRiwayat(session.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(RiwayatActivity.this, "Gagal memuat riwayat.", Toast.LENGTH_SHORT).show();
                    tampilkanKosong(true);
                    return;
                }

                try {
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray data  = root.optJSONArray("data");

                    list.clear();
                    if (data != null) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject o = data.getJSONObject(i);
                            list.add(new RiwayatModel(
                                    o.optInt("id"),
                                    o.optString("tanggal"),
                                    o.optString("nama_produk"),
                                    o.optInt("jumlah_beli"),
                                    o.optLong("harga_satuan"),
                                    o.optLong("total_harga"),
                                    o.isNull("gambar") ? null : o.optString("gambar"),
                                    o.optString("status", "menunggu"),
                                    o.isNull("estimasi_siap")  ? "" : o.optString("estimasi_siap"),
                                    o.isNull("estimasi_kirim") ? "" : o.optString("estimasi_kirim")
                            ));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    tampilkanKosong(list.isEmpty());
                } catch (Exception e) {
                    Toast.makeText(RiwayatActivity.this, "Format data salah: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    tampilkanKosong(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RiwayatActivity.this, "Gagal konek: " + t.getMessage(), Toast.LENGTH_LONG).show();
                tampilkanKosong(true);
            }
        });
    }

    private void tampilkanKosong(boolean kosong) {
        emptyView.setVisibility(kosong ? View.VISIBLE : View.GONE);
        rvRiwayat.setVisibility(kosong ? View.GONE : View.VISIBLE);
    }
}
