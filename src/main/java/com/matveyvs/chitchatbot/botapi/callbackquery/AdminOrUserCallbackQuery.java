package com.matveyvs.chitchatbot.botapi.callbackquery;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.cache.UserDataCache;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.WebHookBotService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
@Log4j2
@Component
public class AdminOrUserCallbackQuery implements CallbackQueryHandler{
    private final String ADMIN = "admin";
    private final String USER = "user";
    private final UserDataCache userDataCache;
    private final ReplyMessageService replyMessageService;
    private final WebHookBotService webHookBotService;

    public AdminOrUserCallbackQuery(UserDataCache userDataCache, ReplyMessageService replyMessageService, WebHookBotService webHookBotService) {
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
        this.webHookBotService = webHookBotService;
    }
    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;

        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();
        UserEntity currentUser = userDataCache.getUserByIdFromCache(chatId);
        if (callbackData.equals(ADMIN)){
            currentUser.setStateId(BotState.ADMIN_PASSWORD.ordinal());
            log.info("Switch to {} from user {}", ADMIN, currentUser.toString());
            reply = replyMessageService.getReplyMessage(String.valueOf(chatId), "reply.admin.message");
        } else if (callbackData.equals(USER)){
            currentUser.setStateId(BotState.USER.ordinal());
            log.info("Switch to {} from user {}", USER, currentUser.toString());
            reply = replyMessageService.getReplyMessage(String.valueOf(chatId), "reply.user.message");
        }
        //todo delete message from chat
        webHookBotService.deleteMessage(String.valueOf(chatId), callBackMessageId);
        userDataCache.saveCachedUser(currentUser);

        return reply;
    }

    @Override
    public List<String> getHandlerQueryType() {
        return List.of(ADMIN,USER);
    }
}
