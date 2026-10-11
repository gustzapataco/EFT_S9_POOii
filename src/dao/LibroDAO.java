package dao;

import config.DatabaseConnection;
import model.Categoria;
import model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// DAO encargado de interactuar con la tabla libros.
public class LibroDAO implements CrudDAO<Libro> {

    private final Connection conexion;

    // Obtiene la conexión administrada mediante Singleton.
    public LibroDAO() {
        conexion = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    // CREATE: registra un libro nuevo.
    @Override
    public boolean guardar(Libro libro) {

        String sql = """
                INSERT INTO libros (
                    titulo,
                    autor,
                    isbn,
                    editorial,
                    stock,
                    id_categoria
                )
                VALUES (?, ?, ?, ?, ?, ?)
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
                    libro.getTitulo()
            );

            sentencia.setString(
                    2,
                    libro.getAutor()
            );

            sentencia.setString(
                    3,
                    libro.getIsbn()
            );

            sentencia.setString(
                    4,
                    libro.getEditorial()
            );

            sentencia.setInt(
                    5,
                    libro.getStock()
            );

            sentencia.setInt(
                    6,
                    libro.getCategoria().getId()
            );

            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas > 0) {

                try (
                        ResultSet clavesGeneradas = sentencia.getGeneratedKeys()
                ) {
                    if (clavesGeneradas.next()) {
                        libro.setId(clavesGeneradas.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar el libro: " + e.getMessage());
        }
        return false;
    }

    /**
     * READ: busca un libro mediante su ID.
     *
     * Se utiliza INNER JOIN para recuperar también
     * la categoría relacionada con el libro.
     */
    @Override
    public Libro buscarPorId(int id) {

        String sql = """
                SELECT
                    l.id AS libro_id,
                    l.titulo,
                    l.autor,
                    l.isbn,
                    l.editorial,
                    l.stock,
                    c.id AS categoria_id,
                    c.nombre AS categoria_nombre
                FROM libros l
                INNER JOIN categorias c
                    ON l.id_categoria = c.id
                WHERE l.id = ?
                """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            try (
                    ResultSet resultado = sentencia.executeQuery()
            ) {
                if (resultado.next()) {
                    return crearLibro(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }

        return null;
    }

    // READ: devuelve todos los libros y sus categorías.
    @Override
    public List<Libro> listar() {

        List<Libro> libros = new ArrayList<>();

        String sql = """
                SELECT
                    l.id AS libro_id,
                    l.titulo,
                    l.autor,
                    l.isbn,
                    l.editorial,
                    l.stock,
                    c.id AS categoria_id,
                    c.nombre AS categoria_nombre
                FROM libros l
                INNER JOIN categorias c
                    ON l.id_categoria = c.id
                ORDER BY l.id
                """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                Libro libro = crearLibro(resultado);
                libros.add(libro);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar los libros: " + e.getMessage());
        }

        return libros;
    }

    // UPDATE: modifica un libro existente.
    @Override
    public boolean actualizar(Libro libro) {

        String sql = """
                UPDATE libros
                SET
                    titulo = ?,
                    autor = ?,
                    isbn = ?,
                    editorial = ?,
                    stock = ?,
                    id_categoria = ?
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {
            sentencia.setString(
                    1,
                    libro.getTitulo()
            );

            sentencia.setString(
                    2,
                    libro.getAutor()
            );

            sentencia.setString(
                    3,
                    libro.getIsbn()
            );

            sentencia.setString(
                    4,
                    libro.getEditorial()
            );

            sentencia.setInt(
                    5,
                    libro.getStock()
            );

            sentencia.setInt(
                    6,
                    libro.getCategoria().getId()
            );

            sentencia.setInt(
                    7,
                    libro.getId()
            );
             int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar el libro: " + e.getMessage());
        }
        return false;
    }

    // DELETE: elimina un libro mediante su ID.
    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM libros
                WHERE id = ?
                """;

        try (
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar el libro: " + e.getMessage());
        }

        return false;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Libro.
     *
     * Primero se construye la categoría y después
     * se agrega al libro.
     */
    private Libro crearLibro(
            ResultSet resultado
    ) throws SQLException {

        Categoria categoria = new Categoria(
                resultado.getInt("categoria_id"),
                resultado.getString("categoria_nombre")
        );

        return new Libro(
                resultado.getInt("libro_id"),
                resultado.getString("titulo"),
                resultado.getString("autor"),
                resultado.getString("isbn"),
                resultado.getString("editorial"),
                resultado.getInt("stock"),
                categoria
        );
    }
}