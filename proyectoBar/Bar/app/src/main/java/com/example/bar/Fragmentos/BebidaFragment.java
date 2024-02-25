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
import com.example.bar.adaptadores.BebidasAdapter;
import com.example.bar.adaptadores.ListaBebidasAdapter;
import com.example.bar.adaptadores.PlatosAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import modelo.Bebida;
import modelo.ConexionRetrofit;
import modelo.Data;
import modelo.Menu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BebidaFragment extends Fragment {
    private Data data;
    public BebidaFragment(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bebidas, container, false);

        if(getArguments() != null){
            this.data = getArguments().getParcelable("data");
            System.out.println("comunicacion correcta");
            this.inicializar(view);
        }
        return view;
    }
    public void inicializar(View view){

        Api api = ConexionRetrofit.getConexion().create(Api.class);
        Call<ArrayList<Bebida>> call = api.getBebidas();
        call.enqueue(new Callback<ArrayList<Bebida>>() {
            @Override
            public void onResponse(Call<ArrayList<Bebida>> call, Response<ArrayList<Bebida>> response) {

                if (response.isSuccessful()) {

                    System.out.println(response.body());
                    data.getListaBebidasRestaurante().removeAll(data.getListaBebidasRestaurante());
                    ArrayList<Bebida> items = (ArrayList<Bebida>) response.body();
                    data.getListaBebidasRestaurante().addAll(items);
                    RecyclerView recyclerView = view.findViewById(R.id.contenedorListaBebidas);
                    int columnas = 2;
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnas));
                    ListaBebidasAdapter adapter = new ListaBebidasAdapter(data.getListaBebidasRestaurante());
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
            public void onFailure(Call<ArrayList<Bebida>> call, Throwable t) {
                System.out.println("error");
                System.out.println(t.getMessage());
            }
        });
    }
}
