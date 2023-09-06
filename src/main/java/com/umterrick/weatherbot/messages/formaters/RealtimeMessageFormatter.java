package com.umterrick.weatherbot.messages.formaters;

import com.umterrick.weatherbot.weatherApi.models.WeatherData;
import org.springframework.stereotype.Component;

@Component
public class RealtimeMessageFormatter {


    private static final String WEATHER_PATTERN = """
            **%s**
            %s
            %s
            Температура: %s°C
            Відчувається як: %s°C
            Швидкість вітру: %s км/год
            Тиск: %s мілібарів
            Опади: %s мм
            Вологість: %s%%
            Світанок: %s
            Захід сонця: %s
            Фаза місяця: %s
            Рекомендація від штучного інтелекту:
            """;


    public String format(WeatherData weatherData) {
        String locationName = weatherData.getLocation().getName();
        String conditionText = weatherData.getCurrent().getCondition().getText();
        int isDay = weatherData.getCurrent().getIsDay();
        String timeOfDay = (isDay == 1) ? "Зараз день" : "Зараз ніч";
        double temperatureC = weatherData.getCurrent().getTemperature();
        double feelsLikeC = weatherData.getCurrent().getFeelsLikeC();
        double windSpeed = weatherData.getCurrent().getWindKph();
        double pressureMb = weatherData.getCurrent().getPressureMb();
        double precipMm = weatherData.getCurrent().getPrecipMm();
        int humidity = weatherData.getCurrent().getHumidity();
        String sunrise = weatherData.getForecast().getForecastItems().get(0).getAstro().getSunrise();
        String sunset = weatherData.getForecast().getForecastItems().get(0).getAstro().getSunset();
        String moonPhase = weatherData.getForecast().getForecastItems().get(0).getAstro().getMoonPhase();

        return String.format(WEATHER_PATTERN,
                locationName,
                conditionText,
                timeOfDay,
                temperatureC,
                feelsLikeC,
                windSpeed,
                pressureMb,
                precipMm,
                humidity,
                sunrise,
                sunset,
                moonPhase);
    }
}
