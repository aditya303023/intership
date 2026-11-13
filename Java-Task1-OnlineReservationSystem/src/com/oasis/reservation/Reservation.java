package com.oasis.reservation;

/**
 * Model class representing a train ticket reservation.
 */
public class Reservation {
    private String pnr;
    private String passengerName;
    private int trainNumber;
    private String trainName;
    private String classType;
    private String journeyDate;
    private String sourceStation;
    private String destinationStation;

    public Reservation() {
    }

    public Reservation(String pnr, String passengerName, int trainNumber, String trainName,
                       String classType, String journeyDate, String sourceStation, String destinationStation) {
        this.pnr = pnr;
        this.passengerName = passengerName;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.classType = classType;
        this.journeyDate = journeyDate;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public void setSourceStation(String sourceStation) {
        this.sourceStation = sourceStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    @Override
    public String toString() {
        return "PNR: " + pnr + "\n" +
               "Passenger: " + passengerName + "\n" +
               "Train: " + trainNumber + " - " + trainName + "\n" +
               "Class: " + classType + "\n" +
               "Date: " + journeyDate + "\n" +
               "Route: " + sourceStation + " -> " + destinationStation;
    }
}

