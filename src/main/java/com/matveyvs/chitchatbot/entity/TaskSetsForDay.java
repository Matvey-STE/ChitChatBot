package com.matveyvs.chitchatbot.entity;

import jakarta.persistence.*;
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
@Table(name = "task_types")
public class TaskSetsForDay extends AbstractClassEntity{
    @OneToMany(targetEntity = BestDefinition.class)
    @JoinColumn(name = "task_set_id", referencedColumnName = "id")
    List<BestDefinition> bestDefinitionList;
}
