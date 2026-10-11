package service;

import model.Estudiante;
import model.Libro;
import model.Prestamo;

// Representa la tarea que se ejecutará en un hilo independiente.
public class ProcesoPrestamo implements Runnable {

    private final PrestamoService prestamoService;
    private final Estudiante estudiante;
    private final Libro libro;

    private Prestamo prestamoRegistrado;

    public ProcesoPrestamo(
            PrestamoService prestamoService,
            Estudiante estudiante,
            Libro libro
    ) {
        this.prestamoService = prestamoService;
        this.estudiante = estudiante;
        this.libro = libro;
    }

    // Este metodo se ejecuta cuando comienza el hilo.
    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName()
                        + " inició el préstamo.");

        prestamoRegistrado =
                prestamoService.registrarPrestamo(
                        estudiante,
                        libro
                );

        if (prestamoRegistrado != null) {
            System.out.println(Thread.currentThread().getName()
                            + " terminó correctamente.");

        } else {
            System.out.println(Thread.currentThread().getName()
                            + " no pudo registrar el préstamo.");
        }
    }

    public Prestamo getPrestamoRegistrado() {
        return prestamoRegistrado;
    }
}