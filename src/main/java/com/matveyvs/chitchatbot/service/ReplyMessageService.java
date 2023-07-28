package com.matveyvs.chitchatbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@Service
public class ReplyMessageService {
    private final LocaleMessageService localeMessageService;

    public ReplyMessageService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    public SendMessage getAndSendReplyMessage(Long chatId, String replyMessage){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(localeMessageService.getMessage(replyMessage));
        sendMessage.setParseMode("HTML");
        return sendMessage;
    }
    public SendMessage getAndSendReplyMessage(String chatId, String replyMessage, Object... args){
        return new SendMessage(chatId, localeMessageService.getMessage(replyMessage,args));
    }
    public void setLocaleMessageService(String localeTag) {
        localeMessageService.setLocale(Locale.forLanguageTag(localeTag));
    }
    public String getReplyText(String replyMessage){
        return localeMessageService.getMessage(replyMessage);
    }
}
