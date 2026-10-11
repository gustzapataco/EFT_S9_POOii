package dao;

import config.DatabaseConnection;
import model.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// DAO encargado de interactuar con la tabla estudiantes.
public class EstudianteDAO implements CrudDAO<Estudiante> {
    private final Connection conexion;

    // Obtiene la conexión administrada mediante Singleton.
    public EstudianteDAO() {
        conexion = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    // CREATE: registra un estudiante.
    @Override
    public boolean guardar(Estudiante estudiante) {

        String sql = """
                INSERT INTO estudiantes (
                    nombre,
                    rut,
                    curso,
                    correo
                )
                VALUES (?, ?, ?, ?)
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {
            sentencia.setString(
                    1,
                    estudiante.getNombre()
            );

            sentencia.setString(
                    2,
                    estudiante.getRut()
            );

            sentencia.setString(
                    3,
                    estudiante.getCurso()
            );

            sentencia.setString(
                    4,
                    estudiante.getCorreo()
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            if (filasAfectadas > 0) {

                try (
                        ResultSet clavesGeneradas =
                                sentencia.getGeneratedKeys()
                ) {
                    if (clavesGeneradas.next()) {
                        estudiante.setId(
                                clavesGeneradas.getInt(1)
                        );
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar el estudiante: " + e.getMessage());
        }
        return false;
    }

    // READ: busca un estudiante por su ID.
    @Override
    public Estudiante buscarPorId(int id) {

        String sql = """
                SELECT
                    id,
                    nombre,
                    rut,
                    curso,
                    correo
                FROM estudiantes
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);
            try (
                    ResultSet resultado = sentencia.executeQuery()
            ) {
                if (resultado.next()) {
                    return crearEstudiante(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el estudiante: " + e.getMessage());
        }

        return null;
    }

    // READ: devuelve todos los estudiantes.
    @Override
    public List<Estudiante> listar() {

        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    nombre,
                    rut,
                    curso,
                    correo
                FROM estudiantes
                ORDER BY id
                """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Estudiante estudiante = crearEstudiante(resultado);
                estudiantes.add(estudiante);
            }

        } catch (SQLException e) {

            System.out.println("Error al listar los estudiantes: " + e.getMessage());
        }

        return estudiantes;
    }

    // UPDATE: modifica un estudiante existente.
    @Override
    public boolean actualizar(Estudiante estudiante) {

        String sql = """
                UPDATE estudiantes
                SET
                    nombre = ?,
                    rut = ?,
                    curso = ?,
                    correo = ?
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(
                    1,
                    estudiante.getNombre()
            );

            sentencia.setString(
                    2,
                    estudiante.getRut()
            );

            sentencia.setString(
                    3,
                    estudiante.getCurso()
            );

            sentencia.setString(
                    4,
                    estudiante.getCorreo()
            );

            sentencia.setInt(
                    5,
                    estudiante.getId()
            );

            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println("Error al actualizar el estudiante: " + e.getMessage());
        }

        return false;
    }

    // DELETE: elimina un estudiante por su ID.
    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM estudiantes
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar el estudiante: " + e.getMessage());
        }
        return false;
    }

    // Convierte una fila del ResultSet en un objeto Estudiante.
    private Estudiante crearEstudiante(
            ResultSet resultado
    ) throws SQLException {

        return new Estudiante(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("rut"),
                resultado.getString("curso"),
                resultado.getString("correo")
        );
    }
}