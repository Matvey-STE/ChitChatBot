package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.enums.StaticQueries;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
@Log4j2
@Component
public class UserCallbackQuery implements CallbackQueryHandler {
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    public UserCallbackQuery(KeyboardService keyboardService, UserService userService, ReplyMessageService replyMessageService) {
        this.keyboardService = keyboardService;
        this.userService = userService;
        this.replyMessageService = replyMessageService;
    }
    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        BotApiMethod<?> reply;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();
        UserEntity userEntity = userService.getUserById(chatId);
        reply = replyMessageService.getAnswerCallbackQuery(
                "You unregistered user please ask ADMIN",
                true,
                callbackQuery);

        if (callbackData.equals(StaticQueries.USER.getValue()) && userEntity.isUserAccess()){
            List<String> listOfButtons =
                    List.of("Best Definition TASK",
                            "Return to START");
            List<String> listOfBQueries =
                    List.of(StaticQueries.BESTDEFINITIONTASK.getValue(),
                            StaticQueries.START.getValue());

            reply = replyMessageService.getReplyMessage(
                            chatId,
                            replyMessageService.getLocaleText("reply.user.message"),
                            keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons,listOfBQueries));

            log.info("Delete message from {} where {}", this.getClass().getSimpleName(), StaticQueries.USER);
            replyMessageService.deleteMessage(chatId, callBackMessageId);

            userService.saveConditionBestDefinition(chatId, 5);
        }
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        return List.of(StaticQueries.USER.getValue());
    }
}
