package com.example.bar.Fragmentos;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Api;
import com.example.bar.Margen;
import com.example.bar.R;
import com.example.bar.adaptadores.PlatosAdapter;

import java.util.ArrayList;
import java.util.Optional;

import modelo.ConexionRetrofit;
import modelo.Consumicion;
import modelo.Data;
import modelo.Mesa;
import modelo.Plato;
import modelo.Sitio;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlatosFragment extends Fragment implements PlatosAdapter.OnItemClickListener{
    private Data data;
    public PlatosFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // se carga el fxml de listaplatos que es el que contiene el contenido y se devuelve para que el fragmento lo cargue
        View view = inflater.inflate(R.layout.listaplatos, container, false);

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
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Plato>> call = api.getPlatos();
        call.enqueue(new Callback<ArrayList<Plato>>() {
            @Override
            public void onResponse(Call<ArrayList<Plato>> call, Response<ArrayList<Plato>> response) {
//                si la respuesta es satisfactoria se cargan los platos de la base de datos
                if (response.isSuccessful()) {
                    System.out.println(response.body());

                    data.getListaPlatosRestaurante().removeAll(data.getListaPlatosRestaurante());

                    ArrayList<Plato> items = (ArrayList<Plato>) response.body();
                    if (items!=null){
                        data.getListaPlatosRestaurante().addAll(items);
                    }



                    recorrer(view);



                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("respuesta mal");

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Plato>> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });
    }
//                    se rellena el recyclerview con un gridlayout con 2 columnas, se establecee el adaptador de los platos y se indica la lista
//                    a recorrer, la lista de data.getListaPlatosRestaurante(), tambien crea un espacio que es un estilo para margenes en cada elemento del
//                    gridlayout
    public void recorrer(View view){
        RecyclerView recyclerView = view.findViewById(R.id.contenedorListaPlatos);
        int columnas = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnas));
        PlatosAdapter adapter = new PlatosAdapter(data.getListaPlatosRestaurante());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        int espacio = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()
        );
        recyclerView.addItemDecoration(new Margen(espacio,false));
    }

    @Override
    public void onItemClick(int position, TextView textView) {
        Integer cantidad = Integer.valueOf(String.valueOf(textView.getText()));
        Plato plato = this.data.getListaPlatosRestaurante().get(position);
        Consumicion consumicion = new Consumicion(plato.getNombre(), plato.getPrecio(),cantidad);
        Optional<Consumicion> consumicionOptional = this.data.getPedido().getConsumiciones().stream().filter(consumicion1 -> consumicion1.getNombre().equalsIgnoreCase(consumicion.getNombre())).findAny();
        if (consumicionOptional.isPresent()){
           consumicionOptional.get().setCantidad(consumicionOptional.get().getCantidad() + consumicion.getCantidad());
        }else {
            this.data.getPedido().getConsumiciones().add(consumicion);
        }
        textView.setText("1");



        Api api = ConexionRetrofit.getConexion().create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"cantidad\": " + cantidad + " }");
        Call<ResponseBody> call = api.restarPlatos(plato.getNombre(),body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    plato.setCantidad(plato.getCantidad() - cantidad);
                    modificarPedido();
                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("error");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("error al restar los platos");
                System.out.println(t.getMessage());
            }
        });


    }

    @Override
    public void sumar(TextView textView,int position) {
        Integer cantidad = Integer.valueOf(String.valueOf(textView.getText()));
        Plato plato = this.data.getListaPlatosRestaurante().get(position);
        if (cantidad.equals(plato.getCantidad())){
            return;
        }else if(cantidad == 20){
            return;
        }
        textView.setText(String.valueOf(cantidad+1));
    }

    @Override
    public void restar(TextView textView,int position) {
        Integer cantidad = Integer.valueOf(String.valueOf(textView.getText()));
        Plato plato = this.data.getListaPlatosRestaurante().get(position);
        if (cantidad.equals(plato.getCantidad())){
            return;
        }else if (cantidad == 1){
            return;
        }
        textView.setText(String.valueOf(cantidad-1));
    }

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
}
