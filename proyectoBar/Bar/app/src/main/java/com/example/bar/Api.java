package com.example.bar;

import java.util.ArrayList;

import modelo.Bebida;
import modelo.Consumicion;
import modelo.Menu;
import modelo.MenuMeter;
import modelo.Mesa;
import modelo.Pedido;
import modelo.Plato;
import modelo.PresupuestoRequest;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
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
    @GET("/api/pedido/{id}")
    Call<Pedido> getpedido(@Path("id") int id);
    @PUT("/api/pedido/{id}")
    Call<ResponseBody> modificarPedido(@Path("id") int id, @Body Pedido pedido);
    @PUT("/api/mesa/{nombre_mesa}")
    Call<ResponseBody> modificarMesa();
    @POST("/api/pedidos")
    Call<Pedido> crearPedido();

    @PUT("/api/restarPlatos/{nombre}")
    Call<ResponseBody> restarPlatos(@Path("nombre") String nombrePlato, @Body RequestBody cantidad);
    @PUT("/api/restarBebida/{nombre}")
    Call<ResponseBody> restarBebida(@Path("nombre") String nombreBebida, @Body RequestBody cantidad);
    @PUT("/api/sumarPlatos/{nombre}")
    Call<ResponseBody> sumarPlatos(@Path("nombre") String nombrePlato, @Body RequestBody cantidad);
    @PUT("/api/sumarBebida/{nombre}")
    Call<ResponseBody> sumarBebida(@Path("nombre") String nombreBebida, @Body RequestBody cantidad);
    @PUT("/api/ocuparReserva/{nombreMesa}")
    Call<ResponseBody> ocuparReserva(@Path("nombreMesa") String nombreMesa);

    @PUT("/api/liberar/{nombreMesa}")
    Call<ResponseBody> liberarMesa(@Path("nombreMesa") String nombreMesa);
    @POST("/api/facturas")
    Call<ResponseBody> crearFactura(@Body Pedido pedido);
    @DELETE("/api/pedido/{id}")
    Call<ResponseBody> eliminarPedido(@Path("id") int id);
    @DELETE("/api/reservas/{nombreMesa}")
    Call<ResponseBody> eliminarReserva(@Path("nombreMesa") String nombreMesa);
    @POST("/factura")
    Call<ResponseBody> crearPresupuesto(@Body PresupuestoRequest presupuesto);
}
