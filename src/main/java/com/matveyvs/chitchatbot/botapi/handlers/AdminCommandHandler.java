package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class AdminCommandHandler implements InputMessageHandler{
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    public AdminCommandHandler(KeyboardService keyboardService, ReplyMessageService replyMessageService, UserService userService) {
        this.keyboardService = keyboardService;
        this.replyMessageService = replyMessageService;
        this.userService = userService;
    }
    @Override
    public SendMessage handle(Message message) {
        SendMessage reply = null;
        long chatId = message.getChatId();

        UserEntity userEntity = userService.findUserById(chatId);

        if (userEntity.getStateId() == BotState.ADDUSER.ordinal()){
            List<String> listOfButtons = List.of("Return to ADMIN service");
            List<String> listOfBQueries = List.of("adminservice");
            reply = replyMessageService
                    .getReplyMessage(chatId,
                            "admin.successful.adduser.message",
                            keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
            userEntity.setStateId(BotState.START.ordinal());
            userService.saveUser(userEntity);
        }
        return reply;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADMIN;
    }
}
