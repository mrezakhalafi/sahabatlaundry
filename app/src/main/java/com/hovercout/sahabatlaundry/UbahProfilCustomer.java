package com.hovercout.sahabatlaundry;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.api.CustomerApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahProfilCustomer extends AppCompatActivity {

    EditText etNama, etEmail, etAlamat, etNomorHandphone;
    ImageView btnBack;
    Button btnUbahProfil;
    private Session session;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);

        etNama = findViewById(R.id.etNamaLengkap);
        etEmail = findViewById(R.id.etEmail);
        etAlamat = findViewById(R.id.etAlamat);
        etNomorHandphone = findViewById(R.id.etNomorHandphone);
        btnUbahProfil = findViewById(R.id.btnUbahProfil);

        session = new Session(UbahProfilCustomer.this);

        Intent data = getIntent();
        String nama = data.getStringExtra("nama");
        String email = data.getStringExtra("email");
        String alamat = data.getStringExtra("alamat");
        String latitude = session.getLatitude();
        String longtitude = session.getLongtitude();
        String nomorHandphone = data.getStringExtra("nomor_handphone");

        String oldNama = data.getStringExtra("nama");
        String oldEmail = data.getStringExtra("email");

        String cekAlamat;
        String lewatAlamat = data.getStringExtra("lewatalamat");

        if(lewatAlamat == null) {
            cekAlamat = "0";
        }else{
            cekAlamat = "1";
        }
        String oldNomorHandphone = data.getStringExtra("nomor_handphone");

        etNama.setText(nama);
        etEmail.setText(email);
        etAlamat.setText(alamat);
        etNomorHandphone.setText(nomorHandphone);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(UbahProfilCustomer.this, PilihMaps.class);
                pindah.putExtra("namaLengkap",""+etNama.getText().toString());
                pindah.putExtra("email",""+etEmail.getText().toString());
                pindah.putExtra("nomorHandphone",""+etNomorHandphone.getText().toString());
                startActivity(pindah);
            }
        });

        btnUbahProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (oldNama.equals(etNama.getText().toString()) && oldEmail.equals(etEmail.getText().toString()) && oldNomorHandphone.equals(etNomorHandphone.getText().toString()) && cekAlamat.equals("0")) {
                    finish();
                } else {

                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (etNama.getText().toString().isEmpty()) {
                        etNama.setError("Nama Lengkap harus diisi!");
                    }
                    if (etEmail.getText().toString().isEmpty()) {
                        etEmail.setError("Email harus diisi!");
                    } else if (!etEmail.getText().toString().matches(emailPattern)) {
                        etEmail.setError("Email tidak sesuai Format!");
                    }
                    if (etAlamat.getText().toString().isEmpty()) {
                        etAlamat.setError("Alamat harus diisi!");
                    }
                    if (etNomorHandphone.getText().toString().isEmpty()) {
                        etNomorHandphone.setError("Nomor Handphone harus diisi!");
                    }
                    if (!etNama.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty() && etEmail.getText().toString().matches(emailPattern) && !etAlamat.getText().toString().isEmpty() && !etNomorHandphone.getText().toString().isEmpty()) {
                        loading = new ProgressDialog(UbahProfilCustomer.this);
                        loading.setTitle("Harap Menunggu");
                        loading.setMessage("Loading...");
                        loading.show();

                        CustomerApi api = Retroserver.getClient().create(CustomerApi.class);
                        Call<ResponseModel> update = api.ubahCustomer(session.getId(), etNama.getText().toString(), etEmail.getText().toString(), etAlamat.getText().toString(), latitude, longtitude, etNomorHandphone.getText().toString());
                        update.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                loading.hide();
                                Toast.makeText(UbahProfilCustomer.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                session.setNama_lengkap("");
                                session.setEmail("");
                                Intent pindah = new Intent(UbahProfilCustomer.this, Login.class);
                                startActivity(pindah);
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Toast.makeText(UbahProfilCustomer.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
            String id = prefs.getString("id","");
            return id;
        }
        private String getLatitude() {
            String latitude = prefs.getString("latitude","");
            return latitude;
        }
        private String getLongtitude() {
            String longtitude = prefs.getString("longtitude","");
            return longtitude;
        }
        private void setNama_lengkap(String nama) {
            prefs.edit().putString("nama", nama).apply();
        }
        private void setEmail(String email) {
            prefs.edit().putString("email", email).apply();
        }
    }
}
