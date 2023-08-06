package com.matveyvs.chitchatbot.botapi.callbackquery.tasks;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.entity.BestDefinition;
import com.matveyvs.chitchatbot.enums.StaticQueries;
import com.matveyvs.chitchatbot.service.*;
import com.matveyvs.chitchatbot.service.taskservices.BestDefinitionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Log4j2
@Component
public class BestDefinitionCallBackQuery implements CallbackQueryHandler {
    private final CallbackQueriesService callbackQueriesService;
    private final UserService userService;
    private final KeyboardService keyboardService;
    private final UpdateDBService updateDBService;
    private List<String> callBackQueriesList = new ArrayList<>();
    private final BestDefinitionService bestDefinitionService;
    private final ReplyMessageService replyMessageService;

    public BestDefinitionCallBackQuery(CallbackQueriesService callbackQueriesService,
                                       UserService userService,
                                       KeyboardService keyboardService,
                                       UpdateDBService updateDBService,
                                       BestDefinitionService bestDefinitionService,
                                       ReplyMessageService replyMessageService) {
        this.callbackQueriesService = callbackQueriesService;
        this.userService = userService;
        this.keyboardService = keyboardService;
        this.updateDBService = updateDBService;
        this.bestDefinitionService = bestDefinitionService;
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
        int taskCondition = userService.getUserTaskConditionBestDefinition(chatId);
        if (callbackData.equals(StaticQueries.BESTDEFINITIONTASK.getValue()) && taskCondition != 0){
            //attempt to connect to DB
            BestDefinition bestDefinition = bestDefinitionService.findBestDefinitionTaskById(1);
            if (bestDefinition == null){
                log.error("There is no objects from DB, updated from USER!");
                updateDBService.updateAllRepository();
                List<String> listOfButtons =
                        List.of("Return to USER");
                List<String> listOfBQueries =
                        List.of(StaticQueries.USER.getValue());

                reply = replyMessageService.getReplyMessage(
                        chatId,
                        replyMessageService.getLocaleText("reply.update.DB"),
                        keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons,listOfBQueries));
                replyMessageService.deleteMessage(chatId,callBackMessageId);
            } else {
                bestDefinition = bestDefinitionService.findBestDefinitionTaskById(taskCondition);
                System.out.println(bestDefinition);
                String wordOfTask = bestDefinition.getTaskWord();
                List<String> listOfAnswers = bestDefinition.getListOfAnswers();
                callBackQueriesList = createButtonQueries(className, listOfAnswers.size());

                reply = replyMessageService.getReplyMessage(
                        chatId,
                        createMessageTaskConstructor(wordOfTask, listOfAnswers),
                        createTaskMessageWithButtons(listOfAnswers));
            }
        } else if (callbackData.equals(StaticQueries.BESTDEFINITIONTASK.getValue()) && taskCondition == 0){
            List<String> listOfButtons =
                    List.of("Return to START");
            List<String> listOfBQueries =
                    List.of(StaticQueries.START.getValue());

            reply = replyMessageService.getReplyMessage(
                    chatId,
                    replyMessageService.getLocaleText("reply.best.definition.done"),
                    keyboardService.getInlineKeyboardButtonsAndQueries(listOfButtons,listOfBQueries));

        }
        if (callbackData.contains(className)){
            int rightButton = bestDefinitionService.getIndexOfRightAnswer(taskCondition);
            System.out.println(rightButton);
            if (callbackData.contains(String.valueOf(rightButton))){
                List<String> buttons= List.of("Continue");
                List<String> listOfQueries = List.of(StaticQueries.BESTDEFINITIONTASK.getValue());

                reply = replyMessageService.getReplyMessage(
                        chatId,
                        replyMessageService.getLocaleText("reply.right.best.definition.answer"),
                        keyboardService.getInlineKeyboardButtonsAndQueries(buttons,listOfQueries));

                log.info("Delete message before creation class {} ",this.getClass().getSimpleName());

                taskCondition--;
                userService.saveConditionBestDefinition(chatId, taskCondition);
                replyMessageService.deleteMessage(chatId,callBackMessageId);
            } else {
                reply = replyMessageService.getAnswerCallbackQuery("Wrong answer",true, callbackQuery);
            }
        }

        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        List<String> listOfQueries = new ArrayList<>(callBackQueriesList);
        List<String> queries = List.of(StaticQueries.BESTDEFINITIONTASK.getValue());
        List<String> mergedTwoListsQueries = callbackQueriesService.mergeTwoListOfQueries(listOfQueries, queries);
        return mergedTwoListsQueries;
    }
    private String createMessageTaskConstructor(String word, List<String> listOfAnswers){
        StringBuilder builder = new StringBuilder("<b>Testing message</b>\n" +
                "Please choose the right answer for the word");
        builder.append("\n\n <b>")
                .append(word.toUpperCase())
                .append("</b> \n\n");
        for (int i = 0; i < listOfAnswers.size(); i++){
            builder.append(i + 1)
                    .append(") ")
                    .append(listOfAnswers.get(i))
                    .append("\n\n");
        }
        return builder.toString();
    }
    private List<String> createButtonQueries(String nameOfQuery, int amountOfButtons){
        List<String> listOfQueries = new ArrayList<>();
        for (int i = 1; i <= amountOfButtons; i++){
            listOfQueries.add(nameOfQuery + i);
        }
        return listOfQueries;
    }
    private InlineKeyboardMarkup createTaskMessageWithButtons(List<String> listOfAnswers){
        int listSize = listOfAnswers.size();

        int amountOfRows = listSize/5 + 1;
        //remainder of division
        int remainder = listSize%5;
        String nameOfButton = "";
        int numberOfButton = 0;
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < amountOfRows; i++){
            List<InlineKeyboardButton> row = new ArrayList<>();
            String className = this.getClass().getSimpleName();
            if (listSize >=5){
                for (int counter = 0; counter < 5; counter++){
                    numberOfButton++;
                    nameOfButton = className + numberOfButton;
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setCallbackData(nameOfButton);
                    inlineKeyboardButton.setText(String.valueOf(numberOfButton));
                    row.add(inlineKeyboardButton);
                    listSize--;
                }
            } else {
                for (int counter = 0; counter < remainder; counter++){
                    numberOfButton++;
                    nameOfButton = className + numberOfButton;
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setCallbackData(nameOfButton);
                    inlineKeyboardButton.setText(String.valueOf(numberOfButton));
                    row.add(inlineKeyboardButton);
                }
            }
            rows.add(row);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rows);
        listOfAnswers.clear();
        return inlineKeyboardMarkup;
    }
}
