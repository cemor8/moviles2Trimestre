package com.example.bar.Fragmentos;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Api;
import com.example.bar.Margen;
import com.example.bar.R;
import com.example.bar.adaptadores.BebidasAdapter;
import com.example.bar.adaptadores.PrimerosAdapter;
import com.example.bar.adaptadores.SegundosAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import modelo.Bebida;
import modelo.ConexionRetrofit;
import modelo.Data;
import modelo.Menu;
import modelo.Plato;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenusFragment extends Fragment implements PrimerosAdapter.OnItemClickListener, SegundosAdapter.OnItemClickListener, BebidasAdapter.OnItemClickListener{
    private Data data;
    public MenusFragment(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menusdia, container, false);

        if(getArguments() != null){
            this.data = getArguments().getParcelable("data");
            System.out.println("comunicacion correcta");
            this.inicializar(view);
        }
        return view;
    }
    public void inicializar(View view){

        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Menu>> call = api.getMenus();
        call.enqueue(new Callback<ArrayList<Menu>>() {
            @Override
            public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {

                if (response.isSuccessful()) {

                    System.out.println(response.body());
                    data.getListaPlatosRestaurante().removeAll(data.getListaPlatosRestaurante());
                    ArrayList<Menu> items = (ArrayList<Menu>) response.body();
                    Calendar calendario = Calendar.getInstance();
                    SimpleDateFormat formato = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
                    String dia = formato.format(calendario.getTime());
                    Optional<Menu> menu = items.stream().filter(menu1 -> menu1.getDia().equalsIgnoreCase(dia)).findAny();
                    data.setMenuDia(menu.get());
                    data.setConstruirMenu(new Menu(menu.get().getDia(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),menu.get().getPrecio()));
                    recorrer(view);




                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("respuesta mal");

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });
    }
    public void recorrer(View view){
        TextView textView = view.findViewById(R.id.nombreMenuDia);
        textView.setText("Menú "+this.data.getMenuDia().getDia());
        RecyclerView recyclerView = view.findViewById(R.id.contenedorPrimeros);
        RecyclerView recyclerView2 = view.findViewById(R.id.contenedorSegundos);
        RecyclerView recyclerView3 = view.findViewById(R.id.contenedorBebidas);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        PrimerosAdapter adapter = new PrimerosAdapter(data.getMenuDia().getPrimeros());
        adapter.setOnItemClickListener(this::onItemClickPrimero);
        recyclerView.setAdapter(adapter);





        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        SegundosAdapter segundosAdapter = new SegundosAdapter(data.getMenuDia().getSegundos());
        segundosAdapter.setOnItemClickListener(this::onItemClickSegundo);
        recyclerView2.setAdapter(segundosAdapter);

        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        BebidasAdapter bebidasAdapter = new BebidasAdapter(data.getMenuDia().getBebidas());
        bebidasAdapter.setOnItemClickListener(this::onItemClickBebida);
        recyclerView3.setAdapter(bebidasAdapter);


        int espacio = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14,
                getResources().getDisplayMetrics()
        );
        recyclerView.addItemDecoration(new Margen(espacio,true));

    }

    /**
     * Método que se encarga de establecer un plato como primero en el menú a pedir
     * @param position posicion del plato en la lista
     */
    @Override
    public void onItemClickPrimero(int position) {
        Plato primero = this.data.getMenuDia().getPrimeros().get(position);
        this.data.getConstruirMenu().setPrimeros(new ArrayList<>());
        this.data.getConstruirMenu().getPrimeros().add(primero);
    }
    /**
     * Método que se encarga de establecer un plato como segundo en el menú a pedir
     * @param position posicion del plato en la lista
     */
    @Override
    public void onItemClickSegundo(int position) {
        Plato segundo = this.data.getMenuDia().getSegundos().get(position);
        this.data.getConstruirMenu().setSegundos(new ArrayList<>());
        this.data.getConstruirMenu().getSegundos().add(segundo);

    }
    /**
     * Método que se encarga de establecer una bebida en el menú a pedir
     * @param position posicion de la bebida en la lista
     */
    @Override
    public void onItemClickBebida(int position) {
        Bebida bebida = this.data.getMenuDia().getBebidas().get(position);
        this.data.getConstruirMenu().setBebidas(new ArrayList<>());
        this.data.getConstruirMenu().getBebidas().add(bebida);

    }
}
