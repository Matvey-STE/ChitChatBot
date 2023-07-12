package com.matveyvs.chitchatbot.tasklogic;

import java.util.ArrayList;
import java.util.List;

public class BestDefinition {
    Long id;
    String word;
    List<String> possibleAnswers = new ArrayList<>();

    public BestDefinition(Long id, String word, String answer, List<String> possibleAnswers) {
        this.id = id;
        this.word = word;
        this.possibleAnswers = possibleAnswers;
    }
}
