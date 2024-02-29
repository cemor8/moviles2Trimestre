package modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Bebida implements Parcelable {
    private String nombre;
    private Double precio;
    private Integer cantidad;

    public Bebida(String nombre, Double precio, Integer cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    protected Bebida(Parcel in) {
        this.nombre = in.readString();
        this.precio = in.readDouble();
        this.cantidad = in.readInt();
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeDouble(precio);
        dest.writeInt(cantidad);
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
