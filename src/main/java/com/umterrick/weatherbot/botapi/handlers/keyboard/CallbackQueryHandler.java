package com.umterrick.weatherbot.botapi.handlers.keyboard;

import com.umterrick.weatherbot.enums.BotCallbackPrefix;
import com.umterrick.weatherbot.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CallbackQueryHandler {

    BotApiMethod<?> handle(CallbackQuery callbackQuery);

    BotCallbackPrefix getHandlerName();
}
