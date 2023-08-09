package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.enums.BotState;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

//TODO TESTS DIFFERENT APPROACHES
@Component
@AllArgsConstructor
public class TestCommandHandler implements InputMessageHandler{
    private final ReplyMessageService replyMessageService;

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();

        SendMessage sendMessage;
        sendMessage = replyMessageService.getReplyMessage(chatId, "It's not ready yet");
        return sendMessage;
    }
    @Override
    public BotState getHandlerName() {
        return BotState.TEST;
    }
    // testing database creation new entity in db
}
