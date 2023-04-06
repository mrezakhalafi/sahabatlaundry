package com.hovercout.sahabatlaundry.mitra.profil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.hovercout.sahabatlaundry.R;
import com.hovercout.sahabatlaundry.UbahPasswordMitra;
import com.hovercout.sahabatlaundry.UbahProfilMitra;
import com.hovercout.sahabatlaundry.UploadFoto;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ProfilFragment extends Fragment {

    private TextView tvNamaMitra, tvEmail, tvAlamat, tvNomorHandphone;
    private Session session;

    private static final SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profil, container, false);

        TextView tvBergabung, tvJamOperasional;
        ImageView ivFoto;
        Button btnUpload, btnUbahProfil, btnUbahPassword;

        tvNamaMitra = root.findViewById(R.id.tvNamaMitra);
        tvEmail = root.findViewById(R.id.tvEmail);
        tvAlamat = root.findViewById(R.id.tvAlamat);
        tvBergabung = root.findViewById(R.id.tvBergabung);
        tvNomorHandphone = root.findViewById(R.id.tvNomorHandphone);
        ivFoto = root.findViewById(R.id.ivFoto);

        tvJamOperasional = root.findViewById(R.id.tvJamOperasional);
        btnUpload = root.findViewById(R.id.btnUpload);
        btnUbahProfil = root.findViewById(R.id.btnUbahProfil);
        btnUbahPassword = root.findViewById(R.id.btnUbahPassword);

        btnUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getContext(), UbahPasswordMitra.class);
                startActivity(pindah);
            }
        });

        btnUbahProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getContext(), UbahProfilMitra.class);
                pindah.putExtra("nama",tvNamaMitra.getText().toString());
                pindah.putExtra("alamat",tvAlamat.getText().toString());
                pindah.putExtra("email",tvEmail.getText().toString());
                String enamdua = tvNomorHandphone.getText().toString().substring(1, tvNomorHandphone.getText().length());
                pindah.putExtra("nomor_handphone",enamdua);
                pindah.putExtra("jamBuka", session.getJam_buka());
                pindah.putExtra("jamTutup", session.getJam_tutup());
                startActivity(pindah);
            }
        });

        session = new Session(getContext());
        tvNamaMitra.setText(session.getNama_mitra());
        tvEmail.setText(session.getEmail());
        tvAlamat.setText(session.getAlamat());
        tvJamOperasional.setText(session.getJam_operasional());
        tvNomorHandphone.setText(session.getNo_handphone());

        String foto = session.getFoto();
        Picasso.get().load(foto).fit().centerCrop().transform(new CropCircleTransformation()).into(ivFoto);

        String bergabung = session.getBergabung();
        String formatBergabung = parseDate(bergabung, inputDate, outputDate);

        tvBergabung.setText(formatBergabung);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getContext(), UploadFoto.class);
                startActivity(pindah);
            }
        });

        return root;
    }

    private static class Session {

        private SharedPreferences prefs;

        private Session(Context ctx) {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        private String getNama_mitra() {
            String namaMitra = prefs.getString("namaMitra","");
            return namaMitra;
        }
        private String getEmail() {
            String email = prefs.getString("email","");
            return email;
        }
        private String getAlamat() {
            String alamat = prefs.getString("alamat","");
            return alamat;
        }
        private String getNo_handphone() {
            String no_handphone = prefs.getString("no_handphone","");
            return no_handphone;
        }
        private String getBergabung() {
            String bergabung = prefs.getString("bergabung","");
            return bergabung;
        }
        private String getJam_operasional() {
            String jamOperasional = prefs.getString("jamOperasional","");
            return jamOperasional;
        }
        private String getJam_buka() {
            String jamBuka = prefs.getString("jamBuka","");
            return jamBuka;
        }
        private String getJam_tutup() {
            String jamTutup = prefs.getString("jamTutup","");
            return jamTutup;
        }
        private String getFoto() {
            String foto = prefs.getString("foto","");
            return foto;
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
