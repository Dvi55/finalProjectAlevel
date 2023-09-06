package com.umterrick.weatherbot.botapi.handlers.comands.message.handlers;

import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import com.umterrick.weatherbot.db.repositories.UserRepository;
import com.umterrick.weatherbot.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpHandler implements InputMessageHandler {
    private final UserRepository userRepository;

    public HelpHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SendMessage handle(Message message) {
        String text = """
                **Початок роботи.**
                Після запуску бота у телеграмі, відправте йому перше повідомлення або команду, наприклад /start, щоб розпочати взаємодію.

                **Додаткові міста.**
                Бот може надавати погоду для різних міст. Для цього перейдіть по кнопці "Список міст швидкого доступу" і додайте міста для швидкого прогнозу погоди.

                **Головне меню.**
                Після вибору міста ви потрапите в головне меню, де зможете вибирати, яку інформацію ви бажаєте отримати:

                **Поточна погода:** 
                Дізнайтеся, яка зараз погода в обраному місті.
                **Прогноз:** 
                Отримайте прогноз на 1, 3 або 5 днів вперед.
                **Змінити місто:** 
                Якщо ви хочете змінити місто, де ви отримуєте погоду, скористайтеся цією опцією.
                **Поточна погода.**
                Після вибору "Поточна погода" ви отримаєте інформацію про температуру, стан неба, вологість, швидкість вітру та інші показники для обраного міста.

                **Прогноз.**
                При виборі опції "Прогноз" ви зможете вказати кількість днів (1, 3 або 5), на які ви хочете отримати прогноз погоди. Бот надішле вам інформацію про температуру, опади, швидкість вітру та інші параметри на обраний період.

                **Змінити місто.**
                Якщо ви хочете змінити місто, де ви отримуєте погоду, виберіть "Змінити місто" і введіть нову назву міста.

                **Допомога.**
                Якщо у вас виникли питання або потребуєте допомоги, скористайтеся опцією "Допомога" для зв'язку з адміністраторами бота.""";

        String chatId = String.valueOf(message.getChatId());
        TelegramUser user = userRepository.findByChatId(Long.parseLong(chatId));
        user.setState(BotState.SHOW_MAIN_MENU);
        userRepository.save(user);
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.HELP;
    }
}
