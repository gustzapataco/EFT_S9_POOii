package app;

import dao.EstudianteDAO;
import dao.LibroDAO;
import dao.PrestamoDAO;
import model.Estudiante;
import model.Libro;
import model.Prestamo;

import java.time.LocalDate;
import java.util.List;

// Prueba las operaciones CRUD de PrestamoDAO.
public class PruebaPrestamoDAO {

    public static void main(String[] args) {
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        LibroDAO libroDAO = new LibroDAO();
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        /*
         * Recuperamos registros existentes para crear
         * la relación del préstamo.
         */
        Estudiante estudiante = estudianteDAO.buscarPorId(1);
        Libro libro = libroDAO.buscarPorId(1);

        if (estudiante == null || libro == null) {
            System.out.println("No fue posible encontrar el estudiante o el libro.");
            return;
        }

        // Fecha actual.
        LocalDate fechaPrestamo =
                LocalDate.now();

        // La fecha de devolución será siete días después.
        LocalDate fechaDevolucion =
                fechaPrestamo.plusDays(7);

        // CREATE
        Prestamo nuevoPrestamo = new Prestamo(
                estudiante,
                libro,
                fechaPrestamo,
                fechaDevolucion,
                false
        );

        boolean guardado = prestamoDAO.guardar(nuevoPrestamo);

        if (guardado) {
            System.out.println("Préstamo guardado con ID: " + nuevoPrestamo.getId());

        } else {
            System.out.println("No fue posible guardar el préstamo.");
            return;
        }

        // READ: buscar por ID
        Prestamo prestamoEncontrado = prestamoDAO.buscarPorId(nuevoPrestamo.getId());
        if (prestamoEncontrado != null) {
            System.out.println("Préstamo encontrado: " + prestamoEncontrado.obtenerResumen());
        }

        // UPDATE: marcar temporalmente como devuelto.
        nuevoPrestamo.setDevuelto(true);
        boolean actualizado = prestamoDAO.actualizar(nuevoPrestamo);
        System.out.println("Préstamo actualizado: " + actualizado);

        // READ: listar
        List<Prestamo> prestamos = prestamoDAO.listar();
        System.out.println("\nLISTADO DE PRÉSTAMOS");
        for (Prestamo prestamo : prestamos) {
            System.out.println(prestamo.obtenerResumen());
        }

        // DELETE: eliminar el préstamo temporal.
        boolean eliminado = prestamoDAO.eliminar(nuevoPrestamo.getId());
        System.out.println("\nPréstamo eliminado: " + eliminado);
    }
}