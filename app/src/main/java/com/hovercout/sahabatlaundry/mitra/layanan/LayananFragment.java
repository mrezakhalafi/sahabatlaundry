package com.hovercout.sahabatlaundry.mitra.layanan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hovercout.sahabatlaundry.R;
import com.hovercout.sahabatlaundry.TambahLayanan;
import com.hovercout.sahabatlaundry.adapter.AdapterLayananMitra;
import com.hovercout.sahabatlaundry.api.MitraApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.PakaianModel;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LayananFragment extends Fragment {

    private RecyclerView tampilLayanan;
    private RecyclerView.Adapter adapterLayanan;

    private ProgressDialog loading;
    private List<PakaianModel> itemLayanan = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_layanan, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Layanan Saya");

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setChecked(true);

        Session session;
        session = new Session(getContext());
        String id = session.getId();

        Button btnTambah;
        btnTambah = root.findViewById(R.id.btnTambah);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getContext(), TambahLayanan.class);
                startActivity(pindah);
            }
        });

        RecyclerView.LayoutManager layoutLayanan;

        tampilLayanan = root.findViewById(R.id.tampilLayanan);
        layoutLayanan = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        tampilLayanan.setLayoutManager(layoutLayanan);

        loading = new ProgressDialog(getContext());
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        MitraApi api = Retroserver.getClient().create(MitraApi.class);
        Call<ResponseModel> tampilJenisPakaian = api.lihatLayanan(id);
        tampilJenisPakaian.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                loading.hide();
                itemLayanan = response.body().getListPakaian();

                adapterLayanan = new AdapterLayananMitra(getContext(), itemLayanan);
                tampilLayanan.setAdapter(adapterLayanan);
                adapterLayanan.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                Log.d("TOL",t.getMessage());
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
            String id = prefs.getString("id", "");
            return id;
        }
    }
}
