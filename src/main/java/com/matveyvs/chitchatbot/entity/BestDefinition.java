package com.matveyvs.chitchatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "best_definition")
public class BestDefinition extends AbstractClassEntity{
    String taskWord;
    String rightAnswer;
    Integer indexOfRightAnswer;
    List<String> listOfAnswers;

    @Override
    public String toString() {
        return "BestDefinition{" +
                "taskWord='" + taskWord + '\'' +
                ", rightAnswer='" + rightAnswer + '\'' +
                ", indexOfRightAnswer=" + indexOfRightAnswer +
                ", listOfAnswers=" + listOfAnswers +
                '}';
    }
}

