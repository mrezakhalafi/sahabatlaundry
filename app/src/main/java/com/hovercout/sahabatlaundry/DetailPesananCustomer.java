package com.hovercout.sahabatlaundry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hovercout.sahabatlaundry.api.PesananApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.ItemModel;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPesananCustomer extends AppCompatActivity {

    ImageView btnBack;
    Button btnCancel, btnTelepon;
    TextView tvNamaLaundry, tvHarga, tvTotalTransaksi, tvPesanan, tvOngkir;
    ProgressDialog loading;

    List<ItemModel> itemItem = new ArrayList<>();

    Locale localeID = new Locale("in", "ID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        btnBack = findViewById(R.id.btnBack);
        btnCancel = findViewById(R.id.btnCancel);
        tvNamaLaundry = findViewById(R.id.tvNamaLaundry);
        tvHarga = findViewById(R.id.tvHarga);
        tvTotalTransaksi = findViewById(R.id.tvTotalTransaksi);
        tvPesanan = findViewById(R.id.tvPesanan);
        tvOngkir = findViewById(R.id.tvOngkir);
        btnTelepon = findViewById(R.id.btnTelepon);


        Intent data = getIntent();
        String id = data.getStringExtra("id");
        String nama = data.getStringExtra("namaMitra");
        String harga = data.getStringExtra("harga");
        String status = data.getStringExtra("status");
        String ongkir = data.getStringExtra("ongkir");
        String totalHarga = data.getStringExtra("totalHarga");
        String telepon = data.getStringExtra("telepon");

        btnTelepon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+telepon));
                startActivity(intent);
            }
        });

        if(status.equals("3") || status.equals("5") || status.equals("4")){
            btnCancel.setVisibility(View.GONE);
            btnTelepon.setVisibility(View.GONE);
        }else if(status.equals("2")){
            btnCancel.setBackgroundColor(R.color.abuabu);
            btnCancel.setEnabled(false);
        }

        loading = new ProgressDialog(DetailPesananCustomer.this);
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        PesananApi api = Retroserver.getClient().create(PesananApi.class);
        Call<ResponseModel> readItem = api.lihatItem(id);
        readItem.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                itemItem = response.body().getListItem();
                loading.hide();

                tvNamaLaundry.setText(nama);

                Double formatHarga = Double.parseDouble(harga);
                Double formatOngkir = Double.parseDouble(ongkir);
                Double formatTotalTransaksi = Double.parseDouble(totalHarga);

                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                tvHarga.setText(formatRupiah.format(formatHarga));
                tvOngkir.setText(formatRupiah.format(formatOngkir));
                tvTotalTransaksi.setText(formatRupiah.format(formatTotalTransaksi));

                for(int i=0;i<itemItem.size();i++) {
                    ItemModel item = itemItem.get(i);
                    tvPesanan.append(item.getNamapakaian()+" x"+item.getJumlah_pakaian()+"\n");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(DetailPesananCustomer.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete(id);
            }
        });
    }

    public void confirmDelete(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPesananCustomer.this);
        builder.setCancelable(false);
        builder.setMessage("Apakah anda yakin untuk membatalkan pesanan?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loading = new ProgressDialog(DetailPesananCustomer.this);
                loading.setTitle("Loading...");
                loading.show();

                PesananApi api = Retroserver.getClient().create(PesananApi.class);
                Call<ResponseModel> deletePesanan = api.hapusPesanan(id);
                deletePesanan.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        loading.hide();
                        Toast.makeText(DetailPesananCustomer.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                        Intent pindah = new Intent(DetailPesananCustomer.this, LaundrySaya.class);
                        startActivity(pindah);
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(DetailPesananCustomer.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
