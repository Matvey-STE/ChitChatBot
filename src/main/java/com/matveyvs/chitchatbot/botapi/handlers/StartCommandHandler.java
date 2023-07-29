package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Log4j2
@Component
public class StartCommandHandler implements InputMessageHandler{
    private final KeyboardService keyboardService;
    private final UserService userService;
    private final ReplyMessageService replyMessageService;

    public StartCommandHandler(KeyboardService keyboardService, UserService userService, ReplyMessageService replyMessageService) {
        this.keyboardService = keyboardService;
        this.userService = userService;
        this.replyMessageService = replyMessageService;
    }
    @Override
    public SendMessage handle(Message message) {
        SendMessage reply;
        long chatId = message.getChatId();
        org.telegram.telegrambots.meta.api.objects.User telegram = message.getFrom();

        UserEntity userEntity = userService.findUserById(chatId);

        if (userEntity == null) {
            List<String> listOfButtons = List.of("Let's begin journey!");
            List<String> listOfBQueries = List.of("start");
            reply = replyMessageService
                    .getReplyMessage(chatId,
                            "reply.access.ask",
                            keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));

            userEntity = new UserEntity(chatId, telegram.getFirstName(), telegram.getLastName(), telegram.getUserName(), "en-UK",false,false,0);
            userEntity.setStateId(BotState.START.ordinal());
            userService.saveUser(userEntity);
            log.info("Add new user: {}", userEntity.toString());

        } else {
            List<String> listOfButtons = List.of("Hi again, let's play the game");
            List<String> listOfBQueries = List.of("start");
            reply = replyMessageService
                    .getReplyMessage(chatId,
                            "reply.hello.registered",
                            keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));

            userEntity.setStateId(BotState.START.ordinal());
            userService.saveUser(userEntity);
        }
        return reply;
    }
    @Override
    public BotState getHandlerName() {
        return BotState.START;
    }
}
