package com.matveyvs.chitchatbot.botapi.handlers;

import com.matveyvs.chitchatbot.enums.BotState;
import com.matveyvs.chitchatbot.entity.BestDefinition;
import com.matveyvs.chitchatbot.service.ReplyMessageService;
import com.matveyvs.chitchatbot.service.taskservices.BestDefinitionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO TESTS DIFFERENT APPROACHES
@Log4j2
@Component
public class TestCommandHandler implements InputMessageHandler{
    private final List<String> callBackQueriesList = new ArrayList<>();
    private final BestDefinitionService bestDefinitionService;
    private final ReplyMessageService replyMessageService;

    public TestCommandHandler(BestDefinitionService bestDefinitionService, ReplyMessageService replyMessageService) {
        this.bestDefinitionService = bestDefinitionService;
        this.replyMessageService = replyMessageService;
    }

    public List<String> getCallBackQueriesList() {
        return callBackQueriesList;
    }

    private InlineKeyboardMarkup createTaskMessageWithButtons(List<String> listOfAnswers){
        if (!callBackQueriesList.isEmpty()){
            callBackQueriesList.clear();
        }
        int listSize = listOfAnswers.size();
        int amountOfRows = listSize/5 + 1;
        //remainder of division
        int remainder = listSize%5;
        String nameOfButton = "";
        int numberOfButton = 0;
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < amountOfRows; i++){
            List<InlineKeyboardButton> row = new ArrayList<>();

            if (listSize >=5){
                for (int counter = 0; counter < 5; counter++){
                    numberOfButton++;
                    nameOfButton = "testButton" + numberOfButton;
                    callBackQueriesList.add(nameOfButton);
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setCallbackData(nameOfButton);
                    inlineKeyboardButton.setText(String.valueOf(numberOfButton));
                    row.add(inlineKeyboardButton);
                    listSize--;
                }
            } else {
                for (int counter = 0; counter < remainder; counter++){
                    numberOfButton++;
                    nameOfButton = "testButton " + numberOfButton;
                    callBackQueriesList.add(nameOfButton);
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
    private String createMessageTaskConstructor(String word, List<String> listOfAnswers){
        StringBuilder builder = new StringBuilder(replyMessageService.getLocaleText("reply.test.message"));
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
    @Override
    public SendMessage handle(Message message) {
        createAndSaveBestDefinitionTest();

        BestDefinition bestDefinition = bestDefinitionService.findBestDefinitionTaskById(1);
        String wordOfTask = bestDefinition.getTaskWord();
        List<String> listOfAnswers = bestDefinition.getListOfAnswers();
        SendMessage reply;
        Integer messageId = message.getMessageId();
        long chatId = message.getChatId();
        reply = new SendMessage();
        reply.setChatId(chatId);
        reply.enableHtml(true);
        reply.setText(createMessageTaskConstructor(wordOfTask,listOfAnswers));
        reply.setReplyMarkup(createTaskMessageWithButtons(listOfAnswers));

        replyMessageService.deleteMessage(chatId,messageId);

        return reply;
    }
    @Override
    public BotState getHandlerName() {
        return BotState.TEST;
    }
    // testing database creation new entity in db
    private void createAndSaveBestDefinitionTest(){
        String word = "soak (v.)";
        String rightAnswer = "leave something in liquid, especially to clean it or make it softer";
        List<String> stringList = new ArrayList<>();
        stringList.add("remove liquid from the surface by pressing a piece of soft paper or cloth onto it");
        stringList.add("use water to clean the soap or dirt from something");
        stringList.add("leave something in liquid, especially to clean it or make it softer");
        Collections.shuffle(stringList);
        BestDefinition bestDefinition = new BestDefinition(word,rightAnswer,3,stringList);

        bestDefinition.setIndexOfRightAnswer(indexOfRightAnswer(rightAnswer, stringList));

        bestDefinitionService.saveBestDefinitionTask(bestDefinition);
    }
    private int indexOfRightAnswer(String rightAnswer, List<String> listOfAnswers){
        return listOfAnswers.indexOf(rightAnswer) + 1;
    }


}
