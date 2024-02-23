package com.example.bar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.platos) {
                System.out.println("hola");
                selectedFragment = new PlatosFragment();
            }else if (id == R.id.menusDia){
                selectedFragment = new menusFragment();
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true; // Return true to indicate that the item selection has been handled
        });

        // Establece el fragmento inicial
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.menusDia);
        }
    }
}