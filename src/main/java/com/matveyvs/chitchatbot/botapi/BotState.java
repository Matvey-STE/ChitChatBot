package com.matveyvs.chitchatbot.botapi;

public enum BotState {

    START,
    ADMINPASSWORD,
    ADDUSER, ADDADMIN,
    LISTOFADMINS, LISTOFUSERS,
    HELP,
    TEST,



    NONE,
    ADMIN,
    DELETEUSER, UPDATEDATA, ADMINHELP,
    USER, USER_HELP;
    //method that return BotState by Integer
    public static BotState getValueByInteger(int botState) {
        for (BotState state : BotState.values()) {
            if (state.ordinal() == botState) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for the BotState enum.");
    }
}
