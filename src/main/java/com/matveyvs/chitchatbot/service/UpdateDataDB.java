package com.matveyvs.chitchatbot.service;

import com.matveyvs.chitchatbot.entity.BestDefinition;
import com.matveyvs.chitchatbot.service.taskservices.BestDefinitionService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UpdateDataDB {
    private final GoogleSheetsService googleSheetsService;
    private final BestDefinitionService bestDefinitionService;

    public UpdateDataDB(GoogleSheetsService googleSheetsService, BestDefinitionService bestDefinitionService) {
        this.googleSheetsService = googleSheetsService;
        this.bestDefinitionService = bestDefinitionService;
    }

    public void updateBestDefinitionGoogleSheet(){
        List<List<String>> lineGoogleSheet =
                googleSheetsService.getLineGoogleSheet(SheetNames.BEST_DEFINITION.getValue());
        for (List<String> line: lineGoogleSheet){
            BestDefinition bestDefinition = new BestDefinition();
            bestDefinition.setTaskWord(line.get(0));
            bestDefinition.setRightAnswer(line.get(1));

            List<String> listOfAnswers = new ArrayList<>();
            listOfAnswers.add(line.get(1));
            listOfAnswers.add(line.get(2));
            listOfAnswers.add(line.get(3));
            Collections.shuffle(listOfAnswers);

            bestDefinition.setIndexOfRightAnswer(indexOfRightAnswer(line.get(1), listOfAnswers));

            bestDefinition.setListOfAnswers(listOfAnswers);

            bestDefinitionService.saveBestDefinitionTask(bestDefinition);
        }
    }
    private int indexOfRightAnswer(String rightAnswer, List<String> listOfAnswers){
        return listOfAnswers.indexOf(rightAnswer) + 1;
    }
}
