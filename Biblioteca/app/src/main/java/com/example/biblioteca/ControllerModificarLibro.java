package com.example.biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerModificarLibro extends AppCompatActivity {
    private ArrayList<Libro> libros;
    private Libro libroSeleccionado;
    private EditText modificarAutor;
    private EditText modificarTitulo;
    private EditText modificarPag;
    private EditText modificarFecha;
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
        setContentView(R.layout.modificar_libro);
        Intent intent = getIntent();
        if (intent.hasExtra("libros")) {
            this.libros = (ArrayList<Libro>) intent.getSerializableExtra("libros");
        }

        if(intent.hasExtra("posicionLibro")){
            this.libroSeleccionado = this.libros.get(intent.getIntExtra("posicionLibro",0));
        }
        this.modificarAutor = findViewById(R.id.modificarAutor);
        this.modificarFecha = findViewById(R.id.modificarFecha);
        this.modificarPag = findViewById(R.id.modificarPag);
        this.modificarTitulo = findViewById(R.id.modificarTitulo);

    }
    public void volver(View view){
        Intent intent = new Intent(this, ControllerCadaLibro.class);
        intent.putExtra("posicionLibro", this.libros.indexOf(this.libroSeleccionado));
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
    public void guardar(View view){
        boolean error = false;
        if(!validarDatos(this.columnasExpresiones.get("Titulo"),this.modificarTitulo.getText().toString())){
            error = true;
        }
        if(!validarDatos(this.columnasExpresiones.get("Autor"),this.modificarAutor.getText().toString())){
            error = true;
        }
        if(!validarDatos(this.columnasExpresiones.get("Paginas"),this.modificarPag.getText().toString())){
            error = true;
        }
        if(!validarDatos(this.columnasExpresiones.get("Fecha"),this.modificarFecha.getText().toString())){
            error = true;
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

    @Override
    protected void onDestroy() {
        //operacionesBase.cerrar();
        super.onDestroy();
    }
}
