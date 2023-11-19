package com.example.appmenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarPlatillosActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nombreProducto, descripProducto, precioEditText;
    private Spinner categoriaSpinner;
    private ImageView productoImagen, selecionImagen;
    private Button chosseImageButton;

    private FirebaseAuth mAuth;
    private CollectionReference productsRef, categoriesRef;
    private StorageReference storageReference;

    private Uri imageUrl;

    private List<CategoriaModel> categoriaList = new ArrayList<>();

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_platillos);


        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        productsRef = db.collection("products");
        categoriesRef = db.collection("categoria");
        storageReference = FirebaseStorage.getInstance().getReference("product_images");

        // Inicializar vistas
        nombreProducto = findViewById(R.id.etNomPlatillo);
        descripProducto = findViewById(R.id.etDescripcionPlatillo);
        precioEditText = findViewById(R.id.etPrecio);
        categoriaSpinner = findViewById(R.id.spiCategorias);
        productoImagen = findViewById(R.id.save);
        selecionImagen = findViewById(R.id.category_image);
        chosseImageButton = findViewById(R.id.select_image_btn);

        // Configurar el Spinner con las categorías de la base de datos
        loadCategories();

        //Manejar clic en el boton Elegir Imagen
        chosseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        //Manejar clic en el boton Agregar Producto
        productoImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarProducto();
            }
        });
        //imagepicker para mostrar la imagen seleccionada
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // La lógica para manejar la imagen seleccionada
                        imageUrl = result.getData().getData();
                        selecionImagen.setImageURI(imageUrl);

                        // Cambiar el texto del botón
                        chosseImageButton.setText("Imagen seleccionada");
                    }
                });



    }



    private void showMessage(String message) {
        Toast.makeText(AgregarPlatillosActivity.this, message, Toast.LENGTH_SHORT).show();
    }



    // Método para cargar las categorías desde Firebase
    private void loadCategories(){
        categoriesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    categoriaList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CategoriaModel categoriaModel = document.toObject(CategoriaModel.class);
                        categoriaModel.setId(document.getId());
                        categoriaList.add(categoriaModel);
                    }

                    // Imprime las categorías cargadas en el log
                    for (CategoriaModel categoriaModel : categoriaList) {
                        Log.d("CATEGORIA", "ID: " + categoriaModel.getId() + ", Title: " + categoriaModel.getTitle());
                    }

                    // Configurar el adaptador del Spinner
                    ArrayAdapter<CategoriaModel> adapter = new ArrayAdapter<>(AgregarPlatillosActivity.this,
                            android.R.layout.simple_spinner_item, categoriaList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categoriaSpinner.setAdapter(adapter);
                } else {
                    showMessage("Error al cargar categorías: " + task.getException().getMessage());
                }
            }
        });


    // Después de loadCategories();
        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CategoriaModel selectedCategory = (CategoriaModel) parentView.getSelectedItem();
                if (selectedCategory != null) {
                    showMessage("Categoría seleccionada: " + selectedCategory.getTitle());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No se seleccionó nada
            }
        });


    }



    //metodo para abrir el buscador de imagenes
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(intent);
    }

    //

    private void agregarProducto() {
        String nombre = nombreProducto.getText().toString().trim();
        String descripcion = descripProducto.getText().toString().trim();
        String precioString = precioEditText.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty() || precioString.isEmpty()) {
            showMessage("Por favor, completa todos los campos");
            return;
        }

        double precio = Double.parseDouble(precioString);
        CategoriaModel selectedCategory = (CategoriaModel) categoriaSpinner.getSelectedItem();
        if (selectedCategory == null) {
            showMessage("Selecciona una categoría");
            return;
        }
        String categoriaId = selectedCategory.getId();


        if (imageUrl != null) {
            StorageReference imgRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
            imgRef.putFile(imageUrl)
                    .addOnSuccessListener(taskSnapshot -> {
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // Modifica la llamada a saveProductData para pasar el ID de la categoría
                            saveProductData(nombre, descripcion, precio, ((CategoriaModel) categoriaSpinner.getSelectedItem()).getId(), imageUrl, ((CategoriaModel) categoriaSpinner.getSelectedItem()).getTitle());
                        });
                    })
                    .addOnFailureListener(e -> showMessage("Error al subir la imagen"));
        } else {
            // Si no hay imagen, llama a saveProductData sin la URL de la imagen
            saveProductData(nombre, descripcion, precio, ((CategoriaModel) categoriaSpinner.getSelectedItem()).getId(), null, ((CategoriaModel) categoriaSpinner.getSelectedItem()).getTitle());
        }

    }

    private void saveProductData(String nombre, String descripcion, double precio, String categoriaId, String imageUrl, String nombreCategoria) {
        ProductoModel nuevoProducto = new ProductoModel(null, nombre, descripcion, categoriaId, precio, imageUrl,nombreCategoria);
        // Obtener el nombre de la categoría seleccionada

        productsRef.add(nuevoProducto)
                .addOnSuccessListener(documentReference -> {
                    showMessage("Producto agregado exitosamente");
                    finish();
                })
                .addOnFailureListener(e -> showMessage("Error al agregar producto: " + e.getMessage()));
    }


}