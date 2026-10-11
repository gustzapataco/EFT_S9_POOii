package controller;

import dao.PrestamoDAO;
import model.Estudiante;
import model.Libro;
import model.Prestamo;
import service.PrestamoService;
import service.ProcesoPrestamo;
import javax.swing.SwingUtilities;
import java.util.List;
import java.util.function.Consumer;

// Controla préstamos y devoluciones.
public class PrestamoController {
    private final PrestamoDAO prestamoDAO;
    private final PrestamoService prestamoService;

    public PrestamoController() {
        prestamoDAO = new PrestamoDAO();
        prestamoService = new PrestamoService();
    }

    public Prestamo buscarPorId(int id) {
        return prestamoDAO.buscarPorId(id);
    }
    public List<Prestamo> listar() {
        return prestamoDAO.listar();
    }

    // Registra el préstamo en segundo plano.
    public void registrarPrestamoSegundoPlano(
            Estudiante estudiante,
            Libro libro,
            Consumer<Prestamo> callback
    ) {
        if (
                estudiante == null
                        || libro == null
        ) {
            callback.accept(null);
            return;
        }

        ProcesoPrestamo proceso = new ProcesoPrestamo(
                        prestamoService,
                        estudiante,
                        libro
                );

        Thread hilo = new Thread(
                () -> {
                    /*
                     * La operación del préstamo se ejecuta
                     * fuera del hilo de la interfaz.
                     */
                    proceso.run();

                    Prestamo prestamo =
                            proceso.getPrestamoRegistrado();

                    /*
                     * La actualización visual debe volver
                     * al hilo de Swing.
                     */
                    SwingUtilities.invokeLater(
                            () -> callback.accept(prestamo)
                    );
                },
                "Hilo-Prestamo"
        );

        hilo.start();
    }

    // Registra la devolución de un préstamo.
    public boolean registrarDevolucion(
            Prestamo prestamo
    ) {
        return prestamoService.registrarDevolucion(
                prestamo
        );
    }
}