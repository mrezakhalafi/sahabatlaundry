package com.hovercout.sahabatlaundry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.api.MitraApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahLayanan extends AppCompatActivity {

    ImageView btnBack;
    EditText etNamaPakaian, etHargaPakaian;
    Button btnTambah, btnUpload;
    ProgressDialog loading;
    Session session;
    Bitmap bitmap;
    Uri path;
    ImageView ivFoto;
    final int REQUEST_GALLERY = 9544;
    String validasi = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_layanan);

        etNamaPakaian = findViewById(R.id.etNamaLayanan);
        etHargaPakaian = findViewById(R.id.etHargaLayanan);
        btnTambah = findViewById(R.id.btnTambah);
        ivFoto = findViewById(R.id.ivFoto);
        btnUpload = findViewById(R.id.btnUpload);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Open Gallery"),REQUEST_GALLERY);
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new Session(TambahLayanan.this);
                String id = session.getId();
                String namapakaian = etNamaPakaian.getText().toString();
                String hargapakaian = etHargaPakaian.getText().toString();

                RequestBody idBaru = RequestBody.create(MediaType.parse("text/plain"), id);
                RequestBody namaBaru = RequestBody.create(MediaType.parse("text/plain"), namapakaian);
                RequestBody hargaBaru = RequestBody.create(MediaType.parse("text/plain"), hargapakaian);

                if (namapakaian.isEmpty()) {
                    etNamaPakaian.setError("Nama harus diisi!");
                }
                if (hargapakaian.isEmpty()) {
                    etHargaPakaian.setError("Harga harus diisi!");
                }
                if (!namapakaian.isEmpty() && !hargapakaian.isEmpty() && !validasi.equals("0")) {

                    loading = new ProgressDialog(TambahLayanan.this);
                    loading.setTitle("Harap Menunggu");
                    loading.setMessage("Loading...");
                    loading.show();

                    File imagefile = createTempFile(bitmap);
                    RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imagefile);
                    MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto", imagefile.getName(), reqBody);

                    MitraApi api = Retroserver.getClient().create(MitraApi.class);
                    Call<ResponseModel> tambahJenis = api.tambahLayanan(idBaru, namaBaru, hargaBaru, partImage);
                    tambahJenis.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            loading.hide();
                            Toast.makeText(TambahLayanan.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();

                            Intent pindah = new Intent(TambahLayanan.this, HalamanMitra.class);
                            pindah.putExtra("kodeHalaman","TAMBAHLAYANAN");
                            startActivity(pindah);
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(TambahLayanan.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (!namapakaian.isEmpty() && !hargapakaian.isEmpty() && !validasi.equals("1")){
                    loading = new ProgressDialog(TambahLayanan.this);
                    loading.setTitle("Harap Menunggu");
                    loading.setMessage("Loading...");
                    loading.show();

                    MitraApi api = Retroserver.getClient().create(MitraApi.class);
                    Call<ResponseModel> tambahLayananNoFoto = api.tambahLayananNoFoto(id,namapakaian,hargapakaian);
                    tambahLayananNoFoto.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            loading.hide();
                            Toast.makeText(TambahLayanan.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();

                            Intent pindah = new Intent(TambahLayanan.this, HalamanMitra.class);
                            pindah.putExtra("kodeHalaman","TAMBAHLAYANAN");
                            startActivity(pindah);
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(TambahLayanan.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private static class Session {

        private SharedPreferences prefs;

        private Session(Context ctx) {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        private String getId() {
            String id = prefs.getString("id", "");
            return id;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
            path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                ivFoto.setImageBitmap(bitmap);
                validasi = "1";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +".webp");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.WEBP,0, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
