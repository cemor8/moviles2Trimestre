package com.example.librosbasedatos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import java.sql.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Libro> libros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void crear(View view){
        this.obtenerLibros();
        Intent intent = new Intent(this, ControllerCrearLibro.class);
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
    public void ver(View view){
        this.obtenerLibros();
        Intent intent = new Intent(this, ControllerListaLibros.class);
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }

    public void obtenerLibros(){
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        this.libros = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            //conexion = new Conexion();
            //connection = conexion.hacerConexion("root", "",false);
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/biblioteca","root","");

            String updateSQL = "Select * from biblioteca.libro";
            preparedStatement = connection.prepareStatement(updateSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                System.out.println("ewaejaepoaejaeajpo");
                String titulo = resultSet.getString("titulo");
                String autor = resultSet.getString("autor");
                Integer numero_paginas = resultSet.getInt("numero_paginas");
                String fecha = resultSet.getString("fecha_lanzamiento");

                this.libros.add(new Libro(titulo,autor,numero_paginas,fecha));
            }

            preparedStatement.close();
            connection.close();

            //conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException err) {
                    System.out.println(err.getMessage());
                }
            }
            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }
}