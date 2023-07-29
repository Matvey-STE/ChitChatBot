package com.matveyvs.chitchatbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass

@Access(AccessType.FIELD)

@Getter
@Setter
public class AbstractClassEntity {
    public static final int START_SEQ = 0;
    @Id
    @SequenceGenerator(name = "global_seq",sequenceName = "global_seq",allocationSize = 1,initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Integer id;
    protected AbstractClassEntity(){

    }
}
