package com.example.mncfstaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class acceuil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        TextView btn1=findViewById(R.id.btn1);
        btn1.setOnClickListener(v -> {
            Intent intent2 = new Intent(acceuil.this,register.class);
            startActivity(intent2);
        });

        TextView btn2=findViewById(R.id.btn2);

        btn2.setOnClickListener(v -> {
            Intent intent1 = new Intent(acceuil.this,login.class);
            startActivity(intent1);
        });

    }
}