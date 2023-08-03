package com.matveyvs.chitchatbot.service.testrabbitmq.rabbitmqsender.impl;

import com.matveyvs.chitchatbot.service.testrabbitmq.rabbitmqsender.ConsumeService;
import com.matveyvs.chitchatbot.service.testrabbitmq.rabbitmqsender.ProduceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.matveyvs.chitchatbot.service.testrabbitmq.RabbitQueue.TEXT_MESSAGE_UPDATE;
@Service
@Log4j2
public class ConsumerServiceImpl implements ConsumeService {
    private final ProduceService produceService;

    public ConsumerServiceImpl(ProduceService produceService) {
        this.produceService = produceService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdate(Update update) {
        log.error("Consumer service achievement");
        Message updateMessage = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(updateMessage.getChatId());
        sendMessage.setText("Hello from consumer");
        produceService.produce(sendMessage);
    }
}
