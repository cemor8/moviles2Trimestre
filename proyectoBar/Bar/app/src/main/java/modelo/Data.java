package modelo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bar.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Data implements Parcelable {
    ArrayList<Bebida> listaBebidasRestaurante = new ArrayList<>();
    ArrayList<Plato> listaPlatosRestaurante = new ArrayList<>();
    private Mesa mesaSeleccionada;
    private Menu menuDia;
    private Menu construirMenu;

    ArrayList<Mesa> mesasRestaurante = new ArrayList<>();
    private Pedido pedido;
    /*
    ArrayList<Factura> facturas = new ArrayList<>();
    */


    public Data(){

    }
    /**
     * Método para poder pasar Data entre controladores
     */
    protected Data(Parcel in) {
        this.listaBebidasRestaurante = in.createTypedArrayList(Bebida.CREATOR);
        this.listaPlatosRestaurante = in.createTypedArrayList(Plato.CREATOR);
        this.mesaSeleccionada = in.readParcelable(Mesa.class.getClassLoader());
        this.menuDia = in.readParcelable(Menu.class.getClassLoader());
        this.construirMenu = in.readParcelable(Menu.class.getClassLoader());
        this.mesasRestaurante = in.createTypedArrayList(Mesa.CREATOR);
        this.pedido = in.readParcelable(Pedido.class.getClassLoader());
    }

    /**
     * Método para poder pasar Data entre controladores
     */
    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.listaBebidasRestaurante);
        dest.writeTypedList(this.listaPlatosRestaurante);
        dest.writeParcelable(this.mesaSeleccionada,flags);
        dest.writeParcelable(this.menuDia,flags);
        dest.writeParcelable(this.construirMenu,flags);
        dest.writeTypedList(this.mesasRestaurante);
        dest.writeParcelable(this.pedido,flags);
    }

    public Mesa getMesaSeleccionada() {
        return mesaSeleccionada;
    }

    public void setListaBebidasRestaurante(ArrayList<Bebida> listaBebidasRestaurante) {
        this.listaBebidasRestaurante = listaBebidasRestaurante;
    }

    public void setListaPlatosRestaurante(ArrayList<Plato> listaPlatosRestaurante) {
        this.listaPlatosRestaurante = listaPlatosRestaurante;
    }

    public void setMesaSeleccionada(Mesa mesaSeleccionada) {
        this.mesaSeleccionada = mesaSeleccionada;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ArrayList<Bebida> getListaBebidasRestaurante() {
        return listaBebidasRestaurante;
    }

    public ArrayList<Plato> getListaPlatosRestaurante() {
        return listaPlatosRestaurante;
    }

    public Menu getMenuDia() {
        return menuDia;
    }

    public void setMenuDia(Menu menuDia) {
        this.menuDia = menuDia;
    }

    public Menu getConstruirMenu() {
        return construirMenu;
    }

    public void setConstruirMenu(Menu construirMenu) {
        this.construirMenu = construirMenu;
    }

    public ArrayList<Mesa> getMesasRestaurante() {
        return mesasRestaurante;
    }

    public void setMesasRestaurante(ArrayList<Mesa> mesasRestaurante) {
        this.mesasRestaurante = mesasRestaurante;
    }
}