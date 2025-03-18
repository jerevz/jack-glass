package com.crudapp.service;

import com.crudapp.dao.ArchivoDAO;
import com.crudapp.dao.DatoDAO;
import com.crudapp.file.ExcelHandler;
import com.crudapp.file.FileHandler;
import com.crudapp.file.WordHandler;
import com.crudapp.model.Archivo;
import com.crudapp.model.Dato;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DatoService {
    private DatoDAO datoDAO;
    private ArchivoDAO archivoDAO;
    private FileHandler excelHandler;
    private FileHandler wordHandler;
    private ArrayList<Archivo> archivosExcel;
    private ArrayList<Archivo> archivosWord;

    public DatoService(DatoDAO datoDAO, ArchivoDAO archivoDAO) {
        this.datoDAO = datoDAO;
        this.archivoDAO = archivoDAO;
        this.excelHandler = new ExcelHandler(datoDAO);
        this.wordHandler = new WordHandler(datoDAO);
        this.archivosExcel = new ArrayList<>();
        this.archivosWord = new ArrayList<>();
    }

    // Métodos para Datos
    public boolean agregarDato(Dato dato, Component parent) {
        return datoDAO.agregarDato(dato, parent);
    }

    public boolean actualizarDato(Dato dato, Component parent) {
        return datoDAO.actualizarDato(dato, parent);
    }

    public boolean eliminarDato(int id, Component parent) {
        return datoDAO.eliminarDato(id, parent);
    }

    public ArrayList<Dato> cargarDatos(Component parent) {
        return datoDAO.cargarDatos(parent);
    }

    public boolean importarDatos(ArrayList<Dato> datos, Component parent) {
        return datoDAO.importarDatos(datos, parent);
    }

    // Métodos para archivos Excel y Word
    public void editarExcel(JFrame parent) {
        excelHandler.editar(parent);
    }

    public void previsualizarEImportarExcel(JFrame parent) {
        excelHandler.previsualizarEImportar(parent, this);
    }

    public void editarWord(JFrame parent) {
        wordHandler.editar(parent);
    }

    public void cargarArchivoExcel(String ruta, Component parent) {
        java.io.File file = new java.io.File(ruta);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(parent, "El archivo no existe en la ruta especificada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Archivo archivo = new Archivo();
        archivo.setNombre(file.getName());
        archivo.setRuta(ruta);
        archivo.setTipo(file.getName().substring(file.getName().lastIndexOf(".") + 1));
        if (archivoDAO != null && archivoDAO.guardarArchivo(archivo, parent)) {
            archivosExcel.add(archivo);
            JOptionPane.showMessageDialog(parent, "Archivo Excel cargado y guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "Error al guardar el archivo Excel en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarArchivoWord(String ruta, Component parent) {
        java.io.File file = new java.io.File(ruta);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(parent, "El archivo no existe en la ruta especificada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Archivo archivo = new Archivo();
        archivo.setNombre(file.getName());
        archivo.setRuta(ruta);
        archivo.setTipo(file.getName().substring(file.getName().lastIndexOf(".") + 1));
        if (archivoDAO != null && archivoDAO.guardarArchivo(archivo, parent)) {
            archivosWord.add(archivo);
            JOptionPane.showMessageDialog(parent, "Archivo Word cargado y guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "Error al guardar el archivo Word en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ArrayList<Archivo> getArchivosExcel() {
        return archivosExcel;
    }

    public ArrayList<Archivo> getArchivosWord() {
        return archivosWord;
    }

    public void actualizarArchivoExcel(int index, String ruta) {
        if (index >= 0 && index < archivosExcel.size()) {
            archivosExcel.set(index, new Archivo(new java.io.File(ruta).getName(), ruta));
        }
    }

    public void actualizarArchivoWord(int index, String ruta) {
        if (index >= 0 && index < archivosWord.size()) {
            archivosWord.set(index, new Archivo(new java.io.File(ruta).getName(), ruta));
        }
    }
}