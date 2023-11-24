package com.example.appmenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Iterator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PlatillosCategoria extends AppCompatActivity implements AdaptadorComprarPlatillo.OnItemClickListener {

    private RecyclerView rvPlatillos;
    private AdaptadorComprarPlatillo adaptadorComprarPlatillo;
    private AdaptadorPlatillo AdaptadorPlatillo;

    private List<ProductoModel> productosSeleccionadosList; // Nueva lista para rastrear productos seleccionados
    private List<ProductoModel> platilloList;
    private TextView tvCuentaTotal;
    private double cuentaTotal = 0.0;
    private ImageButton carro;
    SearchView searchViewP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);



        // Inicializar la lista de platillos
        platilloList = new ArrayList<>();

        // Obtener la referencia del RecyclerView
        rvPlatillos = findViewById(R.id.rvPlatillos);

        // Configurar el LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPlatillos.setLayoutManager(layoutManager);

        // Crear una instancia del nuevo adaptador (AdaptadorComprarPlatillo) y establecerlo en el RecyclerView
        adaptadorComprarPlatillo = new AdaptadorComprarPlatillo(platilloList, this);
        adaptadorComprarPlatillo.setOnItemClickListener(this); // Establecer el listener
        rvPlatillos.setAdapter(adaptadorComprarPlatillo);
        searchViewP = findViewById(R.id.searchView);

        AdaptadorPlatillo = new AdaptadorPlatillo(platilloList, this);
        rvPlatillos.setLayoutManager(new LinearLayoutManager(this));
        rvPlatillos.setAdapter(AdaptadorPlatillo);
       searchViewP.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {return false;}

           @Override
           public boolean onQueryTextChange(String newText) {
               BuscarPlatillo(newText);
               return false;
           }
       });

        // Obtener la referencia del TextView para mostrar la cuenta total
        tvCuentaTotal = findViewById(R.id.tvCuentaTotal);
        carro = findViewById(R.id.carro);

        // Inicializar la lista de productos seleccionados
        productosSeleccionadosList = new ArrayList<>();

        // Cargar los platillos
        cargarPlatillos();

        carro.setOnClickListener(v->{
            // Al hacer clic en el botón "carro", enviar la cuenta total a la actividad Cuentas
            Intent intent = new Intent(PlatillosCategoria.this, Cuentas.class);
            intent.putExtra("cuentaTotal", cuentaTotal);
            intent.putExtra("productosSeleccionados", serializeProductoModels(productosSeleccionadosList));
            startActivity(intent);
                });


    }

    private String serializeProductoModels(List<ProductoModel> productoModels) {
        Gson gson = new Gson();
        return gson.toJson(productoModels);
    }
    void BuscarPlatillo(String searchText){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        platilloList.clear();
        buscarCampo(db.collection("products").whereEqualTo("nombreProducto", searchText));
        buscarCampo(db.collection("products").whereEqualTo("nombreCategoria",searchText));

    }

    private void buscarCampo(Query query) {
        query.get().addOnSuccessListener(queryDocumentSnapshots ->{
           for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
               ProductoModel ProductoModel = documentSnapshot.toObject(ProductoModel.class);
               platilloList.add(ProductoModel);
           }
           AdaptadorPlatillo.notifyDataSetChanged();
        }).addOnFailureListener(e->{
            Toast.makeText(this, "Error al mostrar", Toast.LENGTH_SHORT).show();
        });
    }



    // Este método carga los platillos
    private void cargarPlatillos() {
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
                adaptadorComprarPlatillo.notifyDataSetChanged(); // Cambiado a adaptadorComprarPlatillo
            } else {
                // Manejar el error
                Toast.makeText(PlatillosCategoria.this, "Error al cargar los platillos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Implementación del método onItemClick
    @Override
    public void onItemClick(ProductoModel producto) {

        // Agregar el producto directamente a la lista
        productosSeleccionadosList.add(producto);

        // Sumar el precio del producto a la cuenta total
        cuentaTotal += producto.getPrecio();

        // Actualizar el TextView con la nueva cuenta total
        tvCuentaTotal.setText(String.format("Cuenta Total: $%.2f", cuentaTotal));

        // Mostrar un mensaje de éxito
        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCarritoClick(ProductoModel producto) {

    }


}
