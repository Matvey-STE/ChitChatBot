package com.matveyvs.chitchatbot.service.testrabbitmq.rabbimqreciever;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

public interface AnswerConsumer {
    void consume(SendMessage sendMessage);
    void consume(DeleteMessage deleteMessage);
    void consume(AnswerCallbackQuery answerCallbackQuery);
}
