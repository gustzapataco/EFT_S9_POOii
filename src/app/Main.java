package app;

import view.VentanaLogin;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// Clase inicia la aplicación.
public class Main {
    public static void main(String[] args) {

        /*
         * SwingUtilities.invokeLater inicia la interfaz
         * en el hilo correcto de Swing.
         */
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager
                                .getSystemLookAndFeelClassName()
                );

            } catch (Exception e) {
                System.out.println("No se pudo aplicar el estilo del sistema.");
            }

            VentanaLogin ventanaLogin =
                    new VentanaLogin();

            ventanaLogin.setVisible(true);
        });
    }
}