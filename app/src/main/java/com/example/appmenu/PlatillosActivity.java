package com.example.appmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlatillosActivity extends AppCompatActivity {
    private RecyclerView rvPlatillos;
    private AdaptadorPlatillo AdaptadorPlatillo;
    private List<ProductoModel> platilloList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platillos);

        rvPlatillos = findViewById(R.id.rvPlatillos);
        platilloList = new ArrayList<>();
        AdaptadorPlatillo = new AdaptadorPlatillo(platilloList, this);
        rvPlatillos.setLayoutManager(new LinearLayoutManager(this));
        rvPlatillos.setAdapter(AdaptadorPlatillo);

        FloatingActionButton addProductBtn = findViewById(R.id.AddCategoriaBtn);

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Iniciar la Actividad AgregarPlatillos
                Intent intent = new Intent(PlatillosActivity.this, AgregarPlatillosActivity.class);
                startActivity(intent);
            }
        });

        // Cargar los platillos desde Firebase
        cargarPlatillos();
    }

    private void cargarPlatillos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("products");

        productsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    platilloList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ProductoModel platillo = document.toObject(ProductoModel.class);
                        platilloList.add(platillo);
                    }
                    AdaptadorPlatillo.notifyDataSetChanged();
                } else {
                    // Manejar el error
                }
            }
        });
    }
}