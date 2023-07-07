package com.matveyvs.ChitChatBot.service;

import org.springframework.stereotype.Component;
@Component
public class StaticMessages {

    public String getAdminHelpMessage() {
        return """
                Hello admin, possible commands are:
                /createuser - add new user to the system
                /deleteuser - delete a user from the system
                /listofadmins - show all admins registered in the system
                /listofusers - show all users with access to the system
                /adminhelp - list all possible commands for admin
                /exit - exit to the main menu
                /help - get access to help""";
    }
    public String getHelpMessage() {
        return """
                This bot is used for learning new things
                /start - starting message, not working if you're /admin or /user
                /admin - get rights by entering the password
                /user - admins can give access to this bot
                /help - general information for this bot""";
    }
    public String getUnsupportedMessage(){
        return "Unsupported message, use /help";
    }

}
