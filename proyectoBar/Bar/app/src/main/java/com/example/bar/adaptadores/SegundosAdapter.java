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

        /**
         * Método se establecen elementos de cada elemento del recyclerview, se asignan onclicks
         * @param itemView
         */
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

    /**
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @Override
    public SegundosAdapter.SegundosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        se lee el fxml de cadaprimero ya que es el molde para cada elemento de la lista
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cadaprimero, parent, false);

        return new SegundosAdapter.SegundosViewHolder(v);
    }

    /**
     * Método que establece onclick para el adaptador
     * @param onItemClickListener onItemClickListener
     */
    public void setOnItemClickListener(SegundosAdapter.OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    /**
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(SegundosAdapter.SegundosViewHolder holder, int position) {
        Plato plato = listaPlatosMenu.get(position);
        holder.tvNombrePlato.setText(plato.getNombre());
        holder.btn.setEnabled(position != ultimoBoton && plato.getCantidad() > 0);
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
        return listaPlatosMenu.size();
    }

    /**
     * Interfaz que tiene los metodos onclick para modificarlos posteriormete segun la
     * necesidad del recycler
     */
    public interface OnItemClickListener {
        void onItemClickSegundo(int position);
    }

}
