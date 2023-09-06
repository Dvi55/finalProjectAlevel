package com.umterrick.weatherbot.weatherApi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastItem {
    private String date;
    private DayForecast day;
    private Astro astro;
}
