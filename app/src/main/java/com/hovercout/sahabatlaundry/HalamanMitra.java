package com.hovercout.sahabatlaundry;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.api.LoginApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.mitra.layanan.LayananFragment;
import com.hovercout.sahabatlaundry.mitra.profil.ProfilFragment;
import com.hovercout.sahabatlaundry.mitra.proses.ProsesFragment;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HalamanMitra extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Session session;
    ImageView imageView;
    TextView tvNamaMitra, tvAlamat;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_mitra);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            confirmLogout();
            return true;
        });

        View tampil =  navigationView.getHeaderView(0);
        tvNamaMitra = tampil.findViewById(R.id.tvNamaMitra);
        tvAlamat = tampil.findViewById(R.id.tvAlamat);
        imageView = tampil.findViewById(R.id.imageView);

        session = new Session(HalamanMitra.this);
        String namaMitra = session.getNama_mitra();
        String alamat = session.getAlamat();
        String foto = session.getFoto();

        Picasso.get().load(foto).fit().centerCrop().transform(new CropCircleTransformation()).into(imageView);

        tvNamaMitra.setText(namaMitra);
        tvAlamat.setText(alamat);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_pesanan, R.id.nav_layanan, R.id.nav_profil, R.id.nav_proses, R.id.nav_history, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent data = getIntent();
        String kodeHalaman = data.getStringExtra("kodeHalaman");

        if(kodeHalaman.equals("TAMBAHLAYANAN")){
            LayananFragment layananFragment = new LayananFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,layananFragment);
            ft.commit();
        }if(kodeHalaman.equals("UBAHLAYANAN")){
            LayananFragment layananFragment = new LayananFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,layananFragment);
            ft.commit();
        }if(kodeHalaman.equals("SELESAI")){
            ProsesFragment prosesFragment = new ProsesFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,prosesFragment);
            ft.commit();
        }if(kodeHalaman.equals("PROFIL")){
            ProfilFragment profilFragment = new ProfilFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,profilFragment);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Kosong
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private static class Session {

        private SharedPreferences prefs;

        private Session(Context ctx) {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        private void setNama_mitra(String namaMitra) {
            prefs.edit().putString("namaMitra", namaMitra).apply();
        }

        private void setEmail(String email) {
            prefs.edit().putString("email", email).apply();
        }

        private String getNama_mitra() {
            String namaMitra = prefs.getString("namaMitra", "");
            return namaMitra;
        }

        private String getAlamat() {
            String alamat = prefs.getString("alamat", "");
            return alamat;
        }

        private String getFoto() {
            String foto = prefs.getString("foto", "");
            return foto;
        }

        private String getId() {
            String id = prefs.getString("id", "");
            return id;
        }
    }

    public void confirmLogout() {
        session = new Session(HalamanMitra.this);
        String id = session.getId();

        AlertDialog.Builder builder = new AlertDialog.Builder(HalamanMitra.this);
        builder.setCancelable(false);
        builder.setMessage("Apakah anda yakin untuk logout?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loading = new ProgressDialog(HalamanMitra.this);
                loading.setTitle("Harap Menunggu");
                loading.setMessage("Loading...");
                loading.show();

                LoginApi api = Retroserver.getClient().create(LoginApi.class);
                Call<ResponseModel> logoutMitra = api.logoutMitra(id);
                logoutMitra.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        loading.hide();

                        session = new Session(HalamanMitra.this);
                        session.setNama_mitra("");
                        session.setEmail("");
                        Intent pindah = new Intent(HalamanMitra.this, MenuUtama.class);
                        startActivity(pindah);
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(HalamanMitra.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
