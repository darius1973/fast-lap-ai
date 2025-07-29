package com.example.f1prediction.schedule;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Map;

@Component
public class RaceSchedule {
    private final NavigableMap<LocalDate, String> races = new TreeMap<>();

    public RaceSchedule() {
        // 2025 F1 calendar
        races.put(LocalDate.of(2025, 3, 16), "Australian Grand Prix");
        races.put(LocalDate.of(2025, 3, 23), "Chinese Grand Prix");
        races.put(LocalDate.of(2025, 4, 6), "Japanese Grand Prix");
        races.put(LocalDate.of(2025, 4, 13), "Bahrain Grand Prix");
        races.put(LocalDate.of(2025, 4, 20), "Saudi Arabian Grand Prix");
        races.put(LocalDate.of(2025, 5, 4), "Miami Grand Prix");
        races.put(LocalDate.of(2025, 5, 18), "Emilia‑Romagna Grand Prix");
        races.put(LocalDate.of(2025, 5, 25), "Monaco Grand Prix");
        races.put(LocalDate.of(2025, 6, 1), "Spanish Grand Prix");
        races.put(LocalDate.of(2025, 6, 15), "Canadian Grand Prix");
        races.put(LocalDate.of(2025, 6, 29), "Austrian Grand Prix");
        races.put(LocalDate.of(2025, 7, 6), "British Grand Prix");
        races.put(LocalDate.of(2025, 7, 27), "Belgian Grand Prix");
        races.put(LocalDate.of(2025, 8, 3), "Hungarian Grand Prix");
        races.put(LocalDate.of(2025, 8, 31), "Dutch Grand Prix");
        races.put(LocalDate.of(2025, 9, 7), "Italian Grand Prix");
        races.put(LocalDate.of(2025, 9, 21), "Azerbaijan Grand Prix");
        races.put(LocalDate.of(2025, 10, 5), "Singapore Grand Prix");
        races.put(LocalDate.of(2025, 10, 19), "United States Grand Prix");
        races.put(LocalDate.of(2025, 10, 26), "Mexico City Grand Prix");
        races.put(LocalDate.of(2025, 11, 9), "São Paulo Grand Prix");
        races.put(LocalDate.of(2025, 11, 22), "Las Vegas Grand Prix");
        races.put(LocalDate.of(2025, 11, 30), "Qatar Grand Prix");
        races.put(LocalDate.of(2025, 12, 7), "Abu Dhabi Grand Prix");
    }

    /**
     * Returns the next upcoming race from today's date, if any.
     * @return Optional containing the next race entry, or empty if none remain.
     */
    public String getNextRace() {
        LocalDate today = LocalDate.now();
        Map.Entry<LocalDate, String> next = races.higherEntry(today);
        return next != null ? next.getValue() : "No upcoming races.";
    }

}

