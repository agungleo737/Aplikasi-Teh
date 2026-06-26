package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    private int jumlahBeli = 1;
    private int stokTersedia = 0;

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
    }
}