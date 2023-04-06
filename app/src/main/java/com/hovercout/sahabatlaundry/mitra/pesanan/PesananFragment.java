package com.hovercout.sahabatlaundry.mitra.pesanan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananFragment extends Fragment {

    private RecyclerView tampilPesanan;
    private RecyclerView.Adapter adapterPesanan;

    private ProgressDialog loading;
    private List<PesananModel> itemPesanan = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pesanan, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Pesanan Masuk");

        RecyclerView.LayoutManager layoutPesanan;

        tampilPesanan = root.findViewById(R.id.tampilPesanan);
        layoutPesanan = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        tampilPesanan.setLayoutManager(layoutPesanan);

        loading = new ProgressDialog(getContext());
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        Session session;
        session = new Session(getContext());
        String id = session.getId();

        PesananApi api = Retroserver.getClient().create(PesananApi.class);
        Call<ResponseModel> readMitra = api.lihatPesananMitra(id);
        readMitra.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                loading.hide();
                itemPesanan = response.body().getListPesanan();

                adapterPesanan = new AdapterPesananMitra(getContext(), itemPesanan);
                tampilPesanan.setAdapter(adapterPesanan);
                adapterPesanan.notifyDataSetChanged();
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
