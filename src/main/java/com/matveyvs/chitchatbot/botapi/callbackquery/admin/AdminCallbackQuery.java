package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.enums.BotState;
import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.enums.StaticQueries;
import com.matveyvs.chitchatbot.service.CallbackQueriesService;
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
    private final CallbackQueriesService callbackQueriesService;
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    public AdminCallbackQuery(CallbackQueriesService callbackQueriesService,
                              KeyboardService keyboardService,
                              ReplyMessageService replyMessageService,
                              UserService userService) {
        this.callbackQueriesService = callbackQueriesService;
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

        if (callbackData.equals(StaticQueries.ADMIN.getValue())){
            List<String> listOfButtons =
                    List.of("Login",
                    "Create Password",
                    "Return to start");
            List<String> listOfBQueries =
                    List.of(StaticQueries.LOGIN.getValue(),
                            StaticQueries.PASSWORD.getValue(),
                            StaticQueries.START.getValue());

            reply = replyMessageService.getReplyMessage(
                    chatId,
                    replyMessageService.getLocaleText("reply.admin.keyboard.message"),
                    keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons,listOfBQueries));
        }
        if (callbackData.equals(StaticQueries.ADMINSERVICE.getValue())){
            List<String> listOfButtons = List
                    .of( "Add user",
                            "List of users",
                            "Update data",
                            "Return to start");
            List<String> listOfBQueries =
                    List.of(StaticQueries.ADDUSER.getValue(),
                            StaticQueries.LISTOFUSERS.getValue(),
                            StaticQueries.UPDATEDATA.getValue(),
                            StaticQueries.START.getValue());

            reply = replyMessageService.getReplyMessage(
                    chatId,
                    replyMessageService.getLocaleText("reply.admin.keyboard.message"),
                    keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons,listOfBQueries));
        }
        if (callbackData.equals(StaticQueries.ADDUSER.getValue())){
            reply = replyMessageService.getReplyMessage(
                    chatId,
                    replyMessageService.getLocaleText("admin.adduser.question"));

            userEntity.setStateId(BotState.ADDUSER.ordinal());
            userService.saveUser(userEntity);
        }
        if (callbackData.equals(StaticQueries.LISTOFUSERS.getValue())){
            reply = replyMessageService.getReplyMessage(
                    chatId,
                    replyMessageService.getLocaleText("reply.admin.users.list"),
                    keyboardService.getInlineKeyboardLineByLine(userService.getAllRegisteredUsers()));
        }
        //remove user from list of registered users
        List<String> listOfUsers = userService.getAllRegisteredUsers();
        for (String userName: listOfUsers){
            if (callbackData.equals(userName)){
                List<String> listOfButtons =
                        List.of("List of users",
                                "Return to ADMIN service");
                List<String> listOfBQueries =
                        List.of(StaticQueries.LISTOFUSERS.getValue(),
                                StaticQueries.ADMINSERVICE.getValue());

                userService.deleteRegisterUserByName(callbackData);
                //it removes user from his NOT only db but from his access completely
                userService.deleteUserByUserName(callbackData);

                log.info("User {} was deleted from DB", userName);

                reply = replyMessageService.getReplyMessage(
                        chatId,
                        replyMessageService.getLocaleText("reply.admin.remove.user"),
                        keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons,listOfBQueries));
                break;
            }
        }
        replyMessageService.deleteMessage(chatId,callBackMessageId);
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        //merge two lists to remove user from list
        List<String> listOfQueriesRegisterUsers = userService.getAllRegisteredUsers();
        List<String> listOfQueries = List.of(StaticQueries.ADMIN.getValue(),
                StaticQueries.ADMINSERVICE.getValue(),
                StaticQueries.ADDUSER.getValue(),
                StaticQueries.LISTOFUSERS.getValue());

        return callbackQueriesService
                .mergeTwoListOfQueries(listOfQueriesRegisterUsers, listOfQueries);
    }
}
