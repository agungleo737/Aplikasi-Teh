package com.example.aplikasiseduhteh;
import com.bumptech.glide.Glide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

        String hargaclean = item.getHarga().replaceAll("[^0-9]", "");
        int hargasatuan = hargaclean.isEmpty() ? 0 : Integer.parseInt(hargaclean);
        int qty = item.getQtyDibeli();

        holder.harga.setText(item.getHarga());
        holder.tvQty.setText(String.valueOf(qty));

        int subtotal = hargasatuan * qty;
        holder.tvSubtotal.setText("Subtotal: Rp " + String.format("%,d", subtotal).replace(',', '.'));

        String namaFile = item.getGambarNama();
        if (!namaFile.isEmpty() && !namaFile.endsWith(".png") && !namaFile.endsWith(".jpg")) {
            namaFile = namaFile + ".png";
        }
        Glide.with(holder.itemView.getContext())
                .load(ApiClient.gambar_url + namaFile)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.img);

        // Tombol kurang qty
        holder.btnMinus.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_ID) return;
            Teh curr = list.get(pos);
            if (curr.getQtyDibeli() > 1) {
                curr.setQtyDibeli(curr.getQtyDibeli() - 1);
                notifyItemChanged(pos);
                if (listener != null) listener.onDataChanged();
            } else {
                list.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, list.size());
                if (listener != null) listener.onDataChanged();
            }
        });

        // Tombol tambah qty
        holder.btnPlus.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_ID) return;
            Teh curr = list.get(pos);
            if (curr.getQtyDibeli() < curr.getStok()) {
                curr.setQtyDibeli(curr.getQtyDibeli() + 1);
                notifyItemChanged(pos);
                if (listener != null) listener.onDataChanged();
            } else {
                Toast.makeText(v.getContext(), "Stok tidak mencukupi (maks " + curr.getStok() + ")", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol hapus item
        holder.btnHapus.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                list.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, list.size());
                if (listener != null) listener.onDataChanged();
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nama, harga, tvQty, tvSubtotal;
        ImageView img, btnHapus, btnMinus, btnPlus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nama       = itemView.findViewById(R.id.txt_nama_cart);
            harga      = itemView.findViewById(R.id.txt_harga_cart);
            img        = itemView.findViewById(R.id.img_cart);
            btnHapus   = itemView.findViewById(R.id.btn_delete_item);
            tvQty      = itemView.findViewById(R.id.tv_qty_cart);
            tvSubtotal = itemView.findViewById(R.id.tv_subtotal_cart);
            btnMinus   = itemView.findViewById(R.id.btn_minus_cart);
            btnPlus    = itemView.findViewById(R.id.btn_plus_cart);
        }
    }
}