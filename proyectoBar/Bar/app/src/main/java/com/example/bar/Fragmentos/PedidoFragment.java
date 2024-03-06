package com.example.bar.Fragmentos;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.bar.adaptadores.MenuMeterAdapter;
import com.example.bar.adaptadores.PlatosAdapter;
import com.example.bar.adaptadores.PrimerosAdapter;
import com.example.bar.adaptadores.SegundosAdapter;

import java.util.ArrayList;

import modelo.ConexionRetrofit;
import modelo.Data;
import modelo.Pedido;
import modelo.Plato;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoFragment extends Fragment implements ConsumicionAdapter.OnItemClickListener, MenuMeterAdapter.OnItemClickListener{
    private Data data;
    private Button button;
    public PedidoFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // se carga el fxml de listaplatos que es el que contiene el contenido y se devuelve para que el fragmento lo cargue
        View view = inflater.inflate(R.layout.pedidovista, container, false);
        button = view.findViewById(R.id.btnConfirmar);

        if(getArguments() != null){
            this.data = getArguments().getParcelable("data");
            System.out.println("comunicacion correcta");
            this.recibirPedido(view);
        }

        return view;
    }


    /**
     * Método que recibe el pedido de la base de datos
     * @param view
     */
    public void recibirPedido(View view){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<Pedido> call = api.getpedido(data.getPedido().getId());
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
//                si la respuesta es satisfactoria se cargan los platos de la base de datos
                if (response.isSuccessful()) {
                    System.out.println(response.body());

                    Pedido item = (Pedido) response.body();
                    if (item!=null){
                        data.setPedido(item);
                        recorrer(view);
                        recorrerMenusMeter(view);
                    }



                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("respuesta mal");

                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    public void onItemClickConsumicion(int position) {
        this.data.getPedido().getConsumiciones().remove(position);
        RecyclerView recyclerView = getView().findViewById(R.id.contenedorPrimeros);
        ConsumicionAdapter adapter = (ConsumicionAdapter) recyclerView.getAdapter();
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());

        /* Meter cantidades otra vez */

    }

    @Override
    public void eliminar(int position) {
        this.data.getPedido().getMenus().remove(position);
        RecyclerView recyclerView = getView().findViewById(R.id.contenedorMenusMeter);
        ConsumicionAdapter adapter = (ConsumicionAdapter) recyclerView.getAdapter();
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());

        /* Meter cantidades otra vez */
    }

    @Override
    public void ver(int position) {
        /* Vista detallada del menu */
    }

    public void recorrer(View view){
        RecyclerView recyclerView = view.findViewById(R.id.contenedorPrimeros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        System.out.println("aqui");
        System.out.println(data.getPedido().getConsumiciones());
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
    public void recorrerMenusMeter(View view){
        RecyclerView recyclerView = view.findViewById(R.id.contenedorMenusMeter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        MenuMeterAdapter adapter = new MenuMeterAdapter(data.getPedido().getMenus());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        int espacio = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14,
                getResources().getDisplayMetrics()
        );
        recyclerView.addItemDecoration(new Margen(espacio,true));
    }
}
