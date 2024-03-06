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
import modelo.MenuMeter;

public class MenuMeterAdapter extends RecyclerView.Adapter<MenuMeterAdapter.MenuMeterViewHolder> {
    private ArrayList<MenuMeter> menusMeter;
    //    posicion en la lista del elemento el cual el ultimo botón se ha clickado
    public Button btn;
    //    método para asignar al boton
    private MenuMeterAdapter.OnItemClickListener listener;

    // Constructor
    public MenuMeterAdapter(ArrayList<MenuMeter> listaMenuMeter) {
        this.menusMeter = listaMenuMeter;
    }

    // ViewHolder
    public class MenuMeterViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreMenuMeter;
        public ImageView imgMenuMeter;
        public TextView tvPrecioMenuMeter;
        public TextView cantidadMenuMeter;
        public Button btn;


        public MenuMeterViewHolder(View itemView) {
            super(itemView);
//            elementos de cada item
            tvNombreMenuMeter = itemView.findViewById(R.id.nombreMenuMeter);
            imgMenuMeter = itemView.findViewById(R.id.imagenMenuMeter);
            tvPrecioMenuMeter = itemView.findViewById(R.id.precioMenuMeter);
            cantidadMenuMeter = itemView.findViewById(R.id.cantidadMenuMeter);
            btn = itemView.findViewById(R.id.btnEliminarMenuMeter);


//            establecer onclick
            btn.setOnClickListener(v -> {

                int position = getAdapterPosition();


                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.eliminar(position);
                    System.out.println("hola");
                }else {
                    System.out.println("es nyl");
                }
            });
            tvNombreMenuMeter.setOnClickListener(v -> {

                int position = getAdapterPosition();


                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.ver(position);
                    System.out.println("hola");
                }else {
                    System.out.println("es nyl");
                }
            });
            imgMenuMeter.setOnClickListener(v -> {

                int position = getAdapterPosition();


                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.ver(position);
                    System.out.println("hola");
                }else {
                    System.out.println("es nyl");
                }
            });

        }
    }

    @Override
    public MenuMeterAdapter.MenuMeterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        se lee el xml de cadamenu ya que va a ser el molde para cada elemento del recyclerview
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cadamenumeter, parent, false);
        return new MenuMeterAdapter.MenuMeterViewHolder(v);
    }


    public void setOnItemClickListener(MenuMeterAdapter.OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(MenuMeterAdapter.MenuMeterViewHolder holder, int position) {
        MenuMeter menuMeter = menusMeter.get(position);

        holder.tvNombreMenuMeter.setText("Menú del día");
        holder.tvPrecioMenuMeter.setText(String.valueOf(menuMeter.getPrecio() * menuMeter.getCantidad()));
        String cantidad = menuMeter.getCantidad()+" unidades";
        holder.cantidadMenuMeter.setText(cantidad);

    }

    @Override
    public int getItemCount() {
        return menusMeter.size();
    }
    public interface OnItemClickListener {
        void eliminar(int position);
        void ver(int position);
    }

}
