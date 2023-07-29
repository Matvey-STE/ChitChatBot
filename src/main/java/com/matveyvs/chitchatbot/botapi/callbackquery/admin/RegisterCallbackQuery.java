package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
@Component
public class RegisterCallbackQuery implements CallbackQueryHandler {
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;

    public RegisterCallbackQuery(KeyboardService keyboardService, ReplyMessageService replyMessageService) {
        this.keyboardService = keyboardService;
        this.replyMessageService = replyMessageService;
    }
    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        if (callbackData.equals("login")){
            List<String> listOfButtons = List.of("Return to START");
            List<String> listOfBQueries = List.of("start");
            reply = replyMessageService
                    .getReplyMessage(chatId,
                            "admin.password.button",
                            keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
        }
        if (callbackData.equals("password")){
            List<String> listOfButtons = List.of("Return to START");
            List<String> listOfBQueries = List.of("start");
            reply = replyMessageService
                    .getReplyMessage(chatId,
                            "admin.generate.admin.password.message",
                            keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
        }
        replyMessageService.deleteMessage(chatId,callBackMessageId);
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        return List.of("login","password");
    }
}
