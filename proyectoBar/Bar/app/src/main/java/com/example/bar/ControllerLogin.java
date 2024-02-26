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
        cambiarActivity();
        /*
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Mesa>> call = api.getMesas();
        call.enqueue(new Callback<ArrayList<Mesa>>() {
            @Override
            public void onResponse(Call<ArrayList<Mesa>> call, Response<ArrayList<Mesa>> response) {

                if (response.isSuccessful()) {

                    System.out.println(response.body());
                    data.getListaPlatosRestaurante().removeAll(data.getListaPlatosRestaurante());
                    ArrayList<Mesa> items = (ArrayList<Mesa>) response.body();
                    Optional<Mesa> mesaSeleccionada = items.stream().filter(mesa -> mesa.getOcupada()
                            && (mesa.getNombre().equalsIgnoreCase(String.valueOf(textView.getText())) || mesa.getSitios().stream().anyMatch(sitio -> sitio.getNombre().equalsIgnoreCase(String.valueOf(textView.getText()))))).findAny();
                    if (!mesaSeleccionada.isPresent()){
                        System.out.println("Mesa no encontrada");
                        return;
                    }
                    data.setMesaSeleccionada(mesaSeleccionada.get());
                    System.out.println(mesaSeleccionada);
                    cambiarActivity();




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

         */

    }
    public void cambiarActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("data",this.data);
        startActivity(intent);
    }
}
