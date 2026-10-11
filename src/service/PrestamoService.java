package service;

import dao.OperacionPrestamoDAO;
import model.Estudiante;
import model.Libro;
import model.Prestamo;

import java.time.LocalDate;

// Contiene la lógica de negocio de préstamos y devoluciones.
public class PrestamoService {

    private final OperacionPrestamoDAO operacionDAO;
    public PrestamoService() {
        operacionDAO = new OperacionPrestamoDAO();
    }

    /**
     * synchronized permite que solamente un hilo a la vez
     * ejecute el registro de prestamo sobre este servicio.
     */
    public synchronized Prestamo registrarPrestamo(
            Estudiante estudiante,
            Libro libro
    ) {
        if (estudiante == null || libro == null) {
            System.out.println("El estudiante y el libro son obligatorios.");
            return null;
        }

        if (libro.getStock() <= 0) {
            System.out.println("No existe stock disponible.");
            return null;
        }

        LocalDate fechaPrestamo =
                LocalDate.now();

        /*
         * La fecha de vencimiento se calcula
         * automáticamente siete días después.
         */
        LocalDate fechaDevolucion =
                fechaPrestamo.plusDays(7);

        Prestamo prestamo = new Prestamo(
                estudiante,
                libro,
                fechaPrestamo,
                fechaDevolucion,
                false
        );

        boolean registrado = operacionDAO.registrarPrestamo(prestamo);

        if (registrado) {

            System.out.println("Préstamo registrado correctamente.");
            return prestamo;
        }

        return null;
    }

    // Registra la devolución y aumenta el stock.
    public synchronized boolean registrarDevolucion(Prestamo prestamo) {
        if (prestamo == null) {
            System.out.println("El préstamo es obligatorio.");
            return false;
        }

        return operacionDAO.registrarDevolucion(
                prestamo
        );
    }
}