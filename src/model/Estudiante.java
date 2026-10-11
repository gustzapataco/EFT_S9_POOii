package model;

// Representa un registro de la tabla estudiantes.
public class Estudiante extends Persona {

    private String curso;

    //Constructor para registrar un estudiante nuevo.
    public Estudiante(
            String nombre,
            String rut,
            String curso,
            String correo
    ) {
        super(nombre, rut, correo);
        this.curso = curso;
    }

    // Constructor para recuperar un estudiante desde MySQL.
    public Estudiante(
            int id,
            String nombre,
            String rut,
            String curso,
            String correo
    ) {
        super(id, nombre, rut, correo);
        this.curso = curso;
    }

    public String getCurso() {
        return curso;
    }
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     * Esta es una implementación diferente del mismo metodo
     * utilizado por Usuario.
     */
    @Override
    public String obtenerResumen() {
        return "Estudiante: " + getNombre()
                + " | RUT: " + getRut()
                + " | Curso: " + curso;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}