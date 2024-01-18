package com.example.bibliotecaapi;
import java.lang.reflect.Array;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("/api/libros")
    Call<ArrayList<Libro>> getItems();
}
