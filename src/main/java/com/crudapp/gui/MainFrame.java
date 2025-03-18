package com.crudapp.gui;

import com.crudapp.dao.ArchivoDAOImpl;
import com.crudapp.dao.DatoDAOImpl;
import com.crudapp.model.Archivo;
import com.crudapp.model.Dato;
import com.crudapp.service.DatoService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private DatoService datoService;
    private JTable tablaDatos, tablaExcel, tablaWord;
    private DefaultTableModel modeloDatos, modeloExcel, modeloWord;
    private JTextField txtBusquedaDatos, txtBusquedaExcel, txtBusquedaWord;
    private JTextField campoTexto, campoCategoria;
    private JFormattedTextField campoFecha;

    public MainFrame() {
        setTitle("Gestor de Archivos y Datos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Fondo gris claro

        // Inicializar DatoService con DatoDAOImpl y ArchivoDAOImpl
        datoService = new DatoService(new DatoDAOImpl(), new ArchivoDAOImpl());

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Menú
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(211, 211, 211)); // Gris medio
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setForeground(Color.BLACK);
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        // Pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(211, 211, 211)); // Gris medio para pestañas inactivas
        tabbedPane.setForeground(Color.BLACK);
        UIManager.put("TabbedPane.selectedBackground", new Color(74, 144, 226)); // Azul suave para pestaña activa
        UIManager.put("TabbedPane.selectedForeground", Color.WHITE);

        // Pestaña Datos
        JPanel panelDatos = crearPanelDatos();
        tabbedPane.addTab("Datos", panelDatos);

        // Pestaña Archivos Excel
        JPanel panelExcel = crearPanelExcel();
        tabbedPane.addTab("Archivos Excel", panelExcel);

        // Pestaña Archivos Word
        JPanel panelWord = crearPanelWord();
        tabbedPane.addTab("Archivos Word", panelWord);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelDatos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245)); // Gris claro

        // Búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.setBackground(new Color(245, 245, 245));
        txtBusquedaDatos = new JTextField(20);
        txtBusquedaDatos.setBorder(BorderFactory.createLineBorder(new Color(74, 144, 226))); // Borde azul suave
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBusquedaDatos);
        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Campos de entrada y tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(245, 245, 245));
        JPanel panelEntrada = new JPanel(new GridBagLayout());
        panelEntrada.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panelEntrada.add(new JLabel("Texto:"), gbc);
        gbc.gridx = 1;
        campoTexto = new JTextField(15);
        campoTexto.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204))); // Borde gris claro
        panelEntrada.add(campoTexto, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelEntrada.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        campoFecha = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        campoFecha.setValue(new java.util.Date());
        campoFecha.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        panelEntrada.add(campoFecha, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelEntrada.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        campoCategoria = new JTextField(15);
        campoCategoria.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        panelEntrada.add(campoCategoria, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 204, 113)); // Verde
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnAgregar.setBackground(new Color(55, 235, 130)); // Hover más claro
            }
            public void mouseExited(MouseEvent evt) {
                btnAgregar.setBackground(new Color(46, 204, 113));
            }
        });
        panelEntrada.add(btnAgregar, gbc);

        modeloDatos = new DefaultTableModel(new Object[]{"ID", "Texto", "Fecha", "Categoría"}, 0);
        tablaDatos = new JTable(modeloDatos);
        tablaDatos.setBackground(Color.WHITE);
        tablaDatos.setGridColor(new Color(204, 204, 204));
        tablaDatos.getTableHeader().setBackground(new Color(41, 128, 185)); // Azul oscuro
        tablaDatos.getTableHeader().setForeground(Color.WHITE);
        tablaDatos.setSelectionBackground(new Color(74, 144, 226)); // Selección azul suave
        panelCentral.add(panelEntrada, BorderLayout.WEST);
        panelCentral.add(new JScrollPane(tablaDatos), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(245, 245, 245));
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(52, 152, 219)); // Azul
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnActualizar.setBackground(new Color(62, 182, 255));
            }
            public void mouseExited(MouseEvent evt) {
                btnActualizar.setBackground(new Color(52, 152, 219));
            }
        });

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnEliminar.setBackground(new Color(255, 99, 71));
            }
            public void mouseExited(MouseEvent evt) {
                btnEliminar.setBackground(new Color(231, 76, 60));
            }
        });

        JButton btnCargarDB = new JButton("Cargar desde DB");
        btnCargarDB.setBackground(new Color(52, 152, 219)); // Azul
        btnCargarDB.setForeground(Color.WHITE);
        btnCargarDB.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCargarDB.setBackground(new Color(62, 182, 255));
            }
            public void mouseExited(MouseEvent evt) {
                btnCargarDB.setBackground(new Color(52, 152, 219));
            }
        });

        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargarDB);

        panel.add(panelCentral, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnAgregar.addActionListener(e -> agregarDato());
        btnActualizar.addActionListener(e -> actualizarDato());
        btnEliminar.addActionListener(e -> eliminarDato());
        btnCargarDB.addActionListener(e -> cargarDatos());
        txtBusquedaDatos.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrarDatos(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrarDatos(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrarDatos(); }
        });
        tablaDatos.getSelectionModel().addListSelectionListener(e -> llenarCamposDatos());

        cargarDatos();
        return panel;
    }

    private JPanel crearPanelExcel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.setBackground(new Color(245, 245, 245));
        txtBusquedaExcel = new JTextField(20);
        txtBusquedaExcel.setBorder(BorderFactory.createLineBorder(new Color(74, 144, 226)));
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBusquedaExcel);
        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla
        modeloExcel = new DefaultTableModel(new Object[]{"Nombre", "Ruta", "Fecha"}, 0);
        tablaExcel = new JTable(modeloExcel);
        tablaExcel.setBackground(Color.WHITE);
        tablaExcel.setGridColor(new Color(204, 204, 204));
        tablaExcel.getTableHeader().setBackground(new Color(41, 128, 185));
        tablaExcel.getTableHeader().setForeground(Color.WHITE);
        tablaExcel.setSelectionBackground(new Color(74, 144, 226));
        panel.add(new JScrollPane(tablaExcel), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(245, 245, 245));
        JButton btnCargar = new JButton("Cargar Excel");
        btnCargar.setBackground(new Color(46, 204, 113)); // Verde
        btnCargar.setForeground(Color.WHITE);
        btnCargar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCargar.setBackground(new Color(55, 235, 130));
            }
            public void mouseExited(MouseEvent evt) {
                btnCargar.setBackground(new Color(46, 204, 113));
            }
        });

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(52, 152, 219)); // Azul
        btnEditar.setForeground(Color.WHITE);
        btnEditar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnEditar.setBackground(new Color(62, 182, 255));
            }
            public void mouseExited(MouseEvent evt) {
                btnEditar.setBackground(new Color(52, 152, 219));
            }
        });

        JButton btnPrevisualizar = new JButton("Previsualizar e Importar");
        btnPrevisualizar.setBackground(new Color(52, 152, 219)); // Azul
        btnPrevisualizar.setForeground(Color.WHITE);
        btnPrevisualizar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnPrevisualizar.setBackground(new Color(62, 182, 255));
            }
            public void mouseExited(MouseEvent evt) {
                btnPrevisualizar.setBackground(new Color(52, 152, 219));
            }
        });

        panelBotones.add(btnCargar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnPrevisualizar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnCargar.addActionListener(e -> cargarArchivoExcel());
        btnEditar.addActionListener(e -> editarArchivoExcel());
        btnPrevisualizar.addActionListener(e -> datoService.previsualizarEImportarExcel(this));
        txtBusquedaExcel.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrarExcel(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrarExcel(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrarExcel(); }
        });

        cargarArchivosExcel();
        return panel;
    }

    private JPanel crearPanelWord() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.setBackground(new Color(245, 245, 245));
        txtBusquedaWord = new JTextField(20);
        txtBusquedaWord.setBorder(BorderFactory.createLineBorder(new Color(74, 144, 226)));
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBusquedaWord);
        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla
        modeloWord = new DefaultTableModel(new Object[]{"Nombre", "Ruta", "Fecha"}, 0);
        tablaWord = new JTable(modeloWord);
        tablaWord.setBackground(Color.WHITE);
        tablaWord.setGridColor(new Color(204, 204, 204));
        tablaWord.getTableHeader().setBackground(new Color(41, 128, 185));
        tablaWord.getTableHeader().setForeground(Color.WHITE);
        tablaWord.setSelectionBackground(new Color(74, 144, 226));
        panel.add(new JScrollPane(tablaWord), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(245, 245, 245));
        JButton btnCargar = new JButton("Cargar Word");
        btnCargar.setBackground(new Color(46, 204, 113)); // Verde
        btnCargar.setForeground(Color.WHITE);
        btnCargar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCargar.setBackground(new Color(55, 235, 130));
            }
            public void mouseExited(MouseEvent evt) {
                btnCargar.setBackground(new Color(46, 204, 113));
            }
        });

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(52, 152, 219)); // Azul
        btnEditar.setForeground(Color.WHITE);
        btnEditar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnEditar.setBackground(new Color(62, 182, 255));
            }
            public void mouseExited(MouseEvent evt) {
                btnEditar.setBackground(new Color(52, 152, 219));
            }
        });

        panelBotones.add(btnCargar);
        panelBotones.add(btnEditar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnCargar.addActionListener(e -> cargarArchivoWord());
        btnEditar.addActionListener(e -> editarArchivoWord());
        txtBusquedaWord.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrarWord(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrarWord(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrarWord(); }
        });

        cargarArchivosWord();
        return panel;
    }

    // Métodos de la pestaña Datos
    private void agregarDato() {
        Dato dato = new Dato(campoTexto.getText().trim(), campoFecha.getText(), campoCategoria.getText().trim());
        if (datoService.agregarDato(dato, this)) {
            limpiarCamposDatos();
            cargarDatos();
        }
    }

    private void actualizarDato() {
        int fila = tablaDatos.getSelectedRow();
        if (fila != -1) {
            int id = (int) modeloDatos.getValueAt(fila, 0);
            Dato dato = new Dato(id, campoTexto.getText().trim(), campoFecha.getText(), campoCategoria.getText().trim());
            if (datoService.actualizarDato(dato, this)) {
                limpiarCamposDatos();
                cargarDatos();
            }
        }
    }

    private void eliminarDato() {
        int fila = tablaDatos.getSelectedRow();
        if (fila != -1) {
            int id = (int) modeloDatos.getValueAt(fila, 0);
            if (datoService.eliminarDato(id, this)) {
                limpiarCamposDatos();
                cargarDatos();
            }
        }
    }

    private void cargarDatos() {
        modeloDatos.setRowCount(0);
        for (Dato dato : datoService.cargarDatos(this)) {
            modeloDatos.addRow(new Object[]{dato.getId(), dato.getTexto(), dato.getFecha(), dato.getCategoria()});
        }
    }

    private void filtrarDatos() {
        String filtro = txtBusquedaDatos.getText().toLowerCase();
        modeloDatos.setRowCount(0);
        for (Dato dato : datoService.cargarDatos(this)) {
            if (dato.getTexto().toLowerCase().contains(filtro) || dato.getCategoria().toLowerCase().contains(filtro)) {
                modeloDatos.addRow(new Object[]{dato.getId(), dato.getTexto(), dato.getFecha(), dato.getCategoria()});
            }
        }
    }

    private void llenarCamposDatos() {
        int fila = tablaDatos.getSelectedRow();
        if (fila != -1) {
            campoTexto.setText((String) modeloDatos.getValueAt(fila, 1));
            campoFecha.setText((String) modeloDatos.getValueAt(fila, 2));
            campoCategoria.setText((String) modeloDatos.getValueAt(fila, 3));
        }
    }

    private void limpiarCamposDatos() {
        campoTexto.setText("");
        campoCategoria.setText("");
        campoFecha.setValue(new java.util.Date());
        tablaDatos.clearSelection();
    }

    // Métodos de la pestaña Excel
    private void cargarArchivoExcel() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel (*.xls, *.xlsx)", "xls", "xlsx");
        fileChooser.setFileFilter(filter);
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            if (archivoSeleccionado != null && archivoSeleccionado.exists()) {
                String ruta = archivoSeleccionado.getAbsolutePath();
                try {
                    datoService.cargarArchivoExcel(ruta, this);
                    cargarArchivosExcel();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al cargar el archivo Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace(); // Para depuración
                }
            } else {
                JOptionPane.showMessageDialog(this, "El archivo seleccionado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarArchivoExcel() {
        int fila = tablaExcel.getSelectedRow();
        if (fila != -1) {
            String ruta = (String) modeloExcel.getValueAt(fila, 1);
            File archivo = new File(ruta);
            if (archivo.exists()) {
                try {
                    Desktop.getDesktop().open(archivo); // Abrir el archivo en la aplicación predeterminada
                    int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas actualizar los cambios en la aplicación?", "Guardar Cambios", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.YES_OPTION) {
                        datoService.actualizarArchivoExcel(fila, ruta);
                        cargarArchivosExcel();
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al abrir el archivo Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no existe en la ruta especificada: " + ruta, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo para editar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarArchivosExcel() {
        modeloExcel.setRowCount(0);
        try {
            ArrayList<Archivo> archivos = datoService.getArchivosExcel();
            if (archivos != null) {
                for (Archivo archivo : archivos) {
                    modeloExcel.addRow(new Object[]{archivo.getNombre(), archivo.getRuta(), archivo.getFechaCarga()});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar archivos Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void filtrarExcel() {
        String filtro = txtBusquedaExcel.getText().toLowerCase();
        modeloExcel.setRowCount(0);
        try {
            ArrayList<Archivo> archivos = datoService.getArchivosExcel();
            if (archivos != null) {
                for (Archivo archivo : archivos) {
                    if (archivo.getNombre().toLowerCase().contains(filtro) || archivo.getRuta().toLowerCase().contains(filtro)) {
                        modeloExcel.addRow(new Object[]{archivo.getNombre(), archivo.getRuta(), archivo.getFechaCarga()});
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar archivos Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Métodos de la pestaña Word
    private void cargarArchivoWord() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Word (*.docx)", "docx");
        fileChooser.setFileFilter(filter);
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            if (archivoSeleccionado != null && archivoSeleccionado.exists()) {
                String ruta = archivoSeleccionado.getAbsolutePath();
                try {
                    datoService.cargarArchivoWord(ruta, this);
                    cargarArchivosWord();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al cargar el archivo Word: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace(); // Para depuración
                }
            } else {
                JOptionPane.showMessageDialog(this, "El archivo seleccionado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarArchivoWord() {
        int fila = tablaWord.getSelectedRow();
        if (fila != -1) {
            String ruta = (String) modeloWord.getValueAt(fila, 1);
            File archivo = new File(ruta);
            if (archivo.exists()) {
                try {
                    Desktop.getDesktop().open(archivo); // Abrir el archivo en la aplicación predeterminada
                    int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas actualizar los cambios en la aplicación?", "Guardar Cambios", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.YES_OPTION) {
                        datoService.actualizarArchivoWord(fila, ruta);
                        cargarArchivosWord();
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al abrir el archivo Word: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no existe en la ruta especificada: " + ruta, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo para editar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarArchivosWord() {
        modeloWord.setRowCount(0);
        try {
            ArrayList<Archivo> archivos = datoService.getArchivosWord();
            if (archivos != null) {
                for (Archivo archivo : archivos) {
                    modeloWord.addRow(new Object[]{archivo.getNombre(), archivo.getRuta(), archivo.getFechaCarga()});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar archivos Word: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void filtrarWord() {
        String filtro = txtBusquedaWord.getText().toLowerCase();
        modeloWord.setRowCount(0);
        try {
            ArrayList<Archivo> archivos = datoService.getArchivosWord();
            if (archivos != null) {
                for (Archivo archivo : archivos) {
                    if (archivo.getNombre().toLowerCase().contains(filtro) || archivo.getRuta().toLowerCase().contains(filtro)) {
                        modeloWord.addRow(new Object[]{archivo.getNombre(), archivo.getRuta(), archivo.getFechaCarga()});
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar archivos Word: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}