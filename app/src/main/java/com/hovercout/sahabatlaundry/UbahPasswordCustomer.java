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

public class UbahPasswordCustomer extends AppCompatActivity {

    EditText etPasswordLama, etPasswordBaru, etKPasswordBaru;
    ImageView btnBack;
    Button btnUbahPassword;
    private Session session;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);

        etPasswordLama = findViewById(R.id.etPasswordLama);
        etPasswordBaru = findViewById(R.id.etPasswordBaru);
        etKPasswordBaru = findViewById(R.id.etKPasswordBaru);
        btnUbahPassword = findViewById(R.id.btnUbahPassword);

        btnBack = findViewById(R.id.btnBack);

        session = new Session(UbahPasswordCustomer.this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = session.getId();
                String password = session.getPassword();

                if (etPasswordLama.getText().toString().isEmpty()) {
                    etPasswordLama.setError("Password Lama harus diisi!");
                }
                else if(etPasswordLama.getText().toString().length() < 6){
                    etPasswordLama.setError("Password minimal 6 karakter!");
                }
                if (etPasswordBaru.getText().toString().isEmpty()) {
                    etPasswordBaru.setError("Password Baru harus diisi!");
                }
                else if(etPasswordBaru.getText().toString().length() < 6){
                    etPasswordBaru.setError("Password minimal 6 karakter!");
                }
                if (etKPasswordBaru.getText().toString().isEmpty()) {
                    etKPasswordBaru.setError("Konfirmasi Password harus diisi!");
                }
                else if(etKPasswordBaru.getText().toString().length() < 6){
                    etKPasswordBaru.setError("Password minimal 6 karakter!");
                }
                if(etPasswordLama.getText().toString().length() >= 6 && etPasswordBaru.getText().toString().length() >= 6 && etKPasswordBaru.getText().toString().length() >=6){

                    if (etPasswordLama.getText().toString().equals(password)) {
                        if (etPasswordBaru.getText().toString().equals(etKPasswordBaru.getText().toString())) {
                            loading = new ProgressDialog(UbahPasswordCustomer.this);
                            loading.setTitle("Harap Menunggu");
                            loading.setMessage("Loading...");
                            loading.show();
                            CustomerApi api = Retroserver.getClient().create(CustomerApi.class);
                            Call<ResponseModel> ubahPassword = api.ubahPassword(id, etPasswordBaru.getText().toString());
                            ubahPassword.enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    loading.hide();
                                    Toast.makeText(UbahPasswordCustomer.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                    session.setNama_lengkap("");
                                    session.setEmail("");
                                    Intent pindah = new Intent(UbahPasswordCustomer.this, Login.class);
                                    startActivity(pindah);
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    Toast.makeText(UbahPasswordCustomer.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(UbahPasswordCustomer.this, "Password baru tidak sama.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UbahPasswordCustomer.this, "Password lama tidak sama.", Toast.LENGTH_SHORT).show();
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
        private String getPassword() {
            String password = prefs.getString("password", "");
            return password;
        }
        private void setNama_lengkap(String nama) {
            prefs.edit().putString("nama", nama).apply();
        }
        private void setEmail(String email) {
            prefs.edit().putString("email", email).apply();
        }
    }
}
