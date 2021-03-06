/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.UseCaseDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Repository.*;
import com.Sergio.EasyRMT.Service.Converter.UseCaseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UseCaseService {
    private ObjectRepository objectRepository;
    private FeatureRepository featureRepository;
    private ProjectRepository projectRepository;
    private UseCaseRepository useCaseRepository;
    private UseCaseConverter useCaseConverter;
    private DocumentService documentService;
    private UserRepository userRepository;

    @Autowired
    public UseCaseService(ObjectRepository objectRepository, FeatureRepository featureRepository, ProjectRepository projectRepository,
                          UseCaseRepository useCaseRepository, UseCaseConverter useCaseConverter,
                          DocumentService documentService, UserRepository userRepository) {
        this.objectRepository = objectRepository;
        this.featureRepository = featureRepository;
        this.projectRepository = projectRepository;
        this.useCaseRepository = useCaseRepository;
        this.useCaseConverter = useCaseConverter;
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    /**
     * This method gets a list of existing use cases in a feature from db and provides it
     * @param featureId id of feature related
     * @return List of {@link UseCaseDom} with existing userStories
     */
    @Transactional(readOnly = true)
    public List<UseCaseDom> getUseCases(int featureId) {
        List<UseCase> useCases = useCaseRepository.findByFeatureId(featureId);
        List<UseCaseDom> useCaseDomList = useCaseConverter.toDomain(useCases);
        return useCaseDomList;
    }

    /**
     * This method gets an usecase from db and provides it
     * @param useCaseId id of use case fo be found
     * @return {@link UseCaseDom} Use case converted to domain object
     */
    @Transactional(readOnly = true)
    public UseCaseDom getUseCase(int useCaseId) {
        UseCase useCase = useCaseRepository.findOne(useCaseId);
        UseCaseDom useCaseDom = useCaseConverter.toDomain(useCase);
        return useCaseDom;
    }

    /**
     * Method which interacts with {@link ProjectRepository}, {@link ObjectRepository}, {@link FeatureRepository}
     * {@link UseCaseRepository} and {@link UseCaseConverter}
     * The UserStoryController method "createUseCase" calls this method and provides it a {@link UseCaseDom}
     * object filled with information, a feature id and a project id. Then, the method create, parse the attributes
     * to an {@link UseCase} object using UseCaseConverter class. When the conversion  finish, create calls
     * {@link UseCaseRepository} to persist data.
     * @param featureId {@link Feature} featureId
     * @param useCaseDom {@link UseCaseDom} object with information.
     * @param projectId projectId
     * @return {@link UseCaseDom} object with information persisted
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public UseCaseDom create(UseCaseDom useCaseDom, int featureId, int projectId) {
        ObjectEntity objectEntity = new ObjectEntity();
        Optional<Project> project;
        Feature featureModel;
        UseCase useCase;
        project = projectRepository.findByIdProject(projectId);
        featureModel = featureRepository.findOne(featureId);
        if(project.isPresent()){
            objectEntity.setProject(project.get());
            objectEntity = objectRepository.save(objectEntity);
        }
        User author = userRepository.findOne(useCaseDom.getAuthorId());
        User assignedTo = userRepository.findOne(useCaseDom.getAssignedId());
        Date timestamp = new Date();
        useCaseDom.setCreated(timestamp);
        useCaseDom.setLastUpdated(timestamp);
        useCase = useCaseConverter.toModel(useCaseDom);
        useCase.setAuthor(author);
        useCase.setAssignedTo(assignedTo);
        useCase.setIdUseCase(objectEntity.getIdobject());
        useCase.setFeature(featureModel);
        useCase.setObject(objectEntity);
        useCaseRepository.save(useCase);
        ObjectEntity feature = objectRepository.findOne(featureId);
        feature.getTraced().add(objectEntity);
        feature = objectRepository.save(feature);
        objectEntity.getTraced().add(feature);
        objectRepository.save(objectEntity);
        useCaseDom = useCaseConverter.toDomain(useCase);
        return useCaseDom;
    }

    public UseCaseDom update(int projectId, int featureId, int useCaseId, UseCaseDom useCaseDom) {
        UseCase useCaseModel = useCaseRepository.findOne(useCaseId);
        UseCaseDom useCaseDom1;
        boolean changed = false;
        if(!useCaseModel.getIdentifier().equals(useCaseDom.getIdentifier())){
            changed = true;
            useCaseModel.setIdentifier(useCaseDom.getIdentifier());
        }
        if(!useCaseModel.getDescription().equals(useCaseDom.getDescription())){
            changed = true;
            useCaseModel.setDescription(useCaseDom.getDescription());
        }
        if(!useCaseModel.getPriority().equals(useCaseDom.getPriority())){
            changed = true;
            useCaseModel.setPriority(useCaseDom.getPriority());
        }
        if(!useCaseModel.getState().equals(useCaseDom.getState())){
            changed = true;
            useCaseModel.setState(useCaseDom.getState());
        }
        if(!useCaseModel.getStoryPoints().equals(useCaseDom.getStoryPoints())){
            changed = true;
            useCaseModel.setStoryPoints(useCaseDom.getStoryPoints());
        }
        if(!useCaseModel.getRisk().equals(useCaseDom.getRisk())){
            changed = true;
            useCaseModel.setRisk(useCaseDom.getRisk());
        }
        if(!useCaseModel.getAssignedTo().equals(useCaseDom.getAssignedTo())){
            changed = true;
            User user = userRepository.findOne(useCaseDom.getAssignedId());
            useCaseModel.setAssignedTo(user);
        }
        if(!useCaseModel.getVersion().equals(useCaseDom.getVersion())){
            changed = true;
            useCaseModel.setVersion(useCaseDom.getVersion());
        }
        if(!useCaseModel.getSource().equals(useCaseDom.getSource())){
            changed = true;
            useCaseModel.setSource(useCaseDom.getSource());
        }
        if(!useCaseModel.getComplexity().equals(useCaseDom.getComplexity())){
            changed = true;
            useCaseModel.setComplexity(useCaseDom.getComplexity());
        }
        if(!useCaseModel.getCost().equals(useCaseDom.getCost())){
            changed = true;
            useCaseModel.setCost(useCaseDom.getCost());
        }
        if(!useCaseModel.getEstimatedHours().equals(useCaseDom.getEstimatedHours())){
            changed = true;
            useCaseModel.setEstimatedHours(useCaseDom.getEstimatedHours());
        }
        if(!useCaseModel.getScope().equals(useCaseDom.getScope())){
            changed = true;
            useCaseModel.setScope(useCaseDom.getScope());
        }
        if(!useCaseModel.getValidationMethod().equals(useCaseDom.getValidationMethod())){
            changed = true;
            useCaseModel.setValidationMethod(useCaseDom.getValidationMethod());
        }
        if(!useCaseModel.getJustification().equals(useCaseDom.getJustification())){
            changed = true;
            useCaseModel.setJustification(useCaseDom.getJustification());
        }
        if(!useCaseModel.getTestCases().equals(useCaseDom.getTestCases())){
            changed = true;
            useCaseModel.setTestCases(useCaseDom.getTestCases());
        }
        if(!useCaseModel.getActors().equals(useCaseDom.getActors())){
            changed = true;
            useCaseModel.setActors(useCaseDom.getActors());
        }
        if(!useCaseModel.getPreconditions().equals(useCaseDom.getPreconditions())){
            changed = true;
            useCaseModel.setPreconditions(useCaseDom.getPreconditions());
        }
        if(!useCaseModel.getSteps().equals(useCaseDom.getSteps())){
            changed = true;
            useCaseModel.setSteps(useCaseDom.getSteps());
        }
        if(!useCaseModel.getPostconditions().equals(useCaseDom.getPostconditions())){
            changed = true;
            useCaseModel.setPostconditions(useCaseDom.getPostconditions());
        }
        if(changed){
            Date timestamp = new Date();
            useCaseModel.setLastUpdated(timestamp);
            useCaseModel = useCaseRepository.save(useCaseModel);
        }
        useCaseDom1 = useCaseConverter.toDomain(useCaseModel);
        return useCaseDom1;
    }

    /**
     * This method calls the JPA Repository to delete an useCase with the useCaseId received as parameter.
     * Then, calls again the {@link ObjectRepository} to check if useCase has been deleted.
     * If not exists in database, method will return true and false if exists.
     * @param useCaseId UseCaseId to be deleted
     * @return If not exists in database, method will return true and false if exists.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUseCase(int useCaseId) {
        if (objectRepository.exists(useCaseId)){
            ObjectEntity object= objectRepository.findOne(useCaseId);
            documentService.deleteFiles(object.getProject().getIdProject(),object.getIdobject());
            objectRepository.deleteObject(useCaseId);
            if(objectRepository.exists(useCaseId) ||useCaseRepository.exists(useCaseId)){
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * This method returns a list of all use cases related with a project
     * @param projectId
     * @return List of {@link UseCaseDom}
     */
    @Transactional(readOnly = true)
    public List<UseCaseDom> getByProjectID(int projectId) {
        List<UseCase> useCaseList = useCaseRepository.findByProjectId(projectId);
        List<UseCaseDom> useCaseDomList = useCaseConverter.toDomain(useCaseList);
        return useCaseDomList;
    }

    /**
     * This methods checks if exists a use case in DB
     * @param idobject object to check if exists
     * @return true if exists or false if not exists
     */
    @Transactional(readOnly = true)
    public boolean exists(int idobject) {
        return useCaseRepository.exists(idobject);
    }
}
