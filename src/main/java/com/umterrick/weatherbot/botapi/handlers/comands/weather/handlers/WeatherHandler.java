package com.umterrick.weatherbot.botapi.handlers.comands.weather.handlers;

import com.umterrick.weatherbot.botapi.handlers.comands.message.handlers.InputMessageHandler;
import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class WeatherHandler implements InputMessageHandler {

private final ForecastHandler forecastHandler;
private final RealtimeHandler realtimeHandler;
private final UserRepository userRepository;

    public WeatherHandler(ForecastHandler forecastHandler, RealtimeHandler realtimeHandler, UserRepository userRepository) {
        this.forecastHandler = forecastHandler;
        this.realtimeHandler = realtimeHandler;
        this.userRepository = userRepository;
    }

    @Override
    public SendMessage handle(Message message) {
        String text = message.getText();
        TelegramUser user = userRepository.findByChatId(message.getChatId());
        if (text.equals("Погода зараз")) {
            user.setState(BotState.TAKE_WEATHER);
            userRepository.save(user);
       return realtimeHandler.handle(message);
        }
        if (text.equals("Прогноз погоди")) {
            user.setState(BotState.TAKE_FORECAST);
            userRepository.save(user);
            return forecastHandler.handle(message, 1);
        }
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_WEATHER;
    }
}
