package com.example.biblioteca;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Libro> libros = new ArrayList<>();
    private OperacionesBase operaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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