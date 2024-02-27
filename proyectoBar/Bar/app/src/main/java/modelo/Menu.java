package modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Menu implements Parcelable {
    private String dia;
    private ArrayList<Plato> primeros;
    private ArrayList<Plato> segundos;
    private ArrayList<Bebida> bebidas;
    private Double precio;

    public Menu(String dia, ArrayList<Plato> primeros, ArrayList<Plato> segundos, ArrayList<Bebida> bebidas, Double precio) {
        this.dia = dia;
        this.primeros = primeros;
        this.segundos = segundos;
        this.bebidas = bebidas;
        this.precio = precio;
    }

    protected Menu(Parcel in) {
        dia = in.readString();
        primeros = in.createTypedArrayList(Plato.CREATOR);
        segundos = in.createTypedArrayList(Plato.CREATOR);
        bebidas = in.createTypedArrayList(Bebida.CREATOR);
        precio = in.readDouble();
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    public String getDia() {
        return dia;
    }

    public ArrayList<Plato> getPrimeros() {
        return primeros;
    }

    public ArrayList<Plato> getSegundos() {
        return segundos;
    }

    public ArrayList<Bebida> getBebidas() {
        return bebidas;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setPrimeros(ArrayList<Plato> primeros) {
        this.primeros = primeros;
    }

    public void setSegundos(ArrayList<Plato> segundos) {
        this.segundos = segundos;
    }

    public void setBebidas(ArrayList<Bebida> bebidas) {
        this.bebidas = bebidas;
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
        dest.writeTypedList(primeros);
        dest.writeTypedList(segundos);
        dest.writeTypedList(bebidas);
        dest.writeDouble(precio);
    }
}
