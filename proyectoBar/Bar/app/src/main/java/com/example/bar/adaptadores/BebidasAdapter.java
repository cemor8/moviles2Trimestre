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
import java.util.Locale;

import modelo.Bebida;

public class BebidasAdapter extends RecyclerView.Adapter<BebidasAdapter.BebidasViewHolder> {
    private ArrayList<Bebida> listaBebidas;
//    posicion en la lista del elemento el cual el ultimo botón se ha clickado
    public int ultimoBoton = -1;
    public Button btn;
//    método para asignar al boton
    private BebidasAdapter.OnItemClickListener listener;

    // Constructor
    public BebidasAdapter(ArrayList<Bebida> listaBebidas) {
        this.listaBebidas = listaBebidas;
    }

    // ViewHolder
    public class BebidasViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreBebida;
        public ImageView imgBebida;
        public Button btn;


        public BebidasViewHolder(View itemView) {
            super(itemView);
//            elementos de cada item
            tvNombreBebida = itemView.findViewById(R.id.nombreBebida);
            imgBebida = itemView.findViewById(R.id.imagenBebida);
            btn = itemView.findViewById(R.id.btnMeterBebida);


//            establecer onclick
            btn.setOnClickListener(v -> {

                int position = getAdapterPosition();


                if(listener != null && position != RecyclerView.NO_POSITION) {

                    if(ultimoBoton != position) {
                        int posicionAnterior = ultimoBoton;
                        ultimoBoton = position;
//                        se llama al elemento del recyclerview para que se vuelva a dibujar, como ha cambiado el ultimo botón en BebidasAdapter
//                        se deshabilitara un boton y se habilitará otro
                        if (posicionAnterior!= -1 ){
                            notifyItemChanged(posicionAnterior);
                        }

                        notifyItemChanged(position);
                    }
                    listener.onItemClickBebida(position);

                }else {
                    System.out.println("es nyl");
                }
            });

        }
    }

    @Override
    public BebidasAdapter.BebidasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        se lee el xml de bebidamenu ya que va a ser el molde para cada elemento del recyclerview
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bebidamenu, parent, false);
        return new BebidasAdapter.BebidasViewHolder(v);
    }


    public void setOnItemClickListener(BebidasAdapter.OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(BebidasAdapter.BebidasViewHolder holder, int position) {
        Bebida bebida = listaBebidas.get(position);
        holder.tvNombreBebida.setText(bebida.getNombre());
        holder.btn.setEnabled(position != ultimoBoton && bebida.getCantidad() > 0);

        if(bebida.getNombre().toLowerCase(Locale.ROOT).contains("agua")){
            holder.imgBebida.setImageResource(R.drawable.agua);
        }else if(bebida.getNombre().toLowerCase(Locale.ROOT).contains("kas")){
            holder.imgBebida.setImageResource(R.drawable.kas);
        }else if(bebida.getNombre().toLowerCase(Locale.ROOT).contains("cocacola")){
            holder.imgBebida.setImageResource(R.drawable.cocacola);
        }else {
            holder.imgBebida.setImageResource(R.drawable.cocacola);
        }
    }

    @Override
    public int getItemCount() {
        return listaBebidas.size();
    }
    public interface OnItemClickListener {
        void onItemClickBebida(int position);
    }

}
