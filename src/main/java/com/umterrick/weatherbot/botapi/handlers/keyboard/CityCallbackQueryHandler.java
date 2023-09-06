package com.umterrick.weatherbot.botapi.handlers.keyboard;

import com.umterrick.weatherbot.cache.DeleteMessageCache;
import com.umterrick.weatherbot.db.models.telegram.City;
import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.CityRepository;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotCallbackPrefix;
import com.umterrick.weatherbot.enums.BotState;
import com.umterrick.weatherbot.messages.formaters.ForecastMessageFormatter;
import com.umterrick.weatherbot.messages.formaters.RealtimeMessageFormatter;
import com.umterrick.weatherbot.service.keyboard.inline.AdditionalCitiesManageInlineKeyboard;
import com.umterrick.weatherbot.weatherApi.models.WeatherData;
import com.umterrick.weatherbot.weatherApi.request.WeatherApiSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CityCallbackQueryHandler implements CallbackQueryHandler {
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final WeatherApiSendRequest weatherApiSendRequest;
    private final RealtimeMessageFormatter realtimeMessageFormatter;
    private final ForecastMessageFormatter forecastMessageFormatter;
    private final AdditionalCitiesManageInlineKeyboard additionalCitiesManageInlineKeyboard;
    private final DeleteMessageCache deleteMessageCache;

    public CityCallbackQueryHandler(CityRepository cityRepository, UserRepository userRepository, WeatherApiSendRequest weatherApiSendRequest,
                                    RealtimeMessageFormatter realtimeMessageFormatter, ForecastMessageFormatter forecastMessageFormatter, AdditionalCitiesManageInlineKeyboard additionalCitiesManageInlineKeyboard, DeleteMessageCache deleteMessageCache) {
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.weatherApiSendRequest = weatherApiSendRequest;
        this.realtimeMessageFormatter = realtimeMessageFormatter;
        this.forecastMessageFormatter = forecastMessageFormatter;
        this.additionalCitiesManageInlineKeyboard = additionalCitiesManageInlineKeyboard;
        this.deleteMessageCache = deleteMessageCache;
    }


    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {
        TelegramUser user = userRepository.findByChatId(callbackQuery.getMessage().getChatId());
        String[] parsedCallbackData = callbackQuery.getData().split("-");
        City city = null;
        String callbackPrefix = parsedCallbackData[0];
        String callbackAction = parsedCallbackData[1];

        if (parsedCallbackData.length == 3) {
            city = cityRepository.findById(Long.parseLong(parsedCallbackData[2])).orElse(null);
        }

        log.info("handle callback prefix: " + callbackPrefix);
        log.info("handle callback action: " + callbackAction);

        if (callbackAction.equals("weather") && city != null) {
            WeatherData cityWeather = weatherApiSendRequest.getWeather(city, 0);
            String messageText = realtimeMessageFormatter.format(cityWeather);
            return editMessageText(callbackQuery, messageText);

        } else if (callbackAction.equals("forecast") && city != null) {
            WeatherData cityWeather = weatherApiSendRequest.getWeather(city, 3);
            String messageText = forecastMessageFormatter.format(cityWeather, 3);
            return editMessageText(callbackQuery, messageText);

        } else if (callbackAction.equals("delete") && city != null) {
            user.removeCityByName(city.getName());
            log.info("City {} deleted", city.getName());
            log.info("User {} cities: {}", user.getChatId(), user.getCities());
            userRepository.save(user);
            cityRepository.save(city);
//            List<City> cities = new ArrayList<>(user.getCities());
//            cities.remove(city);
//            user.setCities(cities);
//            assert city != null;
//            city.getUsers().remove(user);
//            userRepository.save(user);
//            cityRepository.save(city);
            return editMessageText(callbackQuery, "Місто видалено. Скористайтесь кнопками.");
        } else if (callbackAction.equals("add")) {
            deleteMessageCache.addMessageToDelete(callbackQuery.getMessage().getChatId(),  callbackQuery.getMessage().getMessageId());
            user.setState(BotState.SET_ADDITIONAL_CITY);
            userRepository.save(user);
            return new SendMessage(callbackQuery.getMessage().getChatId().toString(), "Введіть назву міста");
        }


        return new SendMessage(callbackQuery.getMessage().getChatId().toString(), "Упс, щось пішло не так");


    }

    private EditMessageText editMessageText(CallbackQuery callbackQuery, String messageText) {
        TelegramUser user = userRepository.findByChatId(callbackQuery.getMessage().getChatId());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(messageText);
        editMessageText.setReplyMarkup(additionalCitiesManageInlineKeyboard.getKeyboard(user));
        return editMessageText;
    }

    @Override
    public BotCallbackPrefix getHandlerName() {
        return BotCallbackPrefix.CITY;
    }
}
