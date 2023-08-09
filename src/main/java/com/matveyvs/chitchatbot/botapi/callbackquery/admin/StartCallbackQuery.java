package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.entity.UserTaskCondition;
import com.matveyvs.chitchatbot.enums.BotState;
import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.enums.StaticQueries;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
@Log4j2
@Component
@AllArgsConstructor
public class StartCallbackQuery implements CallbackQueryHandler {
    private final UserService userService;
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();
        User telegram = callbackQuery.getFrom();

        UserEntity userEntity = userService.getUserById(chatId);

        if(userEntity == null){
            UserTaskCondition userTaskCondition = new UserTaskCondition(0);

            userEntity = new UserEntity(chatId,
                    telegram.getFirstName(),
                    telegram.getLastName(),
                    telegram.getUserName(),
                    "en-UK",
                    false,
                    false,
                    0,
                    userTaskCondition);

            userEntity.setStateId(BotState.START.ordinal());
            userService.saveUserTaskCondition(userTaskCondition);
            userService.saveUser(userEntity);
            log.info("Add new user from StartCallbackQuery: {}", userEntity.toString());
        }

        if (callbackData.equals(StaticQueries.START.getValue())){
            List<String> listOfButtons =
                    List.of("Admin login", "User login");
            List<String> listOfBQueries =
                    List.of(StaticQueries.ADMIN.getValue(),
                            StaticQueries.USER.getValue());

            reply = replyMessageService
                    .getReplyMessage(chatId, replyMessageService.getLocaleText("reply.access.ask"),
                            keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons,listOfBQueries));
            log.info("Delete message after using {}", this.getClass().getSimpleName());
            replyMessageService.deleteMessage(chatId,callBackMessageId);
        }
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        return List.of(StaticQueries.START.getValue());
    }
}
