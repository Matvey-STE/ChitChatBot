package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.enums.BotState;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@Slf4j
@Component
@AllArgsConstructor
public class NoneStateHandler implements InputMessageHandler{
    private final ReplyMessageService replyMessageService;

    //todo MAKE SURE THAT METHOD REACHABLE
    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        log.info("Handle " + BotState.NONE);
        return replyMessageService
                .getReplyMessage(chatId, replyMessageService.getLocaleText("reply.user.new"));
    }
    @Override
    public BotState getHandlerName() {
        return BotState.NONE;
    }
}
