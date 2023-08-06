package com.matveyvs.chitchatbot.enums;

public enum StaticQueries {

    ADMIN ("admin"),
    USER ("user"),

    START ("start"),
    LOGIN ("login"),
    ADMINSERVICE ("adminservice"),
    ADDUSER ("adduser"),
    PASSWORD ("password"),
    LISTOFUSERS ("listofusers"),
    BESTDEFINITIONTASK ("bestdefinitiontask"),
    UPDATEDATA ("updatedata");


    private final String value;

    StaticQueries(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
