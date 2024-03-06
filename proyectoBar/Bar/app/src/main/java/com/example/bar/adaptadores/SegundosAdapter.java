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

import modelo.Plato;

public class SegundosAdapter extends RecyclerView.Adapter<SegundosAdapter.SegundosViewHolder> {
    private ArrayList<Plato> listaPlatosMenu;
    public int ultimoBoton = -1;
    public Button btn;
    private SegundosAdapter.OnItemClickListener listener;

    // Constructor
    public SegundosAdapter(ArrayList<Plato> listaPlatos) {
        this.listaPlatosMenu = listaPlatos;
    }

    // ViewHolder
    public class SegundosViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombrePlato;
        public ImageView imgPlato;
        public Button btn;


        public SegundosViewHolder(View itemView) {
            super(itemView);
            tvNombrePlato = itemView.findViewById(R.id.nombrePlato);
            imgPlato = itemView.findViewById(R.id.imagenPlato);
            btn = itemView.findViewById(R.id.btnMeterPlato);

            btn.setOnClickListener(v -> {

                int position = getAdapterPosition();


                if(listener != null && position != RecyclerView.NO_POSITION) {

                    if(ultimoBoton != position) {
                        int posicionAnterior = ultimoBoton;
                        ultimoBoton = position;
//                        se llama al elemento del recyclerview para que se vuelva a dibujar, como ha cambiado el ultimo botón en SegundosAdapter
//                        se deshabilitara un boton y se habilitará otro
                        if (posicionAnterior!= -1 ){
                            notifyItemChanged(posicionAnterior);
                        }

                        notifyItemChanged(position);
                    }
                    listener.onItemClickSegundo(position);
                }else {
                    System.out.println("es nyl");
                }
            });

        }
    }

    @Override
    public SegundosAdapter.SegundosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        se lee el fxml de cadaprimero ya que es el molde para cada elemento de la lista
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cadaprimero, parent, false);

        return new SegundosAdapter.SegundosViewHolder(v);
    }


    public void setOnItemClickListener(SegundosAdapter.OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(SegundosAdapter.SegundosViewHolder holder, int position) {
        Plato plato = listaPlatosMenu.get(position);
        holder.tvNombrePlato.setText(plato.getNombre());
        holder.btn.setEnabled(position != ultimoBoton);
    }

    @Override
    public int getItemCount() {
        return listaPlatosMenu.size();
    }
    public interface OnItemClickListener {
        void onItemClickSegundo(int position);
    }

}
