package com.hovercout.sahabatlaundry.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.hovercout.sahabatlaundry.HalamanMitra;
import com.hovercout.sahabatlaundry.R;
import com.hovercout.sahabatlaundry.UbahLayanan;
import com.hovercout.sahabatlaundry.api.MitraApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.PakaianModel;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterLayananMitra extends RecyclerView.Adapter<AdapterLayananMitra.TampungData> {

    private Context ctx;
    private List<PakaianModel> listPakaian;
    private ProgressDialog loading;

    private Locale localeID = new Locale("in", "ID");

    public AdapterLayananMitra(Context ctx, List<PakaianModel> listPakaian){
        this.ctx = ctx;
        this.listPakaian = listPakaian;
    }

    @NonNull
    @Override
    public TampungData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampil_layanan,parent,false);
        TampungData tampungData = new TampungData(layout);

        return tampungData;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLayananMitra.TampungData holder, int position) {
        PakaianModel dataPakaian = listPakaian.get(position);

        holder.etNamaPakaian.setText(dataPakaian.getNamaPakaian());

        Double harga = Double.parseDouble(dataPakaian.getHargaPakaian());
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.etHargaPakaian.setText(formatRupiah.format(harga)+"/ item");

        Picasso.get().load(dataPakaian.getFoto()).fit().centerCrop().into(holder.ivLayanan);
        holder.dataPakaian = dataPakaian;
    }

    @Override
    public int getItemCount() {
        return listPakaian.size();
    }

    class TampungData extends RecyclerView.ViewHolder {

        TextView etNamaPakaian, etHargaPakaian;
        PakaianModel dataPakaian;
        ImageView ivLayanan;
        Button btnHapus, btnUbah;

        private TampungData(View v) {
            super(v);

            etNamaPakaian = v.findViewById(R.id.etNamaPakaian);
            etHargaPakaian = v.findViewById(R.id.etHargaPakaian);
            ivLayanan = v.findViewById(R.id.ivLayanan);
            btnHapus = v.findViewById(R.id.btnHapus);
            btnUbah = v.findViewById(R.id.btnUbahLayanan);

            btnUbah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pindah = new Intent(ctx, UbahLayanan.class);
                    pindah.putExtra("id",dataPakaian.getId());
                    pindah.putExtra("nama",dataPakaian.getNamaPakaian());
                    pindah.putExtra("harga",dataPakaian.getHargaPakaian());
                    pindah.putExtra("foto",dataPakaian.getFoto());
                    ctx.startActivity(pindah);
                }
            });

            btnHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = dataPakaian.getId();
                    confirmDelete(id);
                }
            });
        }
    }

    private void confirmDelete(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setMessage("Apakah anda yakin untuk menghapus layanan?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loading = new ProgressDialog(ctx);
                loading.setTitle("Harap Menunggu");
                loading.setMessage("Loading...");
                loading.show();

                MitraApi api = Retroserver.getClient().create(MitraApi.class);
                Call<ResponseModel> hapusJenis = api.hapusLayanan(id);
                hapusJenis.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        Toast.makeText(ctx, response.body().getPesan(), Toast.LENGTH_SHORT).show();

                        Intent pindah = new Intent(ctx,HalamanMitra.class);
                        pindah.putExtra("kodeHalaman","UBAHLAYANAN");
                        ctx.startActivity(pindah);
                        loading.hide();
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(ctx, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
