package com.matveyvs.chitchatbot.service;

import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.entity.repository.UserEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Component
public class UserService {
    private final UserEntityRepository userRepository;
    public UserService(UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional(readOnly = true)
    public UserEntity findUserById (Long chatId) {
        return userRepository.findByChatId(chatId);
    }
    public void deleteUserByUserName (String userName){
        userRepository.deleteByUserName(userName);
    }
    public void deleteUserByUserId (Long chatId){
        userRepository.deleteByChatId(chatId);
    }
    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }
    public void saveUser (UserEntity userEntity){
        userRepository.save(userEntity);
    }
}
