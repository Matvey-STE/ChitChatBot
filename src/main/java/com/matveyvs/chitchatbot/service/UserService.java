package com.matveyvs.chitchatbot.service;

import com.matveyvs.chitchatbot.entity.UserTaskCondition;
import com.matveyvs.chitchatbot.repository.UserTaskConditionRepository;
import com.matveyvs.chitchatbot.entity.RegisteredUser;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.repository.RegisteredUsersRepository;
import com.matveyvs.chitchatbot.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Component
@AllArgsConstructor
public class UserService {
    private final UserTaskConditionRepository userTaskConditionRepository;
    private final RegisteredUsersRepository registeredUsersRepository;
    private final UserEntityRepository userRepository;
    
    public void saveUser (UserEntity userEntity){
        userRepository.save(userEntity);
    }
    @Transactional(readOnly = true)
    public UserEntity getUserById(Long chatId) {
        return userRepository.findByChatId(chatId);
    }
    public void deleteUserByUserName (String userName){
        userRepository.deleteByUserName(userName);
    }
    public void saveUserTaskCondition(UserTaskCondition userTaskCondition){
        userTaskConditionRepository.save(userTaskCondition);
    }
    public void saveConditionBestDefinition(Long chatId, Integer value){
        userRepository.saveUserTaskConditionBestDefinition(chatId,value);
    }
    public int getUserTaskConditionBestDefinition (Long chatId){
        return userRepository.getUserTaskConditionBestDefinition(chatId);
    }
    public void saveRegisteredUser(String userName){
        RegisteredUser registeredUser = new RegisteredUser(userName);
        registeredUsersRepository.save(registeredUser);
    }
    public List<String> getAllRegisteredUsers(){
        List<String> stringList = new ArrayList<>();
        List<RegisteredUser> all = registeredUsersRepository.findAll();
        for (RegisteredUser registeredUser : all) {
            String userName = registeredUser.getUserName();
            stringList.add(userName);
        }
        return stringList;
    }
    public RegisteredUser getRegisterUserByName(String userName){
        return registeredUsersRepository.findByUserName(userName);
    }
    public boolean isUserInListRegisteredUser(String userName){
        List <String> listOfNames = getAllRegisteredUsers();
        return listOfNames.stream().anyMatch(name -> name.equals(userName));
    }
    public void deleteRegisterUserByName(String userName){
        registeredUsersRepository.deleteByUserName(userName);
    }
}
