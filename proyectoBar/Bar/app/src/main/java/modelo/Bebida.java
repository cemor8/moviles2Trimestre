package modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Bebida implements Parcelable {
    private String nombre;
    private Double precio;

    public Bebida(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    protected Bebida(Parcel in) {
        this.nombre = in.readString();
        this.precio = in.readDouble();
    }

    public static final Creator<Bebida> CREATOR = new Creator<Bebida>() {
        @Override
        public Bebida createFromParcel(Parcel in) {
            return new Bebida(in);
        }

        @Override
        public Bebida[] newArray(int size) {
            return new Bebida[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeDouble(precio);
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
