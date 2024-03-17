package com.example.bar.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.bar.ControllerLogin;
import com.example.bar.MainActivity;
import com.example.bar.Margen;
import com.example.bar.R;
import com.example.bar.adaptadores.ConsumicionAdapter;
import com.example.bar.adaptadores.MenuMeterAdapter;

import java.util.ArrayList;

import modelo.ConexionRetrofit;
import modelo.Consumicion;
import modelo.Data;
import modelo.MenuMeter;
import modelo.Pedido;
import modelo.PresupuestoRequest;
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
    private Button btnPagar;
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
        this.btnPagar = view.findViewById(R.id.btnPagar);
        btnPagar.setOnClickListener(this::pagarPedido);


        if(getArguments() != null){
            this.data = getArguments().getParcelable("data");

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

        if(getActivity() instanceof CambiarFragmentoInterfaz) {
            ((CambiarFragmentoInterfaz)getActivity()).cambiarFragmento(R.id.platos);
        }

    }

    public void pagarPedido(View view){
        this.data.getPedido().setEstado("Pagado");
        crearPresupuesto();
        crearFactura();

    }

    public interface CambiarFragmentoInterfaz {
        void cambiarFragmento(int menuItemId);
    }



    @Override
    public void onItemClickConsumicion(int position) {
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<Pedido> call = api.getpedido(data.getPedido().getId());
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
//                si la respuesta es satisfactoria se cargan los platos de la base de datos
                if (response.isSuccessful()) {


                    Pedido item = (Pedido) response.body();
                    if (item!=null){
                        data.setPedido(item);

                        button.setEnabled(data.getPedido().getEstado().equalsIgnoreCase("libre"));
                        btnPagar.setEnabled(data.getPedido().getEstado().equalsIgnoreCase("Servido"));

                    }
                    if (data.getPedido().getEstado().equalsIgnoreCase("Preparando")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
                        builder.setTitle("Pedido en preparación");
                        builder.setMessage("El pedido esta en preparación por nuestro cocinero, no es posible modificarlo");

                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }else if(data.getPedido().getEstado().equalsIgnoreCase("servido")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
                        builder.setTitle("Pedido servido");
                        builder.setMessage("No es posible modificar el pedido una vez está servido");

                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }

                    Consumicion consumicion = data.getPedido().getConsumiciones().get(position);
                    data.getPedido().getConsumiciones().remove(position);
                    ConsumicionAdapter adapter = (ConsumicionAdapter) recyclerViewCons.getAdapter();
                    /*
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());

                     */
                    adapter.actualizarDatos(data.getPedido().getConsumiciones());
                    calcularPrecio();
                    sumarConsumicion(consumicion);


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





    /**
     * Método que elimina del pedido el menú seleccionado
     * @param position
     */
    @Override
    public void eliminar(int position) {
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<Pedido> call = api.getpedido(data.getPedido().getId());
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
//                si la respuesta es satisfactoria se cargan los platos de la base de datos
                if (response.isSuccessful()) {


                    Pedido item = (Pedido) response.body();
                    if (item!=null){
                        data.setPedido(item);

                        button.setEnabled(data.getPedido().getEstado().equalsIgnoreCase("libre"));
                        btnPagar.setEnabled(data.getPedido().getEstado().equalsIgnoreCase("Servido"));

                    }
                    if (data.getPedido().getEstado().equalsIgnoreCase("Preparando")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
                        builder.setTitle("Pedido en preparación");
                        builder.setMessage("El pedido esta en preparación por nuestro cocinero, no es posible modificarlo");

                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }else if(data.getPedido().getEstado().equalsIgnoreCase("servido")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
                        builder.setTitle("Pedido servido");
                        builder.setMessage("No es posible modificar el pedido una vez está servido");

                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }

                    MenuMeter menuMeter = data.getPedido().getMenus().get(position);
                    data.getPedido().getMenus().remove(position);
                    MenuMeterAdapter adapter = (MenuMeterAdapter) recyclerViewMen.getAdapter();

                    adapter.actualizarDatos(data.getPedido().getMenus());
                    calcularPrecio();
                    sumarMenu(menuMeter);


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


                    Pedido item = (Pedido) response.body();
                    if (item!=null){
                        data.setPedido(item);

                        button.setEnabled(data.getPedido().getEstado().equalsIgnoreCase("libre"));
                        btnPagar.setEnabled(data.getPedido().getEstado().equalsIgnoreCase("Servido"));
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
     * Método que se encarga de crear una factura en la base de datos con los datos del pedido
     */
    public void crearFactura(){

        Api api = ConexionRetrofit.getConexion().create(Api.class);

        Call<ResponseBody> call = api.crearFactura(data.getPedido());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    eliminarPedido();

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

    public void crearPresupuesto(){
        ArrayList<Consumicion> consumiciones= new ArrayList<>();
        for (Consumicion consumicion : data.getPedido().getConsumiciones()){
            consumiciones.add(consumicion);
        }
        for (MenuMeter consumicion : data.getPedido().getMenus()){
            consumiciones.add(new Consumicion("Menu dia",consumicion.getPrecio(),consumicion.getCantidad(),"menu"));
        }
        String nombreCliente = "";
        if (data.getMesaSeleccionada().getSitios().isEmpty()){
            nombreCliente = data.getMesaSeleccionada().getNombre();
        }else {
            nombreCliente = data.getMesaSeleccionada().getSitios().get(0).getNombre();
        }
        PresupuestoRequest presupuestoRequest = new PresupuestoRequest(nombreCliente,consumiciones);
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ResponseBody> call = api.crearPresupuesto(presupuestoRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("presupuesto creado");
                } else {

                    System.out.println("error al crear el presupuesto");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                System.out.println("presupuesto creado");
            }
        });
    }



    /**
     * Método que se encarga de eliminar el pedido de la base de datos
     */
    public void eliminarPedido(){
        Api api = ConexionRetrofit.getConexion().create(Api.class);

        Call<ResponseBody> call = api.eliminarPedido(data.getPedido().getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    eliminarReserva();
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

    /**
     * Método que se encarga de eliminar la reserva de la base de datos
     */
    public void eliminarReserva(){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        String nombre = "";
        if (data.getMesaSeleccionada().getSitios().isEmpty()){
            nombre = data.getMesaSeleccionada().getNombre();
        }else{
            nombre = data.getMesaSeleccionada().getSitios().get(0).getNombre();
        }
        Call<ResponseBody> call = api.eliminarReserva(nombre);
        String finalNombre = nombre;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    liberarMesa(finalNombre);
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

    /**
     * Método que se encarga de liberar la mesa en la base de datos
     */
    public void liberarMesa(String nombre){
        Api api = ConexionRetrofit.getConexion().create(Api.class);

        Call<ResponseBody> call = api.liberarMesa(nombre);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    irLogin();
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

    /**
     * Método que se encarga de volver al login
     */
    public void irLogin(){
        Intent intent = new Intent(getContext(), ControllerLogin.class);
        startActivity(intent);
    }
    public void calcularPrecio() {
        this.data.getPedido().setPrecio(0.0);
        double cantidadLugar = 0;
        for (Consumicion consumicion : this.data.getPedido().getConsumiciones()){
            this.data.getPedido().setPrecio(consumicion.getPrecio() * consumicion.getCantidad() + this.data.getPedido().getPrecio());
        }
        for (MenuMeter menuMeter : this.data.getPedido().getMenus()){
            this.data.getPedido().setPrecio(menuMeter.getPrecio() * menuMeter.getCantidad() + this.data.getPedido().getPrecio());
        }
        if (data.getMesaSeleccionada().getUbicacion().equalsIgnoreCase("barra")){
            cantidadLugar = -1;
        }else if(data.getMesaSeleccionada().getUbicacion().equalsIgnoreCase("terraza")){
            cantidadLugar = 2;
        }
        if (this.data.getPedido().getPrecio()>0){
            this.data.getPedido().setPrecio(this.data.getPedido().getPrecio() + cantidadLugar);
        }

    }

}
