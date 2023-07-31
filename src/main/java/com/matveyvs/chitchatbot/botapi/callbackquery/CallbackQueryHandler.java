package com.matveyvs.chitchatbot.botapi.callbackquery;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
public interface CallbackQueryHandler {
    BotApiMethod<?> handleCallbackQuery (CallbackQuery callbackQuery);
    List<String> getHandlerQueryType();

}
