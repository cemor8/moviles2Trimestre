package modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Mesa implements Parcelable {
    private String nombre_mesa;
    private Boolean ocupada;
    private String ubicacion;
    private Integer capacidad;
    private ArrayList<Sitio> sitios;

    public Mesa(String nombre, Boolean ocupada, String ubicacion, Integer capacidad, ArrayList<Sitio> sitios) {
        this.nombre_mesa = nombre;
        this.ocupada = ocupada;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
        this.sitios = sitios;
    }

    protected Mesa(Parcel in) {
        nombre_mesa = in.readString();
        byte tmpOcupada = in.readByte();
        ocupada = tmpOcupada == 0 ? null : tmpOcupada == 1;
        ubicacion = in.readString();
        if (in.readByte() == 0) {
            capacidad = null;
        } else {
            capacidad = in.readInt();
        }
        sitios = in.createTypedArrayList(Sitio.CREATOR);
    }

    public static final Creator<Mesa> CREATOR = new Creator<Mesa>() {
        @Override
        public Mesa createFromParcel(Parcel in) {
            return new Mesa(in);
        }

        @Override
        public Mesa[] newArray(int size) {
            return new Mesa[size];
        }
    };

    public String getNombre() {
        return nombre_mesa;
    }

    public Boolean getOcupada() {
        return ocupada;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public ArrayList<Sitio> getSitios() {
        return sitios;
    }

    public void setNombre(String nombre) {
        this.nombre_mesa = nombre;
    }

    public void setOcupada(Boolean ocupada) {
        this.ocupada = ocupada;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public void setSitios(ArrayList<Sitio> sitios) {
        this.sitios = sitios;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombre_mesa);
        dest.writeByte((byte) (ocupada == null ? 0 : ocupada ? 1 : 2));
        dest.writeString(ubicacion);
        if (capacidad == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(capacidad);
        }
        dest.writeTypedList(sitios);
    }

    @Override
    public String toString() {
        return "Mesa{" +
                "nombre='" + nombre_mesa + '\'' +
                ", ocupada=" + ocupada +
                ", ubicacion='" + ubicacion + '\'' +
                ", capacidad=" + capacidad +
                ", sitios=" + sitios +
                '}';
    }
}
