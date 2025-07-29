package com.example.f1prediction.model.predict;


import com.example.f1prediction.model.training.PredictionFeatures;

import java.util.List;
import java.util.stream.Collectors;

public class DriverStats {




    public static PredictionFeatures getDriverStats(List<PredictionFeatures> records, String driver, String race) {
        List<PredictionFeatures> driverRecords = records.stream()
                .filter(r -> r.driver().equals(driver))
                .collect(Collectors.toList());

        RaceInfo raceInfo = RaceInfoDetails.getRaceData(race);
        if (driverRecords.isEmpty()) return null; // or throw

        int nrRaces = driverRecords.size();
        double totalPoints = driverRecords.stream().mapToDouble(r ->  r.driverAvgCumulatedPoints()).sum();
        double avgPoints = totalPoints / nrRaces;
        double avgTopSpeed = driverRecords.stream().mapToDouble(r -> r.driverTopSpeedKph()).average().orElse(0);
        double avgLapTime = driverRecords.stream().mapToDouble(r -> r.driverAvgLapTimeSpeed()).average().orElse(0);
        double avgBestLapTime = driverRecords.stream().mapToDouble(r -> r.driverBestLapTimeSpeed()).average().orElse(0);
        double raceDetail = toRaceDetail(raceInfo);
        // prediction element
        System.out.println("race detail:" + raceDetail);
        return PredictionFeatures
                .builder()
                .raceName(raceInfo.raceName())
                .raceDetail(raceDetail)
                .driver(driver)
                .driverBestLapTimeSpeed(avgBestLapTime)
                .driverAvgLapTimeSpeed(avgLapTime)
                .driverTopSpeedKph(avgTopSpeed)
                .driverAvgCumulatedPoints(avgPoints)
                //weather info
                //.weatherAvgAirTempC(22.0)
                //.weatherAvgWindSpeedKmh(2.0)
                //.weatherAvgHumidityPercent(54)
                //.weatherTotalRainfallMm(0.0)
                .trackCondition(0.00001)
                .build();

    }

    private static double toRaceDetail(RaceInfo raceInfo) {
        return  0.2  * raceInfo.lapLengthKm() +
                0.15 * raceInfo.numberOfCorners() +
                0.25 * raceInfo.downforceLevel() +
                0.1  * raceInfo.brakingZonesCount()  +
                0.1  * raceInfo.overtakingDifficulty() +
                0.1  * raceInfo.pitLaneTimeLossSeconds();
    }
}
