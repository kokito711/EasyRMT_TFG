/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Model.Epic;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EpicConverter {

    /**
     * this method converts an EpicList (Model) to an EpicList(Domain)
     * @param epicList List of {@link Epic} obtained from DB
     * @return List of {@link EpicDom}
     */
    public List<EpicDom> toDomain(List<Epic> epicList) {
        List<EpicDom> epicDomList = new ArrayList<>();
        for(Epic epic: epicList){
            EpicDom epicDom = new EpicDom(
                    epic.getIdEpic(),
                    epic.getName(),
                    epic.getIdentifier(),
                    epic.getAuthor(),
                    epic.getAssignedTo(),
                    epic.getObject().getProject().getIdProject()
            );
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
                epic.getAuthor(),
                epic.getAssignedTo(),
                epic.getJustification(),
                epic.getTestCases(),
                epic.getObject().getProject().getIdProject()
        );
        return epicDom;
    }

    /**
     * This method converts an {@link EpicDom} (Domain) to an {@link Epic} (DB)
     * @param epicDom {@link EpicDom}
     * @return {@link Epic}
     */
    public EpicDom toModel(EpicDom epicDom) {
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
        epic.setAuthor(epicDom.getAuthor());
        epic.setAssignedTo(epicDom.getAssignedTo());
        epic.setJustification(epicDom.getJustification());
        epic.setTestCases(epicDom.getTestCases());
        epic.setObject(objectEntity);
        return epicDom;
    }
}
