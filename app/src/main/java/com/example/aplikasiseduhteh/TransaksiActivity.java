package com.example.aplikasiseduhteh;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaksiActivity extends AppCompatActivity implements TransaksiAdapter.AksiListener {

    private RecyclerView rv;
    private TransaksiAdapter adapter;
    private final ArrayList<TransaksiModel> list = new ArrayList<>();
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        session = new SessionManager(this);
        rv = findViewById(R.id.rvTransaksi);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransaksiAdapter(list, this);
        rv.setAdapter(adapter);

        loadPesananMasuk();
    }

    private void loadPesananMasuk() {
        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getPenjualan(session.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(TransaksiActivity.this,
                            "Gagal memuat pesanan masuk.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray data  = root.optJSONArray("data");
                    list.clear();
                    if (data != null) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject o = data.getJSONObject(i);
                            String invoice = "#" + o.optInt("id");
                            String produk  = o.optString("nama_produk")
                                    + "  (x" + o.optInt("jumlah_beli") + ")";
                            String harga   = "Rp " + o.optLong("total_harga");
                            list.add(new TransaksiModel(
                                    o.optInt("id"),
                                    invoice,
                                    produk,
                                    o.optString("nama_pembeli", "-"),
                                    harga,
                                    o.optString("status", "menunggu"),
                                    o.optString("tanggal"),
                                    o.isNull("estimasi_siap")  ? "" : o.optString("estimasi_siap"),
                                    o.isNull("estimasi_kirim") ? "" : o.optString("estimasi_kirim")
                            ));
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(TransaksiActivity.this,
                            "Format data salah: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TransaksiActivity.this,
                        "Gagal konek: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Penjual verifikasi
    @Override
    public void onProses(TransaksiModel m) {
        pilihTanggal("Pilih estimasi SIAP", siap ->
                pilihTanggal("Pilih estimasi KIRIM", kirim ->
                        prosesPesanan(m.getId(), siap, kirim)));
    }

    @Override
    public void onKirim(TransaksiModel m) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.kirimPesanan(m.getId(), session.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TransaksiActivity.this, "Pesanan dikirim.", Toast.LENGTH_SHORT).show();
                    loadPesananMasuk();
                } else {
                    Toast.makeText(TransaksiActivity.this, pesanError(response), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TransaksiActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prosesPesanan(int id, String estimasiSiap, String estimasiKirim) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.prosesPesanan(id, session.gettoken(), estimasiSiap, estimasiKirim)
                .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TransaksiActivity.this, "Pesanan ditandai dikemas.", Toast.LENGTH_SHORT).show();
                    loadPesananMasuk();
                } else {
                    Toast.makeText(TransaksiActivity.this, pesanError(response), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TransaksiActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private interface TanggalCallback { void onPilih(String tanggal); }

    private void pilihTanggal(String judul, TanggalCallback cb) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this, (view, y, mo, d) -> {
            String tgl = String.format(Locale.US, "%04d-%02d-%02d", y, mo + 1, d);
            cb.onPilih(tgl);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dlg.setTitle(judul);
        dlg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dlg.show();
    }

    private String pesanError(Response<ResponseBody> response) {
        try {
            if (response.errorBody() != null) {
                JSONObject err = new JSONObject(response.errorBody().string());
                return err.optString("message", "Gagal memproses.");
            }
        } catch (Exception ignored) {}
        return "Gagal memproses.";
    }
}
