package com.example.aplikasiseduhteh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.VH> {

    private final List<TransaksiModel> list;

    public TransaksiAdapter(List<TransaksiModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaksi, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TransaksiModel m = list.get(position);
        h.invoice.setText(m.getInvoice());
        h.produk.setText(m.getProduk());
        h.harga.setText(m.getHarga());
        h.status.setText(m.getStatus());
        h.tanggal.setText(m.getTanggal());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView invoice, produk, harga, status, tanggal;
        VH(View v) {
            super(v);
            invoice = v.findViewById(R.id.txtInvoice);
            produk  = v.findViewById(R.id.txtProduk);
            harga   = v.findViewById(R.id.txtHarga);
            status  = v.findViewById(R.id.txtStatus);
            tanggal = v.findViewById(R.id.txtTanggal);
        }
    }
}
