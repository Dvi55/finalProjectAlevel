package com.umterrick.weatherbot.openai.api;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChatGptRequest {
    @Value("${openai.token}")
    private String openaiToken;

    public String chatGptGetRealtimeRec(String inputWeather) {
        String reply = null;

        OpenAiService service = new OpenAiService(openaiToken, Duration.ofSeconds(30));

        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Надай коротку рекомендацію по поточній погоді (не звертай увагу на світанок, захід сонця " +
                        "та фазу місяця, якщо потрібно, нагадай про парасольку чи теплий одяг): " + inputWeather);

        messages.add(systemMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(300)
                .build();

        ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);
        reply = result.getChoices().get(0).getMessage().getContent();

        return reply;

    }

    public String chatGptGetForecastRec(String inputWeather) {
        String reply = null;

        OpenAiService service = new OpenAiService(openaiToken, Duration.ofSeconds(30));

        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Надай коротку рекомендацію по прогнозу погоди(1 речення): " + inputWeather);

        messages.add(systemMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(300)
                .build();

        ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);
        reply = result.getChoices().get(0).getMessage().getContent();

        return reply;
    }
}
