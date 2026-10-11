package dao;

import config.DatabaseConnection;
import model.Categoria;
import model.Estudiante;
import model.Libro;
import model.LibroPrestadoReporte;
import model.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// DAO encargado de las consultas utilizadas para generar los reportes del sistema.
public class ReporteDAO {

    private final Connection conexion;

    public ReporteDAO() {
        conexion = DatabaseConnection
                .getInstance()
                .getConnection();
    }


    // Reporte 1: libros ordenados desde el más al menos prestado
    public List<LibroPrestadoReporte>
    listarLibrosMasPrestados() {

        List<LibroPrestadoReporte> reporte =
                new ArrayList<>();

        String sql = """
                SELECT
                    l.titulo,
                    l.autor,
                    COUNT(p.id) AS cantidad_prestamos
                FROM libros l
                INNER JOIN prestamos p
                    ON l.id = p.id_libro
                GROUP BY
                    l.id,
                    l.titulo,
                    l.autor
                ORDER BY
                    cantidad_prestamos DESC,
                    l.titulo ASC
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {
            while (resultado.next()) {

                LibroPrestadoReporte fila =
                        new LibroPrestadoReporte(
                                resultado.getString(
                                        "titulo"
                                ),
                                resultado.getString(
                                        "autor"
                                ),
                                resultado.getInt(
                                        "cantidad_prestamos"
                                )
                        );

                reporte.add(fila);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al generar el reporte "
                            + "de libros más prestados: "
                            + e.getMessage()
            );
        }

        return reporte;
    }

    // Reporte 2: historial de préstamos de un estudiante.
    public List<Prestamo> listarHistorialEstudiante(
            int idEstudiante
    ) {
        List<Prestamo> prestamos =
                new ArrayList<>();

        String sql = consultaPrestamos()
                + """
                  WHERE e.id = ?
                  ORDER BY p.fecha_prestamo DESC
                  """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, idEstudiante);

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {
                while (resultado.next()) {

                    Prestamo prestamo =
                            crearPrestamo(resultado);

                    prestamos.add(prestamo);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al consultar el historial: "
                            + e.getMessage()
            );
        }

        return prestamos;
    }

    // Reporte 3: préstamos que todavía no han sido devueltos.
    public List<Prestamo> listarPrestamosActivos() {

        List<Prestamo> prestamos =
                new ArrayList<>();

        String sql = consultaPrestamos()
                + """
                  WHERE p.devuelto = FALSE
                  ORDER BY p.fecha_devolucion ASC
                  """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {
            while (resultado.next()) {

                Prestamo prestamo =
                        crearPrestamo(resultado);

                prestamos.add(prestamo);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar los préstamos activos: "
                            + e.getMessage()
            );
        }

        return prestamos;
    }

    // Consulta base compartida por los reportes
    private String consultaPrestamos() {

        return """
                SELECT
                    p.id AS prestamo_id,
                    p.fecha_prestamo,
                    p.fecha_devolucion,
                    p.devuelto,

                    e.id AS estudiante_id,
                    e.nombre AS estudiante_nombre,
                    e.rut AS estudiante_rut,
                    e.curso AS estudiante_curso,
                    e.correo AS estudiante_correo,

                    l.id AS libro_id,
                    l.titulo AS libro_titulo,
                    l.autor AS libro_autor,
                    l.isbn AS libro_isbn,
                    l.editorial AS libro_editorial,
                    l.stock AS libro_stock,

                    c.id AS categoria_id,
                    c.nombre AS categoria_nombre

                FROM prestamos p

                INNER JOIN estudiantes e
                    ON p.id_estudiante = e.id

                INNER JOIN libros l
                    ON p.id_libro = l.id

                INNER JOIN categorias c
                    ON l.id_categoria = c.id
                """;
    }

    // Convierte el resultado SQL en un objeto Prestamo.
    private Prestamo crearPrestamo(ResultSet resultado) throws SQLException {

        Estudiante estudiante = new Estudiante(
                resultado.getInt("estudiante_id"),
                resultado.getString("estudiante_nombre"),
                resultado.getString("estudiante_rut"),
                resultado.getString("estudiante_curso"),
                resultado.getString("estudiante_correo")
        );

        Categoria categoria = new Categoria(
                resultado.getInt("categoria_id"),
                resultado.getString("categoria_nombre")
        );

        Libro libro = new Libro(
                resultado.getInt("libro_id"),
                resultado.getString("libro_titulo"),
                resultado.getString("libro_autor"),
                resultado.getString("libro_isbn"),
                resultado.getString("libro_editorial"),
                resultado.getInt("libro_stock"),
                categoria
        );

        return new Prestamo(
                resultado.getInt("prestamo_id"),
                estudiante,
                libro,
                resultado
                        .getDate("fecha_prestamo")
                        .toLocalDate(),
                resultado
                        .getDate("fecha_devolucion")
                        .toLocalDate(),
                resultado.getBoolean("devuelto")
        );
    }
}