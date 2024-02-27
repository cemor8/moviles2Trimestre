package modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Sitio implements Parcelable {
    private String nombre;
    private Boolean ocupado;

    public Sitio(String nombre, Boolean ocupado) {
        this.nombre = nombre;
        this.ocupado = ocupado;
    }

    protected Sitio(Parcel in) {
        nombre = in.readString();
        byte tmpOcupado = in.readByte();
        ocupado = tmpOcupado == 0 ? null : tmpOcupado == 1;
    }

    public static final Creator<Sitio> CREATOR = new Creator<Sitio>() {
        @Override
        public Sitio createFromParcel(Parcel in) {
            return new Sitio(in);
        }

        @Override
        public Sitio[] newArray(int size) {
            return new Sitio[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public Boolean getOcupado() {
        return ocupado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setOcupado(Boolean ocupado) {
        this.ocupado = ocupado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeByte((byte) (ocupado == null ? 0 : ocupado ? 1 : 2));
    }

    @Override
    public String toString() {
        return "Sitio{" +
                "nombre='" + nombre + '\'' +
                ", ocupado=" + ocupado +
                '}';
    }
}
