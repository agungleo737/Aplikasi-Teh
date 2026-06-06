package com.example.aplikasiseduhteh;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class manage_profile extends AppCompatActivity {

    private EditText etTanggalLahir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        // 1. Tombol Back
        findViewById(R.id.btn_back_account).setOnClickListener(v -> {
            finish();
        });

        // 2. EditText Tanggal Lahir
        etTanggalLahir = findViewById(R.id.et_tanggal_lahir);
        etTanggalLahir.setOnClickListener(v -> showDatePicker());

        // 3. Tombol Update
        findViewById(R.id.btn_update_profile).setOnClickListener(v -> {
            Toast.makeText(this, "Data Berhasil Diperbarui!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    etTanggalLahir.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}