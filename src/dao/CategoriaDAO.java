package dao;

import config.DatabaseConnection;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// DAO encargado de comunicarse con la tabla categorias.
public class CategoriaDAO implements CrudDAO<Categoria> {
    private final Connection conexion;

    // Obtiene la conexión administrada por el Singleton.
    public CategoriaDAO() {
        conexion = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    // CREATE: guarda una categoría nueva.
    @Override
    public boolean guardar(Categoria categoria) {

        String sql = """
                INSERT INTO categorias (nombre)
                VALUES (?)
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {
            sentencia.setString(1, categoria.getNombre());
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas > 0) {
                try (
                        ResultSet clavesGeneradas =
                                sentencia.getGeneratedKeys()
                ) {
                    if (clavesGeneradas.next()) {
                        categoria.setId(
                                clavesGeneradas.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar la categoría: " + e.getMessage());
        }

        return false;
    }

    // READ: busca una categoría utilizando su ID.
    @Override
    public Categoria buscarPorId(int id) {

        String sql = """
                SELECT id, nombre
                FROM categorias
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

                    return new Categoria(
                            resultado.getInt("id"),
                            resultado.getString("nombre")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al buscar la categoría: " + e.getMessage());
        }

        return null;
    }

    // READ: devuelve todas las categorías registradas.
    @Override
    public List<Categoria> listar() {

        List<Categoria> categorias = new ArrayList<>();

        String sql = """
                SELECT id, nombre
                FROM categorias
                ORDER BY id
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {
            while (resultado.next()) {

                Categoria categoria = new Categoria(
                        resultado.getInt("id"),
                        resultado.getString("nombre")
                );

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar las categorías: " + e.getMessage());
        }

        return categorias;
    }

    // UPDATE: modifica el nombre de una categoría.
    @Override
    public boolean actualizar(Categoria categoria) {

        String sql = """
                UPDATE categorias
                SET nombre = ?
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, categoria.getNombre());
            sentencia.setInt(2, categoria.getId());

            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar la categoría: " + e.getMessage());
        }

        return false;
    }

    // DELETE: elimina una categoría utilizando su ID.
    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM categorias
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar la categoría: " + e.getMessage());
        }

        return false;
    }
}