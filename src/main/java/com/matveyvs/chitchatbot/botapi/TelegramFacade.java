package com.matveyvs.chitchatbot.botapi;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryFacade;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class TelegramFacade {
    private final UserService userService;
    private final BotStateContext botStateContext;
    private final CallbackQueryFacade callbackQueryFacade;

    public TelegramFacade(UserService userService, BotStateContext botStateContext, CallbackQueryFacade callbackQueryFacade) {
        this.userService = userService;
        this.botStateContext = botStateContext;
        this.callbackQueryFacade = callbackQueryFacade;
    }

    public BotApiMethod<?> handleUpdate (Update update){
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New CallbackQuery from: {} with data: {}",
                    update.getCallbackQuery().getFrom().getUserName(),
                    update.getCallbackQuery().getData());

            replyMessage = callbackQueryFacade.processCallBackQuery(callbackQuery);
            return replyMessage;
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()){
            log.info("New message from user: {}, chat id: {}, with text: {}",
                    message.getChat().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        SendMessage reply;
        Long chatId = message.getFrom().getId();
        BotState botState = this.getUserStateByMessage(message);
        try {
            UserEntity  userEntity = userService.findUserById(chatId);
            if (userEntity != null) {
                log.info("User before return reply message : {} ", userEntity);
                userEntity.setStateId(botState.ordinal());
                userService.saveUser(userEntity);
                System.out.println("User was saved");
            }
            reply = botStateContext.processInputMessage(botState, message);
        } catch (Exception e) {
            reply = new SendMessage(chatId.toString(),"Can`t handle update on state : " + botState);
            log.info("Can't handle state : {}", botState);
            e.printStackTrace();
        }
        return reply;
    }

    private BotState getUserStateByMessage(Message message) {
        Long chatId = message.getChatId();
        BotState botState;
        String inputMessage = message.getText();
        switch (inputMessage) {
            case "/start" -> botState = BotState.START;
            case "/admin" -> botState = BotState.ADMIN;
            case "/user" -> botState = BotState.USER;
            case "/help" -> botState = BotState.HELP;
            case "/test" -> botState = BotState.TEST;
            default -> {
                UserEntity userEntity = userService.findUserById(chatId);
                if (userEntity == null) {
                    botState = BotState.START;
                } else {
                    botState = userService.findUserById(chatId) != null ? BotState.getValueByInteger(userEntity.getStateId()) : BotState.NONE;
                }
            }
        }
        return botState;
    }
}
