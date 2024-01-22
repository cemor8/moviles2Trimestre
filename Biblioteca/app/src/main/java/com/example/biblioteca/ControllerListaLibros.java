package com.example.biblioteca;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
        // crear un textview para cada libro
        for (int i = 0; i < this.libros.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(this.libros.get(i).getTitulo());
            textView.setTextSize(18);
            textView.setPadding(0, 160, 0, 0);
            textView.setTextColor(getResources().getColor(R.color.white));

            int finalI = i;

            textView.setOnClickListener(view -> abrirDetalleLibro(finalI));

            // Configurar la gravedad para centrar verticalmente el TextView
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.gravity = Gravity.CENTER;
            textView.setLayoutParams(layoutParams);

            linearLayout.addView(textView);
        }
    }
    /**
     * Método que se encarga de volver a la anterior actividad
     * */
    public void volver(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Método que abre la actividad donde se muestra cada libro, con los datos
     * del libro seleccionado
     * */
    private void abrirDetalleLibro(int posicionLibro) {
        Intent intent = new Intent(this, ControllerCadaLibro.class);
        intent.putExtra("posicionLibro", posicionLibro);
        intent.putExtra("libros",this.libros);
        startActivity(intent);
    }
}
