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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.api.PesananApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HalamanKonfirmasi extends AppCompatActivity {

    ArrayList<String> pesanan;
    TextView tvPesanan;
    TextView tvNamaLengkap, tvAlamat, tvHarga, tvTotalTransaksi, tvNamaLaundry, tvOngkir;
    private Session session;
    private SharedPreferences prefs;
    ImageView btnBack;

    ArrayList<String> idPesanan = new ArrayList<>();
    ArrayList<String> namaItem = new ArrayList<>();
    ArrayList<Integer> jumlahItem = new ArrayList<>();

    Locale localeID = new Locale("in", "ID");

    Button btnPesan;
    ProgressDialog loading;
    int parseOngkir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_konfirmasi);

        prefs = PreferenceManager.getDefaultSharedPreferences(HalamanKonfirmasi.this);

        Intent data = getIntent();
        idPesanan = data.getStringArrayListExtra("idPesanan");
        pesanan = data.getStringArrayListExtra("Pesanan");
        tvPesanan = findViewById(R.id.tvPesanan);
        tvNamaLengkap = findViewById(R.id.tvNamaLengkap);
        tvAlamat = findViewById(R.id.tvAlamat);
        tvHarga = findViewById(R.id.tvHarga);
        tvTotalTransaksi = findViewById(R.id.tvTotalTransaksi);
        tvNamaLaundry = findViewById(R.id.tvNamaLaundry);
        tvNamaLaundry.setText(data.getStringExtra("namaLaundry"));
        tvOngkir = findViewById(R.id.tvOngkir);

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        if(Integer.parseInt(data.getStringExtra("jauh"))>=1){
            parseOngkir = Integer.parseInt(data.getStringExtra("jauh"))*2000;
        }else{
            parseOngkir = 2000;
        }

        Double formatOngkir = Double.valueOf(parseOngkir);
        tvOngkir.setText(formatRupiah.format(formatOngkir));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        StringBuilder builder = new StringBuilder();
        for (String details : pesanan) {
            builder.append(details + " x" + prefs.getInt(details,0) + "\n"); //GET JUMLAH DARI SET PREF ATAS NAMA PAKAIAN
            namaItem.add(details);
            jumlahItem.add(prefs.getInt(details,0));
        }
        tvPesanan.setText(builder.toString());

        session = new Session(HalamanKonfirmasi.this);
        String namaLengkap = session.getNama_lengkap();
        tvNamaLengkap.setText(namaLengkap);
        String alamat = session.getAlamat();
        tvAlamat.setText(alamat);

        String totalBiaya = data.getStringExtra("totalBiaya");
        Double formatHarga = Double.parseDouble(totalBiaya);
        tvHarga.setText(formatRupiah.format(formatHarga));

        int totalAll = Integer.parseInt(totalBiaya)+parseOngkir;
        Double formatTotalAll = Double.valueOf(totalAll);
        tvTotalTransaksi.setText(formatRupiah.format(formatTotalAll));

        String token = data.getStringExtra("token");

        btnPesan = findViewById(R.id.btnPesan);
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = new ProgressDialog(HalamanKonfirmasi.this);
                loading.setTitle("Harap Menunggu");
                loading.setMessage("Loading...");
                loading.show();

                PesananApi api = Retroserver.getClient().create(PesananApi.class);
                Call<ResponseModel> tambahPesanan = api.tambahPesanan(session.getId(),data.getStringExtra("idLaundry"),idPesanan,jumlahItem,totalBiaya,parseOngkir,totalAll,token);
                tambahPesanan.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        loading.hide();
                        Intent pindah = new Intent(HalamanKonfirmasi.this, LaundrySaya.class);
                        Toast.makeText(HalamanKonfirmasi.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                        startActivity(pindah);
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(HalamanKonfirmasi.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
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
        private String getNama_lengkap() {
            String nama = prefs.getString("nama","");
            return nama;
        }
        private String getAlamat() {
            String alamat = prefs.getString("alamat","");
            return alamat;
        }
    }
}
