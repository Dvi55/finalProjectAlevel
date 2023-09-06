package com.umterrick.weatherbot.weatherApi.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Condition {
    private String text;
}
