package com.example.biblioteca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OperacionesBase {
    private Context context;
    private static OperacionesBase instancia;
    private SQLiteDatabase baseDatos;
    private Conexion conexion;
    private OperacionesBase(Context context) {
        try {
            conexion = new Conexion(context.getApplicationContext());
            baseDatos = conexion.getWritableDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void cerrar() {
        conexion.close();
    }
    public long meterLibro(String titulo, String autor, String fecha, Integer numPaginas) {
        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("fecha_lanzamiento", fecha);
        valores.put("autor", autor);
        valores.put("numero_paginas",numPaginas);
        System.out.println("metiendo el libro en la base de datos");
        return baseDatos.insert("libro", null, valores);
    }
    public Cursor devolverLibros() {
        return baseDatos.query("libro",  new String[] { "titulo", "autor", "numero_paginas","fecha_lanzamiento" }, null, null, null, null, null);
    }
    public int actualizarLibros(String titulo, String autor,Integer numero_paginas, String fecha_lanzamiento) {
        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("autor", autor);
        valores.put("numero_paginas", numero_paginas);
        valores.put("fecha_lanzamiento", fecha_lanzamiento);
        return baseDatos.update("libro", valores, "titulo = ?", new String[] { String.valueOf(titulo) });
    }
    public void borrarLibro(String titulo) {
        baseDatos.delete("libro", "titulo = ?", new String[] { String.valueOf(titulo) });
    }
    public SQLiteDatabase getDatabase() {
        return baseDatos;
    }
    public static synchronized OperacionesBase getInstance(Context context) {
        if (instancia == null) {
            instancia = new OperacionesBase(context.getApplicationContext());
        }
        return instancia;
    }
}
