package view;

import controller.CategoriaController;
import controller.LibroController;
import model.Categoria;
import model.Libro;
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

// Ventana registra, modifica, elimina y visualiza libros.
public class VentanaLibros extends JFrame {

    private final Usuario usuario;

    private final LibroController libroController;
    private final CategoriaController categoriaController;

    private final JTextField campoTitulo;
    private final JTextField campoAutor;
    private final JTextField campoIsbn;
    private final JTextField campoEditorial;
    private final JTextField campoStock;

    private final JComboBox<Categoria> comboCategoria;

    private final JButton botonNuevo;
    private final JButton botonGuardar;
    private final JButton botonActualizar;
    private final JButton botonEliminar;
    private final JButton botonCerrar;

    private final JTable tablaLibros;
    private final DefaultTableModel modeloTabla;

    // Guarda el ID del libro seleccionado.
    private int idLibroSeleccionado;
    public VentanaLibros(Usuario usuario) {
        this.usuario = usuario;

        libroController = new LibroController();
        categoriaController = new CategoriaController();

        campoTitulo = new JTextField(20);
        campoAutor = new JTextField(20);
        campoIsbn = new JTextField(20);
        campoEditorial = new JTextField(20);
        campoStock = new JTextField(20);

        comboCategoria = new JComboBox<>();

        botonNuevo = new JButton("Nuevo");
        botonGuardar = new JButton("Guardar");
        botonActualizar = new JButton("Actualizar");
        botonEliminar = new JButton("Eliminar");
        botonCerrar = new JButton("Cerrar");

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Título",
                        "Autor",
                        "ISBN",
                        "Editorial",
                        "Stock",
                        "Categoría"
                },
                0
        ) {
            // Impide modificar directamente las celdas.
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);

        idLibroSeleccionado = 0;

        configurarVentana();
        crearContenido();
        configurarTabla();
        configurarEventos();
        configurarPermisos();

        cargarCategorias();
        cargarLibros();
    }

    private void configurarVentana() {
        setTitle("Biblioteca Escolar - Gestión de libros");
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setSize(1100, 680);

        setMinimumSize(
                new Dimension(950, 600)
        );

        setLocationRelativeTo(null);
    }

    // Organiza los componentes dentro del JFrame.
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
                new JLabel("Gestión de libros");

        etiquetaTitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        JPanel panelFormulario =
                crearPanelFormulario();

        JPanel panelBotones =
                crearPanelBotones();

        JPanel panelSuperior =
                new JPanel(new BorderLayout(10, 10));

        panelSuperior.add(
                etiquetaTitulo,
                BorderLayout.NORTH
        );

        panelSuperior.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        panelSuperior.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        JScrollPane panelDesplazamiento =
                new JScrollPane(tablaLibros);

        panelDesplazamiento.setBorder(
                BorderFactory.createTitledBorder(
                        "Libros registrados"
                )
        );

        panelPrincipal.add(
                panelSuperior,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                panelDesplazamiento,
                BorderLayout.CENTER
        );

        setContentPane(panelPrincipal);
    }

    // Crea el formulario de libros.
    private JPanel crearPanelFormulario() {

        JPanel panel =
                new JPanel(new GridBagLayout());

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del libro"
                )
        );

        GridBagConstraints restricciones =
                new GridBagConstraints();

        restricciones.fill =
                GridBagConstraints.HORIZONTAL;

        restricciones.insets =
                new Insets(6, 8, 6, 8);

        restricciones.weightx = 1;

        agregarCampo(
                panel,
                restricciones,
                new JLabel("Título"),
                campoTitulo,
                0,
                0
        );

        agregarCampo(
                panel,
                restricciones,
                new JLabel("Autor"),
                campoAutor,
                0,
                2
        );

        agregarCampo(
                panel,
                restricciones,
                new JLabel("ISBN"),
                campoIsbn,
                1,
                0
        );

        agregarCampo(
                panel,
                restricciones,
                new JLabel("Editorial"),
                campoEditorial,
                1,
                2
        );

        agregarCampo(
                panel,
                restricciones,
                new JLabel("Stock"),
                campoStock,
                2,
                0
        );

        agregarCampo(
                panel,
                restricciones,
                new JLabel("Categoría"),
                comboCategoria,
                2,
                2
        );

        return panel;
    }

    // Agrega una etiqueta al formulario.
    private void agregarCampo(
            JPanel panel,
            GridBagConstraints restricciones,
            JLabel etiqueta,
            java.awt.Component componente,
            int fila,
            int columna
    ) {
        restricciones.gridy = fila;
        restricciones.gridx = columna;
        restricciones.weightx = 0;

        panel.add(
                etiqueta,
                restricciones
        );

        restricciones.gridx = columna + 1;
        restricciones.weightx = 1;

        panel.add(
                componente,
                restricciones
        );
    }

    // Crea los botones del CRUD.
    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(new GridLayout(
                        1,
                        5,
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

        panel.add(botonNuevo);
        panel.add(botonGuardar);
        panel.add(botonActualizar);
        panel.add(botonEliminar);
        panel.add(botonCerrar);

        return panel;
    }

     // Configura comportamiento visual de la tabla.
     private void configurarTabla() {

        tablaLibros.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaLibros.setRowHeight(25);

        tablaLibros.getTableHeader().setReorderingAllowed(
                false
        );

        // La columna ID queda más angosta.
        tablaLibros
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(35);

        tablaLibros
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(180);

        tablaLibros
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(130);
    }
    private void configurarEventos() {

        botonNuevo.addActionListener(
                evento -> limpiarFormulario()
        );

        botonGuardar.addActionListener(
                evento -> guardarLibro()
        );

        botonActualizar.addActionListener(
                evento -> actualizarLibro()
        );

        botonEliminar.addActionListener(
                evento -> eliminarLibro()
        );

        botonCerrar.addActionListener(
                evento -> dispose()
        );

        tablaLibros
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento.getValueIsAdjusting()) {
                                cargarLibroSeleccionado();
                            }
                        }
                );
    }

    // Restringe las operaciones según el rol.
    private void configurarPermisos() {

        boolean esBibliotecario =
                usuario.getRol()
                        == Rol.BIBLIOTECARIO;

        campoTitulo.setEditable(esBibliotecario);
        campoAutor.setEditable(esBibliotecario);
        campoIsbn.setEditable(esBibliotecario);
        campoEditorial.setEditable(esBibliotecario);
        campoStock.setEditable(esBibliotecario);

        comboCategoria.setEnabled(esBibliotecario);

        botonNuevo.setEnabled(esBibliotecario);
        botonGuardar.setEnabled(esBibliotecario);
        botonActualizar.setEnabled(esBibliotecario);
        botonEliminar.setEnabled(esBibliotecario);
    }

    // Carga categorías desde MySQL.
    private void cargarCategorias() {

        comboCategoria.removeAllItems();

        List<Categoria> categorias =
                categoriaController.listar();

        for (Categoria categoria : categorias) {
            comboCategoria.addItem(categoria);
        }
    }

    // Carga libros desde MySQL en la tabla.
    private void cargarLibros() {

        modeloTabla.setRowCount(0);

        List<Libro> libros = libroController.listar();
        for (Libro libro : libros) {
            modeloTabla.addRow(
                    new Object[]{
                            libro.getId(),
                            libro.getTitulo(),
                            libro.getAutor(),
                            libro.getIsbn(),
                            libro.getEditorial(),
                            libro.getStock(),
                            libro
                                    .getCategoria()
                                    .getNombre()
                    }
            );
        }
    }

    // Registra un libro nuevo.
    private void guardarLibro() {

        Libro libro = obtenerLibroFormulario();

        if (libro == null) {
            return;
        }

        boolean guardado =
                libroController.guardar(libro);

        if (guardado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro registrado correctamente.",
                    "Registro correcto",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarLibros();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el libro.\n"
                            + "Verifique que el ISBN no esté repetido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // Modifica el libro seleccionado.
    private void actualizarLibro() {

        if (idLibroSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un libro de la tabla.",
                    "Libro no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Libro libro = obtenerLibroFormulario();

        if (libro == null) {
            return;
        }

        libro.setId(idLibroSeleccionado);

        boolean actualizado =
                libroController.actualizar(libro);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro actualizado correctamente.",
                    "Actualización correcta",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarLibros();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Elimina el libro seleccionado.
    private void eliminarLibro() {

        if (idLibroSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un libro de la tabla.",
                    "Libro no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el libro seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado =
                libroController.eliminar(
                        idLibroSeleccionado
                );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro eliminado correctamente.",
                    "Eliminación correcta",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarLibros();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el libro.\n"
                            + "Puede tener préstamos relacionados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Valida los campos y crea un objeto Libro.
    private Libro obtenerLibroFormulario() {

        String titulo = campoTitulo.getText().trim();
        String autor = campoAutor.getText().trim();
        String isbn = campoIsbn.getText().trim();
        String editorial = campoEditorial.getText().trim();
        String textoStock = campoStock.getText().trim();

        Categoria categoria =
                (Categoria) comboCategoria
                        .getSelectedItem();

        if (
                titulo.isBlank()
                        || autor.isBlank()
                        || isbn.isBlank()
                        || editorial.isBlank()
                        || textoStock.isBlank()
                        || categoria == null
        ) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe completar todos los campos.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        int stock;

        try {
            stock = Integer.parseInt(textoStock);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero.",
                    "Stock incorrecto",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        if (stock < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El stock no puede ser negativo.",
                    "Stock incorrecto",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        return new Libro(
                titulo,
                autor,
                isbn,
                editorial,
                stock,
                categoria
        );
    }

    // Pasa datos fila seleccionada al formulario.
    private void cargarLibroSeleccionado() {

        int fila =
                tablaLibros.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idLibroSeleccionado =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        Libro libro =
                libroController.buscarPorId(
                        idLibroSeleccionado
                );

        if (libro == null) {
            return;
        }

        campoTitulo.setText(
                libro.getTitulo()
        );

        campoAutor.setText(
                libro.getAutor()
        );

        campoIsbn.setText(
                libro.getIsbn()
        );

        campoEditorial.setText(
                libro.getEditorial()
        );

        campoStock.setText(
                String.valueOf(
                        libro.getStock()
                )
        );

        seleccionarCategoria(
                libro.getCategoria().getId()
        );
    }

    // Selecciona en el JComboBox la categoría del libro.
    private void seleccionarCategoria(
            int idCategoria
    ) {
        for (
                int posicion = 0;
                posicion < comboCategoria.getItemCount();
                posicion++
        ) {
            Categoria categoria =
                    comboCategoria.getItemAt(posicion);

            if (categoria.getId() == idCategoria) {

                comboCategoria.setSelectedIndex(
                        posicion
                );

                break;
            }
        }
    }

    // Limpia los campos de relleno.
    private void limpiarFormulario() {

        idLibroSeleccionado = 0;
        campoTitulo.setText("");
        campoAutor.setText("");
        campoIsbn.setText("");
        campoEditorial.setText("");
        campoStock.setText("");

        if (comboCategoria.getItemCount() > 0) {
            comboCategoria.setSelectedIndex(0);
        }

        tablaLibros.clearSelection();
        campoTitulo.requestFocus();
    }
}