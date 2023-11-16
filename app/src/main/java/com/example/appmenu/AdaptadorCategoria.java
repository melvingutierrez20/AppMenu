package com.example.appmenu;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AdaptadorCategoria extends RecyclerView.Adapter<AdaptadorCategoria.ViewHolder>{

    private final List<CategoriaModel> categoriaModelList;
    private final Context context;
    private AdapterView.OnItemClickListener itemClickListener;

    public AdaptadorCategoria(Context context, List<CategoriaModel> categoriaModelList) {
        this.context = context;
        this.categoriaModelList = categoriaModelList;

    }

    @NonNull
    @Override
    public AdaptadorCategoria.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriaModel categoriaM = categoriaModelList.get(position);
        holder.tvNom_Categoria.setText(categoriaM.getTitle());

        //cargar la imagen utilizando Glide
        Glide.with(context)
                .load(categoriaM.getImageUrl())
                .placeholder(R.drawable.img_9)
                .error(R.drawable.img_9)
                .into(holder.iv_Categoria);


        holder.btnBorrar.setOnClickListener(v -> {
            // Llamar al método para manejar la eliminación de la categoría desde la actividad
            ((CategoriasActivity) context).eliminarCategoria(categoriaM.getTitle());
        });
    }

    @Override
    public int getItemCount() {
        return categoriaModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_Categoria;
        ImageButton btnBorrar;
        TextView tvNom_Categoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom_Categoria = itemView.findViewById(R.id.tvNomCategoria);
            iv_Categoria = itemView.findViewById(R.id.ivCategoria);
            btnBorrar = itemView.findViewById(R.id.ibtnBorrar);

        }
    }
}
