package modelo;

import java.util.ArrayList;

public class Pedido {
    private String nombreMesa;
    private Integer precio;
    private ArrayList<Consumicion> consumiciones;
    private String estado;
    private int id;

    public Pedido(String nombreMesa, Integer precio, ArrayList<Consumicion> consumiciones, String estado, int id) {
        this.nombreMesa = nombreMesa;
        this.precio = precio;
        this.consumiciones = consumiciones;
        this.estado = estado;
        this.id = id;
    }

    public String getNombreMesa() {
        return nombreMesa;
    }

    public Integer getPrecio() {
        return precio;
    }

    public ArrayList<Consumicion> getConsumiciones() {
        return consumiciones;
    }

    public String getEstado() {
        return estado;
    }

    public int getId() {
        return id;
    }

    public void setNombreMesa(String nombreMesa) {
        this.nombreMesa = nombreMesa;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public void setConsumiciones(ArrayList<Consumicion> consumiciones) {
        this.consumiciones = consumiciones;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setId(int id) {
        this.id = id;
    }
}
