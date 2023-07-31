package com.matveyvs.chitchatbot.botapi;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryFacade;
import com.matveyvs.chitchatbot.entity.RegisteredUser;
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
        BotApiMethod<?> replyMessage = null;
        if (update.hasCallbackQuery()){
            Long id = update.getCallbackQuery().getFrom().getId();
            UserEntity userEntity = userService.getUserById(id);
            CallbackQuery callbackQuery = update.getCallbackQuery();
            if (userEntity != null){
                log.info("New CallbackQuery from: {} with data: {}",
                        update.getCallbackQuery().getFrom().getUserName(),
                        update.getCallbackQuery().getData());
                replyMessage = callbackQueryFacade.processCallBackQuery(callbackQuery);
            } else {
                log.info("User entity is null");
                callbackQuery.setData("start");
                replyMessage = callbackQueryFacade.processCallBackQuery(callbackQuery);
            }
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
        String textFromMessage = message.getText();
        BotState botState = this.getUserStateByMessage(message);
        try {
            UserEntity  userEntity = userService.getUserById(chatId);
            if (userEntity != null) {
                log.info("User before return reply message : {} ", userEntity);
                System.out.println("Handle input message");
            }
            reply = botStateContext.processInputMessage(botState, message);
        } catch (Exception e) {
            reply = new SendMessage(chatId.toString(),"Can`t handle update on state : " + botState);
            log.info("Can't handle state : {}", botState);
            e.printStackTrace();
        }
        //telegram bug when you create new chat. You have to have at least 2 messages
        if (!textFromMessage.equals("/start")) {
            //todo make sure that it works for more that one user
            log.info("message");
//            replyMessageService.deleteMessage(chatId, messageId);
//            int i = messageId - 1;
//            replyMessageService.deleteMessage(chatId, messageId-1);
//            System.out.println("Previous message equals " + i);
        }
        return reply;
    }

    private BotState getUserStateByMessage(Message message) {
        Long chatId = message.getChatId();
        BotState botState;
        String inputMessage = message.getText();
        switch (inputMessage) {
            case "/start" -> botState = BotState.START;
            case "/help" -> botState = BotState.HELP;
            case "/test" -> botState = BotState.TEST;
            default -> {
                UserEntity userEntity = userService.getUserById(chatId);
                if (userEntity == null) {
                    botState = BotState.START;
                } else if (userEntity.getStateId() == BotState.ADMINPASSWORD.ordinal()) {
                    botState = BotState.ADMINPASSWORD;
                } else if (userEntity.getStateId() == BotState.ADDUSER.ordinal()){
                    RegisteredUser registerUserByName = userService.getRegisterUserByName(inputMessage);
                    if (registerUserByName == null || !registerUserByName.getUserName().equals(inputMessage)){
                        userService.saveRegisteredUser(inputMessage);
                        log.info("New registered user {} was created",inputMessage);
                    }
                        log.info("User {} has already created",inputMessage);
                    botState = BotState.ADMIN;
                } else {
                    botState = userService.getUserById(chatId) != null ? BotState.getValueByInteger(userEntity.getStateId()) : BotState.NONE;
                }
            }
        }
        return botState;
    }
}
