package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

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

import java.util.List;
@Log4j2
@Component
@AllArgsConstructor
public class RegisterCallbackQuery implements CallbackQueryHandler {
    private final UserService userService;
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        UserEntity userEntity = userService.getUserById(chatId);

        if (callbackData.equals(StaticQueries.LOGIN.getValue())){
            reply = replyMessageService.getReplyMessage(
                    chatId,
                    replyMessageService.getLocaleText("admin.password.question"));

            userEntity.setStateId(BotState.ADMINPASSWORD.ordinal());
            userService.saveUser(userEntity);
        }
        if (callbackData.equals(StaticQueries.PASSWORD.getValue())){
            List<String> listOfButtons =
                    List.of("Return to START");
            List<String> listOfBQueries =
                    List.of(StaticQueries.START.getValue());

            reply = replyMessageService.getReplyMessage(
                            chatId,
                            replyMessageService.getLocaleText("admin.generate.admin.password.message"),
                            keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons,listOfBQueries));
        }

        log.info("Delete message from " + this.getClass().getSimpleName());
        replyMessageService.deleteMessage(chatId,callBackMessageId);
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        return
                List.of(StaticQueries.LOGIN.getValue(),
                        StaticQueries.PASSWORD.getValue());
    }
}
