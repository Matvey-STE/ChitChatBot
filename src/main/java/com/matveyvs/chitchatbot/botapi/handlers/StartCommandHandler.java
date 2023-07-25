package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@Log4j2
@Component
public class StartCommandHandler implements InputMessageHandler{
    private final UserService userService;
    private final ReplyMessageService replyMessageService;

    public StartCommandHandler(UserService userService, ReplyMessageService replyMessageService) {
        this.userService = userService;
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply;
        long chatId = message.getChatId();
        org.telegram.telegrambots.meta.api.objects.User telegram = message.getFrom();
        UserEntity user = userService.findUserById(chatId);
        if (user == null) {
            user = new UserEntity(chatId, telegram.getFirstName(), telegram.getLastName(), telegram.getUserName(), "en-UK",0);
            user.setStateId(BotState.NONE.ordinal());
            userService.saveUser(user);
            log.info("Add new user: username : {} chat_id : {} firstname : {}",telegram.getUserName(),message.getChatId(),telegram.getFirstName());
            reply = replyMessageService.getReplyMessage(chatId,"reply.hello");
        } else {
            user.setStateId(BotState.USER.ordinal());
            userService.saveUser(user);
            reply = replyMessageService.getReplyMessage(chatId,"reply.hello.registered");
        }
        return reply;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START_MESSAGE;
    }
}
