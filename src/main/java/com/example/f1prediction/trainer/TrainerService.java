package com.example.f1prediction.trainer;

import com.example.f1prediction.converter.DataRecordToIssueFeatureConverter;
import com.example.f1prediction.importer.F1DataImporter;
import com.example.f1prediction.model.csv.F1DataRecord;
import com.example.f1prediction.model.training.PredictionFeatures;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smile.classification.LogisticRegression;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.vector.BooleanVector;
import smile.data.vector.DoubleVector;
import smile.data.vector.IntVector;
import smile.feature.Standardizer;

import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

@Service
public class TrainerService {

    @Autowired
    F1DataImporter f1DataImporter;

    @Autowired
    DataRecordToIssueFeatureConverter dataRecordToIssueFeatureConverter;

    private volatile LogisticRegression model;

    @Getter
    private List<PredictionFeatures> predictionFeatures;

    @Getter
    private Standardizer standardizer;

    public LogisticRegression getModel() {
        if (model == null) {
            throw new IllegalStateException("Model has not been trained yet");
        }
        return model;
    }

    @PostConstruct
    public void trainModelOnStartup() {
        trainModel();
    }

    @SneakyThrows
    private void trainModel()  {

        List<F1DataRecord> records = f1DataImporter.importFromCsv(Path.of("src/main/resources/f1_dataset_training_2025.csv"));
        predictionFeatures = dataRecordToIssueFeatureConverter.convert(records);

        DataFrame df = toDataFrame(predictionFeatures);
        String target = "win";

        // Step 2: Split into X (features) and y (target)
        DataFrame xdf = df.drop(target);
        BooleanVector winVector = df.booleanVector(target);
        int[] y = new int[winVector.size()];
        for (int i = 0; i < winVector.size(); i++) {
            y[i] = winVector.getBoolean(i) ? 1 : 0;
        }

        // Standardize features
        standardizer = Standardizer.fit(xdf);
        DataFrame xdfStandardized = standardizer.transform(xdf);

        // Add target column back
        xdfStandardized = xdfStandardized.merge(IntVector.of(target, y));

        // Use default LogisticRegression.fit
        Properties props = new Properties();
        props.setProperty("smile.logistic.max.iterations", "7000");
        props.setProperty("smile.logistic.lambda", "0.001");
        LogisticRegression model = LogisticRegression.fit(Formula.lhs("win"), xdfStandardized, props);

        
        //print weights
        if (model instanceof LogisticRegression.Binomial binomial) {
            double[] weights = binomial.coefficients();
            String[] featureNames = df.names();
            System.out.println("Intercept: " + weights[weights.length - 1]);
            for (int i = 0; i < weights.length - 1; i++) {
                System.out.printf("%-30s : %.5f%n", featureNames[i], weights[i]);
            }
        }

        this.model = model;

    }

    public DataFrame toDataFrame(List<PredictionFeatures> issues) {
        double[] raceDetail = issues.stream().mapToDouble(PredictionFeatures::raceDetail).toArray();
        double[] driverAvgLapTimeRaceKm = issues.stream().mapToDouble(PredictionFeatures::driverAvgLapTimeSpeed).toArray();
        double[] driverBestLapTimeSpeed = issues.stream().mapToDouble(PredictionFeatures::driverBestLapTimeSpeed).toArray();
        double[] driverTopSpeedKph = issues.stream().mapToDouble(PredictionFeatures::driverTopSpeedKph).toArray();
        double[] driverCumulatedPoints = issues.stream().mapToDouble(PredictionFeatures::driverAvgCumulatedPoints).toArray();
        double[] trackCondition = issues.stream().mapToDouble(PredictionFeatures::trackCondition).toArray();
        boolean[] win = extractBoolean(issues, PredictionFeatures::win);
        return DataFrame.of(
                DoubleVector.of("raceDetail", raceDetail),
                DoubleVector.of("driverAvgLapTimeSpeed", driverAvgLapTimeRaceKm),
                DoubleVector.of("driverBestLapTimeSpeed", driverBestLapTimeSpeed),
                DoubleVector.of("driverTopSpeedKph", driverTopSpeedKph),
                DoubleVector.of("driverCumulatedPoints", driverCumulatedPoints),
                DoubleVector.of("trackCondition", trackCondition),
                BooleanVector.of("win", win)


        );
    }


    private static boolean[] extractBoolean(List<PredictionFeatures> list, Function<PredictionFeatures, Boolean> getter) {
        boolean[] result = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = getter.apply(list.get(i));
        }
        return result;
    }

}
