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
    private List<notifmodel> listNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifuy);

        btnBack     = findViewById(R.id.btn_back_notif);
        layoutKosong = findViewById(R.id.layout_notif_kosong);
        rvNotif     = findViewById(R.id.rv_notifikasi);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (rvNotif != null) {
            rvNotif.setLayoutManager(new LinearLayoutManager(this));
        }

        listNotif = notifmanager.getList(this);
        checkNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listNotif = notifmanager.getList(this);
        checkNotification();
    }

    private void checkNotification() {
        if (layoutKosong == null || rvNotif == null) return;

        if (listNotif == null || listNotif.isEmpty()) {
            layoutKosong.setVisibility(View.VISIBLE);
            rvNotif.setVisibility(View.GONE);
        } else {
            layoutKosong.setVisibility(View.GONE);
            rvNotif.setVisibility(View.VISIBLE);
            notif adapter = new notif(this, listNotif, () -> {
                layoutKosong.setVisibility(View.VISIBLE);
                rvNotif.setVisibility(View.GONE);
            });
            rvNotif.setAdapter(adapter);
        }
    }
}