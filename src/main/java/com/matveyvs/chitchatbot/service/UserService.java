package com.matveyvs.chitchatbot.service;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.entity.RegisteredUser;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.entity.repository.RegisteredUsersRepository;
import com.matveyvs.chitchatbot.entity.repository.UserEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserService {
    private final RegisteredUsersRepository registeredUsersRepository;
    private final UserEntityRepository userRepository;
    public UserService(RegisteredUsersRepository registeredUsersRepository, UserEntityRepository userRepository) {
        this.registeredUsersRepository = registeredUsersRepository;
        this.userRepository = userRepository;
    }
    @Transactional(readOnly = true)
    public UserEntity getUserById(Long chatId) {
        return userRepository.findByChatId(chatId);
    }
    public UserEntity getUserByUserName(String userName) {return userRepository.findByUserName(userName);}
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
    //todo create special methods for userService
    public BotState getIndexOfUserCurrentBotState(long chatId) {
        int botStateFromDb = userRepository.getUserBotStateById(chatId);
        if (botStateFromDb == 0){
            botStateFromDb = BotState.START.ordinal();
        }
        return BotState.getValueByInteger(botStateFromDb);
    }
    public void setUsersCurrentBotState(long chatId, BotState botState) {
        userRepository.setUserBotStateById(chatId,botState.ordinal());
    }
    // User registration table of users
    public List<String> getAllRegisteredUsers(){
        List<String> stringList = new ArrayList<>();
        List<RegisteredUser> all = registeredUsersRepository.findAll();
        for (RegisteredUser registeredUser : all) {
            String userName = registeredUser.getUserName();
            stringList.add(userName);
        }
        return stringList;
    }
    public void saveRegisteredUser(String userName){
        RegisteredUser registeredUser = new RegisteredUser(userName);
        registeredUsersRepository.save(registeredUser);
    }
    public void deleteRegisterUserByName(String userName){
        registeredUsersRepository.deleteByUserName(userName);
    }
    public RegisteredUser getRegisterUserByName(String userName){
       return registeredUsersRepository.findByUserName(userName);
    }
    public boolean isUserInListRegisteredUser(String userName){
        List <String> listOfNames = getAllRegisteredUsers();
        listOfNames.forEach(System.out::println);
        System.out.println("From user service: " + userName);
        boolean b = listOfNames.stream().anyMatch(name -> name.equals(userName));
        System.out.println("From user service: " + b);
        return b;
    }
}
