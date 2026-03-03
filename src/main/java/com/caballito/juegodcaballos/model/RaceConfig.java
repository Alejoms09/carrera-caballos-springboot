package com.caballito.juegodcaballos.model;

public record RaceConfig(int trackLength, boolean falteringEnabled) {
    public RaceConfig {
        if (trackLength < 3 || trackLength > 12) {
            throw new IllegalArgumentException("trackLength recomendado entre 3 y 12.");
        }
    }
}
