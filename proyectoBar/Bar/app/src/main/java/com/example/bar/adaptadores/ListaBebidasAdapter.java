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
import java.util.List;

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

        public ListaBebidasViewHolder(View itemView) {
            super(itemView);
            tvNombreBebida = itemView.findViewById(R.id.nombreBebida);
            tvPrecioBebida = itemView.findViewById(R.id.precioBebida);
            imgBebida = itemView.findViewById(R.id.imagenBebida);
            btn = itemView.findViewById(R.id.btnMeterBebida);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("clcike un boton");
                    // Maneja el clic aquí
                    // Puedes obtener la posición del elemento con getAdapterPosition()
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Haz algo con la posición del elemento, por ejemplo, invocar un método en tu adaptador
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
    }

    @Override
    public int getItemCount() {
        return listaBebidas.size();
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
