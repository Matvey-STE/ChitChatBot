package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.WebHookBotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Component
public class RegisterCallbackQuery implements CallbackQueryHandler {
    private final ReplyMessageService replyMessageService;
    private final WebHookBotService webHookBotService;

    public RegisterCallbackQuery(ReplyMessageService replyMessageService, WebHookBotService webHookBotService) {
        this.replyMessageService = replyMessageService;
        this.webHookBotService = webHookBotService;
    }
    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();
        if (callbackData.equals("login")){
            reply = replyMessageService.getAndSendReplyMessage(chatId, "admin.password.button");
        } else if (callbackData.equals("password")){
            reply = new SendMessage(String.valueOf(chatId), replyMessageService.getReplyText("admin.generate.admin.password.message"));
                    reply.setReplyMarkup(getPasswordInlineMenu());
        }
        webHookBotService.deleteMessage(String.valueOf(chatId),callBackMessageId);
        return reply;
    }
    private InlineKeyboardMarkup getPasswordInlineMenu(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton adminButton = new InlineKeyboardButton(replyMessageService.getReplyText("start.return"));
        adminButton.setCallbackData("admin");
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(adminButton);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(row1);

        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
    private InlineKeyboardMarkup getLoginCallbackQuery(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton adminButton = new InlineKeyboardButton(replyMessageService.getReplyText("start.return"));
        adminButton.setCallbackData("admin");
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(adminButton);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(row1);

        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }


    @Override
    public List<String> getHandlerQueryType() {
        return List.of("login","password");
    }
}
