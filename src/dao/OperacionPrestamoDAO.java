package dao;

import config.DatabaseConnection;
import model.Estudiante;
import model.Libro;
import model.Prestamo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO encargado de las operaciones críticas de préstamos.
 *
 * Utiliza transacciones para asegurar que el préstamo
 * y la modificación del stock se completen juntos.
 */
public class OperacionPrestamoDAO {

    private final Connection conexion;

    public OperacionPrestamoDAO() {
        conexion = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    /**
     * Registra un préstamo y disminuye el stock.
     *
     * Si una operación falla, se ejecuta rollback
     * y no se guarda ningún cambio.
     */
    public boolean registrarPrestamo(
            Prestamo prestamo
    ) {
        String consultarStock = """
                SELECT stock
                FROM libros
                WHERE id = ?
                FOR UPDATE
                """;

        String insertarPrestamo = """
                INSERT INTO prestamos (
                    id_estudiante,
                    id_libro,
                    fecha_prestamo,
                    fecha_devolucion,
                    devuelto
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        String disminuirStock = """
                UPDATE libros
                SET stock = stock - 1
                WHERE id = ?
                  AND stock > 0
                """;

        try {
            /*
             * Desactivamos el guardado automático.
             * Desde aquí comienza la transacción.
             */
            conexion.setAutoCommit(false);

            int stockActual;

            /*
             * FOR UPDATE bloquea temporalmente la fila del libro
             * mientras se completa la operación.
             */
            try (
                    PreparedStatement sentenciaStock =
                            conexion.prepareStatement(
                                    consultarStock
                            )
            ) {
                sentenciaStock.setInt(
                        1,
                        prestamo.getLibro().getId()
                );

                try (
                        ResultSet resultado =
                                sentenciaStock.executeQuery()
                ) {
                    if (!resultado.next()) {

                        conexion.rollback();

                        System.out.println(
                                "El libro no existe."
                        );

                        return false;
                    }

                    stockActual = resultado.getInt("stock");
                }
            }

            if (stockActual <= 0) {
                conexion.rollback();
                System.out.println("El libro no tiene stock disponible.");
                return false;
            }

            // Registramos el préstamo.
            try (
                    PreparedStatement sentenciaPrestamo =
                            conexion.prepareStatement(
                                    insertarPrestamo,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {
                sentenciaPrestamo.setInt(
                        1,
                        prestamo.getEstudiante().getId()
                );

                sentenciaPrestamo.setInt(
                        2,
                        prestamo.getLibro().getId()
                );

                sentenciaPrestamo.setDate(
                        3,
                        Date.valueOf(
                                prestamo.getFechaPrestamo()
                        )
                );

                sentenciaPrestamo.setDate(
                        4,
                        Date.valueOf(
                                prestamo.getFechaDevolucion()
                        )
                );

                sentenciaPrestamo.setBoolean(
                        5,
                        prestamo.isDevuelto()
                );

                int filasPrestamo =
                        sentenciaPrestamo.executeUpdate();

                if (filasPrestamo == 0) {

                    conexion.rollback();

                    return false;
                }

                try (
                        ResultSet clavesGeneradas =
                                sentenciaPrestamo
                                        .getGeneratedKeys()
                ) {
                    if (clavesGeneradas.next()) {
                        prestamo.setId(
                                clavesGeneradas.getInt(1)
                        );
                    }
                }
            }

            // Disminuimos el stock del libro.
            try (
                    PreparedStatement sentenciaStock =
                            conexion.prepareStatement(
                                    disminuirStock
                            )
            ) {
                sentenciaStock.setInt(
                        1,
                        prestamo.getLibro().getId()
                );

                int filasStock = sentenciaStock.executeUpdate();

                if (filasStock == 0) {

                    conexion.rollback();

                    return false;
                }
            }

            // Confirmamos ambas operaciones.
            conexion.commit();

            prestamo.getLibro().setStock(
                    stockActual - 1
            );

            return true;

        } catch (SQLException e) {

            try {
                conexion.rollback();
            } catch (SQLException errorRollback) {

                System.out.println(
                        "Error durante rollback: "
                                + errorRollback.getMessage()
                );
            }

            System.out.println(
                    "Error al registrar el préstamo: "
                            + e.getMessage()
            );

            return false;

        } finally {

            try {
                conexion.setAutoCommit(true);
            } catch (SQLException e) {

                System.out.println(
                        "Error al restaurar AutoCommit: "
                                + e.getMessage()
                );
            }
        }
    }

    // Marca el préstamo como devuelto y aumenta el stock.
    public boolean registrarDevolucion(
            Prestamo prestamo
    ) {
        String consultarPrestamo = """
                SELECT devuelto
                FROM prestamos
                WHERE id = ?
                FOR UPDATE
                """;

        String actualizarPrestamo = """
                UPDATE prestamos
                SET devuelto = TRUE
                WHERE id = ?
                  AND devuelto = FALSE
                """;

        String aumentarStock = """
                UPDATE libros
                SET stock = stock + 1
                WHERE id = ?
                """;

        try {
            conexion.setAutoCommit(false);

            boolean yaDevuelto;

            try (
                    PreparedStatement sentenciaConsulta =
                            conexion.prepareStatement(
                                    consultarPrestamo
                            )
            ) {
                sentenciaConsulta.setInt(
                        1,
                        prestamo.getId()
                );

                try (
                        ResultSet resultado =
                                sentenciaConsulta.executeQuery()
                ) {
                    if (!resultado.next()) {

                        conexion.rollback();

                        System.out.println(
                                "El préstamo no existe."
                        );

                        return false;
                    }

                    yaDevuelto =
                            resultado.getBoolean("devuelto");
                }
            }

            if (yaDevuelto) {

                conexion.rollback();

                System.out.println(
                        "El préstamo ya fue devuelto."
                );

                return false;
            }

            try (
                    PreparedStatement sentenciaPrestamo =
                            conexion.prepareStatement(
                                    actualizarPrestamo
                            )
            ) {
                sentenciaPrestamo.setInt(
                        1,
                        prestamo.getId()
                );

                int filasPrestamo =
                        sentenciaPrestamo.executeUpdate();

                if (filasPrestamo == 0) {

                    conexion.rollback();

                    return false;
                }
            }

            try (
                    PreparedStatement sentenciaStock =
                            conexion.prepareStatement(
                                    aumentarStock
                            )
            ) {
                sentenciaStock.setInt(
                        1,
                        prestamo.getLibro().getId()
                );

                int filasStock =
                        sentenciaStock.executeUpdate();

                if (filasStock == 0) {

                    conexion.rollback();

                    return false;
                }
            }

            conexion.commit();

            prestamo.setDevuelto(true);

            prestamo.getLibro().setStock(
                    prestamo.getLibro().getStock() + 1
            );

            return true;

        } catch (SQLException e) {

            try {
                conexion.rollback();
            } catch (SQLException errorRollback) {

                System.out.println("Error durante rollback: " + errorRollback.getMessage());
            }

            System.out.println("Error al registrar la devolución: " + e.getMessage());
            return false;

        } finally {

            try {
                conexion.setAutoCommit(true);
            } catch (SQLException e) {

                System.out.println("Error al restaurar AutoCommit: " + e.getMessage());
            }
        }
    }
}