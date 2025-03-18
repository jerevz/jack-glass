package com.crudapp.dao;

import com.crudapp.model.Archivo;

import java.awt.Component;
import java.util.ArrayList;

public interface ArchivoDAO {
    boolean guardarArchivo(Archivo archivo, Component parent);
    ArrayList<Archivo> cargarArchivos(Component parent);
    Archivo obtenerArchivoPorId(int id, Component parent);
    boolean actualizarArchivo(Archivo archivo, Component parent);
    boolean eliminarArchivo(int id, Component parent);
}