package com.example.f1prediction.importer;


import com.example.f1prediction.model.csv.F1DataRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class F1DataImporter {

    public List<F1DataRecord> importFromCsv(Path csvFilePath) throws IOException {
        List<F1DataRecord> records = new ArrayList<>();

        try (Reader reader = new FileReader(csvFilePath.toFile());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withTrim()
                     .withDelimiter(';'))) {

            for (CSVRecord csv : csvParser) {
                F1DataRecord record = F1DataRecord.builder()
                        .id(Integer.parseInt(csv.get("id")))
                        .year(Integer.parseInt(csv.get("year")))
                        .raceName(csv.get("race_name"))
                        .lapLengthKm(parseDouble(csv.get("lap_length_km")))
                        .numberOfCorners(Integer.parseInt(csv.get("NumberOfCorners")))
                        .downforceLevel(csv.get("DownforceLevel"))
                        .brakingZonesCount(Integer.parseInt(csv.get("BrakingZonesCount")))
                        .overtakingDifficulty(csv.get("OvertakingDifficulty"))
                        .pitLaneTimeLoss(Double.parseDouble(csv.get("PitLaneTimeLoss")))
                        .trackSurfaceGrip(csv.get("TrackSurfaceGrip"))
                        .driver(csv.get("driver"))
                        .team(csv.get("team"))
                        .avgLapTimeRaceKm(parseDouble(csv.get("avg-lap-time-race-km")))
                        .bestLapTimeRaceKm(parseDouble(csv.get("best-lap-time-race-km")))
                        .topSpeedKph(Integer.parseInt(csv.get("top_speed_kph")))
                        .points(Integer.parseInt(csv.get("points")))
                        .win(Integer.parseInt(csv.get("win")))
                        .avgAirTempC(Double.parseDouble(csv.get("AvgAirTemp_C")))
                        .avgWindSpeedKmh(Double.parseDouble(csv.get("AvgWindSpeed_kmh")))
                        .avgHumidityPercent(Double.parseDouble(csv.get("AvgHumidity_%")))
                        .totalRainfallMm(Double.parseDouble(csv.get("TotalRainfall_mm")))
                        .trackCondition(csv.get("TrackCondition"))
                        .build();

                records.add(record);
            }
        }

        return records;
    }

    private static double parseDouble(String value) {
        return Double.parseDouble(value.replace(",", "."));
    }
}

