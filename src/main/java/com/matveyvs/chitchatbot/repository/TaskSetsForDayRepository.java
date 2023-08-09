package com.matveyvs.chitchatbot.repository;

import com.matveyvs.chitchatbot.entity.TaskSetsForDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TaskSetsForDayRepository extends JpaRepository<TaskSetsForDay, Integer> {
    int countAllBy();
    @Transactional
    void deleteAllBy();
}
