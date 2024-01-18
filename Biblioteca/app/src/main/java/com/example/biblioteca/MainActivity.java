package com.example.biblioteca;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.*;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Libro> libros = new ArrayList<>();
    private OperacionesBase operaciones;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Libro>> call = api.getLibros();
        call.enqueue(new Callback<ArrayList<Libro>>() {
            @Override
            public void onResponse(Call<ArrayList<Libro>> call, Response<ArrayList<Libro>> response) {
                System.out.println("respuesta");
                if (response.isSuccessful()) {
                    System.out.println(response.body());
                    ArrayList<Libro> items = (ArrayList<Libro>) response.body();
                    for (Libro libro : items){
                        libros.add(libro);
                    }
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
        for (Libro libro : libros){
            System.out.println(libro);
        }

        /*
        operaciones = OperacionesBase.getInstance(this);
        Cursor cursor = null;
        try {
            cursor = operaciones.devolverLibros();
        }catch (Exception err){
            System.out.println(err.getMessage());
        }

        if (cursor != null) {
            int tituloIndex = cursor.getColumnIndex("titulo");
            int autorIndex = cursor.getColumnIndex("autor");
            int numeroPaginasIndex = cursor.getColumnIndex("numero_paginas");
            int fechaLanzamientoIndex = cursor.getColumnIndex("fecha_lanzamiento");

            while (cursor.moveToNext()) {
                String titulo = cursor.getString(tituloIndex);
                String autor = cursor.getString(autorIndex);
                int numeroPaginas = cursor.getInt(numeroPaginasIndex);
                String fechaLanzamiento = cursor.getString(fechaLanzamientoIndex);
                Libro libro = new Libro(titulo, autor, numeroPaginas, fechaLanzamiento);
                this.libros.add(libro);
            }

            cursor.close();
        }

         */

    }
    public void crear(View view){
        Intent intent = new Intent(this, ControllerCrearLibro.class);
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
    public void ver(View view){
        Intent intent = new Intent(this, ControllerListaLibros.class);
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
}