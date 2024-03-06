package com.example.bar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import modelo.Data;
import modelo.MenuMeter;

public class ControllerVistaCadaMenuMeter extends AppCompatActivity {
    private TextView tvPrimero;
    private TextView tvSegundo;
    private TextView tvBebida;
    private ImageView imgPrimero;
    private ImageView imgSegundo;
    private ImageView imgBebida;
    private MenuMeter menuSeleccionado;
    private Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vistacadamenumeter);
        this.data = getIntent().getParcelableExtra("data");
        this.inicializar();
    }
    public void inicializar(){
        this.tvPrimero.setText(menuSeleccionado.getPrimero().getNombre());
        this.tvSegundo.setText(menuSeleccionado.getSegundo().getNombre());
        this.tvBebida.setText(menuSeleccionado.getBebida().getNombre());


    }

    public void volver(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("data",this.data);
        startActivity(intent);
    }
}
