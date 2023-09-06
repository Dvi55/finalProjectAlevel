package com.umterrick.weatherbot.botapi.handlers.comands.message.handlers;

import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotState;
import com.umterrick.weatherbot.service.keyboard.reply.MainMenuKeyboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StartCommandHandler implements InputMessageHandler {
    private final UserRepository userRepository;
    private final MainMenuKeyboardService mainMenuKeyboardService;

    @Autowired
    public StartCommandHandler(UserRepository userRepository, MainMenuKeyboardService mainMenuKeyboardService) {
        this.userRepository = userRepository;
        this.mainMenuKeyboardService = mainMenuKeyboardService;
    }

    /*
     * Handle the start command
     * returns the main menu
     */
    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();

        TelegramUser user = userRepository.findByChatId(userId);
        SendMessage reply = new SendMessage();
        String replyText;
        if (user.getMainCity() != null) {
            replyText = "З поверненням, " + user.getUsername() + "!";
            user.setState(BotState.SHOW_MAIN_MENU);
            userRepository.save(user);
            return mainMenuKeyboardService.getMainMenuMessage(chatId, replyText);
        } else {
            String username = message.getFrom().getUserName();
            user.setState(BotState.SAVE_MAIN_CITY);
            replyText = "Привіт, " + username + "! Ласкаво прошу, я бот погоди.\n" +
                    "Введіть назву свого міста";
            userRepository.save(user);
            reply.setChatId(chatId);
            reply.setText(replyText);
            return reply;
        }
    }


    @Override
    public BotState getHandlerName() {
        return BotState.START;
    }
}

