package com.crudapp.dao;

import com.crudapp.model.Dato;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class MySQLDatoDAO implements DatoDAO {
    private Connection conexion;

    public MySQLDatoDAO() {
        conectarBaseDatos();
    }

    private void conectarBaseDatos() {
// el try  catch sirve para hacer la coneccion a la base de datos
        try {
            String url = "jdbc:mysql://localhost:3306/crud_db";
            String usuario = "root";
            String contraseña = "toor"; // Cambia esto por tu contraseña de MySQL
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            crearTablaSiNoExiste();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    private void crearTablaSiNoExiste() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS datos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "texto VARCHAR(255), " +
                    "fecha DATE, " +
                    "categoria VARCHAR(100))";
            Statement stmt = conexion.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear tabla: " + e.getMessage());
        }
    }

    @Override
    public boolean agregarDato(Dato dato, Component parent) {
        try {
            String sql = "INSERT INTO datos (texto, fecha, categoria) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, dato.getTexto());
            pstmt.setDate(2, Date.valueOf(dato.getFecha()));
            pstmt.setString(3, dato.getCategoria());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al agregar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarDato(Dato dato, Component parent) {
        try {
            String sql = "UPDATE datos SET texto = ?, fecha = ?, categoria = ? WHERE id = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, dato.getTexto());
            pstmt.setDate(2, Date.valueOf(dato.getFecha()));
            pstmt.setString(3, dato.getCategoria());
            pstmt.setInt(4, dato.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarDato(int id, Component parent) {
        try {
            String sql = "DELETE FROM datos WHERE id = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Dato> cargarDatos(Component parent) {
        ArrayList<Dato> datos = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM datos");
            while (rs.next()) {
                Dato dato = new Dato(rs.getString("texto"), rs.getDate("fecha").toString(), rs.getString("categoria"));
                dato.setId(rs.getInt("id"));
                datos.add(dato);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al cargar desde DB: " + e.getMessage());
        }
        return datos;
    }

    @Override
    public boolean importarDatos(ArrayList<Dato> datos, Component parent) {
        try {
            String sql = "INSERT INTO datos (texto, fecha, categoria) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            for (Dato dato : datos) {
                pstmt.setString(1, dato.getTexto());
                pstmt.setDate(2, Date.valueOf(dato.getFecha()));
                pstmt.setString(3, dato.getCategoria());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al importar: " + e.getMessage());
            return false;
        }
    }


}