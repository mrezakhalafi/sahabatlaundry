package com.hovercout.sahabatlaundry.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hovercout.sahabatlaundry.DetailMitra;
import com.hovercout.sahabatlaundry.R;
import com.hovercout.sahabatlaundry.model.MitraModel;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AdapterMitra extends RecyclerView.Adapter<AdapterMitra.TampungData> {

    private Context ctx;
    private List<MitraModel> listMitra;

    public AdapterMitra(Context ctx, List<MitraModel> listMitra){
        this.ctx = ctx;
        this.listMitra = listMitra;
    }

    @NonNull
    @Override
    public TampungData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tampil_mitra,parent,false);
        TampungData tampungData = new TampungData(layout);

        return tampungData;
    }

    @Override
    public void onBindViewHolder(@NonNull TampungData holder, int position) {

        Session session;
        session = new Session(ctx);

        MitraModel dataMitra = listMitra.get(position);

        Location startPoint=new Location("locationA");
        startPoint.setLatitude(Double.parseDouble(session.getLatitude()));
        startPoint.setLongitude(Double.parseDouble(session.getLongtitude()));

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(Double.parseDouble(dataMitra.getLatitude()));
        endPoint.setLongitude(Double.parseDouble(dataMitra.getLongtitude()));

        float distance = startPoint.distanceTo(endPoint)/1000;

        holder.etId.setText(dataMitra.getId());
        holder.etNamaMitra.setText(dataMitra.getNama());
        holder.etAlamatMitra.setText(dataMitra.getAlamat());
        holder.etJamOperasional.setText(dataMitra.getJam_buka().substring(0,dataMitra.getJam_buka().length() -3)+"-"+dataMitra.getJam_tutup().substring(0,dataMitra.getJam_tutup().length()
        -3));

        if(distance>=10 && distance<99){
            holder.etJauh.setTextColor(Color.parseColor("#CD2626"));
            holder.etJauh.setText(dataMitra.getJarak().toString().substring(0,5)+" KM");
            holder.ongkir = dataMitra.getJarak().toString().substring(0,2);
        }else if(distance>=10 && distance>99 && distance < 1000){
            holder.etJauh.setTextColor(Color.parseColor("#CD2626"));
            holder.etJauh.setText(dataMitra.getJarak().toString().substring(0,6)+" KM");
            holder.ongkir = dataMitra.getJarak().toString().substring(0,3);
        }else if(distance>=10 && distance>99 && distance >= 1000 && distance < 10000){
            holder.etJauh.setTextColor(Color.parseColor("#CD2626"));
            holder.etJauh.setText(dataMitra.getJarak().toString().substring(0,7)+" KM");
            holder.ongkir = dataMitra.getJarak().toString().substring(0,4);
        }else if(distance>=10 && distance>99 && distance >= 1000 && distance >= 10000 ){
            holder.etJauh.setTextColor(Color.parseColor("#CD2626"));
            holder.etJauh.setText(dataMitra.getJarak().toString().substring(0,8)+" KM");
            holder.ongkir = dataMitra.getJarak().toString().substring(0,5);
        }else{
            holder.etJauh.setText(dataMitra.getJarak().toString().substring(0,4)+" KM");
            holder.ongkir = dataMitra.getJarak().toString().substring(0,1);
            if(distance>5){
                holder.etJauh.setTextColor(Color.parseColor("#CD2626"));
            }
        }

        Picasso.get().load(dataMitra.getFoto()).fit().centerCrop().into(holder.ivMitra);
        holder.dataMitra = dataMitra;
    }

    @Override
    public int getItemCount() {
        return listMitra.size();
    }

    class TampungData extends RecyclerView.ViewHolder {

        TextView etNamaMitra, etAlamatMitra, etJamOperasional, etId, etJauh;
        MitraModel dataMitra;
        ImageView ivMitra;
        String ongkir;

        private TampungData(View v){

            super(v);

            etNamaMitra = v.findViewById(R.id.etNamaMitra);
            etAlamatMitra = v.findViewById(R.id.etAlamatMitra);
            etJamOperasional = v.findViewById(R.id.etJamOperasional);
            etJauh = v.findViewById(R.id.etJauh);
            etId = v.findViewById(R.id.etId);

            ivMitra = v.findViewById(R.id.ivMitra);
            etId.setVisibility(View.GONE);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pindah = new Intent(ctx, DetailMitra.class);
                    pindah.putExtra("id",dataMitra.getId());
                    pindah.putExtra("nama",dataMitra.getNama());
                    pindah.putExtra("alamat",dataMitra.getAlamat());
                    pindah.putExtra("jamOperasional",dataMitra.getJam_buka().substring(0,dataMitra.getJam_buka().length() -3)+"-"+dataMitra.getJam_tutup().substring(0,dataMitra.getJam_tutup().length()
                            -3));
                    pindah.putExtra("jauh",ongkir);
                    pindah.putExtra("foto",dataMitra.getFoto());
                    pindah.putExtra("telepon",dataMitra.getNomor_handphone());

                    pindah.putExtra("token",dataMitra.getToken());
                    ctx.startActivity(pindah);
                }
            });
        }
    }

    private static class Session {

        private SharedPreferences prefs;

        private Session(Context ctx) {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        private String getLatitude() {
            String latitude = prefs.getString("latitude", "");
            return latitude;
        }
        private String getLongtitude() {
            String longtitude = prefs.getString("longtitude", "");
            return longtitude;
        }
    }
}
