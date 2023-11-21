package com.example.appmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlatillosActivity extends AppCompatActivity {
    private RecyclerView rvPlatillos;
    private AdaptadorPlatillo AdaptadorPlatillo;
    private List<ProductoModel> platilloList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platillos);
        // Inicializa el contexto para edicion es necesario y asi me abre el otro intent
        context = this;

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

        // Configurar el Listener para editar el platillos

        // Configurar el Listener para la eliminación de platillos
        AdaptadorPlatillo.setOnItemClickListener(new AdaptadorPlatillo.OnItemClickListener() {

            public void onDeleteClick(int position) {
                // Obtener idProducto
                String idProducto = obtenerIdProducto(position);

                // Llamar el metodo de eliminar
                eliminarPlatillo(idProducto);
            }
            @Override
            public void onEditClick(int position) {
                try {
                    ProductoModel platillo = platilloList.get(position);
                    abrirActividadEdicion(platillo);
                } catch (Exception e) {
                    // Manejar la excepción
                    e.printStackTrace();
                    Log.e("PlatillosActivity", "Excepción al abrir la actividad de edición: " + e.getMessage());
                    Toast.makeText(PlatillosActivity.this, "Error al abrir la actividad de edición", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Cargar los platillos desde Firebase
        cargarPlatillos();
    }
    private String obtenerIdProducto(int position) {
        ProductoModel producto = platilloList.get(position);
        return producto.getIdProducto();
    }

    void cargarPlatillos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("products");

        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                platilloList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ProductoModel platillo = document.toObject(ProductoModel.class);
                    platillo.setIdProducto(document.getId()); // Establecer el ID del producto
                    platilloList.add(platillo);
                }
                AdaptadorPlatillo.notifyDataSetChanged();
            } else {
                // Manejar el error
                Toast.makeText(PlatillosActivity.this, "Error al cargar los platillos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirActividadEdicion(ProductoModel platillo) {
        Intent editarIntent = new Intent(context, AgregarPlatillosActivity.class);
        editarIntent.putExtra("EDIT_MODE", true);
        editarIntent.putExtra("id", platillo.getIdProducto());
        editarIntent.putExtra("nombrePlatillo", platillo.getNombreProducto());
        editarIntent.putExtra("descripcionPlatillo", platillo.getDescripcion());
        editarIntent.putExtra("categoriaPlatillo", platillo.getCategoriaId());
        editarIntent.putExtra("precioPlatillo", platillo.getPrecio());
        editarIntent.putExtra("imageUrlPlatillo", platillo.getImageUrl());
        startActivity(editarIntent);
    }



    // Método para eliminar el producto en Firebase Firestore
    void eliminarPlatillo(String idProducto) {
        // Utilizar un iterador para evitar ConcurrentModificationException
        Iterator<ProductoModel> iterator = platilloList.iterator();
        while (iterator.hasNext()) {
            ProductoModel producto = iterator.next();
            if (producto.getIdProducto() != null && producto.getIdProducto().equals(idProducto)) {
                // Utilizar el iterador para eliminar el elemento de la lista
                iterator.remove();
                break;  // No necesitas seguir iterando después de encontrar y eliminar el elemento
            }
        }

        // Eliminar el producto de Firestore
        FirebaseFirestore.getInstance().collection("products").document(idProducto).delete()
                .addOnSuccessListener(aVoid -> {
                    // Éxito al eliminar en Firestore
                    Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                    // Actualizar la lista y notificar al adaptador después de eliminar de Firestore
                    cargarPlatillos();
                })
                .addOnFailureListener(e -> {
                    // Error al eliminar en Firestore
                    Toast.makeText(this, "Error al eliminar el producto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
