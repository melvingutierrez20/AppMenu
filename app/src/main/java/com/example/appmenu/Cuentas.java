package com.example.appmenu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cuentas extends AppCompatActivity {

        private List<ProductoModel> productosSeleccionadosList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_cuentas);

                // Recibir datos del Intent
                Intent intent = getIntent();
                double cuentaTotal = intent.getDoubleExtra("cuentaTotal", 0.0);
                String productosSeleccionadosJson = intent.getStringExtra("productosSeleccionados");

                // Imprimir los datos en la consola
                Log.d("CuentasActivity", "Cuenta Total: " + cuentaTotal);
                Log.d("CuentasActivity", "Productos Seleccionados: " + productosSeleccionadosJson);

                // Mostrar el total en el TextView correspondiente
                TextView totalTextView = findViewById(R.id.PrecioPlato);
                totalTextView.setText(String.format("Total: $%.2f", cuentaTotal));

                if (productosSeleccionadosJson != null) {
                        productosSeleccionadosList = deserializeProductoModels(productosSeleccionadosJson);
                        mostrarProductosSeleccionados(productosSeleccionadosList);
                }

                // Configurar el botón "cancelar cuenta"
                findViewById(R.id.cancelar).setOnClickListener(v -> {
                        // Aquí, guarda la compra en la base de datos
                        guardarCompraFirestore(productosSeleccionadosList, cuentaTotal);

                        // También podrías agregar la lógica para finalizar esta actividad si es necesario
                        finish();

                        Intent intent1 = new Intent(Cuentas.this, Opciones.class);
                        startActivity(intent1);
                });
        }

        private void mostrarProductosSeleccionados(List<ProductoModel> productosSeleccionadosList) {
                // Crear una cadena de texto con los nombres de los productos separados por coma
                StringBuilder productosText = new StringBuilder();
                for (ProductoModel producto : productosSeleccionadosList) {
                        productosText.append(producto.getNombreProducto()).append(", ");
                }

                // Eliminar la última coma y espacio si la cadena no está vacía
                if (productosText.length() > 0) {
                        productosText.delete(productosText.length() - 2, productosText.length());
                }

                // Mostrar la cadena en un TextView
                TextView textView = findViewById(R.id.NombrePlato); // Asegúrate de tener un TextView en tu layout
                textView.setText(productosText.toString());
        }

        private void guardarCompraFirestore(List<ProductoModel> productosSeleccionadosList, double cuentaTotal) {
                // Crear una instancia de FirebaseFirestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Obtener solo los nombres de los productos
                List<String> nombresProductos = obtenerNombresProductos(productosSeleccionadosList);

                // Convertir la lista de nombres a una cadena separada por comas
                String productosNombres = String.join(", ", nombresProductos);

                // Crear una cadena de texto con los nombres de los productos separados por coma
                StringBuilder nombrePlato = new StringBuilder();
                for (ProductoModel producto : productosSeleccionadosList) {
                        nombrePlato.append(producto.getNombreProducto()).append(", ");
                }

                // Eliminar la última coma y espacio si la cadena no está vacía
                if (nombrePlato.length() > 0) {
                        nombrePlato.delete(nombrePlato.length() - 2, nombrePlato.length());
                }

                // Generar un código automático (puedes ajustar esto según tus necesidades)
                String codigoCuenta = "C" + System.currentTimeMillis();

                // Crear un nuevo documento en la colección "compras"
                Map<String, Object> compraData = new HashMap<>();
                compraData.put("nombrePlato", nombrePlato.toString());
                compraData.put("totalPedido", cuentaTotal);
                compraData.put("codigoCuenta", codigoCuenta);

                db.collection("compras")
                        .add(compraData)
                        .addOnSuccessListener(documentReference -> {
                                // El documento se ha agregado con éxito
                                Log.d("CuentasActivity", "Compra guardada con ID: " + documentReference.getId());
                                // Mostrar Toast de éxito
                                mostrarToast("Cuenta agregada exitosamente");
                        })
                        .addOnFailureListener(e -> {
                                // Error al agregar el documento
                                Log.w("CuentasActivity", "Error al guardar la compra", e);
                                // Mostrar Toast de error
                                mostrarToast("Error al guardar la cuenta");
                        });
        }

        private void mostrarToast(String mensaje) {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }


        private List<ProductoModel> deserializeProductoModels(String json) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<ProductoModel>>() {}.getType();
                return gson.fromJson(json, type);
        }

        private List<String> obtenerNombresProductos(List<ProductoModel> productosSeleccionadosList) {
                List<String> nombresProductos = new ArrayList<>();
                for (ProductoModel producto : productosSeleccionadosList) {
                        nombresProductos.add(producto.getNombreProducto());
                }
                return nombresProductos;
        }
}
