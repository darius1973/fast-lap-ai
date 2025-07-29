package com.example.f1prediction.schedule;

import com.example.f1prediction.model.csv.F1DataRecord;
import com.example.f1prediction.model.predict.DriverStats;
import com.example.f1prediction.model.training.PredictionFeatures;
import com.example.f1prediction.trainer.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smile.classification.LogisticRegression;
import smile.classification.SoftClassifier;
import smile.data.DataFrame;
import smile.feature.Standardizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PredictService {

    List<String> drivers = List.of("VER", "NOR", "PIA","LEC","HAM","RUS");


    @Autowired
    private TrainerService trainerservice;

    public Map<String, Double> predictionResults(String race) {
        Map<String, Double> predictedMap = new HashMap<>();
        LogisticRegression model = trainerservice.getModel();

        // Cast to SoftClassifier for probabilities
        SoftClassifier<double[]> softModel = (SoftClassifier<double[]>) model;

        // Get Standardizer used during training
        Standardizer standardizer = trainerservice.getStandardizer();

        drivers.forEach(driver -> {
            PredictionFeatures predictionFeatures =
                    DriverStats.getDriverStats(trainerservice.getPredictionFeatures(), driver, race);
            // Build DataFrame with a single row using training-compatible structure
            DataFrame featureDf = trainerservice.toDataFrame(List.of(predictionFeatures)).drop("win");

            // Apply the same standardizer used during training
            DataFrame standardized = standardizer.transform(featureDf);

            // Extract feature array from the first (and only) row
            double[] features = standardized.get(0).toArray();

            double[] probs = new double[2];
            softModel.predict(features, probs);
            double winProbability = probs[1];

            System.out.printf("Driver: %s → Win probability: %.2f%%%n", driver, winProbability * 100);
            predictedMap.put(driver, winProbability);
        });

        return predictedMap;
    }
}
