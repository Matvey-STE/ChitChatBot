package com.matveyvs.chitchatbot.botapi.callbackquery;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Component
public class CallbackQueryFacade {
    private final List<CallbackQueryHandler> callbackQueryHandlers;
    public CallbackQueryFacade(List<CallbackQueryHandler> callbackQueryHandlers) {
        this.callbackQueryHandlers = callbackQueryHandlers;
    }
    public SendMessage processCallBackQuery(CallbackQuery userQuery){
        return getHandlerByCallBackQuery(userQuery.getData()).handleCallbackQuery(userQuery);
    }
    private CallbackQueryHandler getHandlerByCallBackQuery(String query) {
        return callbackQueryHandlers.stream()
                .filter(h -> h.getHandlerQueryType().stream()
                        .anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }
}
