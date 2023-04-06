package com.hovercout.sahabatlaundry.customer.chome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.hovercout.sahabatlaundry.HistoryLaundry;
import com.hovercout.sahabatlaundry.LaundrySaya;
import com.hovercout.sahabatlaundry.PesanLaundry;
import com.hovercout.sahabatlaundry.R;


public class CHomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chome, container, false);

        Button btnPesanLaundry, btnLaundrySaya, btnHistoryLaundry;
        TextView tvWelcome;

        btnPesanLaundry = root.findViewById(R.id.btnPesanLaundry);
        btnLaundrySaya = root.findViewById(R.id.btnLaundrySaya);
        btnHistoryLaundry = root.findViewById(R.id.btnHistoryLaundry);
        tvWelcome = root.findViewById(R.id.tvWelcome);

        btnPesanLaundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getContext(), PesanLaundry.class);
                startActivity(pindah);
            }
        });

        btnLaundrySaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getContext(), LaundrySaya.class);
                startActivity(pindah);
            }
        });

        btnHistoryLaundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getContext(), HistoryLaundry.class);
                startActivity(pindah);
            }
        });

        Session session;
        session = new Session(getContext());
        tvWelcome.setText("Selamat Datang, "+session.getNama_lengkap());

        return root;
    }

    private static class Session {

        private SharedPreferences prefs;

        private Session(Context ctx) {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        private String getNama_lengkap() {
            String nama = prefs.getString("nama","");
            return nama;
        }
    }
}