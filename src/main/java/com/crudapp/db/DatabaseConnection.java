package com.crudapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Configuración de la conexión
    private static final String URL = "jdbc:mysql://localhost:3307/Crud_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Cloud123!"; // Contraseña de Cloud SQL

    // Método para obtener la conexión
    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver de MySQL (asegúrate de tener la dependencia en tu proyecto)
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Intentando conectar a: " + URL);
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a Cloud SQL!");
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL. Asegúrate de incluir la dependencia.");
            throw new SQLException("Driver no encontrado", e);
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
    }

    // Método main para probar la conexión
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}