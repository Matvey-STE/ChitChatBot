package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

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
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        if (callbackData.equals("user")) {
            List<String> listOfButtons = List.of("Return to START");
            List<String> listOfBQueries = List.of("start");
            InlineKeyboardMarkup inlineKeyboardMarkup = keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries);

            reply = replyMessageService.getReplyMessage(chatId, "reply.user.message",inlineKeyboardMarkup);
        }
            replyMessageService.deleteMessage(chatId, callBackMessageId);
        return reply;
    }

    @Override
    public List<String> getHandlerQueryType() {
        return List.of("user");
    }
}
