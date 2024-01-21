package com.example.biblioteca;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConexionRetrofit {
    private static Retrofit instancia;
    private static final String url = "http://10.0.2.2:3000";

    public static Retrofit getConexion() {
        if (instancia == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Libro.class, new DeserializarLibro()).setLenient()
                    .create();

            instancia = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return instancia;
    }
}
