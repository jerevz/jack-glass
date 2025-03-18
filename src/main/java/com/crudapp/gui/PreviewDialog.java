package com.crudapp.gui;

import com.crudapp.model.Dato;
import com.crudapp.service.DatoService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PreviewDialog extends JDialog {
    private DefaultListModel<String> previewModel;
    private JList<String> previewList;
    private ArrayList<Dato> datosParaImportar;
    private DatoService datoService;

    public PreviewDialog(JFrame parent, ArrayList<Dato> datos, DatoService datoService) {
        super(parent, "Previsualización de Excel", true);
        this.datosParaImportar = datos;
        this.datoService = datoService;
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Aplicar un diseño moderno (FlatLaf)
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        previewModel = new DefaultListModel<>();
        previewList = new JList<>(previewModel);
        for (Dato dato : datosParaImportar) {
            previewModel.addElement(dato.toString());
        }

        JScrollPane scrollPane = new JScrollPane(previewList);
        JButton importarButton = new JButton("Importar seleccionados");
        importarButton.addActionListener(e -> importarSeleccionados());

        add(scrollPane, BorderLayout.CENTER);
        add(importarButton, BorderLayout.SOUTH);
    }

    private void importarSeleccionados() {
        int[] indices = previewList.getSelectedIndices();
        ArrayList<Dato> seleccionados = new ArrayList<>();
        for (int i : indices) {
            seleccionados.add(datosParaImportar.get(i));
        }
        if (datoService.importarDatos(seleccionados, this)) {
            dispose();
            JOptionPane.showMessageDialog(this, "Datos importados exitosamente");
        }
    }
}