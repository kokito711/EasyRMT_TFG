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

    //@Autowired


    @Transactional(readOnly = true)
    public List<EpicDom> getEpics(int projectId){
        List<Epic> epicList = epicRepository.findByProjectID(projectId);
        List<EpicDom> epicDomList = epicConverter.toDomain(epicList);
        return epicDomList;
    }

    @Transactional(readOnly = true)
    public EpicDom getEpic(int epicId){
        Epic epic = epicRepository.findOne(epicId);
        EpicDom epicDom = epicConverter.toDomain(epic);
        return epicDom;
    }
}
