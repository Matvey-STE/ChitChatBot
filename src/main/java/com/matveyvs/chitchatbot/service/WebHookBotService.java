package com.matveyvs.chitchatbot.service;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j2
public class WebHookBotService extends TelegramWebhookBot {
    private String webHookPath;
    private String botName;
    private String botToken;

    public WebHookBotService() {
        log.info("Create instance of ChitChatBot");
    }
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }
    //create to delete message not using WebHookFacade
    public void deleteMessage(Long chatId, Integer messageId){
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(messageId);
        deleteMessage.setChatId(String.valueOf(chatId));
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void editMessage(Long charId, Integer messageId, String text){
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(charId);
        editMessageText.setText(text);
        editMessageText.setParseMode("HTML");
        editMessageText.setMessageId(messageId);
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void editMessage(Long chatId, Integer messageId,
                                        String text, InlineKeyboardMarkup inlineKeyboardMarkup){
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setText(text);
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
