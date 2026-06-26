package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputProdukActivity extends AppCompatActivity {

    private EditText etNama, etKategori, etHarga, etStok, etDeskripsi;
    private ImageView btnBackInput, imgPreviewThumb, imgPreviewFull;
    private Button btnPilihThumb, btnPilihFull, btnSimpan;
    private Uri uriThumb = null;
    private Uri uriFull = null;

    // Mode edit
    private boolean editMode = false;
    private int produkId = -1;
    private SessionManager session;

    private final ActivityResultLauncher<Intent> launcherThumb =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    uriThumb = result.getData().getData();
                    imgPreviewThumb.setImageURI(uriThumb);
                    imgPreviewThumb.setVisibility(android.view.View.VISIBLE);
                }
            });

    private final ActivityResultLauncher<Intent> launcherFull =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    uriFull = result.getData().getData();
                    imgPreviewFull.setImageURI(uriFull);
                    imgPreviewFull.setVisibility(android.view.View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_produk);
        etNama        = findViewById(R.id.et_nama_teh);
        etKategori    = findViewById(R.id.et_kategori_teh);
        etHarga       = findViewById(R.id.et_harga_teh);
        etStok        = findViewById(R.id.et_stok_teh);
        etDeskripsi   = findViewById(R.id.et_deskripsi_teh);
        btnSimpan     = findViewById(R.id.btn_simpan_teh);
        btnBackInput  = findViewById(R.id.btn_back_input);
        btnPilihThumb = findViewById(R.id.btn_pilih_thumbnail);
        btnPilihFull  = findViewById(R.id.btn_pilih_full);
        imgPreviewThumb = findViewById(R.id.img_preview_thumbnail);
        imgPreviewFull  = findViewById(R.id.img_preview_full);

        session = new SessionManager(this);
        if ("edit".equals(getIntent().getStringExtra("mode"))) {
            editMode = true;
            produkId = getIntent().getIntExtra("id", -1);
            etNama.setText(getIntent().getStringExtra("nama"));
            etKategori.setText(getIntent().getStringExtra("kategori"));
            etHarga.setText(getIntent().getStringExtra("harga"));
            etStok.setText(getIntent().getStringExtra("stok"));
            etDeskripsi.setText(getIntent().getStringExtra("deskripsi"));
            btnSimpan.setText("Update Produk");
        }

        btnBackInput.setOnClickListener(v -> finish());

        btnPilihThumb.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcherThumb.launch(intent);
        });

        btnPilihFull.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcherFull.launch(intent);
        });
        btnSimpan.setOnClickListener(v -> simpanProduk());
    }

    private File uriToFile(Uri uri, String prefix) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile(prefix, ".jpg", getCacheDir());
        FileOutputStream fos = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }
        fos.close();
        inputStream.close();
        return tempFile;
    }

    private void simpanProduk() {
        String nama      = etNama.getText().toString().trim();
        String kategori  = etKategori.getText().toString().trim();
        String hargaStr  = etHarga.getText().toString().trim();
        String stokStr   = etStok.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();

        if (nama.isEmpty() || kategori.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        RequestBody rbNama      = RequestBody.create(MediaType.parse("text/plain"), nama);
        RequestBody rbKategori  = RequestBody.create(MediaType.parse("text/plain"), kategori);
        RequestBody rbHarga     = RequestBody.create(MediaType.parse("text/plain"), hargaStr);
        RequestBody rbStok      = RequestBody.create(MediaType.parse("text/plain"), stokStr);
        RequestBody rbDeskripsi = RequestBody.create(MediaType.parse("text/plain"), deskripsi);

        // Mode edit
        if (editMode) {
            updateProduk(apiService, rbNama, rbKategori, rbHarga, rbStok, rbDeskripsi);
            return;
        }

        Call<Void> call;

        if (uriThumb != null && uriFull != null) {
            try {
                File fileThumb = uriToFile(uriThumb, "thumb");
                File fileFull  = uriToFile(uriFull, "full");

                MultipartBody.Part partThumb = MultipartBody.Part.createFormData(
                        "gambar", fileThumb.getName(),
                        RequestBody.create(MediaType.parse("image/*"), fileThumb));

                MultipartBody.Part partFull = MultipartBody.Part.createFormData(
                        "gambar_full", fileFull.getName(),
                        RequestBody.create(MediaType.parse("image/*"), fileFull));

                call = apiService.tambahProduk(session.gettoken(), partThumb, partFull, rbNama, rbKategori, rbHarga, rbStok, rbDeskripsi);
            } catch (Exception e) {
                Toast.makeText(this, "Gagal proses foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            call = apiService.tanpagambar(session.gettoken(), rbNama, rbKategori, rbHarga, rbStok, rbDeskripsi);
        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InputProdukActivity.this, "Produk berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(InputProdukActivity.this, "Gagal simpan ke server!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(InputProdukActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduk(ApiService apiService, RequestBody rbNama, RequestBody rbKategori,
                              RequestBody rbHarga, RequestBody rbStok, RequestBody rbDeskripsi) {
        String token = session.gettoken();
        Call<ResponseBody> call;

        if (uriThumb != null && uriFull != null) {
            try {
                File fileThumb = uriToFile(uriThumb, "thumb");
                File fileFull  = uriToFile(uriFull, "full");

                MultipartBody.Part partThumb = MultipartBody.Part.createFormData(
                        "gambar", fileThumb.getName(),
                        RequestBody.create(MediaType.parse("image/*"), fileThumb));

                MultipartBody.Part partFull = MultipartBody.Part.createFormData(
                        "gambar_full", fileFull.getName(),
                        RequestBody.create(MediaType.parse("image/*"), fileFull));

                call = apiService.updateProduk(produkId, token, partThumb, partFull,
                        rbNama, rbKategori, rbHarga, rbStok, rbDeskripsi);
            } catch (Exception e) {
                Toast.makeText(this, "Gagal proses foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            call = apiService.updateProdukTanpaGambar(produkId, token,
                    rbNama, rbKategori, rbHarga, rbStok, rbDeskripsi);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InputProdukActivity.this, "Produk berhasil diupdate!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(InputProdukActivity.this, "Gagal update (cek login/akses).", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(InputProdukActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}