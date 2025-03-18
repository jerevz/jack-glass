package com.crudapp.file;

import com.crudapp.dao.DatoDAO;
import com.crudapp.model.Dato;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class WordHandler implements FileHandler {
    private DatoDAO datoDAO;

    public WordHandler(DatoDAO datoDAO) {
        this.datoDAO = datoDAO;
    }

    @Override
    public void editar(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Word (*.docx)", "docx");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".docx")) {
                JOptionPane.showMessageDialog(parent, "Por favor, selecciona un archivo Word (.docx).");
                return;
            }
            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {
                XWPFParagraph paragraph = document.createParagraph();
                for (Dato dato : datoDAO.cargarDatos(parent)) {
                    XWPFRun run = paragraph.createRun();
                    run.setText(dato.toString());
                    run.addBreak();
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    document.write(fos);
                    JOptionPane.showMessageDialog(parent, "Word editado exitosamente");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error al editar Word: " + e.getMessage());
            }
        }
    }

    @Override
    public void previsualizarEImportar(JFrame parent, Object service) {
        JOptionPane.showMessageDialog(parent, "Previsualización no disponible para Word.");
    }
}