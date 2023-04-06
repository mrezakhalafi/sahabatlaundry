package com.hovercout.sahabatlaundry.mitra.history;

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

public class HistoryFragment extends Fragment {

    private RecyclerView tampilHistory;
    private RecyclerView.Adapter adapterHistory;

    private ProgressDialog loading;
    private List<PesananModel> itemPesanan = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);

        Session session;
        session = new Session(getContext());
        String id = session.getId();

        RecyclerView.LayoutManager layoutHistory;

        tampilHistory = root.findViewById(R.id.tampilHistory);
        layoutHistory = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        tampilHistory.setLayoutManager(layoutHistory);

        loading = new ProgressDialog(getContext());
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        PesananApi api = Retroserver.getClient().create(PesananApi.class);
        Call<ResponseModel> historyMitra = api.lihatHistoryMitra(id);
        historyMitra.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                loading.hide();
                itemPesanan = response.body().getListPesanan();

                adapterHistory = new AdapterPesananMitra(getContext(), itemPesanan);
                tampilHistory.setAdapter(adapterHistory);
                adapterHistory.notifyDataSetChanged();
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

