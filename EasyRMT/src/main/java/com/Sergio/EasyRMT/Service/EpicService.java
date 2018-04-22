/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Model.Epic;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.UserStory;
import com.Sergio.EasyRMT.Repository.EpicRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Service.Converter.EpicConverter;
import com.Sergio.EasyRMT.Service.Converter.ProjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EpicService {
    ObjectRepository objectRepository;
    EpicRepository epicRepository;
    ProjectRepository projectRepository;
    EpicConverter epicConverter;

    @Autowired
    public EpicService(ObjectRepository objectRepository, EpicRepository epicRepository,
                       ProjectRepository projectRepository, EpicConverter epicConverter) {
        this.objectRepository = objectRepository;
        this.epicRepository = epicRepository;
        this.projectRepository = projectRepository;
        this.epicConverter = epicConverter;
    }

    /**
     * This method gets a list of existing epics in a project in db and provides it
     * @param projectId id of project related
     * @return List of {@link EpicDom} with existing epics
     */
    @Transactional(readOnly = true)
    public List<EpicDom> getEpics(int projectId){
        List<Epic> epicList = epicRepository.findByProjectID(projectId);
        List<EpicDom> epicDomList = epicConverter.toDomain(epicList);
        return epicDomList;
    }

    /**
     * This method gets an epic in db and provides it
     * @param epicId id of epic fo be found
     * @return {@link EpicDom} Epic converted to domain object
     */
    @Transactional(readOnly = true)
    public EpicDom getEpic(int epicId){
        Epic epic = epicRepository.findOne(epicId);
        EpicDom epicDom = epicConverter.toDomain(epic);
        return epicDom;
    }

    /**
     * Method which interacts with {@link ProjectRepository}, {@link ObjectRepository}, {@link EpicRepository}
     * and {@link EpicConverter}
     * The EpicController method "createEpic" calls this method and provides it a {@link EpicDom} object filled
     * with information and a project id. Then, the method createEpic, parse the attributes to an {@link Epic} object using
     * EpicConverter class. When the conversion  finish, createEpic call {@link EpicRepository} to persist data.
     * @param epic {@link EpicDom} object with information.
     * @param projectId projectId
     * @return {@link EpicDom} object with information persisted
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public EpicDom create(EpicDom epic, int projectId) {
        ObjectEntity objectEntity = new ObjectEntity();
        Optional<Project> project;
        Epic epicModel;
        project = projectRepository.findByIdProject(projectId);
        if(project.isPresent()){
            objectEntity.setProject(project.get());
            objectEntity = objectRepository.save(objectEntity);
        }

        Date timestamp = new Date();
        epic.setCreated(timestamp);
        epic.setLastUpdated(timestamp);
        epic.setAuthor(0); //TODO change this on second iteration
        epicModel = epicConverter.toModel(epic);
        epicModel.setIdEpic(objectEntity.getIdobject());
        epicModel.setUserStories(new ArrayList<>());
        epicModel.setObject(objectEntity);
        epicModel = epicRepository.save(epicModel);
        epic = epicConverter.toDomain(epicModel);
        return epic;
    }

    /**
     * The update method receives an id and a {@link EpicDom} with data modified.
     * Then obtains de persisted epic from database and compares if there is any change.
     * If the epic information has been changed, this method will persist the changes.
     * @param epicId epic Id
     * @param epic {@link EpicDom} object with changed data
     * @param projectId projectId
     * @return {@link EpicDom} with persisted information
     */
    @Transactional(rollbackFor = Exception.class)
    public EpicDom update(EpicDom epic, int epicId, int projectId) {
        Epic epicModel = epicRepository.findOne(epicId);
        EpicDom epicDom;
        boolean changed = false;
        if(!epicModel.getIdentifier().equals(epic.getIdentifier())){
            changed = true;
            epicModel.setIdentifier(epic.getIdentifier());
        }
        if(!epicModel.getDescription().equals(epic.getDescription())){
            changed = true;
            epicModel.setDescription(epic.getDescription());
        }
        if(!epicModel.getDefinitionOfDone().equals(epic.getDefinitionOfDone())){
            changed = true;
            epicModel.setDefinitionOfDone(epic.getDefinitionOfDone());
        }
        if(!epicModel.getPriority().equals(epic.getPriority())){
            changed = true;
            epicModel.setPriority(epic.getPriority());
        }
        if(!epicModel.getState().equals(epic.getState())){
            changed = true;
            epicModel.setState(epic.getState());
        }
        if(!epicModel.getStoryPoints().equals(epic.getStoryPoints())){
            changed = true;
            epicModel.setStoryPoints(epic.getStoryPoints());
        }
        if(!epicModel.getRisk().equals(epic.getRisk())){
            changed = true;
            epicModel.setRisk(epic.getRisk());
        }
        if(!epicModel.getAssignedTo().equals(epic.getAssignedTo())){
            changed = true;
            epicModel.setAssignedTo(epic.getAssignedTo());
        }
        if(!epicModel.getVersion().equals(epic.getVersion())){
            changed = true;
            epicModel.setVersion(epic.getVersion());
        }
        if(!epicModel.getSource().equals(epic.getSource())){
            changed = true;
            epicModel.setSource(epic.getSource());
        }
        if(!epicModel.getComplexity().equals(epic.getComplexity())){
            changed = true;
            epicModel.setComplexity(epic.getComplexity());
        }
        if(!epicModel.getCost().equals(epic.getCost())){
            changed = true;
            epicModel.setCost(epic.getCost());
        }
        if(!epicModel.getEstimatedHours().equals(epic.getEstimatedHours())){
            changed = true;
            epicModel.setEstimatedHours(epic.getEstimatedHours());
        }
        if(!epicModel.getScope().equals(epic.getScope())){
            changed = true;
            epicModel.setScope(epic.getScope());
        }
        if(!epicModel.getValidationMethod().equals(epic.getValidationMethod())){
            changed = true;
            epicModel.setValidationMethod(epic.getValidationMethod());
        }
        if(!epicModel.getJustification().equals(epic.getJustification())){
            changed = true;
            epicModel.setJustification(epic.getJustification());
        }
        if(!epicModel.getTestCases().equals(epic.getTestCases())){
            changed = true;
            epicModel.setTestCases(epic.getTestCases());
        }
        if(changed){
            Date timestamp = new Date();
            epicModel.setLastUpdated(timestamp);
            epicModel = epicRepository.save(epicModel);
        }
        epicDom = epicConverter.toDomain(epicModel);
        return epicDom;
    }

    /**
     * This method calls the JPA Repository to delete an epic with the epicId received as parameter.
     * Then, calls again the {@link ObjectRepository} to check if epic has been deleted.
     * If not exists in database, method will return true and false if exists.
     * @param epicId epicId to be deleted
     * @return If not exists in database, method will return true and false if exists.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEpic(int epicId) {
        if (objectRepository.exists(epicId)){
            Epic epic = epicRepository.findOne(epicId);
            if(!epic.getUserStories().isEmpty()){
                for(UserStory userStory: epic.getUserStories()){
                    objectRepository.deleteObject(userStory.getIdUserStory());
                }
            }
            objectRepository.deleteObject(epicId);
            if(objectRepository.exists(epicId) ||epicRepository.exists(epicId)){
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
}
