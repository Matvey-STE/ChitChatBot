package com.matveyvs.chitchatbot.entity.repository;


import com.matveyvs.chitchatbot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAll();
    UserEntity findByChatId (Long chatId);
    void deleteByChatId (Long chatId);
    void deleteByUserName (String userName);
}
