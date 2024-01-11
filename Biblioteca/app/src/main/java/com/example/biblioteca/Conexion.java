package com.example.biblioteca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.SpringAnimation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion extends SQLiteOpenHelper {
    private static String nombreBaseDatos = "biblioteca.db";
    private static final int version = 1;

    public Conexion(Context context) {
        super(context, nombreBaseDatos, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase baseDatos) {
        String consulta = "Create table libro (titulo, autor, numero_paginas, fecha_lanzamiento)";
        baseDatos.execSQL(consulta);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS libro");
        onCreate(db);
    }
}
