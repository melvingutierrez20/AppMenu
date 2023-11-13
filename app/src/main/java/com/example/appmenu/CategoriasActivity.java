package com.example.appmenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmenu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoriasActivity extends AppCompatActivity {

    FloatingActionButton addCategoriaBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);


        RecyclerView recyclerView = findViewById(R.id.rvCategorias);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);


        recyclerView.setLayoutManager(layoutManager);
        addCategoriaBtn = findViewById(R.id.AddCategoriaBtn);
        addCategoriaBtn.setOnClickListener(v->startActivity(new Intent(CategoriasActivity.this, AgregarCategoriaActivity.class)));


    }
}
