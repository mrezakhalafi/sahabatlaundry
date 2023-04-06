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

public class HistoryLaundry extends AppCompatActivity {

    RecyclerView tampilHistory;
    RecyclerView.LayoutManager layoutHistory;
    RecyclerView.Adapter adapterHistory;
    ProgressDialog loading;
    List<PesananModel> itemHistory = new ArrayList<>();;

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_laundry);

        btnBack = findViewById(R.id.btnBack);
        tampilHistory = findViewById(R.id.tampilHistory);
        layoutHistory = new LinearLayoutManager(HistoryLaundry.this, RecyclerView.VERTICAL, false);
        tampilHistory.setLayoutManager(layoutHistory);

        loading = new ProgressDialog(HistoryLaundry.this);
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        Session session = new Session(HistoryLaundry.this);
        String idCustomer = session.getId();

        PesananApi api = Retroserver.getClient().create(PesananApi.class);
        Call<ResponseModel> lihatHistory = api.lihatHistoryCustomer(idCustomer);
        lihatHistory.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                loading.hide();
                itemHistory = response.body().getListPesanan();

                adapterHistory = new AdapterPesanan(HistoryLaundry.this, itemHistory);
                tampilHistory.setAdapter(adapterHistory);
                adapterHistory.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(HistoryLaundry.this,"Koneksi Gagal",Toast.LENGTH_LONG).show();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        Intent pindah = new Intent(HistoryLaundry.this, HalamanCustomer.class);
        pindah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Clear semua Activity sebelumnya
        startActivity(pindah);
    }
}
