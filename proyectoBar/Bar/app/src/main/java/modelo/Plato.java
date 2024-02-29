package modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import retrofit2.Call;

public class Plato implements Parcelable {
    private String nombre;
    private Double precio;
    private Integer cantidad;

    public Plato(String nombre, Double precio, Integer cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    protected Plato(Parcel in) {
        nombre = in.readString();
        precio = in.readDouble();
        cantidad = in.readInt();
    }

    public static final Creator<Plato> CREATOR = new Creator<Plato>() {
        @Override
        public Plato createFromParcel(Parcel in) {
            return new Plato(in);
        }

        @Override
        public Plato[] newArray(int size) {
            return new Plato[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
        dest.writeString(nombre);
        dest.writeDouble(precio);
        dest.writeInt(cantidad);
    }

    @Override
    public String toString() {
        return "Plato{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                '}';
    }
}
