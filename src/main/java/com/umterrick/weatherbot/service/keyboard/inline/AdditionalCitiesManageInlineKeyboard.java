package com.umterrick.weatherbot.service.keyboard.inline;

import com.umterrick.weatherbot.db.models.telegram.City;
import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.enums.BotCallbackPrefix;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdditionalCitiesManageInlineKeyboard implements TelegramInlineKeyboard {

    @Override
    public InlineKeyboardMarkup getKeyboard(TelegramUser user) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<City> userAdditionalCities = user.getCities();
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        for (City city : userAdditionalCities) {
            List<InlineKeyboardButton> keyboardButtonsRow = getCityButtons(city);

            buttonRows.add(keyboardButtonsRow);

        }

        if (user.getCities().size() < 5) {
            List<InlineKeyboardButton> addCityButtonRow = new ArrayList<>();
            InlineKeyboardButton addCityButton = new InlineKeyboardButton();
            addCityButton.setText("Додати місто");
            addCityButton.setCallbackData(BotCallbackPrefix.CITY + "-add");

            addCityButtonRow.add(addCityButton);
            buttonRows.add(addCityButtonRow);
        }
        inlineKeyboardMarkup.setKeyboard(buttonRows);
        return inlineKeyboardMarkup;
    }

    @Override
    public SendMessage createMessageWithKeyboard(TelegramUser user, String messageText) {
        final SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(user.getChatId());
        message.setText(messageText);
        InlineKeyboardMarkup inlineKeyboardMarkup = this.getKeyboard(user);
        if (inlineKeyboardMarkup != null) {
            message.setReplyMarkup(inlineKeyboardMarkup);
        }
        return message;
    }


    @NotNull
    private static List<InlineKeyboardButton> getCityButtons(City city) {
        InlineKeyboardButton cityButton = new InlineKeyboardButton();
        cityButton.setText(city.getName() + " Погода зараз");
        cityButton.setCallbackData(BotCallbackPrefix.CITY + "-weather-" + city.getId());

        InlineKeyboardButton forecastCityButton = new InlineKeyboardButton();
        forecastCityButton.setText("Forecast");
        forecastCityButton.setCallbackData(BotCallbackPrefix.CITY + "-forecast-" + city.getId());

        InlineKeyboardButton deleteCityButton = new InlineKeyboardButton();
        deleteCityButton.setText("Delete");
        deleteCityButton.setCallbackData(BotCallbackPrefix.CITY + "-delete-" + city.getId());

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(cityButton);
        keyboardButtonsRow.add(forecastCityButton);
        keyboardButtonsRow.add(deleteCityButton);

        return keyboardButtonsRow;
    }
}
