package com.umterrick.weatherbot.botapi.handlers.comands.message.handlers;

import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotState;
import com.umterrick.weatherbot.service.keyboard.inline.AdditionalCitiesManageInlineKeyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AdditionalCitiesHandler  implements InputMessageHandler{
    private final UserRepository userRepository;
    private final AdditionalCitiesManageInlineKeyboard additionalCitiesManageInlineKeyboard;

    public AdditionalCitiesHandler(UserRepository userRepository, AdditionalCitiesManageInlineKeyboard additionalCitiesManageInlineKeyboard) {
        this.userRepository = userRepository;
        this.additionalCitiesManageInlineKeyboard = additionalCitiesManageInlineKeyboard;
    }

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        TelegramUser user = userRepository.findByChatId(chatId);
        return additionalCitiesManageInlineKeyboard.createMessageWithKeyboard(user, "Ваші обрані міста:");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_ADDITIONAL_CITIES;
    }
}
