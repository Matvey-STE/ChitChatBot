package com.matveyvs.chitchatbot.cache;

import com.matveyvs.chitchatbot.botapi.BotState;
import com.matveyvs.chitchatbot.entity.UserEntity;

public interface DataCache {
    void setUsersCurrentBotState(long chatId, BotState botState);
    BotState getUserCurrentBotState(long chatId);
    UserEntity getUserByIdFromCache(long chatId);
    void saveCachedUser(UserEntity userEntity);
}
