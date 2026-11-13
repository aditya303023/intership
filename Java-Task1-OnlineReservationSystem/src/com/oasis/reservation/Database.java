package com.oasis.reservation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles database connectivity and schema initialization using SQLite JDBC.
 */
public class Database {

    private static final String DB_URL = "jdbc:sqlite:reservation_system.db";

    /**
     * Obtains a connection to the SQLite database.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found in classpath.");
        }
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Initializes tables and default user accounts if they do not exist.
     */
    public static void initDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "username TEXT PRIMARY KEY, " +
                "password_hash TEXT NOT NULL" +
                ");";

        String createReservationsTable = "CREATE TABLE IF NOT EXISTS reservations (" +
                "pnr TEXT PRIMARY KEY, " +
                "passenger_name TEXT NOT NULL, " +
                "train_number INTEGER NOT NULL, " +
                "train_name TEXT NOT NULL, " +
                "class_type TEXT NOT NULL, " +
                "journey_date TEXT NOT NULL, " +
                "source_station TEXT NOT NULL, " +
                "destination_station TEXT NOT NULL" +
                ");";

        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute(createUsersTable);
            stmt.execute(createReservationsTable);

            // Seed default users (admin / admin123, user / user123)
            insertUserIfNotExists(con, "admin", "admin123");
            insertUserIfNotExists(con, "user", "user123");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Seeds initial user with hashed password.
     */
    private static void insertUserIfNotExists(Connection con, String username, String plainPassword) {
        String checkSql = "SELECT username FROM users WHERE username = ?";
        String insertSql = "INSERT OR IGNORE INTO users (username, password_hash) VALUES (?, ?)";

        try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
            insertPs.setString(1, username);
            insertPs.setString(2, hashPassword(plainPassword));
            insertPs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes SHA-256 hash of a password string.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}

