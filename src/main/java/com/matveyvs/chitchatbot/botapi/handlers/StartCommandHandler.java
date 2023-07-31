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

        UserEntity userEntity = userService.getUserById(chatId);

        if (userEntity == null) {
            List<String> listOfButtons = List.of("LET'S BEGIN THE JOURNEY!");
            List<String> listOfBQueries = List.of("start");
            reply = replyMessageService
                    .getReplyMessage(chatId, replyMessageService.getLocaleText("reply.access.ask"),
                            keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));

            userEntity = new UserEntity(chatId, telegram.getFirstName(), telegram.getLastName(), telegram.getUserName(), "en-UK",false,false,0);

            userEntity.setStateId(BotState.START.ordinal());
            log.info("Add new user from StartCommandHandler: {}", userEntity.toString());

            //set isUser to true if user has already created by userName
            if (userService.isUserInListRegisteredUser(userEntity.getUserName())){
                userEntity.setUserAccess(true);
            }
            log.info("User {} was checked if he has access", message.getChat().getUserName());
            userService.saveUser(userEntity);
        } else {
            List<String> listOfButtons = List.of("LET'S BEGIN THE JOURNEY!");
            List<String> listOfBQueries = List.of("start");
            reply = replyMessageService
                    .getReplyMessage(chatId, replyMessageService.getLocaleText("reply.hello.registered"),
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
