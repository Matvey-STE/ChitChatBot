package com.matveyvs.chitchatbot.enums;

public enum Queries {

    ADMIN ("admin"),
    USER ("user"),

    START ("start"),
    LOGIN ("login"),
    ADMINSERVICE ("adminservice"),
    ADDUSER ("adduser"),
    PASSWORD ("password"),
    LISTOFUSERS ("listofusers"),
    BESTDEFINITIONTASK ("bestdefinitiontask");

    private final String value;

    Queries(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
