package com.hovercout.sahabatlaundry;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PilihMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText etAlamat;
    Double latitute, longtitude;
    Button btnGetLocation, btnConfirm;
    private Session session;
    Integer utama = 0;
    Marker a;
    private static final int MY_MAPS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_MAPS_REQUEST_CODE);
        }

        etAlamat = findViewById(R.id.etAlamat);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnConfirm = findViewById(R.id.btnConfirm);

        session = new Session(PilihMaps.this);

        btnConfirm.setVisibility(View.GONE);

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(PilihMaps.this);
                mFusedLocation.getLastLocation().addOnSuccessListener(PilihMaps.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            latitute = location.getLatitude();
                            longtitude = location.getLongitude();

                            String alamat = getAddress(latitute,longtitude);
                            etAlamat.setText(alamat);

                            LatLng posisi = new LatLng(latitute, longtitude);
                            float zoomLevel = 16.0f;

                            if (utama.equals(0)) {
                                a = mMap.addMarker(new MarkerOptions()
                                        .position(posisi)
                                        .draggable(true));
                                utama = 1;
                            }else{
                                a.remove();
                                a = mMap.addMarker(new MarkerOptions()
                                        .position(posisi)
                                        .draggable(true));
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi, zoomLevel));
                            btnConfirm.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(session.getEmail().equals("")) {
                    String alamat = etAlamat.getText().toString();
                    Intent pindah = new Intent(PilihMaps.this, Register.class);
                    pindah.putExtra("namaLengkap", "" + getIntent().getStringExtra("namaLengkap"));
                    pindah.putExtra("email", "" + getIntent().getStringExtra("email"));
                    pindah.putExtra("password", "" + getIntent().getStringExtra("password"));

                    pindah.putExtra("jamBuka",""+getIntent().getStringExtra("jamBuka"));
                    pindah.putExtra("jamTutup",""+getIntent().getStringExtra("jamTutup"));
                    pindah.putExtra("jamBuka2",""+getIntent().getStringExtra("jamBuka2"));
                    pindah.putExtra("jamTutup2",""+getIntent().getStringExtra("jamTutup2"));

                    pindah.putExtra("user",""+getIntent().getStringExtra("user"));
                    pindah.putExtra("alamat", "" + alamat);
                    session.setLatitude(Double.toString(latitute));
                    session.setLongtitude(Double.toString(longtitude));
                    pindah.putExtra("nomorHandphone", "" + getIntent().getStringExtra("nomorHandphone"));
                    startActivity(pindah);

                }else if(!session.getNama_lengkap().equals("")){
                    String alamat = etAlamat.getText().toString();
                    Intent pindah = new Intent(PilihMaps.this, UbahProfilCustomer.class);
                    pindah.putExtra("nama", "" + getIntent().getStringExtra("namaLengkap"));
                    pindah.putExtra("email", "" + getIntent().getStringExtra("email"));
                    pindah.putExtra("password", "" + getIntent().getStringExtra("password"));
                    pindah.putExtra("alamat", "" + alamat);
                    pindah.putExtra("lewatalamat","1");
                    session.setLatitude(Double.toString(latitute));
                    session.setLongtitude(Double.toString(longtitude));
                    pindah.putExtra("nomor_handphone", "" + getIntent().getStringExtra("nomorHandphone"));
                    startActivity(pindah);
                    finish();

                }else if(!session.getNama_mitra().equals("")){
                    String alamat = etAlamat.getText().toString();
                    Intent pindah = new Intent(PilihMaps.this, UbahProfilMitra.class);
                    pindah.putExtra("nama", "" + getIntent().getStringExtra("namaMitra"));
                    pindah.putExtra("email", "" + getIntent().getStringExtra("email"));
                    pindah.putExtra("password", "" + getIntent().getStringExtra("password"));
                    pindah.putExtra("alamat", "" + alamat);
                    session.setLatitude(Double.toString(latitute));
                    session.setLongtitude(Double.toString(longtitude));

                    pindah.putExtra("jamBuka", ""+getIntent().getStringExtra("jamBuka"));
                    pindah.putExtra("jamTutup",""+getIntent().getStringExtra("jamTutup"));
                    pindah.putExtra("jamBuka2",""+getIntent().getStringExtra("jamBuka2"));
                    pindah.putExtra("jamTutup2",""+getIntent().getStringExtra("jamTutup2"));

                    pindah.putExtra("nomor_handphone", "" + getIntent().getStringExtra("nomorHandphone"));
                    startActivity(pindah);
                    finish();
                }
            }
        });
    }

    private static class Session {

        private SharedPreferences prefs;

        private Session(Context ctx) {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        private String getEmail() {
            String email = prefs.getString("email", "");
            return email;
        }
        private String getNama_lengkap() {
            String nama = prefs.getString("nama", "");
            return nama;
        }
        private String getNama_mitra() {
            String namaMitra = prefs.getString("namaMitra", "");
            return namaMitra;
        }
        private void setLatitude(String latitude) {
            prefs.edit().putString("latitude", latitude).apply();
        }
        private void setLongtitude(String longtitude) {
            prefs.edit().putString("longtitude", longtitude).apply();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //Nothing
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //Nothing
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng geser = marker.getPosition();
                latitute = geser.latitude;
                longtitude = geser.longitude;

                String alamat = getAddress(latitute,longtitude);
                etAlamat.setText(alamat);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_MAPS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin akses lokasi diberikan.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Izin akses lokasi ditolak.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                if(address.getSubThoroughfare()!=null){
                    result.append(address.getThoroughfare()+" No."+address.getSubThoroughfare()+", "+address.getSubLocality());
                }else {
                    result.append(address.getThoroughfare());
                }
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }
}
