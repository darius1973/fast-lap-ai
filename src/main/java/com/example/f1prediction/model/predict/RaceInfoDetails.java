package com.example.f1prediction.model.predict;


import java.util.List;

public class RaceInfoDetails {

    public static List<RaceInfo> raceDetails() {
        return List.of(
                new RaceInfo("Bahrain Grand Prix", 5.412, 15, 4, 7, 3, 22.0, "MEDIUM"),
                new RaceInfo("Saudi Arabian Grand Prix", 6.174, 27, 5, 6, 3, 21.5, "LOW"),
                new RaceInfo("Australian Grand Prix", 5.278, 14, 4, 6, 3, 23.0, "LOW"),
                new RaceInfo("Japanese Grand Prix", 5.807, 18, 1, 6, 2, 22.5, "HIGH"),
                new RaceInfo("Chinese Grand Prix", 5.451, 16, 4, 7, 3, 20.0, "MEDIUM"),
                new RaceInfo("Miami Grand Prix", 5.412, 19, 5, 7, 3, 22.3, "LOW"),
                new RaceInfo("Emilia Romagna Grand Prix", 4.909, 19, 1, 5, 2, 25.0, "HIGH"),
                new RaceInfo("Monaco Grand Prix", 3.337, 19, 1, 5, 1, 20.0, "LOW"),
                new RaceInfo("Canadian Grand Prix", 4.361, 14, 5, 7, 3, 20.5, "LOW"),
                new RaceInfo("Spanish Grand Prix", 4.657, 14, 1, 6, 3, 22.5, "HIGH"),
                new RaceInfo("Austrian Grand Prix", 4.318, 10, 5, 6, 3, 20.0, "HIGH"),
                new RaceInfo("British Grand Prix", 5.891, 18, 4, 7, 3, 21.0, "HIGH"),
                new RaceInfo("Hungarian Grand Prix", 4.381, 14, 1, 6, 2, 21.5, "LOW"),
                new RaceInfo("Belgian Grand Prix", 7.004, 19, 5, 6, 3, 21.0, "HIGH"),
                new RaceInfo("Dutch Grand Prix", 4.259, 14, 1, 5, 2, 18.5, "MEDIUM"),
                new RaceInfo("Italian Grand Prix", 5.793, 11, 5, 5, 3, 21.0, "HIGH"),
                new RaceInfo("Azerbaijan Grand Prix", 6.003, 20, 5, 6, 3, 19.5, "LOW"),
                new RaceInfo("Singapore Grand Prix", 4.940, 19, 1, 7, 2, 23.5, "LOW"),
                new RaceInfo("United States Grand Prix", 5.513, 20, 4, 6, 3, 20.0, "MEDIUM"),
                new RaceInfo("Mexico City Grand Prix", 4.304, 17, 1, 6, 3, 22.0, "LOW"),
                new RaceInfo("Sao Paulo Grand Prix", 4.309, 15, 4, 6, 3, 21.5, "HIGH"),
                new RaceInfo("Las Vegas Grand Prix", 6.201, 17, 5, 6, 3, 20.0, "LOW"),
                new RaceInfo("Qatar Grand Prix", 5.418, 16, 1, 6, 3, 21.0, "HIGH"),
                new RaceInfo("Abu Dhabi Grand Prix", 5.281, 16, 4, 6, 3, 22.0, "MEDIUM")
        );
    }

    public static RaceInfo getRaceData(String raceName) {
        return raceDetails().stream().filter(r -> r.raceName().equalsIgnoreCase(raceName)).findAny()
                .orElseThrow(() -> new RuntimeException( raceName + "race not found"));
    }
}
