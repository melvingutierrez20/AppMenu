package com.example.appmenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmenu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Iterator;
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
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        ac = new AdaptadorCategoria(this, categoriaModelList);
        recyclerView.setAdapter(ac);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(ac);
        addCategoriaBtn.setOnClickListener(v->startActivity(new Intent(CategoriasActivity.this, AgregarCategoriaActivity.class)));

        cargaDatos();
    }

    void cargaDatos() {
        FirebaseFirestore.getInstance().collection("categoria")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }

                    if (value != null) {
                        categoriaModelList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            CategoriaModel categoriaModel = document.toObject(CategoriaModel.class);
                            categoriaModel.setId(document.getId()); // Establecer el ID de la categoría
                            categoriaModelList.add(categoriaModel);
                        }
                        ac.notifyDataSetChanged();
                    }
                });
    }

    void eliminarCategoria(String categoriaId) {
        // Utilizar un iterador para evitar ConcurrentModificationException
        Iterator<CategoriaModel> iterator = categoriaModelList.iterator();
        while (iterator.hasNext()) {
            CategoriaModel categoria = iterator.next();
            if (categoria.getId() != null && categoria.getId().equals(categoriaId)) {
                // Utilizar el iterador para eliminar el elemento de la lista
                iterator.remove();
                ac.notifyDataSetChanged();
                break;  // No necesitas seguir iterando después de encontrar y eliminar el elemento
            }
        }

        // Eliminar la categoría de Firestore
        FirebaseFirestore.getInstance().collection("categoria").document(categoriaId).delete()
                .addOnSuccessListener(aVoid -> {
                    // Éxito al eliminar en Firestore
                    Toast.makeText(this, "Categoría eliminada correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error al eliminar en Firestore
                    Toast.makeText(this, "Error al eliminar la categoría: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }





}
