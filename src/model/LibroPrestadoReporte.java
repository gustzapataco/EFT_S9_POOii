package model;

// Representa una fila del reporte de libros más prestados.
public class LibroPrestadoReporte {
    private String titulo;
    private String autor;
    private int cantidadPrestamos;

    public LibroPrestadoReporte(String titulo, String autor, int cantidadPrestamos) {
        this.titulo = titulo;
        this.autor = autor;
        this.cantidadPrestamos = cantidadPrestamos;
    }

    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}
    public String getAutor() {return autor;}
    public void setAutor(String autor) {this.autor = autor;}
    public int getCantidadPrestamos() {return cantidadPrestamos;}
    public void setCantidadPrestamos(int cantidadPrestamos) {
        this.cantidadPrestamos = cantidadPrestamos;}

    @Override
    public String toString() {
        return "Libro: " + titulo
                + " | Autor: " + autor
                + " | Cantidad de préstamos: "
                + cantidadPrestamos;
    }
}