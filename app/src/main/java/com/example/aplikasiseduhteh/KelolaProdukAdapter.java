package com.example.aplikasiseduhteh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class KelolaProdukAdapter extends RecyclerView.Adapter<KelolaProdukAdapter.ViewHolder> {

    public interface OnAksiListener {
        void onEdit(TehModel produk);
        void onHapus(TehModel produk);
    }

    private final Context context;
    private final List<TehModel> listData;
    private final OnAksiListener listener;

    public KelolaProdukAdapter(Context context, List<TehModel> listData, OnAksiListener listener) {
        this.context = context;
        this.listData = listData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kelola_produk, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TehModel produk = listData.get(position);

        holder.tvNama.setText(produk.getNamaTeh());
        holder.tvKategori.setText(produk.getKategori());
        holder.tvHarga.setText("Rp " + (long) produk.getHarga());
        holder.tvStok.setText("Stok: " + produk.getStok());

        if (produk.getGambar() != null && !produk.getGambar().isEmpty()) {
            Glide.with(context)
                    .load(ApiClient.gambar_url + produk.getGambar())
                    .placeholder(R.drawable.teh1)
                    .into(holder.imgKelola);
        } else {
            holder.imgKelola.setImageResource(R.drawable.teh1);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(produk);
        });
        holder.btnHapus.setOnClickListener(v -> {
            if (listener != null) listener.onHapus(produk);
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgKelola;
        TextView tvNama, tvKategori, tvHarga, tvStok;
        ImageButton btnEdit, btnHapus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgKelola  = itemView.findViewById(R.id.img_kelola);
            tvNama     = itemView.findViewById(R.id.tv_nama);
            tvKategori = itemView.findViewById(R.id.tv_kategori);
            tvHarga    = itemView.findViewById(R.id.tv_harga);
            tvStok     = itemView.findViewById(R.id.tv_stok);
            btnEdit    = itemView.findViewById(R.id.btn_edit);
            btnHapus   = itemView.findViewById(R.id.btn_hapus);
        }
    }
}
