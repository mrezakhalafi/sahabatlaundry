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
import com.hovercout.sahabatlaundry.api.MitraApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahProfilMitra extends AppCompatActivity {

    EditText etNama, etEmail, etAlamat, etNomorHandphone, etJamBuka, etJamTutup, etJamBuka2, etJamTutup2;
    ImageView btnBack;
    Button btnUbahProfil;
    private Session session;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil_mitra);

        etNama = findViewById(R.id.etNamaLengkap);
        etEmail = findViewById(R.id.etEmail);
        etAlamat = findViewById(R.id.etAlamat);
        etNomorHandphone = findViewById(R.id.etNomorHandphone);
        btnUbahProfil = findViewById(R.id.btnUbahProfil);
        etJamBuka = findViewById(R.id.etJamBuka);
        etJamTutup = findViewById(R.id.etJamTutup);
        etJamBuka2 = findViewById(R.id.etJamBuka2);
        etJamTutup2 = findViewById(R.id.etJamTutup2);

        session = new Session(UbahProfilMitra.this);
        String id = session.getId();

        Intent data = getIntent();
        String nama = data.getStringExtra("nama");
        String email = data.getStringExtra("email");
        String alamat = data.getStringExtra("alamat");
        String latitude = session.getLatitude();
        String longtitude = session.getLongtitude();
        String jamBuka = data.getStringExtra("jamBuka");
        String jamTutup = data.getStringExtra("jamTutup");
        String jamBuka2 = data.getStringExtra("jamBuka2");
        String jamTutup2 = data.getStringExtra("jamTutup2");
        String nomorHandphone = data.getStringExtra("nomor_handphone");

        String oldNama = data.getStringExtra("nama");
        String oldEmail = data.getStringExtra("email");
        String oldJamBuka = data.getStringExtra("jamBuka").substring(0,2);
        String oldJamTutup = data.getStringExtra("jamTutup").substring(0,2);
        String cekAlamat;
        String oldNomorHandphone = data.getStringExtra("nomor_handphone");

        String oldJamBuka2;
        String oldJamTutup2;

        if(jamBuka2 == null) {
            oldJamTutup2 = data.getStringExtra("jamTutup").substring(3,5);
            oldJamBuka2 = data.getStringExtra("jamBuka").substring(3,5);
        }else{
            oldJamTutup2 = data.getStringExtra("jamTutup2");
            oldJamBuka2 = data.getStringExtra("jamBuka2");
        }

        etNama.setText(nama);
        etEmail.setText(email);
        etAlamat.setText(alamat);
        etNomorHandphone.setText(nomorHandphone);
        etJamBuka.setText(jamBuka.substring(0,2));
        etJamTutup.setText(jamTutup.substring(0,2));

        if(jamBuka2 == null) {
            etJamBuka2.setText(jamBuka.substring(3, 5));
            etJamTutup2.setText(jamTutup.substring(3, 5));
            cekAlamat = "0";
        }else{
            etJamBuka2.setText(jamBuka2);
            etJamTutup2.setText(jamTutup2);
            cekAlamat = "1";
        }

        etAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(UbahProfilMitra.this, PilihMaps.class);
                pindah.putExtra("namaMitra",""+etNama.getText().toString());
                pindah.putExtra("email",""+etEmail.getText().toString());
                pindah.putExtra("jamBuka",""+etJamBuka.getText().toString());
                pindah.putExtra("jamTutup",""+etJamTutup.getText().toString());
                pindah.putExtra("jamBuka2",""+etJamBuka2.getText().toString());
                pindah.putExtra("jamTutup2",""+etJamTutup2.getText().toString());
                pindah.putExtra("nomorHandphone",""+etNomorHandphone.getText().toString());
                startActivity(pindah);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUbahProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (oldNama.equals(etNama.getText().toString()) && cekAlamat.equals("0") && oldEmail.equals(etEmail.getText().toString()) && oldNomorHandphone.equals(etNomorHandphone.getText().toString()) && oldJamBuka.equals(etJamBuka.getText().toString()) && oldJamBuka2.equals(etJamBuka2.getText().toString()) && oldJamTutup.equals(etJamTutup.getText().toString()) && oldJamTutup2.equals(etJamTutup2.getText().toString())) {
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
                    if (etJamBuka.getText().toString().isEmpty()) {
                        etJamBuka.setError("Jam Buka harus diisi!");
                    } else if (Integer.parseInt(etJamBuka.getText().toString()) > 23) {
                        etJamBuka.setError("Jam harus sesuai Format!");
                    }
                    if (etJamTutup.getText().toString().isEmpty()) {
                        etJamTutup.setError("Jam Tutup harus diisi");
                    } else if (Integer.parseInt(etJamTutup.getText().toString()) > 23) {
                        etJamTutup.setError("Jam harus sesuai Format!");
                    }
                    if (etJamBuka2.getText().toString().isEmpty()) {
                        etJamBuka2.setError("Jam Buka harus diisi!");
                    } else if (Integer.parseInt(etJamBuka2.getText().toString()) > 59) {
                        etJamBuka2.setError("Jam harus sesuai Format!");
                    }
                    if (etJamTutup2.getText().toString().isEmpty()) {
                        etJamTutup2.setError("Jam Tutup harus diisi");
                    } else if (Integer.parseInt(etJamTutup2.getText().toString()) > 59) {
                        etJamTutup2.setError("Jam harus sesuai Format!");
                    }
                    if (etNomorHandphone.getText().toString().isEmpty()) {
                        etNomorHandphone.setError("Nomor Handphone harus diisi!");
                    }
                    if (!etNama.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty() && etEmail.getText().toString().matches(emailPattern) && !etAlamat.getText().toString().isEmpty() && !etJamBuka.getText().toString().isEmpty() && Integer.parseInt(etJamBuka.getText().toString()) <= 23 && !etJamTutup.getText().toString().isEmpty() && Integer.parseInt(etJamTutup.getText().toString()) <= 23 && !etJamBuka2.getText().toString().isEmpty() && Integer.parseInt(etJamBuka2.getText().toString()) <= 59 && !etJamTutup2.getText().toString().isEmpty() && Integer.parseInt(etJamTutup2.getText().toString()) <= 59 && !etNomorHandphone.getText().toString().isEmpty()) {

                        loading = new ProgressDialog(UbahProfilMitra.this);
                        loading.setTitle("Harap Menunggu");
                        loading.setMessage("Loading...");
                        loading.show();

                        MitraApi api = Retroserver.getClient().create(MitraApi.class);
                        Call<ResponseModel> updateMitra = api.ubahMitra(id, etNama.getText().toString(), etEmail.getText().toString(), etAlamat.getText().toString(), latitude, longtitude, etJamBuka.getText().toString() + etJamBuka2.getText().toString() + "00", etJamTutup.getText().toString() + etJamTutup2.getText().toString() + "00", etNomorHandphone.getText().toString());
                        updateMitra.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                loading.hide();
                                session.setNama_mitra("");
                                session.setEmail("");
                                Toast.makeText(UbahProfilMitra.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                Intent pindah = new Intent(UbahProfilMitra.this, Login.class);
                                startActivity(pindah);
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Toast.makeText(UbahProfilMitra.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
        private void setNama_mitra(String namaMitra) {
            prefs.edit().putString("namaMitra", namaMitra).apply();
        }
        private void setEmail(String email) {
            prefs.edit().putString("email", email).apply();
        }
    }
}
