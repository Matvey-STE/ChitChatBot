package com.matveyvs.chitchatbot.service;

import com.matveyvs.chitchatbot.entity.BestDefinition;
import com.matveyvs.chitchatbot.entity.TaskSetsForDay;
import com.matveyvs.chitchatbot.repository.TaskSetsForDayRepository;
import com.matveyvs.chitchatbot.enums.SheetNames;
import com.matveyvs.chitchatbot.service.taskservices.BestDefinitionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Log4j2
@Component
public class UpdateDBService {
    private final TaskSetsForDayRepository taskSetsForDayRepository;
    private final GoogleSheetsService googleSheetsService;
    private final BestDefinitionService bestDefinitionService;

    public UpdateDBService(TaskSetsForDayRepository taskSetsForDayRepository, GoogleSheetsService googleSheetsService, BestDefinitionService bestDefinitionService) {
        this.taskSetsForDayRepository = taskSetsForDayRepository;
        this.googleSheetsService = googleSheetsService;
        this.bestDefinitionService = bestDefinitionService;
    }
    public void updateBestDefinitionGoogleSheet(){
        List<List<String>> lineGoogleSheet =
                googleSheetsService.getLineGoogleSheet(SheetNames.BEST_DEFINITION.getValue());
        List<BestDefinition> bestDefinitionList = new ArrayList<>();
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

            bestDefinitionList.add(bestDefinition);
            bestDefinitionService.saveBestDefinitionTask(bestDefinition);
        }
        TaskSetsForDay taskSetsForDay = new TaskSetsForDay();
        taskSetsForDay.setBestDefinitionList(bestDefinitionList);
        taskSetsForDayRepository.save(taskSetsForDay);
    }
    public void updateAllRepository(){
        log.info("remove all data from best definition repository");
        bestDefinitionService.deleteAllBestDefinition();
        updateBestDefinitionGoogleSheet();
    }
    private int indexOfRightAnswer(String rightAnswer, List<String> listOfAnswers){
        return listOfAnswers.indexOf(rightAnswer) + 1;
    }
}
