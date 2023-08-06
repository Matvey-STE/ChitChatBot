package com.matveyvs.chitchatbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserTaskCondition{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;
    Integer bestDefinition;

    public UserTaskCondition(Integer bestDefinition) {
        this.bestDefinition = bestDefinition;
    }
}
