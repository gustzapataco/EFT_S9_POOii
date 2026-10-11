package view;

import controller.EstudianteController;
import model.Estudiante;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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

// Ventana para administrar estudiantes.
public class VentanaEstudiantes extends JFrame {

    private final EstudianteController
            estudianteController;

    private final JTextField campoNombre;
    private final JTextField campoRut;
    private final JTextField campoCurso;
    private final JTextField campoCorreo;

    private final JButton botonNuevo;
    private final JButton botonGuardar;
    private final JButton botonActualizar;
    private final JButton botonEliminar;
    private final JButton botonCerrar;

    private final JTable tablaEstudiantes;
    private final DefaultTableModel modeloTabla;

    private int idEstudianteSeleccionado;

    public VentanaEstudiantes() {

        estudianteController =
                new EstudianteController();

        campoNombre = new JTextField(20);
        campoRut = new JTextField(20);
        campoCurso = new JTextField(20);
        campoCorreo = new JTextField(20);

        botonNuevo = new JButton("Nuevo");
        botonGuardar = new JButton("Guardar");
        botonActualizar = new JButton("Actualizar");
        botonEliminar = new JButton("Eliminar");
        botonCerrar = new JButton("Cerrar");

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre",
                        "RUT",
                        "Curso",
                        "Correo"
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

        tablaEstudiantes =
                new JTable(modeloTabla);

        idEstudianteSeleccionado = 0;

        configurarVentana();
        crearContenido();
        configurarTabla();
        configurarEventos();
        cargarEstudiantes();
    }

    private void configurarVentana() {

        setTitle("Biblioteca Escolar - Gestión de estudiantes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 620);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);
    }

    private void crearContenido() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                ));

        JLabel etiquetaTitulo = new JLabel("Gestión de estudiantes");
        etiquetaTitulo.setFont(new Font(
                        "Tahoma",
                        Font.BOLD,
                        24
                ));

        JPanel panelFormulario = crearPanelFormulario();
        JPanel panelBotones = crearPanelBotones();
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));

        panelSuperior.add(etiquetaTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        JScrollPane desplazamiento = new JScrollPane(tablaEstudiantes);
        desplazamiento.setBorder(BorderFactory.createTitledBorder(
                        "Estudiantes registrados"));

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(desplazamiento, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelFormulario() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createTitledBorder("Datos del estudiante"));
        GridBagConstraints restricciones = new GridBagConstraints();

        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.insets = new Insets(6, 8, 6, 8);

        agregarCampo(
                panel,
                restricciones,
                "Nombre",
                campoNombre,
                0,
                0
        );

        agregarCampo(
                panel,
                restricciones,
                "RUT",
                campoRut,
                0,
                2
        );

        agregarCampo(
                panel,
                restricciones,
                "Curso",
                campoCurso,
                1,
                0
        );

        agregarCampo(
                panel,
                restricciones,
                "Correo",
                campoCorreo,
                1,
                2
        );

        return panel;
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints restricciones,
            String texto,
            java.awt.Component componente,
            int fila,
            int columna
    ) {
        restricciones.gridy = fila;
        restricciones.gridx = columna;
        restricciones.weightx = 0;

        panel.add(
                new JLabel(texto),
                restricciones
        );

        restricciones.gridx = columna + 1;
        restricciones.weightx = 1;

        panel.add(
                componente,
                restricciones
        );
    }

    private JPanel crearPanelBotones() {

        JPanel panel = new JPanel(new GridLayout(
                        1,
                        5,
                        10,
                        10
                ));

        panel.setBorder(BorderFactory.createEmptyBorder(
                        10,
                        0,
                        5,
                        0
                )
        );

        panel.add(botonNuevo);
        panel.add(botonGuardar);
        panel.add(botonActualizar);
        panel.add(botonEliminar);
        panel.add(botonCerrar);

        return panel;
    }

    private void configurarTabla() {

        tablaEstudiantes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaEstudiantes.setRowHeight(25);

        tablaEstudiantes
                .getTableHeader()
                .setReorderingAllowed(false);
    }

    private void configurarEventos() {

        botonNuevo.addActionListener(
                evento -> limpiarFormulario());
        botonGuardar.addActionListener(
                evento -> guardarEstudiante());
        botonActualizar.addActionListener(
                evento -> actualizarEstudiante());
        botonEliminar.addActionListener(
                evento -> eliminarEstudiante());
        botonCerrar.addActionListener(
                evento -> dispose());
        tablaEstudiantes
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento.getValueIsAdjusting()) {
                                cargarEstudianteSeleccionado();
                            }
                        }
                );
    }

    private void cargarEstudiantes() {
        modeloTabla.setRowCount(0);
        List<Estudiante> estudiantes = estudianteController.listar();
        for (Estudiante estudiante : estudiantes) {

            modeloTabla.addRow(
                    new Object[]{
                            estudiante.getId(),
                            estudiante.getNombre(),
                            estudiante.getRut(),
                            estudiante.getCurso(),
                            estudiante.getCorreo()
                    }
            );
        }
    }

    private void guardarEstudiante() {

        Estudiante estudiante = obtenerEstudianteFormulario();

        if (estudiante == null) {
            return;
        }

        boolean guardado =
                estudianteController.guardar(
                        estudiante
                );

        if (guardado) {

            mostrarMensaje(
                    "Estudiante registrado correctamente.",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarEstudiantes();

        } else {

            mostrarMensaje(
                    "No fue posible registrar el estudiante.\n"
                            + "Verifique que el RUT no esté repetido.",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizarEstudiante() {

        if (idEstudianteSeleccionado == 0) {

            mostrarMensaje("Debe seleccionar un estudiante.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Estudiante estudiante =
                obtenerEstudianteFormulario();

        if (estudiante == null) {
            return;
        }

        estudiante.setId(idEstudianteSeleccionado);
        boolean actualizado = estudianteController.actualizar(estudiante);
        if (actualizado) {

            mostrarMensaje("Estudiante actualizado correctamente.",
                    JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarEstudiantes();

        } else {

            mostrarMensaje("No fue posible actualizar el estudiante.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEstudiante() {

        if (idEstudianteSeleccionado == 0) {
            mostrarMensaje("Debe seleccionar un estudiante.", JOptionPane.WARNING_MESSAGE);

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el estudiante seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = estudianteController.eliminar(idEstudianteSeleccionado);
        if (eliminado) {

            mostrarMensaje("Estudiante eliminado correctamente.",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarEstudiantes();

        } else {

            mostrarMensaje(
                    "No fue posible eliminar el estudiante.\n"
                            + "Puede tener préstamos relacionados.",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Estudiante obtenerEstudianteFormulario() {

        String nombre = campoNombre.getText().trim();
        String rut = campoRut.getText().trim();
        String curso = campoCurso.getText().trim();
        String correo = campoCorreo.getText().trim();

        if (
                nombre.isBlank()
                        || rut.isBlank()
                        || curso.isBlank()
                        || correo.isBlank()
        ) {
            mostrarMensaje("Debe completar todos los campos.",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        return new Estudiante(
                nombre,
                rut,
                curso,
                correo
        );
    }

    private void cargarEstudianteSeleccionado() {

        int fila = tablaEstudiantes.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idEstudianteSeleccionado = (int) modeloTabla.getValueAt(fila,0);
        Estudiante estudiante = estudianteController.buscarPorId(idEstudianteSeleccionado);

        if (estudiante == null) {
            return;
        }

        campoNombre.setText(estudiante.getNombre());
        campoRut.setText(estudiante.getRut());
        campoCurso.setText(estudiante.getCurso());
        campoCorreo.setText(estudiante.getCorreo());
    }

    private void limpiarFormulario() {

        idEstudianteSeleccionado = 0;

        campoNombre.setText("");
        campoRut.setText("");
        campoCurso.setText("");
        campoCorreo.setText("");

        tablaEstudiantes.clearSelection();
        campoNombre.requestFocus();
    }

    private void mostrarMensaje(
            String mensaje,
            int tipo
    ) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Biblioteca Escolar",
                tipo
        );
    }
}