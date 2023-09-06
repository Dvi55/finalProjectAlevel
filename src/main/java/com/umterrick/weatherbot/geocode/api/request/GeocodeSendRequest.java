package com.umterrick.weatherbot.geocode.api.request;

import com.umterrick.weatherbot.geocode.api.models.GeocodeResponse;
import com.umterrick.weatherbot.geocode.api.models.Geometry;
import com.umterrick.weatherbot.geocode.api.models.Location;
import com.umterrick.weatherbot.geocode.api.models.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeocodeSendRequest {

    @Value("${google_geocoding_api.key}")
    private String apiKey;
    @Value("${google_geocoding_api.url}")
    private String url;


    public Location getCoordinates(String city) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String uri = String.format(url, city, apiKey);
        GeocodeResponse geocodeResponse = restTemplate.getForObject(uri, GeocodeResponse.class);

        if (geocodeResponse != null && "OK".equals(geocodeResponse.getStatus()) && !geocodeResponse.getResults().isEmpty()) {
            Result result = geocodeResponse.getResults().get(0);
            Geometry geometry = result.getGeometry();
            if (geometry != null) {
                return geometry.getLocation();
            }
        }
        throw new Exception("Could not get coordinates for city: " + city);
    }
}