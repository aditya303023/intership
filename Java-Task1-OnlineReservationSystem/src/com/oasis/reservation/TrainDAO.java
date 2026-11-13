package com.oasis.reservation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data Access Object for train information lookup.
 */
public class TrainDAO {

    private static final Map<Integer, String> TRAIN_DATABASE = new LinkedHashMap<>();

    static {
        TRAIN_DATABASE.put(12001, "New Delhi - Bhopal Shatabdi Express");
        TRAIN_DATABASE.put(12002, "Bhopal - New Delhi Shatabdi Express");
        TRAIN_DATABASE.put(12301, "Howrah Rajdhani Express");
        TRAIN_DATABASE.put(12302, "New Delhi - Howrah Rajdhani");
        TRAIN_DATABASE.put(12951, "Mumbai Central - New Delhi Tejas Rajdhani");
        TRAIN_DATABASE.put(12952, "New Delhi - Mumbai Central Tejas Rajdhani");
        TRAIN_DATABASE.put(22436, "Vande Bharat Express (NDLS - BSB)");
        TRAIN_DATABASE.put(22435, "Vande Bharat Express (BSB - NDLS)");
        TRAIN_DATABASE.put(12626, "Kerala Superfast Express");
        TRAIN_DATABASE.put(12625, "Kerala Superfast Express (Return)");
        TRAIN_DATABASE.put(12259, "Sealdah - Bikaner AC Duronto Express");
        TRAIN_DATABASE.put(12137, "Punjab Mail (CSMT - FZR)");
        TRAIN_DATABASE.put(12840, "Howrah - Chennai Central Mail");
        TRAIN_DATABASE.put(12622, "Tamil Nadu Express (NDLS - MAS)");
    }

    /**
     * Looks up a train name by its train number.
     *
     * @param trainNumber numeric train number
     * @return Train name if found, or null if unknown
     */
    public static String findTrainName(int trainNumber) {
        if (TRAIN_DATABASE.containsKey(trainNumber)) {
            return TRAIN_DATABASE.get(trainNumber);
        }
        return null;
    }

    /**
     * Returns an unmodifiable map of all registered sample trains.
     */
    public static Map<Integer, String> getAllTrains() {
        return Collections.unmodifiableMap(TRAIN_DATABASE);
    }
}

