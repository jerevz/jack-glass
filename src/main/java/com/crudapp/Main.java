package com.crudapp;

import com.crudapp.gui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame ventana = new LoginFrame();
            ventana.setVisible(true);
        });
    }
}