package com.hovercout.sahabatlaundry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class UploadFoto extends AppCompatActivity {

    Button btnUpload,btnUbah;
    Session session;
    Bitmap bitmap;
    String validasi = "0";
    Uri path;
    ImageView ivFoto, btnBack;
    final int REQUEST_GALLERY = 9544;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_foto);

        btnUpload = findViewById(R.id.btnUpload);
        btnBack = findViewById(R.id.btnBack);
        ivFoto = findViewById(R.id.ivFoto);
        btnUbah = findViewById(R.id.btnUbah);

        session = new Session(UploadFoto.this);
        String id = session.getId();
        String foto = session.getFoto();
        Picasso.get().load(foto).fit().centerCrop().into(ivFoto);

        RequestBody idBaru = RequestBody.create(MediaType.parse("text/plain"), id);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validasi.equals("0")) {

                    File imagefile = createTempFile(bitmap);
                    RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imagefile);
                    MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto", imagefile.getName(), reqBody);

                    MitraApi api = Retroserver.getClient().create(MitraApi.class);
                    Call<ResponseModel> uploadFoto = api.uploadFoto(idBaru, partImage);
                    uploadFoto.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            Toast.makeText(UploadFoto.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();

                            Intent pindah = new Intent(UploadFoto.this, HalamanMitra.class);
                            session = new Session(UploadFoto.this);
                            session.setFoto(response.body().getKode());
                            pindah.putExtra("kodeHalaman","PROFIL");
                            startActivity(pindah);
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(UploadFoto.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    finish();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Open Gallery"),REQUEST_GALLERY);
            }
        });
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

    private static class Session {

        private SharedPreferences prefs;

        private Session(Context ctx) {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        private String getId() {
            String id = prefs.getString("id", "");
            return id;
        }
        private String getFoto() {
            String foto = prefs.getString("foto", "");
            return foto;
        }

        private void setFoto(String foto) {
            prefs.edit().putString("foto", foto).apply();
        }
    }
}
