package com.umterrick.weatherbot.geocode.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Result extends GeocodeModel {
    @JsonProperty("address_components")
    private List<AddressComponent> addressComponents;

    private Geometry geometry;

    public String getCityName() {
        if (addressComponents != null && !addressComponents.isEmpty()) {
            return addressComponents.get(0).getCityName();
        }
        return null;
    }
}
