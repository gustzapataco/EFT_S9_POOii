package app;

import dao.CategoriaDAO;
import model.Categoria;

import java.util.List;

// Prueba las operaciones CRUD de CategoriaDAO.
public class PruebaCategoriaDAO {

    public static void main(String[] args) {

        CategoriaDAO categoriaDAO = new CategoriaDAO();

        // CREATE: crear y guardar una categoría.
        Categoria nuevaCategoria =
                new Categoria("Programación");

        boolean guardada = categoriaDAO.guardar(nuevaCategoria);

        if (guardada) {
            System.out.println("Categoría guardada con ID: "
                            + nuevaCategoria.getId()
            );
        } else {
            System.out.println("No fue posible guardar la categoría.");
            return;
        }

        // READ: buscar la categoría por su ID.
        Categoria categoriaEncontrada = categoriaDAO.buscarPorId(
                        nuevaCategoria.getId()
                );

        if (categoriaEncontrada != null) {
            System.out.println("Categoría encontrada: "
                            + categoriaEncontrada.obtenerResumen()
            );
        }

        // UPDATE: modificar la categoría.
        nuevaCategoria.setNombre("Programación Java");

        boolean actualizada = categoriaDAO.actualizar(nuevaCategoria);

        System.out.println("Categoría actualizada: " + actualizada);

        // READ: listar todas las categorías.
        List<Categoria> categorias = categoriaDAO.listar();

        System.out.println("\nLISTADO DE CATEGORÍAS");

        for (Categoria categoria : categorias) {
            System.out.println(categoria.obtenerResumen());
        }

        // DELETE: eliminar la categoría de prueba.
        boolean eliminada = categoriaDAO.eliminar(nuevaCategoria.getId());

        System.out.println("\nCategoría eliminada: " + eliminada);
    }
}