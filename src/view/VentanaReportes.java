package view;

import controller.EstudianteController;
import controller.ReporteController;
import model.Estudiante;
import model.LibroPrestadoReporte;
import model.Prestamo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.util.List;

/**
 * Ventana que muestra los tres reportes solicitados.
 */
public class VentanaReportes extends JFrame {

    private final ReporteController reporteController;
    private final EstudianteController estudianteController;

    private final DefaultTableModel modeloMasPrestados;
    private final DefaultTableModel modeloHistorial;
    private final DefaultTableModel modeloActivos;

    private final JComboBox<Estudiante>
            comboEstudiante;

    public VentanaReportes() {

        reporteController =
                new ReporteController();

        estudianteController =
                new EstudianteController();

        comboEstudiante =
                new JComboBox<>();

        modeloMasPrestados =
                crearModelo(
                        "Libro",
                        "Autor",
                        "Cantidad de préstamos"
                );

        modeloHistorial =
                crearModelo(
                        "ID",
                        "Libro",
                        "Fecha préstamo",
                        "Fecha devolución",
                        "Estado"
                );

        modeloActivos =
                crearModelo(
                        "ID",
                        "Estudiante",
                        "Libro",
                        "Fecha préstamo",
                        "Fecha devolución",
                        "Estado"
                );

        configurarVentana();
        crearContenido();

        cargarEstudiantes();
        cargarLibrosMasPrestados();
        cargarPrestamosActivos();
    }

    private void configurarVentana() {

        setTitle(
                "Biblioteca Escolar - Reportes"
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setSize(1050, 650);

        setMinimumSize(
                new Dimension(900, 550)
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
                new JLabel("Reportes");

        etiquetaTitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        JTabbedPane pestanas =
                new JTabbedPane();

        pestanas.addTab(
                "Libros más prestados",
                crearPanelMasPrestados()
        );

        pestanas.addTab(
                "Historial por estudiante",
                crearPanelHistorial()
        );

        pestanas.addTab(
                "Préstamos activos",
                crearPanelPrestamosActivos()
        );

        JButton botonCerrar =
                new JButton("Cerrar");

        botonCerrar.addActionListener(
                evento -> dispose()
        );

        JPanel panelInferior =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT
                ));

        panelInferior.add(botonCerrar);

        panelPrincipal.add(
                etiquetaTitulo,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                pestanas,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelInferior,
                BorderLayout.SOUTH
        );

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelMasPrestados() {

        JTable tabla =
                new JTable(modeloMasPrestados);

        prepararTabla(tabla);

        JPanel panel =
                new JPanel(new BorderLayout());

        JButton botonActualizar =
                new JButton("Actualizar");

        botonActualizar.addActionListener(
                evento -> cargarLibrosMasPrestados()
        );

        JPanel panelBoton =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT
                ));

        panelBoton.add(botonActualizar);

        panel.add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        panel.add(
                panelBoton,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel crearPanelHistorial() {

        JTable tabla =
                new JTable(modeloHistorial);

        prepararTabla(tabla);

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        JPanel panelSeleccion =
                new JPanel(new FlowLayout(
                        FlowLayout.LEFT
                ));

        JButton botonConsultar =
                new JButton("Consultar historial");

        botonConsultar.addActionListener(
                evento -> cargarHistorial()
        );

        panelSeleccion.add(
                new JLabel("Estudiante")
        );

        panelSeleccion.add(comboEstudiante);
        panelSeleccion.add(botonConsultar);

        panel.add(
                panelSeleccion,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel crearPanelPrestamosActivos() {

        JTable tabla =
                new JTable(modeloActivos);

        prepararTabla(tabla);

        JPanel panel =
                new JPanel(new BorderLayout());

        JButton botonActualizar =
                new JButton("Actualizar");

        botonActualizar.addActionListener(
                evento -> cargarPrestamosActivos()
        );

        JPanel panelBoton =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT
                ));

        panelBoton.add(botonActualizar);

        panel.add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        panel.add(
                panelBoton,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private DefaultTableModel crearModelo(
            String... columnas
    ) {
        return new DefaultTableModel(
                columnas,
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
    }

    private void prepararTabla(JTable tabla) {

        tabla.setRowHeight(25);

        tabla.getTableHeader()
                .setReorderingAllowed(false);
    }

    private void cargarEstudiantes() {

        comboEstudiante.removeAllItems();

        List<Estudiante> estudiantes =
                estudianteController.listar();

        for (Estudiante estudiante : estudiantes) {
            comboEstudiante.addItem(estudiante);
        }
    }

    private void cargarLibrosMasPrestados() {

        modeloMasPrestados.setRowCount(0);

        List<LibroPrestadoReporte> libros =
                reporteController
                        .listarLibrosMasPrestados();

        for (LibroPrestadoReporte libro : libros) {

            modeloMasPrestados.addRow(
                    new Object[]{
                            libro.getTitulo(),
                            libro.getAutor(),
                            libro.getCantidadPrestamos()
                    }
            );
        }
    }

    private void cargarHistorial() {

        modeloHistorial.setRowCount(0);

        Estudiante estudiante =
                (Estudiante) comboEstudiante
                        .getSelectedItem();

        if (estudiante == null) {
            return;
        }

        List<Prestamo> prestamos =
                reporteController
                        .listarHistorialEstudiante(
                                estudiante.getId()
                        );

        for (Prestamo prestamo : prestamos) {

            modeloHistorial.addRow(
                    new Object[]{
                            prestamo.getId(),
                            prestamo
                                    .getLibro()
                                    .getTitulo(),
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion(),
                            obtenerEstado(prestamo)
                    }
            );
        }
    }

    private void cargarPrestamosActivos() {

        modeloActivos.setRowCount(0);

        List<Prestamo> prestamos =
                reporteController
                        .listarPrestamosActivos();

        for (Prestamo prestamo : prestamos) {

            modeloActivos.addRow(
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
                            obtenerEstado(prestamo)
                    }
            );
        }
    }

    private String obtenerEstado(
            Prestamo prestamo
    ) {
        if (prestamo.isDevuelto()) {
            return "Devuelto";
        }

        if (prestamo.estaAtrasado()) {
            return "Atrasado";
        }

        return "En préstamo";
    }
}