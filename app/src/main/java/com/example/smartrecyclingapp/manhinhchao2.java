package com.example.smartrecyclingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class manhinhchao2 extends AppCompatActivity {
    Button btnLogin;
    Button btnSigin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manhinhchao2);
        btnLogin = findViewById(R.id.btnLogin);
        btnSigin = findViewById(R.id.btnSigin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(manhinhchao2.this, manhinhlogin.class));
            }
        });
        btnSigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(manhinhchao2.this, manhinhdangky.class));
            }
        });



    }
}