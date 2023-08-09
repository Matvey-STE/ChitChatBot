package com.matveyvs.chitchatbot.botapi.callbackquery.tasks;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.enums.StaticQueries;
import com.matveyvs.chitchatbot.repository.TaskSetsForDayRepository;
import com.matveyvs.chitchatbot.service.KeyboardService;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
@Log4j2
@Component
public class TaskTypesCallbackQuery implements CallbackQueryHandler {
    private final KeyboardService keyboardService;
    private final TaskSetsForDayRepository taskSetsForDayRepository;
    private final ReplyMessageService replyMessageService;

    public TaskTypesCallbackQuery(KeyboardService keyboardService,
                                  TaskSetsForDayRepository taskSetsForDayRepository,
                                  ReplyMessageService replyMessageService) {
        this.keyboardService = keyboardService;
        this.taskSetsForDayRepository = taskSetsForDayRepository;
        this.replyMessageService = replyMessageService;
    }
    @Override
    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery) {
        BotApiMethod<?> reply = null;
        Message message = callbackQuery.getMessage();
        Integer callBackMessageId = message.getMessageId();
        Long chatId = message.getChatId();
        String callbackData = callbackQuery.getData();
//        use it for queries creation and if class changed
        String className = this.getClass().getSimpleName();
        if (callbackData.equals(StaticQueries.TASKTYPESFORDAY.getValue())){
            List<String> listOfButtons =
                    List.of("Return to USER");
            List<String> listOfBQueries =
                    List.of(StaticQueries.USER.getValue());

            Integer amountOfTypesTasks = taskSetsForDayRepository.countAllBy();
            var inlineKeyboardMarkupMain =
                    keyboardService.getInlineKeyboardNumberButtons(amountOfTypesTasks, className);
            var inlineKeyboardMarkupAddition =
                    keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons, listOfBQueries);

            InlineKeyboardMarkup inlineKeyboardMarkup = keyboardService
                    .mergeTwoInlineKeyboards(inlineKeyboardMarkupMain, inlineKeyboardMarkupAddition);

            reply = replyMessageService.getReplyMessage(
                    chatId,
                    "Menu is not ready yet",
                    inlineKeyboardMarkup);
            log.info("Message removed after creation " + className);
            replyMessageService.deleteMessage(chatId, callBackMessageId);
        }
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        return List.of(StaticQueries.TASKTYPESFORDAY.getValue());
    }
}
