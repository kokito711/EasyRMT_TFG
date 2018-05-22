/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.UserStoryDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Repository.*;
import com.Sergio.EasyRMT.Service.Converter.UserStoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserStoryService {
    private ObjectRepository objectRepository;
    private EpicRepository epicRepository;
    private ProjectRepository projectRepository;
    private UserStoryRepository userStoryRepository;
    private UserStoryConverter userStoryConverter;
    private DocumentService documentService;
    private UserRepository userRepository;

    @Autowired
    public UserStoryService(ObjectRepository objectRepository, EpicRepository epicRepository, ProjectRepository projectRepository,
                            UserStoryRepository userStoryRepository, UserStoryConverter userStoryConverter,
                            DocumentService documentService, UserRepository userRepository) {
        this.objectRepository = objectRepository;
        this.epicRepository = epicRepository;
        this.projectRepository = projectRepository;
        this.userStoryRepository = userStoryRepository;
        this.userStoryConverter = userStoryConverter;
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    /**
     * This method gets a list of existing user stories in an epic in db and provides it
     * @param epicId id of epic related
     * @return List of {@link UserStoryDom} with existing userStories
     */
    @Transactional(readOnly = true)
    public List<UserStoryDom> getUserStories(int epicId) {
        List<UserStory> userStories = userStoryRepository.findByEpicId(epicId);
        List<UserStoryDom> userStoryDomList = userStoryConverter.toDomain(userStories);
        return userStoryDomList;
    }

    /**
     * This method gets an user story from db and provides it
     * @param userStoryId id of user story fo be found
     * @return {@link UserStoryDom} User story converted to domain object
     */
    @Transactional(readOnly = true)
    public UserStoryDom getUserStory(int userStoryId) {
        UserStory userStory = userStoryRepository.findOne(userStoryId);
        UserStoryDom userStoryDom = userStoryConverter.toDomain(userStory);
        return userStoryDom;
    }

    /**
     * Method which interacts with {@link ProjectRepository}, {@link ObjectRepository}, {@link EpicRepository}
     * {@link UserStoryRepository} and {@link UserStoryConverter}
     * The UserStoryController method "createUserStory" calls this method and provides it a {@link UserStoryDom}
     * object filled with information, an epic id and a project id. Then, the method createEpic, parse the attributes
     * to an {@link UserStory} object using UserStoryConverter class. When the conversion  finish, create calls
     * {@link UserStoryRepository} to persist data.
     * @param epicId {@link Epic} epicId
     * @param userStoryDom {@link UserStoryDom} object with information.
     * @param projectId projectId
     * @return {@link UserStoryDom} object with information persisted
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public UserStoryDom create(UserStoryDom userStoryDom, int epicId, int projectId) {
        ObjectEntity objectEntity = new ObjectEntity();
        Optional<Project> project;
        Epic epicModel;
        UserStory userStoryModel;
        project = projectRepository.findByIdProject(projectId);
        epicModel = epicRepository.findOne(epicId);
        if(project.isPresent()){
            objectEntity.setProject(project.get());
            objectEntity = objectRepository.save(objectEntity);
        }
        User author = userRepository.findOne(userStoryDom.getAuthorId());
        User assignedTo = userRepository.findOne(userStoryDom.getAssignedId());
        Date timestamp = new Date();
        userStoryDom.setCreated(timestamp);
        userStoryDom.setLastUpdated(timestamp);
        userStoryModel = userStoryConverter.toModel(userStoryDom);
        userStoryModel.setAuthor(author);
        userStoryModel.setAssignedTo(assignedTo);
        userStoryModel.setIdUserStory(objectEntity.getIdobject());
        userStoryModel.setEpic(epicModel);
        userStoryModel.setObject(objectEntity);
        userStoryModel = userStoryRepository.save(userStoryModel);
        ObjectEntity epic = objectRepository.findOne(epicId);
        epic.getTraced().add(objectEntity);
        epic = objectRepository.save(epic);
        objectEntity.getTraced().add(epic);
        objectRepository.save(objectEntity);
        userStoryDom = userStoryConverter.toDomain(userStoryModel);
        return userStoryDom;
    }

    public UserStoryDom update(int projectId, int epicId, int userStoryId, UserStoryDom userStoryDom) {
        UserStory userStoryModel = userStoryRepository.findOne(userStoryId);
        UserStoryDom userStoryDom1;
        boolean changed = false;
        if(!userStoryModel.getIdentifier().equals(userStoryDom.getIdentifier())){
            changed = true;
            userStoryModel.setIdentifier(userStoryDom.getIdentifier());
        }
        if(!userStoryModel.getDescription().equals(userStoryDom.getDescription())){
            changed = true;
            userStoryModel.setDescription(userStoryDom.getDescription());
        }
        if(!userStoryModel.getDefinitionOfDone().equals(userStoryDom.getDefinitionOfDone())){
            changed = true;
            userStoryModel.setDefinitionOfDone(userStoryDom.getDefinitionOfDone());
        }
        if(!userStoryModel.getPriority().equals(userStoryDom.getPriority())){
            changed = true;
            userStoryModel.setPriority(userStoryDom.getPriority());
        }
        if(!userStoryModel.getState().equals(userStoryDom.getState())){
            changed = true;
            userStoryModel.setState(userStoryDom.getState());
        }
        if(!userStoryModel.getStoryPoints().equals(userStoryDom.getStoryPoints())){
            changed = true;
            userStoryModel.setStoryPoints(userStoryDom.getStoryPoints());
        }
        if(!userStoryModel.getRisk().equals(userStoryDom.getRisk())){
            changed = true;
            userStoryModel.setRisk(userStoryDom.getRisk());
        }
        if(!userStoryModel.getAssignedTo().equals(userStoryDom.getAssignedTo())){
            changed = true;
            User user = userRepository.findOne(userStoryDom.getAssignedId());
            userStoryModel.setAssignedTo(user);
        }
        if(!userStoryModel.getVersion().equals(userStoryDom.getVersion())){
            changed = true;
            userStoryModel.setVersion(userStoryDom.getVersion());
        }
        if(!userStoryModel.getSource().equals(userStoryDom.getSource())){
            changed = true;
            userStoryModel.setSource(userStoryDom.getSource());
        }
        if(!userStoryModel.getComplexity().equals(userStoryDom.getComplexity())){
            changed = true;
            userStoryModel.setComplexity(userStoryDom.getComplexity());
        }
        if(!userStoryModel.getCost().equals(userStoryDom.getCost())){
            changed = true;
            userStoryModel.setCost(userStoryDom.getCost());
        }
        if(!userStoryModel.getEstimatedHours().equals(userStoryDom.getEstimatedHours())){
            changed = true;
            userStoryModel.setEstimatedHours(userStoryDom.getEstimatedHours());
        }
        if(!userStoryModel.getScope().equals(userStoryDom.getScope())){
            changed = true;
            userStoryModel.setScope(userStoryDom.getScope());
        }
        if(!userStoryModel.getValidationMethod().equals(userStoryDom.getValidationMethod())){
            changed = true;
            userStoryModel.setValidationMethod(userStoryDom.getValidationMethod());
        }
        if(!userStoryModel.getJustification().equals(userStoryDom.getJustification())){
            changed = true;
            userStoryModel.setJustification(userStoryDom.getJustification());
        }
        if(!userStoryModel.getTestCases().equals(userStoryDom.getTestCases())){
            changed = true;
            userStoryModel.setTestCases(userStoryDom.getTestCases());
        }
        if(changed){
            Date timestamp = new Date();
            userStoryModel.setLastUpdated(timestamp);
            userStoryModel = userStoryRepository.save(userStoryModel);
        }
        userStoryDom1 = userStoryConverter.toDomain(userStoryModel);
        return userStoryDom1;
    }


    /**
     * This method calls the JPA Repository to delete an userStory with the userStoryId received as parameter.
     * Then, calls again the {@link ObjectRepository} to check if user story has been deleted.
     * If not exists in database, method will return true and false if exists.
     * @param userStoryId userStoryId to be deleted
     * @return If not exists in database, method will return true and false if exists.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserStory(int userStoryId) {
        if (objectRepository.exists(userStoryId)){
            ObjectEntity object= objectRepository.findOne(userStoryId);
            documentService.deleteFiles(object.getProject().getIdProject(),object.getIdobject());
            objectRepository.deleteObject(userStoryId);
            if(objectRepository.exists(userStoryId) ||userStoryRepository.exists(userStoryId)){
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
     * This method returns a list of all user stories related with a project
     * @param projectId
     * @return
     */
    @Transactional(readOnly = true)
    public List<UserStoryDom> getByProjectID(int projectId) {
        List<UserStory> userStoryList = userStoryRepository.findByProjectId(projectId);
        List<UserStoryDom> userStoryDomList = userStoryConverter.toDomain(userStoryList);
        return userStoryDomList;
    }

    /**
     * This methods checks if exists a user story in DB
     * @param idobject object to check if exists
     * @return true if exists or false if not exists
     */
    @Transactional(readOnly = true)
    public boolean exists(int idobject) {
        return userStoryRepository.exists(idobject);
    }
}
