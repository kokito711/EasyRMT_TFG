/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.*;
import com.Sergio.EasyRMT.Model.Feature;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TraceabilityService {

    private ObjectRepository objectRepository;
    private RequirementService requirementService;
    private ReqTypeRepository reqTypeRepository;
    private FeatureService featureService;
    private EpicService epicService;
    private UseCaseService useCaseService;
    private UserStoryService userStoryService;

    @Autowired
    public TraceabilityService(ObjectRepository objectRepository, RequirementService requirementService,
                               ReqTypeRepository reqTypeRepository, FeatureService featureService, EpicService epicService,
                               UseCaseService useCaseService, UserStoryService userStoryService) {
        this.objectRepository = objectRepository;
        this.requirementService = requirementService;
        this.reqTypeRepository = reqTypeRepository;
        this.featureService = featureService;
        this.epicService = epicService;
        this.useCaseService = useCaseService;
        this.userStoryService = userStoryService;
    }

    @Transactional(readOnly = true)
    public TraceDom getTraceability(int idOjbect) {
        ObjectEntity objectEntity = objectRepository.findOne(idOjbect);
        TraceDom traceability = new TraceDom();
        traceability.init();
        List<RequirementDom> requirements = new ArrayList<>();
        addObjectToTrace(objectEntity, traceability, requirements);
        return traceability;
    }

    @Transactional
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

    @Transactional
    public List<FeatureDom> getNotTracedFeatures(int projectId, int object){
        List<FeatureDom> notTraced = new ArrayList<>();
        List<FeatureDom> featureDoms = featureService.getFeatures(projectId);
        for (FeatureDom featureDom : featureDoms){
            if(featureDom.getIdFeature()!= object) {
                ObjectEntity objectEntity = objectRepository.findOne(object);
                ObjectEntity objectRequirement = objectRepository.findOne(featureDom.getIdFeature());
                if (!objectEntity.getTraced().contains(objectRequirement)) {
                    notTraced.add(featureDom);
                }
            }
        }
        return notTraced;
    }

    @Transactional
    public List<EpicDom> getNotTracedEpics(int projectId, int object){
        List<EpicDom> notTraced = new ArrayList<>();
        List<EpicDom> epicDomList = epicService.getEpics(projectId);
        for (EpicDom epicDom : epicDomList){
            if(epicDom.getIdEpic()!= object) {
                ObjectEntity objectEntity = objectRepository.findOne(object);
                ObjectEntity objectRequirement = objectRepository.findOne(epicDom.getIdEpic());
                if (!objectEntity.getTraced().contains(objectRequirement)) {
                    notTraced.add(epicDom);
                }
            }
        }
        return notTraced;
    }

    @Transactional
    public List<UseCaseDom> getNotTracedUseCases(int projectId, int object){
        List<UseCaseDom> notTraced = new ArrayList<>();
        List<UseCaseDom> useCaseDoms = useCaseService.getByProjectID(projectId);
        for (UseCaseDom useCaseDom : useCaseDoms){
            if(useCaseDom.getIdUseCase()!= object) {
                ObjectEntity objectEntity = objectRepository.findOne(object);
                ObjectEntity objectRequirement = objectRepository.findOne(useCaseDom.getIdUseCase());
                if (!objectEntity.getTraced().contains(objectRequirement)) {
                    notTraced.add(useCaseDom);
                }
            }
        }
        return notTraced;
    }

    @Transactional
    public List<UserStoryDom> getNotTracedUserStories(int projectId, int object){
        List<UserStoryDom> notTraced = new ArrayList<>();
        List<UserStoryDom> userStoryDoms = userStoryService.getByProjectID(projectId);
        for (UserStoryDom userStoryDom : userStoryDoms){
            if(userStoryDom.getIdUserStory()!= object) {
                ObjectEntity objectEntity = objectRepository.findOne(object);
                ObjectEntity objectRequirement = objectRepository.findOne(userStoryDom.getIdUserStory());
                if (!objectEntity.getTraced().contains(objectRequirement)) {
                    notTraced.add(userStoryDom);
                }
            }
        }
        return notTraced;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveRelationship(int object1Id, int object2Id){
        ObjectEntity object1 = objectRepository.findOne(object1Id);
        ObjectEntity object2 = objectRepository.findOne(object2Id);
        object1.getTraced().add(object2);
        object2.getTraced().add(object1);
        try {
            objectRepository.save(object1);
            objectRepository.save(object1);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRelatonship(int object1Id, int object2Id){
        if(objectRepository.exists(object1Id)&& objectRepository.exists(object2Id)){
            ObjectEntity object1 = objectRepository.findOne(object1Id);
            ObjectEntity object2 = objectRepository.findOne(object2Id);
            for(ObjectEntity object : object1.getTraced()){
                if(object.equals(object2)){
                    int index = object1.getTraced().indexOf(object);
                    object1.getTraced().remove(index);
                    break;
                }
            }
            for(ObjectEntity object : object2.getTraced()){
                if(object.equals(object2)){
                    int index = object2.getTraced().indexOf(object);
                    object2.getTraced().remove(index);
                    break;
                }
            }
            objectRepository.save(object1);
            objectRepository.save(object2);
            return true;
        }
        return false;
    }

    private void addObjectToTrace(ObjectEntity objectEntity, TraceDom traceability, List<RequirementDom> requirements) {
        for(ObjectEntity object : objectEntity.getTraced()){
            if(requirementService.exists(object.getIdobject())){
                RequirementDom requirement = requirementService.getRequirement(object.getIdobject());
                requirements.add(requirement);
            }
            else if(featureService.exists(object.getIdobject())){
                FeatureDom feature = featureService.getFeature(object.getIdobject());
                traceability.getFeatures().add(feature);
            }
            else if(epicService.exists(object.getIdobject())){
                EpicDom epic = epicService.getEpic(object.getIdobject());
                traceability.getEpics().add(epic);
            }
            else if(userStoryService.exists(object.getIdobject())){
                UserStoryDom userStory = userStoryService.getUserStory(object.getIdobject());
                traceability.getUserStories().add(userStory);
            }
            else if(useCaseService.exists(object.getIdobject())){
                UseCaseDom useCaseDom = useCaseService.getUseCase(object.getIdobject());
                traceability.getUseCases().add(useCaseDom);
            }
        }
        addRequirementToTrace(traceability, requirements);
    }

    private void addRequirementToTrace(TraceDom traceability, List<RequirementDom> requirements) {
        for (RequirementDom requirement : requirements){
            RequirementType requirementType = reqTypeRepository.findOne(requirement.getRequirementTypeId());
            switch (requirementType.getType()){
                case SCOPE:
                    traceability.getScope().add(requirement);
                    break;
                case ENGINEERING:
                    traceability.getEngineering().add(requirement);
                    break;
                case USER_EXP:
                    traceability.getUserExp().add(requirement);
                    break;
                case QA:
                    traceability.getQuality().add(requirement);
                    break;
            }
        }
    }
}
