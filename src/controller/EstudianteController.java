package controller;

import dao.EstudianteDAO;
import model.Estudiante;
import java.util.List;

// Controla las operaciones de estudiantes.
public class EstudianteController {
    private final EstudianteDAO estudianteDAO;
    public EstudianteController() {
        estudianteDAO = new EstudianteDAO();
    }

    public boolean guardar(Estudiante estudiante) {
        if (!esValido(estudiante)) {
            return false;
        }

        return estudianteDAO.guardar(estudiante);
    }

    public Estudiante buscarPorId(int id) {
        return estudianteDAO.buscarPorId(id);
    }
    public List<Estudiante> listar() {
        return estudianteDAO.listar();
    }
    public boolean actualizar(Estudiante estudiante) {
        if (
                !esValido(estudiante)
                        || estudiante.getId() <= 0
        ) {
            return false;
        }

        return estudianteDAO.actualizar(estudiante);
    }

    public boolean eliminar(int id) {

        if (id <= 0) {
            return false;
        }

        return estudianteDAO.eliminar(id);
    }

     // Valida los campos obligatorios del estudiante.
     private boolean esValido(Estudiante estudiante) {
        return estudiante != null
                && estudiante.getNombre() != null
                && !estudiante.getNombre().isBlank()
                && estudiante.getRut() != null
                && !estudiante.getRut().isBlank()
                && estudiante.getCurso() != null
                && !estudiante.getCurso().isBlank()
                && estudiante.getCorreo() != null
                && !estudiante.getCorreo().isBlank();
    }
}