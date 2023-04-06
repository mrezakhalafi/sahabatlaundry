package com.hovercout.sahabatlaundry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.adapter.AdapterPakaian;
import com.hovercout.sahabatlaundry.api.MitraApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.PakaianModel;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMitra extends AppCompatActivity {

    TextView tvNama, tvAlamat, tvJamOperasional, tvTelepon;
    RecyclerView tampilJenis;
    RecyclerView.LayoutManager layoutJenis;
    RecyclerView.Adapter adapterJenis;
    ImageView ivMitraBig;

    List<PakaianModel> itemPakaian = new ArrayList<>();

    ArrayList<String> idPesanan;
    ArrayList<String> pesanan;

    TextView tvTotalItem, tvTotalHarga;
    Button btnLanjut;
    ImageView btnBack;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mitra);

        tvNama = findViewById(R.id.tvNama);
        tvAlamat = findViewById(R.id.tvAlamat);
        tvJamOperasional = findViewById(R.id.tvJamOperasional);
        ivMitraBig = findViewById(R.id.ivMitraBig);

        tvTotalItem = findViewById(R.id.tvTotalItem);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);

        tvTelepon = findViewById(R.id.tvTelepon);

        btnLanjut = findViewById(R.id.btnLanjut);
        btnLanjut.setVisibility(View.GONE);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loading = new ProgressDialog(DetailMitra.this);
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        idPesanan = new ArrayList<>();
        pesanan = new ArrayList<>();

        tampilJenis = findViewById(R.id.tampilJenis);
        layoutJenis = new LinearLayoutManager(DetailMitra.this, RecyclerView.VERTICAL, false);
        tampilJenis.setLayoutManager(layoutJenis);

        Intent pindah = getIntent();
        String id = pindah.getStringExtra("id");
        String nama = pindah.getStringExtra("nama");
        String alamat = pindah.getStringExtra("alamat");
        String jamOperasional = pindah.getStringExtra("jamOperasional");
        String foto = pindah.getStringExtra("foto");

        String ongkir = pindah.getStringExtra("jauh");

        String token = pindah.getStringExtra("token");

        String telepon = pindah.getStringExtra("telepon");

        tvNama.setText(nama);
        tvAlamat.setText(alamat);
        tvJamOperasional.setText(jamOperasional);
        tvTelepon.setText(telepon);

        Picasso.get().load(foto).fit().centerCrop().into(ivMitraBig);

        btnLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(DetailMitra.this, HalamanKonfirmasi.class);
                pindah.putExtra("idPesanan", idPesanan);
                pindah.putExtra("Pesanan",pesanan);
                pindah.putExtra("idLaundry",id);
                pindah.putExtra("totalBiaya", tvTotalHarga.getText().toString());
                pindah.putExtra("namaLaundry",tvNama.getText().toString());
                pindah.putExtra("jauh",ongkir);
                pindah.putExtra("token",token);
                startActivity(pindah);
            }
        });

        MitraApi api = Retroserver.getClient().create(MitraApi.class);
        Call<ResponseModel> tampilJenisPakaian = api.lihatLayanan(id);
        tampilJenisPakaian.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                itemPakaian = response.body().getListPakaian();
                adapterJenis = new AdapterPakaian(DetailMitra.this, itemPakaian, tvTotalItem, tvTotalHarga, btnLanjut, idPesanan, pesanan);
                tampilJenis.setAdapter(adapterJenis);
                adapterJenis.notifyDataSetChanged();
                loading.hide();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(DetailMitra.this,"Koneksi Gagal",Toast.LENGTH_LONG).show();
            }
        });

    }
}
