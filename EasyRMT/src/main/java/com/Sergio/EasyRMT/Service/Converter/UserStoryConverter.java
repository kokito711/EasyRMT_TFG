/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Domain.UserStoryDom;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.UserStory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserStoryConverter {
    UserConverter userConverter;
    @Autowired
    public UserStoryConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    /**
    * this method converts an UserStoryList (Model) to an USerStory(Domain)
    * @param userStoryList List of {@link UserStory} obtained from DB
    * @return List of {@link UserStoryDom}
    */
    public List<UserStoryDom> toDomain(List<UserStory> userStoryList){
        List<UserStoryDom> userStoryDomList = new ArrayList<>();
        for(UserStory userStory:userStoryList){
            UserDom author = userConverter.toDomain(userStory.getAuthor());
            UserDom assignedTo = userConverter.toDomain(userStory.getAssignedTo());
            UserStoryDom userStoryDom = new UserStoryDom(
              userStory.getIdUserStory(),
              userStory.getName(),
              userStory.getIdentifier(),
              author,
              assignedTo,
              userStory.getObject().getProject().getIdProject(),
              userStory.getEpic().getIdEpic()
            );
            userStoryDom.setState(userStory.getState());
            userStoryDomList.add(userStoryDom);
        }
        return userStoryDomList;
    }

    /**
     * This method converts an {@link UserStory} (DB) to an {@link UserStoryDom} (Domain)
     * @param userStory {@link UserStory} obtained from DB
     * @return {@link UserStoryDom}
     */
    public UserStoryDom toDomain(UserStory userStory) {
        UserDom author = userConverter.toDomain(userStory.getAuthor());
        UserDom assignedTo = userConverter.toDomain(userStory.getAssignedTo());
        if(userStory.getEstimatedHours() == null){
            userStory.setEstimatedHours(0.00);
        }
        if (userStory.getStoryPoints()==null){
            userStory.setStoryPoints(0);
        }
        if(userStory.getCost()==null){
            userStory.setCost(0.0);
        }
        UserStoryDom userStoryDom = new UserStoryDom(
                userStory.getIdUserStory(),
                userStory.getName(),
                userStory.getIdentifier(),
                userStory.getDescription(),
                userStory.getDefinitionOfDone(),
                userStory.getPriority(),
                userStory.getComplexity(),
                userStory.getState(),
                userStory.getCost(),
                userStory.getEstimatedHours(),
                userStory.getStoryPoints(),
                userStory.getSource(),
                userStory.getScope(),
                userStory.getRisk(),
                userStory.getCreated(),
                userStory.getLastUpdated(),
                userStory.getVersion(),
                userStory.getValidationMethod(),
                author,
                assignedTo,
                userStory.getJustification(),
                userStory.getTestCases(),
                userStory.getObject().getProject().getIdProject(),
                userStory.getEpic().getIdEpic()
        );
        return userStoryDom;
    }

    /**
     * This method converts an {@link UserStoryDom} (Domain) to an {@link UserStory} (DB)
     * @param userStoryDom {@link UserStoryDom}
     * @return {@link UserStory}
     */
    public UserStory toModel(UserStoryDom userStoryDom) {
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setIdobject(userStoryDom.getIdUserStory());
        if(userStoryDom.getCost()==null){
            userStoryDom.setCost(0.0);
        }
        UserStory userStory = new UserStory();
        userStory.setIdUserStory(userStoryDom.getIdUserStory());
        userStory.setName(userStoryDom.getName());
        userStory.setIdentifier(userStoryDom.getIdentifier());
        userStory.setDescription(userStoryDom.getDescription());
        userStory.setDefinitionOfDone(userStoryDom.getDefinitionOfDone());
        userStory.setPriority(userStoryDom.getPriority());
        userStory.setComplexity(userStoryDom.getComplexity());
        userStory.setState(userStoryDom.getState());
        userStory.setCost(userStoryDom.getCost());
        userStory.setEstimatedHours(userStoryDom.getEstimatedHours());
        userStory.setStoryPoints(userStoryDom.getStoryPoints());
        userStory.setSource(userStoryDom.getSource());
        userStory.setScope(userStoryDom.getScope());
        userStory.setRisk(userStoryDom.getRisk());
        userStory.setCreated(userStoryDom.getCreated());
        userStory.setLastUpdated(userStoryDom.getLastUpdated());
        userStory.setVersion(userStoryDom.getVersion());
        userStory.setValidationMethod(userStoryDom.getValidationMethod());
        userStory.setJustification(userStoryDom.getJustification());
        userStory.setTestCases(userStoryDom.getTestCases());
        userStory.setObject(objectEntity);
        return userStory;
    }
}
