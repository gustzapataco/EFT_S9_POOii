package dao;

import java.util.List;

/**
 * Define las cuatro operaciones principales de un CRUD.
 *
 * C: Create  -> guardar
 * R: Read    -> buscar y listar
 * U: Update  -> actualizar
 * D: Delete  -> eliminar
 */
public interface CrudDAO<T> {
    boolean guardar(T objeto);
    T buscarPorId(int id);
    List<T> listar();
    boolean actualizar(T objeto);
    boolean eliminar(int id);
}