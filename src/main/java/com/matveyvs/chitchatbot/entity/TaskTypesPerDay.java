package com.matveyvs.chitchatbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_types")
public class TaskTypesPerDay extends AbstractClassEntity{
    Integer setNumber;
}
