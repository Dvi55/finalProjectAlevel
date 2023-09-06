package com.umterrick.weatherbot.weatherApi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeather {
    @JsonProperty("temp_c")
    private double temperature;

    @JsonProperty("is_day")
    private int isDay;

    @JsonProperty("condition")
    private Condition condition;

    @JsonProperty("wind_kph")
    private double windKph;

    @JsonProperty("pressure_mb")
    private double pressureMb;

    @JsonProperty("precip_mm")
    private double precipMm;

    @JsonProperty("humidity")
    private int humidity;

    @JsonProperty("feelslike_c")
    private double feelsLikeC;

    @JsonProperty("uv")
    private double uv;
}
