package app;

import dao.EstudianteDAO;
import model.Estudiante;

import java.util.List;

// Prueba las operaciones CRUD de EstudianteDAO.
public class PruebaEstudianteDAO {
    public static void main(String[] args) {
        EstudianteDAO estudianteDAO = new EstudianteDAO();

        // CREATE
        Estudiante nuevoEstudiante =
                new Estudiante(
                        "Estudiante Prueba",
                        "11111111-1",
                        "1ro Medio A",
                        "prueba@correo.cl"
                );

        boolean guardado = estudianteDAO.guardar(nuevoEstudiante);

        if (guardado) {
            System.out.println("Estudiante guardado con ID: "
                            + nuevoEstudiante.getId());

        } else {
            System.out.println("No fue posible guardar el estudiante.");
            return;
        }

        // READ: buscar por ID
        Estudiante estudianteEncontrado = estudianteDAO.buscarPorId(
                        nuevoEstudiante.getId());

        if (estudianteEncontrado != null) {

            System.out.println("Estudiante encontrado: "
                            + estudianteEncontrado.obtenerResumen());
        }

        // UPDATE
        nuevoEstudiante.setNombre("Estudiante Modificado");

        nuevoEstudiante.setCurso("2do Medio B");
        boolean actualizado = estudianteDAO.actualizar(nuevoEstudiante);

        System.out.println(
                "Estudiante actualizado: " + actualizado);

        // READ: listar
        List<Estudiante> estudiantes = estudianteDAO.listar();
        System.out.println("\nLISTADO DE ESTUDIANTES");
        for (Estudiante estudiante : estudiantes) {
            System.out.println(estudiante.obtenerResumen());
        }

        // DELETE
        boolean eliminado = estudianteDAO.eliminar(
                        nuevoEstudiante.getId());

        System.out.println("\nEstudiante eliminado: " + eliminado);
    }
}