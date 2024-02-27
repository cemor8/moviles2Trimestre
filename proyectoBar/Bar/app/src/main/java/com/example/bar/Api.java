package com.example.bar;

import java.util.ArrayList;

import modelo.Bebida;
import modelo.Menu;
import modelo.Mesa;
import modelo.Pedido;
import modelo.Plato;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {
    @GET("/api/mesas")
    Call<ArrayList<Mesa>> getMesas();
    @GET("/api/bebidas")
    Call<ArrayList<Bebida>> getBebidas();
    @GET("/api/platos")
    Call<ArrayList<Plato>> getPlatos();
    @GET("/api/menusDia")
    Call<ArrayList<Menu>> getMenus();
    @GET("/api/pedidos")
    Call<ArrayList<Pedido>> getpedidos();
    @PUT("/api/pedido/{id}")
    Call<ResponseBody> modificarPedido();
    @PUT("/api/mesa/{nombre_mesa}")
    Call<ResponseBody> modificarMesa();
    @POST("/api/pedidos")
    Call<Pedido> crearPedido();
    @POST("/api/facturas")
    Call<ResponseBody> crearFactura();

}
