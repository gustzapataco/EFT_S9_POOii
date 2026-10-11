package view;

import controller.EstudianteController;
import controller.LibroController;
import controller.PrestamoController;
import controller.ReporteController;
import model.Estudiante;
import model.Libro;
import model.Prestamo;
import model.Rol;
import model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Ventana para registrar préstamos y devoluciones.
 */
public class VentanaPrestamos extends JFrame {

    private final Usuario usuario;

    private final EstudianteController
            estudianteController;

    private final LibroController
            libroController;

    private final PrestamoController
            prestamoController;

    private final ReporteController
            reporteController;

    private final JComboBox<Estudiante>
            comboEstudiante;

    private final JComboBox<Libro>
            comboLibro;

    private final JButton botonPrestar;
    private final JButton botonDevolver;
    private final JButton botonActualizar;
    private final JButton botonCerrar;

    private final JTable tablaPrestamos;
    private final DefaultTableModel modeloTabla;

    private int idPrestamoSeleccionado;

    public VentanaPrestamos(Usuario usuario) {

        this.usuario = usuario;

        estudianteController =
                new EstudianteController();

        libroController =
                new LibroController();

        prestamoController =
                new PrestamoController();

        reporteController =
                new ReporteController();

        comboEstudiante = new JComboBox<>();
        comboLibro = new JComboBox<>();

        botonPrestar =
                new JButton("Registrar préstamo");

        botonDevolver =
                new JButton("Registrar devolución");

        botonActualizar =
                new JButton("Actualizar listado");

        botonCerrar =
                new JButton("Cerrar");

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Estudiante",
                        "Libro",
                        "Fecha préstamo",
                        "Fecha devolución",
                        "Estado"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }
        };

        tablaPrestamos = new JTable(modeloTabla);

        idPrestamoSeleccionado = 0;

        configurarVentana();
        crearContenido();
        configurarTabla();
        configurarEventos();

        cargarEstudiantes();
        cargarLibros();
        cargarPrestamosActivos();
    }

    private void configurarVentana() {

        setTitle(
                "Biblioteca Escolar - Préstamos y devoluciones"
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setSize(1050, 650);

        setMinimumSize(
                new Dimension(900, 580)
        );

        setLocationRelativeTo(null);
    }

    private void crearContenido() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(10, 10));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JLabel etiquetaTitulo =
                new JLabel(
                        "Préstamos y devoluciones"
                );

        etiquetaTitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        JPanel panelSeleccion =
                crearPanelSeleccion();

        JPanel panelBotones =
                crearPanelBotones();

        JPanel panelSuperior =
                new JPanel(new BorderLayout(10, 10));

        panelSuperior.add(
                etiquetaTitulo,
                BorderLayout.NORTH
        );

        panelSuperior.add(
                panelSeleccion,
                BorderLayout.CENTER
        );

        panelSuperior.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        JScrollPane desplazamiento =
                new JScrollPane(tablaPrestamos);

        desplazamiento.setBorder(
                BorderFactory.createTitledBorder(
                        "Préstamos actualmente activos"
                )
        );

        panelPrincipal.add(
                panelSuperior,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                desplazamiento,
                BorderLayout.CENTER
        );

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelSeleccion() {

        JPanel panel =
                new JPanel(new GridBagLayout());

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Nuevo préstamo"
                )
        );

        GridBagConstraints restricciones =
                new GridBagConstraints();

        restricciones.fill =
                GridBagConstraints.HORIZONTAL;

        restricciones.insets =
                new Insets(8, 8, 8, 8);

        restricciones.gridy = 0;
        restricciones.gridx = 0;
        restricciones.weightx = 0;

        panel.add(
                new JLabel("Estudiante"),
                restricciones
        );

        restricciones.gridx = 1;
        restricciones.weightx = 1;

        panel.add(
                comboEstudiante,
                restricciones
        );

        restricciones.gridx = 2;
        restricciones.weightx = 0;

        panel.add(
                new JLabel("Libro"),
                restricciones
        );

        restricciones.gridx = 3;
        restricciones.weightx = 1;

        panel.add(
                comboLibro,
                restricciones
        );

        return panel;
    }

    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(new GridLayout(
                        1,
                        4,
                        10,
                        10
                ));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        0,
                        5,
                        0
                )
        );

        panel.add(botonPrestar);
        panel.add(botonDevolver);
        panel.add(botonActualizar);
        panel.add(botonCerrar);

        return panel;
    }

    private void configurarTabla() {

        tablaPrestamos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaPrestamos.setRowHeight(25);

        tablaPrestamos
                .getTableHeader()
                .setReorderingAllowed(false);
    }

    private void configurarEventos() {

        botonPrestar.addActionListener(
                evento -> registrarPrestamo()
        );

        botonDevolver.addActionListener(
                evento -> registrarDevolucion()
        );

        botonActualizar.addActionListener(
                evento -> actualizarDatos()
        );

        botonCerrar.addActionListener(
                evento -> dispose()
        );

        tablaPrestamos
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento.getValueIsAdjusting()) {
                                guardarPrestamoSeleccionado();
                            }
                        }
                );
    }

    /**
     * El bibliotecario ve todos los estudiantes.
     *
     * El estudiante solamente ve el registro que
     * tenga el mismo RUT que su usuario.
     */
    private void cargarEstudiantes() {

        comboEstudiante.removeAllItems();

        List<Estudiante> estudiantes =
                estudianteController.listar();

        for (Estudiante estudiante : estudiantes) {

            if (
                    usuario.getRol() == Rol.BIBLIOTECARIO
                            || estudiante.getRut().equals(
                            usuario.getRut()
                    )
            ) {
                comboEstudiante.addItem(estudiante);
            }
        }
    }

    /**
     * Carga solamente libros que tengan stock disponible.
     */
    private void cargarLibros() {

        comboLibro.removeAllItems();

        List<Libro> libros =
                libroController.listar();

        for (Libro libro : libros) {

            if (libro.getStock() > 0) {
                comboLibro.addItem(libro);
            }
        }
    }

    private void cargarPrestamosActivos() {

        idPrestamoSeleccionado = 0;
        modeloTabla.setRowCount(0);

        List<Prestamo> prestamos =
                reporteController
                        .listarPrestamosActivos();

        for (Prestamo prestamo : prestamos) {

            /*
             * El estudiante solo visualiza sus préstamos.
             */
            if (
                    usuario.getRol() == Rol.ESTUDIANTE
                            && !prestamo
                            .getEstudiante()
                            .getRut()
                            .equals(usuario.getRut())
            ) {
                continue;
            }

            String estado;

            if (prestamo.estaAtrasado()) {
                estado = "Atrasado";
            } else {
                estado = "En préstamo";
            }

            modeloTabla.addRow(
                    new Object[]{
                            prestamo.getId(),
                            prestamo
                                    .getEstudiante()
                                    .getNombre(),
                            prestamo
                                    .getLibro()
                                    .getTitulo(),
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion(),
                            estado
                    }
            );
        }
    }

    private void registrarPrestamo() {

        Estudiante estudiante =
                (Estudiante) comboEstudiante
                        .getSelectedItem();

        Libro libro =
                (Libro) comboLibro
                        .getSelectedItem();

        if (estudiante == null || libro == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante "
                            + "y un libro disponible.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        /*
         * Se desactiva el botón mientras trabaja el hilo.
         */
        botonPrestar.setEnabled(false);
        botonPrestar.setText("Registrando...");

        prestamoController
                .registrarPrestamoSegundoPlano(
                        estudiante,
                        libro,
                        prestamo -> {

                            botonPrestar.setEnabled(true);
                            botonPrestar.setText(
                                    "Registrar préstamo"
                            );

                            if (prestamo != null) {

                                JOptionPane.showMessageDialog(
                                        this,
                                        "Préstamo registrado correctamente.\n"
                                                + "Fecha de devolución: "
                                                + prestamo
                                                .getFechaDevolucion(),
                                        "Préstamo correcto",
                                        JOptionPane.INFORMATION_MESSAGE
                                );

                                actualizarDatos();

                            } else {

                                JOptionPane.showMessageDialog(
                                        this,
                                        "No fue posible registrar el préstamo.\n"
                                                + "Verifique el stock disponible.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                );
    }

    private void guardarPrestamoSeleccionado() {

        int fila =
                tablaPrestamos.getSelectedRow();

        if (fila == -1) {
            idPrestamoSeleccionado = 0;
            return;
        }

        idPrestamoSeleccionado =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );
    }

    private void registrarDevolucion() {

        if (idPrestamoSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un préstamo de la tabla.",
                    "Préstamo no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Prestamo prestamo =
                prestamoController.buscarPorId(
                        idPrestamoSeleccionado
                );

        if (prestamo == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el préstamo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea registrar la devolución de "
                                + prestamo
                                .getLibro()
                                .getTitulo()
                                + "?",
                        "Confirmar devolución",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean devuelto =
                prestamoController
                        .registrarDevolucion(prestamo);

        if (devuelto) {

            JOptionPane.showMessageDialog(
                    this,
                    "Devolución registrada correctamente.",
                    "Devolución correcta",
                    JOptionPane.INFORMATION_MESSAGE
            );

            actualizarDatos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar la devolución.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizarDatos() {

        cargarEstudiantes();
        cargarLibros();
        cargarPrestamosActivos();
    }
}