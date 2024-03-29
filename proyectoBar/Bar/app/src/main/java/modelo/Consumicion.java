package modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Consumicion implements Parcelable {
    private String nombre;
    private Double precio;
    private Integer cantidad;
    private String tipo;

    public Consumicion(String nombre, Double precio, Integer cantidad, String tipo) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    protected Consumicion(Parcel in) {
        nombre = in.readString();
        if (in.readByte() == 0) {
            precio = null;
        } else {
            precio = in.readDouble();
        }
        if (in.readByte() == 0) {
            cantidad = null;
        } else {
            cantidad = in.readInt();
        }
        tipo = in.readString();
    }

    public static final Creator<Consumicion> CREATOR = new Creator<Consumicion>() {
        @Override
        public Consumicion createFromParcel(Parcel in) {
            return new Consumicion(in);
        }

        @Override
        public Consumicion[] newArray(int size) {
            return new Consumicion[size];
        }
    };

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombre);
        if (precio == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(precio);
        }
        if (cantidad == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cantidad);
        }
        dest.writeString(tipo);
    }
}
