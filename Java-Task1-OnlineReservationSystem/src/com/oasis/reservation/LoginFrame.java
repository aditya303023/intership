package com.oasis.reservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Login window for the Online Reservation System.
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        setTitle("OASIS Online Reservation System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 380);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(248, 250, 252));

        // Header Title
        JLabel titleLabel = new JLabel("Online Reservation System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));

        JLabel subtitleLabel = new JLabel("Sign in to your account", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(100, 116, 139));

        JPanel headerPanel = new JPanel(new BorderLayout(0, 4));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Form Fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(userLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        formPanel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(passLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        formPanel.add(passwordField, gbc);

        // Hint label for default credentials
        JLabel hintLabel = new JLabel("Default: admin / admin123", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setForeground(new Color(148, 163, 184));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(hintLabel, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setBackground(new Color(37, 99, 235));
        loginButton.setForeground(Color.BLACK);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());

        registerButton = new JButton("Register New User");
        registerButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> handleRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Allow pressing Enter in password field to login
        passwordField.addActionListener(e -> handleLogin());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this,
                    "Welcome, " + username + "! Login successful.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            new ReservationFrame(username).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Access Denied",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    /**
     * Checks entered credentials against stored user records using PreparedStatement.
     */
    public static boolean authenticateUser(String username, String password) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    String inputHash = Database.hashPassword(password);
                    return storedHash.equals(inputHash);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void handleRegister() {
        String newUsername = JOptionPane.showInputDialog(this, "Enter desired username:", "Register User", JOptionPane.QUESTION_MESSAGE);
        if (newUsername == null || newUsername.trim().isEmpty()) {
            return;
        }
        newUsername = newUsername.trim();

        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(this, pf, "Enter desired password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) {
            return;
        }
        String newPassword = new String(pf.getPassword());
        if (newPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newUsername);
            ps.setString(2, Database.hashPassword(newPassword));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "User registered successfully! You can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText(newUsername);
            passwordField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Username already exists or database error: " + ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
