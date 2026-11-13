package com.oasis.reservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Cancellation window for searching reservations by PNR and cancelling tickets.
 */
public class CancellationFrame extends JFrame {

    private ReservationFrame parentFrame;

    private JTextField pnrField;
    private JButton fetchButton;
    private JTextArea detailsArea;
    private JButton cancelButton;
    private JButton backButton;

    private Reservation currentReservation = null;

    public CancellationFrame(ReservationFrame parent) {
        this.parentFrame = parent;

        setTitle("OASIS Reservation System - Ticket Cancellation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(580, 520);
        setLocationRelativeTo(parent);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(248, 250, 252));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Cancel Train Reservation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitle = new JLabel("Fetch ticket details using your PNR to proceed with cancellation");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(100, 116, 139));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel pnrLabel = new JLabel("Enter PNR Number:");
        pnrLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        searchPanel.add(pnrLabel, gbc);

        pnrField = new JTextField(15);
        pnrField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.weightx = 0.5;
        searchPanel.add(pnrField, gbc);

        fetchButton = new JButton("Fetch Booking");
        fetchButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fetchButton.setBackground(new Color(37, 99, 235));
        fetchButton.setForeground(Color.BLACK);
        fetchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fetchButton.addActionListener(e -> fetchBookingByPNR());
        gbc.gridx = 2; gbc.weightx = 0.2;
        searchPanel.add(fetchButton, gbc);

        // Allow pressing Enter in PNR field
        pnrField.addActionListener(e -> fetchBookingByPNR());

        // Center: Details display area
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setOpaque(false);

        JLabel detailsLabel = new JLabel("Booking Details:");
        detailsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        detailsLabel.setForeground(new Color(51, 65, 85));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        detailsArea.setBackground(new Color(241, 245, 249));
        detailsArea.setText("Please enter a PNR number and click 'Fetch Booking' to view ticket details.");
        detailsArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(500, 170));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));

        centerPanel.add(detailsLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Content aggregation panel
        JPanel bodyPanel = new JPanel(new BorderLayout(10, 10));
        bodyPanel.setOpaque(false);
        bodyPanel.add(searchPanel, BorderLayout.NORTH);
        bodyPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        cancelButton = new JButton("Cancel Reservation");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(220, 38, 38));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setPreferredSize(new Dimension(170, 38));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setEnabled(false); // Disabled until fetched
        cancelButton.addActionListener(e -> cancelBooking());

        backButton = new JButton("Back to Reservation");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backButton.setPreferredSize(new Dimension(160, 38));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            dispose();
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Handle window closing back to parent
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });
    }

    /**
     * Searches the database by PNR and displays the matching reservation.
     */
    public void fetchBookingByPNR() {
        String pnr = pnrField.getText().trim();
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a PNR number.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Reservation res = ReservationDAO.findByPNR(pnr);
            if (res != null) {
                this.currentReservation = res;
                StringBuilder sb = new StringBuilder();
                sb.append("==================================================\n");
                sb.append("               RESERVATION DETAILS                \n");
                sb.append("==================================================\n");
                sb.append(String.format("%-18s: %s\n", "PNR Number", res.getPnr()));
                sb.append(String.format("%-18s: %s\n", "Passenger Name", res.getPassengerName()));
                sb.append(String.format("%-18s: %d - %s\n", "Train", res.getTrainNumber(), res.getTrainName()));
                sb.append(String.format("%-18s: %s\n", "Class", res.getClassType()));
                sb.append(String.format("%-18s: %s\n", "Journey Date", res.getJourneyDate()));
                sb.append(String.format("%-18s: %s\n", "Boarding Station", res.getSourceStation()));
                sb.append(String.format("%-18s: %s\n", "Destination", res.getDestinationStation()));
                sb.append("==================================================\n");
                sb.append("Status: ACTIVE / CONFIRMED\n");

                detailsArea.setText(sb.toString());
                detailsArea.setBackground(new Color(240, 253, 244)); // light green tint
                cancelButton.setEnabled(true);
            } else {
                this.currentReservation = null;
                detailsArea.setText("No reservation found for PNR: " + pnr);
                detailsArea.setBackground(new Color(254, 242, 242)); // light red tint
                cancelButton.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "PNR not found in the database: " + pnr,
                        "Record Not Found",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database query error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Confirms cancellation with "Are you sure?" and removes booking.
     */
    public void cancelBooking() {
        if (currentReservation == null) {
            JOptionPane.showMessageDialog(this, "No booking is currently selected.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel the ticket for:\n\n" +
                "PNR: " + currentReservation.getPnr() + "\n" +
                "Passenger: " + currentReservation.getPassengerName() + "\n" +
                "Train: " + currentReservation.getTrainNumber() + " (" + currentReservation.getTrainName() + ")?\n\n" +
                "This action cannot be undone.",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = ReservationDAO.deleteByPNR(currentReservation.getPnr());
                if (deleted) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Ticket with PNR " + currentReservation.getPnr() + " has been successfully cancelled.",
                            "Cancellation Confirmed",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    detailsArea.setText("Ticket " + currentReservation.getPnr() + " has been cancelled.");
                    detailsArea.setBackground(new Color(241, 245, 249));
                    currentReservation = null;
                    cancelButton.setEnabled(false);
                    pnrField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to cancel ticket. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database deletion error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

