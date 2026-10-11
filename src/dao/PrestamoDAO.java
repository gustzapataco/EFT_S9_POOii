package dao;

import config.DatabaseConnection;
import model.Categoria;
import model.Estudiante;
import model.Libro;
import model.Prestamo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// DAO encargado de interactuar con la tabla prestamos.
public class PrestamoDAO implements CrudDAO<Prestamo> {

    private final Connection conexion;

    // Obtiene la conexión administrada mediante Singleton.
    public PrestamoDAO() {
        conexion = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    // CREATE: registra un préstamo nuevo.
    @Override
    public boolean guardar(Prestamo prestamo) {

        String sql = """
                INSERT INTO prestamos (
                    id_estudiante,
                    id_libro,
                    fecha_prestamo,
                    fecha_devolucion,
                    devuelto
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {
            sentencia.setInt(
                    1,
                    prestamo.getEstudiante().getId()
            );

            sentencia.setInt(
                    2,
                    prestamo.getLibro().getId()
            );

            sentencia.setDate(
                    3,
                    Date.valueOf(
                            prestamo.getFechaPrestamo()
                    )
            );

            sentencia.setDate(
                    4,
                    Date.valueOf(
                            prestamo.getFechaDevolucion()
                    )
            );

            sentencia.setBoolean(
                    5,
                    prestamo.isDevuelto()
            );

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {

                try (
                        ResultSet clavesGeneradas = sentencia.getGeneratedKeys()
                ) {
                    if (clavesGeneradas.next()) {
                        prestamo.setId(clavesGeneradas.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar el préstamo: " + e.getMessage());
        }

        return false;
    }

    // READ: busca un préstamo mediante su ID.
    @Override
    public Prestamo buscarPorId(int id) {

        String sql = consultaPrestamos()
                + """
                  WHERE p.id = ?
                  """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            try (
                    ResultSet resultado = sentencia.executeQuery()
            ) {
                if (resultado.next()) {
                    return crearPrestamo(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el préstamo: " + e.getMessage());
        }

        return null;
    }

    // READ: devuelve todos los préstamos.
    @Override
    public List<Prestamo> listar() {

        List<Prestamo> prestamos =
                new ArrayList<>();

        String sql = consultaPrestamos()
                + """
                  ORDER BY p.id
                  """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Prestamo prestamo = crearPrestamo(resultado);
                prestamos.add(prestamo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar los préstamos: " + e.getMessage());
        }

        return prestamos;
    }

    // UPDATE: modifica un préstamo existente.
    @Override
    public boolean actualizar(Prestamo prestamo) {

        String sql = """
                UPDATE prestamos
                SET
                    id_estudiante = ?,
                    id_libro = ?,
                    fecha_prestamo = ?,
                    fecha_devolucion = ?,
                    devuelto = ?
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(
                    1,
                    prestamo.getEstudiante().getId()
            );

            sentencia.setInt(
                    2,
                    prestamo.getLibro().getId()
            );

            sentencia.setDate(
                    3,
                    Date.valueOf(
                            prestamo.getFechaPrestamo()
                    )
            );

            sentencia.setDate(
                    4,
                    Date.valueOf(
                            prestamo.getFechaDevolucion()
                    )
            );

            sentencia.setBoolean(
                    5,
                    prestamo.isDevuelto()
            );

            sentencia.setInt(
                    6,
                    prestamo.getId()
            );

            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el préstamo: " + e.getMessage());
        }

        return false;
    }

    // DELETE: elimina un préstamo mediante su ID.
    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM prestamos
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);
            int filasAfectadas = sentencia.executeUpdate();

            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar el préstamo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Consulta base utilizada para recuperar:
     *
     * - Préstamo
     * - Estudiante
     * - Libro
     * - Categoría
     */
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

    // Convierte una fila del ResultSet en un objeto Prestamo completo.
    private Prestamo crearPrestamo(
            ResultSet resultado
    ) throws SQLException {

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