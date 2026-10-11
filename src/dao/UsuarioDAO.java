package dao;

import config.DatabaseConnection;
import model.Rol;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de interactuar con la tabla usuarios.
 * Además del CRUD, contiene el metodo de autenticación.
 */
public class UsuarioDAO implements CrudDAO<Usuario> {

    private final Connection conexion;

    // Obtiene la conexión administrada mediante Singleton.
    public UsuarioDAO() {
        conexion = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    // CREATE: registra un usuario nuevo.
    @Override
    public boolean guardar(Usuario usuario) {

        String sql = """
                INSERT INTO usuarios (
                    nombre,
                    rut,
                    correo,
                    contraseña,
                    rol
                )
                VALUES (?, ?, ?, ?, ?)
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
                    usuario.getNombre()
            );

            sentencia.setString(
                    2,
                    usuario.getRut()
            );

            sentencia.setString(
                    3,
                    usuario.getCorreo()
            );

            sentencia.setString(
                    4,
                    usuario.getContrasena()
            );

            /*
             * El enum se guarda en minúsculas porque MySQL
             * utiliza bibliotecario y estudiante.
             */
            sentencia.setString(
                    5,
                    usuario.getRol()
                            .name()
                            .toLowerCase()
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            if (filasAfectadas > 0) {

                try (
                        ResultSet clavesGeneradas =
                                sentencia.getGeneratedKeys()
                ) {
                    if (clavesGeneradas.next()) {
                        usuario.setId(
                                clavesGeneradas.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar el usuario: "
                            + e.getMessage()
            );
        }

        return false;
    }

    // READ: busca un usuario mediante su ID.
    @Override
    public Usuario buscarPorId(int id) {

        String sql = """
                SELECT
                    id,
                    nombre,
                    rut,
                    correo,
                    contraseña,
                    rol
                FROM usuarios
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {
                if (resultado.next()) {
                    return crearUsuario(resultado);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar el usuario: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // READ: devuelve todos los usuarios registrados.
    @Override
    public List<Usuario> listar() {

        List<Usuario> usuarios =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    nombre,
                    rut,
                    correo,
                    contraseña,
                    rol
                FROM usuarios
                ORDER BY id
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {
            while (resultado.next()) {

                Usuario usuario =
                        crearUsuario(resultado);

                usuarios.add(usuario);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar los usuarios: "
                            + e.getMessage()
            );
        }

        return usuarios;
    }

    // UPDATE: modifica un usuario existente.
    @Override
    public boolean actualizar(Usuario usuario) {

        String sql = """
                UPDATE usuarios
                SET
                    nombre = ?,
                    rut = ?,
                    correo = ?,
                    contraseña = ?,
                    rol = ?
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {
            sentencia.setString(
                    1,
                    usuario.getNombre()
            );

            sentencia.setString(
                    2,
                    usuario.getRut()
            );

            sentencia.setString(
                    3,
                    usuario.getCorreo()
            );

            sentencia.setString(
                    4,
                    usuario.getContrasena()
            );

            sentencia.setString(
                    5,
                    usuario.getRol()
                            .name()
                            .toLowerCase()
            );

            sentencia.setInt(
                    6,
                    usuario.getId()
            );

            int filasAfectadas =
                    sentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar el usuario: "
                            + e.getMessage()
            );
        }

        return false;
    }

    // DELETE: elimina un usuario mediante su ID.
    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM usuarios
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            int filasAfectadas =
                    sentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar el usuario: "
                            + e.getMessage()
            );
        }

        return false;
    }

    /**
     * Valida el correo y la contraseña ingresados.
     *
     * Si los datos son correctos, devuelve el usuario.
     * Si no coinciden, devuelve null.
     */
    public Usuario autenticar(
            String correo,
            String contrasena
    ) {
        String sql = """
                SELECT
                    id,
                    nombre,
                    rut,
                    correo,
                    contraseña,
                    rol
                FROM usuarios
                WHERE correo = ?
                  AND contraseña = ?
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, correo);
            sentencia.setString(2, contrasena);

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {
                if (resultado.next()) {
                    return crearUsuario(resultado);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error durante la autenticación: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // Convierte una fila del ResultSet en un objeto Usuario.
    private Usuario crearUsuario(
            ResultSet resultado
    ) throws SQLException {

        /*
         * MySQL entrega bibliotecario o estudiante.
         * toUpperCase() permite convertir ese texto
         * a los valores del enum Rol.
         */
        Rol rol = Rol.valueOf(
                resultado
                        .getString("rol")
                        .toUpperCase()
        );

        return new Usuario(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("rut"),
                resultado.getString("correo"),
                resultado.getString("contraseña"),
                rol
        );
    }
}