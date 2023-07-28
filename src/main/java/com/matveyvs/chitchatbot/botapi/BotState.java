package com.matveyvs.chitchatbot.botapi;

public enum BotState {
    NONE,
    ADMIN, CREATEUSER, DELETEUSER, UPDATEDATA, ADMINHELP, LISTOFADMINS, LISTOFUSERS,
    USER, USER_HELP,
    ADMIN_PASSWORD, HELP,
    START,
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
