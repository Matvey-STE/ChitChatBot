package com.matveyvs.chitchatbot.enums;

public enum SheetNames {
    BEST_DEFINITION ("best_definition!B2:F"),
    WRITE_ANSWER("write_answer!B2:C"),
    TYPES_ANSWERS("types_answers!A1:C");
    private final String listRange;

    SheetNames(String listRange) {
        this.listRange = listRange;
    }
    public String getValue() {
        return listRange;
    }
}
