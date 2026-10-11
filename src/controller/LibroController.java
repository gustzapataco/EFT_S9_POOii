package controller;

import dao.LibroDAO;
import model.Libro;
import java.util.List;

// Controla las operaciones de libros.
public class LibroController {

    private final LibroDAO libroDAO;
    public LibroController() {
        libroDAO = new LibroDAO();
    }
    public boolean guardar(Libro libro) {
        if (!esValido(libro)) {
            return false;
        }
        return libroDAO.guardar(libro);
    }
    public Libro buscarPorId(int id) {
        return libroDAO.buscarPorId(id);
    }
    public List<Libro> listar() {
        return libroDAO.listar();
    }
    public boolean actualizar(Libro libro) {
        if (
                !esValido(libro)
                        || libro.getId() <= 0
        ) {
            return false;
        }

        return libroDAO.actualizar(libro);
    }
    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return libroDAO.eliminar(id);
    }

    // Valida los campos obligatorios del libro.
    private boolean esValido(Libro libro) {
        return libro != null
                && libro.getTitulo() != null
                && !libro.getTitulo().isBlank()
                && libro.getAutor() != null
                && !libro.getAutor().isBlank()
                && libro.getIsbn() != null
                && !libro.getIsbn().isBlank()
                && libro.getEditorial() != null
                && !libro.getEditorial().isBlank()
                && libro.getStock() >= 0
                && libro.getCategoria() != null
                && libro.getCategoria().getId() > 0;
    }
}