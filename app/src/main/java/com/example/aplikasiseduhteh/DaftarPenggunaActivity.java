package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class DaftarPenggunaActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private UserAdapter userAdapter;
    private EditText etSearch;
    private TextView tabSemua, tabPembeli, tabPenjual;
    private final List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pengguna);

        rvUsers    = findViewById(R.id.rvUsers);
        etSearch   = findViewById(R.id.etSearch);
        tabSemua   = findViewById(R.id.tabSemua);
        tabPembeli = findViewById(R.id.tabPembeli);
        tabPenjual = findViewById(R.id.tabPenjual);

        View btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) btnMenu.setOnClickListener(v -> finish());

        // Bottom nav admin
        View navLaporan = findViewById(R.id.navLaporan);
        if (navLaporan != null) navLaporan.setOnClickListener(v ->
                startActivity(new Intent(this, LaporanActivity.class)));

        View navPengaturan = findViewById(R.id.navPengaturan);
        if (navPengaturan != null) navPengaturan.setOnClickListener(v ->
                startActivity(new Intent(this, SettingActivity.class)));

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, users);
        rvUsers.setAdapter(userAdapter);

        setupSearch();
        setupTabs();
        loadUsers();
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                userAdapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupTabs() {
        tabSemua.setOnClickListener(v -> setActiveTab(tabSemua, "Semua"));
        tabPembeli.setOnClickListener(v -> setActiveTab(tabPembeli, "user"));
        tabPenjual.setOnClickListener(v -> setActiveTab(tabPenjual, "admin"));
    }

    private void setActiveTab(TextView active, String roleFilter) {
        TextView[] all = {tabSemua, tabPembeli, tabPenjual};
        for (TextView t : all) {
            t.setBackgroundResource(R.drawable.bg_tab_unselected);
            t.setTextColor(getResources().getColor(R.color.colorTextSecondary));
            t.setTypeface(null, Typeface.NORMAL);
        }
        active.setBackgroundResource(R.drawable.bg_tab_selected);
        active.setTextColor(getResources().getColor(android.R.color.white));
        active.setTypeface(null, Typeface.BOLD);

        userAdapter.filterByRole(roleFilter);
    }

    private void loadUsers() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        SessionManager session = new SessionManager(this);

        api.adminDaftarUser(session.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(DaftarPenggunaActivity.this,
                            "Gagal memuat pengguna (khusus admin).", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray data  = root.optJSONArray("data");
                    users.clear();
                    if (data != null) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject o = data.getJSONObject(i);
                            users.add(new User(
                                    o.optString("name"),
                                    o.optString("email"),
                                    o.isNull("alamat") ? null : o.optString("alamat"),
                                    o.optString("role", "user")
                            ));
                        }
                    }
                    userAdapter.setData(new ArrayList<>(users));
                } catch (Exception e) {
                    Toast.makeText(DaftarPenggunaActivity.this,
                            "Format data salah: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DaftarPenggunaActivity.this,
                        "Gagal konek: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
