package com.example.bar.Fragmentos;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Api;
import com.example.bar.Margen;
import com.example.bar.R;
import com.example.bar.adaptadores.PlatosAdapter;

import java.util.ArrayList;

import modelo.ConexionRetrofit;
import modelo.Data;
import modelo.Plato;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlatosFragment extends Fragment {
    private Data data;
    public PlatosFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // se carga el fxml de listaplatos que es el que contiene el contenido y se devuelve para que el fragmento lo cargue
        View view = inflater.inflate(R.layout.listaplatos, container, false);

        if(getArguments() != null){
            this.data = getArguments().getParcelable("data");
            System.out.println("comunicacion correcta");
            this.inicializar(view);
        }

        return view;
    }

    /**
     * Método que carga la lista de platos en el recyclerview, hace una petición para
     * obtener la lista de platos disponibles
     * @param view
     */
    public void inicializar(View view){
        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Plato>> call = api.getPlatos();
        call.enqueue(new Callback<ArrayList<Plato>>() {
            @Override
            public void onResponse(Call<ArrayList<Plato>> call, Response<ArrayList<Plato>> response) {
//                si la respuesta es satisfactoria se cargan los platos de la base de datos
                if (response.isSuccessful()) {
                    System.out.println(response.body());

                    data.getListaPlatosRestaurante().removeAll(data.getListaPlatosRestaurante());

                    ArrayList<Plato> items = (ArrayList<Plato>) response.body();
                    data.getListaPlatosRestaurante().addAll(items);
//                    se rellena el recyclerview con un gridlayout con 2 columnas, se establecee el adaptador de los platos y se indica la lista
//                    a recorrer, la lista de data.getListaPlatosRestaurante(), tambien crea un espacio que es un estilo para margenes en cada elemento del
//                    gridlayout
                    RecyclerView recyclerView = view.findViewById(R.id.contenedorListaPlatos);
                    int columnas = 2;
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnas));
                    PlatosAdapter adapter = new PlatosAdapter(data.getListaPlatosRestaurante());
                    recyclerView.setAdapter(adapter);
                    int espacio = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            16,
                            getResources().getDisplayMetrics()
                    );
                    recyclerView.addItemDecoration(new Margen(espacio,false));


                }else {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    System.out.println("respuesta mal");

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Plato>> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });
    }
}
