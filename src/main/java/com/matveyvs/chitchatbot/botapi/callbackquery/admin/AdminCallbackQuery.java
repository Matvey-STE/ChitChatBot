package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import com.matveyvs.chitchatbot.service.WebHookBotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Component
public class AdminCallbackQuery implements CallbackQueryHandler {

    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    public AdminCallbackQuery(ReplyMessageService replyMessageService, UserService userService) {
        this.replyMessageService = replyMessageService;
        this.userService = userService;
    }
    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();
        if (callbackData.equals("admin")){
            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getReplyText("reply.admin.keyboard.message"));
            reply.setReplyMarkup(getChooseInlineAdminUserMessages());
        }
        replyMessageService.deleteMessage(chatId,callBackMessageId);
        return reply;
    }
    public InlineKeyboardMarkup getChooseInlineAdminUserMessages(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton loginButton = new InlineKeyboardButton(replyMessageService.getReplyText("admin.password.button"));
        InlineKeyboardButton passwordButton = new InlineKeyboardButton(replyMessageService.getReplyText("admin.generate.admin.password.button"));
        InlineKeyboardButton startButton = new InlineKeyboardButton(replyMessageService.getReplyText("start.return"));

        loginButton.setCallbackData("login");
        passwordButton.setCallbackData("password");
        startButton.setCallbackData("admin");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row1.add(loginButton);
        row2.add(passwordButton);
        row3.add(startButton);

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(row1);
        buttons.add(row2);
        buttons.add(row3);

        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
    @Override
    public List<String> getHandlerQueryType() {
        return List.of("admin");
    }
}
