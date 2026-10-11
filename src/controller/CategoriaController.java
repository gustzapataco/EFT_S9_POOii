package controller;

import dao.CategoriaDAO;
import model.Categoria;
import java.util.List;

// Controla las operaciones de categorías.
public class CategoriaController {
    private final CategoriaDAO categoriaDAO;
    public CategoriaController() {
        categoriaDAO = new CategoriaDAO();
    }

    public boolean guardar(Categoria categoria) {
        if (
                categoria == null
                        || categoria.getNombre() == null
                        || categoria.getNombre().isBlank()
        ) {
            return false;
        }

        return categoriaDAO.guardar(categoria);
    }

    public Categoria buscarPorId(int id) {
        return categoriaDAO.buscarPorId(id);
    }
    public List<Categoria> listar() {
        return categoriaDAO.listar();
    }
    public boolean actualizar(Categoria categoria) {
        if (
                categoria == null
                        || categoria.getId() <= 0
                        || categoria.getNombre() == null
                        || categoria.getNombre().isBlank()
        ) {
            return false;
        }

        return categoriaDAO.actualizar(categoria);
    }

    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }

        return categoriaDAO.eliminar(id);
    }
}