package com.umterrick.weatherbot.service.keyboard.inline;

import com.umterrick.weatherbot.db.models.telegram.TelegramUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface TelegramInlineKeyboard {
    InlineKeyboardMarkup getKeyboard(TelegramUser user);
    SendMessage createMessageWithKeyboard(TelegramUser user, String messageText);
}
