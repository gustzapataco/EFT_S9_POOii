package app;

import dao.UsuarioDAO;
import model.Rol;
import model.Usuario;

import java.util.List;

// Prueba el CRUD y la autenticación de UsuarioDAO.
public class PruebaUsuarioDAO {
    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // CREATE
        Usuario nuevoUsuario = new Usuario(
                "Usuario Prueba",
                "10101010-1",
                "usuario.prueba@correo.cl",
                "clavePrueba",
                Rol.ESTUDIANTE
        );

        boolean guardado = usuarioDAO.guardar(nuevoUsuario);

        if (guardado) {
            System.out.println("Usuario guardado con ID: " + nuevoUsuario.getId());

        } else {

            System.out.println("No fue posible guardar el usuario.");

            return;
        }

        // READ: buscar por ID
        Usuario usuarioEncontrado = usuarioDAO.buscarPorId(nuevoUsuario.getId());
        if (usuarioEncontrado != null) {
            System.out.println("Usuario encontrado: " + usuarioEncontrado.obtenerResumen());
        }

        // UPDATE
        nuevoUsuario.setNombre("Usuario Modificado");
        nuevoUsuario.setRol(Rol.BIBLIOTECARIO);
        boolean actualizado = usuarioDAO.actualizar(nuevoUsuario);
        System.out.println("Usuario actualizado: " + actualizado);

        // READ: listar
        List<Usuario> usuarios = usuarioDAO.listar();
        System.out.println("\nLISTADO DE USUARIOS");
        for (Usuario usuario : usuarios) {
            // obtenerResumen no muestra la contraseña.
            System.out.println(usuario.obtenerResumen());
        }

        // AUTENTICACIÓN
        Usuario usuarioAutenticado = usuarioDAO.autenticar(
                        "usuario.prueba@correo.cl",
                        "clavePrueba"
                );

        if (usuarioAutenticado != null) {
            System.out.println("\nAutenticación correcta.");
            System.out.println("Usuario: " + usuarioAutenticado.getNombre());
            System.out.println("Rol: " + usuarioAutenticado.getRol());

        } else {
            System.out.println("\nCorreo o contraseña incorrectos.");
        }

        // DELETE
        boolean eliminado = usuarioDAO.eliminar(nuevoUsuario.getId());
        System.out.println("\nUsuario eliminado: " + eliminado);
    }
}