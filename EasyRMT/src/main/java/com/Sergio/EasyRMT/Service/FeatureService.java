/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.FeatureDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Repository.FeatureRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
import com.Sergio.EasyRMT.Service.Converter.FeatureConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeatureService {
    private ObjectRepository objectRepository;
    private FeatureRepository featureRepository;
    private ProjectRepository projectRepository;
    private FeatureConverter featureConverter;
    private DocumentService documentService;
    private UserRepository userRepository;

    @Autowired
    public FeatureService(ObjectRepository objectRepository, FeatureRepository featureRepository,
                          ProjectRepository projectRepository, FeatureConverter featureConverter,
                          DocumentService documentService, UserRepository userRepository) {
        this.objectRepository = objectRepository;
        this.featureRepository = featureRepository;
        this.projectRepository = projectRepository;
        this.featureConverter = featureConverter;
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    /**
     * This method gets a list of existing features in a project in db and provides it
     * @param projectId id of project related
     * @return List of {@link FeatureDom} with existing features
     */
    @Transactional(readOnly = true)
    public List<FeatureDom> getFeatures(int projectId){
        List<Feature> featureList = featureRepository.findByProjectID(projectId);
        List<FeatureDom> featureDomList = featureConverter.toDomain(featureList);
        return featureDomList;
    }

    /**
     * This method gets a feature from db and provides it
     * @param featureId of feature fo be found
     * @return {@link FeatureDom} Feature converted to domain object
     */
    @Transactional(readOnly = true)
    public FeatureDom getFeature(int featureId){
        Feature feature = featureRepository.findOne(featureId);
        FeatureDom featureDom = featureConverter.toDomain(feature);
        return featureDom;
    }

    /**
     * Method which interacts with {@link ProjectRepository}, {@link ObjectRepository}, {@link FeatureRepository}
     * and {@link FeatureConverter}
     * The FeatureController method "createFeature" calls this method and provides it a {@link FeatureDom} object filled
     * with information and a project id. Then, the method createFeature, parse the attributes to an {@link Feature}
     * object using FeatureConverter class. When the conversion  finish, createFeature calls {@link FeatureRepository}
     * to persist data.
     * @param featureDom {@link FeatureDom} object with information.
     * @param projectId projectId
     * @return {@link FeatureDom} object with information persisted
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public FeatureDom create(FeatureDom featureDom, int projectId) {
        ObjectEntity objectEntity = new ObjectEntity();
        Optional<Project> project;
        Feature feature;
        project = projectRepository.findByIdProject(projectId);
        if(project.isPresent()){
            objectEntity.setProject(project.get());
            objectEntity = objectRepository.save(objectEntity);
        }
        User author = userRepository.findOne(featureDom.getAuthorId());
        User assignedTo = userRepository.findOne(featureDom.getAssignedId());
        Date timestamp = new Date();
        featureDom.setCreated(timestamp);
        featureDom.setLastUpdated(timestamp);
        feature = featureConverter.toModel(featureDom);
        feature.setAuthor(author);
        feature.setAssignedTo(assignedTo);
        feature.setIdFeature(objectEntity.getIdobject());
        feature.setUseCases(new ArrayList<>());
        feature.setObject(objectEntity);
        feature = featureRepository.save(feature);
        featureDom = featureConverter.toDomain(feature);
        return featureDom;
    }

    /**
     * The update method receives an id and a {@link FeatureDom} with data modified.
     * Then obtains de persisted feature from database and compares if there is any change.
     * If the feature information has been changed, this method will persist the changes.
     * @param featureId feature Id
     * @param featureDom {@link FeatureDom} object with changed data
     * @param projectId projectId
     * @return {@link FeatureDom} with persisted information
     */
    @Transactional(rollbackFor = Exception.class)
    public FeatureDom update(FeatureDom featureDom, int featureId, int projectId) {
        Feature feature = featureRepository.findOne(featureId);
        FeatureDom featureDom1;
        boolean changed = false;
        if(!feature.getIdentifier().equals(featureDom.getIdentifier())){
            changed = true;
            feature.setIdentifier(featureDom.getIdentifier());
        }
        if(!feature.getDescription().equals(featureDom.getDescription())){
            changed = true;
            feature.setDescription(featureDom.getDescription());
        }
        if(!feature.getPriority().equals(featureDom.getPriority())){
            changed = true;
            feature.setPriority(featureDom.getPriority());
        }
        if(!feature.getState().equals(featureDom.getState())){
            changed = true;
            feature.setState(featureDom.getState());
        }
        if(!feature.getStoryPoints().equals(featureDom.getStoryPoints())){
            changed = true;
            feature.setStoryPoints(featureDom.getStoryPoints());
        }
        if(!feature.getRisk().equals(featureDom.getRisk())){
            changed = true;
            feature.setRisk(featureDom.getRisk());
        }
        if(!feature.getAssignedTo().equals(featureDom.getAssignedTo())){
            changed = true;
            User user = userRepository.findOne(featureDom.getAssignedId());
            feature.setAssignedTo(user);
        }
        if(!feature.getVersion().equals(featureDom.getVersion())){
            changed = true;
            feature.setVersion(featureDom.getVersion());
        }
        if(!feature.getSource().equals(featureDom.getSource())){
            changed = true;
            feature.setSource(featureDom.getSource());
        }
        if(!feature.getComplexity().equals(featureDom.getComplexity())){
            changed = true;
            feature.setComplexity(featureDom.getComplexity());
        }
        if(!feature.getCost().equals(featureDom.getCost())){
            changed = true;
            feature.setCost(featureDom.getCost());
        }
        if(!feature.getEstimatedHours().equals(featureDom.getEstimatedHours())){
            changed = true;
            feature.setEstimatedHours(featureDom.getEstimatedHours());
        }
        if(!feature.getScope().equals(featureDom.getScope())){
            changed = true;
            feature.setScope(featureDom.getScope());
        }
        if(!feature.getValidationMethod().equals(featureDom.getValidationMethod())){
            changed = true;
            feature.setValidationMethod(featureDom.getValidationMethod());
        }
        if(!feature.getJustification().equals(featureDom.getJustification())){
            changed = true;
            feature.setJustification(featureDom.getJustification());
        }
        if(!feature.getTestCases().equals(featureDom.getTestCases())){
            changed = true;
            feature.setTestCases(featureDom.getTestCases());
        }
        if(changed){
            Date timestamp = new Date();
            feature.setLastUpdated(timestamp);
            feature = featureRepository.save(feature);
        }
        featureDom1 = featureConverter.toDomain(feature);
        return featureDom1;
    }

    /**
     * This method calls the JPA Repository to delete a feature with the featureId received as parameter.
     * Then, calls again the {@link ObjectRepository} to check if feature has been deleted.
     * If not exists in database, method will return true and false if exists.
     * @param featureId featureId to be deleted
     * @return If not exists in database, method will return true and false if exists.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFeature(int featureId) {
        if (objectRepository.exists(featureId)){
            Feature feature = featureRepository.findOne(featureId);
            if(!feature.getUseCases().isEmpty()){
                for(UseCase useCase: feature.getUseCases()){
                    ObjectEntity object = useCase.getObject();
                    documentService.deleteFiles(object.getProject().getIdProject(),object.getIdobject());
                    objectRepository.deleteObject(useCase.getIdUseCase());
                }
            }
            ObjectEntity object = feature.getObject();
            documentService.deleteFiles(object.getProject().getIdProject(),object.getIdobject());
            objectRepository.deleteObject(featureId);
            if(objectRepository.exists(featureId) ||featureRepository.exists(featureId)){
                return false;
            }
            else{
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * This methods checks if exists a feature in DB
     * @param idobject object to check if exists
     * @return true if exists or false if not exists
     */
    @Transactional(readOnly = true)
    public boolean exists(int idobject) {
        return featureRepository.exists(idobject);
    }
}
