package model;

// Representa un registro de la tabla categorias.
public class Categoria implements Mostrable {

    private int id;
    private String nombre;

    // Constructor para registrar una categoría nueva.
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Constructor para recuperar una categoría desde MySQL.
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String obtenerResumen() {
        return "Categoría: " + nombre;
    }

    /**
     * Esto permitirá mostrar correctamente la categoría
     * en componentes gráficos como JComboBox.
     */
    @Override
    public String toString() {
        return nombre;
    }
}