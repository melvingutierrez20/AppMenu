package com.example.appmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Opciones extends AppCompatActivity {


    ImageView img_categoria, img_ordenes, img_historial, img_productos, btnSalir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        img_categoria = findViewById(R.id.imgCategoria);
        img_ordenes = findViewById(R.id.imgOrden);
        img_historial = findViewById(R.id.imgHistorial);
        img_productos = findViewById(R.id.imgProductos);
        btnSalir = findViewById(R.id.btnSalir);

        img_categoria.setOnClickListener(v->startActivity(new Intent(Opciones.this, CategoriasActivity.class)));
        img_ordenes.setOnClickListener(v->startActivity(new Intent(Opciones.this, PlatillosActivity.class)));
        img_historial.setOnClickListener(v->startActivity(new Intent(Opciones.this, Historial.class)));
        img_productos.setOnClickListener(v->startActivity(new Intent(Opciones.this, PlatillosCategoria.class)));


        findViewById(R.id.btnSalir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra completamente la aplicación
                System.exit(0);
            }
        });



    }
}