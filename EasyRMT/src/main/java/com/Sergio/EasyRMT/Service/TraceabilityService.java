/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.RequirementDom;
import com.Sergio.EasyRMT.Domain.TraceDom;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import com.Sergio.EasyRMT.Repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TraceabilityService {

    private ObjectRepository objectRepository;
    private RequirementRepository requirementRepository;
    private RequirementService requirementService;
    private ReqTypeRepository reqTypeRepository;

    @Autowired
    public TraceabilityService(ObjectRepository objectRepository, RequirementRepository requirementRepository, RequirementService requirementService, ReqTypeRepository reqTypeRepository) {
        this.objectRepository = objectRepository;
        this.requirementRepository = requirementRepository;
        this.requirementService = requirementService;
        this.reqTypeRepository = reqTypeRepository;
    }

    @Transactional(readOnly = true)
    public TraceDom getTraceability(int idOjbect) {
        ObjectEntity objectEntity = objectRepository.findOne(idOjbect);
        List<RequirementDom> requirements = new ArrayList<>();
        List<RequirementDom> scope = new ArrayList<>();
        List<RequirementDom> engineering= new ArrayList<>();
        List<RequirementDom> quality= new ArrayList<>();
        List<RequirementDom> userExp= new ArrayList<>();
        for(ObjectEntity object : objectEntity.getTraced()){
            if(requirementRepository.exists(object.getIdobject())){
                RequirementDom requirement = requirementService.getRequirement(object.getIdobject());
                requirements.add(requirement);
            }
        }
        for (RequirementDom requirement : requirements){
            RequirementType requirementType = reqTypeRepository.findOne(requirement.getRequirementTypeId());
            switch (requirementType.getType()){
                case SCOPE:
                    scope.add(requirement);
                    break;
                case ENGINEERING:
                    engineering.add(requirement);
                    break;
                case USER_EXP:
                    userExp.add(requirement);
                    break;
                case QA:
                    quality.add(requirement);
                    break;
            }
        }
        TraceDom traceability = new TraceDom(
                scope,
                engineering,
                quality,
                userExp
        );
        return traceability;
    }

    @Transactional(readOnly = true)
    public List<RequirementDom> getNotTracedReqs(int projectId, int object){
        List<RequirementDom> notTraced = new ArrayList<>();
        List<RequirementDom> requirements = requirementService.getRequirements(projectId);
        for (RequirementDom requirement : requirements){
            ObjectEntity objectEntity = objectRepository.findOne(object);
            ObjectEntity objectRequirement = objectRepository.findOne(requirement.getIdRequirement());
            if (!objectEntity.getTraced().contains(objectRequirement)){
                notTraced.add(requirement);
            }
        }
        return notTraced;
    }
}
