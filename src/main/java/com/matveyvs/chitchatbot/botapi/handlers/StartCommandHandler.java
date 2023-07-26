package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import com.matveyvs.chitchatbot.service.WebHookBotService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class StartCommandHandler implements InputMessageHandler{
    private final UserService userService;
    private final ReplyMessageService replyMessageService;
    private final WebHookBotService webHookBotService;

    public StartCommandHandler(UserService userService, ReplyMessageService replyMessageService, WebHookBotService webHookBotService) {
        this.userService = userService;
        this.replyMessageService = replyMessageService;
        this.webHookBotService = webHookBotService;
    }
    @Override
    public SendMessage handle(Message message) {
        SendMessage reply;
        Integer messageId = message.getMessageId();
        long chatId = message.getChatId();
        org.telegram.telegrambots.meta.api.objects.User telegram = message.getFrom();
        UserEntity user = userService.findUserById(chatId);

        //delete message before create a new one
        webHookBotService.deleteMessage(String.valueOf(chatId), messageId);

        if (user == null) {
            user = new UserEntity(chatId, telegram.getFirstName(), telegram.getLastName(), telegram.getUserName(), "en-UK",false,false,0);
            user.setStateId(BotState.NONE.ordinal());
            userService.saveUser(user);
            log.info("Add new user: {}", user.toString());
            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.enableHtml(true);
            reply.setText(replyMessageService.getReplyText("reply.access.ask"));
            reply.setReplyMarkup(getChooseInlineAdminUserMessages());
        } else {
            user.setStateId(BotState.USER.ordinal());
            userService.saveUser(user);
            reply = replyMessageService.getReplyMessage(chatId,"reply.hello.registered");
        }
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
    public BotState getHandlerName() {
        return BotState.START_MESSAGE;
    }
}