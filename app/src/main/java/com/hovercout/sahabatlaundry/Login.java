package com.hovercout.sahabatlaundry;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.api.LoginApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.CustomerModel;
import com.hovercout.sahabatlaundry.model.MitraModel;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    Session session;
    TextView textRegister;
    ImageView btnBack;
    ProgressDialog loading;
    String token;
    private List<CustomerModel> itemCustomer = new ArrayList<>();
    private List<MitraModel> itemMitra = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textRegister = findViewById(R.id.textRegister);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                finish();
            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(Login.this, PilihRegister.class);
                startActivity(pindah);
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
                Log.d("TOKEN GAN",""+token);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();


                if (email.isEmpty()) {
                    etEmail.setError("Email harus diisi!");
                }
                if (password.isEmpty()) {
                    etPassword.setError("Password harus diisi!");
                }
                if(!email.isEmpty() && !password.isEmpty()){
                    loading = new ProgressDialog(Login.this);
                    loading.setTitle("Harap Menunggu");
                    loading.setMessage("Loading...");
                    loading.show();

                    LoginApi api = Retroserver.getClient().create(LoginApi.class);
                    Call<ResponseModel> login = api.login(email, password, token);
                    login.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.body().getKode().equals("1")) {
                                itemCustomer = response.body().getListCustomer();
                                CustomerModel customer = itemCustomer.get(0);

                                String id = customer.getId();
                                String nama = customer.getNama_lengkap();
                                String alamat = customer.getAlamat();
                                String no_handphone = customer.getNomor_handphone();
                                String bergabung = customer.getBergabung();
                                String latitude = customer.getLatitude();
                                String longtitude = customer.getLongtitude();

                                session = new Session(Login.this);
                                session.setId(id);
                                session.setNama_lengkap(nama);
                                session.setEmail(etEmail.getText().toString());
                                session.setPassword(etPassword.getText().toString());
                                session.setAlamat(alamat);
                                session.setNo_handphone(no_handphone);
                                session.setBergabung(bergabung);
                                session.setLatitude(latitude);
                                session.setLongtitude(longtitude);

                                Intent pindah = new Intent(Login.this, HalamanCustomer.class);
                                pindah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Clear semua Activity sebelumnya
                                startActivity(pindah);
                            }else if(response.body().getKode().equals("4")) {
                                itemMitra = response.body().getListMitra();
                                MitraModel mitra = itemMitra.get(0);

                                session = new Session(Login.this);
                                session.setId(mitra.getId());
                                session.setNama_mitra(mitra.getNama());
                                session.setAlamat(mitra.getAlamat());
                                session.setEmail(mitra.getEmail());
                                session.setPassword(etPassword.getText().toString());
                                session.setNo_handphone(mitra.getNomor_handphone());
                                session.setBergabung(mitra.getBergabung());
                                session.setJam_operasional(mitra.getJam_buka().substring(0,mitra.getJam_buka().length() -3)+"-"+mitra.getJam_tutup().substring(0,mitra.getJam_tutup().length()
                                        -3));
                                session.setJam_buka(mitra.getJam_buka().substring(0,mitra.getJam_buka().length() -3));
                                session.setJam_tutup(mitra.getJam_tutup().substring(0,mitra.getJam_tutup().length() -3));

                                session.setLatitude(mitra.getLatitude());
                                session.setLongtitude(mitra.getLongtitude());

                                session.setFoto(mitra.getFoto());

                                Intent pindah = new Intent(Login.this, HalamanMitra.class);
                                pindah.putExtra("kodeHalaman","LOGIN");
                                startActivity(pindah);
                            }
                            else{
                                Toast.makeText(Login.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                loading.hide();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(Login.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private static class Session {

        private SharedPreferences prefs;

        private Session(Context ctx) {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        private void setId(String id) {
            prefs.edit().putString("id", id).apply();
        }
        private void setNama_lengkap(String nama) {
            prefs.edit().putString("nama", nama).apply();
        }
        private void setNama_mitra(String namaMitra) {
            prefs.edit().putString("namaMitra", namaMitra).apply();
        }
        private void setEmail(String email) {
            prefs.edit().putString("email", email).apply();
        }
        private void setPassword(String password) {
            prefs.edit().putString("password", password).apply();
        }
        private void setAlamat(String alamat) {
            prefs.edit().putString("alamat", alamat).apply();
        }
        private void setNo_handphone(String no_handphone) {
            prefs.edit().putString("no_handphone", no_handphone).apply();
        }
        private void setBergabung(String bergabung) {
            prefs.edit().putString("bergabung", bergabung).apply();
        }
        private void setLatitude(String latitude) {
            prefs.edit().putString("latitude", latitude).apply();
        }
        private void setLongtitude(String longtitude) {
            prefs.edit().putString("longtitude", longtitude).apply();
        }
        private void setJam_operasional(String jamOperasional) {
            prefs.edit().putString("jamOperasional", jamOperasional).apply();
        }
        private void setJam_buka(String jamBuka) {
            prefs.edit().putString("jamBuka", jamBuka).apply();
        }
        private void setJam_tutup(String jamTutup) {
            prefs.edit().putString("jamTutup", jamTutup).apply();
        }
        private void setFoto(String foto) {
            prefs.edit().putString("foto", foto).apply();
        }
    }

    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}
