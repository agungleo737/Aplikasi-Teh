package com.example.aplikasiseduhteh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.VH> {

    public interface AksiListener {
        void onProses(TransaksiModel m);
        void onKirim(TransaksiModel m);
    }

    private final List<TransaksiModel> list;
    private final AksiListener listener;

    public TransaksiAdapter(List<TransaksiModel> list, AksiListener listener) {
        this.list = list;
        this.listener = listener;
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
        h.pembeli.setText("Pembeli: " + m.getNamaPembeli());
        h.harga.setText(m.getHarga());
        h.status.setText("Status: " + labelStatus(m.getStatus()));
        h.tanggal.setText(m.getTanggal());

        // Estimasi
        if (m.getEstimasiSiap() != null && !m.getEstimasiSiap().isEmpty()) {
            h.estimasi.setVisibility(View.VISIBLE);
            h.estimasi.setText("Estimasi siap " + m.getEstimasiSiap() + ", kirim " + m.getEstimasiKirim());
        } else {
            h.estimasi.setVisibility(View.GONE);
        }
        String status = m.getStatus();
        if ("menunggu".equals(status)) {
            h.btnAksi.setVisibility(View.VISIBLE);
            h.btnAksi.setText("Verifikasi & Kemas");
            h.btnAksi.setOnClickListener(v -> listener.onProses(m));
        } else if ("dikemas".equals(status)) {
            h.btnAksi.setVisibility(View.VISIBLE);
            h.btnAksi.setText("Kirim Sekarang");
            h.btnAksi.setOnClickListener(v -> listener.onKirim(m));
        } else {
            h.btnAksi.setVisibility(View.GONE);
            h.btnAksi.setOnClickListener(null);
        }
    }

    private String labelStatus(String s) {
        if (s == null) return "-";
        switch (s) {
            case "menunggu": return "Menunggu diproses";
            case "dikemas":  return "Sedang dikemas";
            case "dikirim":  return "Dalam perjalanan";
            case "selesai":  return "Selesai";
            default:         return s;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView invoice, produk, pembeli, harga, status, estimasi, tanggal;
        Button btnAksi;
        VH(View v) {
            super(v);
            invoice = v.findViewById(R.id.txtInvoice);
            produk  = v.findViewById(R.id.txtProduk);
            pembeli = v.findViewById(R.id.txtPembeli);
            harga   = v.findViewById(R.id.txtHarga);
            status  = v.findViewById(R.id.txtStatus);
            estimasi = v.findViewById(R.id.txtEstimasi);
            tanggal = v.findViewById(R.id.txtTanggal);
            btnAksi = v.findViewById(R.id.btnAksi);
        }
    }
}
