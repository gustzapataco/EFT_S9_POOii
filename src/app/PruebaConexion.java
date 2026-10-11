package app;

import config.DatabaseConnection;
import java.sql.Connection;

// Clase temporal para comprobar la conexión JDBC.
public class PruebaConexion {

    public static void main(String[] args) {
        Connection conexion =
                DatabaseConnection
                        .getInstance()
                        .getConnection();

        if (conexion != null) {
            System.out.println("Prueba finalizada: Java está conectado con MySQL.");

        } else {
            System.out.println("Prueba fallida: no fue posible conectar con MySQL.");
        }
    }
}