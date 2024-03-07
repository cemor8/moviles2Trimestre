package com.example.bar.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Api;
import com.example.bar.Margen;
import com.example.bar.R;
import com.example.bar.adaptadores.ConsumicionAdapter;
import com.example.bar.adaptadores.MenuMeterAdapter;

import modelo.ConexionRetrofit;
import modelo.Consumicion;
import modelo.Data;
import modelo.MenuMeter;
import modelo.Pedido;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoFragment extends Fragment implements ConsumicionAdapter.OnItemClickListener, MenuMeterAdapter.OnItemClickListener{
    private Data data;
    private Button button;
    private RecyclerView recyclerViewCons;
    private RecyclerView recyclerViewMen;
    public PedidoFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // se carga el fxml de listaplatos que es el que contiene el contenido y se devuelve para que el fragmento lo cargue
        View view = inflater.inflate(R.layout.pedidovista, container, false);
        button = view.findViewById(R.id.btnConfirmar);
        button.setOnClickListener(this::confirmarPedido);
        this.recyclerViewMen = view.findViewById(R.id.contenedorMenusMeter);
        this.recyclerViewCons = view.findViewById(R.id.contenedorConsumiciones);

        if(getArguments() != null){
            this.data = getArguments().getParcelable("data");
            System.out.println("comunicacion correcta");
            this.recibirPedido(view);
        }

        return view;
    }

    public void confirmarPedido(View view){
        if (this.data.getPedido().getConsumiciones().isEmpty() && this.data.getPedido().getMenus().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
            builder.setTitle("Ordene alguna consumición");
            builder.setMessage("Asegurese de pedir al menos una consumicion para poder proceder");

            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        this.data.getPedido().setEstado("Confirmado");
        modificarPedido();

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
        builder.setTitle("Pedido confirmado");
        builder.setMessage("Puede modificar el pedido mientras no esté en preparación");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        this.button.setEnabled(false);


    }




    @Override
    public void onItemClickConsumicion(int position) {
        Consumicion consumicion = this.data.getPedido().getConsumiciones().get(position);
        this.data.getPedido().getConsumiciones().remove(position);
        ConsumicionAdapter adapter = (ConsumicionAdapter) recyclerViewCons.getAdapter();
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());

        sumarConsumicion(consumicion);

    }

    /**
     * Método que elimina del pedido el menú seleccionado
     * @param position
     */
    @Override
    public void eliminar(int position) {
        MenuMeter menuMeter = this.data.getPedido().getMenus().get(position);
        this.data.getPedido().getMenus().remove(position);
        MenuMeterAdapter adapter = (MenuMeterAdapter) recyclerViewMen.getAdapter();
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());

        sumarMenu(menuMeter);
    }

    /**
     * Método que abre la vista detallada del menu del día seleccionado
     * @param position
     */
    @Override
    public void ver(int position) {
        /* Vista detallada del menu */
    }

    public void recorrer(View view){
        RecyclerView recyclerView = view.findViewById(R.id.contenedorConsumiciones);
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
                        System.out.println(data.getPedido());
                        button.setEnabled(data.getPedido().getEstado().equalsIgnoreCase("libre"));
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
    public void sumarMenu(MenuMeter menuMeter){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"cantidad\": " + menuMeter.getCantidad() + " }");
        Call<ResponseBody> call = api.sumarBebida(menuMeter.getBebida().getNombre(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    sumarPrimero(menuMeter);
                    System.out.println(data.getListaPlatosRestaurante());
                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("error");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("error al sumar");
                System.out.println(t.getMessage());
            }
        });

    }

    public void sumarPrimero(MenuMeter menuMeter){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"cantidad\": " + menuMeter.getCantidad() + " }");
        Call<ResponseBody> call = api.sumarPlatos(menuMeter.getPrimero().getNombre(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    sumarSegundo(menuMeter);
                    System.out.println(data.getListaPlatosRestaurante());
                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("error");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("error al sumar");
                System.out.println(t.getMessage());
            }
        });
    }

    public void sumarSegundo(MenuMeter menuMeter){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"cantidad\": " + menuMeter.getCantidad() + " }");
        Call<ResponseBody> call = api.sumarPlatos(menuMeter.getSegundo().getNombre(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
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
                System.out.println("error al sumar");
                System.out.println(t.getMessage());
            }
        });
    }





    public void sumarConsumicion(Consumicion consumicion){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"cantidad\": " + consumicion.getCantidad() + " }");
        Call<ResponseBody> call;
        if (consumicion.getTipo().equalsIgnoreCase("bebida")){
            call = api.sumarBebida(consumicion.getNombre(),body);
        }else {
            call = api.sumarPlatos(consumicion.getNombre(),body);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
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
}
