package com.example.aplikasiseduhteh;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class manage_profile extends AppCompatActivity {

    private EditText etNamaLengkap, etPanggilan, etTanggalLahir, etAlamat;
    private Button btnUpdate;
    private SessionManager session;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        session    = new SessionManager(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        etNamaLengkap  = findViewById(R.id.et_nama_lengkap);
        etPanggilan    = findViewById(R.id.et_panggilan);
        etTanggalLahir = findViewById(R.id.et_tanggal_lahir);
        etAlamat       = findViewById(R.id.et_alamat);
        btnUpdate      = findViewById(R.id.btn_update_profile);

        // Tombol Back
        findViewById(R.id.btn_back_account).setOnClickListener(v -> finish());

        // Pilih tanggal lahir
        etTanggalLahir.setOnClickListener(v -> showDatePicker());

        // Tombol Update
        btnUpdate.setOnClickListener(v -> updateProfile());
        loadProfile();
    }

    private void loadProfile() {
        apiService.getProfile(session.gettoken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject root = new JSONObject(response.body().string());
                        JSONObject user = root.getJSONObject("user");
                        etNamaLengkap.setText(user.optString("name", ""));
                        etPanggilan.setText(emptyIfNull(user.optString("panggilan", "")));
                        etTanggalLahir.setText(emptyIfNull(user.optString("tanggal_lahir", "")));
                        etAlamat.setText(emptyIfNull(user.optString("alamat", "")));
                    } catch (Exception e) {
                        Toast.makeText(manage_profile.this,
                                "Gagal baca data profil.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(manage_profile.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile() {
        String nama      = etNamaLengkap.getText().toString().trim();
        String panggilan = etPanggilan.getText().toString().trim();
        String tglLahir  = etTanggalLahir.getText().toString().trim();
        String alamat    = etAlamat.getText().toString().trim();

        if (nama.isEmpty()) {
            etNamaLengkap.setError("Nama tidak boleh kosong");
            return;
        }

        btnUpdate.setEnabled(false);
        apiService.updateProfile(session.gettoken(), nama, panggilan, tglLahir, alamat)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        btnUpdate.setEnabled(true);
                        if (response.isSuccessful()) {
                            session.setname(nama);
                            notifmanager.tambahnotif(manage_profile.this,
                                    "Profil Diperbarui",
                                    "Data profil kamu berhasil diperbarui.");
                            Toast.makeText(manage_profile.this,
                                    "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(manage_profile.this,
                                    "Gagal update (cek data/login).", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        btnUpdate.setEnabled(true);
                        Toast.makeText(manage_profile.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String emptyIfNull(String s) {
        return (s == null || s.equals("null")) ? "" : s;
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day   = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.US, "%04d-%02d-%02d",
                            year1, monthOfYear + 1, dayOfMonth);
                    etTanggalLahir.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
