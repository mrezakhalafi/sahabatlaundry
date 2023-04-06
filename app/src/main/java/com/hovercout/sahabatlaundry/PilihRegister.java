package com.hovercout.sahabatlaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PilihRegister extends AppCompatActivity {

    Button btnMitra, btnCustomer;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_register);

        btnMitra = findViewById(R.id.btnMitra);
        btnCustomer = findViewById(R.id.btnCustomer);
        btnBack = findViewById(R.id.btnBack);

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(PilihRegister.this, Register.class);
                pindah.putExtra("user","1");
                startActivity(pindah);
            }
        });

        btnMitra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(PilihRegister.this, Register.class);
                pindah.putExtra("user","2");
                startActivity(pindah);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
