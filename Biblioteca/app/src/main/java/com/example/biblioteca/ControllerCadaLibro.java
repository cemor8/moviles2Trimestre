package com.example.biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ControllerCadaLibro extends AppCompatActivity {
    private ArrayList<Libro> libros;
    private TextView mostrarTitulo;
    private TextView mostrarAutor;
    private TextView mostrarPag;
    private TextView mostrarFecha;
    private Libro libroSeleccionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cada_libro);
        Intent intent = getIntent();
        if (intent.hasExtra("libros")) {
            this.libros = (ArrayList<Libro>) intent.getSerializableExtra("libros");
        }

        if(intent.hasExtra("posicionLibro")){
            this.libroSeleccionado = this.libros.get(intent.getIntExtra("posicionLibro",0));
        }
        System.out.println(this.libroSeleccionado);
        System.out.println(this.libroSeleccionado.getAutor());
        System.out.println(this.libroSeleccionado.getFecha());
        System.out.println(this.libroSeleccionado.getTitulo());
        System.out.println(this.libroSeleccionado.getPaginas());
        this.mostrarTitulo = findViewById(R.id.mostrarTitulo);
        this.mostrarAutor = findViewById(R.id.mostrarAutor);
        this.mostrarPag = findViewById(R.id.mostrarPag);
        this.mostrarFecha = findViewById(R.id.mostrarFecha);
        this.mostrarTitulo.setText(this.libroSeleccionado.getTitulo());
        this.mostrarAutor.setText(this.libroSeleccionado.getAutor());
        this.mostrarFecha.setText(this.libroSeleccionado.getFecha());
        this.mostrarPag.setText(String.valueOf(this.libroSeleccionado.getPaginas()));

    }
    public void volver(View view){
        Intent intent = new Intent(this, ControllerListaLibros.class);
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
    public void modificar(View view){
        Intent intent = new Intent(this, ControllerModificarLibro.class);
        intent.putExtra("posicionLibro", this.libros.indexOf(this.libroSeleccionado));
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
    public void eliminar(View view){
        OperacionesBase operacionesBase = OperacionesBase.getInstance(this);
        operacionesBase.borrarLibro(this.libroSeleccionado.getTitulo());
        this.libros.remove(this.libroSeleccionado);
        Intent intent = new Intent(this, ControllerListaLibros.class);
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
}
