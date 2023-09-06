package com.umterrick.weatherbot.botapi.handlers.comands.message.handlers;

import com.umterrick.weatherbot.db.models.telegram.City;
import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.CityRepository;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotState;
import com.umterrick.weatherbot.geocode.api.models.Location;
import com.umterrick.weatherbot.geocode.api.request.GeocodeSendRequest;
import com.umterrick.weatherbot.service.keyboard.reply.MainMenuKeyboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class SaveMainCityHandler implements InputMessageHandler {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final MainMenuKeyboardService mainMenuKeyboardService;
    private final GeocodeSendRequest geocodeSendRequest;

    public SaveMainCityHandler(UserRepository userRepository, CityRepository cityRepository, MainMenuKeyboardService mainMenuKeyboardService, GeocodeSendRequest geocodeSendRequest) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.mainMenuKeyboardService = mainMenuKeyboardService;
        this.geocodeSendRequest = geocodeSendRequest;
    }

    // search and validate city
    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        City city = new City();
        String messageText = message.getText();

        TelegramUser user = userRepository.findByChatId(chatId);
//Result GeocodingApi
        try {
            Location requestResult = geocodeSendRequest.getCoordinates(messageText);
            City existingCity = cityRepository.findByName(messageText);
            if (existingCity != null) {
                city = existingCity;
                log.info("City {} already exists", city.getName());
            } else {
                city.setName(messageText);
                city.setLatitude(Double.parseDouble(requestResult.getLat()));
                city.setLongitude(Double.parseDouble(requestResult.getLng()));
                cityRepository.save(city);
                log.info("City {} saved", city.getName());
            }

            user.setMainCity(city);
            user.setState(BotState.SHOW_MAIN_MENU);
            userRepository.save(user);
            log.info("User {} state is {}", user.getUsername(), user.getState());
            log.info("City {} saved", city.getName());
            log.info("City {} , {}saved", city.getLatitude(), city.getLongitude());

            return mainMenuKeyboardService.getMainMenuMessage(chatId, "Місто збережене. Скористайтесь кнопками головного меню.");
        } catch (Exception e) {
            return new SendMessage(String.valueOf(chatId), "Місто не знайденно. Спробуйте ще раз.");
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SAVE_MAIN_CITY;
    }
}
