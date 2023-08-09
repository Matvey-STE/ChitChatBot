package com.matveyvs.chitchatbot.repository;


import com.matveyvs.chitchatbot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAll();
    UserEntity findByChatId (Long chatId);
    @Transactional
    void deleteByUserName (String userName);
    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_TASK_CONDITION\n" +
            "SET BEST_DEFINITION = :numberOfTasks " + // Add a space before "WHERE"
            "WHERE ID IN (\n" +
            "    SELECT C.ID\n" +
            "    FROM USER_TASK_CONDITION C\n" +
            "    INNER JOIN USERS U ON U.USER_TASK_CONDITION_ID = C.ID\n" +
            "    WHERE U.CHAT_ID = :chatId\n" +
            ")", nativeQuery = true)
    void saveUserTaskConditionBestDefinition(Long chatId, Integer numberOfTasks);

    @Transactional
    @Query(value = "SELECT BEST_DEFINITION \n" +
            "FROM USER_TASK_CONDITION\n" +
            "WHERE ID IN (\n" +
            "SELECT C.ID\n" +
            "FROM USER_TASK_CONDITION C\n" +
            "INNER JOIN USERS U ON U.USER_TASK_CONDITION_ID = C.ID\n" +
            " WHERE U.CHAT_ID = :chatId" +
            ")", nativeQuery = true)
    int getUserTaskConditionBestDefinition(Long chatId);

}
