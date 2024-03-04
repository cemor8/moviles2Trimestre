package com.example.bar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import modelo.ConexionRetrofit;
import modelo.Data;
import modelo.Menu;
import modelo.Mesa;
import modelo.Pedido;
import modelo.Sitio;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerLogin extends AppCompatActivity {
    private Data data;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.data = new Data();
        this.textView = findViewById(R.id.introducirNombreMesa);
    }

    /**
     * Método para cambiar de stage al menu de los platos y pedidos
     * @param view
     */
    public void login(View view){

        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Mesa>> call = api.getMesas();
        call.enqueue(new Callback<ArrayList<Mesa>>() {
            @Override
            public void onResponse(Call<ArrayList<Mesa>> call, Response<ArrayList<Mesa>> response) {

                if (response.isSuccessful()) {


                    data.getListaPlatosRestaurante().removeAll(data.getListaPlatosRestaurante());
                    ArrayList<Mesa> items = (ArrayList<Mesa>) response.body();
                    System.out.println(items);
                    String nombreMesa = String.valueOf(textView.getText());
                    Optional<Mesa> mesaSeleccionada = items.stream().filter(mesa -> !mesa.getOcupada()
                            && (mesa.getNombre().equalsIgnoreCase(nombreMesa) || mesa.getSitios().stream().anyMatch(sitio -> sitio.getNombre().equalsIgnoreCase(nombreMesa)))).findAny();

                    if (!mesaSeleccionada.isPresent()){
                        System.out.println("Mesa no encontrada");
                        return;
                    }
                    if (mesaSeleccionada.get().getUbicacion().equalsIgnoreCase("barra")){
                        Optional<Sitio> sitioOptional = mesaSeleccionada.get().getSitios().stream().filter(sitio1 -> sitio1.getNombre().equalsIgnoreCase(String.valueOf(textView.getText()))).findAny();
                        Sitio sitio = null;
                        if (sitioOptional.isPresent()){
                            sitio = sitioOptional.get();
                        }
                        mesaSeleccionada.get().getSitios().removeAll(mesaSeleccionada.get().getSitios());
                        mesaSeleccionada.get().getSitios().add(sitio);
                        data.setMesaSeleccionada(mesaSeleccionada.get());

                    }else{
                        data.setMesaSeleccionada(mesaSeleccionada.get());
                    }

                    System.out.println("ocupando reserva");
                    ocuparReserva();






                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("respuesta mal");

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Mesa>> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });



    }

    /**
     * Método que cambia a la activity principal
     */
    public void cambiarActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("data",this.data);
        startActivity(intent);
    }

    /**
     * Método que crea un pedido para la mesa
     */
    public void crearPedido(){

        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<Pedido> call = api.crearPedido();
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {

                if (response.isSuccessful()) {

                    System.out.println(response.body());
                    data.getListaPlatosRestaurante().removeAll(data.getListaPlatosRestaurante());
                    Pedido pedido = (Pedido) response.body();
                    data.setPedido(pedido);
                    if(data.getMesaSeleccionada().getSitios().isEmpty()){
                        data.getPedido().setNombreMesa(data.getMesaSeleccionada().getNombre());
                    }else{
                        data.getPedido().setNombreMesa(data.getMesaSeleccionada().getSitios().get(0).getNombre());
                    }

                    modificarPedido();


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
     * Método que se encarga de ocupar la reserva de esa mesa
     */
    public void ocuparReserva(){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ResponseBody> call = api.ocuparReserva(String.valueOf(this.textView.getText()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    System.out.println("Reserva atendida");
                    crearPedido();

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
    public void modificarPedido(){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ResponseBody> call = api.modificarPedido(data.getPedido().getId(),data.getPedido());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    cambiarActivity();

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
