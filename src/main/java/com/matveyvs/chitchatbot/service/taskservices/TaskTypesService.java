package com.matveyvs.chitchatbot.service.taskservices;

import com.matveyvs.chitchatbot.entity.repository.TaskSetsForDayRapository;
import org.springframework.stereotype.Component;

@Component
public class TaskTypesService {
    private final TaskSetsForDayRapository taskSetsForDayRapository;

    public TaskTypesService(TaskSetsForDayRapository taskSetsForDayRapository) {
        this.taskSetsForDayRapository = taskSetsForDayRapository;
    }


}
