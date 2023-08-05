package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandler {
    SendMessage handle(Message message);
    BotState getHandlerName();
}
