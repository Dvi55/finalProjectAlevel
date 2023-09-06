package com.umterrick.weatherbot.botapi;

import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotCallbackPrefix;
import com.umterrick.weatherbot.enums.BotState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
 * This class is a facade for the bot.
 * First to handle message
 * Send messege to choose handler
 */
@Component
@Slf4j
public class BotFacade {
    private BotStateContext botStateContext;
    private BotCallbackQueryContext botCallbackQueryContext;
    private final UserRepository userRepository;


    public BotFacade(BotStateContext botStateContext,
                     BotCallbackQueryContext botCallbackQueryContext,
                     UserRepository userRepository) {
        this.botStateContext = botStateContext;
        this.botCallbackQueryContext = botCallbackQueryContext;
        this.userRepository = userRepository;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            replyMessage = handleInputMessage(message);
            replyMessage.enableMarkdown(true);
        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return handleCallbackQuery(callbackQuery);
        }
        return replyMessage;
    }


    private SendMessage handleInputMessage(Message message) {
        String inputMessageText = message.getText();
        long chatId = message.getChatId();
        TelegramUser user = userRepository.findByChatId(chatId);
        if (user == null) {
            user = new TelegramUser(message.getFrom().getUserName(), chatId);
            userRepository.save(user);
        }
        BotState botState;
        SendMessage replyMessage;
        String commandInMessage = inputMessageText;
        if (inputMessageText.startsWith("/")) {
            commandInMessage = inputMessageText.substring(1).toUpperCase();
        }

        switch (commandInMessage) {
            case "START":
                botState = BotState.START;
                break;
            case "HELP":
                botState = BotState.HELP;
                break;
            case "Погода зараз":
                botState = BotState.SHOW_WEATHER;
                break;
            case "Прогноз погоди":
                botState = BotState.SHOW_WEATHER;
                break;
            case "Змінити основне місто":
                botState = BotState.ASK_MAIN_CITY;
                break;
            case "Список міст швидкого доступу":
                botState = BotState.SHOW_ADDITIONAL_CITIES;
                break;
            case "Довідка":
                botState = BotState.HELP;
                break;
            default:
                botState = user.getState();
                break;
        }

        user.setState(botState);
        log.info("User {} state is {}", user.getUsername(), botState);
        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }

    //method to handle callback from main menu keyboard
    private BotApiMethod<?> handleCallbackQuery(CallbackQuery buttonQuery) {
        long chatId = buttonQuery.getMessage().getChatId();
        String[] parsedCallbackData = buttonQuery.getData().split("-");
        String prefixInCallback = parsedCallbackData[0];

        BotCallbackPrefix prefix;
        switch (prefixInCallback) {
            case "CITY":
                prefix = BotCallbackPrefix.CITY;
                break;
            case "weather":
                prefix = BotCallbackPrefix.WEATHER;
                break;
            default:
                prefix = null;
        }

        return botCallbackQueryContext.processCallbackQuery(prefix, buttonQuery);
    }

}

