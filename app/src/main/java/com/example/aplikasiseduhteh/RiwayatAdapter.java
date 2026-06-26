package com.example.aplikasiseduhteh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    private final Context context;
    private final List<RiwayatModel> listData;

    public RiwayatAdapter(Context context, List<RiwayatModel> listData) {
        this.context  = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RiwayatModel item = listData.get(position);

        holder.tvNama.setText(item.getNamaProduk());
        holder.tvTanggal.setText(item.getTanggal());
        holder.tvRincian.setText(item.getJumlahBeli() + " x Rp " + item.getHargaSatuan());
        holder.tvTotal.setText("Rp " + item.getTotalHarga());

        if (item.getGambar() != null && !item.getGambar().isEmpty()) {
            Glide.with(context)
                    .load(ApiClient.gambar_url + item.getGambar())
                    .placeholder(R.drawable.teh1)
                    .into(holder.imgProduk);
        } else {
            holder.imgProduk.setImageResource(R.drawable.teh1);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduk;
        TextView tvNama, tvTanggal, tvRincian, tvTotal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduk = itemView.findViewById(R.id.img_riwayat);
            tvNama    = itemView.findViewById(R.id.tv_riwayat_nama);
            tvTanggal = itemView.findViewById(R.id.tv_riwayat_tanggal);
            tvRincian = itemView.findViewById(R.id.tv_riwayat_rincian);
            tvTotal   = itemView.findViewById(R.id.tv_riwayat_total);
        }
    }
}
