package com.hovercout.sahabatlaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashScreen extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler splashscreen = new Handler();
        splashscreen.postDelayed(new Runnable() {
            @Override
            public void run() {

                session = new Session(SplashScreen.this);
                String login_customer = session.getNama_Lengkap();
                String login_mitra = session.getNama_mitra();

                if(!login_customer.equals("")) {
                    startActivity(new Intent(SplashScreen.this, HalamanCustomer.class));
                    finish();
                }else if(!login_mitra.equals("")) {
                    Intent pindah = new Intent(SplashScreen.this, HalamanMitra.class);
                    pindah.putExtra("kodeHalaman","SPLASHSCREEN");
                    startActivity(pindah);
                    finish();
                }else{
                    startActivity(new Intent(SplashScreen.this, MenuUtama.class));
                    finish();
                }
            }
        },3000L);
    }

    private static class Session {

        private SharedPreferences prefs;

        private Session(Context ctx) {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        private String getNama_Lengkap() {
            String nama = prefs.getString("nama","");
            return nama;
        }

        private String getNama_mitra() {
            String namaMitra = prefs.getString("namaMitra","");
            return namaMitra;
        }
    }
}
