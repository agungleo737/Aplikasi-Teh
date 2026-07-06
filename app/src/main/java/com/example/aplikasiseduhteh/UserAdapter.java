package com.example.aplikasiseduhteh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> userList;
    private final List<User> userListFull;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.userListFull = new ArrayList<>(userList);
    }
    public void setData(List<User> data) {
        userListFull.clear();
        userListFull.addAll(data);
        userList.clear();
        userList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.tvNama.setText(user.getNama());
        holder.tvEmail.setText(user.getEmail());
        holder.tvAlamat.setText(
                user.getAlamat() != null && !user.getAlamat().isEmpty()
                        ? user.getAlamat() : "Alamat belum diisi");
        holder.tvBadgeRole.setText(user.getRole());
        if ("admin".equalsIgnoreCase(user.getRole())) {
            holder.tvBadgeRole.setBackgroundResource(R.drawable.bg_badge_penjual);
            holder.tvBadgeRole.setTextColor(context.getResources().getColor(R.color.colorBadgePenjualText));
        } else {
            holder.tvBadgeRole.setBackgroundResource(R.drawable.bg_badge_pembeli);
            holder.tvBadgeRole.setTextColor(context.getResources().getColor(R.color.colorBadgePembeliText));
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void filter(String query) {
        userList.clear();
        if (query == null || query.isEmpty()) {
            userList.addAll(userListFull);
        } else {
            String q = query.toLowerCase().trim();
            for (User u : userListFull) {
                if (u.getNama().toLowerCase().contains(q)
                        || u.getEmail().toLowerCase().contains(q)
                        || (u.getAlamat() != null && u.getAlamat().toLowerCase().contains(q))) {
                    userList.add(u);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void filterByRole(String role) {
        userList.clear();
        if ("Semua".equalsIgnoreCase(role)) {
            userList.addAll(userListFull);
        } else {
            for (User u : userListFull) {
                if (u.getRole().equalsIgnoreCase(role)) {
                    userList.add(u);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvAlamat, tvEmail, tvBadgeRole;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama      = itemView.findViewById(R.id.tvNama);
            tvAlamat    = itemView.findViewById(R.id.tvAlamat);
            tvEmail     = itemView.findViewById(R.id.tvEmail);
            tvBadgeRole = itemView.findViewById(R.id.tvBadgeRole);
        }
    }
}
