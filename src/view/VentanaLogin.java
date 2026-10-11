package view;

import controller.AutenticacionController;
import model.Usuario;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

// Ventana utilizada para autenticar usuarios.
public class VentanaLogin extends JFrame {

    private final JTextField campoCorreo;
    private final JPasswordField campoContrasena;
    private final JButton botonIngresar;

    private final AutenticacionController
            autenticacionController;

    public VentanaLogin() {

        autenticacionController =
                new AutenticacionController();

        campoCorreo = new JTextField(22);
        campoContrasena = new JPasswordField(22);
        botonIngresar = new JButton("Ingresar");

        configurarVentana();
        crearContenido();
        configurarEventos();
    }

    // Configura las características generales del JFrame.
    private void configurarVentana() {

        setTitle("Biblioteca Escolar - Inicio de sesión");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(480, 400);
        setMinimumSize(new Dimension(480, 400));
        setLocationRelativeTo(null);
        setResizable(false);
    }

    // Crea y organiza los componentes visuales.
    private void crearContenido() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout());

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        50,
                        35,
                        50
                )
        );

        JPanel panelTitulo =
                crearPanelTitulo();

        JPanel panelFormulario =
                crearPanelFormulario();

        panelPrincipal.add(
                panelTitulo,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        setContentPane(panelPrincipal);

        // Permite presionar Enter para ingresar.
        getRootPane().setDefaultButton(
                botonIngresar
        );
    }

    // Crea el título de la ventana.
    private JPanel crearPanelTitulo() {

        JPanel panel =
                new JPanel(new GridBagLayout());

        JLabel etiquetaIcono =
                new JLabel("BIBLIOTECA");

        etiquetaIcono.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        14
                )
        );

        etiquetaIcono.setForeground(
                new Color(51, 97, 161)
        );

        JLabel etiquetaTitulo =
                new JLabel("Biblioteca Escolar");

        etiquetaTitulo.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        26
                )
        // Tamaño de letra - 14: pequeño, 16: mediano, 18: grande
        );

        JLabel etiquetaDescripcion =
                new JLabel(
                        "Ingrese con su cuenta registrada"
                );
         etiquetaDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 12));

        etiquetaDescripcion.setForeground(
                Color.DARK_GRAY
        );

        GridBagConstraints restricciones =
                new GridBagConstraints();

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.insets =
                new Insets(5, 5, 5, 5);

        panel.add(
                etiquetaIcono,
                restricciones
        );

        restricciones.gridy = 1;

        panel.add(
                etiquetaTitulo,
                restricciones
        );

        restricciones.gridy = 2;
        restricciones.insets =
                new Insets(2, 5, 20, 5);

        panel.add(
                etiquetaDescripcion,
                restricciones
        );

        return panel;
    }

    // Crea los campos de correo y contraseña.
    private JPanel crearPanelFormulario() {

        JPanel panel =
                new JPanel(new GridBagLayout());

        GridBagConstraints restricciones =
                new GridBagConstraints();

        restricciones.fill =
                GridBagConstraints.HORIZONTAL;

        restricciones.weightx = 1;
        restricciones.gridx = 0;
        restricciones.insets =
                new Insets(6, 5, 6, 5);

        JLabel etiquetaCorreo =
                new JLabel("Correo Electrónico :");

        restricciones.gridy = 0;
        panel.add(
                etiquetaCorreo,
                restricciones
        );

        restricciones.gridy = 1;
        panel.add(
                campoCorreo,
                restricciones
        );

        JLabel etiquetaContrasena =
                new JLabel("Contraseña :");

        restricciones.gridy = 2;
        restricciones.insets =
                new Insets(16, 5, 6, 5);

        panel.add(
                etiquetaContrasena,
                restricciones
        );

        restricciones.gridy = 3;
        restricciones.insets =
                new Insets(6, 5, 6, 5);

        panel.add(
                campoContrasena,
                restricciones
        );

        botonIngresar.setPreferredSize(
                new Dimension(200, 38)
        );

        restricciones.gridy = 4;
        restricciones.insets =
                new Insets(22, 5, 5, 5);

        panel.add(
                botonIngresar,
                restricciones
        );

        return panel;
    }

    // Relaciona el botón con su acción.
    private void configurarEventos() {

        botonIngresar.addActionListener(
                evento -> iniciarSesion()
        );
    }

    // Solicita la autenticación al controlador.
    private void iniciarSesion() {

        String correo =
                campoCorreo.getText().trim();

        String contrasena =
                new String(
                        campoContrasena.getPassword()
                );

        if (
                correo.isBlank()
                        || contrasena.isBlank()
        ) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el correo electrónico y la contraseña.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        botonIngresar.setEnabled(false);
        botonIngresar.setText("Ingresando...");

        Usuario usuario =
                autenticacionController.autenticar(
                        correo,
                        contrasena
                );

        if (usuario != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido/a, "
                            + usuario.getNombre()
                            + ".",
                    "Autenticación correcta",
                    JOptionPane.INFORMATION_MESSAGE
            );

            VentanaPrincipal ventanaPrincipal =
                    new VentanaPrincipal(usuario);

            ventanaPrincipal.setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos.",
                    "No fue posible ingresar",
                    JOptionPane.ERROR_MESSAGE
            );

            campoContrasena.setText("");
            campoContrasena.requestFocus();

            botonIngresar.setEnabled(true);
        }
    }
}