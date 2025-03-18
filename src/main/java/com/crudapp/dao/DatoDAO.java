package com.crudapp.dao;

import com.crudapp.model.Dato;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public interface DatoDAO {
    boolean agregarDato(Dato dato, Component parent);
    boolean actualizarDato(Dato dato, Component parent);
    boolean eliminarDato(int id, Component parent);
    ArrayList<Dato> cargarDatos(Component parent);
    boolean importarDatos(ArrayList<Dato> datos, Component parent);
}