package app;

import dao.ReporteDAO;
import model.LibroPrestadoReporte;
import model.Prestamo;
import java.util.List;

// Prueba los tres reportes solicitados.
public class PruebaReporteDAO {
    public static void main(String[] args) {
        ReporteDAO reporteDAO = new ReporteDAO();

        probarLibrosMasPrestados(reporteDAO);
        probarHistorialEstudiante(reporteDAO);
        probarPrestamosActivos(reporteDAO);
    }

    // Prueba reporte de libros más prestados.
    private static void probarLibrosMasPrestados(
            ReporteDAO reporteDAO
    ) {
        System.out.println("\nLIBROS MÁS PRESTADOS");

        List<LibroPrestadoReporte> libros =
                reporteDAO.listarLibrosMasPrestados();

        for (LibroPrestadoReporte libro : libros) {
            System.out.println(libro);
        }
    }

    // Prueba historial del estudiante con ID 1.
    private static void probarHistorialEstudiante(
            ReporteDAO reporteDAO
    ) {
        System.out.println("\nHISTORIAL DEL ESTUDIANTE 1");

        List<Prestamo> historial =
                reporteDAO.listarHistorialEstudiante(1);

        for (Prestamo prestamo : historial) {
            System.out.println(prestamo.obtenerResumen());
        }
    }

    // Prueba listado de préstamos pendientes.
    private static void probarPrestamosActivos(
            ReporteDAO reporteDAO
    ) {
        System.out.println("\nPRÉSTAMOS ACTUALMENTE ACTIVOS");

        List<Prestamo> prestamos =
                reporteDAO.listarPrestamosActivos();

        for (Prestamo prestamo : prestamos) {
            System.out.println(prestamo.obtenerResumen());
        }
    }
}