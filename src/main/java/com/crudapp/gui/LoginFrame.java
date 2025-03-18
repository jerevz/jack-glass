package com.crudapp.gui;

import com.crudapp.dao.ArchivoDAOImpl;
import com.crudapp.dao.DatoDAOImpl;
import com.crudapp.service.DatoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Inicio de Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        txtUsuario = new JTextField(15);
        add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        add(txtPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        btnLogin = new JButton("Iniciar Sesión");
        add(btnLogin, gbc);

        btnLogin.addActionListener(e -> validarLogin());
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Ejemplo de validación simple (ajusta según tus necesidades)
        if ("admin".equals(usuario) && "1234".equals(password)) {
            dispose(); // Cierra la ventana de login
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}