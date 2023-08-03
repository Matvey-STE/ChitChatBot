package com.matveyvs.chitchatbot.controller;

import com.matveyvs.chitchatbot.botapi.TelegramFacade;
import com.matveyvs.chitchatbot.service.testrabbitmq.controller.RabbitMQController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final TelegramFacade telegramFacade;
    private final RabbitMQController rabbitMQController;

    public WebHookController(TelegramFacade telegramFacade, RabbitMQController rabbitMQController) {
        this.telegramFacade = telegramFacade;
        this.rabbitMQController = rabbitMQController;
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){

        rabbitMQController.processUpdate(update);

        return telegramFacade.handleUpdate(update);
    }
}
