package com.hovercout.sahabatlaundry.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hovercout.sahabatlaundry.R;
import com.hovercout.sahabatlaundry.model.PakaianModel;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterPakaian extends RecyclerView.Adapter<AdapterPakaian.TampungData> {

    private Context ctx;
    private List<PakaianModel> listPakaian;
    private TextView tvTotalItem, tvTotalHarga;
    private Button btnLanjut;

    private ArrayList<String> idPesanan;
    private ArrayList<String> pesanan;

    private Locale localeID = new Locale("in", "ID");

    public AdapterPakaian(Context ctx, List<PakaianModel> listPakaian, TextView tvTotalItem, TextView tvTotalHarga, Button btnLanjut, ArrayList<String> idPesanan, ArrayList<String> pesanan){
        this.ctx = ctx;
        this.listPakaian = listPakaian;
        this.tvTotalItem = tvTotalItem;
        this.tvTotalHarga = tvTotalHarga;
        this.btnLanjut = btnLanjut;
        this.idPesanan = idPesanan;
        this.pesanan = pesanan;
    }

    @NonNull
    @Override
    public AdapterPakaian.TampungData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampil_pakaian,parent,false);
        AdapterPakaian.TampungData tampungData = new AdapterPakaian.TampungData(layout);
        return tampungData;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPakaian.TampungData holder, int position) {
        PakaianModel dataPakaian = listPakaian.get(position);
        holder.etNamaPakaian.setText(dataPakaian.getNamaPakaian());

        Double harga = Double.parseDouble(dataPakaian.getHargaPakaian());
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.etHargaPakaian.setText(formatRupiah.format(harga)+"/ item");

        holder.dataPakaian = dataPakaian;

        Picasso.get().load(dataPakaian.getFoto()).fit().centerCrop().into(holder.ivPakaian);
    }

    @Override
    public int getItemCount() {
        return listPakaian.size();
    }

    class TampungData extends RecyclerView.ViewHolder {

        TextView etNamaPakaian, etHargaPakaian, tvJumlah;
        PakaianModel dataPakaian;
        Button btnPlus, btnMinus;
        ImageView ivPakaian;
        private SharedPreferences prefs;

        private TampungData(View v){
            super(v);

            etNamaPakaian = v.findViewById(R.id.etNamaPakaian);
            etHargaPakaian = v.findViewById(R.id.etHargaPakaian);
            ivPakaian = v.findViewById(R.id.ivPakaian);

            btnMinus = v.findViewById(R.id.btnMinus);
            btnPlus = v.findViewById(R.id.btnPlus);
            tvJumlah = v.findViewById(R.id.tvJumlah);

            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int jumlah = Integer.parseInt(tvJumlah.getText().toString());
                    int totalItem = Integer.parseInt(tvTotalItem.getText().toString());
                    int totalHarga = Integer.parseInt(tvTotalHarga.getText().toString());

                    if(jumlah>1){
                        int minus = jumlah-1;
                        tvJumlah.setText("" +minus);
                        int itemminus = totalItem-1;
                        tvTotalItem.setText(""+itemminus);
                        int hargaminus = Integer.parseInt(dataPakaian.getHargaPakaian());
                        int totalhargaminus = totalHarga-hargaminus;
                        tvTotalHarga.setText(""+totalhargaminus);

                        prefs.edit().putInt(dataPakaian.getNamaPakaian(), minus).apply();

                    }else if(jumlah==1){
                        int minus = jumlah-1;
                        tvJumlah.setText("" +minus);
                        int itemminus = totalItem-1;
                        tvTotalItem.setText(""+itemminus);
                        int hargaminus = Integer.parseInt(dataPakaian.getHargaPakaian());
                        int totalhargaminus = totalHarga-hargaminus;
                        tvTotalHarga.setText(""+totalhargaminus);

                        pesanan.remove(dataPakaian.getNamaPakaian());
                    }

                    if(totalItem==1){
                        btnLanjut.setVisibility(View.GONE);
                    }
                }
            });


            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int jumlah = Integer.parseInt(tvJumlah.getText().toString());
                    int totalItem = Integer.parseInt(tvTotalItem.getText().toString());
                    int totalHarga = Integer.parseInt(tvTotalHarga.getText().toString());

                    int plus = jumlah+1;
                    tvJumlah.setText(""+plus);
                    int itemplus = totalItem+1;
                    tvTotalItem.setText(""+itemplus);
                    int hargaplus = Integer.parseInt(dataPakaian.getHargaPakaian());
                    int totalhargaplus = totalHarga+hargaplus;
                    tvTotalHarga.setText(""+totalhargaplus);

                    btnLanjut.setVisibility(View.VISIBLE);

                    if(pesanan.contains(dataPakaian.getNamaPakaian())){
                        Log.d("CEK","Tidak nambah Array");
                    }else{
                        idPesanan.add(dataPakaian.getId());
                        pesanan.add(dataPakaian.getNamaPakaian());
                    }
                    prefs.edit().putInt(dataPakaian.getNamaPakaian(), plus).apply(); //SET JUMLAH SESUAI NAMA
//                    Toast.makeText(ctx, ""+prefs.getInt(dataPakaian.getNamaPakaian(),0), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
