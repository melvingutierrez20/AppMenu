package com.example.appmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlatillosCategoria extends AppCompatActivity implements AdaptadorComprarPlatillo.OnItemClickListener {

    private RecyclerView rvPlatillos;
    private AdaptadorComprarPlatillo adaptadorComprarPlatillo;
    private List<ProductoModel> platilloList;
    private TextView tvCuentaTotal;
    private double cuentaTotal = 0.0;
    private ImageButton carro;

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

        // Obtener la referencia del TextView para mostrar la cuenta total
        tvCuentaTotal = findViewById(R.id.tvCuentaTotal);
        carro = findViewById(R.id.carro);

        // Cargar los platillos
        cargarPlatillos();
        carro.setOnClickListener(v->startActivity(new Intent(PlatillosCategoria.this, Cuentas.class)));
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
        // Lógica para manejar el clic en un elemento del RecyclerView
        // Aquí puedes agregar la lógica para sumar el precio del producto a la cuenta total
        cuentaTotal += producto.getPrecio();

        // Actualizar el TextView con la nueva cuenta total
        tvCuentaTotal.setText(String.format("Cuenta Total: $%.2f", cuentaTotal));

        // Opcional: Mostrar un mensaje de éxito
        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCarritoClick(ProductoModel producto) {

    }
}
