package com.matveyvs.chitchatbot.service.testrabbitmq.rabbimqreciever.impl;

import com.matveyvs.chitchatbot.service.testrabbitmq.controller.RabbitMQController;
import com.matveyvs.chitchatbot.service.testrabbitmq.rabbimqreciever.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import static com.matveyvs.chitchatbot.service.testrabbitmq.RabbitQueue.*;
@Service
public class AnswerConsumerImpl implements AnswerConsumer {
    private final RabbitMQController rabbitMQController;
    public AnswerConsumerImpl(RabbitMQController rabbitMQController) {
        this.rabbitMQController = rabbitMQController;
    }
    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        rabbitMQController.setView(sendMessage);
    }
    @Override
    @RabbitListener(queues = DELETE_MESSAGE)
    public void consume(DeleteMessage deleteMessage) {
    }
    @Override
    @RabbitListener(queues = CALLBACK_ANSWER_MESSAGE)
    public void consume(AnswerCallbackQuery answerCallbackQuery) {
    }
}
