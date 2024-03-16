package modelo;

import java.util.ArrayList;

public class PresupuestoRequest {
    private String nombreCliente;
    private ArrayList<Consumicion> productos;

    public PresupuestoRequest(String nombreCliente, ArrayList<Consumicion> productos) {
        this.nombreCliente = nombreCliente;
        this.productos = productos;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public ArrayList<Consumicion> getProductos() {
        return productos;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setProductos(ArrayList<Consumicion> productos) {
        this.productos = productos;
    }
}
