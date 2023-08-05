package com.matveyvs.chitchatbot.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CallbackQueriesService {
    public List<String> createListOfQueries(Integer amountOfButtons, String classShortName){
        List<String> stringList = new ArrayList<>();
        for (int i = 1; i <= amountOfButtons; i++){
            stringList.add(classShortName + i);
        }
        return stringList;
    }
    public List<String> mergeTwoListOfQueries(List<String> firstList, List<String> secondList){
        List<String> stringList = new ArrayList<>(firstList);
        stringList.addAll(secondList);
        return stringList;
    }
}
