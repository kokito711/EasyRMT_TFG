/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Domain.UserStoryDom;
import com.Sergio.EasyRMT.Model.Epic;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EpicConverter {


    private UserStoryConverter userStoryConverter;
    private UserConverter userConverter;

    @Autowired
    public EpicConverter(UserStoryConverter userStoryConverter, UserConverter userConverter) {
        this.userStoryConverter = userStoryConverter;
        this.userConverter = userConverter;
    }

    /**
     * this method converts an EpicList (Model) to an EpicList(Domain)
     * @param epicList List of {@link Epic} obtained from DB
     * @return List of {@link EpicDom}
     */
    public List<EpicDom> toDomain(List<Epic> epicList) {
        List<EpicDom> epicDomList = new ArrayList<>();
        for(Epic epic: epicList){
            List<UserStoryDom> userStoryDomList = userStoryConverter.toDomain(epic.getUserStories());
            UserDom author = userConverter.toDomain(epic.getAuthor());
            UserDom assignedTo = userConverter.toDomain(epic.getAssignedTo());
            EpicDom epicDom = new EpicDom(
                    epic.getIdEpic(),
                    epic.getName(),
                    epic.getIdentifier(),
                    author,
                    assignedTo,
                    epic.getObject().getProject().getIdProject(),
                    userStoryDomList
            );
            epicDom.setState(epic.getState());
            epicDomList.add(epicDom);
        }
        return epicDomList;
    }

    /**
     * This method converts an {@link Epic} (DB) to an {@link EpicDom} (Domain)
     * @param epic {@link Epic} obtained from DB
     * @return {@link EpicDom}
     */
    public EpicDom toDomain(Epic epic) {
        List<UserStoryDom> userStoryDomList = userStoryConverter.toDomain(epic.getUserStories());
        UserDom author = userConverter.toDomain(epic.getAuthor());
        UserDom assignedTo = userConverter.toDomain(epic.getAssignedTo());
        if(epic.getEstimatedHours() == null){
            epic.setEstimatedHours(0.00);
        }
        if (epic.getStoryPoints()==null){
            epic.setStoryPoints(0);
        }
        EpicDom epicDom = new EpicDom(
                epic.getIdEpic(),
                epic.getName(),
                epic.getIdentifier(),
                epic.getDescription(),
                epic.getDefinitionOfDone(),
                epic.getPriority(),
                epic.getComplexity(),
                epic.getState(),
                epic.getCost(),
                epic.getEstimatedHours(),
                epic.getStoryPoints(),
                epic.getSource(),
                epic.getScope(),
                epic.getRisk(),
                epic.getCreated(),
                epic.getLastUpdated(),
                epic.getVersion(),
                epic.getValidationMethod(),
                author,
                assignedTo,
                epic.getJustification(),
                epic.getTestCases(),
                epic.getObject().getProject().getIdProject(),
                userStoryDomList
        );
        return epicDom;
    }

    /**
     * This method converts an {@link EpicDom} (Domain) to an {@link Epic} (DB)
     * @param epicDom {@link EpicDom}
     * @return {@link Epic}
     */
    public Epic toModel(EpicDom epicDom) {
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setIdobject(epicDom.getIdEpic());
        Epic epic = new Epic();
        epic.setIdEpic(epicDom.getIdEpic());
        epic.setName(epicDom.getName());
        epic.setIdentifier(epicDom.getIdentifier());
        epic.setDescription(epicDom.getDescription());
        epic.setDefinitionOfDone(epicDom.getDefinitionOfDone());
        epic.setPriority(epicDom.getPriority());
        epic.setComplexity(epicDom.getComplexity());
        epic.setState(epicDom.getState());
        epic.setCost(epicDom.getCost());
        epic.setEstimatedHours(epicDom.getEstimatedHours());
        epic.setStoryPoints(epicDom.getStoryPoints());
        epic.setSource(epicDom.getSource());
        epic.setScope(epicDom.getScope());
        epic.setRisk(epicDom.getRisk());
        epic.setCreated(epicDom.getCreated());
        epic.setLastUpdated(epicDom.getLastUpdated());
        epic.setVersion(epicDom.getVersion());
        epic.setValidationMethod(epicDom.getValidationMethod());
        epic.setJustification(epicDom.getJustification());
        epic.setTestCases(epicDom.getTestCases());
        epic.setObject(objectEntity);
        return epic;
    }
}
