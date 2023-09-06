package com.umterrick.weatherbot.weatherApi.request;

import com.umterrick.weatherbot.db.models.telegram.City;
import com.umterrick.weatherbot.weatherApi.models.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Component
@Slf4j
public class WeatherApiSendRequest {
    @Value("${weather_api.key}")
    private String apiKey;
    @Value("${weather_api.url}")
    private String url;

    public WeatherData getWeather(City city, int days) {
        RestTemplate restTemplate = new RestTemplate();
        String lat =  String.format(Locale.US, "%.2f", city.getLatitude());
        String lon =  String.format(Locale.US, "%.2f", city.getLongitude());
        String uri = String.format(url, apiKey, lat, lon, days);
        return restTemplate.getForObject(uri, WeatherData.class);
    }
}