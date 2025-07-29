package com.example.f1prediction.model.predict;


public record RaceInfo(
        String raceName,
        double lapLengthKm,
        int numberOfCorners,
        int downforceLevel,
        int brakingZonesCount,
        int overtakingDifficulty,
        double pitLaneTimeLossSeconds,
        String trackSurfaceGrip
) {}
