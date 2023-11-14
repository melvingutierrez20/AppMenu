package com.example.appmenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmenu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class CategoriasActivity extends AppCompatActivity {

    FloatingActionButton addCategoriaBtn;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    AdaptadorCategoria ac;
    private List<CategoriaModel> categoriaModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        //declaracion  de variables para pasarle los elementos de la vista y librerias
        recyclerView = findViewById(R.id.rvCategorias);
        addCategoriaBtn = findViewById(R.id.AddCategoriaBtn);
        categoriaModelList = new ArrayList<>();
        ac = new AdaptadorCategoria(this, categoriaModelList);
        //layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ac);
        addCategoriaBtn.setOnClickListener(v->startActivity(new Intent(CategoriasActivity.this, AgregarCategoriaActivity.class)));

        cargaDatos();
    }

    void cargaDatos(){
        FirebaseFirestore.getInstance().collection("categoria")
                .addSnapshotListener((value, error)->{
                    if(error != null){
                        return;
                    }

                    if(value != null){
                        categoriaModelList.clear();
                        for(QueryDocumentSnapshot document : value){
                            CategoriaModel categoriaModel = document.toObject(CategoriaModel.class);
                            categoriaModelList.add(categoriaModel);
                        }
                        ac.notifyDataSetChanged();
                    }
                });
    }
}
