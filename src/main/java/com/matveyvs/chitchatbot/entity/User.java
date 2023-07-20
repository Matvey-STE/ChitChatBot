package com.matveyvs.chitchatbot.entity;

import lombok.Data;

@Data
class User {
    private String userId;
    private Long chatId;
    private String userNickName;
    private String userName;
    private String userSurname;
    private String password;
}
