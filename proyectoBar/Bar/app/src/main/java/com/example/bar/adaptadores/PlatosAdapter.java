package com.example.bar.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

import modelo.Plato;



public class PlatosAdapter extends RecyclerView.Adapter<PlatosAdapter.PlatoViewHolder> {
    private List<Plato> listaPlatos;
    private OnItemClickListener listener;

    // Constructor
    public PlatosAdapter(List<Plato> listaPlatos) {
        this.listaPlatos = listaPlatos;
    }

    // ViewHolder
    public class PlatoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombrePlato;
        public TextView tvPrecioPlato;
        public ImageView imgPlato;
        public Button btn;
        public ImageButton añadir;
        public ImageButton restar;
        public TextView tvCantidad;

        public PlatoViewHolder(View itemView) {
            super(itemView);
            tvNombrePlato = itemView.findViewById(R.id.nombrePlato);
            tvPrecioPlato = itemView.findViewById(R.id.precioPlato);
            imgPlato = itemView.findViewById(R.id.imagenPlato);
            añadir = itemView.findViewById(R.id.meter);
            restar = itemView.findViewById(R.id.quitar);
            tvCantidad = itemView.findViewById(R.id.cantidad);
            btn = itemView.findViewById(R.id.btnMeterPlato);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("clcike un boton");
                    // Maneja el clic aquí
                    // Puedes obtener la posición del elemento con getAdapterPosition()
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position,tvCantidad);
                    }
                }
            });
            restar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("clcike un boton");
                    // Maneja el clic aquí
                    // Puedes obtener la posición del elemento con getAdapterPosition()
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.restar(tvCantidad,position);
                    }
                }
            });
            añadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("clcike un boton");
                    // Maneja el clic aquí
                    // Puedes obtener la posición del elemento con getAdapterPosition()
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.sumar(tvCantidad,position);
                    }
                }
            });


        }
    }

    @Override
    public PlatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cadaplato, parent, false);

        return new PlatoViewHolder(v);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    /**
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(PlatoViewHolder holder, int position) {
        Plato plato = listaPlatos.get(position);
        holder.tvNombrePlato.setText(plato.getNombre());
        String str = plato.getPrecio() + " €";
        holder.tvPrecioPlato.setText(str);
        holder.restar.setEnabled(plato.getCantidad() > 0);
        holder.añadir.setEnabled(plato.getCantidad() > 0);
        holder.btn.setEnabled(plato.getCantidad() > 0);

        if (plato.getNombre().toLowerCase(Locale.ROOT).contains("pulpo")){
            holder.imgPlato.setImageResource(R.drawable.pulpo);
        }else if(plato.getNombre().toLowerCase(Locale.ROOT).contains("rape")){
            holder.imgPlato.setImageResource(R.drawable.fideua);
        }else if(plato.getNombre().toLowerCase(Locale.ROOT).contains("tikka")){
            holder.imgPlato.setImageResource(R.drawable.pollo);
        }else if(plato.getNombre().toLowerCase(Locale.ROOT).contains("chipotle")){
            holder.imgPlato.setImageResource(R.drawable.chipotle);
        }else if(plato.getNombre().toLowerCase(Locale.ROOT).contains("mostaza")){
            holder.imgPlato.setImageResource(R.drawable.mostaza);
        }else {
            holder.imgPlato.setImageResource(R.drawable.pulpo);
        }

    }

    @Override
    public int getItemCount() {
        return listaPlatos.size();
    }
    public interface OnItemClickListener {
        /**
         * Método que añade un plato a la lista de consumiciones del pedido
         * @param position
         * @param textView
         */
        void onItemClick(int position, TextView textView);

        /**
         * Método que aumenta la cantidad de un plato a pedir
         * @param textView
         * @param position
         */
        void sumar(TextView textView,int position);

        /**
         * Método que resta la cantidad de un plato a pedir
         * @param textView
         * @param position
         */
        void restar(TextView textView,int position);
    }
}


