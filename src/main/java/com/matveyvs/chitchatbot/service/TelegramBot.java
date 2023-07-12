package com.matveyvs.chitchatbot.service;

import com.matveyvs.chitchatbot.config.BotConfig;
import com.matveyvs.chitchatbot.service.enums.Condition;
import com.matveyvs.chitchatbot.service.enums.TGCommands;
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
    private final StaticMessages staticMessages;
    public boolean passwordMessage = false;
    public static Map<Long, String> admins = new HashMap<>();
    public static Set<String> users = new HashSet<>();

    public TelegramBot(BotConfig botConfig, StaticMessages staticMessages) {
        this.botConfig = botConfig;
        this.staticMessages = staticMessages;
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

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = message.getText();
            String name = update.getMessage().getChat().getFirstName();
            //condition check // todo add feature to using tg bot for admin and not to lose his username
            // 0 - for no accept, 1 - for admins, 2 - for users;
            Condition registerCondition;
            if (isUser(nameOfUser)) {
                registerCondition = Condition.USER;
                System.out.println("user access");
            } else if (isAdmin(chatId)) {
                registerCondition = Condition.ADMIN;
                System.out.println("admin access");
            } else {
                registerCondition = Condition.UNREGISTERED;
                System.out.println("no access");
            }

            switch (registerCondition) {
                case UNREGISTERED -> {
                    if (passwordMessage && text.equals(botConfig.getAdminPassword()) && !admins.containsKey(chatId)) {
                        sendMessage(chatId, "Now you have access to this bot as admin please use /adminhelp " +
                                "to get all possible commands");
                        admins.put(chatId, update.getMessage().getChat().getUserName());
                        passwordMessage = false;
                    } else if (passwordMessage) {
                        passwordMessage = false;
                        sendMessage(chatId, "Password was wrong, please use /help to choose the right command!");
                    } else {
                        if (isEnumValueInList(text, getListOfPossibleCommands())) {
                            switch (getTgCommands(text)) {
                                case START -> {
                                    sendMessage(chatId, "Hello " + name + "  you're unregister user.\n" +
                                            "Your user name is " + nameOfUser + "\n" +
                                            "The purpose of this bot is learn you foreign language");
                                    sendMessage(chatId, "Please choose your rights, are you /admin or /user");
                                }
                                case ADMIN -> {
                                    sendMessage(chatId, "Please write password");
                                    passwordMessage = true;
                                }
                                case USER -> sendMessage(chatId, "Please ask ADMIN to give you access to this bot");
                                case HELP -> sendMessage(chatId, staticMessages.getHelpMessage());
                                default -> sendMessage(chatId, staticMessages.getUnsupportedMessage() + "/help");
                            }
                        } else {
                            sendMessage(chatId, staticMessages.getUnsupportedMessage() + "/help");
                        }
                    }
                }
                case ADMIN -> {
                    if (passwordMessage && admins.containsKey(chatId)) {
                        users.add(text);
                        if (users.contains(nameOfUser)) {
                            sendMessage(chatId, "Hello, " + nameOfUser + " you're in USER MENU" +
                                    " now, please use /userhelp");
                        } else {
                            sendMessage(chatId, "User with the name " + text + " has been added to system");
                        }
                        passwordMessage = false;
                    } else {
                        if (isEnumValueInList(text, getListOfPossibleCommands())) {
                            switch (getTgCommands(text)) {
                                case CREATEUSER -> {
                                    sendMessage(chatId, "Please write user name");
                                    passwordMessage = true;
                                }
                                case DELETEUSER -> sendMessage(chatId, "Delete user here");
                                case LISTOFADMINS -> {
                                    sendMessage(chatId, "The list of ADMINS now:");
                                    printAdmins(chatId, admins);
                                }
                                case LISTOFUSERS -> {
                                    if (users.isEmpty()) {
                                        sendMessage(chatId, "There is no possible users now");
                                    } else {
                                        sendMessage(chatId, "The list of USERS now:");
                                        printUsers(chatId, users);
                                    }
                                }
                                case ADMINHELP -> sendMessage(chatId, staticMessages.getAdminHelpMessage());
                                case EXIT -> {
                                    sendMessage(chatId, "You're in main menu press /help " +
                                            "to get possible messages");
                                    // not to get password message after remove admin
                                    passwordMessage = false;
                                }
                                default -> sendMessage(chatId, staticMessages.getUnsupportedMessage() + "/adminhelp");
                            }
                        } else {
                            sendMessage(chatId, staticMessages.getUnsupportedMessage() + "/adminhelp");
                        }
                    }
                }
                case USER -> {
                    if (isEnumValueInList(text, getListOfPossibleCommands())) {
                        switch (getTgCommands(text)) {
                            case USERHELP -> sendMessage(chatId, staticMessages.getUserHelpMessage());
                            case CHOOSETASK -> sendMessage(chatId, "User CAN CHOOSE TASK HERE");
                            case EXIT -> {
                                if (!admins.containsKey(chatId)) {
                                    sendMessage(chatId, "You can't exit from this menu, only for ADMINS");
                                } else {
                                    sendMessage(chatId, "Hello ADMIN now you're in ADMIN menu, please use /adminhelp");
                                    users.remove(nameOfUser);
                                }
                            }
                            default -> sendMessage(chatId, staticMessages.getUnsupportedMessage() + "/userhelp");
                        }
                    } else {
                        sendMessage(chatId, staticMessages.getUnsupportedMessage() + "/userhelp");
                    }
                }
            }
        } else {
            sendMessage(chatId, staticMessages.getUnsupportedMessage() + "/help");
        }
    }

    //convert command to enum
    private static TGCommands getTgCommands(String command) {
        return TGCommands.valueOf(command.substring(1).toUpperCase());
    }


    private boolean isAdmin(Long chatId) {
        return admins.containsKey(chatId);
    }

    private boolean isUser(String name) {
        return users.contains(name);
    }

    //send message
    private void sendMessage(long chartId, String textToSend) {
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
    private void printAdmins(Long chatId, Map<Long, String> people) {
        people.values().forEach(e -> sendMessage(chatId, e));
    }

    //print all possible users
    private void printUsers(Long chatId, Set<String> people) {
        people.forEach(e -> sendMessage(chatId, e));
    }
    // todo probably create enums for each condition like USER, ADMIN, UNREGISTERED
    // todo or check it earlier
    public static List<String> getListOfPossibleCommands() {
        List<String> strings = new ArrayList<>();
        for (TGCommands command : TGCommands.values()) {
            strings.add(command.toString());
        }
        return strings;
    }
    public static boolean isEnumValueInList(String text, List<String> enumList) {
        String result = text.substring(1).toUpperCase();
        return enumList.contains(result);
    }

}
