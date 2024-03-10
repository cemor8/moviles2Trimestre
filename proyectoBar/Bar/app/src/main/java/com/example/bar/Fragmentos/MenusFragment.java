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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Api;
import com.example.bar.Margen;
import com.example.bar.R;
import com.example.bar.adaptadores.BebidasAdapter;
import com.example.bar.adaptadores.PlatosAdapter;
import com.example.bar.adaptadores.PrimerosAdapter;
import com.example.bar.adaptadores.SegundosAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import modelo.MenuMeter;
import modelo.Pedido;
import modelo.Plato;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenusFragment extends Fragment implements PrimerosAdapter.OnItemClickListener, SegundosAdapter.OnItemClickListener, BebidasAdapter.OnItemClickListener{
    private Data data;
    private Button btn;
    private ImageView btnMeter;
    private ImageView btnQuitar;
    private TextView tvCantidad;
    private Integer cantidad;
    Plato primeroSeleccionado;
    Plato segundoSeleccionado;
    Bebida bebidaSeleccionada;
    private RecyclerView recyclerPrimeros;
    private RecyclerView recyclerSegundos;
    private RecyclerView recyclerBebidas;
    public MenusFragment(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menusdia, container, false);
        /* se establecen onclicks de elementos de la vista del fragment */

        btn = view.findViewById(R.id.btnMeterMenu);
        btn.setOnClickListener(this::meterMenu);
        btnMeter = view.findViewById(R.id.meter);
        btnMeter.setOnClickListener(this::sumar);
        btnQuitar = view.findViewById(R.id.quitar);
        btnQuitar.setOnClickListener(this::quitar);
        tvCantidad = view.findViewById(R.id.cantidadMenu);

        this.cantidad = Integer.parseInt(String.valueOf(tvCantidad.getText()));

        if(getArguments() != null){
            this.data = getArguments().getParcelable("data");
            System.out.println("comunicacion correcta");
            this.recibirPedido(view);
            this.inicializar(view);
        }
        return view;
    }

    /**
     * Método que se encarga de recibir el menu del dia de hoy
     * @param view
     */
    public void inicializar(View view){

        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Menu>> call = api.getMenus();
        call.enqueue(new Callback<ArrayList<Menu>>() {
            @Override
            public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {

                if (response.isSuccessful()) {

                    ArrayList<Menu> items = (ArrayList<Menu>) response.body();
                    Calendar calendario = Calendar.getInstance();
                    SimpleDateFormat formato = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
                    String dia = formato.format(calendario.getTime());
                    Optional<Menu> menu = items.stream().filter(menu1 -> menu1.getDia().equalsIgnoreCase(dia)).findAny();
                    if (!menu.isPresent()){
                        System.out.println(dia);
                        return;
                    }
                    data.setMenuDia(menu.get());
                    recorrer(view);




                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("respuesta mal");

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Método que se encarga de recorrer las listas de primeros, segundos y bebida del menu del dia para mostrar las opciones
     * @param view
     */
    public void recorrer(View view){
        TextView textView = view.findViewById(R.id.nombreMenuDia);
        textView.setText("Menú "+this.data.getMenuDia().getDia());
        RecyclerView recyclerView = view.findViewById(R.id.contenedorConsumiciones);
        this.recyclerPrimeros = recyclerView;
        RecyclerView recyclerView2 = view.findViewById(R.id.contenedorSegundos);
        this.recyclerSegundos = recyclerView2;
        RecyclerView recyclerView3 = view.findViewById(R.id.contenedorBebidas);
        this.recyclerBebidas = recyclerView3;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        PrimerosAdapter adapter = new PrimerosAdapter(data.getMenuDia().getPrimeros());
        adapter.setOnItemClickListener(this::onItemClickPrimero);
        recyclerView.setAdapter(adapter);



        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        SegundosAdapter segundosAdapter = new SegundosAdapter(data.getMenuDia().getSegundos());
        segundosAdapter.setOnItemClickListener(this::onItemClickSegundo);
        recyclerView2.setAdapter(segundosAdapter);

        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        BebidasAdapter bebidasAdapter = new BebidasAdapter(data.getMenuDia().getBebidas());
        bebidasAdapter.setOnItemClickListener(this::onItemClickBebida);
        recyclerView3.setAdapter(bebidasAdapter);


        int espacio = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14,
                getResources().getDisplayMetrics()
        );
        recyclerView.addItemDecoration(new Margen(espacio,true));

    }

    /**
     * Método que se encarga de establecer un plato como primero en el menú a pedir
     * @param position posicion del plato en la lista
     */
    @Override
    public void onItemClickPrimero(int position) {
        Plato primero = this.data.getMenuDia().getPrimeros().get(position);
        this.primeroSeleccionado = primero;
        System.out.println(primeroSeleccionado);

    }
    /**
     * Método que se encarga de establecer un plato como segundo en el menú a pedir
     * @param position posicion del plato en la lista
     */
    @Override
    public void onItemClickSegundo(int position) {
        Plato segundo = this.data.getMenuDia().getSegundos().get(position);
        this.segundoSeleccionado = segundo;
        System.out.println(segundoSeleccionado);

    }
    /**
     * Método que se encarga de establecer una bebida en el menú a pedir
     * @param position posicion de la bebida en la lista
     */
    @Override
    public void onItemClickBebida(int position) {
        Bebida bebida = this.data.getMenuDia().getBebidas().get(position);
        this.bebidaSeleccionada = bebida;
        System.out.println(bebidaSeleccionada);

    }

    /**
     * Método que se encarga de meter el menú en la base de datos
     * @param view
     */
    public void meterMenu(View view){

        if (this.data.getPedido().getEstado().equalsIgnoreCase("Preparando")){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
            builder.setTitle("Pedido en preparación");
            builder.setMessage("El pedido esta en preparación por nuestro cocinero, no es posible modificarlo");

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
        if(this.data.getPedido().getEstado().equalsIgnoreCase("servido")) {
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


        if (this.bebidaSeleccionada == null || this.primeroSeleccionado == null || this.segundoSeleccionado == null || cantidad == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
            builder.setTitle("Faltan datos");
            builder.setMessage("Asegurese de seleccionar al menos un primero, un segundo y una bebida");

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




        System.out.println("precios y cantidades");
        System.out.println(cantidad);
        System.out.println(data.getMenuDia().getPrecio());

        MenuMeter menuMeter = new MenuMeter(data.getMenuDia().getDia(),this.primeroSeleccionado,this.segundoSeleccionado,this.bebidaSeleccionada,cantidad,data.getMenuDia().getPrecio() * cantidad);

        Optional<MenuMeter> menuOptional = data.getPedido().getMenus().stream().filter(menuMeter1 -> menuMeter1.getPrimero().getNombre().equalsIgnoreCase(menuMeter.getPrimero().getNombre()) &&
                menuMeter1.getSegundo().getNombre().equalsIgnoreCase(menuMeter.getSegundo().getNombre()) && menuMeter1.getBebida().getNombre().equalsIgnoreCase(menuMeter.getBebida().getNombre())).findAny();
        if (menuOptional.isPresent()){
            if (menuOptional.get().getCantidad() + menuMeter.getCantidad() > 15){
                popUpMax();
                return;
            }

            menuOptional.get().setCantidad(menuOptional.get().getCantidad() + menuMeter.getCantidad());
        }else {
            data.getPedido().getMenus().add(menuMeter);
        }

        this.calcularPrecio();
        BigDecimal bd = new BigDecimal(this.data.getPedido().getPrecio()).setScale(2, RoundingMode.HALF_UP);
        double valorRedondeado = bd.doubleValue();
        this.data.getPedido().setPrecio(valorRedondeado);
        tvCantidad.setText("1");
        this.cantidad = 1;
        productoMeter();
        modificarPedido(view);
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

    /**
     * Método que se encarga de modificar un pedido en la base de datos con los nuevos datos
     * del pedido
     * @param view
     */
    public void modificarPedido(View view){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ResponseBody> call = api.modificarPedido(data.getPedido().getId(),data.getPedido());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    restarCantidades(view);
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



    /**
     * Método que reinicia los datos para pedir un nuevo menú
     * @param view
     */
    public void reiniciarSeleccionados(View view){
        primeroSeleccionado = null;
        segundoSeleccionado = null;
        bebidaSeleccionada = null;
        this.cantidad = 1;
        tvCantidad.setText("1");

        if (this.recyclerPrimeros != null && recyclerPrimeros.getAdapter() instanceof PrimerosAdapter) {
            PrimerosAdapter primerosAdapter = (PrimerosAdapter) recyclerPrimeros.getAdapter();
            primerosAdapter.ultimoBoton = -1;
            primerosAdapter.notifyDataSetChanged();
        }

        if (recyclerSegundos != null && recyclerSegundos.getAdapter() instanceof SegundosAdapter) {
            SegundosAdapter segundosAdapter = (SegundosAdapter) recyclerSegundos.getAdapter();
            segundosAdapter.ultimoBoton = -1;
            segundosAdapter.notifyDataSetChanged();
        }

        if (recyclerBebidas != null && recyclerBebidas.getAdapter() instanceof BebidasAdapter) {
            BebidasAdapter bebidasAdapter = (BebidasAdapter) recyclerBebidas.getAdapter();
            bebidasAdapter.ultimoBoton = -1;
            bebidasAdapter.notifyDataSetChanged();
        }
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
    /**
     * Método que resta las cantidades del primer plato, segundo y bebida de la base de datos
     * @param view
     */
    public void restarCantidades(View view){
        System.out.println("Restando");
        System.out.println("Primero");
        System.out.println(primeroSeleccionado);
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"cantidad\": " + cantidad + " }");
        Call<ResponseBody> call = api.restarPlatos(primeroSeleccionado.getNombre(),body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("primeso seleccionado");
                System.out.println(primeroSeleccionado);
                if (response.isSuccessful()) {
                    primeroSeleccionado.setCantidad(primeroSeleccionado.getCantidad() - cantidad);
                    restarSegundo(view);
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

    /**
     * Método que se encarga de restar las cantidades del segundo plato en la base de datos
     * @param view
     */
    public void restarSegundo(View view){
        System.out.println("segundo");
        System.out.println(segundoSeleccionado);
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"cantidad\": " + cantidad + " }");
        Call<ResponseBody> call = api.restarPlatos(segundoSeleccionado.getNombre(),body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    segundoSeleccionado.setCantidad(segundoSeleccionado.getCantidad() - cantidad);
                    restarBebida(view);
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

    /**
     * Método que se encarga de restar la cantidad de la bebida en la base de datos
     * @param view
     */
    public void restarBebida(View view){
        System.out.println("bebida");
        System.out.println(bebidaSeleccionada);
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{ \"cantidad\": " + cantidad + " }");
        Call<ResponseBody> call = api.restarBebida(bebidaSeleccionada.getNombre(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    System.out.println("bebida");
                    System.out.println(bebidaSeleccionada);
                    bebidaSeleccionada.setCantidad(bebidaSeleccionada.getCantidad() - cantidad);
                    reiniciarSeleccionados(view);
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
     * Método que suma la cantidad de menus a pedir
     * @param view
     */
    public void sumar(View view){
        Integer cantidadMostrada = Integer.parseInt(String.valueOf(tvCantidad.getText()));
        System.out.println(primeroSeleccionado.getCantidad());
        System.out.println(bebidaSeleccionada.getCantidad());
        System.out.println(segundoSeleccionado.getCantidad());
        if ((primeroSeleccionado != null && cantidadMostrada + 1 > primeroSeleccionado.getCantidad()) ||
                (segundoSeleccionado != null && cantidadMostrada + 1 > segundoSeleccionado.getCantidad()) ||
                (bebidaSeleccionada != null && cantidadMostrada + 1 > bebidaSeleccionada.getCantidad()) ||
                cantidadMostrada + 1 > 15) {
            return;
        }
        System.out.println("hola");
        this.cantidad = cantidadMostrada + 1;
        tvCantidad.setText(String.valueOf(this.cantidad));
    }

    /**
     * Método que resta la cantidad de menus a pedir
     * @param view
     */
    public void quitar(View view){
        Integer cantidadMostrada = Integer.parseInt(String.valueOf(tvCantidad.getText()));
        if (cantidadMostrada - 1 <= 0){
            return;
        }
        this.cantidad = cantidadMostrada - 1;
        tvCantidad.setText(String.valueOf(cantidadMostrada));

    }
    /**
     * Método que se encarga de indicar el máximo de consumiciones por pedido
     */
    public void popUpMax() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
        builder.setTitle("Cantidad máxima");
        builder.setMessage("Solo se permite como máximo 15 unidades por consumición");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void productoMeter(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomAlertDialog));
        builder.setTitle("Artículo añadido");
        builder.setMessage("Artículo añadido al pedido");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
