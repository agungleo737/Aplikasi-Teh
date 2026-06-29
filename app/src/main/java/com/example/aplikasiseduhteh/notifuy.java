package com.example.aplikasiseduhteh;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class notifuy extends AppCompatActivity {
    private ImageView btnBack;
    private View layoutKosong;
    private RecyclerView rvNotif;
    private List<notifmodel> listNotif = new ArrayList<>();

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

        muatNotifServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        muatNotifServer();
    }
    private void muatNotifServer() {
        SessionManager session = new SessionManager(this);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getNotifikasiServer(session.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                List<notifmodel> hasil = new ArrayList<>();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONObject root = new JSONObject(response.body().string());
                        JSONObject data = root.optJSONObject("data");
                        JSONArray arr = data != null ? data.optJSONArray("notifikasi") : null;
                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject n = arr.getJSONObject(i);
                                hasil.add(new notifmodel(
                                        n.optString("judul"),
                                        n.optString("pesan")));
                            }
                        }
                        listNotif = hasil;
                        // Tandai sudah dibaca
                        api.bacaSemuaNotif(session.gettoken()).enqueue(new Callback<ResponseBody>() {
                            @Override public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {}
                            @Override public void onFailure(Call<ResponseBody> c, Throwable t) {}
                        });
                    } else {
                        listNotif = notifmanager.getList(notifuy.this);
                    }
                } catch (Exception e) {
                    listNotif = notifmanager.getList(notifuy.this);
                }
                checkNotification();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listNotif = notifmanager.getList(notifuy.this);
                checkNotification();
            }
        });
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
