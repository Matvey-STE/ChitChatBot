package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
@Component
public class AdminCallbackQuery implements CallbackQueryHandler {
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    public AdminCallbackQuery(KeyboardService keyboardService, ReplyMessageService replyMessageService, UserService userService) {
        this.keyboardService = keyboardService;
        this.replyMessageService = replyMessageService;
        this.userService = userService;
    }
    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        UserEntity userEntity = userService.findUserById(chatId);

        if (callbackData.equals("admin")){
            List<String> listOfButtons = List.of("Login", "Create Password", "Return to start");
            List<String> listOfBQueries = List.of("login", "password", "start");

            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getReplyText("reply.admin.keyboard.message"));
            reply.setReplyMarkup(keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
        }
        if (callbackData.equals("adminservice")){
            List<String> listOfButtons = List
                    .of("Add user", "List of users", "Return to start");
            List<String> listOfBQueries = List
                    .of("adduser","listofusers", "start");

            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getReplyText("reply.admin.keyboard.message"));
            reply.setReplyMarkup(keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));

        }
        if (callbackData.equals("adduser")){
            reply = replyMessageService
                    .getReplyMessage(chatId,
                            "admin.adduser.question");

            userEntity.setStateId(BotState.ADDUSER.ordinal());
            userService.saveUser(userEntity);
        }
        if (callbackData.equals("listofusers")){
            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getReplyText("reply.admin.users.list"));
            reply.setReplyMarkup(keyboardService.getInlineKeyboard(userService.getAllRegisteredUsers()));
        }
        replyMessageService.deleteMessage(chatId,callBackMessageId);
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        return List.of("admin","adminservice","adduser","listofusers");
    }
}
