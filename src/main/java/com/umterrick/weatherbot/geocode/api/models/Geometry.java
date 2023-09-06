package com.umterrick.weatherbot.geocode.api.models;

import lombok.Data;

@Data
public class Geometry extends GeocodeModel {
    private Location location;
}
