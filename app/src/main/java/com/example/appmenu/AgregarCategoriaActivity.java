package com.example.appmenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgregarCategoriaActivity extends AppCompatActivity {


    ImageView saveButton, categoryImage;
    EditText etNomCategoria;
    Button selectImgCat;
    TextView tv_Titulo;


    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private  final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        imageUri = result;
                        categoryImage.setImageURI(imageUri);
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_categoria);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        etNomCategoria = findViewById(R.id.etNomCategoria);
        saveButton = findViewById(R.id.save);
        categoryImage = findViewById(R.id.category_image);
        selectImgCat = findViewById(R.id.select_image_btn);

        etNomCategoria.setFilters(new InputFilter[]{new LetterInputFilter(), new InputFilter.LengthFilter(15)});


        selectImgCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verificar si el EditText no está vacío
                String nombreCategoria = etNomCategoria.getText().toString().trim();
                if (TextUtils.isEmpty(nombreCategoria)) {
                    Toast.makeText(AgregarCategoriaActivity.this, "Ingrese un nombre de categoría", Toast.LENGTH_SHORT).show();
                } else {
                    // Verificar la longitud del nombre de la categoría
                    if (nombreCategoria.length() > 15) {
                        Toast.makeText(AgregarCategoriaActivity.this, "El nombre no puede tener más de 15 caracteres", Toast.LENGTH_SHORT).show();
                    } else {
                        //  acción de guardar
                        // ...
                        // Toast.makeText(AgregarCategoriaActivity.this, "Guardado exitosamente", Toast.LENGTH_SHORT).show();}
                        addToFirestore();
                    }
                }
            }
        });
    }

    private void openImage() {
        // Lanzar el selector de imágenes utilizando ActivityResultLauncher
        imagePickerLauncher.launch("image/*");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            imageUri = data.getData();
            categoryImage.setImageURI(imageUri);
        }
    }

    void addToFirestore() {
        String title = etNomCategoria.getText().toString();
        if (imageUri != null) {
            StorageReference imgRef = storageRef.child("images/*" + System.currentTimeMillis() + ".jpg");
            imgRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    saveCategoryData(title, imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AgregarCategoriaActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();

                        }
                    });

        } else {
            // No hay imagen, solo guardar los datos de texto
            saveCategoryData(title, null);
        }


    }

    void saveCategoryData(String title, String imageUrl) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        if (imageUrl != null) {
            data.put("imageUrl", imageUrl);
        }
        firestore.collection("categoria")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AgregarCategoriaActivity.this, "Se guardo con exito!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarCategoriaActivity.this, "Error no se guardo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Clase para permitir solo letras en el EditText
    public static class LetterInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isLetter(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    }
}
