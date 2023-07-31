package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@Slf4j
@Component
public class NoneStateHandler implements InputMessageHandler{
    private final ReplyMessageService replyMessageService;

    public NoneStateHandler(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    //todo MAKE SURE THAT METHOD REACHABLE
    @Override
    public SendMessage handle(Message message) {
        Long chat_id = message.getChatId();
        log.info("Handle " + BotState.NONE);
        return replyMessageService
                .getReplyMessage(chat_id, replyMessageService.getLocaleText("reply.user.new"));
    }
    @Override
    public BotState getHandlerName() {
        return BotState.NONE;
    }
}
