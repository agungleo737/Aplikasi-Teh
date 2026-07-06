package com.example.aplikasiseduhteh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    private final Context context;
    private final List<RiwayatModel> listData;
    private final Runnable onRefresh;

    public RiwayatAdapter(Context context, List<RiwayatModel> listData, Runnable onRefresh) {
        this.context  = context;
        this.listData = listData;
        this.onRefresh = onRefresh;
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
        holder.tvStatus.setText("Status: " + labelStatus(item.getStatus()));

        // Estimasi
        if (item.getEstimasiSiap() != null && !item.getEstimasiSiap().isEmpty()) {
            holder.tvEstimasi.setVisibility(View.VISIBLE);
            holder.tvEstimasi.setText("Estimasi siap " + item.getEstimasiSiap()
                    + ", kirim " + item.getEstimasiKirim());
        } else {
            holder.tvEstimasi.setVisibility(View.GONE);
        }
        if ("dikirim".equals(item.getStatus())) {
            holder.btnTerima.setVisibility(View.VISIBLE);
            holder.btnTerima.setOnClickListener(v -> terimaPesanan(item.getId()));
        } else {
            holder.btnTerima.setVisibility(View.GONE);
            holder.btnTerima.setOnClickListener(null);
        }

        if (item.getGambar() != null && !item.getGambar().isEmpty()) {
            Glide.with(context)
                    .load(ApiClient.gambar_url + item.getGambar())
                    .placeholder(R.drawable.teh1)
                    .into(holder.imgProduk);
        } else {
            holder.imgProduk.setImageResource(R.drawable.teh1);
        }
    }

    private void terimaPesanan(int id) {
        SessionManager session = new SessionManager(context);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.terimaPesanan(id, session.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Pesanan selesai. Terima kasih!", Toast.LENGTH_SHORT).show();
                    if (onRefresh != null) onRefresh.run();
                } else {
                    Toast.makeText(context, "Gagal konfirmasi pesanan.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String labelStatus(String s) {
        if (s == null) return "-";
        switch (s) {
            case "menunggu": return "Menunggu diproses penjual";
            case "dikemas":  return "Sedang dikemas";
            case "dikirim":  return "Dalam perjalanan";
            case "selesai":  return "Selesai";
            default:         return s;
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduk;
        TextView tvNama, tvTanggal, tvRincian, tvTotal, tvStatus, tvEstimasi;
        Button btnTerima;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduk = itemView.findViewById(R.id.img_riwayat);
            tvNama    = itemView.findViewById(R.id.tv_riwayat_nama);
            tvTanggal = itemView.findViewById(R.id.tv_riwayat_tanggal);
            tvRincian = itemView.findViewById(R.id.tv_riwayat_rincian);
            tvTotal   = itemView.findViewById(R.id.tv_riwayat_total);
            tvStatus  = itemView.findViewById(R.id.tv_riwayat_status);
            tvEstimasi = itemView.findViewById(R.id.tv_riwayat_estimasi);
            btnTerima = itemView.findViewById(R.id.btn_terima);
        }
    }
}
