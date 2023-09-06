package com.umterrick.weatherbot.botapi.handlers.comands.message.handlers;

import com.umterrick.weatherbot.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


public interface InputMessageHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();
}
