package com.matveyvs.chitchatbot.controller;

import com.matveyvs.chitchatbot.botapi.TelegramFacade;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final TelegramFacade telegramFacade;

    public WebHookController(TelegramFacade telegramFacade) {
        this.telegramFacade = telegramFacade;
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return telegramFacade.handleUpdate(update);
    }
    //todo remove because of FACADE

/*    private final WebHookBotConfig telegramBot;

    public WebHookController(WebHookBotConfig telegramBot) {
        this.telegramBot = telegramBot;
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return telegramBot.onWebhookUpdateReceived(update);
    }*/
}
