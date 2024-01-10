package com.example.librosbasedatos;

import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerCrearLibro extends AppCompatActivity {
    EditText introducirTitulo;
    EditText introducirAutor;
    EditText introducirPaginas;
    EditText introducirFecha;
    private ArrayList<Libro> libros = new ArrayList<>();
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("Paginas", "^\\d{1,5}$");
            put("Titulo", "^[\\w\\s.,!?-]{1,100}$");
            put("Autor", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
            put("Fecha", "^\\d{4}-\\d{2}-\\d{2}$");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);
        this.introducirTitulo = findViewById(R.id.introducirTitulo);
        this.introducirAutor = findViewById(R.id.introducirAutor);
        this.introducirPaginas = findViewById(R.id.introducirPaginas);
        this.introducirFecha = findViewById(R.id.introducirFecha);
    }
    /**
     * Método que se encarga de crear un libro, comprueba el texto
     * */
    public void crearLibro(View view){
        boolean error = false;
        if(!validarDatos(this.introducirTitulo.getText().toString(),this.columnasExpresiones.get("Titulo"))){
            error = true;
        }
        if(!validarDatos(this.introducirAutor.getText().toString(),this.columnasExpresiones.get("Autor"))){
            error = true;
        }
        if(!validarDatos(this.introducirPaginas.getText().toString(),this.columnasExpresiones.get("Paginas"))){
            error = true;
        }
        if(!validarDatos(this.introducirFecha.getText().toString(),this.columnasExpresiones.get("Fecha"))){
            error = true;
        }
        if (error){
            return;
        }
        Optional<Libro> libroRepetidoOptional = this.libros.stream().filter(libro -> libro.getTitulo().equalsIgnoreCase(this.introducirTitulo.getText().toString())).findAny();
        if(libroRepetidoOptional.isPresent()){
            return;
        }
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion("root", "",false);

            String insertSQL = "INSERT INTO libro (titulo, autor, numero_paginas, fecha_lanzamiento) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1,this.introducirTitulo.getText().toString());
            preparedStatement.setString(2,this.introducirAutor.getText().toString());
            preparedStatement.setInt(3,Integer.parseInt(this.introducirPaginas.getText().toString()));
            preparedStatement.setString(4,this.introducirFecha.getText().toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            conexion.cerrarConexion();
        }catch (SQLException err){
            System.out.println(err.getErrorCode());
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException err) {
                System.out.println(err.getMessage());
            }

            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }


    /**
     * Método que se encarga de validar los datos para que se cumpla la
     * expresion regular.
     *
     * @param patronCumplir patron a cumplir
     * @param textoBuscar   string donde buscar el patron
     */
    public boolean validarDatos(String patronCumplir, String textoBuscar) {
        Pattern patron = Pattern.compile(patronCumplir);
        Matcher matcher = patron.matcher(textoBuscar);
        return matcher.matches();
    }
}
