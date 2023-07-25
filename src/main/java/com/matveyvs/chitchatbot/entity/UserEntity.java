package com.matveyvs.chitchatbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends AbstractClassEntity{
    @Column(name = "chat_id",unique = true)
    private Long chatId;
    private String firstName;
    private String secondName;
    @Column(unique = true)
    private String userName;
    private String languageCode;
    @Column(name = "state_id")
    private Integer stateId;

    @Override
    public String toString() {
        return "UserEntity{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", userName='" + userName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", stateId=" + stateId +
                '}';
    }
}


