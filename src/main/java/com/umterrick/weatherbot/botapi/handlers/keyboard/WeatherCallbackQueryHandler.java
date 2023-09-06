package com.umterrick.weatherbot.botapi.handlers.keyboard;

import com.umterrick.weatherbot.botapi.handlers.comands.weather.handlers.ForecastHandler;
import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.enums.BotCallbackPrefix;
import com.umterrick.weatherbot.service.keyboard.inline.ForecastInlineKeyboardService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class WeatherCallbackQueryHandler implements CallbackQueryHandler{

    private final ForecastHandler forecastHandler;
    private final ForecastInlineKeyboardService forecastInlineKeyboardService;

    public WeatherCallbackQueryHandler(ForecastHandler forecastHandler, ForecastInlineKeyboardService forecastInlineKeyboardService) {
        this.forecastHandler = forecastHandler;
        this.forecastInlineKeyboardService = forecastInlineKeyboardService;
    }


    public EditMessageText handle(CallbackQuery callbackQuery) {
        String[] parsedCallbackData = callbackQuery.getData().split("-");
        int callbackDays = Integer.parseInt(parsedCallbackData[1]);
        TelegramUser user = new TelegramUser();
        Message messageToUpdate = callbackQuery.getMessage();

        if (2 == callbackDays) {
            return getEditMessageText(callbackDays, user, messageToUpdate);

        } else if (4 == callbackDays) {
            return getEditMessageText(callbackDays, user, messageToUpdate);

        } else if (6 == callbackDays) {
            return getEditMessageText(callbackDays, user, messageToUpdate);
        }
        return null;
    }

    @NotNull
    private EditMessageText getEditMessageText(int callbackDays, TelegramUser user, Message messageToUpdate) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(messageToUpdate.getChatId().toString());
        editMessageText.setMessageId(messageToUpdate.getMessageId());
        editMessageText.setText(forecastHandler.handle(messageToUpdate, callbackDays).getText());
        editMessageText.setReplyMarkup(forecastInlineKeyboardService.getKeyboard(user));
        editMessageText.enableMarkdown(true);
        return editMessageText;
    }

    @Override
    public BotCallbackPrefix getHandlerName() {
        return BotCallbackPrefix.WEATHER;
    }
}
