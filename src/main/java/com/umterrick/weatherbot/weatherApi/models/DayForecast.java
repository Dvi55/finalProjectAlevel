package com.umterrick.weatherbot.weatherApi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DayForecast {

    @JsonProperty("maxtemp_c")
    private double maxTempC;

    @JsonProperty("mintemp_c")
    private double minTempC;

    @JsonProperty("maxwind_kph")
    private double maxWindKph;

    @JsonProperty("totalprecip_mm")
    private double totalPrecipMm;

    @JsonProperty("avghumidity")
    private double avgHumidity;

    @JsonProperty("daily_will_it_rain")
    private int willItRain;

    @JsonProperty("daily_chance_of_rain")
    private int chanceOfRain;

    @JsonProperty("daily_will_it_snow")
    private int willItSnow;

    @JsonProperty("daily_chance_of_snow")
    private int chanceOfSnow;

    @JsonProperty("condition")
    private Condition condition;

    @JsonProperty("uv")
    private double uv;

}
