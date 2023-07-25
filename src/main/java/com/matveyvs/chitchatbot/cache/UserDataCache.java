package com.matveyvs.chitchatbot.cache;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.entity.UserEntity;
import com.matveyvs.chitchatbot.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserDataCache implements DataCache{
    private final UserService userService;
    public UserDataCache(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void setUsersCurrentBotState(long chatId, BotState botState) {
        UserEntity userById = userService.findUserById(chatId);
        userById.setStateId(botState.ordinal());
    }

    @Override
    public BotState getUserCurrentBotState(long chatId) {
        UserEntity userById = userService.findUserById(chatId);
        BotState botState = BotState.getValueByInteger(userById.getStateId());
        if (botState == null){
            botState = BotState.START_MESSAGE;
        }
        return botState;
    }
    @Override
    public UserEntity getUserByIdFromCache(long chatId) {
        return userService.findUserById(chatId);
    }

    @Override
    public void saveCachedUser(UserEntity userEntity) {
        userService.saveUser(userEntity);
    }
}
