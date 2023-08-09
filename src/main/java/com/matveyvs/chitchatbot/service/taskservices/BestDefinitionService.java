package com.matveyvs.chitchatbot.service.taskservices;

import com.matveyvs.chitchatbot.entity.BestDefinition;
import com.matveyvs.chitchatbot.repository.BestDefinitionRepository;
import com.matveyvs.chitchatbot.repository.TaskSetsForDayRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class BestDefinitionService {
    private final TaskSetsForDayRepository taskSetsForDayRepository;
    private final BestDefinitionRepository bestDefinitionRepository;

    public void saveBestDefinitionTask(BestDefinition bestDefinition){
        bestDefinitionRepository.save(bestDefinition);
    }

    public BestDefinition findBestDefinitionTaskById(Integer id){
         return bestDefinitionRepository.findBestDefinitionById(id);
    }
    public int getIndexOfRightAnswer(Integer id){
        return bestDefinitionRepository.findIndexOfRightAnswerById(id);
    }
    public String getRightAnswerById(Integer id){
        return bestDefinitionRepository.findRightAnswerOfTaskById(id);
    }
    public String getTaskWordById (Integer id){
        return bestDefinitionRepository.findTaskWordById(id);
    }
    public int getMaxIdForDayTaskById(Integer id){
        return bestDefinitionRepository.findMaxIdForTaskSetById(id);
    }
    public int getMinIdForDayTaskById(Integer id){
        return bestDefinitionRepository.findMinIdForTaskSetById(id);
    }
    public void deleteAllBestDefinition(){
        bestDefinitionRepository.deleteAllBy();
        taskSetsForDayRepository.deleteAllBy();
    }
    public List<BestDefinition> getAllBestDefinitions(){
        return bestDefinitionRepository.findAll();
    }
    public int getAmountOfTasks(){
        return bestDefinitionRepository.findAll().size();
    }
}
