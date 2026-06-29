package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private int jumlahBeli = 1;
    private int stokTersedia = 0;

    private final List<UlasanModel> listUlasan = new ArrayList<>();
    private UlasanAdapter ulasanAdapter;
    private RatingBar detailRataBar, inputRating;
    private TextView detailRataTeks, tvBelumUlasan;
    private RecyclerView rvUlasan;
    private int idTehAktif = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imgFull     = findViewById(R.id.detail_img);
        TextView tvNama       = findViewById(R.id.detail_nama);
        TextView tvHarga      = findViewById(R.id.detail_harga);
        TextView tvDeskripsi  = findViewById(R.id.detail_deskripsi);
        TextView tvStok       = findViewById(R.id.detail_stok);
        ImageView btnMinus    = findViewById(R.id.btn_kurang);
        ImageView btnPlus     = findViewById(R.id.btn_tambah);
        TextView tvAngka      = findViewById(R.id.tv_jumlah);
        Button btnCart        = findViewById(R.id.addcart);
        ImageView btnBack     = findViewById(R.id.btn_back_detail);

        final int idTeh           = getIntent().getIntExtra("ID_TEH", 0);
        final String namaTeh      = getIntent().getStringExtra("NAMA_TEH");
        final String hargaTeh     = getIntent().getStringExtra("HARGA_TEH");
        final String deskripsiTeh = getIntent().getStringExtra("DESKRIPSI_TEH");
        final String kategoriTeh  = getIntent().getStringExtra("KATEGORI_TEH");
        final String gambarNama     = getIntent().getStringExtra("GAMBAR_NAMA");
        final String gambarFullNama = getIntent().getStringExtra("GAMBAR_FULL_NAMA");
        stokTersedia = getIntent().getIntExtra("STOK_TEH", 0);
        final int sellerId = getIntent().getIntExtra("SELLER_ID", 0);

        // Penjual tidak boleh membeli produknya
        SessionManager session = new SessionManager(this);
        boolean produkMilikSendiri = sellerId != 0 && sellerId == session.getuserid();
        if (produkMilikSendiri) {
            btnMinus.setVisibility(android.view.View.GONE);
            btnPlus.setVisibility(android.view.View.GONE);
            tvAngka.setVisibility(android.view.View.GONE);
            btnCart.setEnabled(false);
            btnCart.setText("Ini produk milikmu");
        }

        tvNama.setText(namaTeh);
        tvHarga.setText(hargaTeh);
        tvDeskripsi.setText(deskripsiTeh);
        tvStok.setText("Stok: " + stokTersedia);
        tvAngka.setText(String.valueOf(jumlahBeli));
        String namaFull = (gambarFullNama != null && !gambarFullNama.isEmpty()) ? gambarFullNama : gambarNama;
        if (namaFull != null && !namaFull.isEmpty() && !namaFull.endsWith(".png") && !namaFull.endsWith(".jpg")) {
            namaFull = namaFull + ".png";
        }
        String urlGambar = ApiClient.gambar_url + namaFull;

        Glide.with(this)
                .load(urlGambar)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imgFull);

        btnBack.setOnClickListener(v -> finish());

        btnPlus.setOnClickListener(v -> {
            int qtyDiKeranjang = 0;
            for (Teh item : CartManager.keranjangList) {
                if (item.getNama().equalsIgnoreCase(namaTeh)) {
                    qtyDiKeranjang = item.getQtyDibeli();
                    break;
                }
            }
            if (jumlahBeli + qtyDiKeranjang < stokTersedia) {
                jumlahBeli++;
                tvAngka.setText(String.valueOf(jumlahBeli));
            } else {
                Toast.makeText(this, "Stok tidak mencukupi (maks " + stokTersedia + ")", Toast.LENGTH_SHORT).show();
            }
        });

        btnMinus.setOnClickListener(v -> {
            if (jumlahBeli > 1) {
                jumlahBeli--;
                tvAngka.setText(String.valueOf(jumlahBeli));
            }
        });

        btnCart.setOnClickListener(v -> {
            if (stokTersedia <= 0) {
                Toast.makeText(this, "Stok habis!", Toast.LENGTH_SHORT).show();
                return;
            }
            int qtyDiKeranjang = 0;
            for (Teh item : CartManager.keranjangList) {
                if (item.getNama().equalsIgnoreCase(namaTeh)) {
                    qtyDiKeranjang = item.getQtyDibeli();
                    break;
                }
            }
            if (qtyDiKeranjang + jumlahBeli > stokTersedia) {
                int sisaBisa = stokTersedia - qtyDiKeranjang;
                if (sisaBisa <= 0) {
                    Toast.makeText(this, "Stok habis", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Stok tidak cukup, sisa " + sisaBisa + " lagi.", Toast.LENGTH_LONG).show();
                }
                return;
            }
            Teh tehBaru = new Teh(idTeh, namaTeh, hargaTeh, deskripsiTeh,
                    gambarNama != null ? gambarNama : "",
                    gambarFullNama != null ? gambarFullNama : "",
                    stokTersedia, kategoriTeh);
            boolean berhasil = CartManager.tambahproduk(tehBaru, jumlahBeli, stokTersedia);
            if (berhasil) {
                Toast.makeText(this, namaTeh + " (" + jumlahBeli + " pcs) ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Stok tidak mencukupi!", Toast.LENGTH_SHORT).show();
            }
        });

        //ulasan
        idTehAktif    = idTeh;
        detailRataBar = findViewById(R.id.detail_rata_bar);
        detailRataTeks = findViewById(R.id.detail_rata_teks);
        tvBelumUlasan = findViewById(R.id.tv_belum_ulasan);
        inputRating   = findViewById(R.id.input_rating);
        rvUlasan      = findViewById(R.id.rv_ulasan);
        EditText inputKomentar = findViewById(R.id.input_komentar);
        Button btnKirimUlasan  = findViewById(R.id.btn_kirim_ulasan);

        ulasanAdapter = new UlasanAdapter(listUlasan);
        rvUlasan.setLayoutManager(new LinearLayoutManager(this));
        rvUlasan.setAdapter(ulasanAdapter);

        muatUlasan();

        btnKirimUlasan.setOnClickListener(v -> {
            int rating = (int) inputRating.getRating();
            String komentar = inputKomentar.getText().toString().trim();
            if (rating < 1) {
                Toast.makeText(this, "Pilih rating dulu (minimal 1 bintang).", Toast.LENGTH_SHORT).show();
                return;
            }
            kirimUlasan(rating, komentar, inputKomentar);
        });
    }

    private void muatUlasan() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getUlasan(idTehAktif).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONObject obj = new JSONObject(response.body().string());
                        JSONObject data = obj.optJSONObject("data");
                        if (data == null) return;

                        double rata = data.optDouble("rata_rata", 0);
                        int jumlah  = data.optInt("jumlah", 0);
                        detailRataBar.setRating((float) rata);
                        detailRataTeks.setText(String.format("%.1f (%d ulasan)", rata, jumlah));

                        listUlasan.clear();
                        JSONArray arr = data.optJSONArray("ulasan");
                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject u = arr.getJSONObject(i);
                                listUlasan.add(new UlasanModel(
                                        u.optString("nama_user", "-"),
                                        (float) u.optDouble("rating", 0),
                                        u.optString("komentar", ""),
                                        u.optString("tanggal", "")
                                ));
                            }
                        }
                        ulasanAdapter.notifyDataSetChanged();
                        tvBelumUlasan.setVisibility(listUlasan.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
                    }
                } catch (Exception e) {
                    Toast.makeText(DetailActivity.this, "Gagal memuat ulasan.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void kirimUlasan(int rating, String komentar, EditText inputKomentar) {
        SessionManager session = new SessionManager(this);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.kirimUlasan(idTehAktif, session.gettoken(), rating, komentar).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Ulasan berhasil dikirim!", Toast.LENGTH_SHORT).show();
                    inputKomentar.setText("");
                    muatUlasan();
                } else {
                    String pesan = "Gagal mengirim ulasan.";
                    try {
                        if (response.errorBody() != null) {
                            JSONObject err = new JSONObject(response.errorBody().string());
                            pesan = err.optString("message", pesan);
                        }
                    } catch (Exception ignored) {}
                    Toast.makeText(DetailActivity.this, pesan, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
