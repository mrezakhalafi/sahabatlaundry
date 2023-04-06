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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.api.CustomerApi;
import com.hovercout.sahabatlaundry.api.MitraApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    EditText etNamaLengkap, etEmail, etPassword, etAlamat, etNomorHandphone, etJamBuka, etJamTutup, etJamBuka2, etJamTutup2;
    TextView tvJamOperasional, tvHingga, titik, titik2;
    Button btnRegister;
    ImageView btnBack;
    Session session;
    ProgressDialog loading;
    CheckBox cbKebijakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNamaLengkap = findViewById(R.id.etNamaLengkap);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etAlamat = findViewById(R.id.etAlamat);
        etNomorHandphone = findViewById(R.id.etNomorHandphone);
        btnRegister = findViewById(R.id.btnRegister);

        etJamBuka = findViewById(R.id.etJamBuka);
        etJamTutup = findViewById(R.id.etJamTutup);
        tvJamOperasional = findViewById(R.id.tvJamOperasional);
        tvHingga = findViewById(R.id.tvHingga);
        etJamBuka2 = findViewById(R.id.etJamBuka2);
        etJamTutup2 = findViewById(R.id.etJamTutup2);
        titik = findViewById(R.id.titik);
        titik2 = findViewById(R.id.titik2);
        cbKebijakan = findViewById(R.id.cbKebijakan);

        etJamBuka.setVisibility(View.GONE);
        etJamTutup.setVisibility(View.GONE);
        tvJamOperasional.setVisibility(View.GONE);
        tvHingga.setVisibility(View.GONE);
        etJamBuka2.setVisibility(View.GONE);
        etJamTutup2.setVisibility(View.GONE);
        titik.setVisibility(View.GONE);
        titik2.setVisibility(View.GONE);

        Intent data = getIntent();
        String user = data.getStringExtra("user");

        if(user.equals("2")){
            etJamBuka.setVisibility(View.VISIBLE);
            etJamTutup.setVisibility(View.VISIBLE);
            tvJamOperasional.setVisibility(View.VISIBLE);
            tvHingga.setVisibility(View.VISIBLE);
            etJamBuka2.setVisibility(View.VISIBLE);
            etJamTutup2.setVisibility(View.VISIBLE);
            titik.setVisibility(View.VISIBLE);
            titik2.setVisibility(View.VISIBLE);
        }

        session = new Session(Register.this);

        String namaLengkap = getIntent().getStringExtra("namaLengkap");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        String alamat = getIntent().getStringExtra("alamat");
        String nomorHandphone = getIntent().getStringExtra("nomorHandphone");
        String jamBuka = getIntent().getStringExtra("jamBuka");
        String jamTutup = getIntent().getStringExtra("jamTutup");
        String jamBuka2 = getIntent().getStringExtra("jamBuka2");
        String jamTutup2 = getIntent().getStringExtra("jamTutup2");

        etNamaLengkap.setText(namaLengkap);
        etEmail.setText(email);
        etPassword.setText(password);
        etAlamat.setText(alamat);
        etNomorHandphone.setText(nomorHandphone);
        etJamBuka.setText(jamBuka);
        etJamTutup.setText(jamTutup);
        etJamBuka2.setText(jamBuka2);
        etJamTutup2.setText(jamTutup2);

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
                Intent pindah = new Intent(Register.this, PilihMaps.class);
                pindah.putExtra("namaLengkap",""+etNamaLengkap.getText().toString());
                pindah.putExtra("email",""+etEmail.getText().toString());
                pindah.putExtra("password",""+etPassword.getText().toString());

                pindah.putExtra("jamBuka",""+etJamBuka.getText().toString());
                pindah.putExtra("jamTutup",""+etJamTutup.getText().toString());
                pindah.putExtra("jamBuka2",""+etJamBuka2.getText().toString());
                pindah.putExtra("jamTutup2",""+etJamTutup2.getText().toString());

                pindah.putExtra("user",""+getIntent().getStringExtra("user"));
                pindah.putExtra("nomorHandphone",""+etNomorHandphone.getText().toString());
                startActivity(pindah);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.equals("1")) {

                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    String namaLengkap = etNamaLengkap.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String alamat = etAlamat.getText().toString();
                    String nomorHandphone = etNomorHandphone.getText().toString();

                    if (namaLengkap.isEmpty()) {
                        etNamaLengkap.setError("Nama Lengkap harus diisi!");
                    }
                    if (email.isEmpty()) {
                        etEmail.setError("Email harus diisi!");
                    }
                    else if(!email.matches(emailPattern)){
                        etEmail.setError("Email tidak sesuai format!");
                    }
                    if (password.isEmpty()) {
                        etPassword.setError("Password harus diisi!");
                    }
                    else if (password.length() < 6){
                        etPassword.setError("Password minimal 6 karakter!");
                    }
                    if (alamat.isEmpty()) {
                        etAlamat.setError("Alamat harus diisi!");
                    }
                    if (nomorHandphone.isEmpty()) {
                        etNomorHandphone.setError("Nomor Handphone harus diisi!");
                    }
                    if (!cbKebijakan.isChecked()){
                        cbKebijakan.setError("Kebijakan harus disetujui!");
                    }
                    if (!namaLengkap.isEmpty() && !email.isEmpty() && email.matches(emailPattern) && !password.isEmpty() && password.length() >= 6 && !alamat.isEmpty() && !nomorHandphone.isEmpty() && cbKebijakan.isChecked()) {
                        loading = new ProgressDialog(Register.this);
                        loading.setTitle("Harap Menunggu");
                        loading.setMessage("Loading...");
                        loading.show();

                        String latitude = session.getLatitude();
                        String longtitude = session.getLongtitude();

                        CustomerApi api = Retroserver.getClient().create(CustomerApi.class);
                        Call<ResponseModel> tambahCustomer = api.tambahCustomer(namaLengkap, email, password, alamat, latitude, longtitude, nomorHandphone);
                        tambahCustomer.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                loading.hide();

                                if(response.body().getKode().equals("10")){
                                    Toast.makeText(Register.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Register.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                    Intent pindah = new Intent(Register.this, Login.class);
                                    pindah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Clear semua Activity sebelumnya
                                    startActivity(pindah);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Toast.makeText(Register.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                                loading.hide();
                            }
                        });
                    }
                }else{

                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    String namaLengkap = etNamaLengkap.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String alamat = etAlamat.getText().toString();
                    String nomorHandphone = etNomorHandphone.getText().toString();
                    String jamBuka = etJamBuka.getText().toString()+etJamBuka2.getText().toString()+"00";
                    String jamTutup = etJamTutup.getText().toString()+etJamTutup2.getText().toString()+"00";

                    String cekBuka = etJamBuka.getText().toString();
                    String cekTutup = etJamTutup.getText().toString();
                    String cekBuka2 = etJamBuka2.getText().toString();
                    String cekTutup2 = etJamTutup2.getText().toString();

                    if (namaLengkap.isEmpty()) {
                        etNamaLengkap.setError("Nama Lengkap harus diisi!");
                    }
                    if (email.isEmpty()) {
                        etEmail.setError("Email harus diisi!");
                    }
                    else if(!email.matches(emailPattern)){
                        etEmail.setError("Email tidak sesuai format!");
                    }
                    if (password.isEmpty()) {
                        etPassword.setError("Password harus diisi!");
                    }
                    else if (password.length() < 6){
                        etPassword.setError("Password minimal 6 karakter!");
                    }
                    if (alamat.isEmpty()) {
                        etAlamat.setError("Alamat harus diisi!");
                    }
                    if (nomorHandphone.isEmpty()) {
                        etNomorHandphone.setError("Nomor Handphone harus diisi!");
                    }
                    if (cekBuka.isEmpty()){
                        etJamBuka.setError("Jam Buka harus Diisi!");
                    }
                    else if(Integer.parseInt(cekBuka) > 23){
                        etJamBuka.setError("Jam harus sesuai Format!");
                    }
                    if (cekTutup.isEmpty()){
                        etJamTutup.setError("Jam Tutup harus Diisi!");
                    }
                    else if(Integer.parseInt(cekTutup) > 23){
                        etJamTutup.setError("Jam harus sesuai Format!");
                    }
                    if (cekBuka2.isEmpty()){
                        etJamBuka2.setError("Jam Buka harus Diisi!");
                    }
                    else if(Integer.parseInt(cekBuka2) > 59){
                        etJamBuka2.setError("Jam harus sesuai Format!");
                    }
                    if (cekTutup2.isEmpty()){
                        etJamTutup2.setError("Jam Tutup harus Diisi!");
                    }
                    else if(Integer.parseInt(cekTutup2) > 59){
                        etJamTutup2.setError("Jam harus sesuai Format!");
                    }
                    if (!cbKebijakan.isChecked()){
                        cbKebijakan.setError("Kebijakan harus disetujui!");
                    }
                    if (!namaLengkap.isEmpty() && !email.isEmpty() && email.matches(emailPattern) && !password.isEmpty() && password.length() >= 6 && !alamat.isEmpty() && !nomorHandphone.isEmpty() && !cekBuka.isEmpty() && Integer.parseInt(cekBuka) <= 23 && !cekBuka2.isEmpty() && Integer.parseInt(cekBuka2) <= 59 && !cekTutup.isEmpty() && Integer.parseInt(cekTutup) <=23 && !cekTutup2.isEmpty() && Integer.parseInt(cekTutup2) <=59 && cbKebijakan.isChecked()) {
                        loading = new ProgressDialog(Register.this);
                        loading.setTitle("Harap Menunggu");
                        loading.setMessage("Loading...");
                        loading.show();

                        String latitude = session.getLatitude();
                        String longtitude = session.getLongtitude();

                        MitraApi api = Retroserver.getClient().create(MitraApi.class);
                        Call<ResponseModel> tambahCustomer = api.tambahMitra(namaLengkap, email, password, alamat, latitude, longtitude, jamBuka, jamTutup, nomorHandphone);
                        tambahCustomer.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                loading.hide();

                                if(response.body().getKode().equals("10")) {
                                    Toast.makeText(Register.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Intent pindah = new Intent(Register.this, Login.class);
                                    Toast.makeText(Register.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                    pindah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Clear semua Activity sebelumnya
                                    startActivity(pindah);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Toast.makeText(Register.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                                loading.hide();
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

        private String getLatitude() {
            String latitude = prefs.getString("latitude", "");
            return latitude;
        }
        private String getLongtitude() {
            String longtitude = prefs.getString("longtitude", "");
            return longtitude;
        }
    }
}
