package controller;

import dao.ReporteDAO;
import model.LibroPrestadoReporte;
import model.Prestamo;

import java.util.List;


// Controla los reportes solicitados por el sistema.
public class ReporteController {
    private final ReporteDAO reporteDAO;
    public ReporteController() {
        reporteDAO = new ReporteDAO();
    }

    public List<LibroPrestadoReporte>
    listarLibrosMasPrestados() {
        return reporteDAO
                .listarLibrosMasPrestados();
    }

    public List<Prestamo> listarHistorialEstudiante(
            int idEstudiante
    ) {
        if (idEstudiante <= 0) {
            return List.of();
        }

        return reporteDAO
                .listarHistorialEstudiante(
                        idEstudiante
                );
    }

    public List<Prestamo>
    listarPrestamosActivos() {

        return reporteDAO
                .listarPrestamosActivos();
    }
}