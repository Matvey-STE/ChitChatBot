package com.matveyvs.chitchatbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Locale;

@Service
public class ReplyMessageService {
    private final LocaleMessageService localeMessageService;
    private final WebHookBotService webHookBotService;

    public ReplyMessageService(LocaleMessageService localeMessageService, WebHookBotService webHookBotService) {
        this.localeMessageService = localeMessageService;
        this.webHookBotService = webHookBotService;
    }
    public String getLocaleText(String replyMessage){
        return localeMessageService.getMessage(replyMessage);
    }
    public void deleteMessage(Long chatId, Integer messageId){
        webHookBotService.deleteMessage(chatId, messageId);
    }
    public SendMessage getReplyMessage(Long chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setParseMode("HTML");
        return sendMessage;
    }
    public SendMessage getReplyMessage(Long chatId, String message, InlineKeyboardMarkup inlineKeyboard){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(inlineKeyboard);
        sendMessage.setParseMode("HTML");
        return sendMessage;
    }
    public AnswerCallbackQuery getAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery){
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
    //change locale to use another language
    public void setLocaleMessageService(String localeTag) {
        localeMessageService.setLocale(Locale.forLanguageTag(localeTag));
    }
}
