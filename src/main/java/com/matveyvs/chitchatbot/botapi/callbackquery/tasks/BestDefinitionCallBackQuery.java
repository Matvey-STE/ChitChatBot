package com.matveyvs.chitchatbot.botapi.callbackquery.tasks;

import com.matveyvs.chitchatbot.botapi.callbackquery.CallbackQueryHandler;
import com.matveyvs.chitchatbot.entity.BestDefinition;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.UpdateDBService;
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
    private final UpdateDBService updateDBService;
    private List<String> callBackQueriesList = new ArrayList<>();
    private final BestDefinitionService bestDefinitionService;
    private final ReplyMessageService replyMessageService;
    private int start = 1;

    public BestDefinitionCallBackQuery(UpdateDBService updateDBService, BestDefinitionService bestDefinitionService, ReplyMessageService replyMessageService) {
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

        if (callbackData.equals("bestdefinitiontask")){
            //attempt to connect to DB
            BestDefinition bestDefinition = bestDefinitionService.findBestDefinitionTaskById(1);
            if (bestDefinition == null){
                updateDBService.updateAllRepository();
                log.error("There is no objects from DB, updated from USER!");
            }
            bestDefinition = bestDefinitionService.findBestDefinitionTaskById(start);
                String wordOfTask = bestDefinition.getTaskWord();
                List<String> listOfAnswers = bestDefinition.getListOfAnswers();
                callBackQueriesList = createButtonQueries(className, listOfAnswers.size());
                start++;
            reply = replyMessageService
                        .getReplyMessage(chatId, createMessageTaskConstructor(wordOfTask, listOfAnswers),
                                createTaskMessageWithButtons(listOfAnswers));
        }
        if (callbackData.contains(className)){
            int rightButton = bestDefinitionService.getIndexOfRightAnswer(1);
            if (callbackData.contains(String.valueOf(rightButton))){
                reply = replyMessageService.sendAnswerCallbackQuery("Right answer",true, callbackQuery);
                log.info("Delete message before creation");
                replyMessageService.deleteMessage(chatId,callBackMessageId);
            } else {
                reply = replyMessageService.sendAnswerCallbackQuery("Wrong answer",true, callbackQuery);
            }
        }
        return reply;
    }
    @Override
    public List<String> getHandlerQueryType() {
        List<String> listOfQueries = new ArrayList<>(callBackQueriesList);
        listOfQueries.add("bestdefinitiontask");
        return listOfQueries;
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
