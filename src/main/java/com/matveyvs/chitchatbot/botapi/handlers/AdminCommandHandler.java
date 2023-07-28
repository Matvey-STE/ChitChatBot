package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@Component
public class AdminCommandHandler implements InputMessageHandler{
    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    public AdminCommandHandler(ReplyMessageService replyMessageService, UserService userService) {
        this.replyMessageService = replyMessageService;
        this.userService = userService;
    }


    @Override
    public SendMessage handle(Message message) {

        SendMessage reply;
        reply = replyMessageService.getAndSendReplyMessage(message.getChatId(), "reply.admin.message");

        return reply;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADMIN;
    }
}
