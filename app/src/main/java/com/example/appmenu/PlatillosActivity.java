package com.example.appmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PlatillosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platillos);

        FloatingActionButton addProductBtn = findViewById(R.id.AddCategoriaBtn);

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Iniciar la Actividad AgregarPlatillos
                Intent intent = new Intent(PlatillosActivity.this, AgregarPlatillosActivity.class);
                startActivity(intent);
            }
        });
    }
}