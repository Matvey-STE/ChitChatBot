package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.enums.BotState;
import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.enums.Queries;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
@Log4j2
@Component
public class AdminCallbackQuery implements CallbackQueryHandler {
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    public AdminCallbackQuery(KeyboardService keyboardService,
                              ReplyMessageService replyMessageService,
                              UserService userService) {
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

        UserEntity userEntity = userService.getUserById(chatId);

        if (callbackData.equals(Queries.ADMIN.getValue())){
            List<String> listOfButtons = List.of("Login", "Create Password", "Return to start");
            List<String> listOfBQueries =
                    List.of(Queries.LOGIN.getValue(),
                            Queries.PASSWORD.getValue(),
                            Queries.START.getValue());

            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getLocaleText("reply.admin.keyboard.message"));
            reply.setReplyMarkup(keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
        }
        if (callbackData.equals(Queries.ADMINSERVICE.getValue())){
            List<String> listOfButtons = List
                    .of( "Add user","List of users","Update data", "Return to start");
            List<String> listOfBQueries =
                    List.of(Queries.ADDUSER.getValue(),
                            Queries.LISTOFUSERS.getValue(),
                            Queries.UPDATEDATA.getValue(),
                            Queries.START.getValue());

            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getLocaleText("reply.admin.keyboard.message"));
            reply.setReplyMarkup(keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
        }
        if (callbackData.equals(Queries.ADDUSER.getValue())){
            reply = replyMessageService
                    .getReplyMessage(chatId, replyMessageService.getLocaleText("admin.adduser.question"));

            userEntity.setStateId(BotState.ADDUSER.ordinal());
            userService.saveUser(userEntity);
        }
        if (callbackData.equals(Queries.LISTOFUSERS.getValue())){
            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getLocaleText("reply.admin.users.list"));
            reply.setParseMode("HTML");
            reply.setReplyMarkup(keyboardService.getInlineKeyboard(userService.getAllRegisteredUsers()));
        }
        //remove user from list of registered users
        List<String> listOfUsers = userService.getAllRegisteredUsers();
        for (String userName: listOfUsers){
            if (callbackData.equals(userName)){
                List<String> listOfButtons = List
                        .of("List of users", "Return to ADMIN service");
                List<String> listOfBQueries = List.of(Queries.LISTOFUSERS.getValue(), Queries.ADMINSERVICE.getValue());

                userService.deleteRegisterUserByName(callbackData);
                //it removes user from his NOT only db but from his access completely
                userService.deleteUserByUserName(callbackData);

                log.info("User {} was deleted from DB", userName);

                reply = new SendMessage();
                reply.setChatId(chatId);
                reply.setText(replyMessageService.getLocaleText("reply.admin.remove.user"));
                reply.setParseMode("HTML");
                reply.setReplyMarkup(keyboardService.getInlineKeyboard(listOfButtons,listOfBQueries));
                break;
            }
        }
        replyMessageService.deleteMessage(chatId,callBackMessageId);
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        //merge two lists to remove user from list
        List<String> listOfQueries = userService.getAllRegisteredUsers();
        listOfQueries.addAll(
                List.of(Queries.ADMIN.getValue(),
                        Queries.ADMINSERVICE.getValue(),
                        Queries.ADDUSER.getValue(),
                        Queries.LISTOFUSERS.getValue()));

        return listOfQueries;
    }
}
