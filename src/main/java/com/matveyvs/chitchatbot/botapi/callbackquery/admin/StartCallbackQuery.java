package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Component
public class StartCallbackQuery implements CallbackQueryHandler {
    private final ReplyMessageService replyMessageService;

    public StartCallbackQuery(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();
        if (callbackData.equals("start")){
            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getReplyText("reply.access.ask"));
            reply.setReplyMarkup(getChooseInlineAdminUserMessages());
        }
        replyMessageService.deleteMessage(chatId,callBackMessageId);
        return reply;
    }
    public InlineKeyboardMarkup getChooseInlineAdminUserMessages(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton adminButton = new InlineKeyboardButton(replyMessageService.getReplyText("admin.button"));
        InlineKeyboardButton userButton = new InlineKeyboardButton(replyMessageService.getReplyText("user.button"));
        adminButton.setCallbackData("admin");
        userButton.setCallbackData("user");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row1.add(adminButton);
        row2.add(userButton);

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(row1);
        buttons.add(row2);

        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
    @Override
    public List<String> getHandlerQueryType() {
        return List.of("start");
    }
}
