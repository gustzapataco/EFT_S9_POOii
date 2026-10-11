package model;

// Representa un registro de la tabla usuarios.
public class Usuario extends Persona {
    private String contrasena;
    private Rol rol;

    // Constructor para registrar un usuario nuevo.
    public Usuario(
            String nombre,
            String rut,
            String correo,
            String contrasena,
            Rol rol
    ) {
        super(nombre, rut, correo);
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Constructor para recuperar un usuario desde MySQL.
    public Usuario(
            int id,
            String nombre,
            String rut,
            String correo,
            String contrasena,
            Rol rol
    ) {
        super(id, nombre, rut, correo);
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Implementación propia del metodo definido en la interfaz Mostrable.
    @Override
    public String obtenerResumen() {
        return "Usuario: " + getNombre()
                + " | RUT: " + getRut()
                + " | Rol: " + rol;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}