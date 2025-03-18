package com.crudapp.gui;

import com.crudapp.dao.ArchivoDAOImpl;
import com.crudapp.dao.DatoDAOImpl;
import com.crudapp.service.DatoService;
import com.crudapp.model.Dato;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class CrudGUI extends JFrame {
    private JTextField campoTexto, campoCategoria;
    private JFormattedTextField campoFecha;
    private DefaultListModel<String> modeloLista;
    private JList<String> listaDatos;
    private DatoService datoService;

    public CrudGUI() {
        // Inicializa datoService con implementaciones concretas
        datoService = new DatoService(new DatoDAOImpl(), new ArchivoDAOImpl());

        setTitle("Aplicación CRUD Avanzada");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarComponentes();
        cargarDesdeDB();
    }

    private void inicializarComponentes() {
        JPanel panelSuperior = new JPanel(new GridLayout(2, 3));
        campoTexto = new JTextField(20);
        campoCategoria = new JTextField(20);
        campoFecha = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        campoFecha.setValue(new java.util.Date());
        JButton botonAgregar = new JButton("Agregar");

        panelSuperior.add(new JLabel("Texto:"));
        panelSuperior.add(campoTexto);
        panelSuperior.add(botonAgregar);
        panelSuperior.add(new JLabel("Categoría:"));
        panelSuperior.add(campoCategoria);
        panelSuperior.add(new JLabel("Fecha:"));
        panelSuperior.add(campoFecha);

        modeloLista = new DefaultListModel<>();
        listaDatos = new JList<>(modeloLista);
        JScrollPane scrollPane = new JScrollPane(listaDatos);

        JPanel panelInferior = new JPanel();
        JButton botonActualizar = new JButton("Actualizar");
        JButton botonEliminar = new JButton("Eliminar");
        JButton botonCargarDB = new JButton("Cargar desde DB");
        JButton botonEditarExcel = new JButton("Editar Excel");
        JButton botonEditarWord = new JButton("Editar Word");
        JButton botonImportarExcel = new JButton("Previsualizar e Importar Excel");

        panelInferior.add(botonActualizar);
        panelInferior.add(botonEliminar);
        panelInferior.add(botonCargarDB);
        panelInferior.add(botonEditarExcel);
        panelInferior.add(botonEditarWord);
        panelInferior.add(botonImportarExcel);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        botonAgregar.addActionListener(e -> agregarDato());
        botonActualizar.addActionListener(e -> actualizarDato());
        botonEliminar.addActionListener(e -> eliminarDato());
        botonCargarDB.addActionListener(e -> cargarDesdeDB());
        botonEditarExcel.addActionListener(e -> datoService.editarExcel(this));
        botonEditarWord.addActionListener(e -> datoService.editarWord(this));
        botonImportarExcel.addActionListener(e -> datoService.previsualizarEImportarExcel(this));
    }

    private void agregarDato() {
        Dato dato = new Dato(campoTexto.getText().trim(), campoFecha.getText(), campoCategoria.getText().trim());
        if (datoService.agregarDato(dato, this)) {
            limpiarCampos();
            cargarDesdeDB();
        }
    }

    private void actualizarDato() {
        int indice = listaDatos.getSelectedIndex();
        if (indice != -1) {
            Dato dato = new Dato(campoTexto.getText().trim(), campoFecha.getText(), campoCategoria.getText().trim());
            dato.setId(obtenerIdPorIndice(indice));
            if (datoService.actualizarDato(dato, this)) {
                limpiarCampos();
                cargarDesdeDB();
            }
        }
    }

    private void eliminarDato() {
        int indice = listaDatos.getSelectedIndex();
        if (indice != -1 && datoService.eliminarDato(obtenerIdPorIndice(indice), this)) {
            cargarDesdeDB();
        }
    }

    private void cargarDesdeDB() {
        modeloLista.clear();
        for (Dato dato : datoService.cargarDatos(this)) {
            modeloLista.addElement(dato.toString());
        }
    }

    private int obtenerIdPorIndice(int indice) {
        String elemento = modeloLista.get(indice);
        return Integer.parseInt(elemento.split(":")[0].trim());
    }

    private void limpiarCampos() {
        campoTexto.setText("");
        campoCategoria.setText("");
        campoFecha.setValue(new java.util.Date());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CrudGUI gui = new CrudGUI();
            gui.setVisible(true);
        });
    }
}