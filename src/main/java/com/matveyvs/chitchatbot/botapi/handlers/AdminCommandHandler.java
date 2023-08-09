package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.enums.BotState;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.enums.StaticQueries;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
@Log4j2
@Component
@AllArgsConstructor
public class AdminCommandHandler implements InputMessageHandler{
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply = null;
        long chatId = message.getChatId();
        Integer messageId = message.getMessageId();

        UserEntity userEntity = userService.getUserById(chatId);
        log.info(userEntity.toString());
        if (userEntity.getStateId() == BotState.ADDUSER.ordinal()){
            List<String> listOfButtons =
                    List.of("Add USER",
                            "Show list of USERS",
                            "Return to ADMIN service");
            List<String> listOfBQueries =
                    List.of(StaticQueries.ADDUSER.getValue(),
                            StaticQueries.LISTOFUSERS.getValue(),
                            StaticQueries.ADMINSERVICE.getValue());
            reply = replyMessageService
                    .getReplyMessage(chatId, replyMessageService.getLocaleText("admin.successful.adduser.message"),
                            keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons,listOfBQueries));

            //todo make sure that it works for many users
            replyMessageService.deleteMessage(chatId, messageId);
            replyMessageService.deleteMessage(chatId,messageId-1);

            if(userService.isUserInListRegisteredUser(userEntity.getUserName())) {
                userEntity.setUserAccess(true);
            }
            log.info(userEntity.isUserAccess());
            log.info("User {} was checked if he has access", message.getChat().getUserName());
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
