package com.example.librosbasedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private Connection conexionDevolver=null;
    /**
     * Método que se encarga de hacer una conexion a la base de datos con el usuario que se le pasa al método y devolverla.
     * @param usuario nombre de usuario
     * @param password contraseña del usuario
     * */
    public Connection hacerConexion(String usuario, String password, boolean crear){

        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/biblioteca";
        if(crear){
            jdbcUrl = "jdbc:mysql://127.0.0.1:3306";
        }
        try {
            conexionDevolver= DriverManager.getConnection(jdbcUrl,usuario,password);
        }catch (SQLException err){
            System.out.println(err.getMessage());
        }
        return conexionDevolver;
    }
    /**
     * Método que se encarga de cerrar una conexion a la base de datos.
     * */
    public void cerrarConexion(){
        if(conexionDevolver!=null){
            try {
                conexionDevolver.close();
            }catch (SQLException err){
                System.out.println(err.getMessage());
            }
            conexionDevolver=null;
        }
    }
}
