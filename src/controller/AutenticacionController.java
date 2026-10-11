package controller;

import dao.UsuarioDAO;
import model.Usuario;

// Controla la autenticación de usuarios.
public class AutenticacionController {
    private final UsuarioDAO usuarioDAO;
    public AutenticacionController() {
        usuarioDAO = new UsuarioDAO();
    }

    // Valida los datos antes de consultar la bd.
    public Usuario autenticar(String correo, String contrasena) {
        if (
                correo == null
                        || correo.isBlank()
                        || contrasena == null
                        || contrasena.isBlank()
        ) {
            return null;
        }

        return usuarioDAO.autenticar(
                correo.trim(),
                contrasena
        );
    }
}