package com.crudapp.file;

import com.crudapp.dao.DatoDAO;
import com.crudapp.gui.PreviewDialog;
import com.crudapp.model.Dato;
import com.crudapp.service.DatoService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExcelHandler implements FileHandler {
    private DatoDAO datoDAO;

    public ExcelHandler(DatoDAO datoDAO) {
        this.datoDAO = datoDAO;
    }

    @Override
    public void editar(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".xlsx")) {
                JOptionPane.showMessageDialog(parent, "Por favor, selecciona un archivo Excel (.xlsx).");
                return;
            }
            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0) != null ? workbook.getSheetAt(0) : workbook.createSheet("Datos");
                int rowNum = 0;
                for (Row row : sheet) rowNum++;
                for (Dato dato : datoDAO.cargarDatos(parent)) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dato.getId());
                    row.createCell(1).setCellValue(dato.getTexto());
                    row.createCell(2).setCellValue(dato.getFecha());
                    row.createCell(3).setCellValue(dato.getCategoria());
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(parent, "Excel editado exitosamente");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error al editar Excel: " + e.getMessage());
            }
        }
    }

    @Override
    public void previsualizarEImportar(JFrame parent, Object service) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel (*.xls, *.xlsx)", "xls", "xlsx");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file)) {
                Workbook workbook;
                if (file.getName().endsWith(".xls")) {
                    workbook = new HSSFWorkbook(fis);
                } else if (file.getName().endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(fis);
                } else {
                    JOptionPane.showMessageDialog(parent, "Formato no soportado. Usa .xls o .xlsx.");
                    return;
                }
                Sheet sheet = workbook.getSheetAt(0);
                ArrayList<Dato> datos = new ArrayList<>();
                for (Row row : sheet) {
                    if (row.getCell(0) == null) continue;

                    // Manejar diferentes tipos de datos en las celdas
                    String texto = getCellValueAsString(row.getCell(1));
                    String fecha = getCellValueAsString(row.getCell(2), "2023-01-01");
                    String categoria = getCellValueAsString(row.getCell(3));

                    datos.add(new Dato(texto, fecha, categoria));
                }
                new PreviewDialog(parent, datos, (DatoService) service).setVisible(true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error al previsualizar Excel: " + e.getMessage());
            }
        }
    }

    // Método auxiliar para obtener el valor de una celda como String
    private String getCellValueAsString(Cell cell) {
        return getCellValueAsString(cell, "");
    }

    private String getCellValueAsString(Cell cell, String defaultValue) {
        if (cell == null) return defaultValue;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return defaultValue;
        }
    }
}