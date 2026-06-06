package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private int jumlahBeli = 1;
    private int stokTersedia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 1. Binding View
        ImageView imgFull = findViewById(R.id.detail_img);
        TextView tvNama = findViewById(R.id.detail_nama);
        TextView tvHarga = findViewById(R.id.detail_harga);
        TextView tvDeskripsi = findViewById(R.id.detail_deskripsi);
        TextView tvStok = findViewById(R.id.detail_stok);
        ImageView btnMinus = findViewById(R.id.btn_kurang);
        ImageView btnPlus = findViewById(R.id.btn_tambah);
        TextView tvAngka = findViewById(R.id.tv_jumlah);
        Button btnCart = findViewById(R.id.addcart);
        ImageView btnBack = findViewById(R.id.btn_back_detail);

        // 2. Ambil data dari Intent
        final String namaTeh = getIntent().getStringExtra("NAMA_TEH");
        final String hargaTeh = getIntent().getStringExtra("HARGA_TEH");
        final String deskripsiTeh = getIntent().getStringExtra("DESKRIPSI_TEH");
        final String kategoriTeh = getIntent().getStringExtra("KATEGORI_TEH");
        final int gambarFull = getIntent().getIntExtra("GAMBAR_FULL", 0);
        final int gambarKecil = getIntent().getIntExtra("GAMBAR_KECIL", 0);
        stokTersedia = getIntent().getIntExtra("STOK_TEH", 0);

        // Set data ke UI
        tvNama.setText(namaTeh);
        tvHarga.setText(hargaTeh);
        tvDeskripsi.setText(deskripsiTeh);
        imgFull.setImageResource(gambarFull);
        tvStok.setText("Stok: " + stokTersedia);

        // 3. Tombol Back
        btnBack.setOnClickListener(v -> finish());

        // 4. Tombol Tambah Jumlah
        btnPlus.setOnClickListener(v -> {
            if (jumlahBeli < stokTersedia) {
                jumlahBeli++;
                tvAngka.setText(String.valueOf(jumlahBeli));
            } else {
                Toast.makeText(this, "Stock is not enough", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Tombol Kurang Jumlah
        btnMinus.setOnClickListener(v -> {
            if (jumlahBeli > 1) {
                jumlahBeli--;
                tvAngka.setText(String.valueOf(jumlahBeli));
            }
        });

        // 6. Tombol Add to Cart
        btnCart.setOnClickListener(v -> {
            Teh tehBaru = new Teh(namaTeh, hargaTeh, deskripsiTeh, gambarKecil, gambarFull, stokTersedia, kategoriTeh);
            CartManager.keranjangList.add(tehBaru);
            Toast.makeText(this, namaTeh + " successfully added to cart!", Toast.LENGTH_SHORT).show();
        });
    }
}