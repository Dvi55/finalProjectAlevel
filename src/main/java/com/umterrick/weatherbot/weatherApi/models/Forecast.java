package com.umterrick.weatherbot.weatherApi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Forecast {

    @JsonProperty("forecastday")
    private List<ForecastItem> forecastItems;
}