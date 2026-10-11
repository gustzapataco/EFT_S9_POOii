package model;

// Registro de la tabla libros.
public class Libro implements Mostrable {

    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private String editorial;
    private int stock;
    private Categoria categoria;

    // Constructor para registrar un libro nuevo.
    public Libro(
            String titulo,
            String autor,
            String isbn,
            String editorial,
            int stock,
            Categoria categoria
    ) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.stock = stock;
        this.categoria = categoria;
    }

    // Constructor para recuperar un libro desde MySQL.
    public Libro(
            int id,
            String titulo,
            String autor,
            String isbn,
            String editorial,
            int stock,
            Categoria categoria
    ) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.stock = stock;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getEditorial() {
        return editorial;
    }
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String obtenerResumen() {
        return "Libro: " + titulo
                + " | Autor: " + autor
                + " | ISBN: " + isbn
                + " | Stock: " + stock
                + " | Categoría: " + categoria.getNombre();
    }

    @Override
    public String toString() {
        return titulo;
    }
}