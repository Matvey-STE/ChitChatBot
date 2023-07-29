package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class PasswordStateHandler implements InputMessageHandler {
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;
    private final UserService userService;
    @Value("${admin.password}")
    private String adminPassword;

    public PasswordStateHandler(KeyboardService keyboardService, ReplyMessageService replyMessageService, UserService userService) {
        this.keyboardService = keyboardService;
        this.replyMessageService = replyMessageService;
        this.userService = userService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply;
        long chatId = message.getChatId();
        String messageText = message.getText();

        UserEntity userEntity = userService.findUserById(chatId);

        if (messageText.equals(adminPassword)){
            List<String> listOfButtons = List.of("Move to ADMIN SERVICE");
            List<String> listOfBQueries = List.of("adminservice");
            reply = replyMessageService
                    .getReplyMessage(chatId,
                            "admin.successful.message",
                            keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
        } else {
            List<String> listOfButtons = List.of("Back to START");
            List<String> listOfBQueries = List.of("start");
            reply = replyMessageService
                    .getReplyMessage(chatId,
                            "admin.unsuccessful.message",
                            keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
        }

        userEntity.setStateId(BotState.START.ordinal());
        userService.saveUser(userEntity);
        return reply;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADMINPASSWORD;
    }
}
