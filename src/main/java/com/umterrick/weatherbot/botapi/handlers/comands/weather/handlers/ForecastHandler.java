package com.umterrick.weatherbot.botapi.handlers.comands.weather.handlers;

import com.umterrick.weatherbot.db.models.telegram.City;
import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotState;
import com.umterrick.weatherbot.messages.formaters.ForecastMessageFormatter;
import com.umterrick.weatherbot.service.keyboard.inline.ForecastInlineKeyboardService;
import com.umterrick.weatherbot.weatherApi.models.WeatherData;
import com.umterrick.weatherbot.weatherApi.request.WeatherApiSendRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class ForecastHandler {
    private final ForecastMessageFormatter forecastMessageFormatter;
    private final UserRepository userRepository;
    private final ForecastInlineKeyboardService forecastInlineKeyboardService;
    private final WeatherApiSendRequest weatherApiSendRequest;

    public ForecastHandler(ForecastMessageFormatter forecastMessageFormatter, UserRepository userRepository, ForecastInlineKeyboardService forecastInlineKeyboardService, WeatherApiSendRequest weatherApiSendRequest) {
        this.forecastMessageFormatter = forecastMessageFormatter;
        this.userRepository = userRepository;
        this.forecastInlineKeyboardService = forecastInlineKeyboardService;
        this.weatherApiSendRequest = weatherApiSendRequest;
    }


    public SendMessage handle(Message message, int days) {
        TelegramUser user = userRepository.findByChatId(message.getChatId());
        City city = user.getMainCity();
        if (city != null) {
            WeatherData cityWeather = weatherApiSendRequest.getWeather(city, days);
            String messageText = forecastMessageFormatter.format(cityWeather, days);
            InlineKeyboardMarkup keyboardMarkup = forecastInlineKeyboardService.getKeyboard(user);
            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), messageText);
            sendMessage.setReplyMarkup(keyboardMarkup);
            sendMessage.enableMarkdown(true);
            return sendMessage;
        }
        return null;
    }


    public BotState getHandlerName() {
        return BotState.TAKE_FORECAST;
    }
}
