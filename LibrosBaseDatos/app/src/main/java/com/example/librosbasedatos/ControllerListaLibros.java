package com.example.librosbasedatos;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerListaLibros extends AppCompatActivity {
    private ArrayList<Libro> libros = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_libros);

        LinearLayout linearLayout = findViewById(R.id.linear_layout_libros);

        Intent intent = getIntent();
        if (intent.hasExtra("libros")) {
            this.libros =(ArrayList<Libro>) intent.getSerializableExtra("libros");
        }

        for (int i = 0; i < this.libros.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(this.libros.get(i).getTitulo());
            textView.setTextSize(18);
            textView.setPadding(16, 16, 16, 16);

            int finalI = i;
            textView.setOnClickListener(view -> abrirDetalleLibro(finalI));

            linearLayout.addView(textView);
        }
    }

    private void abrirDetalleLibro(int posicionLibro) {
        /*
        Intent intent = new Intent(this, OtraActividad.class);
        intent.putExtra("posicionLibro", posicionLibro);
        startActivity(intent);

         */
    }
}
