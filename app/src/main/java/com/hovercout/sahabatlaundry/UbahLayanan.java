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
import com.squareup.picasso.Picasso;
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

public class UbahLayanan extends AppCompatActivity {

    EditText etNamaLayanan, etHargaLayanan;
    Button btnUbah, btnUpload;
    ImageView btnBack;
    ProgressDialog loading;
    Bitmap bitmap;
    Uri path;
    ImageView ivFoto;
    final int REQUEST_GALLERY = 9544;
    String validasi = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_layanan);

        etNamaLayanan = findViewById(R.id.etNamaLayanan);
        etHargaLayanan = findViewById(R.id.etHargaLayanan);
        btnUbah = findViewById(R.id.btnUbah);
        btnUpload = findViewById(R.id.btnUpload);
        ivFoto = findViewById(R.id.ivFoto);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Open Gallery"),REQUEST_GALLERY);
            }
        });

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent data = getIntent();
        String id = data.getStringExtra("id");
        String namapakaian = data.getStringExtra("nama");
        String hargapakaian = data.getStringExtra("harga");
        String foto = data.getStringExtra("foto");

        etNamaLayanan.setText(namapakaian);
        etHargaLayanan.setText(hargapakaian);

        Picasso.get().load(foto).fit().centerCrop().into(ivFoto);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nama = etNamaLayanan.getText().toString();
                String harga = etHargaLayanan.getText().toString();

                RequestBody idBaru = RequestBody.create(MediaType.parse("text/plain"), id);
                RequestBody namaBaru = RequestBody.create(MediaType.parse("text/plain"), nama);
                RequestBody hargaBaru = RequestBody.create(MediaType.parse("text/plain"), harga);

                Session session;
                session = new Session(UbahLayanan.this);
                String idMitra = session.getId();
                RequestBody idMitraBaru = RequestBody.create(MediaType.parse("text/plain"), idMitra);

                if (nama.isEmpty()) {
                    etNamaLayanan.setError("Nama harus diisi!");
                }
                if (harga.isEmpty()) {
                    etHargaLayanan.setError("Harga harus diisi!");
                }
                if (!nama.isEmpty() && !harga.isEmpty()) {

                    loading = new ProgressDialog(UbahLayanan.this);
                    loading.setTitle("Harap Menunggu");
                    loading.setMessage("Loading...");
                    loading.show();

                    if (!validasi.equals("0")) {

                        loading.hide();
                        File imagefile = createTempFile(bitmap);
                        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imagefile);
                        MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto", imagefile.getName(), reqBody);

                        MitraApi api = Retroserver.getClient().create(MitraApi.class);
                        Call<ResponseModel> ubahLayanan = api.ubahLayanan(idBaru, idMitraBaru, namaBaru, hargaBaru, partImage);

                        ubahLayanan.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                Toast.makeText(UbahLayanan.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();

                                Intent pindah = new Intent(UbahLayanan.this, HalamanMitra.class);
                                pindah.putExtra("kodeHalaman","UBAHLAYANAN");
                                startActivity(pindah);
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Toast.makeText(UbahLayanan.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        loading.hide();

                        MitraApi api = Retroserver.getClient().create(MitraApi.class);
                        Call<ResponseModel> ubahLayanan = api.ubahLayananNoFoto(id,nama,harga);
                        ubahLayanan.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                Toast.makeText(UbahLayanan.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();

                                Intent pindah = new Intent(UbahLayanan.this, HalamanMitra.class);
                                pindah.putExtra("kodeHalaman","TAMBAHLAYANAN");
                                startActivity(pindah);
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Toast.makeText(UbahLayanan.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
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
