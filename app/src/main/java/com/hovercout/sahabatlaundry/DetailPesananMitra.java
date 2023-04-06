package com.hovercout.sahabatlaundry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPesananMitra extends AppCompatActivity {

    ImageView btnBack;
    Button btnMaps, btnAccept, btnDecline, btnSelesai, btnTelepon;
    ProgressDialog loading;
    TextView tvPesanan, tvNamaLengkap, tvTotalTransaksi, tvHarga, tvOngkir, tvAlamat;

    List<ItemModel> itemItem = new ArrayList<>();

    private static final SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
    private static final SimpleDateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy (HH:mm)", Locale.US);
    Locale localeID = new Locale("in", "ID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan_mitra);

        btnBack = findViewById(R.id.btnBack);
        btnMaps = findViewById(R.id.btnMaps);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnDecline);
        tvPesanan = findViewById(R.id.tvPesanan);
        tvNamaLengkap = findViewById(R.id.tvNamaLengkap);
        tvTotalTransaksi = findViewById(R.id.tvTotalTransaksi);
        tvHarga = findViewById(R.id.tvHarga);
        tvOngkir = findViewById(R.id.tvOngkir);
        tvAlamat = findViewById(R.id.tvAlamat);
        btnSelesai = findViewById(R.id.btnSelesai);
        btnTelepon = findViewById(R.id.btnTelepon);

        btnSelesai.setVisibility(View.GONE);

        Intent data = getIntent();
        String id = data.getStringExtra("id");
        String nama = data.getStringExtra("nama");
        Double formatHarga = Double.parseDouble(data.getStringExtra("harga"));
        Double formatOngkir = Double.parseDouble(data.getStringExtra("ongkir"));
        Double formatTotal = Double.parseDouble(data.getStringExtra("totalHarga"));
        String alamat = data.getStringExtra("alamat");
        String latitude = data.getStringExtra("latitude");
        String longtitude = data.getStringExtra("longtitude");
        String status = data.getStringExtra("status");
        String token = data.getStringExtra("token");
        String telepon = data.getStringExtra("telepon");

        Log.d("TES",""+token);

        if(status.equals("2")){
            btnAccept.setVisibility(View.GONE);
            btnDecline.setVisibility(View.GONE);
            btnSelesai.setVisibility(View.VISIBLE);
        }

        if(status.equals("3") || status.equals("5")){
            btnAccept.setVisibility(View.GONE);
            btnDecline.setVisibility(View.GONE);
            btnMaps.setVisibility(View.GONE);
            btnTelepon.setVisibility(View.GONE);
        }

        tvNamaLengkap.setText(nama);
        tvAlamat.setText(alamat);

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        tvTotalTransaksi.setText(formatRupiah.format(formatTotal));
        tvOngkir.setText(formatRupiah.format(formatOngkir));
        tvHarga.setText(formatRupiah.format(formatHarga));

        btnTelepon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+telepon));
                startActivity(intent);
            }
        });

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPesananMitra.this);
                builder.setCancelable(false);
                builder.setMessage("Apakah anda yakin untuk menyelesaikan pesanan?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loading = new ProgressDialog(DetailPesananMitra.this);
                        loading.setTitle("Harap Menunggu");
                        loading.setMessage("Loading...");
                        loading.show();

                        PesananApi api = Retroserver.getClient().create(PesananApi.class);
                        Call<ResponseModel> selesaiMitra = api.selesaiPesanan(id,token);
                        selesaiMitra.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                loading.hide();

                                Toast.makeText(DetailPesananMitra.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();

                                Intent pindah = new Intent(DetailPesananMitra.this, HalamanMitra.class);
                                pindah.putExtra("kodeHalaman","SELESAI");
                                startActivity(pindah);
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Toast.makeText(DetailPesananMitra.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longtitude;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPesananMitra.this);
                builder.setCancelable(false);
                builder.setMessage("Apakah anda yakin untuk mengambil pesanan?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loading = new ProgressDialog(DetailPesananMitra.this);
                        loading.setTitle("Harap Menunggu");
                        loading.setMessage("Loading...");
                        loading.show();

                        PesananApi api = Retroserver.getClient().create(PesananApi.class);
                        Call<ResponseModel> terimaPesanan = api.terimaPesanan(id,token);
                        terimaPesanan.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                loading.hide();

                                Toast.makeText(DetailPesananMitra.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                Intent pindah = new Intent(DetailPesananMitra.this, HalamanMitra.class);
                                pindah.putExtra("kodeHalaman","TERIMA");
                                startActivity(pindah);
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Toast.makeText(DetailPesananMitra.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPesananMitra.this);
                builder.setCancelable(false);
                builder.setMessage("Apakah anda yakin untuk menolak pesanan?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loading = new ProgressDialog(DetailPesananMitra.this);
                        loading.setTitle("Harap Menunggu");
                        loading.setMessage("Loading...");
                        loading.show();

                        PesananApi api = Retroserver.getClient().create(PesananApi.class);
                        Call<ResponseModel> tolakPesanan = api.tolakPesanan(id,token);
                        tolakPesanan.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                loading.hide();

                                Toast.makeText(DetailPesananMitra.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                Intent pindah = new Intent(DetailPesananMitra.this, HalamanMitra.class);
                                pindah.putExtra("kodeHalaman","TOLAK");
                                startActivity(pindah);
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Toast.makeText(DetailPesananMitra.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
        });

        loading = new ProgressDialog(DetailPesananMitra.this);
        loading.setTitle("Harap Menunggu");
        loading.setMessage("Loading...");
        loading.show();

        PesananApi api = Retroserver.getClient().create(PesananApi.class);
        System.setProperty("http.keepAlive", "false");
        Call<ResponseModel> readMitra = api.lihatItem(id);
        readMitra.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                loading.hide();
                itemItem = response.body().getListItem();

                for(int i=0;i<itemItem.size();i++) {
                    ItemModel item = itemItem.get(i);
                    tvPesanan.append(item.getNamapakaian()+" x"+item.getJumlah_pakaian()+"\n");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(DetailPesananMitra.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
