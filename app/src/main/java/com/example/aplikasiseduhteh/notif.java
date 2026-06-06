package com.example.aplikasiseduhteh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class notif extends RecyclerView.Adapter<notif.ViewHolder> {
    private List<notifmodel> notifList;

    public notif(List<notifmodel> notifList) {
        this.notifList = notifList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifikasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        notifmodel dataNotif = notifList.get(position);
        holder.tvJudul.setText(dataNotif.getJudul());
        holder.tvPesan.setText(dataNotif.getPesan());

        holder.btnMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.btnMore);
            popup.getMenu().add("Sematkan");
            popup.getMenu().add("Hapus");

            popup.setOnMenuItemClickListener(item -> {
                int currentPos = holder.getBindingAdapterPosition();
                if (currentPos == RecyclerView.NO_POSITION) return false;

                if (item.getTitle().equals("Hapus")) {
                    notifList.remove(currentPos);
                    notifyItemRemoved(currentPos);
                } else if (item.getTitle().equals("Sematkan")) {
                    notifmodel pinned = notifList.remove(currentPos);
                    notifList.add(0, pinned);
                    notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "Berhasil disematkan", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return notifList != null ? notifList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvPesan;
        ImageView ivIcon;
        ImageButton btnMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_notif_judul);
            tvPesan = itemView.findViewById(R.id.tv_notif_pesan);
            ivIcon = itemView.findViewById(R.id.iv_notif_icon);
            btnMore = itemView.findViewById(R.id.btn_more);
        }
    }
}