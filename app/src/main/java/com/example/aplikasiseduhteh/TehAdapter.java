package com.example.aplikasiseduhteh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TehAdapter extends RecyclerView.Adapter<TehAdapter.TehViewHolder> {
    private List<Teh> listTeh;
    private Context context;

    public TehAdapter(Context context, List<Teh> listTeh) {
        this.context = context;
        this.listTeh = listTeh;
    }

    public void filterList(List<Teh> filteredList) {
        this.listTeh = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TehViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutteh, parent, false);
        return new TehViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TehViewHolder holder, int position) {
        Teh dataTeh = listTeh.get(position);
        holder.tvNama.setText(dataTeh.getNama());
        holder.tvHarga.setText(dataTeh.getHarga());

        // Menampilkan gambar
        holder.ivGambar.setImageResource(dataTeh.getGambarKecil());

        // Tombol favorit
        SharedPreferences sp = context.getSharedPreferences("FavoritTeh", Context.MODE_PRIVATE);
        boolean isFav = sp.getBoolean(dataTeh.getNama(), false);
        holder.btnFavorit.setImageResource(isFav ? R.drawable.paporitfull : R.drawable.paporit1);

        holder.btnFavorit.setOnClickListener(v -> {
            boolean statusNow = sp.getBoolean(dataTeh.getNama(), false);
            boolean newStatus = !statusNow;
            sp.edit().putBoolean(dataTeh.getNama(), newStatus).apply();
            holder.btnFavorit.setImageResource(newStatus ? R.drawable.paporitfull : R.drawable.paporit1);

            // Jika di halaman favorit dan di-unfavorite, hapus dari list
            if (context instanceof paporitactivity && !newStatus) {
                int currentPos = holder.getAdapterPosition();
                if (currentPos != RecyclerView.NO_POSITION) {
                    listTeh.remove(currentPos);
                    notifyItemRemoved(currentPos);
                    notifyItemRangeChanged(currentPos, listTeh.size());

                    if (listTeh.isEmpty()) {
                        ((paporitactivity) context).updateView();
                    }
                }
            }
        });

        // Pindah detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("NAMA_TEH", dataTeh.getNama());
            intent.putExtra("HARGA_TEH", dataTeh.getHarga());
            intent.putExtra("DESKRIPSI_TEH", dataTeh.getDeskripsi());
            intent.putExtra("GAMBAR_FULL", dataTeh.getGambarFull());
            intent.putExtra("GAMBAR_KECIL", dataTeh.getGambarKecil());
            intent.putExtra("STOK_TEH", dataTeh.getStok());
            intent.putExtra("KATEGORI_TEH", dataTeh.getKategori());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listTeh.size();
    }

    public static class TehViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga;
        ImageView ivGambar, btnFavorit;
        public TehViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.nama_teh);
            tvHarga = itemView.findViewById(R.id.harga_teh);
            ivGambar = itemView.findViewById(R.id.img_teh);
            btnFavorit = itemView.findViewById(R.id.tombol_favorit);
        }
    }
}