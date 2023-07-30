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
}