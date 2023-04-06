package com.hovercout.sahabatlaundry.mitra.proses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hovercout.sahabatlaundry.R;
import com.hovercout.sahabatlaundry.adapter.AdapterPesananMitra;
import com.hovercout.sahabatlaundry.api.PesananApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.PesananModel;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProsesFragment extends Fragment {

    private RecyclerView tampilProses;
    private RecyclerView.Adapter adapterProses;

    private ProgressDialog loading;
    private List<PesananModel> itemPesanan = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_proses, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Dalam Proses");

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        Session session;
        session = new Session(getContext());
        String id = session.getId();

        RecyclerView.LayoutManager layoutProses;

        tampilProses = root.findViewById(R.id.tampilProses);
        layoutProses = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        tampilProses.setLayoutManager(layoutProses);

        loading = new ProgressDialog(getContext());
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        SearchView searchLaundry;
        searchLaundry = root.findViewById(R.id.searchLaundry);

        searchLaundry.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loading.show();

                PesananApi api = Retroserver.getClient().create(PesananApi.class);
                Call<ResponseModel> searchCustomer = api.searchCustomer(id,query);
                searchCustomer.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        loading.hide();
                        itemPesanan = response.body().getListPesanan();

                        adapterProses = new AdapterPesananMitra(getContext(), itemPesanan);
                        tampilProses.setAdapter(adapterProses);
                        adapterProses.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        PesananApi api = Retroserver.getClient().create(PesananApi.class);
        Call<ResponseModel> prosesMitra = api.lihatProsesMitra(id);
        prosesMitra.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                loading.hide();
                itemPesanan = response.body().getListPesanan();

                adapterProses = new AdapterPesananMitra(getContext(), itemPesanan);
                tampilProses.setAdapter(adapterProses);
                adapterProses.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
