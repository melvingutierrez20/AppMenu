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

public class AdaptadorComprarPlatillo extends RecyclerView.Adapter<AdaptadorComprarPlatillo.PlatilloViewHolder> {

    private List<ProductoModel> platilloList;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(ProductoModel producto);

        void onCarritoClick(ProductoModel producto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AdaptadorComprarPlatillo(List<ProductoModel> platilloList, Context context) {
        this.platilloList = platilloList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlatilloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comprar_producto, parent, false);
        return new PlatilloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatilloViewHolder holder, int position) {
        ProductoModel platillo = platilloList.get(position);

        holder.tvNombrePlatillo.setText(platillo.getNombreProducto());
        holder.tvDescripcionPlatillo.setText(platillo.getDescripcion());
        holder.tvPrecio.setText("$" + platillo.getPrecio());

        // Cargar la imagen usando Glide (o la biblioteca que prefieras)
        Glide.with(context)
                .load(platillo.getImageUrl())
                .into(holder.ivImagen);

        // Agregar el OnClickListener para el carrito
        holder.ivAddLista.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(platillo);
                mListener.onCarritoClick(platillo);
            }
        });
    }



    @Override
    public int getItemCount() {
        return platilloList.size();
    }

    public static class PlatilloViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagen, ivAddLista;
        TextView tvNombrePlatillo, tvDescripcionPlatillo, tvPrecio;

        public PlatilloViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            ivAddLista = itemView.findViewById(R.id.ivAddLista);
            tvNombrePlatillo = itemView.findViewById(R.id.tvNombrePlatillo);
            tvDescripcionPlatillo = itemView.findViewById(R.id.tvDescripcionPlatillo);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);


        }
    }
}
