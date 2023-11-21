package com.example.appmenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    boolean isEditMode = true;
    private TextView tv_Titulo;

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
        tv_Titulo = findViewById(R.id.tvTitulo);

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


        //modo editar
        // Modo edit
        Intent intent = getIntent();
        if (intent.hasExtra("EDIT_MODE")) {
            isEditMode = intent.getBooleanExtra("EDIT_MODE", false);
            if (isEditMode) {
                setupEditModePlatillo();
            }
        }

    }

    private void setupEditModePlatillo() {
        tv_Titulo.setText("Editar platillo");

        String existingNombrePlatillo = getIntent().getStringExtra("nombrePlatillo");
        String existingDescPlatillo = getIntent().getStringExtra("descripcionPlatillo");
        String existingPrecioPlatillo = getIntent().getStringExtra("precioPlatillo");
        String existingCategoriaPlatillo = getIntent().getStringExtra("categoriaPlatillo");
        String existingImageUrl = getIntent().getStringExtra("imageUrlPlatillo");

        // Llenar los campos de la interfaz de usuario con los datos existentes
        nombreProducto.setText(existingNombrePlatillo);
        descripProducto.setText(existingDescPlatillo);
        precioEditText.setText(existingPrecioPlatillo);

        // Configurar el Spinner con las categorías de la base de datos
        loadCategories();
        int posicionDeLaCategoria = encontrarPosicionDeCategoria(existingCategoriaPlatillo);
        categoriaSpinner.setSelection(posicionDeLaCategoria);

        // Cargar la imagen existente utilizando Glide
        Glide.with(this)
                .load(existingImageUrl)
                .into(selecionImagen);
    }

    private int encontrarPosicionDeCategoria(String categoriaId) {
        for (int i = 0; i < categoriaList.size(); i++) {
            if (categoriaList.get(i).getId().equals(categoriaId)) {
                return i; // Devolver la posición cuando encuentres la categoría
            }
        }
        return 0; // Devolver 0 si no se encuentra ninguna coincidencia (puedes ajustar esto según tus necesidades)
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

        // Validación de campos vacíos
        if (nombre.isEmpty() || descripcion.isEmpty() || precioString.isEmpty()) {
            showMessage("Por favor, completa todos los campos");
            return;
        }

        // Validación de nombre y descripción sin números ni caracteres especiales
        if (!nombre.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]+$")) {
            showMessage("El nombre no puede contener números ni caracteres especiales");
            return;
        }

        if (!descripcion.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]+$")) {
            showMessage("La descripción no puede contener números ni caracteres especiales");
            return;
        }

        // Validación de precio solo con números, puntos y comas
        if (!precioString.matches("^\\d+(\\.\\d+)?$")) {
            showMessage("El precio solo puede contener números, puntos y comas");
            return;
        }
        // Validación de límites de caracteres
        if (nombre.length() > 20) {
            showMessage("El nombre del platillo no puede tener más de 20 caracteres");
            return;
        }

        if (descripcion.length() > 30) {
            showMessage("La descripción no puede tener más de 30 caracteres");
            return;
        }

        if (precioString.contains(".") && precioString.substring(precioString.indexOf(".") + 1).length() > 2) {
            showMessage("El precio no puede tener más de 2 decimales");
            return;
        }


        double precio = Double.parseDouble(precioString);
        CategoriaModel selectedCategory = (CategoriaModel) categoriaSpinner.getSelectedItem();

        // Validación de selección de categoría
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
        ProductoModel nuevoProducto = new ProductoModel(null, nombre, descripcion, categoriaId, precio, imageUrl, nombreCategoria);

        productsRef.add(nuevoProducto)
                .addOnSuccessListener(documentReference -> {
                    showMessage("Producto agregado exitosamente");

                    // Limpiar la pila de actividades y regresar a la actividad principal
                    Intent intent = new Intent(AgregarPlatillosActivity.this, PlatillosActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    // No es necesario llamar a finish() aquí
                })
                .addOnFailureListener(e -> showMessage("Error al agregar producto: " + e.getMessage()));
    }

}