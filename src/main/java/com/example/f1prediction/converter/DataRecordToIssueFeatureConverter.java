package com.example.f1prediction.converter;

import com.example.f1prediction.model.csv.F1DataRecord;
import com.example.f1prediction.model.training.PredictionFeatures;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataRecordToIssueFeatureConverter {

    public List<PredictionFeatures> convert(List<F1DataRecord> dataRecords) {
        List<PredictionFeatures> features = new ArrayList<>();
        // add cumulative points before each race
        List<RecordWithCumulative> records = addCumulativePoints(dataRecords);
        System.out.println("total records: " + records.size());
        List<RecordWithCumulative> filteredRecords = records.stream().filter(r ->
                List.of("VER", "NOR", "PIA","LEC","HAM","RUS","ANT","ALB","HUL","OCO").contains(
                r.getOriginal().driver())).toList();
        System.out.println("filtered records: " + filteredRecords.size());
        filteredRecords.forEach( r-> {
                    int duplicatePred = 1;
                   if (r.getOriginal().win() == 1) {
                       duplicatePred = 17;
                    }
                    for (int i = 0; i < duplicatePred; i++) {
                        features.add(PredictionFeatures
                                .builder()
                                .raceName(r.getOriginal().raceName())
                                .driver(r.getOriginal().driver())
                                .raceDetail(toRaceDetail(r.getOriginal()))
                                .driverAvgCumulatedPoints(r.getCumulativePointsBefore())
                                .driverBestLapTimeSpeed(1/r.getOriginal().bestLapTimeRaceKm())
                                .driverAvgLapTimeSpeed(1/r.getOriginal().avgLapTimeRaceKm())
                                .driverTopSpeedKph(r.getOriginal().topSpeedKph())
                                .weatherAvgAirTempC(r.getOriginal().avgAirTempC())
                                .weatherAvgHumidityPercent(r.getOriginal().avgHumidityPercent())
                                .weatherAvgWindSpeedKmh(r.getOriginal().avgWindSpeedKmh())
                                .trackCondition(r.getOriginal().trackCondition().equalsIgnoreCase("wet") ? 0.00001 : 0.00002)
                                .win(r.getOriginal().win() > 0)
                                .build());
                    }
                }

        );
        for (PredictionFeatures f : features) {
            System.out.println(f);
        }
        return features;
    }

    private double toRaceDetail(F1DataRecord original) {
        return  0.2  * original.lapLengthKm() +
                0.15 * original.numberOfCorners() +
                0.25 * convertDownForceLevel(original.downforceLevel()) +
                0.1  * original.brakingZonesCount()  +
                0.1  * convertOvertakingDif(original.overtakingDifficulty()) +
                0.1  * original.pitLaneTimeLoss();
    }


    private double convertDownForceLevel(String s) {
        return switch (s.toLowerCase()) {
            case "high" -> 0.0001;
            case "maximum" -> 0.0002;
            case "medium-high" -> 0.0003;
            case "medium" -> 0.0004;
            case "low" -> 0.0005;
            default -> throw new RuntimeException("force level unknown: " + s);
        };
    }

    private double convertOvertakingDif(String s) {
        //1:Very Hard,2:Hard,3:Medium
        return switch (s.toLowerCase()) {
            case "very hard" -> 0.001;
            case "hard" -> 0.002;
            case "medium" -> 0.003;
            default -> throw new RuntimeException("force level unknown: " + s);
        };
    }

    private  List<RecordWithCumulative> addCumulativePoints(List<F1DataRecord> records) {
        // Sort by year and race id
        records.sort(Comparator.comparingInt((F1DataRecord r) -> r.id()));

        Map<String, Integer> driverPoints = new HashMap<>();
        List<RecordWithCumulative> result = new ArrayList<>();

        for (F1DataRecord record : records) {
            String driver = record.driver();
            int cumulativeBefore = driverPoints.getOrDefault(driver, 0);

            result.add(new RecordWithCumulative(record, cumulativeBefore));

            // Update the map after storing the current cumulative
            driverPoints.put(driver, cumulativeBefore + record.points());
        }

        return result;
    }
}
