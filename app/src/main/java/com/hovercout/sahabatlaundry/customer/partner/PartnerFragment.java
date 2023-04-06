package com.hovercout.sahabatlaundry.customer.partner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.hovercout.sahabatlaundry.DetailMitra;
import com.hovercout.sahabatlaundry.R;
import com.hovercout.sahabatlaundry.api.MitraApi;
import com.hovercout.sahabatlaundry.api.Retroserver;
import com.hovercout.sahabatlaundry.model.MitraModel;
import com.hovercout.sahabatlaundry.model.ResponseModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<MitraModel> itemMitra = new ArrayList<>();
    private HashMap<String, HashMap> extraMarkerInfo = new HashMap<>(); //Nyimpen Var ke Intent
    private ProgressDialog loading;
    private static final int MY_MAPS_REQUEST_CODE = 100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_partner, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        loading = new ProgressDialog(getContext());
        loading.setTitle("Loading...");
        loading.show();

        if(getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_MAPS_REQUEST_CODE);
        }else {
            mMap.setMyLocationEnabled(true);
        }

        MitraApi api = Retroserver.getClient().create(MitraApi.class);
        Call<ResponseModel> tampilMitra = api.lihatMitra();
        tampilMitra.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                loading.hide();
                itemMitra = response.body().getListMitra();

                for (int i=0;i<itemMitra.size();i++) {
                    MitraModel mitra = itemMitra.get(i);

                    double latitude = Double.parseDouble(mitra.getLatitude());
                    double longtitude = Double.parseDouble(mitra.getLongtitude());
                    LatLng posisi = new LatLng(latitude, longtitude);
                    float zoomLevel = 13.0f;

                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.mapsicon);

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(posisi)
                            .icon(icon)
                            .title(mitra.getId()));

                    HashMap<String, String> data = new HashMap<>();  //Nyimpen Var ke Intent
                    data.put("id",mitra.getId());
                    data.put("nama",mitra.getNama());
                    data.put("alamat",mitra.getAlamat());
                    data.put("jamOperasional",mitra.getJam_buka().substring(0,mitra.getJam_buka().length() -3)+"-"+mitra.getJam_tutup().substring(0,mitra.getJam_tutup().length()
                            -3));
                    data.put("foto",mitra.getFoto());
                    data.put("telepon",mitra.getNomor_handphone());

                    Session session = new Session(getContext()); //Lokasi Double
                    Double latitudesaya = Double.parseDouble(session.getLatitude());
                    Double longitudesaya = Double.parseDouble(session.getLongtitude());
                    LatLng posisisaya = new LatLng(latitudesaya, longitudesaya);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisisaya, zoomLevel));


                    Location startPoint=new Location("locationA");
                    startPoint.setLatitude(Double.parseDouble(session.getLatitude()));
                    startPoint.setLongitude(Double.parseDouble(session.getLongtitude()));

                    Location endPoint=new Location("locationA");
                    endPoint.setLatitude(Double.parseDouble(mitra.getLatitude()));
                    endPoint.setLongitude(Double.parseDouble(mitra.getLongtitude()));

                    Float distance = startPoint.distanceTo(endPoint)/1000;

                    if(distance>=10 && distance <99) {
                        data.put("jauh", String.valueOf(distance).substring(0, 2));
                    }else if(distance>=10 && distance >99 && distance <1000){
                        data.put("jauh", String.valueOf(distance).substring(0, 3));
                    }else if(distance>=10 && distance >99 && distance >=1000 && distance <10000) {
                        data.put("jauh", String.valueOf(distance).substring(0, 4));
                    }else if(distance>=10 && distance >99 && distance >=1000 && distance >=10000){
                        data.put("jauh", String.valueOf(distance).substring(0, 5));
                    }else{
                        data.put("jauh", String.valueOf(distance).substring(0, 1));
                    }

                    extraMarkerInfo.put(marker.getId(),data);  //Nyimpen Var ke Intent
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getContext(),"Koneksi Gagal",Toast.LENGTH_LONG).show();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                HashMap<String, String> marker_data = extraMarkerInfo.get(marker.getId());

                Intent pindah = new Intent(getContext(),DetailMitra.class);
                    pindah.putExtra("id",marker_data.get("id"));
                    pindah.putExtra("nama",marker_data.get("nama"));
                    pindah.putExtra("alamat",marker_data.get("alamat"));
                    pindah.putExtra("jamOperasional",marker_data.get("jamOperasional"));
                    pindah.putExtra("foto",marker_data.get("foto"));
                    pindah.putExtra("jauh",marker_data.get("jauh"));
                    pindah.putExtra("telepon",marker_data.get("telepon"));
                    startActivity(pindah);
                return true;
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_MAPS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Izin akses lokasi diberikan.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Izin akses lokasi ditolak.", Toast.LENGTH_LONG).show();
            }
        }
    }
}