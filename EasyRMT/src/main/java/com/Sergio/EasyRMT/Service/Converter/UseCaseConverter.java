/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.UseCaseDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UseCaseConverter {
    UserConverter userConverter;

    @Autowired
    public UseCaseConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    /**
     * this method converts an UseCaseList (Model) to an UseCaseList(Domain)
     * @param useCaseList List of {@link UseCase} obtained from DB
     * @return List of {@link UseCaseDom}
     */
    public List<UseCaseDom> toDomain(List<UseCase> useCaseList){
        List<UseCaseDom> useCaseDomList = new ArrayList<>();
        for(UseCase useCase:useCaseList){
            UserDom author = userConverter.toDomain(useCase.getAuthor());
            UserDom assignedTo = userConverter.toDomain(useCase.getAssignedTo());
            UseCaseDom useCaseDom = new UseCaseDom(
                    useCase.getIdUseCase(),
                    useCase.getName(),
                    useCase.getIdentifier(),
                    author,
                    assignedTo,
                    useCase.getObject().getProject().getIdProject(),
                    useCase.getFeature().getIdFeature()
            );
            useCaseDom.setState(useCase.getState());
            useCaseDomList.add(useCaseDom);
        }
        return useCaseDomList;
    }

    /**
     * This method converts an {@link UseCase} (DB) to an {@link UseCaseDom} (Domain)
     * @param useCase {@link UseCase} obtained from DB
     * @return {@link UseCaseDom}
     */
    public UseCaseDom toDomain(UseCase useCase) {
        UserDom author = userConverter.toDomain(useCase.getAuthor());
        UserDom assignedTo = userConverter.toDomain(useCase.getAssignedTo());
        if(useCase.getEstimatedHours() == null){
            useCase.setEstimatedHours(0.00);
        }
        if (useCase.getStoryPoints()==null){
            useCase.setStoryPoints(0);
        }
        if(useCase.getCost()==null){
            useCase.setCost(0.0);
        }
        UseCaseDom useCaseDom = new UseCaseDom(
                useCase.getIdUseCase(),
                useCase.getName(),
                useCase.getIdentifier(),
                useCase.getDescription(),
                useCase.getPriority(),
                useCase.getComplexity(),
                useCase.getState(),
                useCase.getCost(),
                useCase.getEstimatedHours(),
                useCase.getStoryPoints(),
                useCase.getSource(),
                useCase.getScope(),
                useCase.getRisk(),
                useCase.getCreated(),
                useCase.getLastUpdated(),
                useCase.getVersion(),
                useCase.getValidationMethod(),
                author,
                assignedTo,
                useCase.getJustification(),
                useCase.getTestCases(),
                useCase.getActors(),
                useCase.getPreconditions(),
                useCase.getPostconditions(),
                useCase.getSteps(),
                useCase.getObject().getProject().getIdProject(),
                useCase.getFeature().getIdFeature()
        );
        return useCaseDom;
    }

    /**
     * This method converts an {@link UseCaseDom} (Domain) to an {@link UseCase} (DB)
     * @param useCaseDom {@link UseCaseDom}
     * @return {@link UseCase}
     */
    public UseCase toModel(UseCaseDom useCaseDom) {
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setIdobject(useCaseDom.getIdUseCase());
        if(useCaseDom.getCost()==null){
            useCaseDom.setCost(0.0);
        }
        UseCase useCase = new UseCase();
        useCase.setIdUseCase(useCaseDom.getIdUseCase());
        useCase.setName(useCaseDom.getName());
        useCase.setIdentifier(useCaseDom.getIdentifier());
        useCase.setDescription(useCaseDom.getDescription());
        useCase.setPriority(useCaseDom.getPriority());
        useCase.setComplexity(useCaseDom.getComplexity());
        useCase.setState(useCaseDom.getState());
        useCase.setCost(useCaseDom.getCost());
        useCase.setEstimatedHours(useCaseDom.getEstimatedHours());
        useCase.setStoryPoints(useCaseDom.getStoryPoints());
        useCase.setSource(useCaseDom.getSource());
        useCase.setScope(useCaseDom.getScope());
        useCase.setRisk(useCaseDom.getRisk());
        useCase.setCreated(useCaseDom.getCreated());
        useCase.setLastUpdated(useCaseDom.getLastUpdated());
        useCase.setVersion(useCaseDom.getVersion());
        useCase.setValidationMethod(useCaseDom.getValidationMethod());
        useCase.setJustification(useCaseDom.getJustification());
        useCase.setTestCases(useCaseDom.getTestCases());
        useCase.setActors(useCaseDom.getActors());
        useCase.setPreconditions(useCaseDom.getPreconditions());
        useCase.setPostconditions(useCaseDom.getPostconditions());
        useCase.setSteps(useCaseDom.getSteps());
        useCase.setObject(objectEntity);
        return useCase;
    }
}
