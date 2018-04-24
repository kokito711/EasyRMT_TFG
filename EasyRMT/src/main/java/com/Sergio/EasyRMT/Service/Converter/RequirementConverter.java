/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.RequirementDom;
import com.Sergio.EasyRMT.Model.Requirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequirementConverter {

    @Autowired
    public RequirementConverter() {
    }

    /**
     * this method converts an RequirementList (Model) to an RequirementList(Domain)
     * @param requirementList List of {@link Requirement} obtained from DB
     * @return List of {@link RequirementDom}
     */
    public List<RequirementDom> toDomain(List<Requirement> requirementList) {
        List<RequirementDom> requirementDomList = new ArrayList<>();
        for(Requirement requirement: requirementList){
            if(requirement.getAssignedTo() == null){
                requirement.setAssignedTo(0);
            }
            RequirementDom requirementDom = new RequirementDom(
                    requirement.getIdRequirement(),
                    requirement.getName(),
                    requirement.getIdentifier(),
                    requirement.getAuthor(),
                    requirement.getAssignedTo(),
                    requirement.getRequirementType().getIdType(),
                    requirement.getObject().getProject().getIdProject()
            );
            requirementDomList.add(requirementDom);
        }
        return requirementDomList;
    }

    /**
     * This method converts an {@link Requirement} (DB) to an {@link RequirementDom} (Domain)
     * @param requirement {@link Requirement} obtained from DB
     * @return {@link Requirement}
     */
    public RequirementDom toDomain(Requirement requirement) {
        if(requirement.getAssignedTo() == null){
            requirement.setAssignedTo(0);
        }
        if(requirement.getEstimatedHours() == null){
            requirement.setEstimatedHours(0.00);
        }
        if (requirement.getStoryPoints()==null){
            requirement.setStoryPoints(0);
        }
        RequirementDom requirementDom = new RequirementDom(
                requirement.getIdRequirement(),
                requirement.getName(),
                requirement.getIdentifier(),
                requirement.getDescription(),
                requirement.getPriority(),
                requirement.getComplexity(),
                requirement.getState(),
                requirement.getCost(),
                requirement.getEstimatedHours(),
                requirement.getStoryPoints(),
                requirement.getSource(),
                requirement.getScope(),
                requirement.getRisk(),
                requirement.getCreated(),
                requirement.getLastUpdated(),
                requirement.getVersion(),
                requirement.getValidationMethod(),
                requirement.getAuthor(),
                requirement.getAssignedTo(),
                requirement.getJustification(),
                requirement.getTestCases(),
                requirement.getRequirementType().getIdType(),
                requirement.getObject().getProject().getIdProject()
        );
        return requirementDom;
    }

    /**
     * This method converts an {@link RequirementDom} (Domain) to an {@link Requirement} (DB)
     * @param requirementDom {@link RequirementDom}
     * @return {@link Requirement}
     */
    public Requirement toModel(RequirementDom requirementDom) {
        Requirement requirement = new Requirement();
        requirement.setName(requirementDom.getName());
        requirement.setIdentifier(requirementDom.getIdentifier());
        requirement.setDescription(requirementDom.getDescription());
        requirement.setPriority(requirementDom.getPriority());
        requirement.setComplexity(requirementDom.getComplexity());
        requirement.setState(requirementDom.getState());
        requirement.setCost(requirementDom.getCost());
        requirement.setEstimatedHours(requirementDom.getEstimatedHours());
        requirement.setStoryPoints(requirementDom.getStoryPoints());
        requirement.setSource(requirementDom.getSource());
        requirement.setScope(requirementDom.getScope());
        requirement.setRisk(requirementDom.getRisk());
        requirement.setCreated(requirementDom.getCreated());
        requirement.setLastUpdated(requirementDom.getLastUpdated());
        requirement.setVersion(requirementDom.getVersion());
        requirement.setValidationMethod(requirementDom.getValidationMethod());
        requirement.setAuthor(requirementDom.getAuthor());
        requirement.setAssignedTo(requirementDom.getAssignedTo());
        requirement.setJustification(requirementDom.getJustification());
        requirement.setTestCases(requirementDom.getTestCases());
        return requirement;
    }
}
