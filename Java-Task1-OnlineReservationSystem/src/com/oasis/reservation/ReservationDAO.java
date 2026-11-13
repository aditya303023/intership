package com.oasis.reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Data Access Object for reservation records in SQLite.
 */
public class ReservationDAO {

    /**
     * Inserts a new reservation into the database using PreparedStatement.
     */
    public static boolean insert(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (" +
                "pnr, passenger_name, train_number, train_name, class_type, journey_date, source_station, destination_station" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservation.getPnr());
            ps.setString(2, reservation.getPassengerName());
            ps.setInt(3, reservation.getTrainNumber());
            ps.setString(4, reservation.getTrainName());
            ps.setString(5, reservation.getClassType());
            ps.setString(6, reservation.getJourneyDate());
            ps.setString(7, reservation.getSourceStation());
            ps.setString(8, reservation.getDestinationStation());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Finds a reservation record by its PNR.
     */
    public static Reservation findByPNR(String pnr) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE pnr = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pnr.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Reservation(
                            rs.getString("pnr"),
                            rs.getString("passenger_name"),
                            rs.getInt("train_number"),
                            rs.getString("train_name"),
                            rs.getString("class_type"),
                            rs.getString("journey_date"),
                            rs.getString("source_station"),
                            rs.getString("destination_station")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Deletes a reservation record by its PNR.
     */
    public static boolean deleteByPNR(String pnr) throws SQLException {
        String sql = "DELETE FROM reservations WHERE pnr = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pnr.trim().toUpperCase());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Generates a unique 6-digit PNR string (e.g., PNR748291).
     */
    public static String generatePNR() {
        Random rand = new Random();
        String pnr;
        boolean exists;
        do {
            int num = 100000 + rand.nextInt(900000);
            pnr = "PNR" + num;
            try {
                exists = (findByPNR(pnr) != null);
            } catch (SQLException e) {
                exists = false;
            }
        } while (exists);

        return pnr;
    }
}

