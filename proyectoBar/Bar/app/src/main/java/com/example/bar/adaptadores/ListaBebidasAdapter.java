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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import modelo.Bebida;
import modelo.Plato;

public class ListaBebidasAdapter extends RecyclerView.Adapter<ListaBebidasAdapter.ListaBebidasViewHolder> {
    private ArrayList<Bebida> listaBebidas;
    private OnItemClickListener listener;

    // Constructor
    public ListaBebidasAdapter(ArrayList<Bebida> listaBebidas) {
        this.listaBebidas = listaBebidas;
    }

    // ViewHolder
    public class ListaBebidasViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreBebida;
        public TextView tvPrecioBebida;
        public ImageView imgBebida;
        public Button btn;
        public ImageButton añadir;
        public ImageButton restar;
        public TextView tvCantidad;

        public ListaBebidasViewHolder(View itemView) {
            super(itemView);
            tvNombreBebida = itemView.findViewById(R.id.nombreBebida);
            tvPrecioBebida = itemView.findViewById(R.id.precioBebida);
            imgBebida = itemView.findViewById(R.id.imagenBebida);
            añadir = itemView.findViewById(R.id.meter);
            restar = itemView.findViewById(R.id.quitar);
            tvCantidad = itemView.findViewById(R.id.cantidad);
            btn = itemView.findViewById(R.id.btnMeterBebida);
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
    public ListaBebidasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bebidalista, parent, false);

        return new ListaBebidasViewHolder(v);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ListaBebidasViewHolder holder, int position) {
        Bebida bebida = listaBebidas.get(position);
        holder.tvNombreBebida.setText(bebida.getNombre());
        holder.tvPrecioBebida.setText(String.valueOf(bebida.getPrecio()) + " €");
        holder.btn.setEnabled(bebida.getCantidad()>0);
        holder.restar.setEnabled(bebida.getCantidad()>0);
        holder.añadir.setEnabled(bebida.getCantidad()>0);
        if(bebida.getNombre().toLowerCase(Locale.ROOT).contains("agua")){
            holder.imgBebida.setImageResource(R.drawable.agua);
        }else if(bebida.getNombre().toLowerCase(Locale.ROOT).contains("kas")){
            holder.imgBebida.setImageResource(R.drawable.kas);
        }else if(bebida.getNombre().toLowerCase(Locale.ROOT).contains("cocacola")){
            holder.imgBebida.setImageResource(R.drawable.cocacola);
        }else{
            holder.imgBebida.setImageResource(R.drawable.cocacola);
        }
    }

    @Override
    public int getItemCount() {
        return listaBebidas.size();
    }
    public interface OnItemClickListener {
        void onItemClick(int position,TextView textView);
        void sumar(TextView textView,int position);
        void restar(TextView textView,int position);
    }
}
