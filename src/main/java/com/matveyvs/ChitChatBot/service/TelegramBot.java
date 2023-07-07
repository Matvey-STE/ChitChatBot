package com.matveyvs.ChitChatBot.service;

import com.matveyvs.ChitChatBot.config.BotConfig;
import com.matveyvs.ChitChatBot.service.enums.Condition;
import com.matveyvs.ChitChatBot.service.enums.TGCommands;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component //create instance of class automatically
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;
    public boolean passwordMessage = false;
    public static Map<Long,String> admins = new HashMap<>();
    public static Set<String> users = new HashSet<>();

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
    @Override
    public String getBotToken() {
        return botConfig.getBotKey();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String nameOfUser = message.getChat().getUserName();

        if (update.hasMessage() && update.getMessage().hasText()){
            String text = message.getText();
            String name = update.getMessage().getChat().getFirstName();
            //condition check
            // 0 - for no accept, 1 - for admins, 2 - for users;
            Condition registerCondition;
            if (isAdmin(chatId)){
                registerCondition = Condition.ADMIN;
                System.out.println("admin access");
            } else if (isUser(nameOfUser)){
                registerCondition = Condition.USER;
                System.out.println("user access");
            } else {
                registerCondition = Condition.UNREGISTERED;
                System.out.println("no access");
            }

            //menu based on condition
            try{
            switch (registerCondition){
                    case UNREGISTERED -> {
                        if (passwordMessage && text.equals(botConfig.getAdminPassword()) && !admins.containsKey(chatId)) {
                            sendMessage(chatId, "Now you have access to this bot as admin please use /adminhelp " +
                                    "to get all possible commands");
                            admins.put(chatId, update.getMessage().getChat().getUserName());
                        } else if (passwordMessage) {
                            passwordMessage = false;
                            sendMessage(chatId, "Password was wrong, please user /help to choose the right command!");
                        } else {
                            switch (getTgCommands(text)) {
                                case START -> {
                                    sendMessage(chatId, "Hello " + name + "  you're unregister user. The purpose of this bot is learn you foreign language");
                                    sendMessage(chatId, "Please choose your rights, are you /admin or /user");
                                }
                                case ADMIN -> {
                                    sendMessage(chatId, "Please write password");
                                    passwordMessage = true;
                                }
                                case USER -> sendMessage(chatId, "Please ask admin to give you access to this bot");
                                case HELP -> sendMessage(chatId, getHelpMessage());
                                default -> sendMessage(chatId, "Unsupported message");
                            }
                        }
                    }
                    case ADMIN -> {
                        switch (getTgCommands(text)) {
                            case CREATEUSER -> sendMessage(chatId, "You can create user here");
                            case DELETEUSER -> sendMessage(chatId, "Delete user here");
                            case LISTOFADMINS -> {
                                sendMessage(chatId,"The list of ADMINS now:");
                                printAdmins(chatId, admins);
                            }
                            case LISTOFUSERS -> {
                                if (users.isEmpty()){
                                    sendMessage(chatId,"There is no possible users now");
                                } else {
                                    sendMessage(chatId,"The list of USERS now:");
                                    printUsers(chatId, users);
                                }
                            }
                            case ADMINHELP -> sendMessage(chatId, getAdminHelpMessage());
                        }
                    }
                    case USER -> {
                        sendMessage(chatId, "Hello user possible commands here /userhelp and /help");
                        switch (getTgCommands(text)) {
                            case USERHELP -> sendMessage(chatId, "User help message");
                            case HELP -> sendMessage(chatId, getHelpMessage());
                        }
                    }
                }
            } catch (Exception e) {
                sendMessage(chatId, "Unknown command, use /help to find the right one");
            }
        } else {
            sendMessage(chatId, "Unsupported message!");
        }
    }

    //convert command to enum
    private static TGCommands getTgCommands(String command){
        return TGCommands.valueOf(command.substring(1).toUpperCase());
    }
    //create help message
    private static String getHelpMessage(){
        return """
                This bot using for learning new thigs
                /start - starting message, not workin if you're /admin or /user
                /admin - to get rights you should know the password
                /user - admin can give the access for this bot
                /help - general information for this bot""";
    }
    private static String getAdminHelpMessage(){
        return """
                Hello admin possible commands are
                /createuser - add new user to system
                /deleteuser - delete user from system
                /listofadmins - show all admins that register in system
                /listofusers - show all users that have accees to system
                /adminhelp - all possible commands for admin
                /help - to get access to""";
    }

    private boolean isAdmin (Long chatId){
        return admins.containsKey(chatId);
    }
    private boolean isUser (String name){
        return users.contains(name);
    }
    //send message
    private void sendMessage(long chartId, String textToSend){
        SendMessage message = new SendMessage();
        message.setText(textToSend);
        message.setChatId(String.valueOf(chartId));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    // print all possible admins
    private void printAdmins(Long chatId, Map<Long, String> people){
        people.values().forEach(e -> sendMessage(chatId, e));
    }
    //print all possible users
    private void printUsers(Long chatId, Set<String> people){
        people.forEach(e -> sendMessage(chatId, e));
    }
}
