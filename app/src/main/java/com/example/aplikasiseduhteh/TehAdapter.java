package com.example.aplikasiseduhteh;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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
        String namaFile = dataTeh.getGambarNama();
        if (!namaFile.isEmpty() && !namaFile.endsWith(".png") && !namaFile.endsWith(".jpg")) {
            namaFile = namaFile + ".png";
        }
        String urlGambar = ApiClient.gambar_url + namaFile;
        Glide.with(context)
                .load(urlGambar)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.ivGambar);

        // Tombol favorit
        boolean isFav = FavoritManager.isFavorit(dataTeh.getid());
        holder.btnFavorit.setImageResource(isFav ? R.drawable.paporitfull : R.drawable.paporit1);

        holder.btnFavorit.setOnClickListener(v -> {
            boolean newStatus = !FavoritManager.isFavorit(dataTeh.getid());
            FavoritManager.toggle(context, dataTeh.getid(), newStatus);
            holder.btnFavorit.setImageResource(newStatus ? R.drawable.paporitfull : R.drawable.paporit1);

            if (context instanceof paporitactivity && !newStatus) {
                int currentPos = holder.getAdapterPosition();
                if (currentPos != RecyclerView.NO_POSITION) {
                    listTeh.remove(currentPos);
                    notifyItemRemoved(currentPos);
                    notifyItemRangeChanged(currentPos, listTeh.size());
                    ((paporitactivity) context).updateView();
                }
            }
        });

        // Pindah detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("ID_TEH", dataTeh.getid());
            intent.putExtra("NAMA_TEH", dataTeh.getNama());
            intent.putExtra("HARGA_TEH", dataTeh.getHarga());
            intent.putExtra("DESKRIPSI_TEH", dataTeh.getDeskripsi());
            intent.putExtra("GAMBAR_NAMA", dataTeh.getGambarNama());
            intent.putExtra("GAMBAR_FULL_NAMA", dataTeh.getGambarFullNama());
            intent.putExtra("STOK_TEH", dataTeh.getStok());
            intent.putExtra("KATEGORI_TEH", dataTeh.getKategori());
            intent.putExtra("SELLER_ID", dataTeh.getSellerId());
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