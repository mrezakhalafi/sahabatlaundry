package com.hovercout.sahabatlaundry.customer.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.hovercout.sahabatlaundry.MenuUtama;
import com.hovercout.sahabatlaundry.R;
import com.hovercout.sahabatlaundry.UbahPasswordCustomer;
import com.hovercout.sahabatlaundry.UbahProfilCustomer;
import com.hovercout.sahabatlaundry.api.LoginApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private TextView tvNamaLengkap, tvEmail, tvAlamat, tvNomorHandphone;
    private Session session;
    private ProgressDialog loading;

    private static final SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);

        Button btnLogout, btnUbahProfil, btnUbahPassword;
        TextView tvBergabung;

        tvNamaLengkap = root.findViewById(R.id.tvNamaLengkap);
        tvEmail = root.findViewById(R.id.tvEmail);
        tvAlamat = root.findViewById(R.id.tvAlamat);
        tvNomorHandphone = root.findViewById(R.id.tvNomorHandphone);
        tvBergabung = root.findViewById(R.id.tvBergabung);
        btnLogout = root.findViewById(R.id.btnLogout);
        btnUbahProfil = root.findViewById(R.id.btnUbahProfil);
        btnUbahPassword = root.findViewById(R.id.btnUbahPassword);

        session = new Session(getContext());
        String namaLengkap = session.getNama_lengkap();
        tvNamaLengkap.setText(namaLengkap);
        String email = session.getEmail();
        tvEmail.setText(email);
        String alamat = session.getAlamat();
        tvAlamat.setText(alamat);
        String nomorHandphone = session.getNo_handphone();
        tvNomorHandphone.setText(nomorHandphone);
        String bergabung = session.getBergabung();

        String formatBergabung = parseDate(bergabung, inputDate, outputDate);

        tvBergabung.setText(formatBergabung);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               confirmLogout();
            }
        });

        btnUbahProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getContext(), UbahProfilCustomer.class);
                pindah.putExtra("nama",tvNamaLengkap.getText().toString());
                pindah.putExtra("alamat",tvAlamat.getText().toString());
                pindah.putExtra("email",tvEmail.getText().toString());
                String enamdua = tvNomorHandphone.getText().toString().substring(1, tvNomorHandphone.getText().length());
                pindah.putExtra("nomor_handphone",enamdua);
                startActivity(pindah);
            }
        });

        btnUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getContext(), UbahPasswordCustomer.class);
                startActivity(pindah);
            }
        });

        return root;
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
        private String getNama_lengkap() {
            String nama = prefs.getString("nama","");
            return nama;
        }
        private String getEmail() {
            String email = prefs.getString("email","");
            return email;
        }
        private String getAlamat() {
            String alamat = prefs.getString("alamat","");
            return alamat;
        }
        private String getNo_handphone() {
            String no_handphone = prefs.getString("no_handphone","");
            return no_handphone;
        }
        private String getBergabung() {
            String bergabung = prefs.getString("bergabung","");
            return bergabung;
        }
        private void setNama_lengkap(String nama) {
            prefs.edit().putString("nama", nama).apply();
        }
        private void setEmail(String email) {
            prefs.edit().putString("email", email).apply();
        }
    }

    private void confirmLogout() {
        session = new Session(getContext());
        String id = session.getId();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("Apakah anda yakin untuk logout?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loading = new ProgressDialog(getContext());
                loading.setTitle("Harap Menunggu");
                loading.setMessage("Loading...");
                loading.show();

                LoginApi api = Retroserver.getClient().create(LoginApi.class);
                Call<ResponseModel> logoutCustomer = api.logoutCustomer(id);
                logoutCustomer.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        loading.hide();
                        session.setNama_lengkap("");
                        session.setEmail("");
                        Intent pindah = new Intent(getContext(), MenuUtama.class);
                        startActivity(pindah);
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }
}