package com.matveyvs.chitchatbot.botapi;

import com.matveyvs.chitchatbot.botapi.handlers.InputMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Component
public class BotStateContext {
    private final Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isStartState(currentState)) {
            return messageHandlers.get(BotState.START_MESSAGE);
        }
        if (isAdmin(currentState)) {
            return messageHandlers.get(BotState.ADMIN_HELP);
        }
        if (isUser(currentState)) {
            return messageHandlers.get(BotState.USER_HELP);
        }
        return messageHandlers.get(currentState);
    }

    private boolean isStartState(BotState state) {
        return switch (state) {
            case START_MESSAGE, ADMIN_PASSWORD -> true;
            default -> false;
        };
    }
    private boolean isAdmin(BotState state){
        return switch (state) {
            case CREATEUSER,
                    DELETEUSER,
                    UPDATEDATA,
                    LISTOFADMINS,
                    LISTOFUSERS,
                    ADMIN_HELP -> true;
            default -> false;
        };
    }
    private boolean isUser(BotState state){
        return switch (state) {
            case USER, USER_HELP -> true;
            default -> false;
        };
    }



}
