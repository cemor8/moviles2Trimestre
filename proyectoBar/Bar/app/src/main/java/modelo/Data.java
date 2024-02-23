package modelo;

import android.util.Log;

import com.example.bar.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Data {
    ArrayList<Bebida> listaBebidasRestaurante = new ArrayList<>();
    ArrayList<Plato> listaPlatosRestaurante = new ArrayList<>();
    ArrayList<Menu> listaMenusRestaurante = new ArrayList<>();
    ArrayList<Mesa> mesasRestaurante = new ArrayList<>();
    ArrayList<Factura> facturas = new ArrayList<>();

    public Data(){

    }
    public void recibirBebidas(){
        this.listaBebidasRestaurante = new ArrayList<>();
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Bebida>> call = api.getBebidas();
        call.enqueue(new Callback<ArrayList<Bebida>>() {
            @Override
            public void onResponse(Call<ArrayList<Bebida>> call, Response<ArrayList<Bebida>> response) {
                System.out.println("respuesta");
                if (response.isSuccessful()) {
                    System.out.println(response.body());
                    ArrayList<Bebida> items = (ArrayList<Bebida>) response.body();
                    listaBebidasRestaurante = items;


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
    public void recibirPlatos(){
        this.listaPlatosRestaurante = new ArrayList<>();
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Plato>> call = api.getPlatos();
        call.enqueue(new Callback<ArrayList<Plato>>() {
            @Override
            public void onResponse(Call<ArrayList<Plato>> call, Response<ArrayList<Plato>> response) {
                System.out.println("respuesta");
                if (response.isSuccessful()) {
                    System.out.println(response.body());
                    ArrayList<Plato> items = (ArrayList<Plato>) response.body();
                    listaPlatosRestaurante = items;


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
}
