package com.matveyvs.chitchatbot.entity.repository;

import com.matveyvs.chitchatbot.entity.TaskSetsForDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskSetsForDayRapository extends JpaRepository<TaskSetsForDay, Integer> {
    int countAllBy();
}
