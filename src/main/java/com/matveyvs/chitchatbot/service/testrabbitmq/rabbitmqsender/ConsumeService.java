package com.matveyvs.chitchatbot.service.testrabbitmq.rabbitmqsender;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumeService {
    void consumeTextMessageUpdate(Update update);
}
