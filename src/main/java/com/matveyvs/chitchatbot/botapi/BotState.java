package com.matveyvs.chitchatbot.botapi;

public enum BotState {
    NONE,

    START_MESSAGE, ADMIN_PASSWORD, HELP,

    ADMIN, CREATEUSER, DELETEUSER, UPDATEDATA, ADMINHELP, LISTOFADMINS, LISTOFUSERS, ADMIN_HELP,

    USER, USER_HELP,

    TEST;
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
