package com.umterrick.weatherbot;

import com.umterrick.weatherbot.botapi.BotFacade;
import com.umterrick.weatherbot.cache.DeleteMessageCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;


@Component
public class WeatherTelegramBot extends TelegramLongPollingBot {

    private final BotFacade botFacade;
    @Value("${bot.Name")
    String botName;
    private final DeleteMessageCache deleteMessageCache;

    public WeatherTelegramBot(@Value("${bot.token}") String botToken, BotFacade botFacade, DeleteMessageCache deleteMessageCache) {
        super(botToken);
        this.botFacade = botFacade;
        this.deleteMessageCache = deleteMessageCache;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        BotApiMethod<?> replyMessage = botFacade.handleUpdate(update);
        try {
            execute(replyMessage);

            Map<Long, Integer> messagesToDelete = deleteMessageCache.getMessagesToDelete();
            if (!messagesToDelete.isEmpty()) {
                for (Map.Entry<Long, Integer> entry : messagesToDelete.entrySet()) {
                    DeleteMessage deleteMessage = new DeleteMessage(entry.getKey().toString(), entry.getValue());
                    execute(deleteMessage);
                }
                deleteMessageCache.clearMessagesToDelete();
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
