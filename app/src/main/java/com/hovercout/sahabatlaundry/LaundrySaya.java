package com.hovercout.sahabatlaundry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.adapter.AdapterPesanan;
import com.hovercout.sahabatlaundry.api.PesananApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.PesananModel;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaundrySaya extends AppCompatActivity {

    ImageView btnBack;
    ProgressDialog loading;

    RecyclerView tampilPesanan;
    RecyclerView.LayoutManager layoutPesanan;
    RecyclerView.Adapter adapterPesanan;

    List<PesananModel> itemPesanan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_saya);

        tampilPesanan = findViewById(R.id.tampilPesanan);
        layoutPesanan = new LinearLayoutManager(LaundrySaya.this, RecyclerView.VERTICAL, false);
        tampilPesanan.setLayoutManager(layoutPesanan);

        loading = new ProgressDialog(LaundrySaya.this);
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(LaundrySaya.this, HalamanCustomer.class);
                startActivity(pindah);
            }
        });

        Session session = new Session(LaundrySaya.this);
        String idCustomer = session.getId();

        PesananApi api = Retroserver.getClient().create(PesananApi.class);
        Call<ResponseModel> lihatPesanan = api.lihatPesananCustomer(idCustomer);
        lihatPesanan.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                itemPesanan = response.body().getListPesanan();

                adapterPesanan = new AdapterPesanan(LaundrySaya.this, itemPesanan);
                tampilPesanan.setAdapter(adapterPesanan);
                adapterPesanan.notifyDataSetChanged();
                loading.hide();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(LaundrySaya.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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

    public void onBackPressed() {
        Intent pindah = new Intent(LaundrySaya.this, HalamanCustomer.class);
        pindah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Clear semua Activity sebelumnya
        startActivity(pindah);
    }
}
