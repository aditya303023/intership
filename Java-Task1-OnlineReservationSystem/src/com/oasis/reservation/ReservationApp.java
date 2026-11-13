package com.oasis.reservation;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application launcher for OASIS INFOBYTE Task 1 - Online Reservation System.
 */
public class ReservationApp {

    public static void main(String[] args) {
        // Set cross-platform look and feel or system look and feel for a modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default Swing Look & Feel
        }

        // Initialize SQLite database schema and seed test accounts
        Database.initDatabase();

        // Launch the Login Frame on the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

