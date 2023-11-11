package com.example.appmenu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmenu.R;

public class CategoriasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);


        RecyclerView recyclerView = findViewById(R.id.rvCategorias);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);


        recyclerView.setLayoutManager(layoutManager);


    }
}
