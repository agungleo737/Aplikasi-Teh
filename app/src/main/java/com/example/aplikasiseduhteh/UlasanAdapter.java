package com.example.aplikasiseduhteh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UlasanAdapter extends RecyclerView.Adapter<UlasanAdapter.UlasanViewHolder> {
    private final List<UlasanModel> listUlasan;

    public UlasanAdapter(List<UlasanModel> listUlasan) {
        this.listUlasan = listUlasan;
    }

    @NonNull
    @Override
    public UlasanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ulasan, parent, false);
        return new UlasanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UlasanViewHolder holder, int position) {
        UlasanModel item = listUlasan.get(position);
        holder.tvNama.setText(item.getNamaUser());
        holder.tvTanggal.setText(item.getTanggal());
        holder.ratingBar.setRating(item.getRating());
        if (item.getKomentar() == null || item.getKomentar().trim().isEmpty()) {
            holder.tvKomentar.setVisibility(View.GONE);
        } else {
            holder.tvKomentar.setVisibility(View.VISIBLE);
            holder.tvKomentar.setText(item.getKomentar());
        }
    }

    @Override
    public int getItemCount() {
        return listUlasan.size();
    }

    public static class UlasanViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvTanggal, tvKomentar;
        RatingBar ratingBar;

        public UlasanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.ulasan_nama);
            tvTanggal = itemView.findViewById(R.id.ulasan_tanggal);
            tvKomentar = itemView.findViewById(R.id.ulasan_komentar);
            ratingBar = itemView.findViewById(R.id.ulasan_rating);
        }
    }
}
