package com.matveyvs.chitchatbot.entity.repository;


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
    void deleteByChatId (Long chatId);
    void deleteByUserName (String userName);
    @Query(value = "SELECT STATE_ID FROM USERS WHERE CHAT_ID = :chatId",nativeQuery = true)
    int getUserBotStateById(Long chatId);
    @Transactional
    @Modifying
    @Query(value = "UPDATE user_entity SET bot_state = :botStateInteger WHERE chat_id = :chatId",nativeQuery = true)
    void setUserBotStateById(Long chatId, Integer botStateInteger);
}
