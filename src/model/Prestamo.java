package model;

import java.time.LocalDate;

/**
 * Representa un registro de la tabla prestamos.
 */
public class Prestamo implements Mostrable {

    private int id;
    private Estudiante estudiante;
    private Libro libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto;

    /**
     * Constructor para registrar un préstamo nuevo.
     */
    public Prestamo(
            Estudiante estudiante,
            Libro libro,
            LocalDate fechaPrestamo,
            LocalDate fechaDevolucion,
            boolean devuelto
    ) {
        this.estudiante = estudiante;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    /**
     * Constructor para recuperar un préstamo desde MySQL.
     */
    public Prestamo(
            int id,
            Estudiante estudiante,
            Libro libro,
            LocalDate fechaPrestamo,
            LocalDate fechaDevolucion,
            boolean devuelto
    ) {
        this.id = id;
        this.estudiante = estudiante;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Estudiante getEstudiante() {
        return estudiante;
    }
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    public Libro getLibro() {
        return libro;
    }
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    public boolean isDevuelto() {
        return devuelto;
    }
    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    // Determina si el préstamo se encuentra atrasado.
    public boolean estaAtrasado() {
        return !devuelto
                && fechaDevolucion.isBefore(LocalDate.now());
    }

    @Override
    public String obtenerResumen() {

        String estado;

        if (devuelto) {
            estado = "Devuelto";
        } else if (estaAtrasado()) {
            estado = "Atrasado";
        } else {
            estado = "En préstamo";
        }

        return "Estudiante: " + estudiante.getNombre()
                + " | Libro: " + libro.getTitulo()
                + " | Préstamo: " + fechaPrestamo
                + " | Devolución: " + fechaDevolucion
                + " | Estado: " + estado;
    }

    @Override
    public String toString() {
        return libro.getTitulo()
                + " - "
                + estudiante.getNombre();
    }
}