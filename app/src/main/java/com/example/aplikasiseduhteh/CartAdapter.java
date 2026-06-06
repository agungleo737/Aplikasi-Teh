package com.example.aplikasiseduhteh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Teh> list;
    private OnCartChangeListener listener;
    public interface OnCartChangeListener {
        void onDataChanged();
    }

    public CartAdapter(List<Teh> list, OnCartChangeListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_keranjang, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Teh item = list.get(position);
        holder.nama.setText(item.getNama());
        holder.harga.setText(item.getHarga());
        //gambar tanpa background
        holder.img.setImageResource(item.getGambarKecil());
        holder.btnHapus.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                list.remove(currentPos);
                notifyItemRemoved(currentPos);
                notifyItemRangeChanged(currentPos, list.size());
                if (listener != null) listener.onDataChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nama, harga;
        ImageView img, btnHapus;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txt_nama_cart);
            harga = itemView.findViewById(R.id.txt_harga_cart);
            img = itemView.findViewById(R.id.img_cart);
            btnHapus = itemView.findViewById(R.id.btn_delete_item);
        }
    }
}