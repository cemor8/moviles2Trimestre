package com.example.bar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import modelo.Data;

public class ControllerLogin extends AppCompatActivity {
    private Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.data = new Data();
    }

    /**
     * Método para cambiar de stage al menu de los platos y pedidos
     * @param view
     */
    public void login(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("data",this.data);
        startActivity(intent);
    }
}
