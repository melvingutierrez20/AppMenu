package com.example.appmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdaptadorPlatillo extends RecyclerView.Adapter<AdaptadorPlatillo.PlatilloViewHolder> {

    private List<ProductoModel> platilloList;
    private Context context;

    public AdaptadorPlatillo(List<ProductoModel> platilloList, Context context) {
        this.platilloList = platilloList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlatilloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_platilloitem, parent, false);
        return new PlatilloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatilloViewHolder holder, int position) {
        ProductoModel platillo = platilloList.get(position);

        holder.tvNombrePlatillo.setText(platillo.getNombreProducto());
        holder.tvDescripcionPlatillo.setText(platillo.getDescripcion());
        holder.tvCategoriaPlatillo.setText(platillo.getNombreCategoria());
        holder.tvPrecio.setText("$" + platillo.getPrecio());

        // Cargar la imagen usando Glide (puedes usar otras bibliotecas o métodos)
        Glide.with(context)
                .load(platillo.getImageUrl())
                .into(holder.ivImagen);
    }

    @Override
    public int getItemCount() {
        return platilloList.size();
    }

    public static class PlatilloViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagen;
        TextView tvNombrePlatillo, tvDescripcionPlatillo, tvCategoriaPlatillo, tvPrecio;

        public PlatilloViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvNombrePlatillo = itemView.findViewById(R.id.tvNombrePlatillo);
            tvDescripcionPlatillo = itemView.findViewById(R.id.tvDescripcionPlatillo);
            tvCategoriaPlatillo = itemView.findViewById(R.id.tvCategoriaPlatillo);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
