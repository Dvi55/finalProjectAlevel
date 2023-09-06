package com.umterrick.weatherbot.geocode.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GeocodeResponse extends GeocodeModel {
    private List<Result> results;
    @JsonProperty("status")
    private String status;
}
