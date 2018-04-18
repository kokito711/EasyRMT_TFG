/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Model.Epic;
import com.Sergio.EasyRMT.Repository.EpicRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Service.Converter.EpicConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


}
