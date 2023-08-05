package com.matveyvs.chitchatbot.botapi.callbackquery.admin;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.entity.repository.TaskSetsForDayRapository;
import com.matveyvs.chitchatbot.enums.Queries;
import com.matveyvs.chitchatbot.service.CallbackQueriesService;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UpdateDBService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
@Component
public class UpdateCallbackQuery implements CallbackQueryHandler {
    private final CallbackQueriesService callbackQueriesService;
    private final TaskSetsForDayRapository taskSetsForDayRapository;
    private final KeyboardService keyboardService;
    private final ReplyMessageService replyMessageService;
    private final UpdateDBService updateDBService;

    public UpdateCallbackQuery(CallbackQueriesService callbackQueriesService,
                               TaskSetsForDayRapository taskSetsForDayRapository,
                               KeyboardService keyboardService,
                               ReplyMessageService replyMessageService,
                               UpdateDBService updateDBService) {
        this.callbackQueriesService = callbackQueriesService;
        this.taskSetsForDayRapository = taskSetsForDayRapository;
        this.keyboardService = keyboardService;
        this.replyMessageService = replyMessageService;
        this.updateDBService = updateDBService;
    }
    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage reply = null;
        Integer callBackMessageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        if (callbackData.equals(Queries.UPDATEDATA.getValue())){
            updateDBService.updateAllRepository();

            Integer amountOfTypesTasks = taskSetsForDayRapository.countAllBy();

            List<String> listOfButtons = List
                    .of("Return to ADMIN SERVICE");
            List<String> listOfBQueries =
                    List.of(Queries.ADMINSERVICE.getValue());

            var inlineKeyboardMarkupMain = keyboardService
                    .getInlineKeyboardForButtons(amountOfTypesTasks, this.getClass().getSimpleName());
            var inlineKeyboardMarkupAddition = keyboardService.getInlineKeyboard(listOfButtons, listOfBQueries);

            InlineKeyboardMarkup inlineKeyboardMarkup = keyboardService
                    .mergeTwoInlineKeyboards(inlineKeyboardMarkupMain, inlineKeyboardMarkupAddition);

            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getLocaleText("reply.data.updated"));
            reply.setParseMode("HTML");
            reply.setReplyMarkup(inlineKeyboardMarkup);

            replyMessageService.deleteMessage(chatId,callBackMessageId);
        }
        if (callbackData.contains(this.getClass().getSimpleName())){

            List<String> listOfButtons = List
                    .of("Return to ADMIN SERVICE");
            List<String> listOfBQueries =
                    List.of(Queries.ADMINSERVICE.getValue());
            var inlineKeyboardMarkupAddition = keyboardService.getInlineKeyboard(listOfButtons, listOfBQueries);

            reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(replyMessageService.getLocaleText("reply.buttons.update.message"));
            reply.setParseMode("HTML");
            reply.setReplyMarkup(inlineKeyboardMarkupAddition);
        }
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        Integer amountOfTypesTasks = taskSetsForDayRapository.countAllBy();
        List<String> value = List.of(Queries.UPDATEDATA.getValue());
        List<String> listOfQueries = callbackQueriesService.createListOfQueries(amountOfTypesTasks, this.getClass().getSimpleName());

        return callbackQueriesService.mergeTwoListOfQueries(value,listOfQueries);
    }
}
