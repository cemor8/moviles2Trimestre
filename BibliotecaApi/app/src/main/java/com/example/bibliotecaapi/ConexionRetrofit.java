package com.example.bibliotecaapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConexionRetrofit {
    private static Retrofit instancia;
    private static final String url = "http://10.0.2.2:3000";

    public static Retrofit getConexion() {
        if (instancia == null) {
            instancia = new retrofit2.Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instancia;
    }
}
