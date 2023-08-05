package com.matveyvs.chitchatbot.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class KeyboardService {
    public InlineKeyboardMarkup getInlineKeyboard(List<String> listOfButtons, List<String> listOfQueries){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        if (listOfButtons.size() != listOfQueries.size()){
            log.error("The lists size of Buttons and Queries is not equal!");
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (int i = 0; i < listOfButtons.size(); i++){
            InlineKeyboardButton loginButton = new InlineKeyboardButton(listOfButtons.get(i));
            loginButton.setCallbackData(listOfQueries.get(i));
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(loginButton);
            buttons.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
    //todo try to remove duplicated code
    public InlineKeyboardMarkup getInlineKeyboard(List<String> userNames){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> usersList = new ArrayList<>();
        for (String userName : userNames) {
            InlineKeyboardButton loginButton = new InlineKeyboardButton(userName);
            loginButton.setCallbackData(userName);
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(loginButton);
            usersList.add(row);
        }
        InlineKeyboardButton returnButton = new InlineKeyboardButton("Return to ADMIN service");
        returnButton.setCallbackData("adminservice");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(returnButton);
        usersList.add(row);

        inlineKeyboardMarkup.setKeyboard(usersList);
        return inlineKeyboardMarkup;
    }
    //for create inline buttons 5 in row max
    public InlineKeyboardMarkup getInlineKeyboardForButtons(Integer amountOfButtons, String className){
        int amountOfRows = amountOfButtons/5 + 1;
        //remainder of division
        int remainder = amountOfButtons%5;
        String nameOfButton = "";
        int numberOfButton = 0;
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < amountOfRows; i++){
            List<InlineKeyboardButton> row = new ArrayList<>();
            if (amountOfButtons >=5){
                for (int counter = 0; counter < 5; counter++){
                    numberOfButton++;
                    nameOfButton = className + numberOfButton;
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setCallbackData(nameOfButton);
                    inlineKeyboardButton.setText(String.valueOf(numberOfButton));
                    row.add(inlineKeyboardButton);
                    amountOfButtons--;
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
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup mergeTwoInlineKeyboards(InlineKeyboardMarkup inlineKeyboardMarkupMain,
                                                        InlineKeyboardMarkup inlineKeyboardMarkupAddition){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        var keyboardMain = inlineKeyboardMarkupMain.getKeyboard();
        var keyboardAddition = inlineKeyboardMarkupAddition.getKeyboard();

        List<List<InlineKeyboardButton>> mergedKeyboard = new ArrayList<>(keyboardMain);
        mergedKeyboard.addAll(keyboardAddition);
        inlineKeyboardMarkup.setKeyboard(mergedKeyboard);
        return inlineKeyboardMarkup;
    }
}