package com.matveyvs.chitchatbot.service.testrabbitmq.rabbitmqsender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ProduceService {
    void produce(SendMessage sendMessage);
}
