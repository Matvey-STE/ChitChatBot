package com.matveyvs.chitchatbot.service.taskservices;

import com.matveyvs.chitchatbot.entity.BestDefinition;
import com.matveyvs.chitchatbot.entity.repository.BestDefinitionRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BestDefinitionService {
    private final BestDefinitionRepository bestDefinitionRepository;

    public BestDefinitionService(BestDefinitionRepository bestDefinitionRepository) {
        this.bestDefinitionRepository = bestDefinitionRepository;
    }
    public void saveBestDefinitionTask(BestDefinition bestDefinition){
        bestDefinitionRepository.save(bestDefinition);
    }
    public BestDefinition findBestDefinitionTaskById(Integer id){
         return bestDefinitionRepository.findBestDefinitionById(id);
    }
    public int getIndexOfRightAnswer(Integer id){
        return bestDefinitionRepository.findIndexOfRightAnswerById(id);
    }
    public List<BestDefinition> getAllBestDefinitions(){
        return bestDefinitionRepository.findAll();
    }
    public int getAmountOfTasks(){
        return bestDefinitionRepository.findAll().size();
    }
}
