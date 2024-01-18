package com.example.bibliotecaapi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        System.out.println("alli");
        Call<ArrayList<Libro>> call = api.getItems();
        System.out.println("aqui");
        call.enqueue(new Callback<ArrayList<Libro>>() {
            @Override
            public void onResponse(Call<ArrayList<Libro>> call, Response<ArrayList<Libro>> response) {
                System.out.println("respuesta");
                if (response.isSuccessful()) {
                    System.out.println(response.body());
                    ArrayList<Libro> items = (ArrayList<Libro>) response.body();
                    Log.d("API_RESPONSE", "Items: " + items.toString());

                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("respuesta mal");
                    Log.e("API_ERROR", "Error: ");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Libro>> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });
        System.out.println("hola");
}
}