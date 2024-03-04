package modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Pedido implements Parcelable {
    private String nombre_mesa;
    private Integer precio;
    private ArrayList<Consumicion> consumiciones = new ArrayList<>();
    private ArrayList<MenuMeter> menus = new ArrayList<>();
    private String estado;
    private int id;

    public Pedido(String nombre_mesa, Integer precio, ArrayList<Consumicion> consumiciones, ArrayList<MenuMeter> menus, String estado, int id) {
        this.nombre_mesa = nombre_mesa;
        this.precio = precio;
        this.consumiciones = consumiciones;
        this.menus = menus;
        this.estado = estado;
        this.id = id;
    }

    protected Pedido(Parcel in) {
        nombre_mesa = in.readString();
        if (in.readByte() == 0) {
            precio = null;
        } else {
            precio = in.readInt();
        }
        estado = in.readString();
        menus = in.createTypedArrayList(MenuMeter.CREATOR);
        consumiciones = in.createTypedArrayList(Consumicion.CREATOR);
        id = in.readInt();
    }

    public static final Creator<Pedido> CREATOR = new Creator<Pedido>() {
        @Override
        public Pedido createFromParcel(Parcel in) {
            return new Pedido(in);
        }

        @Override
        public Pedido[] newArray(int size) {
            return new Pedido[size];
        }
    };

    public String getNombreMesa() {
        return nombre_mesa;
    }

    public Integer getPrecio() {
        return precio;
    }

    public ArrayList<Consumicion> getConsumiciones() {
        return consumiciones;
    }

    public String getNombre_mesa() {
        return nombre_mesa;
    }

    public void setNombre_mesa(String nombre_mesa) {
        this.nombre_mesa = nombre_mesa;
    }

    public void setMenus(ArrayList<MenuMeter> menus) {
        this.menus = menus;
    }

    public ArrayList<MenuMeter> getMenus() {
        return menus;
    }

    public String getEstado() {
        return estado;
    }

    public int getId() {
        return id;
    }

    public void setNombreMesa(String nombreMesa) {
        this.nombre_mesa = nombreMesa;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(nombre_mesa);
        if (precio == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(precio);
        }
        dest.writeString(estado);
        dest.writeInt(id);
        dest.writeTypedList(menus);
        dest.writeTypedList(consumiciones);
    }
}
