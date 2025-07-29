package com.example.f1prediction.model.training;

import lombok.Builder;

@Builder
public record PredictionFeatures(
        String raceName,
        String driver,
        double raceDetail,
        double driverAvgLapTimeSpeed,
        double driverBestLapTimeSpeed,
        double driverTopSpeedKph,
        double driverAvgCumulatedPoints,
        double weatherAvgAirTempC,
        double weatherAvgWindSpeedKmh,
        double weatherAvgHumidityPercent,
        double weatherTotalRainfallMm,
        double trackCondition,//1:dry, 2:wet
        boolean win
) {
}
