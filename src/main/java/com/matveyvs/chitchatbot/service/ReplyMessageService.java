package com.matveyvs.chitchatbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@Service
public class ReplyMessageService {
    private final LocaleMessageService localeMessageService;
    private final WebHookBotService webHookBotService;

    public ReplyMessageService(LocaleMessageService localeMessageService, WebHookBotService webHookBotService) {
        this.localeMessageService = localeMessageService;
        this.webHookBotService = webHookBotService;
    }
    //change locale to use another language
    public void setLocaleMessageService(String localeTag) {
        localeMessageService.setLocale(Locale.forLanguageTag(localeTag));
    }
    public SendMessage getAndSendReplyMessage(Long chatId, String replyMessage){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(localeMessageService.getMessage(replyMessage));
        sendMessage.setParseMode("HTML");
        return sendMessage;
    }
    public void deleteMessage(Long chatId, Integer messageId){
        webHookBotService.deleteMessage(chatId, messageId);
    }
    public String getReplyText(String replyMessage){
        return localeMessageService.getMessage(replyMessage);
    }
}
