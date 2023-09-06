package com.umterrick.weatherbot.service.keyboard.inline;

import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForecastInlineKeyboardService implements TelegramInlineKeyboard{
    public InlineKeyboardMarkup getKeyboard(TelegramUser user) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton tomorrowButton = new InlineKeyboardButton();
        InlineKeyboardButton threeDaysButton = new InlineKeyboardButton();
        InlineKeyboardButton fiveDaysButton = new InlineKeyboardButton();

        tomorrowButton.setText("Прогноз на завтра");
        threeDaysButton.setText("Прогноз на 3 дні");
        fiveDaysButton.setText("Прогноз на 5 днів");

        tomorrowButton.setCallbackData("weather-2");
        threeDaysButton.setCallbackData("weather-4");
        fiveDaysButton.setCallbackData("weather-6");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(tomorrowButton);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(threeDaysButton);
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(fiveDaysButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public SendMessage createMessageWithKeyboard(TelegramUser user, String messageText) {
        return null;
    }
}
