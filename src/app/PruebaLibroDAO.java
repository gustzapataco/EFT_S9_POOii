package app;

import dao.CategoriaDAO;
import dao.LibroDAO;
import model.Categoria;
import model.Libro;

import java.util.List;

// Prueba las operaciones CRUD de LibroDAO.
public class PruebaLibroDAO {

    public static void main(String[] args) {

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        LibroDAO libroDAO = new LibroDAO();
        Categoria categoria = categoriaDAO.buscarPorId(5);

        if (categoria == null) {

            System.out.println("No se encontró la categoría.");
            return;
        }

        // CREATE
        Libro nuevoLibro = new Libro(
                "Java para principiantes",
                "Autor de Prueba",
                "9999999999999",
                "Editorial de Prueba",
                4,
                categoria
        );

        boolean guardado = libroDAO.guardar(nuevoLibro);
        if (guardado) {
            System.out.println("Libro guardado con ID: " + nuevoLibro.getId());

        } else {
            System.out.println("No fue posible guardar el libro.");
            return;
        }

        // READ: buscar por ID
        Libro libroEncontrado = libroDAO.buscarPorId(nuevoLibro.getId());

        if (libroEncontrado != null) {
            System.out.println("Libro encontrado: " + libroEncontrado.obtenerResumen());
        }

        // UPDATE
        nuevoLibro.setTitulo("Java Orientado a Objetos");
        nuevoLibro.setStock(8);
        boolean actualizado = libroDAO.actualizar(nuevoLibro);
        System.out.println("Libro actualizado: " + actualizado);

        // READ: listar
        List<Libro> libros = libroDAO.listar();

        System.out.println("\nLISTADO DE LIBROS");
        for (Libro libro : libros) {
            System.out.println(libro.obtenerResumen());
        }

        // DELETE
        boolean eliminado = libroDAO.eliminar(nuevoLibro.getId());
        System.out.println("\nLibro eliminado: " + eliminado);
    }
}