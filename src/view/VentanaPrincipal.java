package view;

import model.Rol;
import model.Usuario;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

// Ventana principal de navegación.
public class VentanaPrincipal extends JFrame {
    private final Usuario usuario;
    private final JButton botonLibros;
    private final JButton botonEstudiantes;
    private final JButton botonPrestamos;
    private final JButton botonReportes;
    private final JButton botonCerrarSesion;

    public VentanaPrincipal(Usuario usuario) {

        this.usuario = usuario;
        // Botones de interaccion
        botonLibros = new JButton("Gestión de libros");
        botonEstudiantes = new JButton("Gestión de estudiantes");
        botonPrestamos = new JButton("Préstamos y devoluciones");
        botonReportes = new JButton("Reportes");
        botonCerrarSesion = new JButton("Cerrar sesión");

        configurarVentana();
        crearContenido();
        configurarPermisos();
        configurarEventos();
    }

    // JFrame principal
    private void configurarVentana() {

        setTitle("Biblioteca Escolar - Menú principal");
        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(850, 550);
        setMinimumSize(
                new Dimension(750, 500)
        );

        setLocationRelativeTo(null);
    }

    // Distribución general.
    private void crearContenido() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout());

        panelPrincipal.setBackground(
                new Color(245, 247, 250)
        );

        JPanel panelSuperior =
                crearPanelSuperior();

        JPanel panelModulos =
                crearPanelModulos();

        JPanel panelInferior =
                crearPanelInferior();

        panelPrincipal.add(
                panelSuperior,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                panelModulos,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelInferior,
                BorderLayout.SOUTH
        );

        setContentPane(panelPrincipal);
    }

    // Muestra el nombre y rol del usuario autenticado.
    private JPanel crearPanelSuperior() {

        JPanel panel =
                new JPanel(new GridLayout(2, 1));

        panel.setBackground(
                new Color(35, 51, 77)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );

        JLabel etiquetaBienvenida =
                new JLabel(
                        "Bienvenido, "
                                + usuario.getNombre()
                );

        etiquetaBienvenida.setForeground(
                Color.WHITE
        );

        etiquetaBienvenida.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        23
                )
        );

        JLabel etiquetaRol =
                new JLabel(
                        "Rol: "
                                + obtenerNombreRol()
                );

        etiquetaRol.setForeground(
                new Color(220, 225, 235)
        );

        etiquetaRol.setFont(
                new Font(
                        "Tahoma",
                        Font.PLAIN,
                        15
                )
        );

        panel.add(etiquetaBienvenida);
        panel.add(etiquetaRol);

        return panel;
    }

    // Botones de navegación.
    private JPanel crearPanelModulos() {

        JPanel panel =
                new JPanel(new GridLayout(
                        2,
                        2,
                        18,
                        18
                ));

        panel.setBackground(
                new Color(245, 247, 250)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        40,
                        45,
                        40,
                        45
                )
        );

        prepararBoton(botonLibros);
        prepararBoton(botonEstudiantes);
        prepararBoton(botonPrestamos);
        prepararBoton(botonReportes);

        panel.add(botonLibros);
        panel.add(botonEstudiantes);
        panel.add(botonPrestamos);
        panel.add(botonReportes);

        return panel;
    }

    // Aplica el mismo formato a los botones principales.
    private void prepararBoton(JButton boton) {

        boton.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        16
                )
        );

        boton.setFocusPainted(false);
        boton.setBackground(Color.WHITE);
    }

    // Botón de cierre de sesión.
    private JPanel crearPanelInferior() {

        JPanel panel = new JPanel();

        panel.setBackground(
                new Color(245, 247, 250)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        20,
                        20,
                        20
                )
        );

        botonCerrarSesion.setPreferredSize(
                new Dimension(180, 35)
        );
        panel.add(botonCerrarSesion);
        return panel;
    }

    // Aplica las restricciones correspondientes al rol.
    private void configurarPermisos() {

        if (usuario.getRol() == Rol.ESTUDIANTE) {

            /*
             * Según la guía, el estudiante no tiene
             * acceso a la gestión de estudiantes.
             */
            botonEstudiantes.setEnabled(false);

            /*
             * La ventana de libros se utilizará
             * solamente como consulta para este rol.
             */
            botonLibros.setText(
                    "Consultar libros"
            );
        }
    }

    // Conecta los botones con sus acciones.
    private void configurarEventos() {

        botonLibros.addActionListener(
                evento -> abrirVentanaLibros()
        );

        botonEstudiantes.addActionListener(
                evento -> abrirVentanaEstudiantes()
        );

        botonPrestamos.addActionListener(
                evento -> abrirVentanaPrestamos()
        );

        botonReportes.addActionListener(
                evento -> abrirVentanaReportes()
        );

        botonCerrarSesion.addActionListener(
                evento -> cerrarSesion()
        );
    }

    // Acción temporal para comprobar que cada botón funciona.
    private void mostrarModulo(
            String nombreModulo
    ) {
        JOptionPane.showMessageDialog(
                this,
                "Abrir módulo: " + nombreModulo,
                "Navegación",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Cierra la ventana principal y regresa al login.
    private void cerrarSesion() {

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea cerrar la sesión?",
                        "Cerrar sesión",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta == JOptionPane.YES_OPTION) {

            VentanaLogin ventanaLogin =
                    new VentanaLogin();

            ventanaLogin.setVisible(true);

            dispose();
        }
    }

    // Entrega un nombre fácil de leer para el rol.
    private String obtenerNombreRol() {
        if (
                usuario.getRol()
                        == Rol.BIBLIOTECARIO
        ) {
            return "Bibliotecario";
        }

        return "Estudiante";
    }

    // Abre la gestión o consulta de libros.
    private void abrirVentanaLibros() {

        VentanaLibros ventana =
                new VentanaLibros(usuario);

        ventana.setVisible(true);
    }

    // Abre la gestión de estudiantes.
    private void abrirVentanaEstudiantes() {

        VentanaEstudiantes ventana =
                new VentanaEstudiantes();

        ventana.setVisible(true);
    }

    // Abre préstamos y devoluciones.
    private void abrirVentanaPrestamos() {

        VentanaPrestamos ventana =
                new VentanaPrestamos(usuario);

        ventana.setVisible(true);
    }

    // Abre los reportes.
    private void abrirVentanaReportes() {

        VentanaReportes ventana =
                new VentanaReportes();

        ventana.setVisible(true);
    }
}