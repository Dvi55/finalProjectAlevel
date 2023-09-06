package com.umterrick.weatherbot.cache;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class DeleteMessageCache {

    private Map<Long, Integer> messagesToDelete = new HashMap<>();

    public void addMessageToDelete(long chatId, int messageId) {
        messagesToDelete.put(chatId, messageId);
    }

    public void clearMessagesToDelete() {
        messagesToDelete.clear();
    }
}
