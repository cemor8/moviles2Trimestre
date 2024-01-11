package com.example.librosbasedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MiBaseDeDatosHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "biblioteca";
    private static final int DATABASE_VERSION = 1;

    public MiBaseDeDatosHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Define la estructura de tus tablas y crea las tablas aquí
        /*
        String createTableQuery = "Select * from biblioteca.libro";
        db.execSQL(createTableQuery);

         */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualiza la base de datos si es necesario
        // Por ejemplo, puedes eliminar tablas y volver a crearlas

    }
}
