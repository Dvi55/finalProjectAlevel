package com.umterrick.weatherbot.weatherApi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WeatherData {
    private Location location;
    private CurrentWeather current;
    private Forecast forecast;
}