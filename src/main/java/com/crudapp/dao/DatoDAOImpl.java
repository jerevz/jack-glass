package com.crudapp.dao;

import com.crudapp.database.DatabaseConnection;
import com.crudapp.model.Dato;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class DatoDAOImpl implements DatoDAO {

    @Override
    public boolean agregarDato(Dato dato, Component parent) {
        String sql = "INSERT INTO Datos (texto, fecha, categoria) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, dato.getTexto());
            pstmt.setString(2, dato.getFecha());
            pstmt.setString(3, dato.getCategoria());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                // Obtener el ID generado
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        dato.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al agregar dato: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public boolean actualizarDato(Dato dato, Component parent) {
        String sql = "UPDATE Datos SET texto = ?, fecha = ?, categoria = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dato.getTexto());
            pstmt.setString(2, dato.getFecha());
            pstmt.setString(3, dato.getCategoria());
            pstmt.setInt(4, dato.getId());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al actualizar dato: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public boolean eliminarDato(int id, Component parent) {
        String sql = "DELETE FROM Datos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al eliminar dato: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public ArrayList<Dato> cargarDatos(Component parent) {
        ArrayList<Dato> datos = new ArrayList<>();
        String sql = "SELECT id, texto, fecha, categoria FROM Datos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Dato dato = new Dato(rs.getString("texto"), rs.getString("fecha"), rs.getString("categoria"));
                dato.setId(rs.getInt("id"));
                datos.add(dato);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return datos;
    }

    @Override
    public boolean importarDatos(ArrayList<Dato> datos, Component parent) {
        for (Dato dato : datos) {
            if (!agregarDato(dato, parent)) {
                return false;
            }
        }
        return true;
    }
}