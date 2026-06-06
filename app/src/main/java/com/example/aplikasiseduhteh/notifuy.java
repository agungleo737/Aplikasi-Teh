package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class notifuy extends AppCompatActivity {
    private ImageView btnBack;
    private View layoutKosong;
    private RecyclerView rvNotif;
    private List<notifmodel> listNotif = notifmanager.listNotifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifuy);
        btnBack = findViewById(R.id.btn_back_notif);
        layoutKosong = findViewById(R.id.layout_notif_kosong);
        rvNotif = findViewById(R.id.rv_notifikasi);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        // Cek rvNotif null atau tidak
        if (rvNotif != null) {
            rvNotif.setLayoutManager(new LinearLayoutManager(this));
        }
        checkNotification();
    }

    private void checkNotification() {
        // Pastikan variabel tidak null
        if (layoutKosong == null || rvNotif == null) return;

        if (listNotif == null || listNotif.isEmpty()) {
            layoutKosong.setVisibility(View.VISIBLE);
            rvNotif.setVisibility(View.GONE);
        } else {
            layoutKosong.setVisibility(View.GONE);
            rvNotif.setVisibility(View.VISIBLE);
            // Inisialisasi adapter
            notif adapter = new notif(listNotif);
            rvNotif.setAdapter(adapter);
        }
    }
}