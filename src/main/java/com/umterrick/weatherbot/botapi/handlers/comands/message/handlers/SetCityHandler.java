package com.umterrick.weatherbot.botapi.handlers.comands.message.handlers;

import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SetCityHandler implements InputMessageHandler {

    private final UserRepository userRepository;

    public SetCityHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SendMessage handle(Message message) {
        String chatId = String.valueOf(message.getChatId());
        long userId = message.getFrom().getId();
        TelegramUser user = userRepository.findByChatId(userId);
        String replyText = "Введіть назву основного міста";
        user.setState(BotState.SAVE_MAIN_CITY);
        userRepository.save(user);
        return new SendMessage(chatId, replyText);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_MAIN_CITY;
    }
}