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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Main reservation form for booking train tickets.
 */
public class ReservationFrame extends JFrame {

    private String loggedInUser;

    private JTextField passengerNameField;
    private JTextField trainNumberField;
    private JTextField trainNameField;
    private JComboBox<String> classComboBox;
    private JTextField journeyDateField;
    private JComboBox<String> sourceComboBox;
    private JComboBox<String> destinationComboBox;

    private JButton bookButton;
    private JButton openCancellationButton;
    private JButton logoutButton;

    private static final String[] STATIONS = {
        "New Delhi (NDLS)",
        "Mumbai Central (MMCT)",
        "Howrah Junction (HWH)",
        "Chennai Central (MAS)",
        "Bengaluru City (SBC)",
        "Bhopal Junction (BPL)",
        "Varanasi Junction (BSB)",
        "Ahmedabad Junction (ADI)",
        "Secunderabad (SC)",
        "Patna Junction (PNBE)"
    };

    private static final String[] CLASSES = {
        "1A - AC First Class",
        "2A - AC 2-Tier",
        "3A - AC 3-Tier",
        "SL - Sleeper Class",
        "EC - Executive Chair Car",
        "CC - AC Chair Car",
        "2S - Second Sitting"
    };

    public ReservationFrame(String username) {
        this.loggedInUser = username;

        setTitle("OASIS Reservation System - Book Ticket (Logged in as: " + username + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 620);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(248, 250, 252));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Train Ticket Reservation Form");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel userLabel = new JLabel("User: " + loggedInUser);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(new Color(37, 99, 235));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Passenger Name
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(createBoldLabel("Passenger Name:"), gbc);

        passengerNameField = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(passengerNameField, gbc);

        // Train Number + lookup
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(createBoldLabel("Train Number:"), gbc);

        JPanel trainNumPanel = new JPanel(new BorderLayout(8, 0));
        trainNumPanel.setOpaque(false);
        trainNumberField = new JTextField();
        JButton viewTrainsButton = new JButton("Sample Trains");
        viewTrainsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewTrainsButton.addActionListener(e -> showSampleTrainsDialog());
        trainNumPanel.add(trainNumberField, BorderLayout.CENTER);
        trainNumPanel.add(viewTrainsButton, BorderLayout.EAST);

        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(trainNumPanel, gbc);

        // Train Name (Auto-populated)
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(createBoldLabel("Train Name (Auto):"), gbc);

        trainNameField = new JTextField();
        trainNameField.setEditable(false);
        trainNameField.setBackground(new Color(241, 245, 249));
        trainNameField.setForeground(new Color(51, 65, 85));
        trainNameField.setText("Enter train number above to auto-lookup");
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(trainNameField, gbc);

        // Auto-lookup train name on input
        trainNumberField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateTrainName(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateTrainName(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateTrainName(); }
        });

        // Class Type
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(createBoldLabel("Class Type:"), gbc);

        classComboBox = new JComboBox<>(CLASSES);
        classComboBox.setSelectedIndex(2); // 3A default
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(classComboBox, gbc);

        // Date of Journey
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(createBoldLabel("Date (YYYY-MM-DD):"), gbc);

        journeyDateField = new JTextField();
        journeyDateField.setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(journeyDateField, gbc);

        // Source Station
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(createBoldLabel("Source Station:"), gbc);

        sourceComboBox = new JComboBox<>(STATIONS);
        sourceComboBox.setSelectedIndex(0);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(sourceComboBox, gbc);

        // Destination Station
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(createBoldLabel("Destination Station:"), gbc);

        destinationComboBox = new JComboBox<>(STATIONS);
        destinationComboBox.setSelectedIndex(1);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(destinationComboBox, gbc);

        // Bottom Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        bookButton = new JButton("Book Ticket");
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookButton.setBackground(new Color(22, 163, 74));
        bookButton.setForeground(Color.BLACK);
        bookButton.setPreferredSize(new Dimension(140, 38));
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookButton.addActionListener(e -> bookTicket());

        openCancellationButton = new JButton("Cancel Ticket Form");
        openCancellationButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        openCancellationButton.setPreferredSize(new Dimension(160, 38));
        openCancellationButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openCancellationButton.addActionListener(e -> {
            new CancellationFrame(this).setVisible(true);
            setVisible(false);
        });

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logoutButton.setPreferredSize(new Dimension(100, 38));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        buttonPanel.add(bookButton);
        buttonPanel.add(openCancellationButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(51, 65, 85));
        return label;
    }

    private void updateTrainName() {
        String input = trainNumberField.getText().trim();
        if (input.isEmpty()) {
            trainNameField.setText("Enter train number above to auto-lookup");
            return;
        }

        try {
            int trainNum = Integer.parseInt(input);
            String name = TrainDAO.findTrainName(trainNum);
            if (name != null) {
                trainNameField.setText(name);
                trainNameField.setForeground(new Color(22, 101, 52));
            } else {
                trainNameField.setText("Custom Express (" + trainNum + ")");
                trainNameField.setForeground(new Color(71, 85, 105));
            }
        } catch (NumberFormatException e) {
            trainNameField.setText("Invalid train number (numeric only)");
            trainNameField.setForeground(new Color(220, 38, 38));
        }
    }

    private void showSampleTrainsDialog() {
        StringBuilder sb = new StringBuilder("Known Trains for Testing:\n\n");
        for (Map.Entry<Integer, String> entry : TrainDAO.getAllTrains().entrySet()) {
            sb.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }
        sb.append("\nEntering any of these numbers will auto-populate the train name.");
        JOptionPane.showMessageDialog(this, sb.toString(), "Sample Train List", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Validates required fields, date format and numeric train number.
     */
    public boolean validateForm() {
        String passenger = passengerNameField.getText().trim();
        if (passenger.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Passenger name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            passengerNameField.requestFocus();
            return false;
        }

        String trainNumText = trainNumberField.getText().trim();
        if (trainNumText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Train number is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            trainNumberField.requestFocus();
            return false;
        }

        try {
            int trainNum = Integer.parseInt(trainNumText);
            if (trainNum <= 0) {
                JOptionPane.showMessageDialog(this, "Train number must be a positive integer.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Train number must contain digits only.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            trainNumberField.requestFocus();
            return false;
        }

        String dateText = journeyDateField.getText().trim();
        try {
            LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ISO_LOCAL_DATE);
            if (date.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Journey date cannot be in the past.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                journeyDateField.requestFocus();
                return false;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in YYYY-MM-DD format.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            journeyDateField.requestFocus();
            return false;
        }

        String source = (String) sourceComboBox.getSelectedItem();
        String destination = (String) destinationComboBox.getSelectedItem();
        if (source != null && source.equals(destination)) {
            JOptionPane.showMessageDialog(this, "Source and destination cannot be the same station.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Validates fields, generates a unique PNR, saves to database, and shows confirmation.
     */
    public void bookTicket() {
        if (!validateForm()) {
            return;
        }

        String passenger = passengerNameField.getText().trim();
        int trainNumber = Integer.parseInt(trainNumberField.getText().trim());
        String trainName = trainNameField.getText().trim();
        String classType = (String) classComboBox.getSelectedItem();
        String date = journeyDateField.getText().trim();
        String source = (String) sourceComboBox.getSelectedItem();
        String destination = (String) destinationComboBox.getSelectedItem();

        String pnr = ReservationDAO.generatePNR();

        Reservation reservation = new Reservation(
                pnr, passenger, trainNumber, trainName, classType, date, source, destination
        );

        try {
            boolean success = ReservationDAO.insert(reservation);
            if (success) {
                String message = "Booking Successful!\n\n" +
                        "PNR Number: " + pnr + "\n" +
                        "Passenger: " + passenger + "\n" +
                        "Train: " + trainNumber + " - " + trainName + "\n" +
                        "Class: " + classType + "\n" +
                        "Journey Date: " + date + "\n" +
                        "From: " + source + "\n" +
                        "To: " + destination + "\n\n" +
                        "Please keep your PNR for future reference or cancellation.";

                JOptionPane.showMessageDialog(this, message, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);

                // Clear input fields
                passengerNameField.setText("");
                trainNumberField.setText("");
                trainNameField.setText("Enter train number above to auto-lookup");
                passengerNameField.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save booking. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

