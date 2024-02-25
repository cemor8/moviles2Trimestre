package modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Bebida implements Parcelable {
    private String nombre;
    private Integer precio;

    public Bebida(String nombre, Integer precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    protected Bebida(Parcel in) {
        this.nombre = in.readString();
        this.precio = in.readInt();
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
        dest.writeInt(precio);
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }
}
