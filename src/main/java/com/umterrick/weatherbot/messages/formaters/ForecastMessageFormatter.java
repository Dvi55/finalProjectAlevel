package com.umterrick.weatherbot.messages.formaters;

import com.umterrick.weatherbot.openai.api.ChatGptRequest;
import com.umterrick.weatherbot.weatherApi.models.DayForecast;
import com.umterrick.weatherbot.weatherApi.models.WeatherData;
import org.springframework.stereotype.Component;

@Component

public class ForecastMessageFormatter {
    private final ChatGptRequest  chatGptRequest;

    public ForecastMessageFormatter(ChatGptRequest chatGptRequest) {
        this.chatGptRequest = chatGptRequest;
    }

//            """
//            **%s**
//            %s
//            %s
//            Мін/макс температура: %s°C/%s°C
//            Кількість опадів: %s
//            Макс. швидкість вітру: %s м/c
//            UV: %s
//            """;

    public String format(WeatherData weatherData, int days) {
        StringBuilder message = new StringBuilder();

        message.append("**").append(weatherData.getLocation().getName()).append("**\n");

        for (int i = 1; i < days; i++) {
            DayForecast forecastDay = weatherData.getForecast().getForecastItems().get(i).getDay();
            message.append(weatherData.getForecast().getForecastItems().get(i).getDate()).append(":\n");
            message.append("Мін/макс температура: ")
                    .append(forecastDay.getMinTempC()).append("°C/")
                    .append(forecastDay.getMaxTempC()).append("°C\n");
            message.append("Кількість опадів протягом дня: ").append(forecastDay.getTotalPrecipMm()).append("мм").append("\n");
            message.append("Макс. швидкість вітру: ").append(forecastDay.getMaxWindKph()).append(" км/год\n");
            message.append("UV: ").append(forecastDay.getUv()).append("\n");
            message.append("Рекомендація від штучного інтелекту:");
            message.append(chatGptRequest.chatGptGetForecastRec(message.toString()));
            message.append("\n");
        }

        return message.toString();
    }
}
