package com.hovercout.sahabatlaundry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.adapter.AdapterMitra;
import com.hovercout.sahabatlaundry.api.MitraApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.MitraModel;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesanLaundry extends AppCompatActivity {

    RecyclerView tampilMitra;
    RecyclerView.LayoutManager layoutMitra;
    RecyclerView.Adapter adapterMitra;

    ImageView btnBack;
    ProgressDialog loading;
    SearchView searchLaundry;
    Session session;

    List<MitraModel> itemMitra = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_laundry);

        tampilMitra = findViewById(R.id.tampilMitra);
        layoutMitra = new LinearLayoutManager(PesanLaundry.this, RecyclerView.VERTICAL, false);
        tampilMitra.setLayoutManager(layoutMitra);

        searchLaundry = findViewById(R.id.searchLaundry);

        loading = new ProgressDialog(PesanLaundry.this);
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchLaundry.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loading.show();

                MitraApi api = Retroserver.getClient().create(MitraApi.class);
                Call<ResponseModel> searchMitra = api.searchMitra(query);
                searchMitra.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        loading.hide();
                        itemMitra = response.body().getListMitra();

                        session = new Session(PesanLaundry.this);

                        for(int i=0; i<itemMitra.size(); i++){
                            String latitude = itemMitra.get(i).getLatitude();
                            String longitude = itemMitra.get(i).getLongtitude();

                            Location startPoint=new Location("locationA");
                            startPoint.setLatitude(Double.parseDouble(session.getLatitude()));
                            startPoint.setLongitude(Double.parseDouble(session.getLongtitude()));

                            Location endPoint=new Location("locationB");
                            endPoint.setLatitude(Double.parseDouble(latitude));
                            endPoint.setLongitude(Double.parseDouble(longitude));

                            Float distance = startPoint.distanceTo(endPoint)/1000;

                            itemMitra.get(i).setJarak(distance);
                        }

                        Collections.sort(itemMitra, new Comparator<MitraModel>() {
                            @Override
                            public int compare(MitraModel mitraModel, MitraModel t1) {
                                return mitraModel.getJarak().compareTo(t1.getJarak());
                            }
                        });

                        adapterMitra = new AdapterMitra(PesanLaundry.this, itemMitra);
                        tampilMitra.setAdapter(adapterMitra);
                        adapterMitra.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(PesanLaundry.this,"Koneksi Gagal",Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MitraApi api = Retroserver.getClient().create(MitraApi.class);
        Call<ResponseModel> lihatMitra = api.lihatMitra();
        lihatMitra.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                itemMitra = response.body().getListMitra();

                session = new Session(PesanLaundry.this);

                for(int i=0; i<itemMitra.size(); i++){
                    String latitude = itemMitra.get(i).getLatitude();
                    String longitude = itemMitra.get(i).getLongtitude();

                    Location startPoint=new Location("locationA");
                    startPoint.setLatitude(Double.parseDouble(session.getLatitude()));
                    startPoint.setLongitude(Double.parseDouble(session.getLongtitude()));

                    Location endPoint=new Location("locationB");
                    endPoint.setLatitude(Double.parseDouble(latitude));
                    endPoint.setLongitude(Double.parseDouble(longitude));

                    Float distance = startPoint.distanceTo(endPoint)/1000;

                    itemMitra.get(i).setJarak(distance);
                }

                Collections.sort(itemMitra, new Comparator<MitraModel>() {
                    @Override
                    public int compare(MitraModel mitraModel, MitraModel t1) {
                        return mitraModel.getJarak().compareTo(t1.getJarak());
                    }
                });

                adapterMitra = new AdapterMitra(PesanLaundry.this, itemMitra);
                tampilMitra.setAdapter(adapterMitra);
                adapterMitra.notifyDataSetChanged();
                loading.hide();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(PesanLaundry.this,"Koneksi Gagal",Toast.LENGTH_LONG).show();
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
