package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
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

    public StartCommandHandler(UserService userService, ReplyMessageService replyMessageService) {
        this.userService = userService;
        this.replyMessageService = replyMessageService;
    }
    @Override
    public SendMessage handle(Message message) {
        SendMessage reply;
        Integer messageId = message.getMessageId();
        long chatId = message.getChatId();
        org.telegram.telegrambots.meta.api.objects.User telegram = message.getFrom();
        UserEntity userEntity = userService.findUserById(chatId);

        //delete message before create a new one
        replyMessageService.deleteMessage(chatId, messageId);

        if (userEntity == null) {
            userEntity = new UserEntity(chatId, telegram.getFirstName(), telegram.getLastName(), telegram.getUserName(), "en-UK",false,false,0);
            userEntity.setStateId(BotState.TEST.ordinal());
            userService.saveUser(userEntity);
            log.info("Add new user: {}", userEntity.toString());
            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.enableHtml(true);
            reply.setText(replyMessageService.getReplyText("reply.access.ask"));
            reply.setReplyMarkup(getChooseInlineAdminUserMessages());
        } else {
            userEntity.setStateId(BotState.USER.ordinal());
            userService.saveUser(userEntity);
            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.enableHtml(true);
            reply.setText(replyMessageService.getReplyText("reply.hello.registered"));
            reply.setReplyMarkup(getChooseInlineAdminUserMessages());
        }
        return reply;
    }
    public InlineKeyboardMarkup getChooseInlineAdminUserMessages(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton adminButton = new InlineKeyboardButton(replyMessageService.getReplyText("start.button"));
        adminButton.setCallbackData("start");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(adminButton);

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(row1);

        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
    @Override
    public BotState getHandlerName() {
        return BotState.START;
    }
}
