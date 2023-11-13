package com.example.appmenu;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AgregarCategoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_categoria);

        EditText etNomCategoria = findViewById(R.id.etNomCategoria);
        etNomCategoria.setFilters(new InputFilter[]{new LetterInputFilter(), new InputFilter.LengthFilter(15)});

        ImageView saveButton = findViewById(R.id.save);

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
                        // Toast.makeText(AgregarCategoriaActivity.this, "Guardado exitosamente", Toast.LENGTH_SHORT).show();
                    }
                }
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
