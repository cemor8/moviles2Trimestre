package com.example.bar.Fragmentos;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Api;
import com.example.bar.Margen;
import com.example.bar.R;
import com.example.bar.adaptadores.BebidasAdapter;
import com.example.bar.adaptadores.ListaBebidasAdapter;
import com.example.bar.adaptadores.PlatosAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import modelo.Bebida;
import modelo.ConexionRetrofit;
import modelo.Consumicion;
import modelo.Data;
import modelo.Menu;
import modelo.Pedido;
import modelo.Plato;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BebidaFragment extends Fragment implements ListaBebidasAdapter.OnItemClickListener{
    private Data data;
    private RecyclerView recyclerView;
    public BebidaFragment(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bebidas, container, false);
        this.recyclerView = view.findViewById(R.id.contenedorListaBebidas);
        if(getArguments() != null){
            this.data = getArguments().getParcelable("data");
            System.out.println("comunicacion correcta");
            this.recibirPedido(view);
            this.inicializar(view);
        }
        return view;
    }

    /**
     * Método que recibe la lista de bebidas de la base de datos
     * @param view
     */
    public void inicializar(View view){

        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Bebida>> call = api.getBebidas();
        call.enqueue(new Callback<ArrayList<Bebida>>() {
            @Override
            public void onResponse(Call<ArrayList<Bebida>> call, Response<ArrayList<Bebida>> response) {

                if (response.isSuccessful()) {

                    System.out.println(response.body());
                    data.getListaBebidasRestaurante().removeAll(data.getListaBebidasRestaurante());
                    ArrayList<Bebida> items = (ArrayList<Bebida>) response.body();
                    data.getListaBebidasRestaurante().addAll(items);
                    recorerr(view);



                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("respuesta mal");

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Bebida>> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método que inicializa el recyclerview
     * @param view
     */
    public void recorerr(View view){
        RecyclerView recyclerView = view.findViewById(R.id.contenedorListaBebidas);
        int columnas = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnas));
        ListaBebidasAdapter adapter = new ListaBebidasAdapter(data.getListaBebidasRestaurante());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        int espacio = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()
        );
        recyclerView.addItemDecoration(new Margen(espacio,false));
    }

    @Override
    public void onItemClick(int position,TextView textView) {


        /* Comprobar estado pedido, si se puede deja añadir, si no no */

        Integer cantidad = Integer.valueOf(String.valueOf(textView.getText()));
        Bebida bebida = this.data.getListaBebidasRestaurante().get(position);
        Consumicion consumicion = new Consumicion(bebida.getNombre(), bebida.getPrecio(),cantidad,"bebida");

        Optional<Consumicion> consumicionOptional = this.data.getPedido().getConsumiciones().stream().filter(consumicion1 -> consumicion1.getNombre().equalsIgnoreCase(consumicion.getNombre())).findAny();
        if (consumicionOptional.isPresent()){
            if (consumicionOptional.get().getCantidad() + consumicion.getCantidad() > 15){

                /* Establecer pop up no se puede mas de 15 por plato*/

                return;
            }
            consumicionOptional.get().setCantidad(consumicionOptional.get().getCantidad() + consumicion.getCantidad());
        }else {
            this.data.getPedido().getConsumiciones().add(consumicion);
            this.data.getPedido().setPrecio(this.data.getPedido().getPrecio() + consumicion.getPrecio());
        }
        textView.setText("1");



        Api api = ConexionRetrofit.getConexion().create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"cantidad\": " + cantidad + " }");
        Call<ResponseBody> call = api.restarBebida(bebida.getNombre(),body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    bebida.setCantidad(bebida.getCantidad() - cantidad);
                    if (recyclerView != null && recyclerView.getAdapter() instanceof ListaBebidasAdapter) {
                        ListaBebidasAdapter listaBebidasAdapter = (ListaBebidasAdapter) recyclerView.getAdapter();
                        listaBebidasAdapter.notifyDataSetChanged();
                    }
                    modificarPedido();
                    System.out.println(data.getListaPlatosRestaurante());
                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("error");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("error al restar las bebidas");
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método que se encarga de modificar el pedido en la base de datos
     */
    public void modificarPedido(){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ResponseBody> call = api.modificarPedido(data.getPedido().getId(),data.getPedido());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    System.out.println("correcto");

                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("respuesta mal");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });
    }



    @Override
    public void sumar(TextView textView, int position) {
        Integer cantidad = Integer.valueOf(String.valueOf(textView.getText()));
        Bebida bebida = this.data.getListaBebidasRestaurante().get(position);
        if (cantidad.equals(bebida.getCantidad())){
            return;
        }else if(cantidad == 15){
            return;
        }
        textView.setText(String.valueOf(cantidad+1));
    }

    @Override
    public void restar(TextView textView, int position) {
        Integer cantidad = Integer.valueOf(String.valueOf(textView.getText()));
        Bebida bebida = this.data.getListaBebidasRestaurante().get(position);
        if (cantidad.equals(bebida.getCantidad())){
            return;
        }else if (cantidad == 1){
            return;
        }
        textView.setText(String.valueOf(cantidad-1));
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
}
