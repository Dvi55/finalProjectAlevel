package com.umterrick.weatherbot.botapi;

import com.umterrick.weatherbot.botapi.handlers.keyboard.CallbackQueryHandler;
import com.umterrick.weatherbot.enums.BotCallbackPrefix;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotCallbackQueryContext {

    private Map<BotCallbackPrefix, CallbackQueryHandler> callbackQueryHandlers = new HashMap<>();

    public BotCallbackQueryContext(List<CallbackQueryHandler> callbackQueryHandlers) {
        callbackQueryHandlers.forEach(handler -> this.callbackQueryHandlers.put(handler.getHandlerName(), handler));
    }

    public BotApiMethod<?> processCallbackQuery(BotCallbackPrefix callbackPrefix, CallbackQuery  callbackQuery) {
        CallbackQueryHandler currentCallbackQueryHandler = findCallbackQueryHandler(callbackPrefix);
        return currentCallbackQueryHandler.handle(callbackQuery);
    }

    private CallbackQueryHandler findCallbackQueryHandler(BotCallbackPrefix callbackPrefix) {
        if (BotCallbackPrefix.CITY == callbackPrefix) {
            return callbackQueryHandlers.get(BotCallbackPrefix.CITY);
        }
        if (BotCallbackPrefix.WEATHER == callbackPrefix) {
            return callbackQueryHandlers.get(BotCallbackPrefix.WEATHER);
        }
        return null;
    }
}
