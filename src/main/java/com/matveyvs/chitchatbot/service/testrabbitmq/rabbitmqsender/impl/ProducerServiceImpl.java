package com.matveyvs.chitchatbot.service.testrabbitmq.rabbitmqsender.impl;

import com.matveyvs.chitchatbot.service.testrabbitmq.rabbitmqsender.ProduceService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.matveyvs.chitchatbot.service.testrabbitmq.RabbitQueue.ANSWER_MESSAGE;
@Service
public class ProducerServiceImpl implements ProduceService {
    private final RabbitTemplate rabbitTemplate;

    public ProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}
