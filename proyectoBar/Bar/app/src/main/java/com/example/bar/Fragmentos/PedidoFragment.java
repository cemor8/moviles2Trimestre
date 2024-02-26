package com.example.bar.Fragmentos;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Api;
import com.example.bar.Margen;
import com.example.bar.R;
import com.example.bar.adaptadores.BebidasAdapter;
import com.example.bar.adaptadores.ConsumicionAdapter;
import com.example.bar.adaptadores.PlatosAdapter;
import com.example.bar.adaptadores.PrimerosAdapter;
import com.example.bar.adaptadores.SegundosAdapter;

import java.util.ArrayList;

import modelo.ConexionRetrofit;
import modelo.Data;
import modelo.Plato;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoFragment extends Fragment implements ConsumicionAdapter.OnItemClickListener{
    private Data data;
    public PedidoFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // se carga el fxml de listaplatos que es el que contiene el contenido y se devuelve para que el fragmento lo cargue
        View view = inflater.inflate(R.layout.pedidovista, container, false);

        if(getArguments() != null){
            this.data = getArguments().getParcelable("data");
            System.out.println("comunicacion correcta");
            this.inicializar(view);
        }

        return view;
    }

    /**
     * Método que carga la lista de platos en el recyclerview, hace una petición para
     * obtener la lista de platos disponibles
     * @param view
     */
    public void inicializar(View view){

        RecyclerView recyclerView = view.findViewById(R.id.contenedorPrimeros);



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ConsumicionAdapter adapter = new ConsumicionAdapter(data.getPedido().getConsumiciones());
        adapter.setOnItemClickListener(this::onItemClickConsumicion);
        recyclerView.setAdapter(adapter);



        int espacio = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14,
                getResources().getDisplayMetrics()
        );
        recyclerView.addItemDecoration(new Margen(espacio,true));
    }

    @Override
    public void onItemClickConsumicion(int position) {
        this.data.getPedido().getConsumiciones().remove(position);
        RecyclerView recyclerView = getView().findViewById(R.id.contenedorPrimeros);
        ConsumicionAdapter adapter = (ConsumicionAdapter) recyclerView.getAdapter();
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());

    }
}
