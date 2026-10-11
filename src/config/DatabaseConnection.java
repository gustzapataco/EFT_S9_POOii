package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Administra conexión JDBC con la base de datos MySQL.
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "Gus7769+";

    private static DatabaseConnection instancia;

    private Connection conexion;

    // Constructor
    private DatabaseConnection() {
        conectar();
    }

    //  Entrega la única instancia de DatabaseConnection.
    public static DatabaseConnection getInstance() {

        if (instancia == null) {
            instancia = new DatabaseConnection();
        }

        return instancia;
    }

    // Establece la conexión con MySQL.
    private void conectar() {

        try {
            conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    CONTRASENA
            );

            System.out.println("Conexión establecida correctamente.");

        } catch (SQLException e) {

            System.out.println("Error al conectar con la base de datos: "
                            + e.getMessage());
        }
    }

    public Connection getConnection() {

        try {
            /*
             * Si la conexión no existe o se encuentra cerrada,
             * el programa intenta conectarse nuevamente.
             */
            if (conexion == null || conexion.isClosed()) {
                conectar();
            }

        } catch (SQLException e) {

            System.out.println("Error al verificar la conexión: " + e.getMessage());
        }
        return conexion;
    }
}