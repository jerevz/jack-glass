package com.crudapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Crud_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Cambia por tu usuario
    private static final String PASSWORD = "toor"; // Cambia por tu contraseña

    public static Connection getConnection() {
        try {
            System.out.println("Intentando conectar a: " + URL);
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a MySQL!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return null;
        }
    }
}