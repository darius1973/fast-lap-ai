package com.example.f1prediction.converter;


import com.example.f1prediction.model.csv.F1DataRecord;
import lombok.Getter;

public class RecordWithCumulative {
    @Getter
    F1DataRecord original;
    @Getter
    double cumulativePointsBefore;

    RecordWithCumulative(F1DataRecord original, int cumulativePointsBefore) {
        this.original = original;
        this.cumulativePointsBefore = cumulativePointsBefore;
    }
}
