package com.matveyvs.chitchatbot.entity.repository;

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
    @Query(value = "SELECT INDEX_OF_RIGHT_ANSWER FROM BEST_DEFINITION WHERE id = :id",nativeQuery = true)
    Integer findIndexOfRightAnswerById(Integer id);

}
