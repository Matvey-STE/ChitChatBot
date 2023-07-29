package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import com.matveyvs.chitchatbot.service.WebHookBotService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
@Log4j2
@Component
public class AdminOrUserCallbackQuery implements CallbackQueryHandler {
    private final UserService userService;
    private final String ADMIN = "admin2";
    private final String USER = "user";
    private final ReplyMessageService replyMessageService;

    public AdminOrUserCallbackQuery(UserService userService, ReplyMessageService replyMessageService) {
        this.userService = userService;
        this.replyMessageService = replyMessageService;
    }
    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;

        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();
        UserEntity currentUser = userService.findUserById(chatId);
        if (callbackData.equals(ADMIN)){
            currentUser.setStateId(BotState.ADMIN_PASSWORD.ordinal());
            log.info("Switch to {} from user {}", ADMIN, currentUser.toString());
            //todo make message to admin
            reply = replyMessageService.getAndSendReplyMessage(chatId, "reply.user.message");
        } else if (callbackData.equals(USER)){
            currentUser.setStateId(BotState.USER.ordinal());
            log.info("Switch to {} from user {}", USER, currentUser.toString());
            reply = replyMessageService.getAndSendReplyMessage(chatId, "reply.user.message");
        }
        replyMessageService.deleteMessage(chatId, callBackMessageId);
        userService.saveUser(currentUser);

        return reply;
    }

    @Override
    public List<String> getHandlerQueryType() {
        return List.of(ADMIN,USER);
    }
}
