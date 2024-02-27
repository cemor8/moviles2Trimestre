package modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Pedido implements Parcelable {
    private String nombreMesa;
    private Integer precio;
    private ArrayList<Consumicion> consumiciones = new ArrayList<>();
    private String estado;
    private int id;

    public Pedido(String nombreMesa, Integer precio, ArrayList<Consumicion> consumiciones, String estado, int id) {
        this.nombreMesa = nombreMesa;
        this.precio = precio;
        this.consumiciones = consumiciones;
        this.estado = estado;
        this.id = id;
    }

    protected Pedido(Parcel in) {
        nombreMesa = in.readString();
        if (in.readByte() == 0) {
            precio = null;
        } else {
            precio = in.readInt();
        }
        estado = in.readString();
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
        return nombreMesa;
    }

    public Integer getPrecio() {
        return precio;
    }

    public ArrayList<Consumicion> getConsumiciones() {
        return consumiciones;
    }

    public String getEstado() {
        return estado;
    }

    public int getId() {
        return id;
    }

    public void setNombreMesa(String nombreMesa) {
        this.nombreMesa = nombreMesa;
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

        dest.writeString(nombreMesa);
        if (precio == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(precio);
        }
        dest.writeString(estado);
        dest.writeInt(id);
    }
}
