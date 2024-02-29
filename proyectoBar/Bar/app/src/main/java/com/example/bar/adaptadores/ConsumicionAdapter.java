package com.example.bar.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.R;

import java.util.ArrayList;

import modelo.Consumicion;
import modelo.Menu;

public class ConsumicionAdapter extends RecyclerView.Adapter<ConsumicionAdapter.ConsumicionViewHolder> {
    private ArrayList<Consumicion> listaConsumiciones;
    //    posicion en la lista del elemento el cual el ultimo botón se ha clickado
    public Button btn;
    //    método para asignar al boton
    private ConsumicionAdapter.OnItemClickListener listener;

    // Constructor
    public ConsumicionAdapter(ArrayList<Consumicion> listaConsumiciones) {
        this.listaConsumiciones = listaConsumiciones;
    }

    // ViewHolder
    public class ConsumicionViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreConsumicion;
        public ImageView imgConsumicion;
        public TextView tvPrecioConsumicion;
        public TextView cantidadConsumicion;
        public Button btn;


        public ConsumicionViewHolder(View itemView) {
            super(itemView);
//            elementos de cada item
            tvNombreConsumicion = itemView.findViewById(R.id.nombreConsumicion);
            imgConsumicion = itemView.findViewById(R.id.imagenConsumicion);
            tvPrecioConsumicion = itemView.findViewById(R.id.precioConsumicion);
            cantidadConsumicion = itemView.findViewById(R.id.cantidadConsumicion);
            btn = itemView.findViewById(R.id.btnEliminarConsumicion);


//            establecer onclick
            btn.setOnClickListener(v -> {

                int position = getAdapterPosition();


                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClickConsumicion(position);
                    System.out.println("hola");
                }else {
                    System.out.println("es nyl");
                }
            });

        }
    }

    @Override
    public ConsumicionAdapter.ConsumicionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        se lee el xml de cadamenu ya que va a ser el molde para cada elemento del recyclerview
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cadaconsumicion, parent, false);
        return new ConsumicionAdapter.ConsumicionViewHolder(v);
    }


    public void setOnItemClickListener(ConsumicionAdapter.OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ConsumicionAdapter.ConsumicionViewHolder holder, int position) {
        Consumicion consumicion = listaConsumiciones.get(position);
        holder.tvNombreConsumicion.setText(consumicion.getNombre());
        holder.tvPrecioConsumicion.setText(String.valueOf(consumicion.getPrecio()));
        String cantidad = consumicion.getCantidad()+" unidades";
        holder.cantidadConsumicion.setText(cantidad);

    }

    @Override
    public int getItemCount() {
        return listaConsumiciones.size();
    }
    public interface OnItemClickListener {
        void onItemClickConsumicion(int position);
    }

}
