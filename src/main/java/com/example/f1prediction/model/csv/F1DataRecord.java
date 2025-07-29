package com.example.f1prediction.model.csv;

import lombok.Builder;

@Builder
public record F1DataRecord(
        int id,
        int year,
        String raceName,
        double lapLengthKm,
        int numberOfCorners,
        String downforceLevel,
        int brakingZonesCount,
        String overtakingDifficulty,
        double pitLaneTimeLoss,
        String trackSurfaceGrip,
        String driver,
        String team,
        double avgLapTimeRaceKm,
        double bestLapTimeRaceKm,
        int topSpeedKph,
        int points,
        int win,
        double avgAirTempC,
        double avgWindSpeedKmh,
        double avgHumidityPercent,
        double totalRainfallMm,
        String trackCondition
) {};
