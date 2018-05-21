/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.RequirementDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Repository.*;
import com.Sergio.EasyRMT.Service.Converter.RequirementConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequirementService {
    private ObjectRepository objectRepository;
    private RequirementConverter requirementConverter;
    private ProjectRepository projectRepository;
    private RequirementRepository requirementRepository;
    private ReqTypeRepository reqTypeRepository;
    private DocumentService documentService;
    private UserRepository userRepository;

    @Autowired
    public RequirementService(ObjectRepository objectRepository, RequirementConverter requirementConverter,
                              ProjectRepository projectRepository, RequirementRepository requirementRepository,
                              ReqTypeRepository reqTypeRepository, DocumentService documentService,
                              UserRepository userRepository) {
        this.objectRepository = objectRepository;
        this.requirementConverter = requirementConverter;
        this.projectRepository = projectRepository;
        this.requirementRepository = requirementRepository;
        this.reqTypeRepository = reqTypeRepository;
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    /**
     * This method gets a list of existing requirements in a project from db and provides it
     * @param projectId id of project related
     * @return List of {@link RequirementDom} with existing epics
     */
    @Transactional(readOnly = true)
    public List<RequirementDom> getRequirements(int projectId){
        List<Requirement> requirementList = requirementRepository.findByProjectID(projectId);
        List<RequirementDom> requirementDomList = requirementConverter.toDomain(requirementList);
        return requirementDomList;
    }

    /**
     * This method gets a requirement from db and provides it
     * @param requirementId id of requirement fo be found
     * @return {@link RequirementDom} Requirement converted to domain object
     */
    @Transactional(readOnly = true)
    public RequirementDom getRequirement(int requirementId){
        Requirement requirement = requirementRepository.findOne(requirementId);
        RequirementDom requirementDom = requirementConverter.toDomain(requirement);
        return requirementDom;
    }

    /**
     * Method which interacts with {@link ProjectRepository}, {@link ObjectRepository}, {@link RequirementRepository}
     * and {@link RequirementConverter}
     * The RequirementController method "createRequirement" calls this method and provides it a {@link RequirementDom}
     * object filled with information and a project id. Then, the method createRequirement, parse the attributes to an
     * {@link Requirement} object using RequirementConverter class. When the conversion  finish, createRequirement calls
     * {@link RequirementRepository} to persist data.
     * @param requirementDom {@link RequirementDom} object with information.
     * @param projectId projectId
     * @return {@link RequirementDom} object with information persisted
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public RequirementDom create(RequirementDom requirementDom, int projectId) {
        ObjectEntity objectEntity = new ObjectEntity();
        Optional<Project> project;
        Requirement requirement;
        project = projectRepository.findByIdProject(projectId);
        if(project.isPresent()){
            objectEntity.setProject(project.get());
            objectEntity = objectRepository.save(objectEntity);
        }
        User author = userRepository.findOne(requirementDom.getAuthorId());
        User assignedTo = userRepository.findOne(requirementDom.getAssignedId());
        Date timestamp = new Date();
        requirementDom.setCreated(timestamp);
        requirementDom.setLastUpdated(timestamp);
        requirement = requirementConverter.toModel(requirementDom);
        requirement.setAuthor(author);
        requirement.setAssignedTo(assignedTo);
        requirement.setIdRequirement(objectEntity.getIdobject());
        Integer reqTypeId = requirementDom.getRequirementTypeId();
        Optional<RequirementType> requirementType = reqTypeRepository.findByIdType(reqTypeId);
        requirement.setRequirementType(requirementType.get());
        requirement.setObject(objectEntity);
        requirement = requirementRepository.save(requirement);
        requirementDom = requirementConverter.toDomain(requirement);
        return requirementDom;
    }

    /**
     * The update method receives an id and a {@link RequirementDom} with data modified.
     * Then obtains de persisted requirement from database and compares if there is any change.
     * If the requirement information has been changed, this method will persist the changes.
     * @param requirementId requirement Id
     * @param requirementObtained {@link RequirementDom} object with changed data
     * @param projectId projectId
     * @return {@link RequirementDom} with persisted information
     */
    @Transactional(rollbackFor = Exception.class)
    public RequirementDom update(RequirementDom requirementObtained, int requirementId, int projectId) {
        Requirement requirement = requirementRepository.findOne(requirementId);
        RequirementDom requirementDom;
        boolean changed = false;
        if(!requirement.getIdentifier().equals(requirementObtained.getIdentifier())
                && requirementObtained.getIdentifier()!=null){
            changed = true;
            requirement.setIdentifier(requirementObtained.getIdentifier());
        }
        if(!requirement.getDescription().equals(requirementObtained.getDescription())
                && requirementObtained.getDescription()!=null){
            changed = true;
            requirement.setDescription(requirementObtained.getDescription());
        }
        if(!requirement.getPriority().equals(requirementObtained.getPriority())
                && requirementObtained.getPriority()!=null){
            changed = true;
            requirement.setPriority(requirementObtained.getPriority());
        }
        if(!requirement.getState().equals(requirementObtained.getState())
                && requirementObtained.getState()!=null){
            changed = true;
            requirement.setState(requirementObtained.getState());
        }
        if(!requirement.getStoryPoints().equals(requirementObtained.getStoryPoints())
                && requirementObtained.getStoryPoints()!=null){
            changed = true;
            requirement.setStoryPoints(requirementObtained.getStoryPoints());
        }
        if(!requirement.getRisk().equals(requirementObtained.getRisk())
                && requirementObtained.getRisk()!=null){
            changed = true;
            requirement.setRisk(requirementObtained.getRisk());
        }
        if(!requirement.getAssignedTo().equals(requirementObtained.getAssignedTo())
                && requirementObtained.getAssignedTo()!=null){
            changed = true;
            User user = userRepository.findOne(requirementObtained.getAssignedId());
            requirement.setAssignedTo(user);
        }
        if(!requirement.getVersion().equals(requirementObtained.getVersion())
                && requirementObtained.getVersion()!=null){
            changed = true;
            requirement.setVersion(requirementObtained.getVersion());
        }
        if(!requirement.getSource().equals(requirementObtained.getSource())
                && requirementObtained.getSource()!=null){
            changed = true;
            requirement.setSource(requirementObtained.getSource());
        }
        if(!requirement.getComplexity().equals(requirementObtained.getComplexity())
                && requirementObtained.getComplexity()!=null){
            changed = true;
            requirement.setComplexity(requirementObtained.getComplexity());
        }
        if(!requirement.getCost().equals(requirementObtained.getCost())
                && requirementObtained.getCost()!=null){
            changed = true;
            requirement.setCost(requirementObtained.getCost());
        }
        if(!requirement.getEstimatedHours().equals(requirementObtained.getEstimatedHours())
                && requirementObtained.getEstimatedHours()!=null){
            changed = true;
            requirement.setEstimatedHours(requirementObtained.getEstimatedHours());
        }
        if(!requirement.getScope().equals(requirementObtained.getScope())
                && requirementObtained.getScope()!=null){
            changed = true;
            requirement.setScope(requirementObtained.getScope());
        }
        if(!requirement.getValidationMethod().equals(requirementObtained.getValidationMethod())
                && requirementObtained.getValidationMethod()!=null){
            changed = true;
            requirement.setValidationMethod(requirementObtained.getValidationMethod());
        }
        if(!requirement.getJustification().equals(requirementObtained.getJustification())
                && requirementObtained.getJustification()!=null){
            changed = true;
            requirement.setJustification(requirementObtained.getJustification());
        }
        if(!requirement.getTestCases().equals(requirementObtained.getTestCases())
                && requirementObtained.getTestCases()!=null){
            changed = true;
            requirement.setTestCases(requirementObtained.getTestCases());
        }
        if(changed){
            Date timestamp = new Date();
            requirement.setLastUpdated(timestamp);
            requirement = requirementRepository.save(requirement);
        }
        requirementDom = requirementConverter.toDomain(requirement);
        return requirementDom;
    }

    /**
     * This method calls the JPA Repository to delete a requirement with the requirementId received as parameter.
     * Then, calls again the {@link ObjectRepository} to check if requirement has been deleted.
     * If not exists in database, method will return true and false if exists.
     * @param requirementId requirementId to be deleted
     * @return If not exists in database, method will return true and false if exists.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRequirement(int requirementId) {
        if (objectRepository.exists(requirementId)){
            //TODO change this when traceability is done
            ObjectEntity object= objectRepository.findOne(requirementId);
            documentService.deleteFiles(object.getProject().getIdProject(),object.getIdobject());
            objectRepository.deleteObject(requirementId);
            if(objectRepository.exists(requirementId) ||requirementRepository.exists(requirementId)){
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
     * This methods checks if exists a requirement in DB
     * @param idobject object to check if exists
     * @return true if exists or false if not exists
     */
    @Transactional(readOnly = true)
    public boolean exists(int idobject) {
        return requirementRepository.exists(idobject);
    }
}
