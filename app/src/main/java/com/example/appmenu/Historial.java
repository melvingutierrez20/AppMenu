package com.example.appmenu;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmenu.CuentasModel;
import com.example.appmenu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Historial extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorHistorial AdaptadorHistorial;
    private List<CuentasModel> cuentasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Inicializar la lista y el adaptador
        cuentasList = new ArrayList<>();
        AdaptadorHistorial = new AdaptadorHistorial(cuentasList);

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.rvPayments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(AdaptadorHistorial);

        // Obtener datos de Firestore
        obtenerDatosDesdeFirestore();
    }

    private void obtenerDatosDesdeFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("compras")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CuentasModel cuenta = document.toObject(CuentasModel.class);
                                cuentasList.add(cuenta);
                            }
                            AdaptadorHistorial.notifyDataSetChanged();
                        } else {
                            // Manejar errores aquí
                        }
                    }
                });
    }
}
