package com.crudapp.dao;

import com.crudapp.database.DatabaseConnection;
import com.crudapp.model.Archivo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ArchivoDAOImpl implements ArchivoDAO {

    @Override
    public boolean guardarArchivo(Archivo archivo, Component parent) {
        String sql = "INSERT INTO Archivos (nombre, ruta, tipo, fecha_carga, contenido, tamaño) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            File file = new File(archivo.getRuta());
            try (FileInputStream fis = new FileInputStream(file)) {
                pstmt.setString(1, archivo.getNombre());
                pstmt.setString(2, archivo.getRuta());
                pstmt.setString(3, archivo.getTipo());
                pstmt.setString(4, LocalDateTime.now().toString()); // Fecha actual como String
                pstmt.setBinaryStream(5, fis, (int) file.length());
                pstmt.setLong(6, file.length());
                int rows = pstmt.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(parent, "Error al guardar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Para depuración
            return false;
        }
    }

    @Override
    public ArrayList<Archivo> cargarArchivos(Component parent) {
        ArrayList<Archivo> archivos = new ArrayList<>();
        String sql = "SELECT id, nombre, ruta, tipo, fecha_carga FROM Archivos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Archivo archivo = new Archivo();
                archivo.setId(rs.getInt("id"));
                archivo.setNombre(rs.getString("nombre"));
                archivo.setRuta(rs.getString("ruta"));
                archivo.setTipo(rs.getString("tipo"));
                archivo.setFechaCarga(rs.getString("fecha_carga"));
                archivos.add(archivo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al cargar archivos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return archivos;
    }

    @Override
    public Archivo obtenerArchivoPorId(int id, Component parent) {
        String sql = "SELECT id, nombre, ruta, tipo, fecha_carga FROM Archivos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Archivo archivo = new Archivo();
                    archivo.setId(rs.getInt("id"));
                    archivo.setNombre(rs.getString("nombre"));
                    archivo.setRuta(rs.getString("ruta"));
                    archivo.setTipo(rs.getString("tipo"));
                    archivo.setFechaCarga(rs.getString("fecha_carga"));
                    return archivo;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al obtener archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean actualizarArchivo(Archivo archivo, Component parent) {
        String sql = "UPDATE Archivos SET nombre = ?, ruta = ?, tipo = ?, fecha_carga = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, archivo.getNombre());
            pstmt.setString(2, archivo.getRuta());
            pstmt.setString(3, archivo.getTipo());
            pstmt.setString(4, archivo.getFechaCarga());
            pstmt.setInt(5, archivo.getId());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al actualizar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarArchivo(int id, Component parent) {
        String sql = "DELETE FROM Archivos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al eliminar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}