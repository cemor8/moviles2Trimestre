package modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MenuMeter implements Parcelable{
    private String dia;
    private Plato primero;
    private Plato segundo;
    private Bebida bebida;
    private Integer cantidad;
    private Double precio;

    public MenuMeter(String dia, Plato primero, Plato segundo, Bebida bebida, Integer cantidad, Double precio) {
        this.dia = dia;
        this.primero = primero;
        this.segundo = segundo;
        this.bebida = bebida;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    protected MenuMeter(Parcel in) {
        dia = in.readString();
        primero = in.readTypedObject(Plato.CREATOR);
        segundo = in.readTypedObject(Plato.CREATOR);
        bebida = in.readTypedObject(Bebida.CREATOR);
        cantidad = in.readInt();
        precio = in.readDouble();
    }

    public static final Parcelable.Creator<MenuMeter> CREATOR = new Parcelable.Creator<MenuMeter>() {
        @Override
        public MenuMeter createFromParcel(Parcel in) {
            return new MenuMeter(in);
        }

        @Override
        public MenuMeter[] newArray(int size) {
            return new MenuMeter[size];
        }
    };

    public String getDia() {
        return dia;
    }

    public Plato getPrimero() {
        return primero;
    }

    public void setPrimero(Plato primero) {
        this.primero = primero;
    }

    public void setSegundo(Plato segundo) {
        this.segundo = segundo;
    }

    public void setBebida(Bebida bebida) {
        this.bebida = bebida;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Plato getSegundo() {
        return segundo;
    }

    public Bebida getBebida() {
        return bebida;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }



    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(dia);
        dest.writeTypedObject(primero,flags);
        dest.writeTypedObject(segundo,flags);
        dest.writeTypedObject(bebida,flags);
        dest.writeInt(cantidad);
        dest.writeDouble(precio);
    }

    @Override
    public String toString() {
        return "MenuMeter{" +
                "dia='" + dia + '\'' +
                ", primero=" + primero +
                ", segundo=" + segundo +
                ", bebida=" + bebida +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                '}';
    }
}
