package app;

import dao.EstudianteDAO;
import dao.LibroDAO;
import model.Estudiante;
import model.Libro;
import model.Prestamo;
import service.PrestamoService;
import service.ProcesoPrestamo;

// Prueba dos préstamos ejecutados mediante hilos.
public class PruebaConcurrencia {

    public static void main(String[] args) {

        EstudianteDAO estudianteDAO =
                new EstudianteDAO();

        LibroDAO libroDAO =
                new LibroDAO();

        Estudiante estudianteUno =
                estudianteDAO.buscarPorId(1);

        Estudiante estudianteDos =
                estudianteDAO.buscarPorId(2);

        Libro libro =
                libroDAO.buscarPorId(1);

        if (
                estudianteUno == null
                        || estudianteDos == null
                        || libro == null
        ) {
            System.out.println("No se encontraron los datos necesarios.");
            return;
        }

        System.out.println("Stock inicial: " + libro.getStock());

        /*
         * Ambos procesos utilizan la misma instancia del servicio.
         * El metodo synchronized coordina su ejecución.
         */
        PrestamoService prestamoService =
                new PrestamoService();

        ProcesoPrestamo procesoUno =
                new ProcesoPrestamo(
                        prestamoService,
                        estudianteUno,
                        libro
                );

        ProcesoPrestamo procesoDos =
                new ProcesoPrestamo(
                        prestamoService,
                        estudianteDos,
                        libro
                );

        Thread hiloUno = new Thread(
                procesoUno,
                "Hilo 1"
        );

        Thread hiloDos = new Thread(
                procesoDos,
                "Hilo 2"
        );

        // start() inicia los hilos.
        hiloUno.start();
        hiloDos.start();

        try {
            /* join() hace que main espere a que ambos
             * hilos terminen antes de continuar.
             */
            hiloUno.join();
            hiloDos.join();

        } catch (InterruptedException e) {

            System.out.println("La ejecución fue interrumpida.");
            Thread.currentThread().interrupt();
        }

        Libro libroActualizado = libroDAO.buscarPorId(libro.getId());

        System.out.println("Stock después de los préstamos: "
                        + libroActualizado.getStock());

        /*
         * Recuperamos los préstamos creados para devolverlos
         * y restaurar el stock original.
         */
        Prestamo prestamoUno = procesoUno.getPrestamoRegistrado();
        Prestamo prestamoDos = procesoDos.getPrestamoRegistrado();

        if (prestamoUno != null) {

            boolean devuelto = prestamoService.registrarDevolucion(prestamoUno);
            System.out.println("Primera devolución: " + devuelto);
        }

        if (prestamoDos != null) {
            boolean devuelto =
                    prestamoService.registrarDevolucion(prestamoDos);

            System.out.println("Segunda devolución: " + devuelto);
        }

        Libro libroFinal = libroDAO.buscarPorId(libro.getId());
        System.out.println("Stock final: " + libroFinal.getStock());
    }
}