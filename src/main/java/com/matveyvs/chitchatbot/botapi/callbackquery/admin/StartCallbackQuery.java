package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
@Component
public class StartCallbackQuery implements CallbackQueryHandler {
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;

    public StartCallbackQuery(KeyboardService keyboardService, ReplyMessageService replyMessageService) {
        this.keyboardService = keyboardService;
        this.replyMessageService = replyMessageService;
    }
    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();


        if (callbackData.equals("start")){
            List<String> listOfButtons = List.of("Admin login", "User login");
            List<String> listOfBQueries = List.of("admin", "user");
            reply = replyMessageService
                    .getReplyMessage(chatId,
                            "reply.access.ask",
                            keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
        }
        replyMessageService.deleteMessage(chatId,callBackMessageId);
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        return List.of("start");
    }
}
