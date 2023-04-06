package com.hovercout.sahabatlaundry.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hovercout.sahabatlaundry.DetailPesananMitra;
import com.hovercout.sahabatlaundry.R;
import com.hovercout.sahabatlaundry.model.PesananModel;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterPesananMitra extends RecyclerView.Adapter<AdapterPesananMitra.TampungData> {

    private Context ctx;
    private List<PesananModel> listPesanan;

    private static final SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
    private static final SimpleDateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy (HH:mm)", Locale.US);
    private Locale localeID = new Locale("in", "ID");

    public AdapterPesananMitra(Context ctx, List<PesananModel> listPesanan){
        this.ctx = ctx;
        this.listPesanan = listPesanan;
    }

    @NonNull
    @Override
    public TampungData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampil_pesanan_mitra,parent,false);
        TampungData tampungData = new TampungData(layout);

        return tampungData;
    }

    @Override
    public void onBindViewHolder(@NonNull TampungData holder, int position) {
        PesananModel dataPesanan = listPesanan.get(position);
        holder.etNamaCustomer.setText(dataPesanan.getNama_lengkap());

        Double harga = Double.parseDouble(dataPesanan.getTotalHarga());
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.etHarga.setText(formatRupiah.format(harga));

        String waktu = dataPesanan.getTgl_pesanan();
        String formatBergabung = parseDate(waktu, inputDate, outputDate);
        holder.etTglTransaksi.setText(formatBergabung);

        if(dataPesanan.getStatus().equals("1")){
            holder.etStatus.setText("Menunggu Konfirmasi");
        }else if(dataPesanan.getStatus().equals("2")){
            holder.etStatus.setText("Proses Berlangsung");
            holder.etStatus.setTextColor(Color.parseColor("#15910D"));
        }else if(dataPesanan.getStatus().equals("3")){
            holder.etStatus.setText("Ditolak");
            holder.etStatus.setTextColor(Color.parseColor("#9A0606"));
        }else{
            holder.etStatus.setText("Selesai");
            holder.etStatus.setTextColor(Color.parseColor("#15910D"));
        }

        holder.dataPesanan = dataPesanan;
    }

    @Override
    public int getItemCount() {
        return listPesanan.size();
    }

    class TampungData extends RecyclerView.ViewHolder {

        TextView etNamaCustomer, etHarga, etStatus, etTglTransaksi;
        Button btnDetail;
        PesananModel dataPesanan;

        private TampungData(View v) {
            super(v);

            etNamaCustomer = v.findViewById(R.id.etNamaCustomer);
            etHarga = v.findViewById(R.id.etHarga);
            btnDetail = v.findViewById(R.id.btnDetail);
            etStatus = v.findViewById(R.id.etStatus);
            etTglTransaksi = v.findViewById(R.id.etTglTransaksi);

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pindah = new Intent(ctx, DetailPesananMitra.class);
                    pindah.putExtra("id", dataPesanan.getId());
                    pindah.putExtra("nama", dataPesanan.getNama_lengkap());
                    pindah.putExtra("alamat", dataPesanan.getAlamat());
                    pindah.putExtra("harga", dataPesanan.getHarga());
                    pindah.putExtra("ongkir", dataPesanan.getOngkir());
                    pindah.putExtra("totalHarga", dataPesanan.getTotalHarga());
                    pindah.putExtra("latitude", dataPesanan.getLatitude());
                    pindah.putExtra("longtitude", dataPesanan.getLongtitude());
                    pindah.putExtra("status", dataPesanan.getStatus());
                    pindah.putExtra("token", dataPesanan.getToken());
                    pindah.putExtra("telepon", dataPesanan.getNomor_handphone());
                    ctx.startActivity(pindah);
                }
            });
        }
    }

    private static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }
}
