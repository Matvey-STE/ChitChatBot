package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class HelpStateHandler implements InputMessageHandler{
    @Override
    public SendMessage handle(Message message) {
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.HELP;
    }
}
