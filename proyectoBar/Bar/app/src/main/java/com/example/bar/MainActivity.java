package com.example.bar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bar.Fragmentos.BebidaFragment;
import com.example.bar.Fragmentos.MenusFragment;
import com.example.bar.Fragmentos.PedidoFragment;
import com.example.bar.Fragmentos.PlatosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import modelo.Data;
import modelo.Pedido;

public class MainActivity extends AppCompatActivity {
    private Data data;
    private TextView textView;
    private TextView tvEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.data = getIntent().getParcelableExtra("data");
        this.textView = findViewById(R.id.nombreMesa);
        if (data.getMesaSeleccionada().getSitios().isEmpty()){
            this.textView.setText(data.getMesaSeleccionada().getNombre());
        }else{
            String str = "Barra - "+data.getMesaSeleccionada().getSitios().get(0).getNombre();
            this.textView.setText(str);
        }
        this.tvEstado = findViewById(R.id.estadoPedido);


//          Obtengo el menu inferior por el id y le establezco en método para que cambie
//          de contenido el fragmento cuando se clicka otra opcion del menu

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

//            para enviar los datos hay que pasarlos como bundle y el contenido debe implementar interfaces que permitan la seria
//                serializacion del contenido, en este caso use Parcelable que es una de ellas.

            Bundle args = new Bundle();
            args.putParcelable("data",this.data);


            int id = item.getItemId();
            if (id == R.id.platos) {
                selectedFragment = new PlatosFragment();
                selectedFragment.setArguments(args);
                this.tvEstado.setText("Pedido : "+data.getPedido().getEstado());

            }else if (id == R.id.menusDia){
                selectedFragment = new MenusFragment();
                selectedFragment.setArguments(args);
                this.tvEstado.setText("Pedido : "+data.getPedido().getEstado());
            } else if (id == R.id.bebidas) {
                selectedFragment = new BebidaFragment();
                selectedFragment.setArguments(args);
                this.tvEstado.setText("Pedido : "+data.getPedido().getEstado());
            } else if (id == R.id.pedido) {
                selectedFragment = new PedidoFragment();
                selectedFragment.setArguments(args);
                this.tvEstado.setText("Pedido : "+data.getPedido().getEstado());
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true; // Se devuelve true para indicar que se ha seleccionado un fragmento válido
        });

        // Establece el fragmento inicial
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.platos);
        }
    }
}