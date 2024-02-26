package modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Consumicion implements Parcelable {
    private String nombre;
    private Integer precio;
    private Integer cantidad;

    public Consumicion(String nombre, Integer precio, Integer cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    protected Consumicion(Parcel in) {
        nombre = in.readString();
        if (in.readByte() == 0) {
            precio = null;
        } else {
            precio = in.readInt();
        }
        if (in.readByte() == 0) {
            cantidad = null;
        } else {
            cantidad = in.readInt();
        }
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

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public Integer getCantidad() {
        return cantidad;
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
            dest.writeInt(precio);
        }
        if (cantidad == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cantidad);
        }
    }
}
