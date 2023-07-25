package com.matveyvs.chitchatbot.botapi;

public enum BotState {
    START_MESSAGE,
    ADMIN_PASSWORD,

    ADMIN,
    CREATEUSER,
    DELETEUSER,
    UPDATEDATA,
    ADMINHELP,
    LISTOFADMINS,
    LISTOFUSERS,

    USER,
    HELP,

    ADMIN_HELP,
    USER_HELP,
    NONE;
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
