package com.umterrick.weatherbot.botapi.handlers.comands.message.handlers;

import com.umterrick.weatherbot.db.models.telegram.City;
import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.CityRepository;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotState;
import com.umterrick.weatherbot.geocode.api.models.Location;
import com.umterrick.weatherbot.geocode.api.request.GeocodeSendRequest;
import com.umterrick.weatherbot.service.keyboard.inline.AdditionalCitiesManageInlineKeyboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SetAdditionalCityHandler implements InputMessageHandler {

    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final GeocodeSendRequest geocodeSendRequest;

    private final AdditionalCitiesManageInlineKeyboard additionalCitiesManageInlineKeyboard;

    public SetAdditionalCityHandler(CityRepository cityRepository, UserRepository userRepository, GeocodeSendRequest geocodeSendRequest, AdditionalCitiesManageInlineKeyboard additionalCitiesManageInlineKeyboard) {
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.geocodeSendRequest = geocodeSendRequest;
        this.additionalCitiesManageInlineKeyboard = additionalCitiesManageInlineKeyboard;
    }

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        TelegramUser user = userRepository.findByChatId(chatId);
        List<City> cities = new ArrayList<>(user.getCities());
        City city = new City();
        String messageText = message.getText();

//Result GeocodingApi
        try {
            City existingCity = cityRepository.findByName(messageText);
            if (existingCity != null) {
                city = existingCity;
                log.info("City {} already exists", city.getName());
                cities.add(city);
                user.setCities(cities);
                user.setState(BotState.SHOW_ADDITIONAL_CITIES);
                cityRepository.save(city);
                userRepository.save(user);

            } else {
                Location requestResult = geocodeSendRequest.getCoordinates(messageText);
                city.setName(messageText);
                city.setLatitude(Double.parseDouble(requestResult.getLat()));
                city.setLongitude(Double.parseDouble(requestResult.getLng()));
                cities.add(city);
                user.setCities(cities);
                cityRepository.save(city);
                userRepository.save(user);
                log.info("City {} saved", city.getName());
            }

            user.setState(BotState.SHOW_ADDITIONAL_CITIES);
            userRepository.save(user);
            log.info("User {} state is {}", user.getUsername(), user.getState());
            log.info("City {} saved", city.getName());
            log.info("City {} , {}saved", city.getLatitude(), city.getLongitude());

            return additionalCitiesManageInlineKeyboard.createMessageWithKeyboard(user, "Місто збережене. Скористайтесь кнопками.");
        } catch (Exception e) {
            return new SendMessage(String.valueOf(chatId), "Місто не знайдено. Спробуйте ще раз.");
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SET_ADDITIONAL_CITY;
    }
}
