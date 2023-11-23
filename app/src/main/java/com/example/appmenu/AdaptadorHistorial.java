package com.example.appmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmenu.CuentasModel;

import java.util.List;

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.ViewHolder> {

    private List<CuentasModel> cuentasList;

    public AdaptadorHistorial(List<CuentasModel> cuentasList) {
        this.cuentasList = cuentasList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itempago, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CuentasModel cuenta = cuentasList.get(position);

        // Configurar las vistas del ViewHolder con los datos de la cuenta
        holder.nombre.setText(cuenta.getNombrePlato());
        holder.precioTextView.setText(String.valueOf("$" + cuenta.getTotalPedido()));
        holder.codigoTextView.setText(cuenta.getCodigoCuenta());
    }

    @Override
    public int getItemCount() {
        return cuentasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView precioTextView;
        TextView codigoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            precioTextView = itemView.findViewById(R.id.precio);
            codigoTextView = itemView.findViewById(R.id.codigo);
        }
    }
}
