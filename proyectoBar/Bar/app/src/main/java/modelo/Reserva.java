package modelo;

import java.util.ArrayList;

public class Reserva {
    private String dni;
    private ArrayList<Mesa> mesas;

    public Reserva(String dni, ArrayList<Mesa> mesas) {
        this.dni = dni;
        this.mesas = mesas;
    }

    public String getDni() {
        return dni;
    }

    public ArrayList<Mesa> getMesas() {
        return mesas;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setMesas(ArrayList<Mesa> mesas) {
        this.mesas = mesas;
    }
}
