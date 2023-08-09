package com.matveyvs.chitchatbot.repository;

import com.matveyvs.chitchatbot.entity.BestDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Repository
public interface BestDefinitionRepository extends JpaRepository<BestDefinition,Integer> {
    BestDefinition findBestDefinitionById(Integer id);
    List<BestDefinition> findAll();
    @Transactional
    void deleteAllBy();
    @Query(value = "SELECT TASK_WORD FROM BEST_DEFINITION WHERE id = :id",nativeQuery = true)
    String findTaskWordById(Integer id);
    @Query(value = "SELECT RIGHT_ANSWER FROM BEST_DEFINITION WHERE id = :id",nativeQuery = true)
    String findRightAnswerOfTaskById(Integer id);
    @Query(value = "SELECT INDEX_OF_RIGHT_ANSWER FROM BEST_DEFINITION WHERE id = :id",nativeQuery = true)
    Integer findIndexOfRightAnswerById(Integer id);
    @Query(value = "SELECT MIN(ID) FROM BEST_DEFINITION WHERE TASK_SET_ID = :id",nativeQuery = true)
    Integer findMinIdForTaskSetById(Integer id);
    @Query(value = "SELECT MAX(ID) FROM BEST_DEFINITION WHERE TASK_SET_ID = :id",nativeQuery = true)
    Integer findMaxIdForTaskSetById(Integer id);

}
